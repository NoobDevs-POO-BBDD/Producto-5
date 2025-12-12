package com.tiendaonline.controller;

import com.tiendaonline.model.Cliente; // Importar la clase Cliente del modelo
import com.tiendaonline.model.TiendaOnline; // Importar la clase principal del modelo

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

// Implementamos Initializable para configurar la tabla y listeners al inicio
public class ClienteController implements Initializable {

    private TiendaOnline modelo;
    private ObservableList<Cliente> observableClientes;

    // === Inyecciones FXML de la Tabla y Filtros ===
    @FXML private TableView<Cliente> clientesTableView;
    @FXML private TableColumn<Cliente, String> nifColumn;
    @FXML private TableColumn<Cliente, String> nombreColumn;
    @FXML private TableColumn<Cliente, String> emailColumn;
    @FXML private TableColumn<Cliente, String> domicilioColumn;
    @FXML private TableColumn<Cliente, String> tipoColumn;
    @FXML private ComboBox<String> filtroComboBox;

    // === Inyecciones FXML del Formulario ===
    @FXML private TextField nifField;
    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private TextField domicilioField;
    @FXML private CheckBox premiumCheckBox;
    @FXML private Label cuotaLabel;
    @FXML private TextField cuotaField;

    // --- Configuración Inicial ---

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // 1. Configurar las Columnas para vincularlas a las propiedades del objeto Cliente
        nifColumn.setCellValueFactory(new PropertyValueFactory<>("nif"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        domicilioColumn.setCellValueFactory(new PropertyValueFactory<>("domicilio"));

        // Asumiendo que tu clase Cliente tiene un método que devuelve el tipo ("Estándar" o "Premium")
        // Si usaste la estructura sugerida, puede ser "tipoCliente" o similar. Ajusta si es necesario.
        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoCliente"));

        // 2. Configurar el ComboBox de Filtrado
        filtroComboBox.getItems().addAll("Todos", "Estándar", "Premium");
        filtroComboBox.setValue("Todos");

        // 3. Implementar la Lógica Visual: Mostrar/Ocultar Cuota
        // Ocultar al inicio:
        cuotaField.setVisible(false);
        cuotaLabel.setVisible(false);

        // Añadir Listener: Muestra la cuota solo si el CheckBox Premium está seleccionado
        premiumCheckBox.selectedProperty().addListener((obs, oldV, newV) -> {
            // newV es true si está seleccionado (Premium)
            cuotaField.setVisible(newV);
            cuotaLabel.setVisible(newV);

            // Si deseleccionamos, limpiamos el campo de cuota para evitar errores
            if (!newV) {
                cuotaField.clear();
            }
        });

        // 4. Asignar el listener de filtrado al ComboBox (cuando el valor cambia)
        filtroComboBox.setOnAction(this::onFiltrarClientes);
    }

    // --- Dependencia del Modelo ---

    public void setModelo(TiendaOnline modelo) {
        this.modelo = modelo;
        refrescarTabla(); // Cargar datos nada más entrar
    }

    // --- MÉTODOS DEL FXML (Eventos de Botones y Filtros) ---

    @FXML
    public void onBotonAnadirClienteClick(ActionEvent event) {
        String nif = nifField.getText();
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String domicilio = domicilioField.getText();
        boolean isPremium = premiumCheckBox.isSelected();
        String cuotaTexto = cuotaField.getText();

        // 1. Validar campos básicos
        if (nif.isEmpty() || nombre.isEmpty() || email.isEmpty() || domicilio.isEmpty()) {
            System.err.println("Error: Todos los campos básicos deben estar rellenos.");
            // En una aplicación real, usarías una alerta JavaFX (Dialogs/Alerts)
            return;
        }

        try {
            // 2. Manejar la lógica de Premium y Cuota
            double cuota = 0.0;
            if (isPremium) {
                if (cuotaTexto.isEmpty()) {
                    System.err.println("Error: El cliente es Premium, debe introducir una cuota.");
                    return;
                }
                // Intenta parsear la cuota
                cuota = Double.parseDouble(cuotaTexto);
            }

            // 3. Llamar a la lógica de negocio (asumiendo que TiendaOnline tiene este método)
            // NOTA: Tu VistaCliente anterior llamaba a "controlador.solicitarAnadirCliente(...)"
            // Aquí, llamamos directamente al modelo, que debe manejar la creación del objeto.
            modelo.anadirCliente(email, nombre, domicilio, nif, isPremium, cuota);

            // 4. Actualizar UI y limpiar formulario
            limpiarFormulario();
            refrescarTabla();
            System.out.println("Cliente añadido con éxito.");

        } catch (NumberFormatException e) {
            System.err.println("Error: La cuota debe ser un número válido.");
        } catch (Exception e) {
            // Capturar errores del modelo (ej. NIF/Email duplicado, etc.)
            System.err.println("Error al añadir cliente: " + e.getMessage());
        }
    }

    @FXML
    public void onFiltrarClientes(ActionEvent event) {
        String filtroSeleccionado = filtroComboBox.getValue();
        List<Cliente> clientesFiltrados;

        // 1. Obtener la lista filtrada del modelo
        switch (filtroSeleccionado) {
            case "Estándar":
                // Asumiendo que tu modelo tiene un método para obtener solo Estándar
                clientesFiltrados = modelo.getListaClientesEstandar();
                break;
            case "Premium":
                // Asumiendo que tu modelo tiene un método para obtener solo Premium
                clientesFiltrados = modelo.getListaClientesPremium();
                break;
            case "Todos":
            default:
                clientesFiltrados = modelo.getListaClientes();
                break;
        }

        // 2. Actualizar la TableView
        observableClientes.setAll(clientesFiltrados);
        clientesTableView.setItems(observableClientes);
    }

    // --- MÉTODOS AUXILIARES ---

    private void refrescarTabla() {
        if (modelo != null) {
            // Obtener la lista completa de clientes del modelo
            List<Cliente> clientes = modelo.getListaClientes();

            // Inicializar ObservableList si es la primera vez, o actualizar si ya existe
            if (observableClientes == null) {
                observableClientes = FXCollections.observableArrayList(clientes);
                clientesTableView.setItems(observableClientes);
            } else {
                observableClientes.setAll(clientes);
            }
        }
    }

    private void limpiarFormulario() {
        nifField.clear();
        nombreField.clear();
        emailField.clear();
        domicilioField.clear();
        premiumCheckBox.setSelected(false);
        // cuotaField se limpia automáticamente si la casilla premium se deselecciona por el listener,
        // pero lo hacemos explícitamente por si acaso.
        cuotaField.clear();
    }
}