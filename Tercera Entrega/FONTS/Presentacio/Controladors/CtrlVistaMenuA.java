package Presentacio.Controladors;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class CtrlVistaMenuA implements Initializable{
    
    @FXML
    private Button btnEntrarDades;

    @FXML
    private Button btnGestionarAlgorismes;

    @FXML
    private Button btnGestionarDistancies;

    @FXML
    private Button btnGestionarItems;

    @FXML
    private Button btnGestionarUsuaris;

    @FXML
    private Button btnGestionarValoracions;

    @FXML
    private Button btnQualitatRecomanacions;

    @FXML
    private Button btnTancarSessio;

    private ControladorPresentacio ctrlPres;

    //Pre:
    //Post: Inicialitza la instancia a ctrlPresentacio
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
        ctrlPres = ControladorPresentacio.getInstance();
        
    }

    //Pre:
    //Post: canvia a la pantalla Entrar Dades
    @FXML
    void showEntrarDades(ActionEvent event) {
        ctrlPres.canviaStage("EntrarDadesA");
    }

    //Pre:
    //Post: canvia a la pantalla de gestio d'ítems
    @FXML
    void showGestionarItems(ActionEvent event) {
        ctrlPres.canviaStage("GestioItemsA");
    }

    //Pre:
    //Post: canvia a la pantalla de gestio d'usuaris
    @FXML
    void showGestionarUsuaris(ActionEvent event) {
        ctrlPres.canviaStage("GestioUsuarisA");
    }

    //Pre:
    //Post: canvia a la pantalla de gestio de valoracions
    @FXML
    void showGestionarValoracions(ActionEvent event) {
        ctrlPres.canviaStage("GestioValoracionsA");
    }

    //Pre:
    //Post: canvia a la pantalla de valorar la qualitat de les recomanacions
    @FXML
    void showValorarQualitatAlgorismes(ActionEvent event) {
        ctrlPres.canviaStage("DCGRecomanacionsA");
    }

    //Pre:
    //Post: canvia a la pantalla de gestio dels algorismes recomanadors
    @FXML
    void showGestionarAlgorismes(ActionEvent event) {
        ctrlPres.canviaStage("GestioAlgorismesA");
    }

    //Pre:
    //Post: canvia a la pantalla de gestio dels tipus de calcul de distancia entre usuaris
    @FXML
    void showGestionarDistancies(ActionEvent event) {
        ctrlPres.canviaStage("GestioCalculDistanciesA");
    }

    //Pre:
    //Post: tanca la sessió de l'administrador
    //Post: canvia a la pantalla de Login
    @FXML
    void tancarSessio(ActionEvent event) {
        ctrlPres.logout();
        ctrlPres.canviaStage("Login");
    }

}
