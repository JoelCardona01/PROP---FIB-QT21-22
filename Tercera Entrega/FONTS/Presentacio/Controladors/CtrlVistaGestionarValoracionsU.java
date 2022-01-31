package Presentacio.Controladors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import Excepcions.ExcepcionsRecomanador;
import Excepcions.PuntuacioNoValida;


public class CtrlVistaGestionarValoracionsU implements Initializable {


    @FXML
    private Button btnCerca;

    @FXML
    private Button btnReturn;

    @FXML
    private Button btnEliminaVal;

    @FXML
    private Button btnEntraPunt;

    @FXML
    private TableView<ObservableList<String>> taulaVals;

    @FXML
    private TextField txtCerca;

    @FXML
    private ChoiceBox<String> chboxValsCerca;

    @FXML
    private TextField txtNovaPuntuacio;

    @FXML
    private Label infoMaxMin;

    private ControladorPresentacio ctrlPres;

    private ArrayList<ArrayList<String>> totalFiles;

    ArrayList<String> columnNames;


    //Pre:
    //Post: S'inicialitzen les columnes, les files, el choiceBox i el text per indicar el maxim i minim de puntuacions
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ctrlPres = ControladorPresentacio.getInstance();

        //Afegim les columnes
        setColumns();
        //Afegim les files
        setRows();
        //Afegim les columnes ID i les que continguin "title" al choiceBox
        setChoiceBox();
        infoMaxMin.setText("Puntua amb un real entre "+ctrlPres.demanaMin()+" i "+ctrlPres.demanaMax());

    }

    //Pre:
    //Post: S'inicialitza la choice box amb les columnes de setColumns
    private void setChoiceBox() {
        ObservableList<String> options = FXCollections.observableArrayList(columnNames);
        chboxValsCerca.setValue(columnNames.get(0));
        chboxValsCerca.setItems(options);
    }

    //Pre:
    //Post: S'inicialitzen les files amb les valoracions de l'usuari
    private void setRows() {
        taulaVals.getItems().clear();
        List<String> columnNamesNoID = columnNames.subList(1, columnNames.size());
        totalFiles = ctrlPres.getValoracionsUser(columnNamesNoID);
        ctrlPres.setRows(totalFiles, taulaVals);
    }

    //Pre:
    //Post: S'inicialitzen les columnes amb l'id del item, les columnes d'item que continguin "title" i la puntuacio d'aquell item
    private void setColumns() {
        ArrayList<String> totalColumnNames = ctrlPres.getNomColumnesItem();
        int size = totalColumnNames.size();
        columnNames = new ArrayList<String>();
        columnNames.add(totalColumnNames.get(0));
        for (int i = 1; i<size; i++) {
            if (totalColumnNames.get(i).contains("title")) columnNames.add(totalColumnNames.get(i));
        }
        columnNames.add("puntuacio");
        ctrlPres.setColumns(columnNames, taulaVals);
    }

    //Pre:
    //Post: Es valida l'entrada del camp de text de novaPuntuacio per tal que nomes es puguin entrar
    //punts i numeros
    @FXML
    public void validaEntrada(KeyEvent event) {
        String txt = txtNovaPuntuacio.getText();
        String txtNou = ctrlPres.ValidaNumeroOPuntAlFinal(txt);
        Boolean esValid = txt.equals(txtNou);
        if (!esValid) {
            txtNovaPuntuacio.setText(txtNou);
            txtNovaPuntuacio.end();
        }
    }

    //Pre:
    //Post: S'actualitza la puntuacio en cas que s'hago seleccionat una valoracio i s'hagi entrat una puntuacio
    @FXML
    public void actualitzaPuntuacio(ActionEvent event) {
        String txt = txtNovaPuntuacio.getText();
        if (txt.length() > 0) {
            List<String> l = taulaVals.getSelectionModel().getSelectedItem();
            if (l == null) {
                ctrlPres.mostraError("Valoracio no seleccionada. Selecciona l'item del que vols canviar la puntuacio");
            }
            else {
                canviaValoracio(l.get(0), txt);
                txtNovaPuntuacio.clear();
            }
        }
    }

    //Pre:
    //Post: Es canvia una valoracio
    private void canviaValoracio(String idItem, String txt) {
        try {
            ctrlPres.canviarValoracio(idItem, Double.parseDouble(txt));
            ctrlPres.mostraInfo("Valoracio canviada", "Valoracio canviada correctament");
            taulaVals.getItems().clear();
            setRows();
        }
        catch(PuntuacioNoValida p) {
            ctrlPres.mostraError(p.toString());
        } 
        catch (ExcepcionsRecomanador e) {
            ctrlPres.mostraError(e.toString());
        }
    }

    //Pre:
    //Post: S'elimina una valoracio en cas que aquesta s'hagi seleccionat.
    @FXML
    void eliminaVal(ActionEvent event) {
        List<String> l = taulaVals.getSelectionModel().getSelectedItem();
        if (l == null) {
            ctrlPres.mostraError("Valoracio no seleccionada. Selecciona la valoracio que vols eliminar");
        }
        else {
            try {
                ctrlPres.eliminarValoracio(l.get(0));
                ctrlPres.mostraInfo("Valoracio eliminada", "La valoracio s'ha eliminat correctament");
            } catch (ExcepcionsRecomanador e) {
                ctrlPres.mostraError(e.toString());
            }
            setRows();
        }
    }

    //Pre:
    //Post: Es cerca una valoracio per la columna indicada a la chbox pel text indicat a "cerca"
    @FXML
    public void CercaVal(ActionEvent event) {
        String cerca = txtCerca.getText();
        if (cerca != "") {
            ctrlPres.cercaAmbCHBox(cerca, chboxValsCerca, columnNames, taulaVals, totalFiles);
        }
        else setRows();
    }
    
    
    //Pre:
    //Post: Es torna a la pantalla anterior que es "MenuU"
    @FXML
    public void returnU(ActionEvent event) {
        ctrlPres.canviaStage("MenuU");
    }
    
}
