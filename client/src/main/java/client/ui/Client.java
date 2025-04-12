package client.ui;

import chess.ChessGame;
import chess.ChessPosition;
import client.net.ResponseException;
import client.net.ServerFacade;
import client.net.ServerMessageObserver;
import model.ListedGame;
import websocket.messages.ErrorServerMessage;
import websocket.messages.LoadGameServerMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Client implements ServerMessageObserver {
    private final String serverURL;
    private final ServerFacade facade;

    private State state = State.LOGGED_OUT;
    private boolean quit = false;
    private String authToken;
    private Map<Integer, Integer> gameIDMap;
    private ChessGame game;
    private int gameID;
    private boolean loadGameReceived = false;

    private enum State {
        LOGGED_OUT,
        LOGGED_IN,
        GAMEPLAY_WHITE,
        GAMEPLAY_BLACK,
        OBSERVE
    }

    public Client(String serverURL) {
        this.serverURL = serverURL;
        facade = new ServerFacade(serverURL, this);
    }

    private String getFirstString() {
        String line = new Scanner(System.in).next();
        var tokens = line.toLowerCase().split(" ");
        return (tokens.length > 0) ? tokens[0] : "";
    }

    private String getFullLine() {
        return new Scanner(System.in).nextLine();
    }

    public void run() {
        System.out.println("CS 240 CHESS");
        System.out.println("Running on " + serverURL + "\n");
        helpLoggedOut();

        while (!quit) {
            System.out.print("\n>>> ");
            String line = getFirstString();

            eval(line);
        }
    }

    private void eval(String input) {
        if (state == State.LOGGED_OUT) {
            switch (input) {
                case "login" -> login();
                case "register" -> register();
                case "quit" -> quitLoggedOut();
                default -> helpLoggedOut();
            }
        }
        else if (state == State.LOGGED_IN) {
            switch (input) {
                case "create" -> create();
                case "list" -> list();
                case "join" -> join();
                case "observe" -> observe();
                case "logout" -> logout();
                case "quit" -> quitLoggedIn();
                default -> helpLoggedIn();
            }
        }
        else if (state == State.GAMEPLAY_WHITE || state == State.GAMEPLAY_BLACK) {
             switch (input) {
                case "redraw" -> redrawGameplay();
                case "leave" -> leave();
                case "highlight" -> highlight();
                default -> helpGameplay();
            }
        }
        else if (state == State.OBSERVE) {
            switch (input) {
                case "redraw" -> redrawObserve();
                case "leave" -> leave();
                case "highlight" -> highlight();
                default -> helpObserve();
            }
        }
    }

    private void login() {
        System.out.println("Logging in:");

        System.out.print("\nEnter username >>> ");
        String username = getFirstString();

        System.out.print("\nEnter password >>> ");
        String password = getFirstString();

        System.out.println();
        try {
            var result = facade.login(username, password);
            authToken = result.authToken();
            state = State.LOGGED_IN;
            System.out.println("Successfully logged in as user " + result.username() +".");
            helpLoggedIn();
        }
        catch (ResponseException e) {
            if (e.getCode() == 401) {
                System.out.println("Invalid username or password. Failed to log in.");
            } else {
                System.out.println("An unexpected error occurred. Failed to log in.");
            }
        }
    }

    private void register() {
        System.out.println("Registering new user:");

        System.out.print("\nEnter username >>> ");
        String username = getFirstString();

        System.out.print("\nEnter password >>> ");
        String password = getFirstString();
        System.out.print("\nConfirm password >>> ");
        String confirmedPassword = getFirstString();
        if (!confirmedPassword.equals(password)) {
            System.out.println("Passwords do not match. Failed to register user.");
            return;
        }

        System.out.print("\nEnter email >>> ");
        String email = getFirstString();

        System.out.println();
        try {
            var result = facade.register(username, password, email);
            authToken = result.authToken();
            state = State.LOGGED_IN;
            System.out.println("Successfully registered user " + result.username() + ".");
            helpLoggedIn();
        }
        catch (ResponseException e) {
            switch (e.getCode()) {
                case 400 -> System.out.println("Invalid registration information. Failed to register user.");
                case 403 -> System.out.println("This username is already taken. Failed to register user.");
                default -> System.out.println("An unexpected error occurred. Failed to register user.");
            }
        }
    }

    private void quitLoggedOut() {
        quit = true;
    }

    private void helpLoggedOut() {
        System.out.println("""
                -> login
                -> register
                -> quit
                -> help""");
    }

    private void create() {
        System.out.println("Creating a new game:");

        System.out.print("\nEnter game name >>> ");
        String gameName = getFullLine();

        System.out.println();
        try {
            facade.createGame(authToken, gameName);
            System.out.println("Successfully created the new game " + gameName + ".");
        }
        catch (ResponseException e) {
            switch (e.getCode()) {
                case 400 -> System.out.println("Invalid game name. Failed to create game.");
                case 401 -> System.out.println("You are no longer logged in. Failed to create game.");
                default -> System.out.println("An unexpected error occurred. Failed to create game.");
            }
        }
    }

    private Map<Integer, Integer> mapNumbersToGameIDs(List<ListedGame> games) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < games.size(); i++) {
            map.put(i + 1, games.get(i).gameID());
        }
        return map;
    }

    private void list() {
        System.out.println("Listing games:\n");

        try {
            var games = facade.listGames(authToken).games();
            gameIDMap = mapNumbersToGameIDs(games);
            for (int i = 0; i < games.size(); i++) {
                var game = games.get(i);
                System.out.print(i + 1);
                System.out.print(": " + game.gameName() + " ");
                if (game.whiteUsername() != null) {
                    System.out.print("White: " + game.whiteUsername() + ". ");
                }
                else {
                    System.out.print("White: OPEN. ");
                }
                if (game.blackUsername() != null) {
                    System.out.print("Black: " + game.blackUsername() + ".");
                }
                else {
                    System.out.print("Black: OPEN.");
                }
                System.out.println();
            }
        }
        catch (ResponseException e) {
            if (e.getCode() == 401) {
                System.out.println("You are no longer logged in. Failed to list games.");
            } else {
                System.out.println("An unexpected error occurred. Failed to list games.");
            }
        }
    }

    private void join() {
        System.out.println("Joining a game:");

        System.out.print("\nEnter game number >>> ");
        String gameNumberString = getFirstString();
        int gameNumber;
        try {
            gameNumber = Integer.parseInt(gameNumberString);
            if (gameIDMap == null || gameIDMap.get(gameNumber) == null) {
                System.out.println("Invalid game number. Failed to join game.");
                return;
            }
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid game number. Failed to join game.");
            return;
        }

        System.out.print("\nSelect player color (white/black) >>> ");
        String color = getFirstString();
        if (!color.equals("white") && !color.equals("black")) {
            System.out.println("Invalid color. Failed to join game.");
            return;
        }

        System.out.println();
        try {
            color = color.equals("white") ? "WHITE" : "BLACK";
            state = color.equals("WHITE") ? State.GAMEPLAY_WHITE : State.GAMEPLAY_BLACK;
            facade.joinGame(authToken, color, gameIDMap.get(gameNumber));
            facade.connect(authToken, gameIDMap.get(gameNumber));
            waitForLoadGame();
            System.out.println("Successfully joined the game.");
            helpGameplay();
            gameID = gameIDMap.get(gameNumber);
        }
        catch (ResponseException e) {
            state = State.LOGGED_IN;
            switch (e.getCode()) {
                case 400 -> System.out.println("Invalid request. Failed to join game.");
                case 401 -> System.out.println("You are no longer logged in. Failed to join game.");
                case 403 -> System.out.println("Already taken. Failed to join game.");
                default -> System.out.println("An unexpected error occurred. Failed to join game.");
            }
        }
    }

    private void observe() {
        System.out.println("Observing a game:");

        System.out.print("\nEnter game number >>> ");
        String gameNumberString = getFirstString();
        int gameNumber;
        try {
            gameNumber = Integer.parseInt(gameNumberString);
            if (gameIDMap == null || gameIDMap.get(gameNumber) == null) {
                System.out.println("Invalid game number. Failed to join game as observer.");
                return;
            }
        }
        catch (NumberFormatException e) {
            System.out.println("Invalid game number. Failed to join game as observer.");
            return;
        }
        System.out.println();

        try {
            state = State.OBSERVE;
            facade.connect(authToken, gameIDMap.get(gameNumber));
            waitForLoadGame();
            System.out.println("Successfully joined the game as observer.");
            helpObserve();
            gameID = gameIDMap.get(gameNumber);
        }
        catch (ResponseException e) {
            state = State.LOGGED_IN;
            System.out.println("Failed to observe game.");
        }
    }

    private void logout() {
        System.out.println("Logging out:\n");

        try {
            facade.logout(authToken);
            authToken = null;
            state = State.LOGGED_OUT;
            System.out.println("Successfully logged out.");
            helpLoggedOut();
        }
        catch (ResponseException e) {
            if (e.getCode() == 401) {
                System.out.println("Invalid username or password. Failed to log out.");
            } else {
                System.out.println("An unexpected error occurred. Failed to log out.");
            }
        }
    }

    private void quitLoggedIn() {
        try {
            facade.logout(authToken);
        }
        catch (ResponseException e) {
            throw new RuntimeException(e.getMessage());
        }
        quit = true;
    }

    private void helpLoggedIn() {
        System.out.println("""
                -> create
                -> list
                -> join
                -> observe
                -> logout
                -> quit
                -> help""");
    }

    private ChessPosition stringToChessPosition(String input) throws InvalidPositionException {
        if (input.length() != 2) {
            throw new InvalidPositionException("Invalid position.");
        }
        char firstChar = input.charAt(0);
        if (firstChar < 'a' || firstChar > 'h') {
            throw new InvalidPositionException("Invalid position.");
        }
        char secondChar = input.charAt(1);
        if (secondChar < '1' || secondChar > '8') {
            throw new InvalidPositionException("Invalid position.");
        }
        return new ChessPosition(Character.getNumericValue(secondChar), firstChar - 'a' + 1);
    }

    private void redrawGameplay() {
        new ChessBoard().printBoard(game, state.equals(State.GAMEPLAY_WHITE));
    }

    private void leave() {
        try {
            facade.leave(authToken, gameID);
            game = null;
            gameID = 0;
            state = State.LOGGED_IN;
            helpLoggedIn();
        }
        catch (ResponseException e) {
            System.out.println("Failed to leave game.");
        }
    }

    private void highlight() {
        System.out.print("\nEnter position to highlight legal moves on >>> ");
        try {
            ChessPosition highlightPosition = stringToChessPosition(getFirstString());
            new ChessBoard().printBoard(new ChessGame(), highlightPosition, !state.equals(State.GAMEPLAY_BLACK));
        }
        catch (InvalidPositionException e) {
            System.out.println(e.getMessage());
        }
    }

    private void helpGameplay() {
        System.out.println("""
                -> redraw
                -> leave
                -> move
                -> resign
                -> highlight
                -> help
                """);
    }

    private void redrawObserve() {
        new ChessBoard().printBoard(game, true);
    }

    private void helpObserve() {
        System.out.println("""
                -> redraw
                -> leave
                -> highlight
                -> help
                """);
    }

    private void waitForLoadGame() throws ResponseException {
        loadGameReceived = false;
        int totalWaitTime = 0;
        while (!loadGameReceived && totalWaitTime < 1000) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new ResponseException(e.getMessage(), 500);
            }
            totalWaitTime += 100;
        }
        if (!loadGameReceived) {
            throw new ResponseException("Error: Request timed out", 408);
        }
    }

    private void loadGame(ChessGame game) {
        this.game = game;
        new ChessBoard().printBoard(game, !state.equals(State.GAMEPLAY_BLACK));
        loadGameReceived = true;
    }

    private void error(String errorMessage) {
        System.out.println("\n" + errorMessage);
    }

    private void notification(String message) {
        System.out.println("\n" + message);
    }

    @Override
    public void notify(ServerMessage message) {
        switch (message.getServerMessageType()) {
            case LOAD_GAME -> loadGame(((LoadGameServerMessage) message).getGame());
            case ERROR -> error(((ErrorServerMessage) message).getErrorMessage());
            case NOTIFICATION -> notification(((NotificationMessage) message).getMessage());
        }
    }
}
