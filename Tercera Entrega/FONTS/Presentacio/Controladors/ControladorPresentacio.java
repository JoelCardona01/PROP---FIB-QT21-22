
package Presentacio.Controladors;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Vector;

import Excepcions.ExcepcionsRecomanador;
import Excepcions.ItemBuit;
import Excepcions.ItemNoExisteix;
import Excepcions.KMassaGran;
import Excepcions.PersonaActualNoValida;
import Excepcions.PuntuacioNoValida;
import Excepcions.RangNoValid;
import Excepcions.UsuariNoExisteix;
import Domini.ControladorsDomini.CtrlDomini;
import  Domini.DataInterface.FactoriaCtrl;
import Domini.Model.Pair;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ControladorPresentacio {

    private CtrlDomini controladorDomini;
    private HashMap<String,String> pathVistes;
    private static ControladorPresentacio singletonObject;
    private Stage stageActual;
    private boolean knownLlegit;
    private boolean unknownLlegit;
    private String idItemAModificar;

    //Per a aquesta entrega només hi ha una sola vista, en la proxima entrega de codi hi haura dues vistes, una per Administrador i l'altre per Usuari

    //Pre:
    //Post: Retorna la unica instancia possible (ja que ControladorPresentacio es singleton) de ControladorPresentacio
    public static ControladorPresentacio getInstance() {
        if (singletonObject == null) {
            singletonObject = new ControladorPresentacio();
        }
        return singletonObject;
    }

    public ControladorPresentacio() {
        pathVistes = new HashMap<String,String>();
        iniciaVistes();
        knownLlegit = false;
        unknownLlegit = false;
        FactoriaCtrl fc = FactoriaCtrl.getInstance();
        try {
            controladorDomini= fc.getCtrlDomini();
        }
        catch (PuntuacioNoValida e) {
            mostraError(e.toString());
        }
        catch (ExcepcionsRecomanador e) {
            mostraError(e.toString());
        }
    }

    //Pre:
    //Post: Inicialitza els noms de les vistes amb el seu path relatiu
    private void iniciaVistes() {
        String[] nomsVistes = {
            "DCGRecomanacionsA","DemanarRecomanacioU","EntrarDadesA","GestioAlgorismesA","GestioCalculDistanciesA",
            "GestioCalculDistanciesA","GestionarCompteU","GestionarValoracionsU","GestioUsuarisA","GestioValoracionsA",
            "AfegirValoracioA","AfegirItemA","Login","MenuA", "MenuU", "Registre", "ConsultarHistoricRecomanacionsU", "ValorarItemU", "GestioItemsA", "ModificarItemA"
        };
        for (String s : nomsVistes) pathVistes.put(s, "Presentacio/Vistes/"+s+".fxml");
    }

    //Pre:
    //Post: Es guarda un item a modificar per passar informacio entre pantalles
    public void setIdItemAModificar(String id) {
        idItemAModificar = id;
    }

    //Pre:
    //Post: Retorna un item a modificar per passar informacio entre pantalles
    public String getIdItemAModificar() {
        return idItemAModificar;
    }

    //Pre:
    //Post: Mostra un missatge d'error en pantalla
    public void mostraError(String mError) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setTitle("Error");
        alert.setContentText("ERROR: " + mError);
        alert.showAndWait();
    }

    //Pre:
    //Post: Mostra una pantalla de confirmacio per pantalla, i retorna true si es prem "OK", altrament, false
    public boolean mostraConfirmacio(String title, String cos) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(cos);
        Optional<ButtonType> result = alert.showAndWait();
        return (result.get() == ButtonType.OK);
    }

    //Pre:
    //Post: Inicialitza l'escena principal
    public void setStage(Stage s) {
        stageActual = s;
        stageActual.setOnCloseRequest(e -> closeProgram());
        stageActual.setResizable(false);
        stageActual.setTitle("Recomanador");
        try {
            Image image = new Image(new FileInputStream("../../DATA/ImatgesVistes/icon.png"));
            stageActual.getIcons().add(image);
        }
        catch (Exception e) {
            mostraError(e.toString());
        }
    }

    //Pre:
    //Post: Funcio associada al tancament del programa, demana a l'usuari si vol guardar els canvis i en cas afirmatiu, els guarda
    private void closeProgram() {
        if (controladorDomini.sessioIniciada()) {
            if (mostraConfirmacio("Guardar els canvis","Vols guardar els canvis fets? Si tanques aquesta finestra o pitjes cancelar, no es guardaran")) {
                controladorDomini.logout();
            }
        }
    }

    //Pre:
    //Post: Canvia d'escena a la indicada a "nomVista"
    public void canviaStage(String nomVista) {
        try {
            URL url = new File(pathVistes.get(nomVista)).toURI().toURL();
            Parent root = FXMLLoader.load(url);
            stageActual.setScene(new Scene(root));
            stageActual.show();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            mostraError("No s'ha pogut canviar de pantalla, mira l'error en terminal");
        } catch (IOException e) {
            e.printStackTrace();
            mostraError("No s'ha pogut canviar de pantalla, mira l'error en terminal");
        }
    }
    //Pre: S'ha iniciat sessio
    //Post: S'esborra el compte de l'usuari actual
    public void esborraCompte() throws PersonaActualNoValida {
        if (mostraConfirmacio("Esborrar Compte", "Estas segur que vols esborrar el teu compte?")) {
            controladorDomini.eliminarUsuariActiu();
            mostraInfo("Compte esborrat", "El teu compte s'ha esborrat correctament");
            canviaStage("Login");
        }
    }

    public void afegirItem (Vector<String> nomColumnes, Vector<Vector<String>> ValorsPerColumna) throws ItemBuit{
        controladorDomini.afegirItem(nomColumnes, ValorsPerColumna);
    }

    public void eliminarItem(String idItem) throws ItemNoExisteix {
        controladorDomini.eliminarItem(idItem);
    }

    public void eliminarUsuariRatings(String id) throws NumberFormatException, UsuariNoExisteix {
        controladorDomini.eliminarUsuariRatings(Integer.parseInt(id));
    }

    //Pre:
    //Post: Mostra una pantalla d'un cuadre d'informacio
    public void mostraInfo(String titol, String missatge) {
        Alert info = new Alert(Alert.AlertType.INFORMATION);
        info.setHeaderText(null);
        info.setTitle(titol);
        info.setContentText(missatge);
        info.showAndWait();
    }

    //Pre:
    //Post: Escriu les columnes indicades per "columnNames" a la taula "taula"
    public void setColumns(List<String> columnNames, TableView<ObservableList<String>> taula) {
        taula.getColumns().clear();
        for (int i = 0; i<columnNames.size(); i++) {
            final int finalIdx = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    columnNames.get(i)
            );
            column.setCellValueFactory(param ->
                    new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx))
            );
            taula.getColumns().add(column);
        }
    }
    //Pre:
    //Post: Escriu les columnes indicades per "columnNames" a la taula "taula" i fa que la taula sigui editable
    public void setEditableColumns(List<String> columnNames, TableView<ObservableList<String>> taula) {
        taula.getColumns().clear();
        for (int i = 0; i<columnNames.size(); i++) {
            final int finalIdx = i;
            TableColumn<ObservableList<String>, String> column = new TableColumn<>(
                    columnNames.get(i)
            );
            column.setCellValueFactory(param ->
                    new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx))
            );
            column.setCellFactory(TextFieldTableCell.forTableColumn());
            column.setOnEditCommit(param -> {
                param.getRowValue().set(finalIdx, param.getNewValue());
            });


            taula.getColumns().add(column);
        }
    }

    //Pre:
    //Post: Escriu les files indicades a "files" a la taula "taula"
    public void setRows(ArrayList<ArrayList<String>> files, TableView<ObservableList<String>> taula) {
        taula.getItems().clear();
        int nFiles = files.size();
        for (int i = 0; i<nFiles; i++) {
            taula.getItems().add(
                    FXCollections.observableArrayList(
                            files.get(i))
                    );
        }
    }

    //Pre:
    //Post: Es fa una cerca en la taula "taula" per la columna "columnaCerca" per l'string strCerca
    public void cercaEnTaula(String strCerca, int columnaCerca ,TableView<ObservableList<String>> taula, ArrayList<ArrayList<String>> totalFiles) {
        taula.getItems().clear();
        for (ArrayList<String> aL : totalFiles) {
            if (aL.get(columnaCerca).contains(strCerca)) {
                taula.getItems().add(
                    FXCollections.observableArrayList(aL)
                );
            } 
        }
    }
    //Pre:
    //Post: Es fa login
    public void iniciaInstancia(String username, String password) throws ExcepcionsRecomanador, FileNotFoundException{
        controladorDomini.login(username,password);
    }

    //Pre:
    //Post: Es fa una cerca a la taula "taula" on el conjunt de files total es "totalFiles" per la columna seleccionada
    //a chboxCerca on el contingut a cercar esta a "cerca"
    public void cercaAmbCHBox(String cerca, ChoiceBox<String> chboxCerca, List<String> columnNames, TableView<ObservableList<String>> taula, ArrayList<ArrayList<String>> totalFiles) {
        //Primer mirem per quina columna hem de fer la cerca
        int selected = 0;
        boolean trobat = false;
        String strSelected = chboxCerca.getValue();
        int size = columnNames.size();
        for (int i = 0; i<size && !trobat; i++){
            if (columnNames.get(i).equals(strSelected)) {
                trobat = true;
                selected = i;
            }
        }
        //Ara cerquem per aquesta columna i filtrem el resultat
        cercaEnTaula(cerca, selected, taula, totalFiles);

    }

    //Pre:
    //Post: Llegeix els items amb nom de fitxer "nom" a domini.
    public void informaFitxerItems(String nom) throws FileNotFoundException{
        controladorDomini.llegeixAllItems(nom);
    }
    //Pre:
    //Post: Llegeix els ratings amb nom de fitxer "nom" a domini.
    public void informaFitxerRatings(String nom) throws Exception{
        if(nom.equals("usuarisRatings")) controladorDomini.llegirRatingsBD();
        else controladorDomini.informaRatings(nom);
        
    }
    //Pre:
    //Post: Llegeix el fitxer known amb nom de fitxer "nom" a domini.
    public void informaFitxerKnown(String nom) throws Exception{
        controladorDomini.informaKnown(nom);
        knownLlegit = true;
    }
    //Pre:
    //Post: Llegeix el fitxer unknown amb nom de fitxer "nom" a domini.
    public void informaFitxerUnKnown(String nom) throws Exception{
        controladorDomini.informaUnknown(nom);
        unknownLlegit = true;
    }
    
    public Double demanaMin(){
        return controladorDomini.getPuntuacioMinima();
    }

    public Double demanaMax(){
        return controladorDomini.getPuntuacioMaxima();
    }

    public void canviaMiniMax(Double min, Double max) throws RangNoValid{
        controladorDomini.canviaMiniMax(min,max);
    }

    public void recalculaClusters() throws ExcepcionsRecomanador {
        controladorDomini.recalculaClusters();
    }

    public String demanaEstrategiaActual(){
        return controladorDomini.demanaEstrategiaActual();
    }

    public void demanaCanviEstrategia(String nom){
        controladorDomini.canviaEstrategia(nom);
    }

    public int demanaKkmeans() {
        return controladorDomini.demanaKKmeans();
    }

    public int demanaKknearest() {
        return controladorDomini.demanaKKnearest();
    }

    public void canviaKKmeans(int e) throws KMassaGran {
        controladorDomini.canviaKKmeans(e);
    }

    public void canviaKKnearest(int e) throws KMassaGran {
        controladorDomini.canviaKKnearest(e);
    }

    public String demanaDistanciaActual() {
        return controladorDomini.demanaDistanciaActual();
    }

    public void demanaCanviDistancia(String e) throws ExcepcionsRecomanador {
        controladorDomini.canviaEstrategiaDistancia(e);
    }
    
    public void guardarEstat(){
        controladorDomini.guardarRatings();
        controladorDomini.guardarItemsBD();
    }

    public void logout() {
        controladorDomini.logout();
    }

    public void registrar(String user, String pass) throws ExcepcionsRecomanador {
        controladorDomini.registrar(user, pass);
    }
    //Pre:
    //Post: Si el text conte un caracter al final diferent d'un numero, s'elimina aquest numero i retorna fals(text no era valid)
    //      Altrament si el text, com a ultim caracter conte un numero, retorna true (el text es valid)
    public String ValidaNumeroOPuntAlFinal(String txt) {
        if (txt.length() >0) {
            char c = txt.charAt(txt.length() - 1);
            if (c != '.' && (c <'0' || c > '9')) {
                txt = txt.substring(0, txt.length()-1);
                return txt;
            }
        }
        return txt;
    }
    //Pre:
    //Post: Comprova si hi ha un numero al final, en aquest cas no fa res, altrament, esborra aquest caracter i retorna l'string
    public String ValidaNumeroAlFinal(String txt) {
        if (txt.length() >0) {
            char c = txt.charAt(txt.length() - 1);
            if (c <'0' || c > '9') {
                txt = txt.substring(0, txt.length()-1);
                return txt;
            }
        }
        return txt;
    }
    //Pre:
    //Post: Retorna el DCG de les configuracions disponibles segons les recomanacions demanades pel fitxer "filename"
    public Vector<Pair<Double, Pair<String, String> > > getMillorConfiguracio(String filename) throws FileNotFoundException, ExcepcionsRecomanador {
        Pair< Vector<Integer>, Pair< Vector<Vector<String>>, Vector<Integer> > > entrada = llegeixInputQueries(filename);
        if (entrada == null) {
            return null;
        }
        else return controladorDomini.BuscaMillorConfiguracio(entrada.first, entrada.second.first, entrada.second.second);
    }

    public Pair <Double, Pair<String, String>> getQualitatRecomanacions(String filename) throws FileNotFoundException, ExcepcionsRecomanador {
        Pair< Vector<Integer>, Pair< Vector<Vector<String>>, Vector<Integer> > > entrada = llegeixInputQueries(filename);
        if (entrada == null) {
            return null;
        }
        else return controladorDomini.getQualitatRecomanacions(entrada.first, entrada.second.first, entrada.second.second);
    } 

    //Pre:
    //Post: LLegeix el fitxer InputQueries i retorna, els items que es consideraran, el nombre d'items a retornar, 
    //i els usuaris a valorar. 
    public  Pair< Vector<Integer>, Pair< Vector<Vector<String>>, Vector<Integer> > > llegeixInputQueries (String filename) throws FileNotFoundException, ExcepcionsRecomanador{
        if (knownLlegit && unknownLlegit) {
            LinkedList<String> dadesQueries = new LinkedList<String>();
            FileReader fr = null;
            fr = new FileReader("../../DATA/"+filename);       
            
            
            try (Scanner scan = new Scanner(fr)) {
                while(scan.hasNextLine()) dadesQueries.add(new String(scan.nextLine()));
            }

            //Despres de emmagatzemar totes les dades
            int nUsuaris = Integer.valueOf(dadesQueries.get(0));  //Nombre de queries a processar

            Vector<Integer> UsuarisAValorar = new Vector<Integer>(nUsuaris); //vector amb tots els usuaris a valora_recomanacio
            Vector<Vector<String>> Items = new Vector<Vector<String>>(); //vector amb el conjunt de items a valorar. Items[i] conté tots els items a predir per UsuarisAValorar[i]
            Vector<Integer> nItemsARetornar = new Vector<Integer>(); //vector amb el nombre d'items a retornar que es vol per cada recomanacio. nItemsARetornar[i] correspont al nombre a retornar pe rla valoració sobre l'usuari UsuarisAValorar[i]
            for(int i = 1; i < dadesQueries.size(); i++){ //comencem x 1 pq la primera es el nº de queries
                
                //valors = { "userid, lengthknown, lengthunknown, Q"}  
                String valors[] = dadesQueries.get(i).split(" ");
                UsuarisAValorar.add(Integer.valueOf(valors[0]));
                int lenghtknown = Integer.valueOf(valors[1]);   //Assignem el valor de lenght known
                int lengthunknown = Integer.valueOf(valors[2]); //Assingem el valor de lenght unknown
                int q = Integer.valueOf(valors[3]); //Assignem la Q (nombre de recomanacions a fer)
                nItemsARetornar.add(q);
                ++i; //avancem en la llista (\n)
                Vector<String> ItemsPredir = new Vector<String>(); 
    
                for (int j = 0; j < lenghtknown; ++j) ++i; //"llegim" totes les dades known, com que ja les tenim guardades, no les tractem
                for (int j = 0; j < lengthunknown; ++j){  //guardem quins son els items de Unknown que es vol predir la valoracio i calcular-ne la qualitat (podria ser que no fossin tots els de unknown per aquell usuari)
                    ItemsPredir.add(dadesQueries.get(i)); 
                    ++i;
                }
                --i;
                Items.add(ItemsPredir);
            }
            Pair< Vector<Integer>, Pair< Vector<Vector<String>>, Vector<Integer> > > result = new Pair< Vector<Integer>, Pair< Vector<Vector<String>>, Vector<Integer> > >();
            Pair< Vector<Vector<String>>, Vector<Integer> > itemsINItems = new Pair< Vector<Vector<String>>, Vector<Integer> >();
            itemsINItems.first = Items;
            itemsINItems.second = nItemsARetornar;
            result.first = UsuarisAValorar;
            result.second = itemsINItems;

            return result;    
        }
        else {
            mostraError("Has de llegir primerament els fitxers known i unknown");
            boolean canviaFitxers = mostraConfirmacio("Llegir fitxers", "Vols anar a la pantalla de llegir fitxers? Si es aixi pitja aceptar, altrament, cancelar");
            if (canviaFitxers) canviaStage("EntrarDadesA");
            return null;
        } 
    }

    public ArrayList<ArrayList<String>> getValoracionsUser(List<String> columnNamesNoID) {
        return controladorDomini.getValoracionsUser(columnNamesNoID);
    }

    public ArrayList<String> getNomColumnesItem() {
        return controladorDomini.getNomColumnesItem();
    }

    public void canviarValoracio(String idItem, double puntuacioNova) throws ExcepcionsRecomanador {
        controladorDomini.canviarValoracio(idItem, puntuacioNova);
    }

    public void eliminarValoracio(String idItem) throws ExcepcionsRecomanador {
        controladorDomini.eliminarValoracio(idItem);
    }

    public String getUsernameUsuari() {
        return controladorDomini.getUsernameUsuari();
    }

    public void canviarContrasenya(String contrasenyaNova) throws PersonaActualNoValida {
        controladorDomini.canviarContrasenya(contrasenyaNova);
    }

    public void canviarNomUsuari(String nouNom) throws ExcepcionsRecomanador {
        controladorDomini.canviarNomUsuari(nouNom);
    }

    public ArrayList<ArrayList<String>> getRecomanacioArrList(String[] items, int nSortida) throws ExcepcionsRecomanador {
        return controladorDomini.getRecomanacioArrList(items, nSortida);
    }

    public ArrayList<ArrayList<String>> getItemsNoValorats() {
        return controladorDomini.getItemsNoValorats();
    }

    public ArrayList<ArrayList<String>> getItems() {
        return controladorDomini.getItems();
    }

    public ArrayList<ArrayList<String>> getItemAModificar() {
        return controladorDomini.getItemAModificar(idItemAModificar);
    }

    public ArrayList<ArrayList<String>> getIDsUsuaris() {
        return controladorDomini.getIDsUsuaris();
    }

    public void canviarValorsAtribut(String id, Vector<String> valorsNous, String nomColumna) {
        controladorDomini.canviarValorsAtribut(id, valorsNous, nomColumna);
    }

    public void afegirValoracio(String itemID, double puntuacioNova) throws ExcepcionsRecomanador {
        controladorDomini.afegirValoracio(itemID, puntuacioNova);
    }

    public ArrayList<ArrayList<String>> getValoracionsUsersRatings(List<String> columnNamesNoID) {
        return controladorDomini.getValoracionsUsuarisRatings(columnNamesNoID);
    }

    public void canviarValoracioRatings(String idUser, String idItem, double puntuacioNova) throws NumberFormatException, ExcepcionsRecomanador {
        controladorDomini.canviarValoracioRatings(Integer.parseInt(idUser), idItem, puntuacioNova);
    }

    public void eliminarValoracioRatings(String idUser, String idItem) throws NumberFormatException, ExcepcionsRecomanador {
        controladorDomini.eliminarValoracioRatings(Integer.parseInt(idUser), idItem);
    }

    public ArrayList<ArrayList<String>> getUserRatingsID() {
        return controladorDomini.getUsersRatingsID();
    }

    public ArrayList<ArrayList<String>> getItemsNoValoratsUserRatings(String userID) {
        return controladorDomini.getItemsNoValoratsUserRatings(userID);
    }

    public void valoraItemUserRatings(String userID, String itemID, Double puntuacio) throws NumberFormatException, ExcepcionsRecomanador {
        controladorDomini.afegirValoracioRatings(Integer.parseInt(userID), itemID, puntuacio);

    }

    //Pre:
    //Post: Retorna true si s'han llegit els dos fitxers known i unknown, altrament false
    public boolean consultaKnownIUnknownLlegits() {
        return (knownLlegit && unknownLlegit);
    }

    public ArrayList<ArrayList<String>> getHistoricRecomanacio() {
        return controladorDomini.getHistoricRecomanacio();
    }

    public boolean userTeValoracions() {
        return controladorDomini.userTeValoracions();
    }



  
    
}

//Classe programada per Marina Alapont, Daniel Pulido i Simon Oliva