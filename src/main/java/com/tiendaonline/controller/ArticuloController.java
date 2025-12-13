package com.tiendaonline.controller;

import com.tiendaonline.model.Articulo;
import com.tiendaonline.model.TiendaOnline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.Optional;

public class ArticuloController {

    // --- 1. Elementos de la VISTA ---
    @FXML private TextField txtCodigo;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;
    @FXML private TextField txtGastos;
    @FXML private TextField txtTiempo;

    @FXML private TableView<Articulo> tablaArticulos;
    @FXML private TableColumn<Articulo, String> colCodigo;
    @FXML private TableColumn<Articulo, String> colDescripcion;
    @FXML private TableColumn<Articulo, Double> colPrecio;
    @FXML private TableColumn<Articulo, Double> colGastos;
    @FXML private TableColumn<Articulo, Integer> colTiempo;

    // --- 2. Modelo de Datos ---
    private TiendaOnline tienda;

    public void setModelo(TiendaOnline tienda) {
        this.tienda = tienda;
        actualizarTabla();
    }

    @FXML
    public void initialize() {
        // Enlazamos columnas
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioVenta"));
        colGastos.setCellValueFactory(new PropertyValueFactory<>("gastosEnvio"));
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempoPreparacion"));

        // MEJORA: Al hacer clic en una fila de la tabla, rellenamos el formulario automáticamente
        tablaArticulos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                cargarArticuloEnFormulario(newSelection);
            }
        });
    }

    // --- ACCIONES DE LOS BOTONES ---

    @FXML
    public void onBuscarClick() {
        String codigo = txtCodigo.getText();
        if (codigo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Escribe un código para buscar.");
            return;
        }

        try {
            Articulo articulo = tienda.buscarArticulo(codigo);
            if (articulo != null) {
                cargarArticuloEnFormulario(articulo);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Encontrado", "Datos cargados en el formulario.");
            } else {
                mostrarAlerta(Alert.AlertType.WARNING, "No encontrado", "No existe ningún artículo con código: " + codigo);
                limpiarFormulario(); // Limpiamos para evitar confusiones
                txtCodigo.setText(codigo); // Dejamos el código que el usuario escribió
            }
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "Error al buscar: " + e.getMessage());
        }
    }

    @FXML
    public void onAnadirClick() {
        if (tienda == null) return;

        try {
            if (txtCodigo.getText().isEmpty() || txtDescripcion.getText().isEmpty()) {
                mostrarAlerta(Alert.AlertType.WARNING, "Datos Incompletos", "El código y la descripción son obligatorios.");
                return;
            }

            tienda.anadirArticulo(
                    txtCodigo.getText(),
                    txtDescripcion.getText(),
                    Double.parseDouble(txtPrecio.getText()),
                    Double.parseDouble(txtGastos.getText()),
                    Integer.parseInt(txtTiempo.getText())
            );

            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Artículo guardado correctamente.");
            limpiarFormulario();
            actualizarTabla();

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Formato Incorrecto", "Precio, Gastos y Tiempo deben ser números.");
        } catch (IllegalArgumentException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Duplicado", e.getMessage());
        } catch (Exception e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", e.getMessage());
        }
    }

    @FXML
    public void onEliminarClick() {
        String codigo = txtCodigo.getText();

        if (codigo.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Aviso", "Selecciona un artículo de la tabla o escribe su código para eliminarlo.");
            return;
        }

        // Confirmación de seguridad
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar Artículo");
        confirm.setHeaderText(null);
        confirm.setContentText("¿Estás seguro de que quieres eliminar el artículo " + codigo + "?");

        Optional<ButtonType> resultado = confirm.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                tienda.eliminarArticulo(codigo);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Eliminado", "Artículo eliminado correctamente.");
                limpiarFormulario();
                actualizarTabla();
            } catch (Exception e) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo eliminar: " + e.getMessage());
            }
        }
    }

    @FXML
    public void onLimpiarClick() {
        limpiarFormulario();
    }

    // --- MÉTODOS AUXILIARES ---

    private void cargarArticuloEnFormulario(Articulo a) {
        txtCodigo.setText(a.getCodigo());
        txtDescripcion.setText(a.getDescripcion());
        txtPrecio.setText(String.valueOf(a.getPrecioVenta()));
        txtGastos.setText(String.valueOf(a.getGastosEnvio()));
        txtTiempo.setText(String.valueOf(a.getTiempoPreparacion()));
    }

    private void actualizarTabla() {
        try {
            if (tienda != null) {
                ObservableList<Articulo> lista = FXCollections.observableArrayList(tienda.mostrarArticulos());
                tablaArticulos.setItems(lista);
            }
        } catch (Exception e) {
            System.err.println("Error al refrescar tabla: " + e.getMessage());
        }
    }

    private void limpiarFormulario() {
        txtCodigo.clear();
        txtDescripcion.clear();
        txtPrecio.clear();
        txtGastos.clear();
        txtTiempo.clear();
        txtCodigo.requestFocus();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}