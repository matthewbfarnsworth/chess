package dataaccess;

import model.GameData;

import java.util.HashMap;
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
    public void createGame(GameData gameData) throws DataAccessException {
        try {
            gameDataMap.put(gameData.gameID(), gameData);
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
