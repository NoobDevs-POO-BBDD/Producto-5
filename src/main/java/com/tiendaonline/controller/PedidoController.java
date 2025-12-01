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

    // --- ELEMENTOS DE LA VISTA (FXML), SE DEBEN USAR LOS MISMOS ID EN LA VISTA ---
    @FXML private TableView<Pedido> tablaPedidos;
    @FXML private TableColumn<Pedido, String> colNumero;
    @FXML private TableColumn<Pedido, String> colFecha;
    @FXML private TableColumn<Pedido, String> colClienteEmail;
    @FXML private TableColumn<Pedido, String> colArticuloCodigo;
    @FXML private TableColumn<Pedido, Integer> colCantidad;
    @FXML private TableColumn<Pedido, Double> colTotal;
    @FXML private TableColumn<Pedido, String> colEstado;

    //Formulario de Alta
    @FXML private ComboBox<Cliente> comboClientes;
    @FXML private ComboBox<Articulo> comboArticulos;
    @FXML private TextField txtNumeroPedido;
    @FXML private TextField txtCantidad;

    // Filtros
    @FXML private TextField txtFiltroEmail;
    @FXML private RadioButton radioTodos;
    @FXML private RadioButton radioPendientes;
    @FXML private RadioButton radioEnviados;
    @FXML private ToggleGroup grupoFiltros; // Para agrupar los radio buttons

    /**
     * Método llamado por el MainController al nevegar a esta vista
     * Prepara el entorno grafico
     * @param modelo
     */
    public void setModelo(TiendaOnline modelo) {
        this.modelo = modelo;
        //inicializarCombos(); // Cargar desplegables  DESCOMENTAR CUANDO ESTÉ LA VISTA CON LOS ID
       // configurarColumnasTabla(); //Como se leeran los datos  DESCOMENTAR CUANDO ESTÉ LA VISTA  CON LOS ID
        refrescarTabla();    // Cargar tabla
    }

    // --- ACCIONES PRINCIPALES ---

    @FXML
    public void onBotonAnadirPedidoClick(ActionEvent event) {
        System.out.println("Botón Añadir Pedido pulsado");
        // TODO
        // 1. Obtener Cliente seleccionado del ComboBox.
        // 2. Obtener Artículo seleccionado del ComboBox.
        // 3. Leer cantidad y numero de pedido.
        // 4. Llamar a modelo.anadirPedido(...).
        refrescarTabla();
    }

    @FXML
    public void onBotonEliminarPedidoClick(ActionEvent event) {
        System.out.println("Botón Eliminar pulsado");
        // TODO
        // 1. Obtener el pedido seleccionado en la Tabla.
        // 2. Llamar a modelo.eliminarPedido(id).
        // 3. Gestionar la excepción si el pedido ya está enviado (mostrar Alerta).

        refrescarTabla();
    }

    @FXML
    public void onBotonMarcarEnviadoClick(ActionEvent event) {
        // TODO
        // Lógica para cambiar estado a enviado.

        refrescarTabla();
    }

    @FXML
    public void onFiltrarPedidos(ActionEvent event) {
        System.out.println("Aplicando filtros...");
        // TODO
        // Este método debe combinar los filtros:
        // - ¿Está marcado el CheckBox/Radio "Solo Pendientes"?
        // - ¿Hay texto en el campo "Buscar por Email"?
        // Llamar a modelo.mostrarPedidosPendientes(email) o el que corresponda.

        refrescarTabla();
    }

    // --- INICIALIZACIÓN / CONFIGURACIÓN ---

    /**
     * Convierte las listas de la BBDD en listas visuales para los ComboBox.
     * También define cómo se "pintan" los objetos en el desplegable (ej. Nombre + Email).
     */
    private void inicializarCombos() {
        try {
            // Convertimos las listas normales de Java a ObservableList de JavaFX
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

    /**
     * Define qué atributo del objeto 'Pedido' va en cada columna de la tabla.
     */
    private void configurarColumnasTabla() {
        // Configuración estándar para campos directos
        colNumero.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumeroPedido()));
        colCantidad.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getCantidad()));
        colTotal.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getPrecioTotal()));

        // Configuración para campos anidados (Cliente dentro de Pedido)
        colClienteEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCliente().getEmail()));
        colArticuloCodigo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getArticulo().getCodigo()));

        // Formato de fecha y estado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        colFecha.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaHora().format(formatter)));

        colEstado.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().isEstado() ? "Enviado" : "Pendiente"
        ));
    }

    private void refrescarTabla() {
        if (modelo != null) {
            // TODO: Miembro 4
            // 1. Mirar qué filtros hay puestos (RadioButtons, TextField email)
            // 2. Pedir la lista correcta al modelo (modelo.mostrarPedidosPendientes(), etc.)
            // 3. Actualizar la TableView
            System.out.println("Refrescando tabla pedidos con filtros...");
        }
    }
}