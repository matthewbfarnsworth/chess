package service;

import dataaccess.*;

import model.UserData;
import org.junit.jupiter.api.*;

public class UserServiceTests {
    UserDAO userDAO;
    AuthDAO authDAO;
    UserService userService;

    @BeforeEach
    public void setupEach() {
        userDAO = new MySQLUserDAO();
        authDAO = new MySQLAuthDAO();
        try {
            userDAO.clear();
            authDAO.clear();
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e.getMessage());
        }
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void testValidRegisterRequest() {
        RegisterRequest request = new RegisterRequest("name", "password", "email@gmail.com");
        RegisterResult result = userService.register(request);
        Assertions.assertEquals(request.username(), result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void testTakenRegisterRequest() {
        try {
            userDAO.createUser(new UserData("name", "password", "email@gmail.com"));
            RegisterRequest request = new RegisterRequest("name", "pass", "e@gmail.com");
            ServiceException exception = Assertions.assertThrows(ServiceException.class, () ->
                    userService.register(request));
            Assertions.assertEquals(403, exception.getCode());
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRegisterRequestWithNullValues() {
        RegisterRequest request = new RegisterRequest("name", null, "email@gmail.com");
        ServiceException exception = Assertions.assertThrows(ServiceException.class, () ->
                userService.register(request));
        Assertions.assertEquals(400, exception.getCode());
    }

    @Test
    public void testValidLoginRequest() {
        userService.register(new RegisterRequest("name", "password", "email@gmail.com"));
        LoginRequest request = new LoginRequest("name", "password");
        LoginResult result = userService.login(request);
        Assertions.assertEquals(request.username(), result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void testInvalidUsernameLoginRequest() {
        userService.register(new RegisterRequest("name", "password", "email@gmail.com"));
        LoginRequest request = new LoginRequest("wrongName", "password");
        ServiceException exception = Assertions.assertThrows(ServiceException.class, () ->
                userService.login(request));
        Assertions.assertEquals(401, exception.getCode());
    }

    @Test
    public void testInvalidPasswordLoginRequest() {
        userService.register(new RegisterRequest("name", "password", "email@gmail.com"));
        LoginRequest request = new LoginRequest("name", "wrongPassword");
        ServiceException exception = Assertions.assertThrows(ServiceException.class, () ->
                userService.login(request));
        Assertions.assertEquals(401, exception.getCode());
    }

    @Test
    public void testValidLogoutRequest() {
        try {
            userService.register(new RegisterRequest("name", "password", "email@gmail.com"));
            LoginResult loginResult = userService.login(new LoginRequest("name", "password"));
            Assertions.assertNotNull(authDAO.getAuth(loginResult.authToken()));
            userService.logout(loginResult.authToken());
            Assertions.assertNull(authDAO.getAuth(loginResult.authToken()));
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testInvalidLogoutRequest() {
        userService.register(new RegisterRequest("name", "password", "email@gmail.com"));
        userService.login(new LoginRequest("name", "password"));
        ServiceException exception = Assertions.assertThrows(ServiceException.class, () -> userService.logout("a"));
        Assertions.assertEquals(401, exception.getCode());
    }
  
}