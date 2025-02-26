package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import service.*;
import spark.*;

public class UserHandler {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserHandler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    private Object jsonErrorFromServiceException(ServiceException e, Response sparkResponse) {
        sparkResponse.status(e.getCode());
        JsonObject returnObject = new JsonObject();
        returnObject.addProperty("message", e.getMessage());
        return returnObject;
    }

    public Object handleRegister(Request sparkRequest, Response sparkResponse) {
        RegisterRequest registerRequest = new Gson().fromJson(sparkRequest.body(), RegisterRequest.class);
        try {
            UserService userService = new UserService(userDAO, authDAO);
            RegisterResult registerResult = userService.register(registerRequest);
            sparkResponse.status(200);
            return new Gson().toJson(registerResult);
        }
        catch (ServiceException e) {
            return jsonErrorFromServiceException(e, sparkResponse);
        }
    }

    public Object handleLogin(Request sparkRequest, Response sparkResponse) {
        LoginRequest loginRequest = new Gson().fromJson(sparkRequest.body(), LoginRequest.class);
        try {
            UserService userService = new UserService(userDAO, authDAO);
            LoginResult loginResult = userService.login(loginRequest);
            sparkResponse.status(200);
            return new Gson().toJson(loginResult);
        }
        catch (ServiceException e) {
            return jsonErrorFromServiceException(e, sparkResponse);
        }
    }

    public Object handleLogout(Request sparkRequest, Response sparkResponse) {
        String authToken = sparkRequest.headers("authorization");
        try {
            UserService userService = new UserService(userDAO, authDAO);
            userService.logout(authToken);
            sparkResponse.status(200);
            return new JsonObject();
        }
        catch (ServiceException e) {
            return jsonErrorFromServiceException(e, sparkResponse);
        }
    }

}
