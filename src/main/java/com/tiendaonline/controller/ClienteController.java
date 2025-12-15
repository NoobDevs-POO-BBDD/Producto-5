package com.tiendaonline.controller;

import com.tiendaonline.model.TiendaOnline;
import javafx.event.ActionEvent; // Necesaria si usas onBotonAnadirClienteClick(ActionEvent event)
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class ClienteController {

    // === Inyecciones FXML de la Vista ===
    @FXML private TextField nifField;
    @FXML private TextField nombreField;
    @FXML private TextField emailField;
    @FXML private TextField domicilioField;
    @FXML private CheckBox premiumCheckBox;
    @FXML private TextField cuotaField;

    private TiendaOnline modelo;

    // Métodos auxiliares (Asumimos que están implementados)
    public void limpiarFormulario() { /* Implementación para limpiar UI */ }
    public void refrescarTabla() { /* Implementación para actualizar UI */ }
    public void setModelo(TiendaOnline modelo) { this.modelo = modelo; }

    // --- LÓGICA DE AÑADIR CLIENTE ---

    @FXML
    public void onBotonAnadirClienteClick(ActionEvent event) { // Se mantiene ActionEvent si lo usa el FXML, aunque no se use.

        // 1. RECUPERACIÓN DE DATOS DEL FORMULARIO
        String nif = nifField.getText();
        String nombre = nombreField.getText();
        String email = emailField.getText();
        String domicilio = domicilioField.getText();

        boolean isPremium = premiumCheckBox.isSelected();
        String cuotaTexto = cuotaField.getText();

        // 1. Validar campos básicos
        if (nif.isEmpty() || nombre.isEmpty() || email.isEmpty() || domicilio.isEmpty()) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "Todos los campos básicos deben estar rellenos.");
            return;
        }

        try {
            // 2. Manejar la lógica de Premium y Cuota
            int cuota = 0;
            double descuento = 0.0; // Placeholder para cumplir la firma del Modelo

            if (isPremium) {
                if (cuotaTexto.isEmpty()) {
                    mostrarAlerta(Alert.AlertType.ERROR, "Error de Validación", "El cliente es Premium, debe introducir una cuota anual.");
                    return;
                }
                // Intenta parsear la cuota
                cuota = Integer.parseInt(cuotaTexto);
            }

            // 3. Llamar a la lógica de negocio (Modelo/Persistencia)
            // Firma COMPLETA con 7 argumentos: (email, nombre, domicilio, nif, isPremium, descuentoControlador, cuotaControlador)
            modelo.anadirCliente(email, nombre, domicilio, nif, isPremium, descuento, cuota);

            // 4. ÉXITO: Actualizar UI y notificar al usuario
            limpiarFormulario();
            refrescarTabla();
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Cliente añadido con éxito.");

        } catch (NumberFormatException e) {
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Entrada", "La cuota debe ser un número entero válido.");
        } catch (Exception e) {
            // Capturar errores del Modelo (ej. NIF/Email duplicado)
            mostrarAlerta(Alert.AlertType.ERROR, "Error de Persistencia", "Error al añadir cliente: " + e.getMessage());
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}