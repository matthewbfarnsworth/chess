package dataaccess;

import model.GameData;

import java.util.List;

public class MySQLGameDAO extends MySQLDAO implements GameDAO {

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void updateGame(int gameID, String username, Color color) throws DataAccessException {

    }
}
