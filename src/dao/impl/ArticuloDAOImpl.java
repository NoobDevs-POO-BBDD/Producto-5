package dao.impl;
import dao.interfaces.ArticuloDAO;
import model.Articulo;
import util.ConexionBD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDAOImpl implements ArticuloDAO {

    @Override
    public void anadirArticulo(Articulo articulo) throws SQLException {
        Connection conn = null; //es necesario para el try y finally

        try {
            conn = ConexionBD.getConnection();
            conn.setAutoCommit(false);//inicia la transacción

            try (CallableStatement cs = conn.prepareCall("{CALL sp_addArticulo(?,?,?,?,?)}")){

                cs.setString(1, articulo.getCodigo());
                cs.setString(2, articulo.getDescripcion());
                cs.setDouble(3, articulo.getPrecioVenta());
                cs.setDouble(4, articulo.getGastosEnvio());
                cs.setInt(5, articulo.getTiempoPreparacion());

                cs.executeUpdate();
            }
            conn.commit();// confirma la transacción si todo va bien

        } catch (SQLException e) {
            if (conn != null){
                try{
                    conn.rollback(); //se deshace la transacción si algo falla.
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: "+ ex.getMessage());
                }
            }
            throw e; // lanza error al controlador
        }finally {
            if (conn != null){
                try{
                    conn.close();
                }catch(SQLException e){
                    System.err.println("Error al cerrar la conexión: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public Articulo getArticuloPorCodigo(String codigo) throws SQLException {

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareCall("{CALL sp_getArticuloByCodigo(?)}")) {

            ps.setString(1, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion")
                );
            }
        }
        return null; // si no encuentra el artículo
    }

    @Override
    public List<Articulo> getTodosLosArticulos() throws SQLException {
        List<Articulo> lista = new ArrayList<>();

        try (Connection conn = ConexionBD.getConnection();
             PreparedStatement ps = conn.prepareCall("{CALL sp_getAllArticulos()}");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Articulo articulo = new Articulo(
                        rs.getString("codigo"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio_venta"),
                        rs.getDouble("gastos_envio"),
                        rs.getInt("tiempo_preparacion")
                );
                lista.add(articulo);
            }
        }
        return lista;
    }


}
