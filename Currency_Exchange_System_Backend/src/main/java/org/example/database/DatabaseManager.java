package org.example.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String DB_PATH = "Currency_Exchange_System_Backend/database/exchange_project.db";
    private static final String URL = "jdbc:sqlite:" + DB_PATH;


    public static Connection getConnection() throws SQLException {

        File dbFile = new File(DB_PATH);
        File parentDir = dbFile.getParentFile();

        if(parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        return DriverManager.getConnection(URL);
    }
}
