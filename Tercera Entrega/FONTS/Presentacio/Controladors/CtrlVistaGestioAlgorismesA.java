package Presentacio.Controladors;

import java.net.URL;
import java.util.ResourceBundle;



import Excepcions.ExcepcionsRecomanador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;

public class CtrlVistaGestioAlgorismesA implements Initializable {

    @FXML
    private CheckBox CheckBoxRecalcularClusters;

    @FXML
    private TextField KCollaborativeFiltering;

    @FXML
    private TextField KContentBasedFiltering;

    @FXML
    private TextField KHybridApproaches;

    @FXML
    private Button btnAplicarCanvis;

    @FXML
    private Button btnReturn;

    @FXML
    private RadioButton rbCollaborativeFiltering;

    @FXML
    private RadioButton rbContentBasedFiltering;

    @FXML
    private RadioButton rbHybridApproaches;

    private ControladorPresentacio ctrlPres;


    //Pre:
    //Post: Inicialitza la instancia a ctrlPresentacio, s'inicialitzen els radiobuttons corresponents a les estratègies, i també les seves K
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    
        ctrlPres = ControladorPresentacio.getInstance();
        ToggleGroup tg = new ToggleGroup();
        rbContentBasedFiltering.setToggleGroup(tg);
        rbCollaborativeFiltering.setToggleGroup(tg);
        rbHybridApproaches.setToggleGroup(tg);
        String estrActual = ctrlPres.demanaEstrategiaActual();
        if (estrActual != null) {
            if (estrActual.equals("ContentBasedFiltering")) rbContentBasedFiltering.setSelected(true);
            else if (estrActual.equals("CollaborativeFiltering")) rbCollaborativeFiltering.setSelected(true);
            else if (estrActual.equals("HybridApproaches")) rbHybridApproaches.setSelected(true);
        }
        actualitzaksPantalla();
    }

    //Pre:
    //Post: Es guarden al sistema els canvis que s'hagin fet en les estratègies
    //Post: Es recalculen els clústers si s'ha marcat la checkbox corresponent
    @FXML
    void aplicarCanvis(ActionEvent event) {
        try {
            int k;
            if (KCollaborativeFiltering.getText().equals("")) KCollaborativeFiltering.setText(String.valueOf(ctrlPres.demanaKkmeans()));
            if (KContentBasedFiltering.getText().equals("")) KContentBasedFiltering.setText(String.valueOf(ctrlPres.demanaKknearest()));
            if (KHybridApproaches.getText().equals("")) KHybridApproaches.setText(String.valueOf(ctrlPres.demanaKknearest()));
            String estrategiaNova;
            if (rbCollaborativeFiltering.isSelected()) {
                k = Integer.parseInt(KContentBasedFiltering.getText());
                ctrlPres.canviaKKnearest(k);
                k = Integer.parseInt(KCollaborativeFiltering.getText());
                estrategiaNova = "CollaborativeFiltering";
                ctrlPres.canviaKKmeans(k);
            }
            else if (rbContentBasedFiltering.isSelected()) {
                k = Integer.parseInt(KCollaborativeFiltering.getText());
                ctrlPres.canviaKKmeans(k);
                k = Integer.parseInt(KContentBasedFiltering.getText());
                estrategiaNova = "ContentBasedFiltering";
                ctrlPres.canviaKKnearest(k);
            }
            else {//HybridApproaches
                k = Integer.parseInt(KCollaborativeFiltering.getText());
                ctrlPres.canviaKKmeans(k);
                k = Integer.parseInt(KHybridApproaches.getText());
                estrategiaNova = "HybridApproaches";
                ctrlPres.canviaKKnearest(k);
            }  
            ctrlPres.demanaCanviEstrategia(estrategiaNova);
            ctrlPres.mostraInfo("Canvis aplicats", "S'ha canviat a l'estrategia "+ estrategiaNova + " correctament amb k = " + k + ", tambe s'han actualitzat les k's indicades");
            if (CheckBoxRecalcularClusters.isSelected()) {
                ctrlPres.recalculaClusters();
                ctrlPres.mostraInfo("Clusters recalculats", "S'han recalculat els clusters correctament");
            }
            actualitzaksPantalla();
           
        }
        catch (ExcepcionsRecomanador e) {
            ctrlPres.mostraError(e.toString());
        }


    }

    //Pre:
    //Post: es mostren les K corresponents a cada estratègia de filtering
    private void actualitzaksPantalla() {
        KCollaborativeFiltering.setText(String.valueOf(ctrlPres.demanaKkmeans()));
        KContentBasedFiltering.setText(String.valueOf(ctrlPres.demanaKknearest()));
        KHybridApproaches.setText(String.valueOf(ctrlPres.demanaKknearest()));
    }

    //Pre:
    //Post: comprova que la K que apareix al camp de text del ContentBasedFiltering és vàlida
    @FXML
    private void validaEntradaKCBF(KeyEvent event) {
        String txt = KContentBasedFiltering.getText();
        Boolean esValid;
        String txtNou;
        if (txt.length()==1 && txt.charAt(0) == '0') {
            txtNou = "";
            esValid = false;
            KHybridApproaches.setText("");
        }
        else {
            txtNou = ctrlPres.ValidaNumeroAlFinal(txt);
            esValid = txt.equals(txtNou);
        }
        if (!esValid) {
            KContentBasedFiltering.setText(txtNou);
            KContentBasedFiltering.end();
        }
        else KHybridApproaches.setText(txt);

    }

    //Pre:
    //Post: comprova que la K que apareix al camp de text del CollaborativeFiltering és vàlida
    @FXML
    private void validaEntradaKCF(KeyEvent event) {
        String txt = KCollaborativeFiltering.getText();
        Boolean esValid;
        String txtNou;
        if (txt.length()==1 && txt.charAt(0) == '0') {
            txtNou = "";
            esValid = false;
        }
        else {
            txtNou = ctrlPres.ValidaNumeroAlFinal(txt);
            esValid = txt.equals(txtNou);
        }
        if (!esValid) {
            KCollaborativeFiltering.setText(txtNou);
            KCollaborativeFiltering.end();
        }
    }

    //Pre:
    //Post: comprova que la K que apareix al camp de text de HybridApproaches és vàlida
    @FXML
    private void validaEntradaKHA(KeyEvent event) {
        String txt = KHybridApproaches.getText();
        String txtNou;
        Boolean esValid;
        if (txt.length()==1 && txt.charAt(0) == '0') {
            txtNou = "";
            esValid = false;
            KContentBasedFiltering.setText("");
        }
        else {
            txtNou = ctrlPres.ValidaNumeroAlFinal(txt);
            esValid = txt.equals(txtNou);
        }
        if (!esValid) {
            KHybridApproaches.setText(txtNou);
            KHybridApproaches.end();
        }
        else KContentBasedFiltering.setText(txt);
    }

    //Pre:
    //Post: Es retorna a la pantalla anterior que es Menu Administrador
    @FXML
    void returnA(ActionEvent event) {
        ctrlPres.canviaStage("MenuA");
    }

}
