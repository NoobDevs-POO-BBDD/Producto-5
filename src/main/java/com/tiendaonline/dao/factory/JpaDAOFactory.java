package com.tiendaonline.dao.factory;

import com.tiendaonline.dao.interfaces.ArticuloDAO;
import com.tiendaonline.dao.interfaces.ClienteDAO;
import com.tiendaonline.dao.interfaces.PedidoDAO;
import com.tiendaonline.dao.jpa.ArticuloDAOJpaImpl;
import com.tiendaonline.dao.jpa.ClienteDAOJpaImpl;
import com.tiendaonline.dao.jpa.PedidoDAOJpaImpl;
import jakarta.persistence.EntityManager;
// ¡Ya no importamos JPAUtil! La factory ya no crea el EntityManager.
// import com.tiendaonline.util.JPAUtil;

public class JpaDAOFactory implements DAOFactory {

    // --- MÉTODOS ANTIGUOS (DE LA INTERFAZ) ---
    // Los dejamos para que el código compile, pero ya no los usaremos.
    // Crean sus propios 'em' y no permiten controlar la transacción.

    /**
     * @return se pasa em el entity Manager
     */
    @Override
    public ArticuloDAO getArticuloDAO() {
        // Este método ya no es útil porque no podemos controlar su transacción
        // Devolvemos null para forzar el uso del método nuevo
        return null;
    }

    /**
     * @return se pasa em el entity Manager
     */
    @Override
    public ClienteDAO getClienteDAO() {
        return null;
    }

    /**
     * @return se pasa em el entity Manager
     */
    @Override
    public PedidoDAO getPedidoDAO() {
        return null;
    }

    // --- ¡MÉTODOS NUEVOS (SOBRECARGADOS)! ---
    // Estos son los métodos que SÍ usará nuestro código (Test y Controlador)
    // Aceptan el EntityManager para poder gestionar la transacción desde fuera.

    public ArticuloDAO getArticuloDAO(EntityManager em) {
        // Ahora inyectamos el 'em' que recibimos
        return new ArticuloDAOJpaImpl(em);
    }

    public ClienteDAO getClienteDAO(EntityManager em) {
        // (Asumiendo que ClienteDAOJpaImpl también fue corregido)
        return new ClienteDAOJpaImpl(em);
    }

    public PedidoDAO getPedidoDAO(EntityManager em) {
        // (Asumiendo que PedidoDAOJpaImpl también fue corregido)
        return new PedidoDAOJpaImpl(em);
    }
}