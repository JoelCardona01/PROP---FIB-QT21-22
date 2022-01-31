package Presentacio.Controladors;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Excepcions.ExcepcionsRecomanador;
import Excepcions.PuntuacioNoValida;
import Excepcions.UsernameJaEnUs;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class CtrlVistaRegistre implements Initializable {



    @FXML
    private Button btnRegiser;

    @FXML
    private Button btnReturn;

    @FXML
    private PasswordField txtPass1;

    @FXML
    private PasswordField txtPass2;

    @FXML
    private TextField txtUser;

    private ControladorPresentacio ctrlPres;

    //Pre:
    //Post: S'inicalitza la instancia de ctrlPresentacio
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ctrlPres = ControladorPresentacio.getInstance();
    }

    //Pre:
    //Post: Es canvia a la pantalla de Login
    @FXML
    void returnToLogin(ActionEvent event) throws IOException{
        ctrlPres.canviaStage("Login");
    }

    //Pre:
    //Post: Es registra amb la parella usuari/contrasenya sempre i quan no hi hagin camps buits i la confirmacio de
    //la contrasenya coincideixi amb la contrasenya. En cas que tot vaji be, es canvia a la pantalla de login
    @FXML
    void registrar(ActionEvent event){
        if (txtUser.getText() == "" || (txtPass1.getText().equals(""))) ctrlPres.mostraError("Introudeix un usuari i contrasenya");
        else if (!txtPass1.getText().equals(txtPass2.getText())) {
            ctrlPres.mostraError("Les contrasenyes no coincideixen");
        }
        else {
            String pass = txtPass1.getText();
            String user = txtUser.getText();
            try {
                ctrlPres.registrar(user, pass);
                ctrlPres.canviaStage("Login");
            }
            catch(UsernameJaEnUs e) {
                ctrlPres.mostraError(e.toString());
            }
            catch (PuntuacioNoValida e) {
                ctrlPres.mostraError(e.toString());
            }
            //FALTA AFEGIR COMPROBACIO DE SEGURETAT DE CONTRASENYES (MAYOR DE 6 CARACTERS PER EXEMPLE)
            catch (ExcepcionsRecomanador e) {
                ctrlPres.mostraError(e.toString());
            }
        }
    }

}
