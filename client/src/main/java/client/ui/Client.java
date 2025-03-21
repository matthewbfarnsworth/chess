package client.ui;

import java.util.Arrays;
import java.util.Scanner;

public class Client {
    private final String serverURL;
    private final State state = State.LOGGED_OUT;
    private boolean quit = false;
    private String authToken;

    private enum State {
        LOGGED_OUT,
        LOGGED_IN
    }

    public Client(String serverURL) {
        this.serverURL = serverURL;
    }

    public void run() {
        System.out.println("CS 240 CHESS\n");
        System.out.println("Running on " + serverURL + "\n");
        new ChessBoard().printBoard(new chess.ChessBoard(), true);
        help();

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
        switch (cmd) {
            case "quit" -> quit();
            default -> help();
        }
    }

    private void quit() {
        quit = true;
    }

    private void help() {
        if (state == State.LOGGED_OUT) {
            System.out.println("""
                       login
                       register
                       quit
                       help""");
        }
        else {
            System.out.println("""
                       create
                       list
                       join
                       observe
                       logout
                       quit
                       help""");
        }
    }
}
