package service;

import dataaccess.*;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

public class DBServiceTests {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    DBService dbService;

    @BeforeEach
    public void setupEach() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        dbService = new DBService(userDAO, authDAO, gameDAO);
    }

    @Test
    public void testClearApplication() {
        try {
            userDAO.createUser(new UserData("username", "password", "email"));
            authDAO.createAuth(new AuthData("a", "username"));
            gameDAO.createGame(new GameData(1, null, null, "g", null));
            Assertions.assertNotNull(userDAO.getUser("username"));
            Assertions.assertNotNull(authDAO.getAuth("a"));
            Assertions.assertNotNull(gameDAO.getGame(1));
            dbService.clearApplication();
            Assertions.assertNull(userDAO.getUser("username"));
            Assertions.assertNull(authDAO.getAuth("a"));
            Assertions.assertNull(gameDAO.getGame(1));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
