package client.net;

import model.*;

public class ServerFacade {
    private final String serverURL;
    private final ServerMessageObserver serverMessageObserver;
    private final HttpCommunicator communicator = new HttpCommunicator();

    public ServerFacade(String serverURL, ServerMessageObserver serverMessageObserver) {
        this.serverURL = serverURL;
        this.serverMessageObserver = serverMessageObserver;
    }

    public void clear() throws ResponseException {
        communicator.makeRequest(serverURL, "DELETE", "/db", null, null, null);
    }

    public RegisterResult register(String username, String password, String email) throws ResponseException {
        RegisterRequest request = new RegisterRequest(username, password, email);
        return communicator.makeRequest(serverURL, "POST", "/user", request, null,
                RegisterResult.class);
    }

    public LoginResult login(String username, String password) throws ResponseException {
        LoginRequest request = new LoginRequest(username, password);
        return communicator.makeRequest(serverURL, "POST", "/session", request, null, LoginResult.class);
    }

    public void logout(String authToken) throws ResponseException {
        communicator.makeRequest(serverURL, "DELETE", "/session", null, authToken, null);
    }

    public ListGamesResult listGames(String authToken) throws ResponseException {
        return communicator.makeRequest(serverURL, "GET", "/game", null, authToken, ListGamesResult.class);
    }

    public CreateGameResult createGame(String authToken, String gameName) throws ResponseException {
        CreateGameRequest request = new CreateGameRequest(gameName);
        return communicator.makeRequest(serverURL, "POST", "/game", request, authToken, CreateGameResult.class);
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws ResponseException {
        JoinGameRequest request = new JoinGameRequest(playerColor, gameID);
        communicator.makeRequest(serverURL, "PUT", "/game", request, authToken, null);
    }
}
