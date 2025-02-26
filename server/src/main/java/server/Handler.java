package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.AuthDAO;
import dataaccess.UserDAO;
import service.RegisterRequest;
import service.RegisterResult;
import service.ServiceException;
import service.UserService;
import spark.*;

public class Handler {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public Handler(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
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
            sparkResponse.status(e.getCode());
            JsonObject returnObject = new JsonObject();
            returnObject.addProperty("message", e.getMessage());
            return returnObject;
        }
    }

}
