package Presentacio.Controladors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Excepcions.ExcepcionsRecomanador;
import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


public class CtrlVistaDemanarRecomanacioU implements Initializable {


    @FXML
    private Button btnRecomana;

    @FXML
    private Button btnReturn;

    @FXML
    private TableView<ObservableList<String>> taulaItemsRec;

    @FXML
    private TextField txtItems;

    @FXML
    private TextField txtNItems;

    private ControladorPresentacio ctrlPres;


    //Pre:
    //Post: S'inicalitza la instancia de ctrlPresentacio i les columnes
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ctrlPres = ControladorPresentacio.getInstance();
        //Afegim les columnes
        setColumns();
    }

    //Pre:
    //Post: S'inicialitzen les files amb la recomanacio d'items
    private void setRows() {
        taulaItemsRec.getItems().clear();
        String itemsString = txtItems.getText();
        String[] items = new String[0];
        if (itemsString.length() > 0) items = itemsString.split(",");
        int nSortida;
        if (txtNItems.getText().equals("") ) nSortida = 10;
        else nSortida =  Integer.parseInt(txtNItems.getText());
        ArrayList<ArrayList<String>> itemsRecomanats;
        try {
            itemsRecomanats = ctrlPres.getRecomanacioArrList(items, nSortida);
            ctrlPres.setRows(itemsRecomanats, taulaItemsRec);
        } catch (ExcepcionsRecomanador e) {
            ctrlPres.mostraError(e.toString());
        }
        
    }

    //Pre:
    //Post: S'inicialitzen les columnes amb les columnes d'item
    private void setColumns() {
        taulaItemsRec.getColumns().clear();
        List<String> columnNames = ctrlPres.getNomColumnesItem();
        ctrlPres.setColumns(columnNames, taulaItemsRec);
    }

    //Pre:
    //Post: Es dona una recomanacio a l'usuari en cas que tingui valoracions
    @FXML
    public void RecomanaItems(ActionEvent event) {
        if (ctrlPres.userTeValoracions()) setRows();
        else ctrlPres.mostraError("Per a poder rebre una recomanacio, primer has de valorar algun item");
    }

    //Pre:
    //Post: Es valida l'entrada per tal que el nombre d'items sigui un nombre enter
    @FXML
    void validaEntrada(KeyEvent event) {
        String txt = txtNItems.getText();
        String txtNou = ctrlPres.ValidaNumeroAlFinal(txt);
        Boolean esValid = txt.equals(txtNou);
        if (!esValid) {
            txtNItems.setText(txtNou);
            txtNItems.end();
        }
    }

    //Pre:
    //Post: Es torna a la pantalla anterior que es "MenuU"
    @FXML
    public void returnU(ActionEvent event) {
        ctrlPres.canviaStage("MenuU");
    }
    
}
