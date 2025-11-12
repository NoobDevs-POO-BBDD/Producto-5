package com.tiendaonline.view;

import com.tiendaonline.controller.Controlador;
import com.tiendaonline.model.Pedido;
import com.tiendaonline.view.VistaUtil;

import java.util.List;
import java.util.Scanner;

public class VistaPedido {

    private Controlador controlador;
    private Scanner teclado;
    private VistaUtil vistaUtil;

    public VistaPedido(Controlador controlador, Scanner teclado, VistaUtil vistaUtil){
        this.controlador = controlador;
        this.teclado = teclado;
        this.vistaUtil = vistaUtil;
    }

    //METODOS PREGUNTA AL CONTROLADOR
    /**
     * Esta función gestiona los pedidos, añade un nuevo pedido (sin solicitar fecha ni estado), elimina pedidos con el
     * nuemro de pedido, muestra los pedidos pendientes y los pedidos enviados.
     */
    public void gestionarPedidos() throws Exception {
        System.out.println("\n ¿Qué acción deseas realizar?: ");
        System.out.println("1. Añadir nuevo pedido");
        System.out.println("2. Eliminar pedido");
        System.out.println("3. Mostrar pedidos pendientes");
        System.out.println("4. Mostrar pedidos enviados");
        int opcion = vistaUtil.askNumero(4);

        switch (opcion){
            case 1:
                System.out.println("---- Añadir nuevo pedido ----");
                System.out.print("Introduce el número de pedido >> ");
                String numeroPedido = teclado.nextLine();
                System.out.print("Introduce el email del cliente >> ");
                String cliente = teclado.nextLine();
                System.out.print("Introduce el código del artículo >> ");
                String articulo = teclado.nextLine();
                int cantidad = vistaUtil.askInt("Introduce la cantidad >> ");
                //Aquí
                controlador.solicitarAnadirPedido(numeroPedido,cliente,articulo,cantidad);
                break;
            case 2:
                System.out.print("Introduce el número de pedido >> ");
                String numeroPedidoBorrar = teclado.nextLine();

                controlador.solicitarEliminarPedido(numeroPedidoBorrar);
                break;
            case 3:
                if (vistaUtil.askConfirmacion("¿Desea filtrar por cliente?")){
                    System.out.print("Introduce el email del cliente >> ");
                    String emailCliente = teclado.nextLine();
                    controlador.solicitarMostrarPedidosPendientesEmail(emailCliente);
                }else{
                    controlador.solicitarMostrarPedidosPendientes();
                }
                break;
            case 4:
                if(vistaUtil.askConfirmacion("¿Desea filtrar por cliente?")){
                    System.out.print("Introduce el email del cliente >> ");
                    String emailCliente = teclado.nextLine();
                    controlador.solicitarMostrarPedidosEnviadosEmail(emailCliente);
                }else {
                    controlador.solicitarMostrarPedidosEnviados();
                }

                break;

        }
    }

    //METODOS RESPUESTA DEL CONTROLADOR

    //Gestión Pedidos

    /**
     * mensaje de pedido añadido correctamente
     */
    public void pedidoAnadido(){
        System.out.println("Pedido añadido correctamente.");
    }

    /**
     * mensaje de pedido eliminado correctamente
     */
    public void pedidoEliminado(){
        System.out.println("Pedido eliminado correctamente.");
    }

    /**
     * muestra los pedidos pendientes si hay.
     * @param pedidosPendientes
     */
    public void mostrarListaPedidosPendientes(List<Pedido> pedidosPendientes){
        if (pedidosPendientes.isEmpty()){
            System.out.println("No hay pedidos pendientes.");
        }else{
            System.out.println("Listado de pedidos pendientes: ");

            for (Pedido pedido : pedidosPendientes){
                System.out.println(pedido.toString());
            }
        }
    }

    /**
     * muestra los pedidos enviados si hay.
     * @param pedidosEnviados
     */
    public void mostrarListaPedidosEnviados(List<Pedido> pedidosEnviados){
        if (pedidosEnviados.isEmpty()){
            System.out.println("No hay pedidos enviados.");
        }else{
            System.out.println("Listado de pedidos enviados: ");

            for (Pedido pedido : pedidosEnviados){
                System.out.println(pedido.toString());
            }
        }
    }
}
