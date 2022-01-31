package Presentacio.Controladors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import Excepcions.UsuariNoExisteix;
import javafx.fxml.Initializable;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;



public class CtrlVistaGestioUsuarisA implements Initializable {

    @FXML
    private Button btnCercarUsuari;

    @FXML
    private Button btnEliminarUsuari;

    @FXML
    private Button btnReturn;

    @FXML
    private TextField txtCerca;

    @FXML
    private TableView<ObservableList<String>> taulaUsuaris;

    private List<String> columnNames;
    private ArrayList<ArrayList<String>> totalFiles;

    private ControladorPresentacio ctrlPres;
    
    //Pre:
    //Post: Inicialitza la instancia a ctrlPresentacio, i s'inicialitzen les files i columnes de la taula
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
        ctrlPres = ControladorPresentacio.getInstance();
        //Afegim les columnes
        setColumns();
        //Afegim les files
        setRows();
    }

    //Pre:
    //Post: S'afegeix a la taula una única columna "UserID"
    private void setColumns() {
        columnNames = new ArrayList<String>();
        columnNames.add("UserID");
        ctrlPres.setColumns(columnNames, taulaUsuaris);
    }

    //Pre:
    //Post: S'inicialitzen les files de la taula amb els IDs dels usuaris
    private void setRows() {
        totalFiles = ctrlPres.getIDsUsuaris();
        ctrlPres.setRows(totalFiles, taulaUsuaris);
    }

    //Pre:
    //Post: filtra les files que apareixen a la taula segons si contenen el número escrit a txtCerca
    @FXML
    void cercaUsuari(ActionEvent event) {
        String cerca = txtCerca.getText();
        if (cerca != "") {
            ctrlPres.cercaEnTaula(cerca, 0, taulaUsuaris, totalFiles);
        }
        else setRows();
    }

    //Pre:
    //Post: Elimina del sistema l'usuari amb l'ID corresponent a la fila seleccionada en la taula
    @FXML
    void eliminaUsuari(ActionEvent event) {
        List<String> l = taulaUsuaris.getSelectionModel().getSelectedItem();
        if (l == null) {
            ctrlPres.mostraError("No has seleccionat cap usuari.");
        }
        else {
            
        try {
            ctrlPres.eliminarUsuariRatings(l.get(0));
            ctrlPres.mostraInfo("usuari eliminat", "L'usuari seleccionat amb id " + l.get(0) + " s'ha eliminat correctament.");

        } catch (NumberFormatException e) {
            ctrlPres.mostraError("Sembla que hi ha algun error amb l'id d'aquest usuari.");
        } catch (UsuariNoExisteix e) {
            ctrlPres.mostraError("S'ha intentat eliminar un usuari que no existeix.");
        }        
        setRows();
        }
    }

    //Pre:
    //Post: Es retorna a la pantalla anterior que es Menu Administrador
    @FXML
    void returnA(ActionEvent event) {
        ctrlPres.canviaStage("MenuA");
    }
    
}
