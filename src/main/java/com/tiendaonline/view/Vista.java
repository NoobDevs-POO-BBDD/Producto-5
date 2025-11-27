package com.tiendaonline.view;

import com.tiendaonline.controller.Controlador;
import java.util.Scanner;
/*
 * Clase destinada a ser el menú principal
 */
public class Vista {
    private Controlador controlador;
    private Scanner teclado;

    private VistaCliente vistaCliente;
    private VistaArticulo vistaArticulo;
    private VistaUtil vistaUtil;
    private VistaPedido vistaPedido;

    public Vista(){
        this.teclado = new Scanner(System.in);
    }

    //Método para que el Main pase el controlador a Vista
    public void setControlador(Controlador controlador){

        this.controlador = controlador;

        this.vistaUtil = new VistaUtil(controlador,teclado);
        this.vistaPedido = new VistaPedido(controlador,teclado, vistaUtil);
        this.vistaArticulo = new VistaArticulo(controlador,teclado, vistaUtil);
        this.vistaCliente = new VistaCliente(controlador,teclado, vistaUtil);

        controlador.setVistas(vistaCliente,vistaPedido,vistaArticulo, vistaUtil);
    }

    public void menu() throws Exception {
        boolean salir = false;

        int opcion;
        System.out.println("""
                                                                                                                     ██████
                                                                                                               █████████████                                                                                  \s
                                                                                                         ████████████    ███
                                                                                  █████████        ████████████          ████
                               ████████╗██╗███████╗███╗   ██╗██████╗  █████╗      ██████████  ███████████                ████
                               ╚══██╔══╝██║██╔════╝████╗  ██║██╔══██╗██╔══██╗          ██████████                      ████
                                  ██║   ██║█████╗  ██╔██╗ ██║██║  ██║███████║           █████                          ████
                                  ██║   ██║██╔══╝  ██║╚██╗██║██║  ██║██╔══██║             ████                         ████
                                  ██║   ██║███████╗██║ ╚████║██████╔╝██║  ██║              ████                      █████
                                  ╚═╝   ╚═╝╚══════╝╚═╝  ╚═══╝╚═════╝ ╚═╝  ╚═╝               █████              ██████████
                                ██████╗ ███╗   ██╗██╗     ██╗███╗   ██╗███████╗                 █████       █████████████
                               ██╔═══██╗████╗  ██║██║     ██║████╗  ██║██╔════╝                  █████████████████  █████████
                               ██║   ██║██╔██╗ ██║██║     ██║██╔██╗ ██║█████╗                      █████████████    ██    ███
                               ██║   ██║██║╚██╗██║██║     ██║██║╚██╗██║██╔══╝                            █████████  ███   ███
                               ╚██████╔╝██║ ╚████║███████╗██║██║ ╚████║███████╗                          ██    ███   ██████
                                                                                                         ███   ███
                                                                                                           █████
                    """);
        do{
            System.out.println("\n---- MENÚ DE OPCIONES: ----");
            System.out.println("1.  Gestionar clientes");  // añadir cliente, mostrar clientes, mostrar clientes estandar, mostrar clientes premium
            System.out.println("2.  Gestionar Artículos"); //añadir artículo, mostrar artículo
            System.out.println("3.  Gestionar Pedidos"); // añadir pedido, eliminar pedido, mostrar pedidos pendientes, mostrar pedidos enviados.
            System.out.println("0.  Salir de la aplicación");
            opcion = vistaUtil.askNumero(4);

            switch (opcion){
                case 1:
                    vistaCliente.gestionarClientes();
                    break;
                case 2:
                    vistaArticulo.gestionarArticulos();
                    break;
                case 3:
                    vistaPedido.gestionarPedidos();
                    break;
                case 0:
                    salir = true;
                    break;
            }

        }while(!salir);
    }




}