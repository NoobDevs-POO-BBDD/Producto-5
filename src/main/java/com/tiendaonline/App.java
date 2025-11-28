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

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/view/MainView.fxml"));
        Parent root = fxmlLoader.load();

        MainController controller = fxmlLoader.getController();
        controller.setModelo(modelo);

        //Preparamos la Escena y el Escenario (Stage)
        scene = new Scene(root, 800, 600);
        stage.setTitle("Tienda Online - Gestión MVC");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}