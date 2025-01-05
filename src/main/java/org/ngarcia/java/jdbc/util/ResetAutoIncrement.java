package org.ngarcia.java.jdbc.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ResetAutoIncrement {

    public static void exec(Connection conn, String table) throws SQLException {

        String sql = "ALTER TABLE " + table + " AUTO_INCREMENT = 1";

        try (Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
        }
    }
}
