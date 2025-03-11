package service;

import dataaccess.*;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;

public class DBServiceTests {
    UserDAO userDAO;
    AuthDAO authDAO;
    GameDAO gameDAO;
    DBService dbService;

    @BeforeEach
    public void setupEach() {
        userDAO = new MySQLUserDAO();
        authDAO = new MySQLAuthDAO();
        gameDAO = new MySQLGameDAO();
        try {
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
        dbService = new DBService(userDAO, authDAO, gameDAO);
    }

    @Test
    public void testClearApplication() {
        try {
            userDAO.createUser(new UserData("username", "password", "email"));
            authDAO.createAuth(new AuthData("a", "username"));
            gameDAO.createGame("g");
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
