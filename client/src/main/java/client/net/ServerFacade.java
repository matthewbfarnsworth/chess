package client.net;

import service.*;

public class ServerFacade {
    private final String serverURL;
    private final ClientCommunicator communicator = new ClientCommunicator();

    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
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

    public CreateGameResult createGame(String authToken, String gameName) throws ResponseException {
        CreateGameRequest request = new CreateGameRequest(gameName);
        return communicator.makeRequest(serverURL, "POST", "/game", request, authToken, CreateGameResult.class);
    }
}
