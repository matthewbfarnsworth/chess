package model;

public record ListedGame(
        int gameID,
        String whiteUsername,
        String blackUsername,
        String gameName) {
}
