package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryGameDAO implements GameDAO {
    private final Map<Integer, GameData> gameDataMap = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        try {
            gameDataMap.clear();
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try {
            return gameDataMap.get(gameID);
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        try {
            int gameID = generateGameID();
            gameDataMap.put(gameID, new GameData(gameID, null, null, gameName, new ChessGame(), false));
            return gameID;
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        try {
            return new ArrayList<>(gameDataMap.values());
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private int generateGameID() {
        int gameID = gameDataMap.size() + 1;
        while (gameDataMap.containsKey(gameID)) {
            gameID++;
        }
        return gameID;
    }

    @Override
    public void updateGame(int gameID, String username, Color color) throws DataAccessException {
        try {
            GameData game = gameDataMap.get(gameID);
            GameData newGame = switch (color) {
                case WHITE -> new GameData(gameID, username, game.blackUsername(), game.gameName(), game.game(),
                        game.gameOver());
                case BLACK -> new GameData(gameID, game.whiteUsername(), username, game.gameName(),game.game(),
                        game.gameOver());
            };
            gameDataMap.replace(gameID, newGame);
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void replaceGame(int gameID, ChessGame game) throws DataAccessException {
        try {
            GameData oldGame = gameDataMap.get(gameID);
            GameData newGame = new GameData(gameID, oldGame.whiteUsername(), oldGame.blackUsername(),
                    oldGame.gameName(), game, oldGame.gameOver());
            gameDataMap.replace(gameID, newGame);
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void setGameOver(int gameID, boolean gameOver) throws DataAccessException {
        try {
            GameData oldGame = gameDataMap.get(gameID);
            GameData newGame = new GameData(gameID, oldGame.whiteUsername(), oldGame.blackUsername(),
                    oldGame.gameName(), oldGame.game(), gameOver);
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
