package Presentacio;

import javafx.application.Application;
import javafx.stage.Stage;
import Presentacio.Controladors.ControladorPresentacio;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        ControladorPresentacio c = ControladorPresentacio.getInstance();
        c.setStage(primaryStage);
        c.canviaStage("Login");
    }

    public static void main(String[] args) {
        launch(args);
    }

}