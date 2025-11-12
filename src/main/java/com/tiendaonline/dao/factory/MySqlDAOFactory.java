package main.java.com.tiendaonline.dao.factory;

import com.tiendaonline.dao.impl.ArticuloDAOImpl;
import com.tiendaonline.dao.impl.ClienteDAOImpl;
import com.tiendaonline.dao.impl.PedidosDAOImpl;
import com.tiendaonline.dao.interfaces.ArticuloDAO;
import com.tiendaonline.dao.interfaces.ClienteDAO;
import com.tiendaonline.dao.interfaces.PedidoDAO;

public class MySqlDAOFactory implements DAOFactory {

    // Guarda las instancias para reutilizarlas
    private ArticuloDAO articuloDAO;
    private ClienteDAO clienteDAO;
    private PedidoDAO pedidoDAO;

    @Override
    public ArticuloDAO getArticuloDAO() {
        if (this.articuloDAO == null) {
            this.articuloDAO = new ArticuloDAOImpl();
        }
        return this.articuloDAO;
    }

    @Override
    public ClienteDAO getClienteDAO() {
        if (this.clienteDAO == null) {
            this.clienteDAO = new ClienteDAOImpl();
        }
        return this.clienteDAO;
    }

    @Override
    public PedidoDAO getPedidoDAO() {
        if (this.pedidoDAO == null) {

            // Cuando creas el PedidoDAO le pasas los otros DAOs que la fábrica ya conoce.
            this.pedidoDAO = new PedidosDAOImpl( getArticuloDAO(), getClienteDAO() );
        }
        return this.pedidoDAO;
    }
}