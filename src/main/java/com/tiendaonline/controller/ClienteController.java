package com.tiendaonline.controller;

import com.tiendaonline.model.TiendaOnline;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class ClienteController {

    private TiendaOnline modelo;

    public void setModelo(TiendaOnline modelo) {
        this.modelo = modelo;
        refrescarTabla(); // Cargar datos nada más entrar

    }

    // --- MÉTODOS DEL FXML (Eventos de Botones) ---

    @FXML
    public void onBotonAnadirClienteClick(ActionEvent event) {
        System.out.println("Botón Añadir Cliente pulsado");
        // TODO
        // 1. Leer datos de los TextFields (email, nombre, etc.)
        // 2. Validar que no estén vacíos.
        // 3. Llamar a modelo.anadirCliente(...) dentro de un try-catch.
        // 4. Si sale bien, actualizar la tabla.
        refrescarTabla();
    }

    @FXML
    public void onFiltrarClientes(ActionEvent event) {
        // TODO
        // Este método se llamará al cambiar el ComboBox de filtro (Todos/Standard/Premium)
        // Debe obtener la lista adecuada del modelo y actualizar la tabla.
    }

    // --- MÉTODOS AUXILIARES ---

    private void refrescarTabla() {
        // TODO
        // Lógica para volver a pedir la lista al modelo y pintar la TableView
    }
}