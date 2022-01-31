package Presentacio.Controladors;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Excepcions.ExcepcionsRecomanador;
import Excepcions.PuntuacioNoValida;
import Excepcions.RangNoValid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class CtrlVistaGestioValoracionsA implements Initializable {
    


    @FXML
    private Button btnAfegirVal;

    @FXML
    private Button btnEliminaVal;

    @FXML
    private Button btnEntraPunt;


    @FXML
    private Button btnActualitzaMaxMin;

    @FXML
    private Button btnCercarValoracions;

    @FXML
    private Button btnReturn;

    @FXML
    private TextField puntMAX;

    @FXML
    private TextField puntMIN;

    @FXML
    private TableView<ObservableList<String>> taulaValoracions;

    @FXML
    private TextField txtCerca;

    @FXML
    private ChoiceBox<String> chboxValsCerca;

    @FXML
    private TextField txtNovaPuntuacio;

    private ControladorPresentacio ctrlPres;

    private ArrayList<String> columnNames;

    private ArrayList<ArrayList<String>> totalFiles;

    //Pre:
    //Post: S'inicialitza la instància de ControladorPresentacio, les columnes i les files de la taula, el choiceBox de criteri de cerca, i el min i max de les valoracions
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ctrlPres = ControladorPresentacio.getInstance();
        setMinMaxPerDefecte();
        setColumns();
        setRows();
        setChoiceBox();
    }

    //Pre:
    //Post: S'inicialitza la choice box amb les columnes del setColumns
    private void setChoiceBox() {
        ObservableList<String> options = FXCollections.observableArrayList(columnNames);
        chboxValsCerca.setValue(columnNames.get(0));
        chboxValsCerca.setItems(options);
    }

    //Pre:
    //Post: S'inicialitzen les columnes de la taula de valoracions
    private void setColumns() {
        ArrayList<String> totalColumnNames = ctrlPres.getNomColumnesItem();
        int size = totalColumnNames.size();
        columnNames = new ArrayList<String>();
        columnNames.add("UserID");
        columnNames.add(totalColumnNames.get(0));
        for (int i = 1; i<size; i++) {
            if (totalColumnNames.get(i).contains("title")) columnNames.add(totalColumnNames.get(i));
        }
        columnNames.add("puntuacio");
        ctrlPres.setColumns(columnNames, taulaValoracions);
    }

    //Pre:
    //Post: S'inicialitzen el min i el max de les puntuacions
    private void setMinMaxPerDefecte() {
        double min = ctrlPres.demanaMin();
        double max = ctrlPres.demanaMax();
        puntMIN.setText(String.valueOf(min));
        puntMAX.setText(String.valueOf(max));
    }

    //Pre:
    //Post:  S'inicialitzen les files de la taula
    private void setRows() {
        taulaValoracions.getItems().clear();
        List<String> columnNamesNoID = columnNames.subList(1, columnNames.size());
        totalFiles = ctrlPres.getValoracionsUsersRatings(columnNamesNoID);
        ctrlPres.setRows(totalFiles, taulaValoracions);
    }

    //Pre: els valors dels camps de text puntMIN i puntMAX no estan buits, són números, i puntMIN < puntMAX
    //Post: s'actualitza el rang vàlid de puntuacions
    @FXML
    public void actualitzaMaxMIn(ActionEvent event) {
        if (puntMIN.getText().equals("") || puntMAX.getText().equals("")) {
            ctrlPres.mostraError("Introdueix uns valors minim i maxim no buits");
        }
        else {
            double min = Double.parseDouble(puntMIN.getText());
            double max = Double.parseDouble(puntMAX.getText());
            try {
                ctrlPres.canviaMiniMax(min, max);
                ctrlPres.mostraInfo("Rang actualitzat", "S'ha actualitzat correctament la puntuacio minima i maxima");
            } catch (RangNoValid e) {
                ctrlPres.mostraError(e.toString());
            }
        }
        setMinMaxPerDefecte();
    }

    //Pre: 
    //Post: s'actualitza la puntiacio de la valoracio seleccionada o es mostra un missatge d'error
    @FXML
    void actualitzaPuntuacio(ActionEvent event) {
        String txt = txtNovaPuntuacio.getText();
        if (txt.length() > 0) {
            List<String> l = taulaValoracions.getSelectionModel().getSelectedItem();
            if (l == null) {
                ctrlPres.mostraError("Valoracio no seleccionada. Selecciona l'item del que vols canviar la puntuacio");
            }
            else {
                canviaValoracio(l.get(0), l.get(1), txt);
                txtNovaPuntuacio.clear();
            }
        }
    }

    //Pre:
    //Post: Actualita la valoració de l'ítem al sistema
    private void canviaValoracio(String idUser, String idItem, String puntuacio) {
        try {
            ctrlPres.canviarValoracioRatings(idUser, idItem, Double.parseDouble(puntuacio));
            ctrlPres.mostraInfo("Valoracio canviada", "Valoracio canviada correctament");
            setRows();
        }
        catch(PuntuacioNoValida p) {
            ctrlPres.mostraError(p.toString());
        } 
        //Mai s'haura d'arribar aqui
        catch (ExcepcionsRecomanador e) {
            ctrlPres.mostraError(e.toString());
        }
        catch(NumberFormatException e2) {
            ctrlPres.mostraError("L'user ID no es un numero");
        }
    }
    

    //Pre:
    //Post: filtra les filas de la taula segons el choicebox i el criteri de cerca
    @FXML
    public void cercarValoracions(ActionEvent event) {
        String cerca = txtCerca.getText();
        if (cerca != "") {
            int selected = 0;
            boolean trobat = false;
            String strSelected = chboxValsCerca.getValue();
            int size = columnNames.size();
            for (int i = 0; i<size && !trobat; i++){
                if (columnNames.get(i).equals(strSelected)) {
                    trobat = true;
                    selected = i;
                }
            }
            ctrlPres.cercaEnTaula(cerca, selected, taulaValoracions, totalFiles);
        }
        else setRows();
    }

    //Pre:
    //Post: crida la funcio d'eliminar una valoració seleccionada mostra un error
    @FXML
    public void eliminaVal(ActionEvent event) {
        List<String> l = taulaValoracions.getSelectionModel().getSelectedItem();
        if (l == null) {
            ctrlPres.mostraError("Valoracio no seleccionada. Selecciona la valoracio que vols eliminar");
        }
        else {
            if (ctrlPres.mostraConfirmacio("Eliminar valoracio", "Estas segur que vols eliminar la valoracio seleccionada? Si es aixi, prem aceptar, altrament, cancelar")) {
                eliminarValoracioRatings(l.get(0), l.get(1));
                setRows();
            }

        }
        
    }

    //Pre:
    //Post: s'elimina del sistema la valoració de l'usuari idUser per a l'ítem "idItem"
    private void eliminarValoracioRatings(String idUser, String idItem) {
        try {
            ctrlPres.eliminarValoracioRatings(idUser, idItem);
            ctrlPres.mostraInfo("Valoracio eliminada", "La valoracio seleccionada s'ha eliminat correctament");
        } 
        catch (ExcepcionsRecomanador e) {
            ctrlPres.mostraError(e.toString());
        }
        //No s'hauria d'arribar aqui.
        catch (NumberFormatException e) {
            ctrlPres.mostraError("L'user ID no es un numero");
        }
    }

    //Pre:
    //Post: retorna a la pantalla Menu Administrador
    @FXML
    public void returnA(ActionEvent event) {
        ctrlPres.canviaStage("MenuA");
    }

    //Pre:
    //Post: canvia a la pantalla d'afegir una valoracio
    @FXML
    void showAfegirVal(ActionEvent event) {
        ctrlPres.canviaStage("AfegirValoracioA");
    }

    //Pre:
    //Post: comprova que la puntuació a introduir és vàlida
    @FXML
    void validaEntradaPunt(KeyEvent event) {
        String txt = txtNovaPuntuacio.getText();
        String txtNou = ctrlPres.ValidaNumeroOPuntAlFinal(txt);
        Boolean esValid = txt.equals(txtNou);
        if (!esValid) {
            txtNovaPuntuacio.setText(txtNou);
            txtNovaPuntuacio.end();
        }
    }


    //Pre:
    //Post: comprova que el valor introduit com a puntuació mínima és vàlid
    @FXML
    public void validaEntradaMIN(KeyEvent event) {
        String txt = puntMIN.getText();
        String txtNou = ctrlPres.ValidaNumeroOPuntAlFinal(txt);
        Boolean esValid = txt.equals(txtNou);
        if (!esValid) {
            puntMIN.setText(txtNou);
            puntMIN.end();
        }
    }

    //Pre:
    //Post: comprova que el valor introduit com a puntuació màxima és vàlid
    @FXML
    public void validaEntradaMAX(KeyEvent event) {
        String txt = puntMAX.getText();
        String txtNou = ctrlPres.ValidaNumeroOPuntAlFinal(txt);
        Boolean esValid = txt.equals(txtNou);
        if (!esValid) {
            puntMAX.setText(txtNou);
            puntMAX.end();
        }
    }

}
