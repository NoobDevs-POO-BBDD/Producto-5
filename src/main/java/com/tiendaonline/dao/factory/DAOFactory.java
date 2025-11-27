package com.tiendaonline.dao.factory;

import com.tiendaonline.dao.interfaces.ArticuloDAO;
import com.tiendaonline.dao.interfaces.ClienteDAO;
import com.tiendaonline.dao.interfaces.PedidoDAO;

/**
 * Esta es la INTERFAZ (el contrato).
 */
public interface DAOFactory {

    ArticuloDAO getArticuloDAO();

    ClienteDAO getClienteDAO();

    PedidoDAO getPedidoDAO();
}