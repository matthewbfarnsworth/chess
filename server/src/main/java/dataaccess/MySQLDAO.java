package dataaccess;

import java.sql.SQLException;

public class MySQLDAO {

    public MySQLDAO() throws DataAccessException {
        configureDatabase();
    }

    private static final String[] CREATE_TABLE_STATEMENTS = {
            """
            CREATE TABLE IF NOT EXISTS userData (
                id int NOT NULL AUTO_INCREMENT,
                username varchar(256) NOT NULL,
                password varchar(256) NOT NULL,
                email varchar(256) NOT NULL,
                PRIMARY KEY (id),
                INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """,
            """
            CREATE TABLE IF NOT EXISTS authData (
                id int NOT NULL AUTO_INCREMENT,
                authToken varchar(256) NOT NULL,
                username varchar(256) NOT NULL,
                PRIMARY KEY (id),
                INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """,
            """
            CREATE TABLE IF NOT EXISTS gameData (
                gameID int NOT NULL AUTO_INCREMENT,
                whiteUsername varchar(256),
                blackUsername varchar(256),
                gameName varchar(256) NOT NULL,
                game TEXT NOT NULL,
                PRIMARY KEY (gameID)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : CREATE_TABLE_STATEMENTS) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }
}
