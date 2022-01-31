package Presentacio.Controladors;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.fxml.Initializable;

public class CtrlVistaConsultarHistoricRecomanacionsU implements Initializable {

    
    @FXML
    private Button btnCercarItem;

    @FXML
    private Button btnReturn;

    @FXML
    private ChoiceBox<String> chboxItemsCerca;

    @FXML
    private TableView<ObservableList<String>> taulaRecomanacions;

    @FXML
    private TextField txtCerca;

    private ControladorPresentacio ctrlPres;

    private ArrayList<ArrayList<String>> totalFiles;

    private ArrayList<String> columnNames;
    
    //Pre: 
    //Post: S'inicialitza la instancia de ctrlPresentacio, les columnes, les files i el chbox
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ctrlPres = ControladorPresentacio.getInstance();
        //Afegim les columnes
        setColumns();
        //Afegim les files
        setRows();
        //Afegim les columnes ID i les que continguin "title" al choiceBox
        setChoiceBox();     

        taulaRecomanacions.setEditable(true);
    }

    //Pre:
    //Post: S'inicialitza el choice box amb les columnes de items.
    private void setChoiceBox() {
        ObservableList<String> options = FXCollections.observableArrayList(columnNames);
        chboxItemsCerca.setValue(columnNames.get(0));
        chboxItemsCerca.setItems(options);
    }

    //Pre:
    //Post: S'inicialitzen les files amb l'historic de recomanacions de l'usuari
    private void setRows() {
        totalFiles =  ctrlPres.getHistoricRecomanacio();
        ctrlPres.setRows(totalFiles, taulaRecomanacions);
    }

    //Pre:
    //Post: S'incializten les columnes amb l'IDRecomanacio i amb les columnes d'item
    private void setColumns() {
        columnNames = new ArrayList<String>();
        columnNames.add("IDRecomanacio");
        columnNames.addAll(ctrlPres.getNomColumnesItem());
        ctrlPres.setColumns(columnNames, taulaRecomanacions);
    }

    //Pre:
    //Post: Es fa una cerca d'un item pels camps disponibles a la chbox
    @FXML
    void cercarItem(ActionEvent event) {
        String cerca = txtCerca.getText();
        if (cerca != "") {
            ctrlPres.cercaAmbCHBox(cerca, chboxItemsCerca, columnNames, taulaRecomanacions, totalFiles);
        }
        else setRows();
    }

    //Pre:
    //Post: Es torna a la pantalla anterior que es "MenuU"
    @FXML
    void returnU(ActionEvent event) {
        ctrlPres.canviaStage("MenuU");
    }

    
}
