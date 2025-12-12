package com.tiendaonline;

import com.tiendaonline.controller.MainController;
import com.tiendaonline.model.TiendaOnline;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        TiendaOnline modelo = new TiendaOnline();
        try {
            modelo.cargarDatosDePrueba();
        } catch (Exception e) {
            System.err.println("Aviso: Datos ya existentes o error al cargar.");
        }

        // Cargamos la vista principal
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/MainView.fxml"));
        Parent root = fxmlLoader.load();

        MainController controller = fxmlLoader.getController();
        controller.setModelo(modelo);

        // Preparamos la Escena
        scene = new Scene(root, 800, 600);

        // --- AQUÍ CARGAMOS EL CSS ---
        try {
            // Asegúrate de que estilos.css esté en la carpeta src/main/resources
            String css = this.getClass().getResource("/estilos.css").toExternalForm();
            scene.getStylesheets().add(css);
        } catch (NullPointerException e) {
            System.err.println("¡ERROR! No se encontró el archivo 'estilos.css' en la carpeta resources.");
        }
        // ---------------------------

        stage.setTitle("Tienda Online - Gestión MVC");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}