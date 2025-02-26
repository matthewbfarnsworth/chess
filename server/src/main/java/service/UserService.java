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

            UserData userData = userDAO.getUser(request.username());
            if (userData != null) {
                throw new ServiceException("Error: already taken", 403);
            }

            userDAO.createUser(new UserData(request.username(), request.password(), request.email()));
            String authToken = generateToken();
            authDAO.createAuth(new AuthData(authToken, request.username()));
            return new RegisterResult(request.username(), authToken);
        }
        catch (DataAccessException e) {
            throw new ServiceException("Error: ".concat(e.getMessage()), 500);
        }
    }

    public LoginResult login(LoginRequest request) throws ServiceException {
        try {
            if (request.username() == null || request.password() == null) {
                throw new ServiceException("Error: unauthorized", 401);
            }

            UserData userData = userDAO.getUser(request.username());
            if (userData == null || !request.password().equals(userData.password())) {
                throw new ServiceException("Error: unauthorized", 401);
            }

            String authToken = generateToken();
            authDAO.createAuth(new AuthData(authToken, request.username()));
            return new LoginResult(request.username(), authToken);
        }
        catch (DataAccessException e) {
            throw new ServiceException("Error: ".concat(e.getMessage()), 500);
        }
    }

    public void logout(String authToken) throws ServiceException {
        try {
            if (authToken == null) {
                throw new ServiceException("Error: unauthorized", 401);
            }

            AuthData authData = authDAO.getAuth(authToken);
            if (authData == null || !authToken.equals(authData.authToken())) {
                throw new ServiceException("Error: unauthorized", 401);
            }

            authDAO.deleteAuth(authData);
        }
        catch (DataAccessException e) {
            throw new ServiceException("Error: ".concat(e.getMessage()), 500);
        }
    }
}
