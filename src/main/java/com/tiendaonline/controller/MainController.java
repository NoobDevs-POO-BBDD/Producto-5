package com.tiendaonline.controller;

import com.tiendaonline.model.TiendaOnline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane rootPane;

    // Inyectamos el VBox del centro para poder guardarlo y restaurarlo
    @FXML
    private VBox vistaBienvenida;

    // Inyectamos la etiqueta donde irá el dibujo
    @FXML
    private Label lblLogo;

    private TiendaOnline modelo;

    private VBox vistaInicioGuardada;

    private static final String ASCII_ART = """
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
            """;

    /**
     * Este método se ejecuta automáticamente al cargar el FXML.
     * Sirve para inicializar componentes visuales.
     */
    @FXML
    public void initialize() {
        // Ponemos el dibujo en la etiqueta
        lblLogo.setText(ASCII_ART);

        //Guardamos la "vistaBienvenida" en una variable para no perderla cuando naveguemos a otras pantallas.
        this.vistaInicioGuardada = vistaBienvenida;
    }

    public void setModelo(TiendaOnline modelo) {
        this.modelo = modelo;
    }

    // --- NAVEGACIÓN ---

    @FXML
    public void mostrarVistaInicio() {
        // Restauramos lo que guardamos al principio
        if (vistaInicioGuardada != null) {
            rootPane.setCenter(vistaInicioGuardada);
        }
    }

    @FXML
    public void mostrarVistaClientes() {
        cargarVista("/view/ClientesView.fxml", "Clientes");
    }

    @FXML
    public void mostrarVistaArticulos() {
        cargarVista("/view/ArticulosView.fxml", "Artículos");
    }

    @FXML
    public void mostrarVistaPedidos() {
        cargarVista("/view/PedidosView.fxml", "Pedidos");
    }

    @FXML
    public void salirAplicacion() {
        // Cierra la aplicación JavaFX completamente
        Platform.exit();
        System.exit(0);
    }

    /**
     * Metodo auxiliar para no repetir código try-catch en cada botón
     */
    private void cargarVista(String rutaFXML, String nombreVista) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent vista = loader.load();
            Object controller = loader.getController();
            if (controller instanceof ClienteController) {
                ((ClienteController) controller).setModelo(this.modelo);
            } else if (controller instanceof ArticuloController) {
                ((ArticuloController) controller).setModelo(this.modelo);
            } else if (controller instanceof PedidoController) {
                ((PedidoController) controller).setModelo(this.modelo);
            }

            rootPane.setCenter(vista);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaError("Error de Navegación", "No se pudo cargar la vista de " + nombreVista);
        }
    }

    // --- UTILIDADES ---
    public void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}