package com.tiendaonline.dao.factory;

import com.tiendaonline.dao.interfaces.ArticuloDAO;
import com.tiendaonline.dao.interfaces.ClienteDAO;
import com.tiendaonline.dao.interfaces.PedidoDAO;
import com.tiendaonline.util.JPAUtil;
import jakarta.persistence.EntityManager;
import com.tiendaonline.dao.jpa.ArticuloDAOJpaImpl;
import com.tiendaonline.dao.jpa.ClienteDAOJpaImpl;
import com.tiendaonline.dao.jpa.PedidoDAOJpaImpl;

public class JpaDAOFactory implements DAOFactory {

    @Override
    public ArticuloDAO getArticuloDAO() {
        // Creamos el EntityManager y el DAO de Artículos
        EntityManager em = JPAUtil.getEntityManager();
        return new ArticuloDAOJpaImpl(em);
    }

    @Override
    public ClienteDAO getClienteDAO() {
        // Creamos el EntityManager y el DAO de Clientes
        EntityManager em = JPAUtil.getEntityManager();
        return new ClienteDAOJpaImpl(em);
    }

    @Override
    public PedidoDAO getPedidoDAO() {
        // Creamos el EntityManager y el DAO de Pedidos
        EntityManager em = JPAUtil.getEntityManager();
        return new PedidoDAOJpaImpl(em);
    }
}