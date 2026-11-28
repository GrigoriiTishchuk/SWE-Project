package com.edujournal.database;

import com.edujournal.config.EnvConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Utility class responsible for database initialization.
 * <p>
 * Creates the database if it does not exist, executes schema scripts,
 * and loads initial seed data when the database is initialized for
 * the first time.
 * </p>
 */
public final class DatabaseInitializer {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    /**
     * Prevents instantiation of this utility class.
     */
    private DatabaseInitializer() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Creates the database if necessary, executes the schema script,
     * and populates the database with initial data when it has not
     * been seeded before.
     */
    @SuppressWarnings({"java:S2077", "java:S112"}) // database name cannot be parameterized in JDBC
    public static void initialize() {

        String databaseName = EnvConfig.get("DB_NAME");
        String username = EnvConfig.get("DB_USERNAME");
        String password = EnvConfig.get("DB_PASSWORD");
        String databasePort = EnvConfig.get("DB_PORT");
        String databaseHost = EnvConfig.get("DB_HOST");

        String serverUrl = "jdbc:mariadb://" + databaseHost + ":" + databasePort;

        try (Connection connection = DriverManager.getConnection(serverUrl, username, password);
                Statement statement = connection.createStatement()) {

            statement.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS "
                            + databaseName
                            + " DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");

            LOGGER.info("Database is ready.");

        } catch (SQLException e) {
            throw new RuntimeException("Could not create database.", e);
        }

        String databaseUrl =
                "jdbc:mariadb://"
                        + databaseHost
                        + ":"
                        + databasePort
                        + "/"
                        + databaseName
                        + "?useUnicode=true&characterEncoding=UTF-8";

        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password)) {

            executeSqlFile(connection, "/database/schema.sql");
            LOGGER.info("Database tables are ready.");

            if (!isAlreadySeeded(connection)) {
                executeSqlFile(connection, "/database/data.sql");
                try (Statement st = connection.createStatement()) {
                    st.executeUpdate("INSERT INTO seed_log (id) VALUES (1)");
                }
                LOGGER.info("Database data is ready.");
            } else {
                LOGGER.info("Database already seeded, skipping data.sql.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Could not initialize database.", e);
        }
    }

    /**
     * Checks whether the database has already been seeded.
     *
     * @param connection active database connection
     * @return {@code true} if seed data has already been inserted,
     *         {@code false} otherwise
     */
    private static boolean isAlreadySeeded(Connection connection) {
        try (Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM seed_log")) {
            return rs.next() && rs.getInt(1) > 0;
        } catch (SQLException ignored) {
            // seed_log may not exist on first run
            return false;
        }
    }

    /**
     * Executes all SQL statements contained in the specified SQL file.
     * Statements are separated using semicolons.
     *
     * @param connection active database connection
     * @param resourcePath path to the SQL resource file
     * @throws SQLException if a database error occurs while executing
     *                      the SQL commands
     * @throws IllegalStateException if the specified resource file
     *                               cannot be found
     */
    private static void executeSqlFile(Connection connection, String resourcePath)
            throws SQLException {

        InputStream inputStream = DatabaseInitializer.class.getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new IllegalStateException(resourcePath + " not found!");
        }

        String sql =
                new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));

        String[] commands = sql.split(";");

        try (Statement statement = connection.createStatement()) {
            for (String command : commands) {
                String trimmedCommand = command.trim();
                if (!trimmedCommand.isEmpty()) {
                    statement.execute(trimmedCommand);
                }
            }
        }
    }
}
