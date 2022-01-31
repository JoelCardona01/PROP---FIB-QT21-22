package Presentacio.Controladors;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class CtrlVistaMenuU implements Initializable{

    @FXML
    private Button btnDemanarRecoma;

    @FXML
    private Button btnGestionarCompte;

    @FXML
    private Button btnGestionarVal;

    @FXML
    private Button btnHistoricRec;

    @FXML
    private Button btnTancarSessio;

    @FXML
    private Button btnValorarItem;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    private ControladorPresentacio ctrlPres;

    //Pre:
    //Post: S'inicialitza la instancia de ctrlPresentacio
    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        ctrlPres = ControladorPresentacio.getInstance();
    }
    
    //Pre:
    //Post: Es tanca sessio i es canvia a la pantalla de login
    @FXML
    void tancarSessio(ActionEvent event) throws IOException{
        ctrlPres.logout();
        ctrlPres.canviaStage("Login");
    }

    //Pre:
    //Post: Es canvia a la pantalla de ValorarItemU
    @FXML
    void showItemsNoValorats(ActionEvent event) throws IOException{
        ctrlPres.canviaStage("ValorarItemU");
    }

    //Pre:
    //Post: Es canvia a la pantalla de GestionarValoracionsU
    @FXML
    void showVistaValItem(ActionEvent event) throws IOException{
        ctrlPres.canviaStage("GestionarValoracionsU");
    }

    //Pre:
    //Post: Es canvia a la pantalla de DemanarRecomanacioU
    @FXML
    void showDemanaRecom(ActionEvent event) {
        ctrlPres.canviaStage("DemanarRecomanacioU");
    }

    //Pre:
    //Post: Es canvia a la pantalla de ConsultarHistoricRecomanacionsU
    @FXML
    void showHistoricRecom(ActionEvent event) {
        ctrlPres.canviaStage("ConsultarHistoricRecomanacionsU");
    }

    //Pre:
    //Post: Es canvia a la pantalla de GestionarCompteU
    @FXML
    void showGestionarCompte(ActionEvent event) {
        ctrlPres.canviaStage("GestionarCompteU");
    }

}
