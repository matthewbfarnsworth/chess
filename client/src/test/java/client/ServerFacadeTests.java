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
        Assertions.assertThrows(ResponseException.class, () -> serverFacade.register("name", "a", "a"));
    }

}
