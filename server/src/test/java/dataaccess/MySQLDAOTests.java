package dataaccess;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class MySQLDAOTests {
    MySQLUserDAO userDAO = new MySQLUserDAO();
    MySQLAuthDAO authDAO = new MySQLAuthDAO();

    @BeforeAll
    public static void dropDatabase() {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DROP DATABASE chess";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    public static void testCreateDatabase() {
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
    }

    @BeforeAll
    public static void testConfigureDatabase() {
        Assertions.assertDoesNotThrow(MySQLDAO::new);
    }

    @BeforeEach
    public void clearTables() {
        try {
            userDAO.clear();
            authDAO.clear();
            //gameDAO.clear();
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testUserClear() {
        try {
            userDAO.createUser(new UserData("name", "password", "email@gmail.com"));
            userDAO.clear();
            Assertions.assertNull(userDAO.getUser("name"));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testValidGetUser() {
        try {
            UserData userData = new UserData("name", "password", "email@gmail.com");
            userDAO.createUser(userData);
            Assertions.assertEquals(userData, userDAO.getUser("name"));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testInvalidGetUser() {
        try {
            Assertions.assertNull(userDAO.getUser("name"));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testValidCreateUser() {
        try {
            UserData userData = new UserData("name", "password", "email@gmail.com");
            userDAO.createUser(userData);
            Assertions.assertEquals(userData, userDAO.getUser("name"));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testInvalidCreateUser() {
        try {
            UserData userData = new UserData("name", "password", "email@gmail.com");
            userDAO.createUser(userData);
            UserData newUserData = new UserData("name", "newPassword", "newEmail@gmail.com");
            Assertions.assertThrows(DataAccessException.class, () ->  userDAO.createUser(newUserData));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testAuthClear() {
        try {
            authDAO.createAuth(new AuthData("a", "name"));
            authDAO.clear();
            Assertions.assertNull(authDAO.getAuth("a"));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testValidGetAuth() {
        try {
            AuthData authData = new AuthData("a", "name");
            authDAO.createAuth(authData);
            Assertions.assertEquals(authData, authDAO.getAuth("a"));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testInvalidGetAuth() {
        try {
            Assertions.assertNull(authDAO.getAuth("a"));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testValidCreateAuth() {
        try {
            AuthData authData = new AuthData("a", "name");
            authDAO.createAuth(authData);
            Assertions.assertEquals(authData, authDAO.getAuth("a"));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testInvalidCreateAuth() {
        try {
            AuthData authData1 = new AuthData("a", "name1");
            AuthData authData2 = new AuthData("a", "name2");
            authDAO.createAuth(authData1);
            Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth(authData2));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testValidDeleteAuth() {
        try {
            AuthData authData = new AuthData("a", "name");
            authDAO.createAuth(authData);
            Assertions.assertNotNull(authDAO.getAuth("a"));
            authDAO.deleteAuth(authData);
            Assertions.assertNull(authDAO.getAuth("a"));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testEmptyDeleteAuth() {
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth(new AuthData("a", "name")));
    }
}
