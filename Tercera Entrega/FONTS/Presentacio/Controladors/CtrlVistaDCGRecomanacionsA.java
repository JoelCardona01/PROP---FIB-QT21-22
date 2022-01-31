package Presentacio.Controladors;


import java.io.FileNotFoundException;
import java.net.URL;

import java.util.ResourceBundle;
import java.util.Vector;

import Domini.Model.Pair;
import Excepcions.ExcepcionsRecomanador;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class CtrlVistaDCGRecomanacionsA implements Initializable{

    @FXML
    private Button btnMesInfo;

    @FXML
    private Button btnReturn;

    @FXML
    private Button btnValorar;

    @FXML
    private TextField nomFitxer;

    @FXML
    private Label conf1;

    @FXML
    private Label conf2;

    @FXML
    private Label conf3;

    @FXML
    private Label conf4;

    @FXML
    private Label conf5;

    @FXML
    private Label conf6;

    @FXML
    private Label conf7;

    @FXML
    private Label conf8;

    @FXML
    private Label conf9;

    @FXML
    private RadioButton rbEstrAct;

    @FXML
    private RadioButton rbTotesComb;

    private Label[] labels;

    private ControladorPresentacio ctrlPres;

    //Pre:
    //Post: S'inicalitza la instancia de ctrlPresentacio, els radio buttons i les labels.
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ctrlPres = ControladorPresentacio.getInstance();
        setToggleGroup();

        Label labelsAux[] = {conf1,conf2,conf3,conf4,conf5,conf6,conf7,conf8,conf9};
        labels = labelsAux;
    }

    private void setToggleGroup() {
        ToggleGroup tg = new ToggleGroup();
        rbEstrAct.setToggleGroup(tg);
        rbTotesComb.setToggleGroup(tg);
        rbEstrAct.setSelected(true);
    }

    @FXML
    void mostraInfo(ActionEvent event) {
        //El \n entre "numValoracionsDesconegudes" i  "numValoracionsConegudes" es degut a que el tamany de la finestra d'informacio
        //es petit i aquestes paraules no caben seguides, llavors per ajudar a la comprensio, es fa explicitament el salt de línia
        //i es fiquem uns pocs espais com si fos un "tabulador" per tal que s'entengui que es la continuacio de la linia anterior
        ctrlPres.mostraInfo("Format fitxer","El fitxer ha de seguir el format:\nnusuaris a demanar recomanacions\n(per cada usuari de nusuaris:)idUsuari numValoracionsConegudes numValoracionsDesconegudes numItemsARetornar\ntantes linies amb parelles <idItem puntuacio> com\n    numValoracionsConegudes\ntantes linies amb iditem com numValoracionsDesconegudes\nPots trobar un exemple del fitxer inputqueries al directori DATA.");   
    }

    //Pre:
    //Post: Es torna a la pantalla anterior, que es "MenuA"
    @FXML
    void returnA(ActionEvent event) {
        ctrlPres.canviaStage("MenuA");
    }

    //Pre:
    //Post: Es valoren les recomanacions amb el fitxer donat i s'indica a la pantalla els NDCG
    @FXML
    void valorar(ActionEvent event) {
        String filename = nomFitxer.getText();
        if (filename.equals("")) ctrlPres.mostraError("El nom del fitxer es buit, introdueix un nom de fitxer no buit");
        else if (!ctrlPres.consultaKnownIUnknownLlegits()) {
            ctrlPres.mostraError("Has de llegir primerament els fitxers known i unknown");
            boolean canviaFitxers = ctrlPres.mostraConfirmacio("Llegir fitxers", "Vols anar a la pantalla de llegir fitxers? Si es aixi pitja aceptar, altrament, cancelar");
            if (canviaFitxers) ctrlPres.canviaStage("EntrarDadesA");
        }
        else {
            try {
                if (rbEstrAct.isSelected()) {
                    Pair<Double, Pair<String, String> > res = ctrlPres.getQualitatRecomanacions(filename);
                    if (res != null) {
                        String s;
                        s = res.second.first;
                        s += "+\n" +res.second.second + ":\n";
                        s += "NDGC= " + res.first;
                        conf4.setText(s);
                    }
                }
                else {
                        Vector<Pair<Double, Pair<String, String> > > resultats = ctrlPres.getMillorConfiguracio(filename);
                        if (resultats != null) {
                            int i = 0;
                            for (Pair<Double, Pair<String, String> > res : resultats) {
                                String s;
                                s = res.second.first;
                                s += "+\n" +res.second.second + ":\n";
                                s += "NDGC= " + res.first;
                                labels[i].setText(s);
                                i++;
                            }
                            ctrlPres.mostraInfo("Calculs fets", "Ja s'han calculat els NDGC");
                        }
                        
                    }
                }
                catch (FileNotFoundException e) {
                    ctrlPres.mostraError("Fitxer no trobat");
                }
                catch (ExcepcionsRecomanador e1) {
                    ctrlPres.mostraError(e1.getMessage());
                }

                catch (Exception e2) {
                    ctrlPres.mostraError("Error greu, mira la descripcio en terminal");
                    e2.printStackTrace();
                }
            }
        }
}
