package client;

import client.net.ResponseException;
import client.net.ServerFacade;
import model.CreateGameResult;
import model.ListedGame;
import model.LoginResult;
import model.RegisterResult;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.List;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade("http://localhost:" + port);
    }

    @BeforeEach
    public void clearFacade() {
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        serverFacade.clear();
        server.stop();
    }


    @Test
    public void testServerFacadeValidRegister() {
        RegisterResult result = serverFacade.register("name", "password", "email@gmail.com");
        Assertions.assertEquals("name", result.username());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertTrue(result.authToken().length() > 10);
    }

    @Test
    public void testServerFacadeInvalidRegister() {
        serverFacade.register("name", "password", "email@gmail.com");
        ResponseException e = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.register("name", "a", "a"));
        Assertions.assertEquals(403, e.getCode());
    }

    @Test
    public void testServerFacadeValidLogin() {
        serverFacade.register("name", "password", "email@gmail.com");
        LoginResult result = serverFacade.login("name", "password");
        Assertions.assertEquals("name", result.username());
        Assertions.assertNotNull(result.authToken());
        Assertions.assertTrue(result.authToken().length() > 10);
    }

    @Test
    public void testServerFacadeInvalidLogin() {
        serverFacade.register("name", "password", "email@gmail.com");
        ResponseException e = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.login("name", "badPassword"));
        Assertions.assertEquals(401, e.getCode());
    }

    @Test
    public void testServerFacadeValidLogout() {
        RegisterResult registerResult = serverFacade.register("name", "password", "email@gmail.com");
        String authToken = registerResult.authToken();
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(authToken));
    }

    @Test
    public void testServerFacadeInvalidLogout() {
        serverFacade.register("name", "password", "email@gmail.com");
        ResponseException e = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.logout("a"));
        Assertions.assertEquals(401, e.getCode());
    }

    @Test
    public void testServerFacadeValidListGames() {
        RegisterResult registerResult = serverFacade.register("name", "password", "email@gmail.com");
        String authToken = registerResult.authToken();
        int gameID1 = serverFacade.createGame(authToken, "game1").gameID();
        int gameID2 = serverFacade.createGame(authToken, "game2").gameID();
        int gameID3 = serverFacade.createGame(authToken, "game3").gameID();
        List<ListedGame> result = serverFacade.listGames(authToken).games();
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(new ListedGame(gameID1, null, null, "game1"), result.get(0));
        Assertions.assertEquals(new ListedGame(gameID2, null, null, "game2"), result.get(1));
        Assertions.assertEquals(new ListedGame(gameID3, null, null, "game3"), result.get(2));
    }

    @Test
    public void testServerFacadeInvalidListGames() {
        ResponseException e = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.listGames("a"));
        Assertions.assertEquals(401, e.getCode());
    }

    @Test
    public void testServerFacadeValidCreateGame() {
        RegisterResult registerResult = serverFacade.register("name", "password", "email@gmail.com");
        String authToken = registerResult.authToken();
        CreateGameResult result = serverFacade.createGame(authToken, "game");
        Assertions.assertTrue(result.gameID() > 0);
    }

    @Test
    public void testServerFacadeInvalidCreateGame() {
        ResponseException e = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.createGame("a", "game"));
        Assertions.assertEquals(401, e.getCode());
    }

    @Test
    public void testServerFacadeValidJoinGame() {
        RegisterResult registerResult = serverFacade.register("name", "password", "email@gmail.com");
        String authToken = registerResult.authToken();
        int gameID = serverFacade.createGame(authToken, "game").gameID();
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(authToken, "WHITE", gameID));
        List<ListedGame> games = serverFacade.listGames(authToken).games();
        Assertions.assertEquals(new ListedGame(gameID, "name", null, "game"), games.get(0));
    }

    @Test
    public void testServerFacadeInvalidJoinGame() {
        RegisterResult registerResult1 = serverFacade.register("name", "password", "email@gmail.com");
        String authToken1 = registerResult1.authToken();
        int gameID = serverFacade.createGame(authToken1, "game").gameID();
        serverFacade.joinGame(authToken1, "WHITE", gameID);
        serverFacade.logout(authToken1);
        RegisterResult registerResult2 = serverFacade.register("name2", "password2", "email2@gmail.com");
        String authToken2 = registerResult2.authToken();
        ResponseException e = Assertions.assertThrows(ResponseException.class, () ->
                serverFacade.joinGame(authToken2, "WHITE", gameID));
        Assertions.assertEquals(403, e.getCode());
    }

}
