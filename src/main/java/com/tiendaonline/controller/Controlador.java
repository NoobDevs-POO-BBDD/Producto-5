
package com.tiendaonline.controller;

import com.tiendaonline.model.Articulo;
import com.tiendaonline.model.Cliente;
import com.tiendaonline.model.Pedido;
import com.tiendaonline.model.TiendaOnline;
import com.tiendaonline.view.*;

import java.util.List;

public class Controlador {
    private Vista vista;
    private TiendaOnline modelo;

    private VistaUtil vistaUtil;
    private VistaCliente vistaCliente;
    private VistaArticulo vistaArticulo;
    private VistaPedido vistaPedido;

    //Constructor
    public Controlador(Vista vista, TiendaOnline modelo){
        this.vista = vista;
        this.modelo = modelo;
    }

    //Se usará este metodo en Vista para obtener las subvistas
    public void setVistas(VistaCliente vc, VistaPedido vp, VistaArticulo va, VistaUtil vu){
        this.vistaCliente = vc;
        this.vistaArticulo = va;
        this.vistaPedido = vp;
        this.vistaUtil = vu;
    }

    // Gestión de clientes.
    public void solicitarAnadirCliente(String email, String nombre, String domicilio, String nif, Boolean premium){
        try {
            modelo.anadirCliente(email, nombre, domicilio, nif,premium);
            vistaCliente.clienteAnadido();
        } catch (IllegalArgumentException e) {
            // CAMBIO: Quitamos SQLException porque tu DAO JPA ya no la lanza.
            vistaUtil.mostrarError(e.getMessage());
        } catch (Exception e) {
            // CAMBIO: Corrección ortográfica 'inesperado'
            vistaUtil.mostrarError("Error inesperado: "+ e.getMessage());
        }
    }

    public void solicitarMostrarClientes() throws Exception {
        try{
            List<Cliente> listaDeClientes = modelo.mostrarClientes();
            vistaCliente.mostrarListaClientes(listaDeClientes);
        }catch (Exception e) {
            vistaUtil.mostrarError("Error inseperado: "+ e.getMessage());
        }

    }

    public void solicitarMostrarClientesEstandar() throws Exception {
        try {
            List<Cliente> listaDeClientesEstandar = modelo.mostrarClientesEstandar();
            vistaCliente.mostarListaClientesEstandar(listaDeClientesEstandar);
        }catch (Exception e) {
            vistaUtil.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    public void solicitarMostrarClientesPremium() throws Exception {
        try{
            List<Cliente> listaDeClientesPremium = modelo.mostrarClientesPremium();
            vistaCliente.mostrarListaClientesPremium(listaDeClientesPremium);
        }catch (Exception e) {
            vistaUtil.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    //Gestión de artículos

    public void solicitarAnadirArticulo(String codigo, String descripcion, double precioVenta, double gastosEnvio, int tiempoPreparacion){
        try {
            modelo.anadirArticulo(codigo,descripcion,precioVenta,gastosEnvio,tiempoPreparacion);
            vistaArticulo.articuloAnadido();
        }catch (Exception e) {
            vistaUtil.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    public void solicitarMostrarArticulos()throws Exception{
        try{
            List<Articulo> listaArticulo = modelo.mostrarArticulos();
            vistaArticulo.mostrarListaArticulos(listaArticulo);
        }catch (Exception e) {
            vistaUtil.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    public void solicitarBuscarArticulo(String codigoBuscar) throws Exception{
        try {
            Articulo articuloBuscado = modelo.buscarArticulo(codigoBuscar);
            vistaArticulo.articuloBuscado(articuloBuscado);
        }catch (Exception e) {
            vistaUtil.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    //Gestión de pedidos.

    public void solicitarAnadirPedido(String numeroPedido,String cliente,String articulo,int cantidad) throws Exception {
        try {
            modelo.anadirPedido(numeroPedido,cliente,articulo,cantidad);
            vistaPedido.pedidoAnadido();
        } catch (IllegalArgumentException e) {
            String error = e.getMessage();
            if (error != null && error.startsWith("No existe el cliente con email")){
                vistaUtil.mostrarError(error);
                vistaUtil.solicitarDatosNuevoCliente(cliente);
                Cliente c = modelo.buscarClientePorEmail(cliente);

                if (c !=null ){
                    solicitarAnadirPedido(numeroPedido, cliente, articulo, cantidad);
                }else {
                    vistaUtil.mostrarError("No se pudo registrar el cliente. Pedido cancelado.");
                }
            }else{
                vistaUtil.mostrarError(e.getMessage());
            }
        }catch (Exception e) {
            vistaUtil.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    public void solicitarEliminarPedido(String numeroPedidoBorrar) throws Exception {
        try{
            boolean verdadero = modelo.eliminarPedido(numeroPedidoBorrar);
            if(verdadero){
                List<Pedido> listaActualizada = modelo.mostrarPedidosPendientes();
                vistaPedido.mostrarListaPedidosPendientes(listaActualizada);
                vistaPedido.pedidoEliminado();
            } else {
                String message = "No se pudo eliminar el pedido " + numeroPedidoBorrar + ". Es posible que ya esté enviado o se ha eliminado con anterioridad.";
                vistaUtil.mostrarError(message);
            }
        }catch (Exception e) {
            vistaUtil.mostrarError("Error inseperado: "+ e.getMessage());
        }

    }

    public void solicitarMostrarPedidosPendientes() throws Exception {
        try{
            List<Pedido> pendientes = modelo.mostrarPedidosPendientes();
            vistaPedido.mostrarListaPedidosPendientes(pendientes);
        }catch (Exception e) {
            vistaUtil.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    public void solicitarMostrarPedidosPendientesEmail(String emailCliente) throws Exception {
        try{
            List<Pedido> pendientesEmail = modelo.mostrarPedidosPendientes(emailCliente);
            vistaPedido.mostrarListaPedidosPendientes(pendientesEmail);
        }catch (Exception e) {
            vistaUtil.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }

    public void solicitarMostrarPedidosEnviados() throws Exception {
        try{
            List<Pedido> enviados = modelo.mostrarPedidosEnviados();
            vistaPedido.mostrarListaPedidosEnviados(enviados);
        }catch (Exception e) {
            vistaUtil.mostrarError("Error inseperado: "+ e.getMessage());
        }
    }
    public void solicitarMostrarPedidosEnviadosEmail(String emailCliente) throws Exception {
        try{
            List<Pedido> enviadosEmail = modelo.mostrarPedidosEnviados(emailCliente);
            vistaPedido.mostrarListaPedidosEnviados(enviadosEmail);
        }catch (Exception e) {
            vistaUtil.mostrarError("Error inseperado: "+ e.getMessage());
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
