package dataaccess;

import model.GameData;

public interface GameDAO {
    void clear() throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void createGame(GameData gameData) throws DataAccessException;
    int generateGameID();
}
