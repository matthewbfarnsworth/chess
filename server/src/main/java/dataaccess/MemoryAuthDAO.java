package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO {
    Map<String, AuthData> authDataMap = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        try {
            authDataMap.put(authData.authToken(), authData);
        }
        catch (Exception e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
