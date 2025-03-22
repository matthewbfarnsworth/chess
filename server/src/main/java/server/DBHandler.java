package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.DBService;
import model.ErrorResult;
import service.ServiceException;
import spark.*;

public class DBHandler {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public DBHandler(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Object handleClearApplication(Request sparkRequest, Response sparkResponse) {
        try {
            DBService dbService = new DBService(userDAO, authDAO, gameDAO);
            dbService.clearApplication();
            sparkResponse.status(200);
            return new JsonObject();
        }
        catch (ServiceException e) {
            sparkResponse.status(e.getCode());
            return new Gson().toJson(new ErrorResult(e.getMessage()));
        }
    }
}
