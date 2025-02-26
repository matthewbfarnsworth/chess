package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class DBService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    private final GameDAO gameDAO;

    public DBService(UserDAO userDAO, AuthDAO authDAO, GameDAO gameDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public void clearApplication() throws ServiceException {
        try {
            userDAO.clear();
            authDAO.clear();
            gameDAO.clear();
        }
        catch (DataAccessException e) {
            throw new ServiceException(e.getMessage(), 500);
        }
    }
}
