package org.example.database;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.plaf.nimbus.State;
import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInitializer {

    private static final String ADMIN_DEFAULT_USERNAME = "admin";
    private static final String ADMIN_DEFAULT_PASSWORD = "admin123";

    public static void initialize() {

        try(
                Connection connection = DatabaseManager.getConnection();
                Statement statement = connection.createStatement();
                ) {

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS "users" (
                	"id"	INTEGER,
                	"username"	TEXT NOT NULL UNIQUE,
                	"password_hash"	TEXT NOT NULL,
                	"role"	TEXT NOT NULL,
                	"is_active"	INTEGER NOT NULL DEFAULT 1,
                	PRIMARY KEY("id" AUTOINCREMENT)
                )"""
            );


            statement.execute("""
                    CREATE TABLE IF NOT EXISTS "customers" (
                	"id"	INTEGER,
                	"full_name"	TEXT NOT NULL,
                	"national_id"	TEXT NOT NULL,
                	"phone_number"	TEXT,
                	"user_id"	INTEGER NOT NULL UNIQUE,
                	PRIMARY KEY("id" AUTOINCREMENT),
                	FOREIGN KEY("user_id") REFERENCES "users"("id")
                );
                """);


            statement.execute("""
                CREATE TABLE IF NOT EXISTS "tellers" (
                	"id"	INTEGER,
                	"full_name"	TEXT NOT NULL,
                	"national_id"	TEXT NOT NULL,
                	"phone_number"	TEXT,
                	"user_id"	INTEGER NOT NULL UNIQUE,
                	PRIMARY KEY("id" AUTOINCREMENT),
                	FOREIGN KEY("user_id") REFERENCES "users"("id")
                );
                """);


            statement.execute("""
                    CREATE TABLE IF NOT EXISTS "currencies" (
                	"id"	INTEGER,
                	"code"	TEXT NOT NULL UNIQUE,
                	"name"	TEXT NOT NULL,
                	"symbol"	TEXT,
                	"is_active"    INTEGER NOT NULL DEFAULT 1,
                	PRIMARY KEY("id" AUTOINCREMENT)
                );
                """);


            statement.execute("""
                CREATE TABLE IF NOT EXISTS "exchange_rates" (
                	"id"	INTEGER,
                	"currency_id"	INTEGER NOT NULL,
                	"buy_rate"	TEXT NOT NULL,
                	"sell_Rate"	TEXT NOT NULL,
                	"effective_date"	TEXT NOT NULL,
                	"created_by"	INTEGER NOT NULL,
                	PRIMARY KEY("id" AUTOINCREMENT),
                	FOREIGN KEY("created_by") REFERENCES "users"("id"),
                	FOREIGN KEY("currency_id") REFERENCES "currencies"("id")
                );
                """);


            statement.execute("""
                CREATE TABLE IF NOT EXISTS "transactions" (
                	"id"	INTEGER,
                	"type"	TEXT NOT NULL,
                	"currency_id"	INTEGER NOT NULL,
                	"customer_id"   INTEGER NOT NULL,
                	"amount_currency"	TEXT NOT NULL,
                	"amount_toman"	TEXT NOT NULL,
                	"requested_rate"	TEXT,
                	"rate_used"	TEXT,
                	"requested_by_customer"	INTEGER NOT NULL DEFAULT 0,
                	"performed_by_userId"	INTEGER,
                	"approved_by_userId"	INTEGER,
                	"created_at"	TEXT NOT NULL,
                	"approved_at"	TEXT,
                	"status"	TEXT NOT NULL,
                	PRIMARY KEY("id" AUTOINCREMENT),
                	FOREIGN KEY("approved_by_userId") REFERENCES "users"("id"),
                	FOREIGN KEY("currency_id") REFERENCES "currencies"("id"),
                	FOREIGN KEY("customer_id") REFERENCES "customers"("id"),
                	FOREIGN KEY("performed_by_userId") REFERENCES "users"("id")
                );
                """);



            statement.execute("""
                CREATE TABLE IF NOT EXISTS"vault_balances" (
                	"id"	INTEGER,
                	"currency_id"	INTEGER NOT NULL UNIQUE,
                	"balance"	TEXT NOT NULL DEFAULT 0,
                	"lastUpdated"	TEXT NOT NULL,
                	PRIMARY KEY("id" AUTOINCREMENT),
                	FOREIGN KEY("currency_id") REFERENCES "currencies"("id")
                );
                """);


            statement.execute("""
                CREATE TABLE IF NOT EXISTS "vault_ledgers" (
                	"id"	INTEGER,
                	"currency_id"	INTEGER NOT NULL,
                	"change_amount"	TEXT NOT NULL DEFAULT 0,
                	"reason"	TEXT NOT NULL,
                	"created_at"	TEXT NOT NULL,
                	"performed_by_userId"	INTEGER NOT NULL,
                	PRIMARY KEY("id" AUTOINCREMENT),
                	FOREIGN KEY("currency_id") REFERENCES "currencies"("id"),
                	FOREIGN KEY("performed_by_userId") REFERENCES "users"("id")
                );
                """);



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static  void seedDefaultAdmin(Connection connection) throws Exception {

        String checkSql = """
                SELECT COUNT(*) FROM users WHERE username = ?
                """;

        try(PreparedStatement check = connection.prepareStatement(checkSql)) {

            check.setString(1, ADMIN_DEFAULT_USERNAME);
            ResultSet resultSet = check.executeQuery();
            resultSet.next();

            if(resultSet.getInt(1) > 0) {
                return;
            }

            String hashedPassword = new BCryptPasswordEncoder().encode(ADMIN_DEFAULT_PASSWORD);
            String insertSql = """
                    INSERT INTO users (
                    username,
                    password_hash,
                    role,
                    is_active)
                    VALUES(?, ?, ?, ?)
                    """;

            try(PreparedStatement insert = connection.prepareStatement(insertSql)) {

                insert.setString(1, ADMIN_DEFAULT_USERNAME);
                insert.setString(2, ADMIN_DEFAULT_PASSWORD);
                insert.setString(3, "ADMIN");
                insert.setInt(1, 1);

                insert.executeUpdate();

            }

            System.out.println("Default admin created");

        }

    }


}
