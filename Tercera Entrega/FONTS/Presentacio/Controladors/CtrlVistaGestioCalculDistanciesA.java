package Presentacio.Controladors;

import java.net.URL;
import java.util.ResourceBundle;

import Excepcions.ExcepcionsRecomanador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class CtrlVistaGestioCalculDistanciesA implements Initializable {

    @FXML
    private Button btnAplicarCanvis;

    @FXML
    private Button btnReturn;

    @FXML
    private RadioButton rbEuclidiana;

    @FXML
    private RadioButton rbMitjana;

    @FXML
    private RadioButton rbPonderada;

    private ControladorPresentacio ctrlPres;

    //Pre:
    //Post: Inicialitza la instancia a ctrlPresentacio, i s'inicialitzen els radiobuttons corresponents als diferents calculs de distancia
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
        ctrlPres = ControladorPresentacio.getInstance();
        ToggleGroup tg = new ToggleGroup();
        rbMitjana.setToggleGroup(tg);
        rbEuclidiana.setToggleGroup(tg);
        rbPonderada.setToggleGroup(tg);
        String distActual = ctrlPres.demanaDistanciaActual();
        if (distActual.equals("DistanciaMitjana")) rbMitjana.setSelected(true); 
        else if (distActual.equals("DistanciaPonderada")) rbPonderada.setSelected(true);
        else if (distActual.equals("DistanciaEuclidiana")) rbEuclidiana.setSelected(true);
    }

    //Pre:
    //Post: S'actualitza l'estrategia de calcul de distancies entre usaris que usa el sistema en les recomanacions
    @FXML
    void aplicarCanvis(ActionEvent event) {
        String estrategia;
        if (rbMitjana.isSelected()) estrategia = "DistanciaMitjana";
        else if (rbPonderada.isSelected()) estrategia = "DistanciaPonderada";
        else estrategia = "DistanciaEuclidiana";
        try {
            ctrlPres.demanaCanviDistancia(estrategia);
            ctrlPres.mostraInfo("Canvis aplicats", "S'ha canviat l'estrategia per fer el calcul de les distancies a "+estrategia+ " correctament");
        } catch (ExcepcionsRecomanador e) {
            ctrlPres.mostraError(e.getMessage());
        }
    }

    //Pre:
    //Post: Es retorna a la pantalla anterior que es Menu Administrador
    @FXML
    void returnA(ActionEvent event) {
        ctrlPres.canviaStage("MenuA");
    }

}
