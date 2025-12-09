package com.tiendaonline.controller;

import com.tiendaonline.model.Articulo;
import com.tiendaonline.model.Cliente;
import com.tiendaonline.model.Pedido;
import com.tiendaonline.model.TiendaOnline;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class PedidoController {

    private TiendaOnline modelo;

    // --- ELEMENTOS DE LA VISTA (FXML) ---
    @FXML private TableView<Pedido> tablaPedidos;
    @FXML private TableColumn<Pedido, String> colNumero;
    @FXML private TableColumn<Pedido, String> colFecha;
    @FXML private TableColumn<Pedido, String> colClienteEmail;
    @FXML private TableColumn<Pedido, String> colArticuloCodigo;
    @FXML private TableColumn<Pedido, Integer> colCantidad;
    @FXML private TableColumn<Pedido, Double> colTotal;
    @FXML private TableColumn<Pedido, String> colEstado;

    // Formulario de Alta
    @FXML private ComboBox<Cliente> comboClientes;
    @FXML private ComboBox<Articulo> comboArticulos;
    @FXML private TextField txtNumeroPedido;
    @FXML private TextField txtCantidad;

    // Filtros
    @FXML private TextField txtFiltroEmail;
    @FXML private RadioButton radioTodos;
    @FXML private RadioButton radioPendientes;
    @FXML private RadioButton radioEnviados;
    @FXML private ToggleGroup grupoFiltros;

    /**
     * Inicializado desde MainController
     */
    public void setModelo(TiendaOnline modelo) {
        this.modelo = modelo;
        inicializarCombos();
        configurarColumnasTabla();
        refrescarTabla();
    }

    // -------------------------
    // ACCIONES
    // -------------------------

    @FXML
    public void onBotonAnadirPedidoClick(ActionEvent event) {
        try {
            Cliente cliente = comboClientes.getValue();
            Articulo articulo = comboArticulos.getValue();
            String numeroPedido = txtNumeroPedido.getText();
            int cantidad = Integer.parseInt(txtCantidad.getText());

            if (cliente == null || articulo == null || numeroPedido.isEmpty() || cantidad <= 0) {
                mostrarAlerta("Campos incompletos", "Debes seleccionar cliente, artículo y cantidad válida.");
                return;
            }

            modelo.anadirPedido(numeroPedido, cliente.getEmail(), articulo.getCodigo(), cantidad);

            refrescarTabla();
            limpiarFormulario();
            mostrarAlerta("Pedido añadido", "El pedido se ha añadido correctamente.");

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "La cantidad debe ser un número entero.");
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    @FXML
    public void onBotonEliminarPedidoClick(ActionEvent event) {
        Pedido seleccionado = tablaPedidos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un pedido para eliminar");
            return;
        }

        try {
            boolean eliminado = modelo.eliminarPedido(seleccionado.getNumeroPedido());
            if (!eliminado) {
                mostrarAlerta("Error", "No se puede eliminar un pedido enviado");
            } else {
                refrescarTabla();
                mostrarAlerta("Éxito", "Pedido eliminado correctamente");
            }
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    @FXML
    public void onBotonMarcarEnviadoClick(ActionEvent event) {
        Pedido seleccionado = tablaPedidos.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Error", "Debe seleccionar un pedido para marcar como enviado");
            return;
        }

        try {
            modelo.marcarPedidoComoEnviado(seleccionado.getNumeroPedido());
            refrescarTabla();
            mostrarAlerta("Éxito", "Pedido marcado como enviado");
        } catch (Exception e) {
            mostrarAlerta("Error", e.getMessage());
        }
    }

    // -------------------------
    // FILTROS
    // -------------------------

    @FXML
    public void onFiltrarPedidos(ActionEvent event) {
        refrescarTabla();
    }

    // -------------------------
    // INICIALIZACIÓN
    // -------------------------

    private void inicializarCombos() {
        try {
            ObservableList<Cliente> clientesOl = FXCollections.observableArrayList(modelo.mostrarClientes());
            ObservableList<Articulo> articulosOl = FXCollections.observableArrayList(modelo.mostrarArticulos());

            comboClientes.setItems(clientesOl);
            comboArticulos.setItems(articulosOl);

            comboClientes.setConverter(new StringConverter<Cliente>() {
                @Override
                public String toString(Cliente c) {
                    return (c != null) ? c.getNombre() + " (" + c.getEmail() + ")" : "";
                }
                @Override
                public Cliente fromString(String string) { return null; }
            });

            comboArticulos.setConverter(new StringConverter<Articulo>() {
                @Override
                public String toString(Articulo a) {
                    return (a != null) ? a.getDescripcion() + " - " + a.getPrecioVenta() + "€" : "";
                }
                @Override
                public Articulo fromString(String string) { return null; }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void configurarColumnasTabla() {
        colNumero.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumeroPedido()));
        colCantidad.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCantidad()));
        colTotal.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrecioTotal()));

        colClienteEmail.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCliente().getEmail()));

        colArticuloCodigo.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getArticulo().getCodigo()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colFecha.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFechaHora().format(formatter)));

        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().isEstado() ? "Enviado" : "Pendiente")
        );
    }

    // -------------------------
    // REFRESCO DE TABLA
    // -------------------------

    private void refrescarTabla() {
        try {
            List<Pedido> pedidos;

            String emailFiltro = txtFiltroEmail.getText().trim();

            if (radioPendientes.isSelected()) {
                pedidos = emailFiltro.isEmpty() ?
                        modelo.mostrarPedidosPendientes() :
                        modelo.mostrarPedidosPendientes(emailFiltro);

            } else if (radioEnviados.isSelected()) {
                pedidos = emailFiltro.isEmpty() ?
                        modelo.mostrarPedidosEnviados() :
                        modelo.mostrarPedidosEnviados(emailFiltro);

            } else { // Todos
                pedidos = modelo.mostrarPedidos();

                if (!emailFiltro.isEmpty()) {
                    pedidos.removeIf(p -> !p.getCliente().getEmail().contains(emailFiltro));
                }
            }

            tablaPedidos.setItems(FXCollections.observableArrayList(pedidos));

        } catch (Exception e) {
            mostrarAlerta("Error al refrescar tabla", e.getMessage());
        }
    }

    // -------------------------
    // AUXILIARES
    // -------------------------

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarFormulario() {
        txtNumeroPedido.clear();
        txtCantidad.clear();
        comboClientes.getSelectionModel().clearSelection();
        comboArticulos.getSelectionModel().clearSelection();
    }
}
