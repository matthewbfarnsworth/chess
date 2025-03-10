package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.List;

public class GameService {
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames(String authToken) throws ServiceException {
        try {
            if (authToken == null || authDAO.getAuth(authToken) == null) {
                throw new ServiceException("Error: unauthorized", 401);
            }

            List<ListedGame> listedGames = new ArrayList<>();
            for (GameData gameData : gameDAO.listGames()) {
                listedGames.add(new ListedGame(gameData.gameID(), gameData.whiteUsername(),
                        gameData.blackUsername(), gameData.gameName()));
            }
            return new ListGamesResult(listedGames);
        }
        catch (DataAccessException e) {
            throw new ServiceException("Error: ".concat(e.getMessage()), 500);
        }
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request) throws ServiceException {
        try {
            if (request.gameName() == null) {
                throw new ServiceException("Error: bad request", 400);
            }

            if (authToken == null || authDAO.getAuth(authToken) == null) {
                throw new ServiceException("Error: unauthorized", 401);
            }

            int gameID = gameDAO.createGame(request.gameName());
            return new CreateGameResult(gameID);
        }
        catch (DataAccessException e) {
            throw new ServiceException("Error: ".concat(e.getMessage()), 500);
        }
    }

    public void joinGame(String authToken, JoinGameRequest request) throws ServiceException {
        try {
            if (request.playerColor() == null ||
                    (!request.playerColor().equals("WHITE") && !request.playerColor().equals("BLACK"))) {
                throw new ServiceException("Error: bad request", 400);
            }

            if (authToken == null) {
                throw new ServiceException("Error: unauthorized", 401);
            }
            AuthData authData = authDAO.getAuth(authToken);
            if (authData == null) {
                throw new ServiceException("Error: unauthorized", 401);
            }

            GameData gameData = gameDAO.getGame(request.gameID());
            if (gameData == null) {
                throw new ServiceException("Error: bad request", 400);
            }

            if (request.playerColor().equals("WHITE")) {
                if (gameDAO.getGame(request.gameID()).whiteUsername() != null) {
                    throw new ServiceException("Error: already taken", 403);
                }
                gameDAO.updateGame(request.gameID(), authData.username(), GameDAO.Color.WHITE);
            }
            else {
                if (gameDAO.getGame(request.gameID()).blackUsername() != null) {
                    throw new ServiceException("Error: already taken", 403);
                }
                gameDAO.updateGame(request.gameID(), authData.username(), GameDAO.Color.BLACK);
            }
        }
        catch (DataAccessException e) {
            throw new ServiceException("Error: ".concat(e.getMessage()), 500);
        }
    }
}
