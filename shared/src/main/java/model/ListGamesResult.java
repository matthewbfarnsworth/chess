package model;

import java.util.List;

public record ListGamesResult(
        List<ListedGame> games) {
}
