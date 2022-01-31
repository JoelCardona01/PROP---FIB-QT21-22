package Presentacio.Controladors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Excepcions.ExcepcionsRecomanador;
import javafx.fxml.Initializable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public class CtrlVistaAfegirValoracioA implements Initializable {
    @FXML
    private Button btnCercarItem;

    @FXML
    private Button btnCercarUser;

    @FXML
    private Button btnEntraPunt;

    @FXML
    private Button btnReturn;

    @FXML
    private ChoiceBox<String> chboxItemsCerca;

    @FXML
    private TableView<ObservableList<String>> taulaItems;

    @FXML
    private TableView<ObservableList<String>> taulaUsuaris;

    @FXML
    private TextField txtCercaItems;

    @FXML
    private TextField txtCercaUser;

    @FXML
    private TextField txtPuntuacio;

    private ControladorPresentacio ctrlPres;

    private ArrayList<ArrayList<String>> totalFilesItems;

    private ArrayList<ArrayList<String>> totalFilesUsuaris;

    private ArrayList<String> itemColumnNames;

    //Pre:
    //Post: S'inicialitzen la instancia de ctrlPresentacio, les columnes i les files de les dues taules
    //i el choiceBox. 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ctrlPres = ControladorPresentacio.getInstance();
        setColumnsUser();
        setRowsUsers();
        setColumnsItems();
        setChoiceBox();
        
    }

    //Pre:
    //Post: S'inicialitza la choice box
    private void setChoiceBox() {
        ObservableList<String> options = FXCollections.observableArrayList(itemColumnNames);
        chboxItemsCerca.setValue(itemColumnNames.get(0));
        chboxItemsCerca.setItems(options);
    }

    //Pre:
    //Post: S'inicialitzen les columnes de la taula d'items
    private void setColumnsItems() {
        itemColumnNames = ctrlPres.getNomColumnesItem();
        ctrlPres.setColumns(itemColumnNames, taulaItems);
        
    }

    //Pre: 
    //Post: S'inicialitzen les files de la taula d'usuaris
    private void setRowsUsers() {
        totalFilesUsuaris = ctrlPres.getUserRatingsID();
        ctrlPres.setRows(totalFilesUsuaris, taulaUsuaris);
    }

    //Pre:
    //Post: S'inicialitzen les columnes de la taula d'usuaris
    private void setColumnsUser() {
        List<String> userColumnNames = new ArrayList<String>();
        userColumnNames.add("UserID");
        ctrlPres.setColumns(userColumnNames, taulaUsuaris);
    }

    //Pre:
    //Post: Mostra per pantalla els items no valorats de l'usuari de la fila seleccionada
    @FXML
    void showItemsNoValorats(MouseEvent event) {
        List<String> filaSeleccionada = taulaUsuaris.getSelectionModel().getSelectedItem();
        if (filaSeleccionada != null) {
            String userID = filaSeleccionada.get(0);
            setRowsItems(userID);
        }
    }
    
    //Pre:
    //Post: Inicialitza les files de la taula d'items, pels items no valorats per userID
    private void setRowsItems(String userID) {
        totalFilesItems = ctrlPres.getItemsNoValoratsUserRatings(userID);
        ctrlPres.setRows(totalFilesItems, taulaItems);
    }

    //Pre:
    //Post: S'afegeix la puntuacio indicada al camp de text per l'usuari seleccionat i l'item seleccionat
    @FXML
    public void afegeixPuntuacio(ActionEvent event) {
        String txt = txtPuntuacio.getText();
        if (txt.length() > 0) {
            List<String> filaItem = taulaItems.getSelectionModel().getSelectedItem();
            List<String> filaUser = taulaUsuaris.getSelectionModel().getSelectedItem();
            if (filaUser == null ) {
                ctrlPres.mostraError("Usuari no seleccionat. Selecciona l'usuari del que vols fer una valoracio");
            }
            else if (filaItem == null) {
                ctrlPres.mostraError("Item no seleccionat. Selecciona l'item del que vols fer una valoracio");
            }
            else {
                valoraItem(filaUser.get(0), filaItem.get(0),txt);
                setRowsItems(filaUser.get(0));
                txtPuntuacio.clear();
            }
        }
        else ctrlPres.mostraError("Introdueix una puntuacio no buida");
    }

    //Pre:
    //Post: Es valora l'item itemID per l'usuari userID amb la puntuacio strPuntuacio
    private void valoraItem(String userID, String itemID, String strPuntuacio) {
        Double puntuacio = Double.parseDouble(strPuntuacio);
        try {
            ctrlPres.valoraItemUserRatings(userID,itemID,puntuacio);
            ctrlPres.mostraInfo("Valoracio feta", "La valoracio s'ha realitzat correctament");
        } catch (NumberFormatException e) {
            ctrlPres.mostraError("La puntuacio indicada no es un numero");
        } catch (ExcepcionsRecomanador e) {
            ctrlPres.mostraError(e.toString());
        }
    }

    //Pre:
    //Post: Cerca items per la cerca indicada al camp de text, per la columna indicada al chbox
    @FXML
    void cercarItem(ActionEvent event) {
        String cerca = txtCercaItems.getText();
        if (cerca != "") {
            //Primer mirem per quina columna hem de fer la cerca
            int selected = 0;
            boolean trobat = false;
            String strSelected = chboxItemsCerca.getValue();
            int size = itemColumnNames.size();
            for (int i = 0; i<size && !trobat; i++){
                if (itemColumnNames.get(i).equals(strSelected)) {
                    trobat = true;
                    selected = i;
                }
            }
            //Ara cerquem per aquesta columna i filtrem el resultat
            ctrlPres.cercaEnTaula(cerca, selected, taulaItems, totalFilesItems);
        }
        else {
            List<String> filaUser = taulaUsuaris.getSelectionModel().getSelectedItem();
            setRowsItems(filaUser.get(0));
        }
    }
    //Pre:
    //Post: Cerca usuaris per la cerca indicada al camp de text
    @FXML
    void cercarUser(ActionEvent event) {
        String strCerca = txtCercaUser.getText();
        if (strCerca != "") {
            ctrlPres.cercaEnTaula(strCerca, 0, taulaUsuaris, totalFilesUsuaris);
        }
        else setRowsUsers();
    }

    //Pre:
    //Post: Es torna a la pantalla anterior que es "GestioValoracionsA"
    @FXML
    void returnA(ActionEvent event) {
        ctrlPres.canviaStage("GestioValoracionsA");
    }

    //Pre:
    //Post: Es valida la entrada de puntuacio per a tal que nomes contingui numeros o punts.
    @FXML
    void validaEntradaPunt(KeyEvent event) {
        String txt = txtPuntuacio.getText();
        String txtNou = ctrlPres.ValidaNumeroOPuntAlFinal(txt);
        Boolean esValid = txt.equals(txtNou);
        if (!esValid) {
            txtPuntuacio.setText(txtNou);
            txtPuntuacio.end();
        }
    }
    
}
