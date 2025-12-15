package com.tiendaonline.model;

import com.tiendaonline.dao.factory.DAOFactory;
import com.tiendaonline.dao.factory.JpaDAOFactory;
import com.tiendaonline.dao.interfaces.ArticuloDAO;
import com.tiendaonline.dao.interfaces.ClienteDAO;
import com.tiendaonline.dao.interfaces.PedidoDAO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TiendaOnline {

    private ArticuloDAO articuloDAO;
    private ClienteDAO clienteDAO;
    private PedidoDAO pedidoDAO;
    private DAOFactory factory;

    public TiendaOnline() {
        // 1. Instancia de la Factoría JPA
        this.factory = new JpaDAOFactory();

        // 2. Obtenemos TODOS los DAO
        this.articuloDAO = factory.getArticuloDAO();
        this.clienteDAO = factory.getClienteDAO();
        this.pedidoDAO = factory.getPedidoDAO();
    }

    // ===================== CLIENTES =====================

    public Cliente anadirCliente(String email, String nombre, String domicilio, String nif, boolean premium, double descuentoControlador, int cuotaControlador) throws Exception {
        Cliente cliente;

        if (premium) {
            // Instancia ClientePremium usando sus constantes internas (30, 0.20)
            cliente = new ClientePremium(email, nombre, domicilio, nif, ClientePremium.DESCUENTO_ENVIO_PREMIUM, ClientePremium.CUOTA_ANUAL_PREMIUM);
        } else {
            // CORRECCIÓN CLAVE: ClienteStandar solo usa los 4 parámetros base.
            cliente = new ClienteStandar(email, nombre, domicilio, nif);
        }

        // El DAO se encarga de la persistencia polimórfica.
        clienteDAO.anadirCliente(cliente);
        return cliente;
    }

    public List<Cliente> mostrarClientes() throws Exception {
        return clienteDAO.getTodosLosClientes();
    }

    public List<Cliente> mostrarClientesEstandar() throws Exception {
        return clienteDAO.getClientesEstandar();
    }

    public List<Cliente> mostrarClientesPremium() throws Exception {
        return clienteDAO.getClientesPremium();
    }

    public Cliente buscarClientePorEmail(String email) throws Exception {
        return clienteDAO.getClientePorEmail(email);
    }

    // ===================== ARTÍCULOS =====================
    public void anadirArticulo(String codigo, String descripcion, Double precioVenta, Double gastosEnvio, int tiempoPreparacion) throws Exception {
        // Validamos si el DAO está disponible
        if (articuloDAO == null) throw new Exception("Error crítico: ArticuloDAO es NULL. Revisa JpaDAOFactory.");

        Articulo existente = buscarArticulo(codigo);
        if (existente != null) {
            throw new IllegalArgumentException("Ya existe un artículo con el código: " + codigo);
        }

        Articulo articulo = new Articulo(codigo, descripcion, precioVenta, gastosEnvio, tiempoPreparacion);
        articuloDAO.anadirArticulo(articulo);
    }

    public List<Articulo> mostrarArticulos() throws Exception {
        if (articuloDAO == null) return null;
        return articuloDAO.getTodosLosArticulos();
    }

    public Articulo buscarArticulo(String codigo) throws Exception {
        if (articuloDAO == null) return null;
        return articuloDAO.getArticuloPorCodigo(codigo);
    }

    public void eliminarArticulo(String codigo) throws Exception {
        if (articuloDAO == null) throw new Exception("Error crítico: ArticuloDAO es NULL.");
        // Llamamos al DAO que actualizamos en el paso anterior
        articuloDAO.eliminarArticulo(codigo);
    }

    // ===================== PEDIDOS =====================
    public void anadirPedido(String numeroPedido, String emailCliente, String codigoArticulo, int cantidad) throws Exception {
        if (pedidoDAO == null) throw new Exception("Error crítico: PedidoDAO es NULL. Revisa JpaDAOFactory.");

        Cliente cliente = clienteDAO.getClientePorEmail(emailCliente);
        Articulo articulo = articuloDAO.getArticuloPorCodigo(codigoArticulo);

        if (cliente == null)
            throw new IllegalArgumentException("No existe el cliente con email: " + emailCliente);
        if (articulo == null)
            throw new IllegalArgumentException("No existe el artículo con código: " + codigoArticulo);
        if (cantidad <= 0)
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0");

        // Creamos el pedido (Estado inicial false = pendiente)
        Pedido pedido = new Pedido(numeroPedido, cliente, articulo, cantidad, LocalDateTime.now(), false);
        pedidoDAO.anadirPedido(pedido);
    }

    public boolean eliminarPedido(String numeroPedido) throws Exception {
        if (pedidoDAO == null) return false;
        return pedidoDAO.eliminarPedido(numeroPedido);
    }

    public void marcarPedidoComoEnviado(String numeroPedido) throws Exception {
        if (pedidoDAO == null) return;
        pedidoDAO.marcarPedidoEnviado(numeroPedido);
    }

    public List<Pedido> mostrarPedidos() throws Exception {
        if (pedidoDAO == null) return new ArrayList<>();
        return new ArrayList<>(pedidoDAO.getTodosLosPedidos());
    }

    public List<Pedido> mostrarPedidosPendientes() throws Exception {
        if (pedidoDAO == null) return null;
        return pedidoDAO.getPedidosPendientes();
    }

    public List<Pedido> mostrarPedidosPendientes(String emailCliente) throws Exception {
        if (pedidoDAO == null) return null;
        return pedidoDAO.getPedidosPendientesPorCliente(emailCliente)
                .stream().filter(p -> !p.isEstado()).toList();
    }

    public List<Pedido> mostrarPedidosEnviados() throws Exception {
        if (pedidoDAO == null) return null;
        return pedidoDAO.getPedidosEnviados();
    }

    public List<Pedido> mostrarPedidosEnviados(String emailCliente) throws Exception {
        if (pedidoDAO == null) return null;
        return pedidoDAO.getPedidosEnviadosPorCliente(emailCliente)
                .stream().filter(Pedido::isEstado).toList();
    }

    public Pedido buscarPedido(String numeroPedido) throws Exception {
        if (pedidoDAO == null) return null;
        return pedidoDAO.getPedidoPorNumero(numeroPedido);
    }

    // === ESTADÍSTICAS (Para toString) ===

    public int getTotalArticulos() throws Exception {
        return (articuloDAO != null) ? articuloDAO.getTodosLosArticulos().size() : 0;
    }

    public int getTotalClientes() throws Exception {
        return clienteDAO.getTodosLosClientes().size();
    }

    public int getTotalClientesEstandar() throws Exception {
        return clienteDAO.getClientesEstandar().size();
    }

    public int getTotalClientesPremium() throws Exception {
        return clienteDAO.getClientesPremium().size();
    }

    public int getTotalPedidos() throws Exception {
        return (pedidoDAO != null) ? pedidoDAO.getTodosLosPedidos().size() : 0;
    }

    public int getTotalPedidosPendientes() throws Exception {
        return (pedidoDAO != null) ? pedidoDAO.getPedidosPendientes().size() : 0;
    }

    public int getTotalPedidosEnviados() throws Exception {
        return (pedidoDAO != null) ? pedidoDAO.getPedidosEnviados().size() : 0;
    }

    @Override
    public String toString() {
        try {
            return "TiendaOnline{" +
                    "articulos=" + getTotalArticulos() +
                    ", clientes=" + getTotalClientes() +
                    " (Estandar: " + getTotalClientesEstandar() +
                    ", Premium: " + getTotalClientesPremium() + ")" +
                    ", pedidos=" + getTotalPedidos() +
                    " (Pendientes: " + getTotalPedidosPendientes() +
                    ", Enviados: " + getTotalPedidosEnviados() + ")" +
                    '}';
        } catch (Exception e) {
            return "Error al generar toString: " + e.getMessage();
        }
    }

    // ===================== DATOS DE PRUEBA COMPLETOS =====================
    public void cargarDatosDePrueba() throws Exception {
        System.out.println("Cargando TODOS los datos de prueba...");

    }
}