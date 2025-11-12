
package main.java.com.tiendaonline.controller;

import com.tiendaonline.model.Articulo;
import com.tiendaonline.model.Cliente;
import com.tiendaonline.model.Pedido;
import com.tiendaonline.model.TiendaOnline;
import com.tiendaonline.view.Vista;

import java.sql.SQLException;
import java.util.List;

public class Controlador {
    private Vista vista;
    private TiendaOnline modelo;

    //Constructor
    public Controlador(Vista vista, TiendaOnline modelo){
        this.vista = vista;
        this.modelo = modelo;
    }

    // Gestión de clientes.
    public void solicitarAnadirCliente(String email, String nombre, String domicilio, String nif, Boolean premium){
        try {
            modelo.anadirCliente(email, nombre, domicilio, nif,premium);
            vista.clienteAnadido();
        } catch (IllegalArgumentException | SQLException e) {
            vista.mostrarError(e.getMessage());
        } catch (Exception e) {
            vista.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    public void solicitarMostrarClientes() throws Exception {
        try{
            List<Cliente> listaDeClientes = modelo.mostrarClientes();
            vista.mostrarListaClientes(listaDeClientes);
        }catch (Exception e) {
            vista.mostrarError("Error inseperado: "+ e.getMessage());
        }

    }

    public void solicitarMostrarClientesEstandar() throws Exception {
        try {
            List<Cliente> listaDeClientesEstandar = modelo.mostrarClientesEstandar();
            vista.mostarListaClientesEstandar(listaDeClientesEstandar);
        }catch (Exception e) {
            vista.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    public void solicitarMostrarClientesPremium() throws Exception {
        try{
            List<Cliente> listaDeClientesPremium = modelo.mostrarClientesPremium();
            vista.mostrarListaClientesPremium(listaDeClientesPremium);
        }catch (Exception e) {
            vista.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    //Gestión de artículos

    public void solicitarAnadirArticulo(String codigo, String descripcion, double precioVenta, double gastosEnvio, int tiempoPreparacion){
        try {
            modelo.anadirArticulo(codigo,descripcion,precioVenta,gastosEnvio,tiempoPreparacion);
            vista.articuloAnadido();
        } catch (SQLException e){
            vista.mostrarError(e.getMessage());
        }catch (Exception e) {
            vista.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    public void solicitarMostrarArticulos()throws Exception{
        try{
            List<Articulo> listaArticulo = modelo.mostrarArticulos();
            vista.mostrarListaArticulos(listaArticulo);
        }catch (Exception e) {
            vista.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    public void solicitarBuscarArticulo(String codigoBuscar) throws Exception{
        try {
            Articulo articuloBuscado = modelo.buscarArticulo(codigoBuscar);
            vista.articuloBuscado(articuloBuscado);
        }catch (Exception e) {
            vista.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    //Gestión de pedidos.

    public void solicitarAnadirPedido(String numeroPedido,String cliente,String articulo,int cantidad) throws Exception {
        try {
            modelo.anadirPedido(numeroPedido,cliente,articulo,cantidad);
            vista.pedidoAnadido();
        } catch (IllegalArgumentException e) {
            String error = e.getMessage();
            if (error != null && error.startsWith("No existe el cliente con email")){
                vista.mostrarError(error);
                vista.solicitarDatosNuevoCliente(cliente);
                Cliente c = modelo.buscarClientePorEmail(cliente);

                if (c !=null ){
                    solicitarAnadirPedido(numeroPedido, cliente, articulo, cantidad);
                }else {
                    vista.mostrarError("No se pudo registrar el cliente. Pedido cancelado.");
                }
            }else{
                vista.mostrarError(e.getMessage());
            }
        } catch (SQLException e){
            vista.mostrarError(e.getMessage());
        }catch (Exception e) {
            vista.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    public void solicitarEliminarPedido(String numeroPedidoBorrar) throws Exception {
        try{
            boolean verdadero = modelo.eliminarPedido(numeroPedidoBorrar);
            if(verdadero){
                List<Pedido> listaActualizada = modelo.mostrarPedidosPendientes();
                vista.mostrarListaPedidosPendientes(listaActualizada);
                vista.pedidoEliminado();
            } else {
                String message = "No se pudo eliminar el pedido " + numeroPedidoBorrar + ". Es posible que ya esté enviado o se ha eliminado con anterioridad.";
                vista.mostrarError(message);
            }
        } catch (SQLException e){
            vista.mostrarError(e.getMessage());
        }catch (Exception e) {
            vista.mostrarError("Error inseperado: "+ e.getMessage());
        }

    }

    public void solicitarMostrarPedidosPendientes() throws Exception {
        try{
            List<Pedido> pendientes = modelo.mostrarPedidosPendientes();
            vista.mostrarListaPedidosPendientes(pendientes);
        }catch (Exception e) {
            vista.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    public void solicitarMostrarPedidosPendientesEmail(String emailCliente) throws Exception {
        try{
            List<Pedido> pendientesEmail = modelo.mostrarPedidosPendientes(emailCliente);
            vista.mostrarListaPedidosPendientes(pendientesEmail);
        }catch (Exception e) {
            vista.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    public void solicitarMostrarPedidosEnviados() throws Exception {
        try{
            List<Pedido> enviados = modelo.mostrarPedidosEnviados();
            vista.mostrarListaPedidosEnviados(enviados);
        }catch (Exception e) {
            vista.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }
    public void solicitarMostrarPedidosEnviadosEmail(String emailCliente) throws Exception {
        try{
            List<Pedido> enviadosEmail = modelo.mostrarPedidosEnviados(emailCliente);
            vista.mostrarListaPedidosEnviados(enviadosEmail);
        }catch (Exception e) {
            vista.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    /**
     * Se inicia la aplicación llamando a la función principal de vista
     */
    public void iniciar() throws Exception {

        try{
            modelo.cargarDatosDePrueba();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        vista.menu();
    }

}
