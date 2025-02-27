package service;

public record ListedGame(
        int gameID,
        String whiteUsername,
        String blackUsername,
        String gameName) {
}
