package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> userDataMap = new HashMap<>();

    @Override
    public void clear() throws DataAccessException {
        try {
            userDataMap.clear();
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException{
        try {
            return userDataMap.get(username);
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        try {
            userDataMap.put(userData.username(), userData);
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
