package client.net;

import chess.ChessMove;
import model.*;
import websocket.WebSocketSerializer;
import websocket.commands.ConnectGameCommand;
import websocket.commands.LeaveGameCommand;
import websocket.commands.MakeMoveGameCommand;
import websocket.commands.ResignGameCommand;

public class ServerFacade {
    private final String serverURL;
    private final HttpCommunicator httpCommunicator = new HttpCommunicator();
    private final WebSocketCommunicator webSocketCommunicator;

    public ServerFacade(String serverURL, ServerMessageObserver serverMessageObserver) {
        this.serverURL = serverURL;
        webSocketCommunicator = new WebSocketCommunicator(serverURL, serverMessageObserver);
    }

    public void clear() throws ResponseException {
        httpCommunicator.makeRequest(serverURL, "DELETE", "/db", null, null, null);
    }

    public RegisterResult register(String username, String password, String email) throws ResponseException {
        RegisterRequest request = new RegisterRequest(username, password, email);
        return httpCommunicator.makeRequest(serverURL, "POST", "/user", request, null,
                RegisterResult.class);
    }

    public LoginResult login(String username, String password) throws ResponseException {
        LoginRequest request = new LoginRequest(username, password);
        return httpCommunicator.makeRequest(serverURL, "POST", "/session", request, null, LoginResult.class);
    }

    public void logout(String authToken) throws ResponseException {
        httpCommunicator.makeRequest(serverURL, "DELETE", "/session", null, authToken, null);
    }

    public ListGamesResult listGames(String authToken) throws ResponseException {
        return httpCommunicator.makeRequest(serverURL, "GET", "/game", null, authToken, ListGamesResult.class);
    }

    public CreateGameResult createGame(String authToken, String gameName) throws ResponseException {
        CreateGameRequest request = new CreateGameRequest(gameName);
        return httpCommunicator.makeRequest(serverURL, "POST", "/game", request, authToken, CreateGameResult.class);
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws ResponseException {
        JoinGameRequest request = new JoinGameRequest(playerColor, gameID);
        httpCommunicator.makeRequest(serverURL, "PUT", "/game", request, authToken, null);
    }

    public void connect(String authToken, int gameID) throws ResponseException {
        ConnectGameCommand command = new ConnectGameCommand(authToken, gameID);
        webSocketCommunicator.send(WebSocketSerializer.serialize(command));
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException {
        MakeMoveGameCommand command = new MakeMoveGameCommand(authToken, gameID, move);
        webSocketCommunicator.send(WebSocketSerializer.serialize(command));
    }

    public void leave(String authToken, int gameID) throws ResponseException {
        LeaveGameCommand command = new LeaveGameCommand(authToken, gameID);
        webSocketCommunicator.send(WebSocketSerializer.serialize(command));
    }

    public void resign(String authToken, int gameID) throws ResponseException {
        ResignGameCommand command = new ResignGameCommand(authToken, gameID);
        webSocketCommunicator.send(WebSocketSerializer.serialize(command));
    }

}
