package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import service.*;
import spark.*;

public class GameHandler {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameHandler(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public Object handleListGames(Request sparkRequest, Response sparkResponse) {
        String authToken = sparkRequest.headers("authorization");
        try {
            GameService gameService = new GameService(authDAO, gameDAO);
            ListGamesResult listGamesResult = gameService.listGames(authToken);
            sparkResponse.status(200);
            return new Gson().toJson(listGamesResult);
        }
        catch (ServiceException e) {
            sparkResponse.status(e.getCode());
            JsonObject returnObject = new JsonObject();
            returnObject.addProperty("message", e.getMessage());
            return returnObject;
        }
    }

    public Object handleCreateGame(Request sparkRequest, Response sparkResponse) {
        String authToken = sparkRequest.headers("authorization");
        CreateGameRequest createGameRequest = new Gson().fromJson(sparkRequest.body(), CreateGameRequest.class);
        try {
            GameService gameService = new GameService(authDAO, gameDAO);
            CreateGameResult createGameResult = gameService.createGame(authToken, createGameRequest);
            sparkResponse.status(200);
            return new Gson().toJson(createGameResult);
        }
        catch (ServiceException e) {
            sparkResponse.status(e.getCode());
            JsonObject returnObject = new JsonObject();
            returnObject.addProperty("message", e.getMessage());
            return returnObject;
        }
    }
}
