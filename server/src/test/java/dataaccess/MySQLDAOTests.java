package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MySQLDAOTests {

    @Test
    public void testCreateDatabase() {
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
    }

    @Test
    public void testConfigureDatabase() {
        Assertions.assertDoesNotThrow(MySQLDAO::new);
    }
}
