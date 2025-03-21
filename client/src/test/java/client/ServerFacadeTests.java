package client;

import client.net.ResponseException;
import client.net.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;
import service.*;


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

}
