package server;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.ServiceException;
import websocket.WebSocketSerializer;
import websocket.commands.ConnectGameCommand;
import websocket.commands.UserGameCommand;
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
            }
        }
        catch (ServiceException e) {
            sendMessage(session, new ErrorServerMessage(e.getMessage()));
        }
        catch (Exception e) {
            sendMessage(session, new ErrorServerMessage("Error: " + e.getMessage()));
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

    private ChessGame getGame(int gameID) throws ServiceException {
        try {
            if (gameDAO.getGame(gameID) == null) {
                return null;
            }
            return gameDAO.getGame(gameID).game();
        }
        catch (DataAccessException e) {
            throw new ServiceException("Error: " + e.getMessage(), 500);
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
        if (getGame(gameID) == null) {
            throw new ServiceException("Error: Invalid game", 400);
        }

        if (!gameSessions.containsKey(gameID)) {
            gameSessions.put(gameID, new ConcurrentHashMap<>());
        }

        var gameSession = gameSessions.get(gameID);
        gameSession.put(username, session);
        sendMessage(session, new LoadGameServerMessage(getGame(gameID)));

        GameData gameData = getGameData(gameID);

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
}
