package com.tiendaonline.controller;

import com.tiendaonline.model.TiendaOnline;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class PedidoController {

    private TiendaOnline modelo;

    public void setModelo(TiendaOnline modelo) {
        this.modelo = modelo;
        inicializarCombos(); // Cargar desplegables
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

    // --- INICIALIZACIÓN ---

    public void inicializarCombos() {
        // TODO
        // Cargar la lista de clientes y artículos en los desplegables.
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