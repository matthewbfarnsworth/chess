package client.net;

import service.LoginRequest;
import service.LoginResult;
import service.RegisterRequest;
import service.RegisterResult;

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
}
