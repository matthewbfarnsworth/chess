package dataaccess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

public class MySQLDAOTests {

    @BeforeAll
    public static void dropDatabase() {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DROP DATABASE chess";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException | DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCreateDatabase() {
        Assertions.assertDoesNotThrow(DatabaseManager::createDatabase);
    }

    @Test
    public void testConfigureDatabase() {
        Assertions.assertDoesNotThrow(MySQLDAO::new);
    }

    @BeforeEach
    public void clearTables() {

    }
}
