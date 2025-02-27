package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
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

            int gameID = gameDAO.generateGameID();
            gameDAO.createGame(new GameData(gameID, null, null, request.gameName(), new ChessGame()));
            return new CreateGameResult(gameID);
        }
        catch (DataAccessException e) {
            throw new ServiceException("Error: ".concat(e.getMessage()), 500);
        }
    }
}
