package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest request) throws ServiceException {
        try {
            if (request.username() == null || request.password() == null || request.email() == null) {
                throw new ServiceException("Error: bad request", 400);
            }

            UserData checkUserData = userDAO.getUser(request.username());
            if (checkUserData != null) {
                throw new ServiceException("Error: already taken", 403);
            }

            userDAO.createUser(new UserData(request.username(), request.password(), request.email()));
            String authToken = generateToken();
            authDAO.createAuth(new AuthData(authToken, request.username()));
            return new RegisterResult(200, request.username(), authToken);
        }
        catch (DataAccessException e) {
            throw new ServiceException("Error: ".concat(e.getMessage()), 500);
        }
    }
}
