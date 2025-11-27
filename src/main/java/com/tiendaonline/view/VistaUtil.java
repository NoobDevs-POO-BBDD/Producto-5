package com.tiendaonline.view;

import com.tiendaonline.controller.Controlador;

import java.util.Scanner;

public class VistaUtil {

    private Controlador controlador;
    private Scanner teclado;

    public VistaUtil(Controlador controlador, Scanner teclado){
        this.controlador = controlador;
        this.teclado = teclado;
    }

    //OTROS METODOS
    /**
     * Si durante la creación de un pedido el email del cliente no está registrado se pide que registre el usuario
     * @param email
     */
    public void solicitarDatosNuevoCliente(String email){
        System.out.println("\n NUEVO CLIENTE DETECTADO \n");
        System.out.println("Introduce los datos para el cliente con email: "+ email);
        System.out.print("Introduce el nombre >> ");
        String nombre = teclado.nextLine();
        System.out.print("Introduce el domicilio >> ");
        String domicilio = teclado.nextLine();
        System.out.print("Introduce el NIF >> ");
        String nif = teclado.nextLine();

        boolean premium = askConfirmacion("¿El cliente es premium?");

        controlador.solicitarAnadirCliente(email,nombre,domicilio,nif,premium);
    }

    /**
     * muestra el mensaje de error genérico al usuario.
     * El controller llamará a este método cuando ocurrar una excepción.
     * @param message
     */
    public void mostrarError(String message){
        System.out.println("\nERROR:");
        System.out.println(message);
    }

    /**
     * Pide el íncide a buscar, comprueba que este sea un número válido.
     * @param max tamaño máximo de la lista que se quiere recorrer.
     * @return devuelve el índice de la lista introducido.
     */
    public int askNumero(int max){
        boolean valido = false;
        int indice = 0;

        while(!valido){
            System.out.print("\nIntroduce el índice >> ");
            if (teclado.hasNextInt()){
                indice = teclado.nextInt();
                teclado.nextLine();
                valido = (indice >= 0 && indice <= max);

                if (!valido) {
                    System.out.println("El índice debe estar entre 0 y "+ max);
                }
            }else{
                System.out.println("Por favor introduce un índice válido");
                teclado.next();
            }
        }
        return indice;
    }

    /**
     * Pregunta al usuario Si o no devuelve un booleano ture s o S false otro
     * @param pregunta pregunta a responder
     * @return
     */
    public boolean askConfirmacion(String pregunta){
        System.out.print( pregunta + "(S/N) >> ");
        String respuesta = teclado.nextLine();
        return respuesta.equalsIgnoreCase("S");
    }

    /**
     * Se asegura que el Int introducido sea valido
     * @param prompt
     * @return
     */
    public int askInt(String prompt){
        while(true){
            System.out.print(prompt);
            try{
                int valor = Integer.parseInt(teclado.nextLine());
                return valor;
            }catch (NumberFormatException e){
                System.out.println("ERROR: introduce un número entero válido");
            }
        }
    }

    /**
     * se asegura que el double introducido sea válido
     * @param prompt
     * @return
     */
    public double askDouble(String prompt){
        while (true){
            System.out.print(prompt);
            try{
                double valor = Double.parseDouble(teclado.nextLine());
                return valor;
            }catch (NumberFormatException e){
                System.out.println("ERROR: introduce un número decimal válido");
            }
        }
    }

}
