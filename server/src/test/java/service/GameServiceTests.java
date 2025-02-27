package service;

import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameServiceTests {
    AuthDAO authDAO;
    GameDAO gameDAO;
    GameService gameService;

    @BeforeEach
    public void setupEach() {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        gameService = new GameService(authDAO, gameDAO);
    }

    @Test
    public void testValidEmptyListGames() {
        try {
            authDAO.createAuth(new AuthData("a", "username"));
            ListGamesResult result = gameService.listGames("a");
            Assertions.assertNotNull(result.games());
            Assertions.assertEquals(0, result.games().size());
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testValidThreeListGames() {
        try {
            authDAO.createAuth(new AuthData("a", "username"));
            gameService.createGame("a", new CreateGameRequest("game1"));
            gameService.createGame("a", new CreateGameRequest("game2"));
            gameService.createGame("a", new CreateGameRequest("game3"));
            ListGamesResult result = gameService.listGames("a");
            Assertions.assertEquals(3, result.games().size());
            Assertions.assertEquals(new ListedGame(1, null, null, "game1"), result.games().get(0));
            Assertions.assertEquals(new ListedGame(2, null, null, "game2"), result.games().get(1));
            Assertions.assertEquals(new ListedGame(3, null, null, "game3"), result.games().get(2));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInvalidAuthDataListGames() {
        ServiceException exception = Assertions.assertThrows(ServiceException.class, () ->
                gameService.listGames("a"));
        Assertions.assertEquals(401, exception.getCode());
    }

    @Test
    public void testValidCreateGameRequest() {
        try {
            authDAO.createAuth(new AuthData("a", "username"));
            CreateGameRequest request = new CreateGameRequest("myGame");
            CreateGameResult result = gameService.createGame("a", request);
            Assertions.assertNotNull(gameDAO.getGame(result.gameID()));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInvalidAuthTokenCreateGameRequest() {
        CreateGameRequest request = new CreateGameRequest("myGame");
        ServiceException exception = Assertions.assertThrows(ServiceException.class, () ->
                gameService.createGame("a", request));
        Assertions.assertEquals(401, exception.getCode());
    }

    @Test
    public void testInvalidCreateGameRequest() {
        CreateGameRequest request = new CreateGameRequest(null);
        ServiceException exception = Assertions.assertThrows(ServiceException.class, () ->
                gameService.createGame("a", request));
        Assertions.assertEquals(400, exception.getCode());
    }
}
