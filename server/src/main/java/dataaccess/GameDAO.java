package dataaccess;

import model.GameData;

import java.util.List;

public interface GameDAO {
    void clear() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void createGame(GameData gameData) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;
    int generateGameID();
    enum Color {
        WHITE,
        BLACK
    }
    void updateGame(int gameID, String username, Color color) throws DataAccessException;
}
