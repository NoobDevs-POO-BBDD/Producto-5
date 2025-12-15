package com.tiendaonline.controller;

import com.tiendaonline.model.TiendaOnline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane; // <--- ¡AÑADE ESTA LÍNEA OBLIGATORIAMENTE!
import javafx.scene.layout.VBox; // Puedes dejarlo o borrarlo si ya no usas VBox

import java.io.IOException;

public class MainController {

    @FXML
    private BorderPane rootPane;
    @FXML
    private StackPane vistaBienvenida;
    private TiendaOnline modelo;
    private StackPane vistaInicioGuardada;

    /**
     * Inicializa el controlador. Se ejecuta automáticamente al cargar la vista.
     */
    @FXML
    public void initialize() {
        vistaBienvenida.setId("vistaBienvenida");
        // Guardamos la vista de bienvenida (el logo) para poder volver a ella
        this.vistaInicioGuardada = vistaBienvenida;
    }

    /**
     * Recibe el modelo de datos (TiendaOnline) desde la clase App.
     */
    public void setModelo(TiendaOnline modelo) {
        this.modelo = modelo;
    }

    // --- MÉTODOS DE NAVEGACIÓN ---

    @FXML
    public void mostrarVistaInicio() {
        if (vistaInicioGuardada != null) {
            rootPane.setCenter(vistaInicioGuardada);
        }
    }

    @FXML
    public void mostrarVistaArticulos() {
        cargarVista("/view/ArticulosView.fxml", "Artículos");
    }

    @FXML
    public void mostrarVistaClientes() {
        cargarVista("/view/ClientesView.fxml", "Clientes");
    }

    @FXML
    public void mostrarVistaPedidos() {
        cargarVista("/view/PedidosView.fxml", "Pedidos");
    }

    @FXML
    public void salirAplicacion() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Método auxiliar para cargar vistas FXML y pasar el modelo al controlador correspondiente.
     */
    private void cargarVista(String rutaFXML, String nombreVista) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFXML));
            Parent vista = loader.load();

            Object controller = loader.getController();


            if (controller instanceof ArticuloController) {
                ((ArticuloController) controller).setModelo(this.modelo);
            }
            else if (controller instanceof ClienteController) {
                ((ClienteController) controller).setModelo(this.modelo);
            }
            else if (controller instanceof PedidoController) {
                ((PedidoController) controller).setModelo(this.modelo);
            }

            rootPane.setCenter(vista);

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlertaError("Error de Navegación", "No se pudo cargar la vista de " + nombreVista + ".\n" +
                    "Verifica que el archivo FXML existe y es correcto.\n" + e.getMessage());
        }
    }

    /**
     * Muestra una alerta de error estándar.
     */
    public void mostrarAlertaError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}