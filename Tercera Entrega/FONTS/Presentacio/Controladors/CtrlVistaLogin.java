package Presentacio.Controladors;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Excepcions.BaseDeDadesBuida;
import Excepcions.ContrasenyaIncorrecte;
import Excepcions.ExcepcionsRecomanador;
import Excepcions.UsuariNoExisteix;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CtrlVistaLogin implements Initializable {
    
    @FXML
    private Button btnLogin;

    @FXML
    private Button btnRegiser;

    @FXML
    private PasswordField txtPass;

    @FXML
    private TextField txtUser;

    private ControladorPresentacio ctrlPres;

    //Pre:
    //Post: S'inicialitza la instancia de ctrlPresentacio
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ctrlPres = ControladorPresentacio.getInstance();
    }

    //Pre:
    //Post: Es fa login pel parell usuari/contrasenya indicats, en cas que algun sigui erroni s'indica per pantalla 
    // En cas que siguin valids, si es un usuari anira al menu d'usuari i si es administrador al menu d'admin
    @FXML
    void login(ActionEvent event) throws IOException {
        boolean error = false;
        boolean entraDades = false;
        if (txtUser.getText().equals("") || txtPass.getText().equals("") )  {
            ctrlPres.mostraError("Introdueix un Usuari i Contrasenya no buits");
        }
        else {
            try {
            ctrlPres.iniciaInstancia(txtUser.getText(), txtPass.getText());
            }
            catch(UsuariNoExisteix e) {
                error = true;
                ctrlPres.mostraError(e.toString());
            }
            catch (ContrasenyaIncorrecte e1) {
                error = true;
                ctrlPres.mostraError(e1.toString());
            }
            catch (BaseDeDadesBuida e2) {
                if (!txtUser.getText().equals("Admin")) {
                    error = true;
                    ctrlPres.mostraError(e2.toString());
                }
                else {
                    ctrlPres.mostraInfo("Base de dades buida", "La base de dades es buida, entra les dades d'items i ratings siusplau, altrament el sistema no funcionara correctament");
                    ctrlPres.canviaStage("EntrarDadesA");
                    entraDades = true;
                } 
            }
            catch(ExcepcionsRecomanador e3) {
                error = true;
                ctrlPres.mostraError(e3.toString());
            }
            if (!error) {
                if (txtUser.getText().equals("Admin")) {
                    if (!entraDades) ctrlPres.canviaStage("MenuA");
                } 
                else ctrlPres.canviaStage("MenuU");
            }
        }

    }

    //Pre:
    //Post: Es canvia a la pantalla de registre
    @FXML
    void register(ActionEvent event) throws IOException {
        ctrlPres.canviaStage("Registre");
    }
}
