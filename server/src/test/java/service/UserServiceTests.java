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
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    public void testValidRegisterRequest() {
        RegisterRequest request = new RegisterRequest("name", "password", "email@gmail.com");
        RegisterResult result = userService.register(request);
        Assertions.assertEquals(result.code(), 200);
        Assertions.assertEquals(result.username(), request.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void testTakenRegisterRequest() {
        try {
            userDAO.createUser(new UserData("name", "password", "email@gmail.com"));
            RegisterRequest request = new RegisterRequest("name", "pass", "e@gmail.com");
            ServiceException exception = Assertions.assertThrows(ServiceException.class, () -> {
                userService.register(request);
            });
            Assertions.assertEquals(403, exception.getCode());
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testRegisterRequestWithNullValues() {
        RegisterRequest request = new RegisterRequest("name", null, "email@gmail.com");
        ServiceException exception = Assertions.assertThrows(ServiceException.class, () -> {
            RegisterResult result = userService.register(request);
        });
        Assertions.assertEquals(400, exception.getCode());
    }
  
}