package client.ui;

import client.net.ResponseException;
import client.net.ServerFacade;

import java.util.Scanner;

public class Client {
    private final String serverURL;
    private final ServerFacade facade;
    private State state = State.LOGGED_OUT;
    private boolean quit = false;
    private String authToken;

    private enum State {
        LOGGED_OUT,
        LOGGED_IN
    }

    public Client(String serverURL) {
        this.serverURL = serverURL;
        facade = new ServerFacade(serverURL);
    }

    private String getFirstString() {
        String line = new Scanner(System.in).next();
        var tokens = line.toLowerCase().split(" ");
        return (tokens.length > 0) ? tokens[0] : "";
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
                case "quit" -> quit();
                default -> helpLoggedOut();
            }
        }
        else if (state == State.LOGGED_IN) {
            switch (input) {
                case "create" -> create();
                case "logout" -> logout();
                case "quit" -> quit();
                default -> helpLoggedIn();
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

    private void quit() {
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
        String gameName = getFirstString();

        System.out.println();
        try {
            facade.createGame(authToken, gameName);
            System.out.println("Successfully created the new game " + gameName + ".");
        }
        catch (ResponseException e) {
            switch (e.getCode()) {
                case 400 -> System.out.println("Invalid game name. Failed to create game.");
                case 401 -> System.out.println("You are no longer logged in. Failed to create game.");
                default -> System.out.println("An unexpected error occurred. Failed to register user.");
            }
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
                System.out.println("Invalid username or password. Failed to log in.");
            } else {
                System.out.println("An unexpected error occurred. Failed to log in.");
            }
        }
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
}
