package Presentacio.Controladors;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

import Excepcions.ItemBuit;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class CtrlVistaAfegirItemA implements Initializable {

    @FXML
    private Button btnAfegirItem;

    @FXML
    private Button btnReturn;

    @FXML
    private TableView<ObservableList<String>> taulaItems;

    private ArrayList<String> columnNames;
    private ArrayList<ArrayList<String>> totalFiles;

    private ControladorPresentacio ctrlPres;

    //Pre:
    //Post: Inicialitza la instancia a ctrlPresentacio, s'inicialitzen les files i columnes i es permet editar la taula.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        ctrlPres = ControladorPresentacio.getInstance();
        //Afegim les columnes
        setColumns();
        //Afegim les files
        setRows();
        //Fem que l'admin pugui afegir els atributs de l'item que vol afegir directament als camps de la taula
        taulaItems.setEditable(true);
    }

    //Pre: 
    //Post: S'inicialitzen les columnes amb les columnes d'item, pero, sense la columna id
    private void setColumns() {
        columnNames = ctrlPres.getNomColumnesItem();
        columnNames.remove("id");
        ctrlPres.setEditableColumns(columnNames, taulaItems);
    }
    //Pre:
    //Post: S'inicialitza una fila buida
    private void setRows() {
        //Inserim una única fila editable a la taula
        totalFiles = new ArrayList<ArrayList<String>>();
        ArrayList<String> filaBuida = new ArrayList<String>();
        for (String s : columnNames) filaBuida.add("");
        totalFiles.add(filaBuida);

        ctrlPres.setRows(totalFiles, taulaItems);
    }

    //Pre:
    //Post: S'afegeix l'item al sistema per les columnes indicades, sempre i quan la fila no sigui buida
    @FXML
    void afegirItem(ActionEvent event) {
        ObservableList<String> filaEmplenada = taulaItems.getItems().get(0);
        
        if (filaEmplenada == null) {
            ctrlPres.mostraError("No hi ha cap item a la taula");
        }
        else {

            //Fiquem en un Vector els noms de les columnes
            Vector<String> nomCols = new Vector<String>(columnNames);

            //Fiquem en un vector els valors de les columnes
            Vector<Vector<String>> valPerCol = new Vector<Vector<String>>();
            for (String val : filaEmplenada) {
                Vector<String> valcol = new Vector<String>();
                if (!val.equals("")) {
                    String valors[] = val.split(";");
                    for (String s2 : valors) {
                        valcol.add(val);
                    } 
                } 
                valPerCol.add(valcol);
                
            }
            try {
                ctrlPres.afegirItem(nomCols, valPerCol);
                ctrlPres.mostraInfo("Item afegit", "L'item que has creat s'ha afegit correctament.");
                ctrlPres.canviaStage("GestioItemsA");
            } catch (ItemBuit e) {
                ctrlPres.mostraError(e.toString());
            }

        }
    }

    //Pre:
    //Post: Es retorna a la pantalla anterior que es GestioItemsA
    @FXML
    void returnA(ActionEvent event) {
        ctrlPres.canviaStage("GestioItemsA");
    }

}
