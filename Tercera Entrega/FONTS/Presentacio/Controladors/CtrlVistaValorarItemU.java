package Presentacio.Controladors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Excepcions.ExcepcionsRecomanador;
import Excepcions.PuntuacioNoValida;
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

public class CtrlVistaValorarItemU implements Initializable {

    @FXML
    private Button btnCerca;

    @FXML
    private Button btnReturn;

    @FXML
    private Button btnValora;

    @FXML
    private TableView<ObservableList<String>> taulaItems;

    @FXML
    private ChoiceBox<String> chboxItemsCerca;

    @FXML
    private TextField txtCerca;

    @FXML
    private TextField txtPuntua;

    @FXML
    private Label infoMaxMin;

    private ControladorPresentacio ctrlPres;

    private ArrayList<ArrayList<String>> totalFiles;

    List<String> columnNames;

    //Pre:
    //Post: S'inicialitza la instancia de ctrlPresentacio, les columnes, les files, la choiceBox i el missatge
    //de minim i maxim de puntuacions
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ctrlPres = ControladorPresentacio.getInstance();
        //Afegim les columnes
        setColumns();
        //Afegim les files
        setRows();
        //Afegim les columnes dels item al choiceBox
        setChoiceBox();
        infoMaxMin.setText("Puntua amb un real entre "+ctrlPres.demanaMin()+" i "+ctrlPres.demanaMax());
    }

    //Pre:
    //Post: S'inicialitza la choiceBox amb les columnes de setcolumns
    private void setChoiceBox() {
        ObservableList<String> options = FXCollections.observableArrayList(columnNames);
        chboxItemsCerca.setValue(columnNames.get(0));
        chboxItemsCerca.setItems(options);
    }

    //Pre:
    //Post: S'inicialitzen les columnes amb les columnes d'item
    private void setColumns() {
        columnNames = ctrlPres.getNomColumnesItem();
        ctrlPres.setColumns(columnNames, taulaItems);
    }

    //Pre:
    //Post: S'inicialitzen les files amb els items no valorats per l'usuari
    private void setRows() {
        totalFiles = ctrlPres.getItemsNoValorats();
        ctrlPres.setRows(totalFiles, taulaItems);
    }   

    //Pre:
    //Post: Es cerca un item per la columna indicada a la chbox amb la cerca indicada al camp de text
    @FXML
    public void CercaItem(ActionEvent event) {
        String cerca = txtCerca.getText();
        if (cerca != "") {
            ctrlPres.cercaAmbCHBox(cerca, chboxItemsCerca, columnNames, taulaItems, totalFiles);
        }
        else setRows();
    }

    //Pre:
    //Post: Es torna a la pantalla anterior, que es "MenuU"
    @FXML
    void returnU(ActionEvent event) {
        ctrlPres.canviaStage("MenuU");
    }

    //Pre:
    //Post: Es valida l'entrada per tal que al camp de text nomes hi hagin numeros o punts
    @FXML
    void validaEntrada(KeyEvent event) {
        String txt = txtPuntua.getText();
        String txtNou = ctrlPres.ValidaNumeroOPuntAlFinal(txt);
        Boolean esValid = txt.equals(txtNou);
        if (!esValid) {
            txtPuntua.setText(txtNou);
            txtPuntua.end();
        }
    }

    //Pre:
    //Post: Es valora l'item seleccionat amb la puntuacio indicada al camp de text
    @FXML
    void valoraItem(ActionEvent event) {
        String txt = txtPuntua.getText();
        if (txt.length() > 0) {
            List<String> l = taulaItems.getSelectionModel().getSelectedItem();
            if (l == null) {
                ctrlPres.mostraError("Item no seleccionat. Selecciona l'item que vols puntuar");
            }
            else {
                valora(l.get(0), txt);
                setRows();
                txtPuntua.clear();
            }
        }
    }

    //Pre:
    //Post: Es valora l'item amb id=ItemID amb la puntuacio "puntuacio"
    private void valora(String ItemID, String puntuacio) {
        try {
            ctrlPres.afegirValoracio(ItemID, Double.parseDouble(puntuacio));
            ctrlPres.mostraInfo("Valoracio feta", "La teva valoracio s'ha fet correctament");
        }
        catch(PuntuacioNoValida e) {
            ctrlPres.mostraError(e.toString());
        } catch (NumberFormatException e) {
            ctrlPres.mostraError(e.toString());
        } catch (ExcepcionsRecomanador e) {
            ctrlPres.mostraError(e.toString());
        }

    }
    
}
