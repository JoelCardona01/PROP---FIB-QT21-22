package Presentacio.Controladors;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CtrlVistaEntrarDadesA implements Initializable{

    @FXML
    private Button btnActualitzaItems;

    @FXML
    private Button btnActualitzaKnown;

    @FXML
    private Button btnActualitzaRatings;

    @FXML
    private Button btnActualitzaTots;

    @FXML
    private Button btnActualitzaUnkown;

    @FXML
    private Button btnReturn;

    @FXML
    private TextField nomFitxerItems;

    @FXML
    private TextField nomFitxerKnown;

    @FXML
    private TextField nomFitxerRatings;

    @FXML
    private TextField nomFitxerUnknown;

    private ControladorPresentacio ctrlPres;

    //Pre:
    //Post: Inicialitza la instancia a ctrlPresentacio
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ctrlPres = ControladorPresentacio.getInstance();
    }
    
    //Pre: 
    //Post: els ítems presents al sistema (si n'hi havia) anteriors a la lectura del nou fitxer són eliminats
    //Post: llegeix l'arxiu "nomFitxer.csv" i guarda els ítems que conté al sistema
    @FXML
    public void actualitzaItems(ActionEvent event) {
        String nomFitxer = nomFitxerItems.getText();
        try {
            if (nomFitxer == "") ctrlPres.mostraError("No has introduit cap nom.");
            else {
                ctrlPres.informaFitxerItems(nomFitxer);
                ctrlPres.mostraInfo("Actualitzacio completa", "S'ha actualitzat el fitxer d'items correctament");
            }
        }
        catch(FileNotFoundException e) {
            ctrlPres.mostraError("no existeix cap fitxer amb el nom " + nomFitxer + " a la carpeta DATA.");
        }
    }

    //Pre: Els ítems que es valoren al fitxer han de ser coherents amb els ítems del fitxer d'ítems 
    //Post: les valoracions conegudes presents al sistema (si n'hi havia) anteriors a la lectura del nou fitxer són eliminades
    //Post: llegeix l'arxiu "nomFitxer.csv" i guarda les valoracions que conté al sistema
    @FXML
    public void actualitzaKnown(ActionEvent event) {
        String nomFitxer = nomFitxerKnown.getText();
        try {
            if (nomFitxer == "") ctrlPres.mostraError("No has introduit cap nom.");
            else {
                ctrlPres.informaFitxerKnown(nomFitxer);
                ctrlPres.mostraInfo("Actualitzacio completa", "S'ha actualitzat el fitxer de known correctament");
            }
        }
        catch(Exception e) {
            ctrlPres.mostraError("no existeix cap fitxer amb el nom " + nomFitxer + " a la carpeta DATA.");
        }
    }

    //Pre: Els ítems que es valoren al fitxer han de ser coherents amb els ítems del fitxer d'ítems 
    //Post: les valoracions i els usuaris presents al sistema (si n'hi havia) anteriors a la lectura del nou fitxer són eliminades
    //Post: llegeix l'arxiu "nomFitxer.csv" i guarda les valoracions i els usuaris que conté al sistema
    @FXML
    public void actualitzaRatings(ActionEvent event) {
        String nomFitxer = nomFitxerRatings.getText();
        try{
            if (nomFitxer == "") ctrlPres.mostraError("No has introduit cap nom.");
            else {
                ctrlPres.informaFitxerRatings(nomFitxer);
                ctrlPres.mostraInfo("Actualitzacio completa", "S'ha actualitzat el fitxer de ratings correctament");
            }
        }
        catch(Exception e) {
            ctrlPres.mostraError("no existeix cap fitxer amb el nom " + nomFitxer + " a la carpeta DATA.");
        }
    }

    //Pre: Els ítems que es valoren al fitxer han de ser coherents amb els ítems del fitxer d'ítems
    //Post: les valoracions desconegudes presents al sistema (si n'hi havia) anteriors a la lectura del nou fitxer són eliminades
    //Post: llegeix l'arxiu "nomFitxer.csv" i guarda les valoracions que conté al sistema
    @FXML
    public void actualitzaUnknown(ActionEvent event) {
        String nomFitxer = nomFitxerUnknown.getText();

        try{
            if (nomFitxer == "") ctrlPres.mostraError("No has introduit cap nom.");
            else {
                ctrlPres.informaFitxerUnKnown(nomFitxer);
                ctrlPres.mostraInfo("Actualitzacio completa", "S'ha actualitzat el fitxer d'unknown correctament");
            }
        }
        catch(Exception e) {
            ctrlPres.mostraError("no existeix cap fitxer amb el nom " + nomFitxer + " a la carpeta DATA.");
        }
    }

    //Pre: Els ítems que es valoren al fitxer han de ser coherents amb els ítems del fitxer d'ítems
    //Post: per a cada nom de fitxer introduit, elimina les dades preexistens en el sistema corresponents al tipus d'arxiu que es llegirà (si n'hi havia)
    //Post: llegeix tots els arxius escrits en els camps de text i guarda les dades que conté cadascun al sistema
    @FXML
    public void actualitzaTots(ActionEvent event) {
        boolean lecturaCorrecta = true;
        boolean algunLlegit = false;
        try{
            if (nomFitxerItems.getText() != "") {
                ctrlPres.informaFitxerItems(nomFitxerItems.getText());
                algunLlegit = true;
            }
        }
        catch(FileNotFoundException e) {
            lecturaCorrecta = false;
            ctrlPres.mostraError("no existeix cap fitxer amb el nom " + nomFitxerItems.getText() + " a la carpeta DATA.");
        }

        try{
            if (nomFitxerKnown.getText() != "") {
                ctrlPres.informaFitxerKnown(nomFitxerKnown.getText());
                algunLlegit = true;
            }
        }
        catch(Exception e) {
            lecturaCorrecta = false;
            ctrlPres.mostraError("no existeix cap fitxer amb el nom " + nomFitxerKnown.getText() + " a la carpeta DATA.");
        }

        try{
            if (nomFitxerRatings.getText() != "") {
                ctrlPres.informaFitxerRatings(nomFitxerRatings.getText());
                algunLlegit = true;
            }
        }
        catch(Exception e) {
            lecturaCorrecta = false;
            ctrlPres.mostraError("no existeix cap fitxer amb el nom " + nomFitxerRatings.getText() + " a la carpeta DATA.");
        }

        try{
            if (nomFitxerUnknown.getText() != "") {
                ctrlPres.informaFitxerUnKnown(nomFitxerUnknown.getText());
                algunLlegit = true;
            }
        }
        catch(Exception e) {
            lecturaCorrecta = false;
            ctrlPres.mostraError("no existeix cap fitxer amb el nom " + nomFitxerUnknown.getText() + " a la carpeta DATA.");
        }
        if (algunLlegit) {
            if (lecturaCorrecta) ctrlPres.mostraInfo("Actualitzacio completa", "S'han actualitzat els fitxers correctament");
        }
        else ctrlPres.mostraError("no has escrit cap nom.");
    }

    //Pre:
    //Post: Es retorna a la pantalla anterior que es Menu Administrador
    @FXML
    public void returnA(ActionEvent event) {
        ctrlPres.canviaStage("MenuA");
    }

}
