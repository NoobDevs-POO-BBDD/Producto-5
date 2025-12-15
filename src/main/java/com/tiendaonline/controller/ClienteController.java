package com.tiendaonline.controller;

import com.tiendaonline.model.Cliente;
import com.tiendaonline.model.TiendaOnline;

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

public class ClienteController implements Initializable {

    private TiendaOnline modelo;
    private ObservableList<Cliente> observableClientes;

    @FXML private TableView<Cliente> clientesTableView;
    @FXML private TableColumn<Cliente, String> nifColumn;
    @FXML private TableColumn<Cliente, String> nombreColumn;
    @FXML private TableColumn<Cliente, String> emailColumn;
    @FXML private TableColumn<Cliente, String> domicilioColumn;
    @FXML private TableColumn<Cliente, String> tipoColumn;
    @FXML private ComboBox<String> filtroComboBox;

    @FXML private TextField nifField;
    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private TextField domicilioField;
    @FXML private CheckBox premiumCheckBox;
    @FXML private Label cuotaLabel;
    @FXML private TextField cuotaField;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nifColumn.setCellValueFactory(new PropertyValueFactory<>("nif"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        domicilioColumn.setCellValueFactory(new PropertyValueFactory<>("domicilio"));

        clientesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tipoColumn.setCellValueFactory(new PropertyValueFactory<>("tipoCliente"));

        filtroComboBox.getItems().addAll("Todos", "Estándar", "Premium");
        filtroComboBox.setValue("Todos");

        cuotaField.setVisible(false);
        cuotaLabel.setVisible(false);

        premiumCheckBox.selectedProperty().addListener((obs, oldV, newV) -> {
            cuotaField.setVisible(newV);
            cuotaLabel.setVisible(newV);

            if (!newV) {
                cuotaField.clear();
            }
        });

        filtroComboBox.setOnAction(this::onFiltrarClientes);
    }


    public void setModelo(TiendaOnline modelo) {
        this.modelo = modelo;
        refrescarTabla();
    }


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
            return;
        }

        try {
            double cuota = 0.0;
            if (isPremium) {
                if (cuotaTexto.isEmpty()) {
                    System.err.println("Error: El cliente es Premium, debe introducir una cuota.");
                    return;
                }
                cuota = Double.parseDouble(cuotaTexto);
            }

            modelo.anadirCliente(email, nombre, domicilio, nif, isPremium, cuota);

            limpiarFormulario();
            refrescarTabla();
            System.out.println("Cliente añadido con éxito.");

        } catch (NumberFormatException e) {
            System.err.println("Error: La cuota debe ser un número válido.");
        } catch (Exception e) {
            System.err.println("Error al añadir cliente: " + e.getMessage());
        }
    }

    @FXML
    public void onFiltrarClientes(ActionEvent event) {
        String filtroSeleccionado = filtroComboBox.getValue();
        List<Cliente> clientesFiltrados;

        try {
            switch (filtroSeleccionado) {
                case "Estándar":
                    clientesFiltrados = modelo.mostrarClientesEstandar();
                    break;
                case "Premium":
                    clientesFiltrados = modelo.mostrarClientesPremium();
                    break;
                case "Todos":
                default:
                    clientesFiltrados = modelo.mostrarClientes();
                    break;
            }

            observableClientes.setAll(clientesFiltrados);
            clientesTableView.setItems(observableClientes);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void refrescarTabla() {
        if (modelo != null) {
            List<Cliente> clientes = modelo.getListaClientes();

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

        cuotaField.clear();
    }
}