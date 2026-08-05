package org.example.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String MODULE_DIR_NAME = "Currency_Exchange_System_Backend";
    private static final String DB_RELATIVE_PATH = "database/exchange_project.db";
    private static final String DB_PATH = resolveDbPath();
    private static final String URL = "jdbc:sqlite:" + DB_PATH;

    /**
     * Always resolves to ".../Currency_Exchange_System_Backend/database/exchange_project.db",
     * no matter whether the app is launched with its working directory set to the
     * Backend module itself or to the parent project folder.
     */
    private static String resolveDbPath() {
        File cwd = new File(System.getProperty("user.dir"));

        if (cwd.getName().equals(MODULE_DIR_NAME)) {
            // already running from inside Currency_Exchange_System_Backend
            return new File(cwd, DB_RELATIVE_PATH).getPath();
        }

        // running from the parent project folder (or anywhere else) -> descend into the module dir
        return new File(new File(cwd, MODULE_DIR_NAME), DB_RELATIVE_PATH).getPath();
    }

    public static Connection getConnection() throws SQLException {

        File dbFile = new File(DB_PATH);
        File parentDir = dbFile.getParentFile();

        if(parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        return DriverManager.getConnection(URL);
    }
}