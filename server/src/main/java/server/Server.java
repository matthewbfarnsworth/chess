package server;

import dataaccess.*;
import spark.*;

public class Server {
    private final UserDAO userDAO = new MySQLUserDAO();
    private final AuthDAO authDAO = new MySQLAuthDAO();
    private final GameDAO gameDAO = new MySQLGameDAO();
    private final DBHandler dbHandler = new DBHandler(userDAO, authDAO, gameDAO);
    private final UserHandler userHandler = new UserHandler(userDAO, authDAO);
    private final GameHandler gameHandler = new GameHandler(authDAO, gameDAO);

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", dbHandler::handleClearApplication);
        Spark.post("/user", userHandler::handleRegister);
        Spark.post("/session", userHandler::handleLogin);
        Spark.delete("/session", userHandler::handleLogout);
        Spark.get("/game", gameHandler::handleListGames);
        Spark.post("/game", gameHandler::handleCreateGame);
        Spark.put("/game", gameHandler::handleJoinGame);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        //Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
