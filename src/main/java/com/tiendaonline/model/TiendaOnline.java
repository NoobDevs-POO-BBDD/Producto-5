package com.tiendaonline.model;

import com.tiendaonline.dao.factory.DAOFactory;
import com.tiendaonline.dao.factory.JpaDAOFactory;
import com.tiendaonline.dao.interfaces.ArticuloDAO;
import com.tiendaonline.dao.interfaces.ClienteDAO;
import com.tiendaonline.dao.interfaces.PedidoDAO;
import java.time.LocalDateTime;
import java.util.List;

public class TiendaOnline {

    private ArticuloDAO articuloDAO;
    private ClienteDAO clienteDAO;
    private PedidoDAO pedidoDAO;
    private DAOFactory factory;

    public TiendaOnline() {
        // 1. Instancia de la Factoría JPA
        this.factory = new JpaDAOFactory();

        // 2. Obtenemos TODOS los DAOs
        this.articuloDAO = factory.getArticuloDAO();
        this.clienteDAO = factory.getClienteDAO();
        this.pedidoDAO = factory.getPedidoDAO();
    }

    // ===================== CLIENTES =====================
    public Cliente anadirCliente(String email, String nombre, String domicilio, String nif, boolean premium) throws Exception {
        Cliente cliente;
        if (premium) {
            cliente = new ClientePremium(email, nombre, domicilio, nif, ClientePremium.DESCUENTO_ENVIO_PREMIUM, ClientePremium.CUOTA_ANUAL_PREMIUM);
        } else {
            cliente = new ClienteStandar(email, nombre, domicilio, nif, ClienteStandar.DESCUENTO_ENVIO_STANDAR);
        }
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

        // 1. CLIENTES
        anadirClienteSeguro("maria@mail.com", "Maria", "Calle Sol 1", "12345678A", false);
        anadirClienteSeguro("thabata@mail.com", "Thabata", "Av. Luna 2", "23456789B", false);
        anadirClienteSeguro("kevin@mail.com", "Kevin", "Plaza Mar 3", "34567890C", false);
        anadirClienteSeguro("mar@mail.com", "Mar", "Calle Río 4", "45678901D", true);
        anadirClienteSeguro("anna@mail.com", "Anna", "Av. Monte 5", "56789012E", true);

        // 2. ARTÍCULOS (ACTIVOS)
        anadirArticuloSeguro("A001", "Laptop Pro 16", 1499.99, 15.0, 120);
        anadirArticuloSeguro("A002", "Mouse Inalámbrico", 35.5, 5.0, 10);
        anadirArticuloSeguro("A003", "Teclado Mecánico RGB", 110.0, 10.0, 30);
        anadirArticuloSeguro("A004", "Monitor Curvo 32", 450.0, 20.0, 180);
        anadirArticuloSeguro("A005", "Silla Ergonómica Pro", 220.0, 30.0, 60);

        // 3. PEDIDOS (ACTIVOS)
        anadirPedidoSeguro("P001", "maria@mail.com", "A002", 2);
        anadirPedidoSeguro("P002", "mar@mail.com", "A003", 1);
        anadirPedidoSeguro("P003", "thabata@mail.com", "A001", 1);

        anadirPedidoSeguro("P004", "anna@mail.com", "A004", 1);
        marcarPedidoComoEnviadoSeguro("P004");

        anadirPedidoSeguro("P005", "kevin@mail.com", "A005", 1);
        marcarPedidoComoEnviadoSeguro("P005");

        System.out.println("Datos de prueba cargados correctamente.");
    }

    // --- MÉTODOS AUXILIARES PARA EVITAR ERRORES POR DUPLICADOS ---

    private void anadirClienteSeguro(String email, String nombre, String dom, String nif, boolean prem) {
        try {
            anadirCliente(email, nombre, dom, nif, prem);
        } catch (Exception e) { /* Ignorar si ya existe */ }
    }

    private void anadirArticuloSeguro(String cod, String desc, Double precio, Double gastos, int tiempo) {
        try {
            anadirArticulo(cod, desc, precio, gastos, tiempo);
        } catch (Exception e) { /* Ignorar si ya existe */ }
    }

    private void anadirPedidoSeguro(String num, String mail, String codArt, int cant) {
        try {
            anadirPedido(num, mail, codArt, cant);
        } catch (Exception e) { /* Ignorar si ya existe o faltan datos */ }
    }

    private void marcarPedidoComoEnviadoSeguro(String num) {
        try {
            marcarPedidoComoEnviado(num);
        } catch (Exception e) { /* Ignorar si falla */ }
    }
}
