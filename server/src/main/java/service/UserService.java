package service;

import dataaccess.AuthDAO;
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

    public RegisterResult register(RegisterRequest request) {
        try {
            if (request.username() == null || request.password() == null || request.email() == null) {
                return new RegisterResult(400, "Error: bad request", null);
            }

            UserData checkUserData = userDAO.getUser(request.username());
            if (checkUserData != null) {
                return new RegisterResult(403, "Error: already taken", null);
            }

            userDAO.createUser(new UserData(request.username(), request.password(), request.email()));
            String authToken = generateToken();
            authDAO.createAuth(new AuthData(authToken, request.username()));
            return new RegisterResult(200, request.username(), authToken);
        }
        catch (Exception e) {
            return new RegisterResult(500, "Error: ".concat(e.getMessage()), null);
        }
    }
}
