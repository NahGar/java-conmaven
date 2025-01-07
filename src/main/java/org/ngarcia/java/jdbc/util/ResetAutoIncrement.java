package org.ngarcia.java.jdbc.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class ResetAutoIncrement {

    public static void exec(String table) throws SQLException {

        String sql = "ALTER TABLE " + table + " AUTO_INCREMENT = 1";
        
        //utiliza una nueva conexión porque de lo contrario rompe la transacción por 
        try(Connection conn = ConexionBaseDatosPool.getConnection();
            Statement stmt = conn.createStatement()) {
            
            stmt.executeUpdate(sql);
        }

    }
}
