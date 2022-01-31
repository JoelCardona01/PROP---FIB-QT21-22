package Presentacio.Controladors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


import Excepcions.ItemNoExisteix;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class CtrlVistaGestioItemsA implements Initializable {

    @FXML
    private Button btnAfegirItem;

    @FXML
    private Button btnCercarItem;

    @FXML
    private Button btnEliminarItem;

    @FXML
    private Button btnModificarItem;

    @FXML
    private Button btnReturn;

    @FXML
    private ChoiceBox<String> chboxItemsCerca;

    @FXML
    private TextField nomItemCercat;

    @FXML
    private TableView<ObservableList<String>> taulaItems;

    private ArrayList<String> columnNames;
    private ArrayList<ArrayList<String>> totalFiles;

    private ControladorPresentacio ctrlPres;

    //Pre:
    //Post: S'inicialitza la instància de ControladorPresentacio, les columnes i les files de la taula d'ítems, i el choiceBox de criteri de cerca
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        ctrlPres = ControladorPresentacio.getInstance();
        //Afegim les columnes
        setColumns();
        //Afegim les files
        setRows();
        //Afegim les columnes dels item al choiceBox
        setChoiceBox();
    }

    //Pre:
    //Post: S'inicialitza la choice box amb les columnes de setColumns
    private void setChoiceBox() {
        ObservableList<String> options = FXCollections.observableArrayList(columnNames);
        chboxItemsCerca.setValue(columnNames.get(0));
        chboxItemsCerca.setItems(options);
    }

    //Pre:
    //Post: S'inicialitzen les columnes de la taula amb els noms dels atributs que pot tenir definits un ítem
    private void setColumns() {
        columnNames = ctrlPres.getNomColumnesItem();
        ctrlPres.setColumns(columnNames, taulaItems);
    }

    //Pre:
    //Post: S'inicialitzen les files de la taula amb els ítems del sistema
    private void setRows() {
        totalFiles = ctrlPres.getItems();
        ctrlPres.setRows(totalFiles, taulaItems);
    }

    //Pre:
    //Post: filtra els ítems que apareixen a la taula d'acord amb la columna seleccionada al chicebox i al criteri de cerca introduït
    @FXML
    void cercaItem(ActionEvent event) {

        String cerca = nomItemCercat.getText();
        if (cerca != "") {
            //Primer mirem per quina columna hem de fer la cerca
            int selected = 0;
            boolean trobat = false;
            String strSelected = chboxItemsCerca.getValue();
            int size = columnNames.size();
            for (int i = 0; i<size && !trobat; i++){
                if (columnNames.get(i).equals(strSelected)) {
                    trobat = true;
                    selected = i;
                }
            }
            //Ara cerquem per aquesta columna i filtrem el resultat
            ctrlPres.cercaEnTaula(cerca, selected, taulaItems, totalFiles);
        }
        else setRows();
    }

    //Pre:
    //Post: Elimina del sistema l'ítem seleccionat
    @FXML
    void eliminarItem(ActionEvent event) {
        List<String> l = taulaItems.getSelectionModel().getSelectedItem();
        if (l == null) {
            ctrlPres.mostraError("No has seleccionat cap item.");
        }
        else {
            
            try {
                ctrlPres.eliminarItem(l.get(0));
                ctrlPres.mostraInfo("Item eliminat", "L'item seleccionat amb id " + l.get(0) + " s'ha eliminat correctament.");
            }
            catch (ItemNoExisteix e) {
                ctrlPres.mostraError("S'ha intentat eliminar un item que no existeix.");
            }
            
            setRows();
        }
    }

    //Pre:
    //Post: Canvia a la pantalla per afegir un ítem nou al sistema
    @FXML
    void afegirItem(ActionEvent event) {
        ctrlPres.canviaStage("AfegirItemA");
    }

    //Pre:
    //Post: Canvia a la pantalla per modificar l'ítem seleccionat
    @FXML
    void modificarItem(ActionEvent event) {
        List<String> l = taulaItems.getSelectionModel().getSelectedItem();
        if (l == null) {
            ctrlPres.mostraError("No has seleccionat cap item.");
        }
        else {
            ctrlPres.setIdItemAModificar(l.get(0));
            ctrlPres.canviaStage("ModificarItemA");
        }
    }

    //Pre:
    //Post: Es retorna a la pantalla anterior que es Menu Administrador
    @FXML
    void returnA(ActionEvent event) {
        ctrlPres.canviaStage("MenuA");
    }

}