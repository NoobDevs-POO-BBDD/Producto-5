package com.tiendaonline.controller;

import com.tiendaonline.model.TiendaOnline;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;

public class ArticuloController {

    private TiendaOnline modelo;

    public void setModelo(TiendaOnline modelo) {
        this.modelo = modelo;
        refrescarTabla(); // Cargar datos nada más entrar
    }


    // --- MÉTODOS DEL FXML ---
    @FXML
    public void onBotonAnadirArticuloClick(ActionEvent event) {
        System.out.println("Botón Añadir Artículo pulsado");
        // TODO
        // 1. Leer campos (Código, Descripción, Precio...).
        // 2. Convertir texto a números (Double.parseDouble) controlando errores.
        // 3. Llamar a modelo.anadirArticulo(...).
        // 4. Capturar excepción si el código ya existe.

        refrescarTabla();
    }

    @FXML
    public void onBotonBuscarArticuloClick(ActionEvent event) {
        // TODO
        // Lógica para buscar un artículo por código y mostrarlo.
    }

    // --- AUXILIARES ---

    private void refrescarTabla() {
        if (modelo != null) {
            // TODO: Miembro 3
            // List<Articulo> lista = modelo.mostrarArticulos();
            // tablaArticulos.setItems(FXCollections.observableArrayList(lista));
            System.out.println("Refrescando tabla artículos...");
        }
    }
}