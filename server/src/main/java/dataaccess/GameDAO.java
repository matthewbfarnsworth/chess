package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    void clear() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;
    enum Color {
        WHITE,
        BLACK
    }
    void updateGame(int gameID, String username, Color color) throws DataAccessException;
}
