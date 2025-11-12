package com.tiendaonline.view;

import com.tiendaonline.controller.Controlador;
import com.tiendaonline.model.Articulo;
import com.tiendaonline.view.VistaUtil;

import java.util.List;
import java.util.Scanner;

public class VistaArticulo {

    private Controlador controlador;
    private Scanner teclado;
    private VistaUtil vistaUtil;

    public VistaArticulo(Controlador controlador, Scanner teclado, VistaUtil vistaUtil){
        this.controlador = controlador;
        this.teclado = teclado;
        this.vistaUtil = vistaUtil;
    }

    //METODOS PREGUNTA AL CONTROLADOR
    /**
     * En esta función se solicita al usuário si desea añadir nuevo artículo, mostrar todos los artículos o buscar un artículo por código.
     * el controlador recibe una función con los campos necesarios para realizarla.
     */
    public void gestionarArticulos() throws Exception {
        System.out.println("\n ¿Qué acción deseas realizar?: ");
        System.out.println("1. Añadir nuevo artículo");
        System.out.println("2. Mostrar artículos");
        System.out.println("3. Buscar artículo");
        int opcion = vistaUtil.askNumero(3);

        switch (opcion){
            case 1:
                System.out.println("---- Añadir nuevo artículo ----");
                System.out.print("Introduce el código >> ");
                String codigo = teclado.nextLine();
                System.out.print("Introduce la descripción >> ");
                String descripcion = teclado.nextLine();
                double precioVenta = vistaUtil.askDouble("Introduce el precio de venta >> ");
                double gastosEnvio = vistaUtil.askDouble("Introduce el precio de envío >> ");
                int tiempoPreparacion = vistaUtil.askInt("Introduce el tiempo de preparación >> ");

                controlador.solicitarAnadirArticulo(codigo,descripcion,precioVenta,gastosEnvio,tiempoPreparacion);
                break;

            case 2:
                controlador.solicitarMostrarArticulos ();
                break;

            case 3:
                System.out.print("Introduce el código del artículo a buscar >> ");
                String codigoBuscar = teclado.nextLine();

                controlador.solicitarBuscarArticulo(codigoBuscar);
                break;
        }
    }

    //METODOS RESPUESTA DEL CONTROLADOR
    //Gestión Artículos

    /**
     * muestra mensaje de cliente añadido correctamente
     */
    public void articuloAnadido(){
        System.out.println("Artículo añadido correctamente");
    }

    /**
     * mostrar lista de articulos si hay registrados.
     * @param lista
     */
    public void mostrarListaArticulos(List<Articulo> lista){
        if (lista.isEmpty()){
            System.out.println("No hay artículos registrados.");
        }else{
            System.out.println("Listado de artículos: ");

            for (Articulo articulo : lista){
                System.out.println(articulo.toString());
            }
        }
    }

    /**
     * devuelve el artículo buscado por el código o un mensaje de artículo no encontrado.
     * @param articulo
     */
    public void articuloBuscado(Articulo articulo){
        if (articulo == null){
            System.out.println("Artículo no encontrado, revisa el código introducido.");
        }else{
            System.out.println(articulo.toString());
        }
    }
}
