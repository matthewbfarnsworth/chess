package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class MySQLDAOTests {
    MySQLUserDAO userDAO = new MySQLUserDAO();
    MySQLAuthDAO authDAO = new MySQLAuthDAO();
    MySQLGameDAO gameDAO = new MySQLGameDAO();

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
            gameDAO.clear();
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
            UserData userData1 = new UserData("name1", "password", "email@gmail.com");
            userDAO.createUser(userData1);
            UserData userData2 = new UserData("name2", "password2", "email2@gmail.com");
            userDAO.createUser(userData2);
            Assertions.assertEquals(userData1, userDAO.getUser("name1"));
            Assertions.assertEquals(userData2, userDAO.getUser("name2"));
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
            AuthData authData1 = new AuthData("a", "name");
            authDAO.createAuth(authData1);
            AuthData authData2 = new AuthData("b", "name2");
            authDAO.createAuth(authData2);
            Assertions.assertEquals(authData1, authDAO.getAuth("a"));
            Assertions.assertEquals(authData2, authDAO.getAuth("b"));
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

    @Test
    public void testGameClear() {
        try {
            int gameID = gameDAO.createGame("game");
            gameDAO.clear();
            Assertions.assertNull(gameDAO.getGame(gameID));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testValidGetGame() {
        try {
            int gameID = gameDAO.createGame("game");
            GameData gameData = new GameData(gameID, null, null, "game", new ChessGame());
            Assertions.assertEquals(gameData, gameDAO.getGame(gameID));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testInvalidGetGame() {
        try {
            Assertions.assertNull(gameDAO.getGame(1));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testValidCreateGame() {
        try {
            int gameID1 = gameDAO.createGame("game");
            GameData gameData1 = new GameData(gameID1, null, null, "game", new ChessGame());
            int gameID2 = gameDAO.createGame("game2");
            GameData gameData2 = new GameData(gameID2, null, null, "game2", new ChessGame());
            Assertions.assertEquals(gameData1, gameDAO.getGame(gameID1));
            Assertions.assertEquals(gameData2, gameDAO.getGame(gameID2));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testInvalidCreateGame() {
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame(null));
    }

    @Test
    public void testValidListGames() {
        try {
            int gameID1 = gameDAO.createGame("game1");
            int gameID2 = gameDAO.createGame("game2");
            List<GameData> listedGames = gameDAO.listGames();
            GameData game1 = new GameData(gameID1, null, null, "game1", new ChessGame());
            GameData game2 = new GameData(gameID2, null, null, "game2", new ChessGame());
            List<GameData> expectedGames = new ArrayList<>();
            expectedGames.add(game1);
            expectedGames.add(game2);
            Assertions.assertEquals(expectedGames, listedGames);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testEmptyListGames() {
        try {
            List<GameData> listedGames = gameDAO.listGames();
            List<GameData> expectedGames = new ArrayList<>();
            Assertions.assertEquals(expectedGames, listedGames);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testValidUpdateGame() {
        try {
            int gameID = gameDAO.createGame("game");
            gameDAO.updateGame(gameID, "name", GameDAO.Color.WHITE);
            GameData expected = new GameData(gameID, "name", null, "game", new ChessGame());
            Assertions.assertEquals(expected, gameDAO.getGame(gameID));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testInvalidUpdateGame() {
        try {
            int gameID = gameDAO.createGame("game");
            gameDAO.updateGame(gameID+1, "name2", GameDAO.Color.WHITE);
            GameData expected = new GameData(gameID, null, null, "game", new ChessGame());
            Assertions.assertEquals(expected, gameDAO.getGame(gameID));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testValidReplaceGame() {
        try {
            int gameID = gameDAO.createGame("game");
            ChessMove move = new ChessMove(new ChessPosition(2, 2), new ChessPosition(4, 2), null);
            ChessGame newGame = new ChessGame();
            newGame.makeMove(move);
            gameDAO.replaceGame(gameID, newGame);
            Assertions.assertEquals(newGame, gameDAO.getGame(gameID).game());
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Test
    public void testInvalidReplaceGame() {
        try {
            int gameID = gameDAO.createGame("game");
            ChessMove move = new ChessMove(new ChessPosition(2, 2), new ChessPosition(4, 2), null);
            ChessGame newGame = new ChessGame();
            newGame.makeMove(move);
            gameDAO.replaceGame(gameID + 1, newGame);
            Assertions.assertEquals(new ChessGame(), gameDAO.getGame(gameID).game());
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
