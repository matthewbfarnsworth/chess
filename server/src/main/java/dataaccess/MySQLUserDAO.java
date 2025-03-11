package dataaccess;

import model.UserData;

public class MySQLUserDAO extends MySQLDAO implements UserDAO {

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {

    }
}
