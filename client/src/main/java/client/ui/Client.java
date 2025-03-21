package client.ui;

import client.net.ResponseException;
import client.net.ServerFacade;

import java.util.Arrays;
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

    public void run() {
        System.out.println("CS 240 CHESS");
        System.out.println("Running on " + serverURL + "\n");
        helpLoggedOut();

        Scanner scanner = new Scanner(System.in);
        while (!quit) {
            System.out.print("\n>>> ");
            String line = scanner.nextLine();

            eval(line);
        }
    }

    private void eval(String input) {
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (state == State.LOGGED_OUT) {
            switch (cmd) {
                case "register" -> register();
                case "quit" -> quit();
                default -> helpLoggedOut();
            }
        }
        else if (state == State.LOGGED_IN) {
            switch (cmd) {
                case "quit" -> quit();
                default -> helpLoggedIn();
            }
        }
    }

    private String getFirstString() {
        return new Scanner(System.in).next();
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
            System.out.println("Successfully registered user " + result.username());
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
