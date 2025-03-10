package dataaccess;

import model.GameData;

import java.util.List;

public class GameDAOImpl implements GameDAO {
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public int generateGameID() {
        return 0;
    }

    @Override
    public void updateGame(int gameID, String username, Color color) throws DataAccessException {

    }
}
