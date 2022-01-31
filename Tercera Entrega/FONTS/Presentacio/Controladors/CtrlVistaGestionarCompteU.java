package Presentacio.Controladors;

import java.net.URL;
import java.util.ResourceBundle;

import Excepcions.ExcepcionsRecomanador;
import Excepcions.PersonaActualNoValida;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CtrlVistaGestionarCompteU implements Initializable{

    @FXML
    private Button btnCanviaContra;

    @FXML
    private Button btnCanviaUsuari;

    @FXML
    private Button btnEliminaCompte;

    @FXML
    private Button btnReturn;

    @FXML
    private PasswordField txtNovaPass;

    @FXML
    private TextField txtUsuariActual;

    @FXML
    private TextField txtUsuariNou;

    private ControladorPresentacio ctrlPres;

    //Pre:
    //Post: S'inicialitza la instancia de ctrlPresentacio i el text del username de l'usuari
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ctrlPres = ControladorPresentacio.getInstance();
        txtUsuariActual.setText(ctrlPres.getUsernameUsuari());
        
    }

    //Pre:
    //Post: Es canvia la contrasenya de l'usuari en cas que la indicada no sigui buida
    @FXML
    public void canviaContra(ActionEvent event) {
        if (txtNovaPass.getText().equals("")) ctrlPres.mostraError("Introdueix una nova contrasenya no buida");
        else {
            try {
                ctrlPres.canviarContrasenya(txtNovaPass.getText());
                ctrlPres.mostraInfo("Contrasenya canviada", "La teva contrasenya s'ha canviat correctament");
            } catch (PersonaActualNoValida e) {
                ctrlPres.mostraError(e.toString());
            }
        }
    }

    //Pre:
    //Post: Es canvia el nom d'usuari en cas que aquest estigui disponible
    @FXML
    public void canviaUsuari(ActionEvent event) {
        if (txtUsuariNou.getText().equals("")) ctrlPres.mostraError("Introdueix un nou usuari no buit");
        else {
            boolean usuariCanviatCorrectament = true;
            try {
                ctrlPres.canviarNomUsuari(txtUsuariNou.getText());
            } catch (ExcepcionsRecomanador e) {
                ctrlPres.mostraError(e.toString());
                usuariCanviatCorrectament = false;
            }
            if (usuariCanviatCorrectament) {
                txtUsuariActual.setText(ctrlPres.getUsernameUsuari());
                ctrlPres.mostraInfo("Usuari canviat", "El teu nom d'usuari s'ha canviat correctament");
            }
        }
    }

    //Pre:
    //Post: Es torna a la pantalla anterior que es "MenuU"
    @FXML
    void returnU(ActionEvent event) {
        ctrlPres.canviaStage("MenuU");
    }

    //Pre:
    //Post: S'elimina el compte de l'usuari
    @FXML
    public void eliminaCompte(ActionEvent event) {
        try {
            ctrlPres.esborraCompte();
        } catch (PersonaActualNoValida e) {
           ctrlPres.mostraError(e.toString());
        }
    }
    
}
