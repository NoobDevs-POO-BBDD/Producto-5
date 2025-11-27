package com.tiendaonline.view;

import com.tiendaonline.controller.Controlador;
import com.tiendaonline.model.Cliente;
import com.tiendaonline.view.VistaUtil;

import java.util.List;
import java.util.Scanner;

public class VistaCliente {

    private Controlador controlador;
    private Scanner teclado;
    private VistaUtil vistaUtil;

    public VistaCliente(Controlador controlador, Scanner teclado, VistaUtil vistaUtil){
        this.controlador = controlador;
        this.teclado = teclado;
        this.vistaUtil = vistaUtil;
    }

    //METODOS PREGUNTA AL CONTROLADOR
    /**
     * Función para gestión de clientes, permite añadir nuevo cliente solicitando los datos, Se pasa un booleano con 1
     * true 0 false si es premium, así se puede añadir la cuota y el descuento. Además se muestra el listado de todos
     * los clientes, listado clientes estándar, listado clientes prémium.
     */
    public void gestionarClientes() throws Exception {
        System.out.println("\n ¿Qué acción deseas realizar?: ");
        System.out.println("1. Añadir nuevo cliente");
        System.out.println("2. Mostrar todos los clientes");
        System.out.println("3. Mostrar los clientes estándar");
        System.out.println("4. Mostrar los clientes prémium");
        int opcion = vistaUtil.askNumero(4);
        switch (opcion){
            case 1:
                System.out.println("---- Añadir nuevo cliente ----");
                System.out.print("Introduce el email >> ");
                String email = teclado.nextLine();
                System.out.print("Introduce el nombre >> ");
                String nombre = teclado.nextLine();
                System.out.print("Introduce el domicilio >> ");
                String domicilio = teclado.nextLine();
                System.out.print("Introduce el NIF >> ");
                String nif = teclado.nextLine();
                boolean premium = vistaUtil.askConfirmacion("¿El cliente es premium?");

                controlador.solicitarAnadirCliente(email, nombre, domicilio, nif,premium);
                break;
            case 2:
                controlador.solicitarMostrarClientes();
                break;
            case 3:
                controlador.solicitarMostrarClientesEstandar();
                break;
            case 4:
                controlador.solicitarMostrarClientesPremium();
                break;
        }
    }


    //METODOS RESPUESTA DEL CONTROLADOR

    //Gestión clientes
    /**
     * muestra mensaje cliente añadido correctamente
     */
    public void clienteAnadido(){
        System.out.println("Cliente añadido correctamente");
    }

    /**
     * muestra mensaje si no hay clientes registrados y lista clientes si hay.
     * @param lista
     */
    public void mostrarListaClientes(List<Cliente> lista){
        if (lista.isEmpty()){
            System.out.println("No hay clientes registrados.");
        }else{
            System.out.println("Listado de clientes: ");
            for ( Cliente cliente : lista ){
                System.out.println(cliente.toString());
            }
        }
    }

    /**
     * muestra mensaje si no hay clientes registrados y lista clientes estandar si hay.
     * @param listaStandar
     */
    public void mostarListaClientesEstandar(List<Cliente> listaStandar){
        if (listaStandar.isEmpty()){
            System.out.println("No hay clientes estándar registrados.");
        }else{
            System.out.println("Listado de clientes estándar: ");
            for ( Cliente cliente : listaStandar){
                System.out.println(cliente.toString());
            }
        }
    }

    /**
     * muestra mensaje si no hay clientes registrados y lista clientes estandar si hay.
     * @param listaPremium
     */
    public void mostrarListaClientesPremium(List<Cliente> listaPremium){
        if (listaPremium.isEmpty()){
            System.out.println("No hay clientes premium registrados.");
        }else{
            System.out.println("Listado de clientes premium: ");
            for ( Cliente cliente : listaPremium){
                System.out.println(cliente.toString());
            }
        }
    }
}
