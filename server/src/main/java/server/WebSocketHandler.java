package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.ServiceException;
import websocket.WebSocketSerializer;
import websocket.commands.*;
import websocket.messages.ErrorServerMessage;
import websocket.messages.LoadGameServerMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class WebSocketHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    private final ConcurrentHashMap<Integer, ConcurrentHashMap<String, Session>> gameSessions =
            new ConcurrentHashMap<>();

    public WebSocketHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            UserGameCommand command = WebSocketSerializer.deserialize(message, UserGameCommand.class);

            String username = getUsername(command.getAuthToken());

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, (ConnectGameCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveGameCommand) command);
                case LEAVE -> leave(session, username, (LeaveGameCommand) command);
                case RESIGN -> resign(session, username, (ResignGameCommand) command);
            }
        }
        catch (ServiceException e) {
            sendMessage(session, new ErrorServerMessage(e.getMessage()));
        }
        catch (Exception e) {
            sendMessage(session, new ErrorServerMessage("Error: " + e.getMessage()));
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int code, String message) {
        for (int gameID : gameSessions.keySet()) {
            var gameSession = gameSessions.get(gameID);
            for (String username : gameSession.keySet()) {
                if (gameSession.get(username) == session) {
                    gameSession.remove(username);
                    if (gameSession.isEmpty()) {
                        gameSessions.remove(gameID);
                    }
                    return;
                }
            }
        }
    }

    private String getUsername(String authToken) throws ServiceException {
        try {
            if (authToken == null) {
                throw new ServiceException("Error: unauthorized", 401);
            }

            var authData = authDAO.getAuth(authToken);

            if (authData == null) {
                throw new ServiceException("Error: unauthorized", 401);
            }

            return authData.username();
        }
        catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), 500);
        }
    }

    private GameData getGameData(int gameID) throws ServiceException {
        try {
            return gameDAO.getGame(gameID);
        }
        catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage(), 500);
        }
    }

    private void setGameOver(int gameID) throws ServiceException {
        try {
            gameDAO.setGameOver(gameID, true);
        }
        catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage(), 500);
        }
    }

    private void sendMessage(Session session, ServerMessage message) throws ServiceException {
        try {
            session.getRemote().sendString(WebSocketSerializer.serialize(message));
        }
        catch (IOException e) {
            throw new ServiceException("Error: " + e.getMessage(), 500);
        }
    }

    private void notify(String excludeUsername, int gameID, String message) throws ServiceException {
        var gameSession = gameSessions.get(gameID);
        for (String key : gameSession.keySet()) {
            if (!key.equals(excludeUsername)) {
                sendMessage(gameSession.get(key), new NotificationMessage(message));
            }
        }
    }

    private void notify(int gameID, String message) throws ServiceException {
        notify(null, gameID, message);
    }

    private void connect(Session session, String username, ConnectGameCommand command) throws ServiceException {
        int gameID = command.getGameID();
        GameData gameData = getGameData(gameID);

        if (gameData == null) {
            throw new ServiceException("Error: Invalid game", 400);
        }

        if (!gameSessions.containsKey(gameID)) {
            gameSessions.put(gameID, new ConcurrentHashMap<>());
        }

        var gameSession = gameSessions.get(gameID);
        gameSession.put(username, session);
        sendMessage(session, new LoadGameServerMessage(gameData.game()));

        if (gameData.whiteUsername().equals(username)) {
            notify(username, gameID, username + " entered the game as white");
        }
        else if (gameData.blackUsername().equals(username)) {
            notify(username, gameID, username + " entered the game as black");
        }
        else {
            notify(username, gameID, username + " entered the game as an observer");
        }
    }

    private void makeMove(Session session, String username, MakeMoveGameCommand command) throws ServiceException {
        int gameID = command.getGameID();
        GameData gameData = getGameData(gameID);

        if (gameData == null) {
            throw new ServiceException("Error: Invalid game", 400);
        }

        if (gameData.gameOver()) {
            throw new ServiceException("Error: Game is over", 403);
        }

        ChessGame.TeamColor teamTurn = gameData.game().getTeamTurn();
        if (!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())) {
            throw new ServiceException("Error: Cannot make move as observer", 403);
        }
        if ((teamTurn == ChessGame.TeamColor.WHITE && !username.equals(gameData.whiteUsername()))
                || (teamTurn == ChessGame.TeamColor.BLACK && !username.equals(gameData.blackUsername()))) {
            throw new ServiceException("Error: Can not make move on wrong team", 403);
        }

        ChessGame updatedGame = gameData.game();
        try {
            updatedGame.makeMove(command.getMove());
        }
        catch (InvalidMoveException e) {
            throw new ServiceException("Error: " + e.getMessage(), 403);
        }
        try {
            gameDAO.replaceGame(gameID, updatedGame);
        }
        catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage(), 500);
        }

        var gameSession = gameSessions.get(gameID);
        for (String key : gameSession.keySet()) {
            sendMessage(gameSession.get(key), new LoadGameServerMessage(updatedGame));
        }

        ChessMove move = command.getMove();
        if (move.getPromotionPiece() == null) {
            notify(username, gameID, username + " moved " + move.getStartPosition() + " to " + move.getEndPosition());
        }
        else if (move.getPromotionPiece() == ChessPiece.PieceType.QUEEN) {
            notify(username, gameID, username + " moved " + move.getStartPosition() + " to " + move.getEndPosition() +
                    " and promoted to queen");
        }
        else if (move.getPromotionPiece() == ChessPiece.PieceType.BISHOP) {
            notify(username, gameID, username + " moved " + move.getStartPosition() + " to " + move.getEndPosition() +
                    " and promoted to bishop");
        }
        else if (move.getPromotionPiece() == ChessPiece.PieceType.KNIGHT) {
            notify(username, gameID, username + " moved " + move.getStartPosition() + " to " + move.getEndPosition() +
                    " and promoted to knight");
        }
        else if (move.getPromotionPiece() == ChessPiece.PieceType.ROOK) {
            notify(username, gameID, username + " moved " + move.getStartPosition() + " to " + move.getEndPosition() +
                    " and promoted to rook");
        }

        ChessGame.TeamColor nextTeamTurn = updatedGame.getTeamTurn();
        String nextTeamName = nextTeamTurn == ChessGame.TeamColor.WHITE ? "white" : "black";
        if (updatedGame.isInStalemate(nextTeamTurn)) {
            setGameOver(gameID);
            notify(gameID, nextTeamName + " is in stalemate");
        }
        else if (updatedGame.isInCheckmate(nextTeamTurn)) {
            setGameOver(gameID);
            notify(gameID, nextTeamName + " is in checkmate");
        }
        else if (updatedGame.isInCheck(nextTeamTurn)) {
            notify(gameID, nextTeamName + " is in check");
        }

    }

    private void leave(Session session, String username, LeaveGameCommand command) throws ServiceException {
        GameData gameData = getGameData(command.getGameID());
        try {
            if (username.equals(gameData.whiteUsername())) {
                gameDAO.updateGame(command.getGameID(), null, GameDAO.Color.WHITE);
            }
            else if (username.equals(gameData.blackUsername())) {
                gameDAO.updateGame(command.getGameID(), null, GameDAO.Color.BLACK);
            }
        }
        catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), 500);
        }
        notify(username, command.getGameID(), username + " has left the game");
        session.close();
    }

    private void resign(Session session, String username, ResignGameCommand command) throws ServiceException {
        GameData gameData = getGameData(command.getGameID());
        if (!username.equals(gameData.whiteUsername()) && !username.equals(gameData.blackUsername())) {
            throw new ServiceException("Error: Cannot resign as observer", 403);
        }

        if (gameData.gameOver()) {
            throw new ServiceException("Error: Game is over", 403);
        }

        setGameOver(command.getGameID());
        notify(command.getGameID(), username + " has resigned");
    }
}
