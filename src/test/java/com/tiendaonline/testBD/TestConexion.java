package com.tiendaonline.testBD;

import com.tiendaonline.util.ConexionBD;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConexion {
    public static void main(String[] args) {
        try (Connection conn = ConexionBD.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Conexión exitosa a la base de datos!");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar: " + e.getMessage());
        }
    }
}
