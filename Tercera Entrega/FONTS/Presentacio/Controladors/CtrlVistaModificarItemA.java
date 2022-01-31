package Presentacio.Controladors;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

public class CtrlVistaModificarItemA implements Initializable{

    @FXML
    private Button btnAplicaCanvis;

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
    //Post: S'inicialitza una fila amb els atributs de l'item que es vol modificar
    private void setRows() {
        totalFiles = ctrlPres.getItemAModificar();
        ctrlPres.setRows(totalFiles, taulaItems);
    }

    //Pre:
    //Post: Es guarden al sistema les modificacions fetes en els atributs de l'ítem
    @FXML
    void aplicaCanvis(ActionEvent event) {
        
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

            String itemID = ctrlPres.getIdItemAModificar();
            //per a cada atribut, actualitza'l
            for (int i = 0; i < nomCols.size(); ++i) {

                //System.out.println("Intento modificar " + nomCols.get(i) + " per posar el valor " + valPerCol.get(i).get(0) + " de l'item amb id " + itemID);
                ctrlPres.canviarValorsAtribut(itemID, valPerCol.get(i), nomCols.get(i));
            }

            ctrlPres.mostraInfo("Item modificat", "L'item s'ha modificat correctament.");
            ctrlPres.canviaStage("GestioItemsA");
        }
    }

    //Pre:
    //Post: canvia a la pantalla anterior de gestió d'ítems
    @FXML
    void returnA(ActionEvent event) {
        ctrlPres.canviaStage("GestioItemsA");
    }

}
