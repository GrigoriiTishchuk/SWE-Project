package com.edujournal.database;

import com.edujournal.config.EnvConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.stream.Collectors;

public class DatabaseInitializer {

    public static void initialize() {

        String databaseName = EnvConfig.get("DB_NAME");
        String username = EnvConfig.get("DB_USERNAME");
        String password = EnvConfig.get("DB_PASSWORD");
        String databasePort = EnvConfig.get("DB_PORT");

        String serverUrl = "jdbc:mariadb://localhost:" + databasePort;

        try (Connection connection =
                     DriverManager.getConnection(serverUrl, username, password);
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(
                    "CREATE DATABASE IF NOT EXISTS " + databaseName
            );

            System.out.println("Database is ready.");

        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not create database.",
                    e
            );
        }

        String databaseUrl =
                "jdbc:mariadb://localhost:" + databasePort + "/" + databaseName;

        try (Connection connection =
                     DriverManager.getConnection(
                             databaseUrl,
                             username,
                             password
                     )) {

            executeSqlFile(connection, "/database/schema.sql");
            System.out.println("Database tables are ready.");

            executeSqlFile(connection, "/database/data.sql");
            System.out.println("Database data is ready.");

        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not initialize database.",
                    e
            );
        }
    }

    private static void executeSqlFile(
            Connection connection,
            String resourcePath
    ) throws Exception {

        InputStream inputStream =
                DatabaseInitializer.class
                        .getResourceAsStream(resourcePath);

        if (inputStream == null) {
            throw new RuntimeException(
                    resourcePath + " not found!"
            );
        }

        String sql = new BufferedReader(
                new InputStreamReader(inputStream)
        )
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