package Domini.ControladorsDomini;

import java.io.*;
import java.util.*;

import Domini.Model.*;
import Domini.DataInterface.* ;
import Excepcions.*;
public class CtrlDomini{
     
    private CtrlItem ctrlItemFile;  //Ctrl de persistencia que gestiona els items
    private CtrlRating ctrlRatingsFile;  //Ctrl de persistencia que gestiona els usuaris de Ratings
    private CtrlUsuariActiu ctrlUsuarisActius; //Ctrl de persistencia que gestiona els usuaris registrats
    private CtrlRecomanacions ctrlRecomanacions; //Ctrl de persistencia que gestiona les recomanacions fetes pel sistema
    private CtrlEstat estatCtrl; //Ctrl de persistencia que guarda l'estat del programa
    private CtrlClusters clustersCtrl; //Ctrl de persistencia que gestiona els clusters i centroides que s'estan usant per a Collaborative filtering i Hybid Approaches
    private Persona PersonaActual; //indica quina persona esta usant en aquest moment el programa.
    private int col_id; //Columna on esta l'id
    private int n_cols = 0; //Numero d'atributs totals que hi ha
    private String estrategia;  //Estrategia usada en el moment actual per les recomanacions
    private String estrategiaDistancia; //Estrategia usada actualment per la distancia
    private int kkmeans;  //la variable k per el kmeans
    private int kknearest;  //la variable per els k items mes propers
    private HashMap<String, Item> Items; //Map amb les dades dels Items on l'String és el id del item
    private Vector<Columna> Columnes;  //Vector amb les columnes
    private HashMap<Integer, UsuariFitxers> UsuarisRatings; //Conjunt d'usuaris de ratings.db
    private HashMap<Integer, UsuariFitxers> UsuarisKnown;  //Conjunt d'usuaris de known
    private HashMap<Integer, UsuariFitxers> UsuarisUnknown;  //Conjunt d'usuaris de unknown
    private Vector<UsuariFitxers>[] clusters; 
    private HashMap<String,Double>[] centroides;  
    private int ultimID; //l'ultim id d'usuari que s'ha atorgat automaticament
    private Double puntuacioMin;
    private Double puntuacioMax;
    private int ultimIDItem; //l'ultim id d'item que s'ha atorgat automaticament
    private int ultimIDRecomanacio; //l'ultim id de recomanacio que s'ha atorgat

    public CtrlDomini() throws ExcepcionsRecomanador{
      inicialitzar();
    }
    //Pre:
    //Post: S'inicialitzen les variables necessaries.
    public void inicialitzar() throws ExcepcionsRecomanador{
        FactoriaCtrl fc = FactoriaCtrl.getInstance();
        ctrlItemFile = fc.getCtrlItem();
        ctrlRatingsFile = fc.getCtrlRating();
        ctrlUsuarisActius = fc.getCtrlUsuariActiu();
        estatCtrl = fc.getCtrlEstat();
        clustersCtrl = fc.getCtrlClusters();
        ctrlRecomanacions = fc.getCtrlRecomanacions();
        Items = new HashMap<String,Item>();
        Columnes = new Vector<Columna>();
        UsuarisRatings = new HashMap<Integer, UsuariFitxers>();
        UsuarisKnown = new HashMap<Integer, UsuariFitxers>();
        UsuarisUnknown = new HashMap<Integer, UsuariFitxers>();
        centroides = new HashMap[0];
        clusters = new Vector[0];
        iniciarConfig();
    }

    //Funcio feta per Joel Cardona
    //Pre: el fitxer configRecomanador.json esta omplert, ja sigui amb la configuracio per defecte o be amb la configuracio feta per l'administrador
    //Post: s'inicialitzen totes les variables que l'admin pot modificar i volguer guardar d'execucio a execucio
    public void iniciarConfig(){
      Pair<Integer, Integer> ks = estatCtrl.getKsBD();
      Pair<String, String> strategies = estatCtrl.getEstrategiesBD();
      Pair<Double, Double> puntuacions = estatCtrl.getIntervalPuntuacionsBD();
      ultimID = estatCtrl.getUltimID();
      ultimIDItem = estatCtrl.getUltimIDItem();
      ultimIDRecomanacio=estatCtrl.getUltimIDRecomanacio();
      estrategia = strategies.second;  
      estrategiaDistancia = strategies.first; 
      kkmeans=ks.first; 
      kknearest=ks.second; 
      puntuacioMax = puntuacions.second;
      puntuacioMin = puntuacions.first;
    }

    //Funcio feta per Marina Alapont i Joel Cardona.
    //Pre: Es rep un nom d'usuari
    //Post: Es crea el usuari amb el username rebut com a parametre i es guarda la instancia creada. 
    public void login(String usuari, String contrasenya) throws ExcepcionsRecomanador, FileNotFoundException {
      if (usuari.equals("Admin")) {  // Si la persona que ha fet log in es l'admin, creem una instancia de PersonaActual de tipus Admin
        Pair<Integer, Vector<Pair<String, Double> >> infoUser = ctrlUsuarisActius.getUsuariActiu(usuari, contrasenya); //Si les credencials d'admin son incorrectes, es mostrara un error
        PersonaActual= new Admin(infoUser.first, usuari, contrasenya); //el Usuari de l'Administrador es sempre Admin
      }

      if(Items.size()==0){  //si no estan carregats en memoria els items (estem al primer login de l'execucio)
        llegirItemsBD();    //llegim el json d'items
        if (Items.size()==0) throw new BaseDeDadesBuida("items"); // Si el json esta buit, vol dir que no s'ha carregat mai el ficher. Enviem un avis de que cal entrar-lo al sistema.
      } 

      if (UsuarisRatings.size()==0){
        llegirRatingsBD(); //si no esta ja carregat a memoria, el carreguem llegint de usuarisRatings.json
        if(UsuarisRatings.size()==0)throw new BaseDeDadesBuida("ratings.db"); // Si el json esta buit, vol dir que no s'ha carregat mai el ficher. Enviem un avis de que cal entrar-lo al sistema.
      } 
       
      if(centroides.length == 0){  //Si no estan a memoria els centroides (i ni els clusters) els llegim del fitxer json
        llegirClustersCentroides();
        if(centroides.length == 0){  //Si el fitxer esta buit, i doncs no s'han calculat mai encara els calculem   
          recalculaClusters();      //Es recalculen nomes doncs, al primer log in de la primera excecucio i quan l'admin demana recalcular-los.
        }
      }

      if(!usuari.equals("Admin")){ // Si la persona que ha fet log in es un usuari del sistema, creem una instancia de PersonaActual de tipus UsuariActiu
        Pair<Integer, Vector<Pair<String, Double> >> infoUser = ctrlUsuarisActius.getUsuariActiu(usuari, contrasenya);  //Si les credencials introduides son incorrectes, es mostrara un error
        Vector<Pair<String,Double>> vals = infoUser.second;
        // creem la instancia de UsuariActiu i li atorguem les seves Valoracions fetes i les recomanacions rebudes
        PersonaActual=new UsuariActiu(infoUser.first, usuari,contrasenya); 
        UsuariActiu usr = PersonaActual.retornaUsuariActiu();

        for (int i= 0; i< vals.size(); ++i){
          Valoracio val = new Valoracio(vals.get(i).second, Items.get(vals.get(i).first), usr);
          usr.afegirValoracio(val);
        }
        
        HashMap<Integer, Vector<Pair<String, Double>>> recomanacions = ctrlRecomanacions.readRecomanacions(infoUser.first);
        for (HashMap.Entry<Integer,Vector<Pair<String,Double>>> r : recomanacions.entrySet()) {
          TreeSet<Valoracio> recomanacionsRebudes = new TreeSet<Valoracio>(new ComparatorValoracio());
           for (int j=0; j < r.getValue().size(); ++j) {
            Valoracio v = new Valoracio (r.getValue().get(j).second,Items.get(r.getValue().get(j).first),usr);
            recomanacionsRebudes.add(v);
          }
          Recomanacio rec = new Recomanacio(r.getKey(),usr, recomanacionsRebudes);
          usr.afegirRecomanacio(rec);
        }
      }
    } 

    //Pre:
    //Post: Es tanca la sessio de la Persona actual, es guarda els canvis fets segons el tipus de persona que havia fet login 
    //     (aixi asegurem que els canvis no es perden per imprevistos, tot i que encara seguiran en memoria per al proxim login si no es para l'execucio).
    public void logout() {
      if (PersonaActual.retornaUsuariActiu()!=null){ //Si la persona que hi ha connectada es un usuari actiu es guarda la informacio que aquest ha pogut modificar.
        guardarCanvisUsuari();
        guardarRecomanacions();
        estatCtrl.saveState(kkmeans, kknearest, estrategiaDistancia, estrategia, puntuacioMin, puntuacioMax, ultimID, ultimIDItem, ultimIDRecomanacio);
      }
      else { //Si la persona que hi ha connectada es un administrador es guarda la informacio que aquest ha pogut modificar.
        guardarClustersCentoids();
        guardarRatings();
        guardarItemsBD();
        estatCtrl.saveState(kkmeans, kknearest, estrategiaDistancia, estrategia, puntuacioMin, puntuacioMax, ultimID, ultimIDItem, ultimIDRecomanacio);
      }
  
      PersonaActual = null;
    }

    //Pre:
    //Post: retorna cert si algu ha iniciat sessio, fals en cas contrari.
    public boolean sessioIniciada() {
      return PersonaActual != null;
    }

    //Funcio feta per Marina Alapont.
    //Pre: Es rep un nom d'usuari i una contrasenya per enregistrar.
    //Post: Es registra un usuari, donant-i un id generat automaticament, el nom d'usuari passat per parametre i la contrasenya passada per parametre. Es guarda aquesta informació al fitxer json.
    public void registrar(String username, String contrasenya) throws ExcepcionsRecomanador{
      //cal asegurar que hi ha els fitxers items i ratings.db carregats, ja que els necessitem per assegurar-nos de que no otorguem ids repetits entre els usuaris de Ratings i els usuaris actius.
      if(Items.size()==0){  
        llegirItemsBD();    
        if (Items.size()==0) throw new BaseDeDadesBuida("items"); // Si el json esta buit, vol dir que no s'ha carregat mai el ficher. Enviem un avis de que cal entrar-lo al sistema.
      } 
      if (UsuarisRatings.size()==0){
        llegirRatingsBD(); 
        if(UsuarisRatings.size()==0)throw new BaseDeDadesBuida("ratings.db"); // Si el json esta buit, vol dir que no s'ha carregat mai el ficher. Enviem un avis de que cal entrar-lo al sistema.
      } 
      int id= creaSeguentID();
      Boolean existeix = ctrlUsuarisActius.existeixUsuari(username); //retorna true si el username ja esta siguent usat per un altre usuari, false en cas contrari.
      if (!existeix){   
        Vector<Pair<String, Double>> v = new Vector<Pair<String, Double>>();
        ctrlUsuarisActius.saveUser(id, username, contrasenya, v); //S'enregistra les credencials del nou usuari
        estatCtrl.saveState(kkmeans, kknearest, estrategiaDistancia, estrategia, puntuacioMin, puntuacioMax, ultimID, ultimIDItem, ultimIDRecomanacio); //per tal de guardar correctament el valor de ultimId

      } 
      else throw new UsernameJaEnUs(username); 
    }
    
    //Funcio feta per Joel Cardona.
    //Pre: 
    //Post: Es crea automaticament un id per poder otorgar-li a un usuari que es vol registrar-se. Es fa tenint en compte l'últim id otorgat i comprovant que cap altre usuari del sistema el té.
    private int creaSeguentID() {
      for(int i = ultimID;;++i){
        if(!ctrlUsuarisActius.existeixUsuari(i) && UsuarisRatings.get(i) == null){
          ultimID = i;
          return i;
        }
      }
    }


    //Pre: Es rep com a parametre un id d'item.
    //Post: Es retorna el item amb identificador id, si no existeix retorna null.
    public Item getItem(String id){ 
      return Items.get(id);
    }
  
    //Pre: 
    //Post: Es retorna el conjunt de tots els items.
    public HashMap<String, Item> getAllItems(){ 
      return Items;
    }
  
    //Pre: Existeix una columna identificada amb numero nC.
    //Post: Retorna la columna identificada per nC.
    public Columna getColumna(int nC){ 
      return Columnes.get(nC);
    }
  
    //Pre:
    //Post: Retorna el conjunt de totes les columnes.
    public Vector<Columna> getAllColumnas(){
      return Columnes;
    }


    //Pre:
    //Post: Retorna el conjunt de tots els usuaris del conjunt UsuarisRatings.
    public HashMap<Integer, UsuariFitxers> getAllUsuarisRatings() {
      return UsuarisRatings;
    }

    //Pre:
    //Post: Retorna el conjunt de tots els usuaris del conjunt Usuaris Known.
    public HashMap<Integer, UsuariFitxers> getAllUsuarisKnown() {
      return UsuarisKnown;
    }

    //Pre: Existeix un usuari identificat per id al conjunt d'UsuarisRatings o al conjunt d'UsuarisKnown.
    //Post: Retorna la instancia del Usuari pertanyent a algun fitxer d'entrada.
    public UsuariFitxers getUsuari(Integer id){
      if (UsuarisRatings.containsKey(id)) return UsuarisRatings.get(id);
      else if (UsuarisKnown.containsKey(id)) return UsuarisKnown.get(id);
      else return null;
    }

    //Pre:
    //Post: Retorna el valor de la puntuacio mes baixa al sistema.
    public Double getPuntuacioMinima(){
      return puntuacioMin;
    }

    //Pre:
    //Post: Retorna el valor de la puntuacio mes alta al sistema.
    public Double getPuntuacioMaxima(){
      return puntuacioMax;
    }

    //Pre:
    //Post: Retorna el nom de l'estrategia de recomanacio actual.
    public String demanaEstrategiaActual(){
        return estrategia;
    }

    //Pre: El string rebut per parametre es CollaborativeFiltering, HybridApproaches o ContentBasedFiltering.
    //Post: Es canvia l'estrategia de recomanacio actual per la rebuda per parametre.
    public void canviaEstrategia(String e){
        estrategia=e;
    }

    //Pre:
    //Post: Retorna el nom de l'estrategia per al calcul de distancies utilitzada.
    public String demanaDistanciaActual() {
      return estrategiaDistancia;
    }

    //Pre: El string rebut per parametre es DistanciaMitjana, DistanciaEuclidiana o  DistanciaPonderada.
    //Post: Es canvia l'estrategia de distancia actual per la rebuda per parametre.
    public void canviaEstrategiaDistancia(String e) throws ExcepcionsRecomanador{
      estrategiaDistancia = e;
      recalculaClusters(); 
  }

    //Pre:
    //Post: Retorna el numero de k del kmeans.
    public int demanaKKmeans(){
      return kkmeans;
    }

    //Pre: Es rep per parametre un numero menor al numero d'usuaris del conjunt d'Usuaris.
    //Post: Es canvia la variable k de kmeans.
    public void canviaKKmeans(int k) throws KMassaGran{
      if (k>UsuarisRatings.size()) throw new KMassaGran(k, UsuarisRatings.size());
      kkmeans=k;
    }

    //Pre:
    //Post: Retorna el numero de k del knearest.
    public int demanaKKnearest(){
      return kknearest;
    }
    
    //Pre: Es rep per parametre un numero menor al numero d'items del conjunt d'Items.
    //Post: Es canvia la variable k de knearests.
    public void canviaKKnearest(int k) throws KMassaGran{
      if (k>Items.size()) throw new KMassaGran(k, Items.size());
      kknearest=k;
    }

    //Pre: Es rep per parametre el nom del fitxer de Ratings.
    //Post: Es llegeix el fitxer i es guarden les dades necessaries.
    public void informaRatings(String nom) throws Exception {
      eliminarDadesRatings(); // esborrem tot el que teniem de Ratings. 
      llegeixAllRatings(nom, UsuarisRatings);
      recalculaClusters();
    }
    
    //Pre: Es rep per parametre el nom del fitxer de Ratings Known.
    //Post: Es llegeix el fitxer i es guarden les dades necessaries.
    public void informaKnown(String nom) throws Exception {
      eliminarDadesKnown(); // esborrem tot el que teniem de Ratings Known.
      llegeixAllRatings(nom, UsuarisKnown);
    }

    //Pre: Es rep per parametre el nom del fitxer de Ratings Unknown.
    //Post: Es llegeix el fitxer i es guarden les dades necessaries.
    public void informaUnknown(String nom) throws Exception {
      eliminarDadesUnknown(); // esborrem tot el que teniem de Ratings Unknown.
      llegeixAllRatings(nom, UsuarisUnknown);
    }


    //Funció implementada per: Marina Alapont.
    //Pre: min < max.
    //Post: Canvia el valor de la puntuacio minima i maxima permesa en el sitema.
    public void canviaMiniMax(Double min, Double max) throws RangNoValid{
      if (min>=max || puntuacioMin>max || puntuacioMax<min || puntuacioMin<min || puntuacioMax>max) throw new RangNoValid(puntuacioMin,puntuacioMax);
      puntuacioMax = max;
      puntuacioMin = min;
    }

    //Funció implementada per: Marina Alapont-
    //Pre:
    //Post: Es recalculen els clusters.
    public void recalculaClusters() throws ExcepcionsRecomanador {
      CollaborativeFiltering cf = new CollaborativeFiltering(estrategiaDistancia,kkmeans);
      Pair< Vector<UsuariFitxers>[] , HashMap<String,Double>[] > result = cf.kmeans(UsuarisRatings, Items);
      clusters = result.first;
      centroides = result.second;      
    }

    //Pre:
    //Post: Retorna els clusters.
    public Vector<UsuariFitxers>[] getClusters(){
      return clusters;
    }

    //Pre:
    //Post: Retorna els centroides.
    public HashMap<String,Double>[] getCentroides(){
      return centroides;
    }
   

    /* FUNCIONS DE GESTIO D'USUARIS DE RATINGS*/

    //Funcio feta per: Marina Alapont.
    //Pre: La persona actual es de tipus Admin. 
    //Pre: Existeix l'usuari amb identificador userId.
    //Post: S'elimina del sistema l'usuari identificat per el id d'usuari passat per parametre.
    public void eliminarUsuariRatings(int userId) throws UsuariNoExisteix{
      if (UsuarisRatings.get(userId) == null) throw new UsuariNoExisteix(userId);
      else {
        UsuarisRatings.remove(userId);
        for (int i = 0; i< clusters.length; ++i){ //l'elminem dels clusters calculats actualment.
          for (int j = 0; j< clusters[i].size(); ++j){
            if (clusters[i].get(j).getUserId()==userId) clusters[i].remove(j);  
          }
        }
      }
    }

    //Funcio feta per: Marina Alapont.
    //Pre: La persona actual es de tipus Admin. 
    //Pre: Existeix una valoracio sobre l'item idItem fet per l'usuari identificat amb userId.
    //Post: Es canvia la puntuacio de la valoracio feta per l'usuari userId sobre l'item amb identificador idItem.
    public void canviarValoracioRatings(int userId, String idItem, Double puntuacioNova) throws ExcepcionsRecomanador{
      if (UsuarisRatings.get(userId) == null) throw new UsuariNoExisteix(userId);
      else{
        UsuariFitxers u=UsuarisRatings.get(userId);
        u.canviarPuntuacio(idItem, puntuacioNova);  
      } 
    }
  
    //Funcio feta per: Marina Alapont.
    //Pre: La persona actual es de tipus Admin. 
    //Pre: Existeix l'usuari identificat per userId.
    //Post: S'afegeix al conjunt de valoracions de l'usuari la valoracio al item idItem amb puntuacio puntuacioNova.
    public void afegirValoracioRatings(int userId, String idItem, Double puntuacioNova) throws ExcepcionsRecomanador{
      UsuariFitxers u=UsuarisRatings.get(userId);
      if (UsuarisRatings.get(userId) == null) throw new UsuariNoExisteix(userId);
      else {
        Valoracio novaVal = new Valoracio(puntuacioNova, Items.get(idItem), u);
        u.afegirValoracio(novaVal);
      }
    }

    //Funcio feta per: Marina Alapont.
    //Pre: La persona actual es de tipus Admin. 
    //Pre: Existeix l'usuari identificat per userId i te una valoracio sobre l'item identificat per idItem.
    //Post: S'elimina la valoracio feta per l'usuari amb userId passat per parametre sobre l'item identificat per idItem.
    public void eliminarValoracioRatings(int userId, String idItem) throws ExcepcionsRecomanador{
      UsuariFitxers u=UsuarisRatings.get(userId);
      if (UsuarisRatings.get(userId) == null) throw new UsuariNoExisteix(userId);
      else u.eliminarValoracio(idItem);
    }

    //Funcio feta per: Marina Alapont.
    //Pre: La persona actual es de tipus Admin. 
    //Post: Es crea l'usuari amb les valoracions formades per idItemsValoats i puntuacions. On puntuacions[i] es la puntuacio donada a idItemsValorats[i].
    public void afegirUsuariRatings(Vector<String> idItemsValorats, Vector<Double> puntuacions) throws ExcepcionsRecomanador{
      int userId= creaSeguentID();
      UsuariFitxers nouUsuari = new UsuariFitxers(userId);
      UsuarisRatings.put(userId, nouUsuari);
      Map<String,Valoracio> vals = new HashMap<String,Valoracio>();
      for (int i=0; i<idItemsValorats.size(); ++i){
        Valoracio nova = new Valoracio(puntuacions.get(i),Items.get(idItemsValorats.get(i)), nouUsuari);
        vals.put(idItemsValorats.get(i),nova);
      }
      nouUsuari.setValoracions(vals);
    }


    /*FUNCIONS DE GESTIO D'USUARI ACTIU*/

     //Funcio feta per: Marina Alapont
    //Pre: La persona actual es de tipus UsuariActiu. 
    //Pre: Existeix una valoracio sobre l'item idItem fet per l'usuari que té inciada la sessio actualment.
    //Post: S'elimina del sistema l'usuari identificat per el nom d'usuari passat per parametre.
    public void canviarValoracio(String idItem, Double puntuacioNova) throws  ExcepcionsRecomanador{    
        UsuariActiu u=PersonaActual.retornaUsuariActiu();
        if (u == null ) throw new PersonaActualNoValida("Admin","UsuariActiu");
        u.canviarPuntuacio(idItem, puntuacioNova);
    }
  
    //Funcio feta per: Marina Alapont
    //Pre: La persona actual es de tipus UsuariActiu.
    //Post: S'afegeix al conjunt de valoracions de l'usuari la valoracio al item idItem amb puntuacio puntuacioNova.
    public void afegirValoracio(String idItem, Double puntuacioNova) throws ExcepcionsRecomanador{
      UsuariActiu u=PersonaActual.retornaUsuariActiu();
      if (u == null ) throw new PersonaActualNoValida("Admin","UsuariActiu");
      Valoracio novaVal = new Valoracio(puntuacioNova, Items.get(idItem), u);
      u.afegirValoracio(novaVal);
    }
    

    //Funcio feta per: Marina Alapont
    //Pre: L'usuari actual fet log in te una valoracio sobre l'item identificat per idItem.
    //Post: S'elimina la valoracio feta sobre l'item identificat per idItem.
    public void eliminarValoracio(String idItem) throws ExcepcionsRecomanador{
      UsuariActiu u=PersonaActual.retornaUsuariActiu();
      if (u == null ) throw new PersonaActualNoValida("Admin","UsuariActiu");
      else {
        u.eliminarValoracio(idItem);
      }
    }
    //Funcio feta per: Marina Alapont.
    //Pre: 
    //Post: Es canvia el nom d'usuari de la persona actual per al passat per parametre (si aquest no esta en us per algun usuari del sistema).
    public void canviarNomUsuari(String nouNom) throws ExcepcionsRecomanador{
      UsuariActiu u=PersonaActual.retornaUsuariActiu();
      if (u == null ) throw new PersonaActualNoValida("Admin","UsuariActiu");
      else {
        Boolean existeix = ctrlUsuarisActius.existeixUsuari(nouNom); //retorna true si el username ja esta siguent usat per un altre usuari, false en cas contrari.
        if (!existeix) u.canviaUsername(nouNom);
        else throw new UsernameJaEnUs(nouNom);
      }
      guardarCanvisUsuari();
    }

    //Funcio feta per: Marina Alapont.
    //Pre: 
    //Post: Es canvia la contrasenya de la persona actual per la passada per parametre.
    public void canviarContrasenya(String contrasenyaNova) throws PersonaActualNoValida{
      UsuariActiu u=PersonaActual.retornaUsuariActiu();
      if (u == null ) throw new PersonaActualNoValida("Admin","UsuariActiu");
      else u.canviaContrasenya(contrasenyaNova);
    }

    //Funcio feta per: Marina Alapont.
    //Pre: 
    //Post: S'elimina l'usuari actiu que esta fet login (es desactiva el compte).
    public void eliminarUsuariActiu() throws PersonaActualNoValida{
      UsuariActiu u=PersonaActual.retornaUsuariActiu();
      if (u == null ) throw new PersonaActualNoValida("Admin","UsuariActiu");
      else {
        ctrlUsuarisActius.eliminarUsuari(PersonaActual.getUserId());
        PersonaActual = null;
      }
    }


    /* GESTIO D'ITEMS */ 

    //Funcio feta per: Marina Alapont
    //Pre: Existeix l'item identificat per idItem.
    //Post: S'elimina del sistema l'item identificat per idItem, eliminant també les valoracions que s'han fet sobre aquest item.
    public void eliminarItem(String idItem) throws ItemNoExisteix {
        if (!Items.containsKey(idItem)) throw new ItemNoExisteix(idItem);
        else {
          Items.remove(idItem);
          for (HashMap.Entry<Integer, UsuariFitxers> u : UsuarisRatings.entrySet()){
              if (u.getValue().getValoracions().containsKey(idItem)) u.getValue().getValoracions().remove(idItem);
          }
          ctrlUsuarisActius.eliminaValoracioItem(idItem);
        }
    }

    //Funcio feta per: Marina Alapont
    //Pre: nomColumnes conte els noms de columnes ja existents en el sistema.
    //Post: S'afegeix l'item amb els atributs passats per parametres. El primer parametre es un vector amb els noms de les columnes (noms dels atributs) 
    //      i el segon parametre es un vector que conte els conjunts de valors de les columnes, tal que els valors de la columna nomColumnes[i] son ValorsPerColumna[i].
    public void afegirItem (Vector<String> nomColumnes, Vector<Vector<String>> ValorsPerColumna) throws ItemBuit{
      boolean buit = true;
      for (int i = 0; i<ValorsPerColumna.size() && buit; i++) if (ValorsPerColumna.get(i).size() >0) buit = false;
      if (buit) throw new ItemBuit();
      if (!buit) {
        FactoriaAtribut fa = new FactoriaAtribut();
        String itemid = crearSeguentIdItem();
        Item iessim = new Item(itemid); //Creem l'item amb nomes l'id.
        Vector<Atribut> AtributsItem = new Vector<Atribut>(Columnes.size()); //Vector d'atributs de l'item. 
        for (int i = 0; i<Columnes.size(); i++) AtributsItem.add(new AtributCategoric("")); 
        for (int j = 0; j < nomColumnes.size(); ++j){ //Per cada columna de l'item a crear.
          Columna col = new Columna();
          boolean trobat = false;
          for (int i = 0; i<Columnes.size() && !trobat; ++i){  //busquem la instancia de columna que correspon a la columna amb nom nomColumnes[j].
              if (Columnes.get(i).getNomCol().equals(nomColumnes.get(j))){
                col = Columnes.get(i);
                trobat = true;
              } 
          }
          int numColumna = col.getNumCol();
          if(ValorsPerColumna.get(j).size()==0){   //Si no em donen un valor per aquesta columna, l'assignem null.
            AtributsItem.set(numColumna,null);
          }
          else{
            HashSet<String> Atrs = new HashSet<String>();
            for(int k = 0; k < ValorsPerColumna.get(j).size(); ++k){  //per cada valor de l'atribut en la columna j i per l'item a crear.  
              Atrs.add(ValorsPerColumna.get(j).get(k)); //afegim al hashset un per un els valors per la columna j i l'item que estem creant.
            }
            AtributsItem.set(numColumna,fa.creaAtribut(iessim, col, Atrs)); //Afegim al Vector<Atribut> instancies d'atribut creades per la factoria atribut.
          } 
        }
        HashSet<String> colid = new HashSet<String>();
        colid.add(itemid);
        AtributsItem.set(col_id, fa.creaAtribut(iessim,Columnes.get(col_id),colid)); // Afegim el valor per a la columna id.

        iessim.afegirAtributs(AtributsItem); //afegim a l'item creat el Vector<Atribut>
        Items.put(itemid, iessim); //afegim l'item al HashMap d'items.
      }
      
    }
    //Funcio feta per: Joel Cardona
    //Pre:
    //Post: Retorna el id a assignar a un item, tinguent en compte l'ultim id assignat. 
    private String crearSeguentIdItem() {
      for(int i = ultimIDItem;;++i){
        if(Items.get(String.valueOf(i)) == null){
          ultimIDItem = i;
          return String.valueOf(i); 
        }
      }
    }

    //Funcio feta per: Marina Alapont
    //Pre: Existeix l'item identificat per id. NomColumna es el nom d'una columna existent en el sistema.
    //Post: Es canvia el valor de l'atribut de la columna amb nom nomColumna per l'item identificat per id amb els valors valorsNous.
    public void canviarValorsAtribut(String id, Vector<String> valorsNous, String nomColumna) { 
      Item i = Items.get(id);
        Columna col = new Columna();
        boolean trobat = false;
        for (int j = 0; j<Columnes.size() && !trobat; ++j){  //busco la instancia de la columna amb nomColumna.
            if (Columnes.get(j).getNomCol().equals(nomColumna)){
              col = Columnes.get(j);
              trobat = true;
            } 
        }
        int numColumna = col.getNumCol();
        Vector<Atribut> Atributs = i.getAtributs(); 
        Atribut a = Atributs.get(numColumna);
        if ( a == null){ //si l'atribut era null
          if (valorsNous.size()!= 0){ // si deixa de ser null
            HashSet<String> atributsNous = new HashSet<String>();
            for (int j = 0 ; j< valorsNous.size(); ++j){
              atributsNous.add(valorsNous.get(j));
            }
            FactoriaAtribut fa = new FactoriaAtribut();
            Atribut at = fa.creaAtribut(i, col, atributsNous ); // creo l'atribut amb valors valorsNous.
            i.afegirAtribut(at, numColumna); //afegim l'atribut a l'item en la posicio de la columna corresponent.
          }
        }
        else{
          if (valorsNous.size()==0){ //si es posa a null
            Atributs.set(numColumna, null);
          }
          a.canviaValors(valorsNous); //canviem els valors de l'atribut.
        } 
    }

    /*FUNCIONS DE RECOMANACIONS */
    
    //Funció implementada per: Marina Alapont.
    //Pre: Es rep com a parametre el nombre d'items recomanats esperats.
    //Post: Es retorna el conjunt d'items recomanats ordenats de millor a pitjor.
    public Vector<String> demanaRecomanacio1(int nSortida) throws ExcepcionsRecomanador{
         int parametre;
         if (estrategia.equals("CollaborativeFiltering")) parametre= kkmeans;
         else parametre=kknearest;

         int id = ultimIDRecomanacio+1;
         ++ultimIDRecomanacio;
         Recomanacio r= new Recomanacio(id, estrategia, estrategiaDistancia, parametre); // creem la recomanacio amb les estrategies i k que fara servir (les actuals configurades).
         
         UsuariActiu u = PersonaActual.retornaUsuariActiu();
         int novalorats = Items.size() - u.getValoracions().size();
         if (novalorats<nSortida) throw new NombreItemsMassaGran(nSortida, novalorats);

         HashMap<String,Item> it = new HashMap<String, Item>();
         for (HashMap.Entry<String,Item> i : Items.entrySet()){ // obtenim els items no valorats per l'usuari.
            if (!u.getValoracions().containsKey(i.getKey())) it.put(i.getKey(), i.getValue());
         }
         Vector<String> result = r.getRecomanacio(u, it, nSortida); 
         return result;
    }

    //Funció implementada per: Marina Alapont.
    //Pre: Es reben com a parametre els identificador dels items que volem ordenar i el nombre d'items recomanats esperats.
    //Post: Es retorna nSortida items del conjunt d'items donats, ordenats de millor a pitjor.
    public Vector<String> demanaRecomanacio2(Vector<String> items, int nSortida) throws ExcepcionsRecomanador{
      int parametre;
      if (estrategia.equals("CollaborativeFiltering")) parametre= kkmeans;
      else parametre=kknearest;

      int id = ultimIDRecomanacio+1;
      ++ultimIDRecomanacio;
      Recomanacio r= new Recomanacio(id, estrategia, estrategiaDistancia, parametre); // creem la recomanacio amb les estrategies i k que fara servir (les actuals configurades).
      UsuariActiu u = PersonaActual.retornaUsuariActiu();

      int novalorats = Items.size() - u.getValoracions().size();
      if (novalorats<nSortida) throw new NombreItemsMassaGran(nSortida, novalorats);

      Map<String, Item> cjtItems = new HashMap<String, Item>();
      for (String s : items){ // per cada item passat per parametre comprovem que existeixi l'item i no estigui valorat ja per l'usuari.
        Item i = Items.get(s);
        if (i == null) throw new ItemNoExisteix(s); 
        if (u.getValoracions().containsKey(s)) throw new ItemJaValorat(s,u.getUserId());
        cjtItems.put(s, i);
      }
      Vector<String> result = r.getRecomanacio(u,cjtItems, nSortida) ;  
      return result;
    }
    
    //Funció implementada per: Marina Alapont.
    //Pre: Es passa per parametre: un conjunt d'usuaris existents a Known, un conjunt d'items existents, un conjunt d'items de sortida per cada usuari.
    //Post: Retorna la valoracio DGC total mitjana per al conjunt de recomanacions demanades i cadascuna de les recomanacions (els id dels items recomanats per cada usuari).
    public Pair <Vector<Vector<String>>,Double> getQualitatRecomanacio(Vector<Integer> UsuarisAValorar, Vector<Vector<String>> ItemsAValorar, Vector<Integer> nItemsSortida) throws ExcepcionsRecomanador {
      int parametre;
      if (estrategia.equals("CollaborativeFiltering")) parametre= kkmeans;
      else parametre=kknearest;

      Double DCGTotal=0.0;
      Vector<Vector<String>> recs = new Vector<Vector<String>>();

      for(int i=0; i<UsuarisAValorar.size(); ++i){ //per cada recomanacio a fer
        int id = ultimIDRecomanacio+1;
        ++ultimIDRecomanacio;
        Recomanacio r= new Recomanacio(id,estrategia, estrategiaDistancia, parametre); //creem la recomanacio amb les estrategies i k que fara servir (les actuals configurades).

        int idUsuari = UsuarisAValorar.get(i);
        UsuariFitxers u = UsuarisKnown.get(idUsuari);
        if (u == null) throw new UsuariNoExisteix(UsuarisAValorar.get(i));

        int novalorats = UsuarisUnknown.get(idUsuari).getValoracions().size();
        if (novalorats<nItemsSortida.get(i)) throw new NombreItemsMassaGran(idUsuari, nItemsSortida.get(i), novalorats);

        Map<String, Item> cjtItems = new HashMap<String, Item>();
        for (String s : ItemsAValorar.get(i)){ //obtenim el conjunt d'items no valorats per l'usuari dels que entren per parametre.
          Item it = Items.get(s);
          if (it == null) throw new ItemNoExisteix(s); 
          else if (u.getValoracions().containsKey(s)) throw new ItemJaValorat(s,UsuarisAValorar.get(i));
          if (!UsuarisUnknown.get(idUsuari).getValoracions().containsKey(s)) throw new ItemNoEsDeUnknown(s, idUsuari);
          cjtItems.put(s, it); 
        }

        Vector<String> recomanacio = r.getRecomanacio(u, cjtItems, nItemsSortida.get(i)); //obtenim la recomanacio.
        recs.add(recomanacio);
        double DCGParcial;
        DCGParcial = getDCGRecomanacio(u, recomanacio); // obtenim el DCG d'aquesta recomanacio i el sumem al total.
        DCGTotal+= DCGParcial;
    }
      Pair <Vector<Vector<String>>,Double> result = new Pair<Vector<Vector<String>>,Double>();
      result.first=recs;
      result.second=DCGTotal/UsuarisAValorar.size();
      return result;
    }

    //Funcio implementada per: Marina Alapont.
    //Pre:Es passa per parametre: un conjunt d'usuaris existents a Known, un conjunt d'items existents, un conjunt d'items de sortida per cada usuari.
    //Post: Es retorna un Pair amb la parella d'estrategies usades per el DCG (les actuals configurades) i el DCG normalitzat obtingut.
    public Pair <Double, Pair<String, String>> getQualitatRecomanacions (Vector<Integer> UsuarisAValorar, Vector<Vector<String>> ItemsAValorar, Vector<Integer> nItemsSortida) throws ExcepcionsRecomanador{
      Double DCG= getQualitatRecomanacio(UsuarisAValorar, ItemsAValorar, nItemsSortida).second;
      Pair <String, String> p = new Pair<String, String>();
      p.first = estrategia;
      p.second = estrategiaDistancia;

      Pair <Double, Pair<String, String>> result = new Pair<Double, Pair<String, String>>();
      result.first = DCG;
      result.second = p;

      return result;

    }
    //Funcio implementada per: Joel Cardona i Marina Alapont.
    //Pre: Es passa per parametre: un conjunt d'usuaris existents a Known, un conjunt d'items existents, un conjunt d'items de sortida per cada usuari.
    //Post: Retorna el DCG mitja de fer un conjunt de recomanacions per cada configuracio d'estrategies possibles.
    // Per cada element del vector, el primer element retornat es el DCG, el segon element  la parella que conte la estrategia de filtering i la de distancia.

    public Vector<Pair<Double, Pair<String, String> > > BuscaMillorConfiguracio(Vector<Integer> UsuarisAValorar, Vector<Vector<String>> ItemsAValorar, Vector<Integer> nItemsSortida) throws ExcepcionsRecomanador{
      Vector<Pair<Double, Pair<String, String > > > result = new Vector<Pair<Double, Pair<String, String > > >();
      String estrategiaFiltering = estrategia; //L'estrategia abans de fer la proba
      String estrategiaDist = estrategiaDistancia; //L'estrategia abans de fer la proba
    
      Vector <String> estrategiesFiltering = new Vector<String> ();
      estrategiesFiltering.add("ContentBasedFiltering");
      estrategiesFiltering.add("CollaborativeFiltering");
      estrategiesFiltering.add("HybridApproaches");

      Vector<String> estrategiesDist = new Vector<String>();
      estrategiesDist.add("DistanciaEuclidiana");
      estrategiesDist.add("DistanciaMitjana");
      estrategiesDist.add("DistanciaPonderada");

      //per cada combinacio possible valorem la qualitat d'un conjunt de recomanacions fetes amb aquella configuracio.
      for(int j = 0; j < estrategiesDist.size(); ++j){
        canviaEstrategiaDistancia(estrategiesDist.get(j));
        for(int i = 0; i < estrategiesFiltering.size(); ++i){ 
          canviaEstrategia(estrategiesFiltering.get(i));
          Pair<String, String> algorismes = new Pair<String, String>();
          algorismes.first = estrategiesFiltering.get(i); algorismes.second = estrategiesDist.get(j);
          double dcgIessim = getQualitatRecomanacio(UsuarisAValorar, ItemsAValorar, nItemsSortida).second;
          Pair<Double, Pair<String, String > > dcgNou = new Pair<Double, Pair<String, String > >();
          dcgNou.first = dcgIessim; dcgNou.second = algorismes;
          result.add(dcgNou);
        }
      }
      //deixem la configuracio tal com estava al principi;
      estrategia = estrategiaFiltering; 
      estrategiaDistancia = estrategiaDist;
      return result;
    }

    //Funcio feta per: Simon Oliva.
    //pre: els items del vector han d'estar ordenats per valoracio de forma decreixent.
    //pre: l'UsuariBDu apareix a UsuarisUnknown amb les puntuacions de test entre les seves valoracions.
    //Post: Retorna el DCG de la recomanacio.
    public Double getDCGRecomanacio(UsuariFitxers u, Vector<String> recomanats) throws ExcepcionsRecomanador{
      int uID = u.getUserId();
      Map<String, Valoracio> LT = UsuarisUnknown.get(uID).getValoracions(); //Conté les valoracions de test.

      double dcg = 0;
      double reli;
      double numerador;
      double denominador;
      double log2 = Math.log(2);

      for (int i = 0; i < recomanats.size(); ++i) { //per a cada item recomanat per l'algorisme.

        String itemID = recomanats.get(i);

        if (LT.containsKey(itemID)) reli = LT.get(itemID).getPuntuacio();
        else reli = 0;

        numerador = Math.pow(2.0, reli) - 1;
        denominador = Math.log(i + 2) / log2; //divisio per passar a base 2

        dcg += numerador/denominador;
      }

      //Ens guardem la permutació ideal (ordenada decreixentment) de les puntuacions de test de l'usuari.
      ArrayList<Double> idealPermutation = new ArrayList<Double>(LT.size());
      for (Map.Entry<String, Valoracio> v : LT.entrySet()) {
        idealPermutation.add(v.getValue().getPuntuacio());
      }
      Collections.sort(idealPermutation, Collections.reverseOrder());

      int p = recomanats.size();
      //Fem el dcg ideal per a les p primeres puntuacions de test.
      double dcgIdeal = 0;

      if (LT.size() >= p) { // S'hauria de complir sempre, però és per si per algun error no hi hagués suficients puntuacions de test.
        for (int i = 0; i < p; ++i) {
          numerador = Math.pow(2.0, idealPermutation.get(i)) - 1;
          denominador = Math.log(i + 2) / log2; //divisio per passar a base 2
        dcgIdeal += numerador/denominador;
        }
      }
      if (dcgIdeal == 0) dcgIdeal = 1;
      return dcg/dcgIdeal;
    }

     /*FUNCIO PER ALS REPLACE DE FITXERS*/

     //Funcio feta per: Joel Cardona.
     //Pre: 
     //Post: s'elimina de memoria els items i la seva informacio.
     public void eliminarFitxerItems(){
      Items = new HashMap<String, Item>();
      Columnes = new Vector<Columna>();
      ctrlUsuarisActius.eliminarTotesValoracions();
      int col_id = 0;
      int n_cols = 0;
    }

    //Funcio feta per: Joel Cardona.
    //Pre: 
    //Post: S'elimina de memoria els usuaris de ratings i la seva informacio.
    public void eliminarDadesRatings(){
      UsuarisRatings = new HashMap<Integer, UsuariFitxers>();
      puntuacioMax = null;
      puntuacioMin = null;
    }

    //Funcio feta per: Joel Cardona.
    //Pre: 
    //Post: S'elimina de memoria els usuaris de known i la seva informacio.
    public void eliminarDadesKnown(){
      UsuarisKnown = new HashMap<Integer, UsuariFitxers>();
    }

    //Funcio feta per: Joel Cardona.
    //Pre: 
    //Post: S'elimina de memoria els usuaris de unknown i la seva informacio.
    public void eliminarDadesUnknown(){
      UsuarisUnknown = new HashMap<Integer, UsuariFitxers>();
    }

    /*FUNCIONS DE TRACTAMENT DE FITXERS*/

     //Funció implementada per: Simón Oliva.
     //Pre: Existeix un fitxer amb nom igual que filename i conte un conjunt de valoracions.
     //Post: Llegeix tot el fitxer i guarda les dades en memoria al sistema.
     public void llegeixAllRatings(String filename, HashMap<Integer, UsuariFitxers> cjtUsuaris) throws Exception{
      double puntMax = Double.MIN_VALUE; //fiquem el mínim i en llegir ratings s'actualitza
      double puntMin = Double.MAX_VALUE; //fiquem el màxim i en llegir ratings s'actualitza
      List<String> dadesRatings = ctrlRatingsFile.getAll(filename+".csv");

      //comencem per 1 perquè la primera fila sempre és el nom de les columnes "userId,itemId,rating"
      for (int i = 1; i < dadesRatings.size(); ++i) {

        /*retorna un array amb 3 strings que corresponen als valors separats per les comes:
        valors = {"userId","itemId","rating"}*/
        String valors[] = dadesRatings.get(i).split(",");

        if (ctrlUsuarisActius.existeixUsuari(valors[0])) throw new IdUsuariRepetit(valors[0]);
        //Si l'usuari encara no existeix, el crearem
        if (!cjtUsuaris.containsKey(Integer.valueOf(valors[0]))) {
          UsuariFitxers u = new UsuariFitxers(Integer.valueOf(valors[0]));
          cjtUsuaris.put(Integer.valueOf(valors[0]), u);
        }

        double puntuacio = Double.parseDouble(valors[2]);

        //comprovem si hem d'actualitzar les puntuacions màxima o mínima (no es pot fer un if/else per com estan inicialitzats els valors)
        if (puntuacio < puntMin) puntMin = puntuacio;
        if (puntuacio > puntMax) puntMax = puntuacio;
        
        Valoracio rating = new Valoracio(puntuacio, Items.get(valors[1]), cjtUsuaris.get(Integer.valueOf(valors[0])));
        cjtUsuaris.get(Integer.valueOf(valors[0])).afegirValoracio(rating);
      }

      if(puntuacioMin!=null) Math.min(puntuacioMin, puntMin);
      else puntuacioMin=puntMin;
      if(puntuacioMax!=null) Math.max(puntuacioMax, puntMax);
      else puntuacioMax=puntMax;
    }
    
    //Funcio implementada per: Joel Cardona. 
    //La factoria d'atributs rep un Item, una Columna i un HashSet de Strings. 
    //Pre: Existeix el fitxer amb nom filename i conte la informacio de tots els items.
    //Post: Crea totes les instancies d'items que existeixin en el fitxer.
    public void llegeixAllItems(String filename) throws FileNotFoundException{
      eliminarFitxerItems();
      List<String> itemsdades = ctrlItemFile.getAll(filename+".csv");
      n_cols = 0;
      String conjunt_dades = itemsdades.get(0);
      String paraula_act = "";
      
      //detectar on esta el id i calcular quants atributs hi ha
      for (int j = 0; j < conjunt_dades.length(); j++){
          if (conjunt_dades.charAt(j) == ',' || j == conjunt_dades.length()-1){
          Columna actual = new Columna(n_cols, paraula_act);
          Columnes.add(actual);
          if (paraula_act.equals("id")){
            col_id = n_cols;
            Columnes.get(n_cols).setEsClau(true);
          }
          ++n_cols;
          paraula_act = "";
          }
          else paraula_act+=(conjunt_dades.charAt(j));
      }

      FactoriaAtribut fa = new FactoriaAtribut();
      String idAct = "";
      for (int k = 1; k < itemsdades.size(); ++k){
        conjunt_dades = itemsdades.get(k);
        paraula_act = "";
        int n_atr_act = 0;
        boolean comilla_detectada = false;
        Vector<HashSet<String>> atributs_item_act = new Vector<HashSet<String>> (n_cols);
        atributs_item_act.setSize(n_cols);
        

        for (int j = 0; j < conjunt_dades.length(); j++){
          if (conjunt_dades.charAt(j) == '"') {
            if(comilla_detectada == false) comilla_detectada = true;
            else{ comilla_detectada = false; }
          }
      
          else if (conjunt_dades.charAt(j) == ';' && comilla_detectada == false){
            if(!paraula_act.equals("")){
              HashSet<String> aiuda = new HashSet<String>();
              
              if (atributs_item_act.elementAt(n_atr_act) != null)
                aiuda =  atributs_item_act.elementAt(n_atr_act);
                aiuda.add(paraula_act);
                atributs_item_act.set(n_atr_act, aiuda);
                paraula_act = "";
            }
          }
      
          else if ((conjunt_dades.charAt(j) == ',' && comilla_detectada == false) || j+1 ==conjunt_dades.length()){
            if(!paraula_act.equals("")){
              HashSet<String> aiuda = new HashSet<String>();
                
              if (atributs_item_act.get(n_atr_act) != null) aiuda =  atributs_item_act.elementAt(n_atr_act);
              aiuda.add(paraula_act);
              atributs_item_act.set(n_atr_act, aiuda);
            }
                      
              if(n_atr_act == col_id) idAct = paraula_act;
              ++n_atr_act;
              paraula_act = "";
          }
                  
          else paraula_act+=(conjunt_dades.charAt(j));
          if(n_atr_act < n_cols-1 && conjunt_dades.length()-1 == j){
            ++k;
            conjunt_dades = itemsdades.get(k);
          }
      }
      

      Item nou = new Item(idAct);

      Vector<Atribut> passarAtributs = new Vector<Atribut> ();
      

      for (int i = 0; i < n_cols; ++i){
        if (atributs_item_act.get(i)!=null) {
          passarAtributs.add(fa.creaAtribut(nou, Columnes.get(i), atributs_item_act.get(i)));
        }
        else passarAtributs.add(null);
      }
      nou.afegirAtributs(passarAtributs);       
      Items.put(idAct, nou);
    }
  }

  //Funcio feta per: Marina Alapont i Joel Cardona.
  //Pre: 
  //Post:  Crida al controlador de dades de ratings per tal de guardar en un fitxer UsuarisRatings.json el contingut, i per tant tota la informacio, sobre els diferents UsuariFitxers que hi ha carregats en memoria.
  public void guardarRatings(){
    Vector<Integer> usrs = new Vector<Integer> ();  //Posem els ids dels usuaris en un vector.
    Vector<Integer> nVals = new Vector<Integer> (); //Vector de cuantes valoracions te l'usuari iessim.
    Vector<String> itemsValorats = new Vector<String> (); //Posem els ids dels items que han valorat els usuaris, els nVals[0] primers corresponen als del usrs[0], etc.
    Vector<Double> puntuacions = new Vector<Double> (); //idem que amb els items pero amb les puntuacions.

    for (HashMap.Entry<Integer, UsuariFitxers> i : UsuarisRatings.entrySet()) { //per tots els usuaris de ratings.
      //omplim els diferents vectors creats anteriorment.
      usrs.add(i.getKey());
      nVals.add(i.getValue().getValoracions().size());
      for (HashMap.Entry<String, Valoracio> j : i.getValue().getValoracions().entrySet()){ //per cada valoracio de l'usuari agafem l'identificador de l'item i la puntuacio donada.
        itemsValorats.add(j.getKey());
        puntuacions.add(j.getValue().getPuntuacio());
      }
    }
    ctrlRatingsFile.saveRatings(usrs, nVals, itemsValorats, puntuacions);
  }

  //Funcio feta per: Marina Alapont i Joel Cardona.
  //Pre: Existeix el fitxer amb nom filename i conte la informacio de tots usuaris pertanyents al fitxer de Rating.
  //Post: Crea totes les instancies de UsuariFitxers que controlador de usuaris de ratings li passa i s'fegeixen al map UsuarisRatings.
  public void llegirRatingsBD() throws ExcepcionsRecomanador{
    Map<Integer, Vector< Pair<String, Double> > > dadesRatings = new HashMap<Integer, Vector< Pair<String, Double> > >();
    dadesRatings = ctrlRatingsFile.readRatings();

    double puntMax = Double.MIN_VALUE; 
    double puntMin = Double.MAX_VALUE; 
    
    for(HashMap.Entry<Integer, Vector< Pair<String, Double> > > i : dadesRatings.entrySet()){
      if (!UsuarisRatings.containsKey(i.getKey())){
        UsuariFitxers usuariIessim = new UsuariFitxers(i.getKey());
        UsuarisRatings.put(i.getKey(), usuariIessim);
      }
      for(int j = 0; j < i.getValue().size(); ++j){
        double puntuacio = i.getValue().get(j).second;
        puntMin = Math.min(puntMin, puntuacio);
        puntMax = Math.max(puntMax, puntuacio);
        
        Valoracio valIessima = new Valoracio(puntuacio, Items.get(i.getValue().get(j).first), UsuarisRatings.get(i.getKey()));
        UsuarisRatings.get(i.getKey()).afegirValoracio(valIessima);
      }
    }
    if(puntuacioMin!=null) Math.min(puntuacioMin, puntMin);
    else puntuacioMin=puntMin;
    if(puntuacioMax!=null) Math.max(puntuacioMax, puntMax);
    else puntuacioMax=puntMax;
  }

  //Funcio feta per: Marina Alapont i Joel Cardona
  //Pre:
  //Post: Crida al controlador de dades d'items per tal de obtenir la info dels items que te el sistema i els guarda a memoria.
  public void llegirItemsBD(){
    Map<String, Pair<Vector<String>, Vector<Vector<String> > > > items = ctrlItemFile.readItems();
    FactoriaAtribut fa = new FactoriaAtribut();
    boolean firstItem = true;
    for (HashMap.Entry<String, Pair<Vector<String>, Vector<Vector<String> > > > i : items.entrySet()) { //per tots els items
      Item iessim = new Item(i.getKey()); //Creem l'item amb nomes l'id.
      Vector<Atribut> AtributsItemIessim = new Vector<Atribut>(); //Vector d'atributs de l'item.
      for (int j = 0; j < i.getValue().first.size(); ++j){ //Per cada columna de l'item iessim.
        if(firstItem){  //Si es el primer item guardem les coses de la columna.
          Columna actual = new Columna(j, i.getValue().first.get(j));
          Columnes.add(actual); //Creem i afegim les columnes al atribut del ctrl domini.
          if(i.getValue().first.get(j).equals("id")){
            col_id = j;
            Columnes.get(j).setEsClau(true);
          } 
        }

        if(i.getValue().second.get(j).size()==0){
          AtributsItemIessim.add(null);
        }
        else{
          HashSet<String> Atrs = new HashSet<String>();
          for(int k = 0; k < i.getValue().second.get(j).size(); ++k){ //Posem tots els atributs de l'item i i la columna j en un hashset (per la factoria)
            Atrs.add(i.getValue().second.get(j).get(k));
          }
          AtributsItemIessim.add(fa.creaAtribut(iessim, Columnes.get(j), Atrs)); //Creem els atributs i els assignem al vector d'atributs
        }

      }
      iessim.afegirAtributs(AtributsItemIessim);
      Items.put(i.getKey(), iessim);
      firstItem = false;
    }
  }

  //Funcio feta per: Joel Cardona i Marina Alapont
  //Pre: 
  //Post: Crida al controlador de dades de items per tal de guardar en un fitxer json tota la informacio sobre els diferents items que hi ha al sistema.
  public void guardarItemsBD(){
    Map<String, Pair<Vector<String>, Vector<Vector<String> > > > items = new HashMap<String, Pair<Vector<String>, Vector<Vector<String> > >>();
    for (HashMap.Entry<String, Item> i : Items.entrySet()) { //per tots els items
      Pair<Vector<String>, Vector<Vector<String> > > itemIessim = new Pair<Vector<String>, Vector<Vector<String> >>();
      Vector<String> nomCols = new Vector<String>();
      Vector<Vector<String> >atributs = new Vector<Vector<String>>();
      for(int j = 0; j < Columnes.size(); ++j){ //Recorrem cada columna i n'agafem el nom.
        nomCols.add(Columnes.get(j).getNomCol());
        Vector<String> atrs = (Items.get(i.getKey()).getAtributString(j)); //agafem tots els valors de l'atribut de l'item 
        atributs.add(atrs); 
      }
      itemIessim.first = nomCols; itemIessim.second = atributs;
      items.put(i.getKey(), itemIessim);
    }
    ctrlItemFile.saveItems(items);
  }

  //Funcio feta per: Joel Cardona.
  //Pre: 
  //Post: Crida al controlador de dades de items per tal de afegir en el fitxer json d'usuaris actius la informacio de l'usuari que esta usant el sistema actualment.
  public void guardarCanvisUsuari(){
    UsuariActiu usr = PersonaActual.retornaUsuariActiu();
    if (usr !=null) { // Si l'usuari es de tipus UsuariActiu
      Vector<Pair<String,Double>> vals = new Vector<Pair<String, Double> >();
      for (HashMap.Entry<String, Valoracio> k : usr.getValoracions().entrySet()){ // recorrem les seves Valoracions per tal de obtenir l'identificador de l'item i la puntuacio donada.
          Pair<String, Double> valIessima = new Pair<String, Double>();
          valIessima.first = k.getKey();
          valIessima.second = k.getValue().getPuntuacio();
          vals.add(valIessima);
      }
      ctrlUsuarisActius.saveUser(PersonaActual.getUserId(), usr.getUsername(), usr.getContrasenya(), vals);
    }
  }

  //Funcio feta per: Marina Alapont.
  //Pre: 
  //Post: Crida al controlador de dades de recomanacions per tal de guardar en el fitxer json de recomanacions les recomanacions fetes per l'usuari PersonaActual.
  public void guardarRecomanacions(){
    UsuariActiu usr = PersonaActual.retornaUsuariActiu();
    if (usr !=null) { // Si Persona Actual es de tipus UsuariActiu
        Vector<Recomanacio> recomanacions = usr.getRecomanacions();
        HashMap <Integer, Vector< Pair<String, Double>>>  recs = new HashMap <Integer, Vector< Pair<String, Double>>>();
        for (Recomanacio r : recomanacions) { //per cada recomanacio feta a l'usuari obtenim el seu id, i el conjunt de parelles idItem+puntuacio recomanades.
          int id = r.getId();
          TreeSet<Valoracio> result = r.getResultat();
          Vector<Pair<String, Double>> predicions = new Vector<Pair<String, Double>>(); 
          for (Valoracio v : result) {
            Pair < String, Double> val = new Pair<String,Double>();
            val.first = v.getIdItem();
            val.second = v.getPuntuacio();
            predicions.add(val);
          }
          recs.put(id, predicions);
        }
        ctrlRecomanacions.saveRecomanacions(usr.getUserId(), recs);     
    }
  }

  //Funcio feta per: Joel Cardona.
  //Pre: 
  //Post: Crida al controlador de dades de clusters per tal de guardar en el fitxer json corresponent els centroides i clusters que te calculats el sistema actualment.
  public void guardarClustersCentoids(){
    Vector<Vector<Integer>> clusts = new Vector<Vector<Integer>>();
    for (int i = 0; i < clusters.length; ++i){ //passem el Vector<Vector<UsuariFitxers>> que representa els clusters a un Vector<Vector<Integer>> on l'integer es el id d'un usuari. 
      Vector<Integer> clusterIessim = new Vector<Integer>();
      for(int j = 0; j< clusters[i].size(); ++j){
        clusterIessim.add(clusters[i].get(j).getUserId());
      }
      clusts.add(clusterIessim);
    }
    clustersCtrl.saveClusters(clusts);
    clustersCtrl.saveCentroides(centroides);
  }

   //Funcio feta per: Joel Cardona.
  //Pre: 
  //Post: Crida al controlador de dades de clusters per tal d'obtenir els centroides i clusters que cal tenir en memoria.
  public void llegirClustersCentroides(){
    //LECTURA DE CLUSTERS PRIMER
    Vector<Vector<Integer>> idUsuarisCluster = clustersCtrl.readClusters();
    clusters = new Vector[idUsuarisCluster.size()];
    for(int i = 0; i < idUsuarisCluster.size(); ++i){ //passem el Vector<Vector<Integer>> on l'integer es el id d'un usuariFitxers a un Vector<Vector<UsuariFitxers>> que representa els cluster.
      Vector<UsuariFitxers> usuarisClusterI = new Vector<UsuariFitxers>();
      for (int j = 0; j < idUsuarisCluster.get(i).size(); ++j){
        usuarisClusterI.add(UsuarisRatings.get(idUsuarisCluster.get(i).get(j)));
      }
      clusters[i] = (usuarisClusterI);
    }

    //LECTURA CENTROIDES
    centroides = clustersCtrl.readCentoides();
  }


  /*FUNCIONS NECESSARIES PER A LES INTERFICIES*/
  //Pre: L'usuari u no es null
  //Post: Retorna les valoracions de l'usuari u en format de matriu d'strings on s'indica la informacio de l'item indicada a columnNamesNoID
  //la puntuacio d'aquest item per l'usuari u
  private ArrayList<ArrayList<String>> getValoracionsUserDonatUsuari(Usuari u, List<String> columnNamesNoID) {
    Map<String, Valoracio> mapVals = u.getValoracions();
    Vector<Integer> posicioColumnes = new Vector<Integer>();
    for (int i = 0; i<Columnes.size(); i++) {
      int j = 0;
      boolean trobat = false;
      while (j<columnNamesNoID.size() && !trobat) {
        if (columnNamesNoID.get(j).equals(Columnes.get(i).getNomCol())) {
          trobat = true;
          posicioColumnes.add(i);
        }
        j++;
      }
    }


    ArrayList<ArrayList<String>> arrVals = new ArrayList<ArrayList<String>>();
    for (Map.Entry<String, Valoracio> entry : mapVals.entrySet()) {
      if (!entry.getValue().esPredictiva()) {
        ArrayList<String> valoracio = new ArrayList<String>();
        valoracio.add(entry.getValue().getIdItem());
        Item i = entry.getValue().getItem();
        for (int x : posicioColumnes) {
          valoracio.add(atrToString(i.getAtribut(x)));
        }
        valoracio.add(entry.getValue().getPuntuacio().toString());
        arrVals.add(valoracio);
      }
    }
    return arrVals;
  }

  //Metode fet per Daniel Pulido
  //Pre: PersonaActual es un UsuariActiu
  //Post: Retorna les valoracions fetes per l'usuari PersonaActual, on per cada valoracio es te la informacio demanada a columnNamesNoID i la puntuacio de
  //l'item donada per l'usuari PersonaActual
  public ArrayList<ArrayList<String>> getValoracionsUser(List<String> columnNamesNoID) {
    Usuari ua = PersonaActual.retornaUsuariActiu();
    return getValoracionsUserDonatUsuari(ua, columnNamesNoID);
  }
  
  //Pre: 
  //Post: Retorna les valoracions dels usuaris de ratings en forma de matriu d'Strings, on s'indica, per cada valoracio, l'userID+ itemID + les columnes de l'item
  //indicades a columnNamesNoIDItem
  public ArrayList<ArrayList<String>> getValoracionsUsuarisRatings(List<String> columnNamesNoIDItem) {
    ArrayList<ArrayList<String>> valsUsersRatings = new ArrayList<ArrayList<String>>();

    for (Map.Entry<Integer, UsuariFitxers> entry : UsuarisRatings.entrySet()) {
      Usuari user = entry.getValue();
      if (user != null) {
        List<String> columnNamesNoIDItemNoUser = columnNamesNoIDItem.subList(1, columnNamesNoIDItem.size());
        ArrayList<ArrayList<String>> valsUserRatings = getValoracionsUserDonatUsuari(user, columnNamesNoIDItemNoUser);
        for (ArrayList<String> aL : valsUserRatings) {
          aL.add(0, String.valueOf(user.getUserId()));
        }
        valsUsersRatings.addAll(valsUserRatings);
      }

    }
    return valsUsersRatings;
  }
  //Pre:
  //Post: Retorna el nom de les columnes dels items en forma d'array d'strings
  public ArrayList<String> getNomColumnesItem() {
    int size = Columnes.size();
    ArrayList<String> nomsColumnes = new ArrayList<String>(size);
    nomsColumnes.add(Columnes.get(col_id).getNomCol());
    for (Columna c : Columnes) {
      if (!c.getEsClau())nomsColumnes.add(c.getNomCol());
    }
    return nomsColumnes;
  }

  //Pre:  atr no es null
  //Post: Retorna un atribut en forma d'string
  private String atrToString(Atribut atr) {
    Vector<String> vectorValors = atr.getValorsString();
    String valors = "";
    boolean primer = true;
    for (String s : vectorValors) {
      if (primer) {
        valors = s;
        primer = false;
      }
      else valors += ";" + s;
    }
    return valors;
  }

  //Pre:  i no es null
  //Post: Retorna l'item i en forma d'array d'strings
  private ArrayList<String> itemToArrayString(Item i) {
    ArrayList<String> atributsItemString = new ArrayList<String>();
    Vector<Atribut> atributsItem = i.getAtributs();

    //Primer afegim la columna clau i li treiem el .00 

    int j;
    boolean trobat=false;
    for (j =0; j< atributsItem.size() && !trobat; ++j){ //busco dins de tots els atributs del item quin te la columna de l'id
      if(atributsItem.get(j)!=null){
        if(atributsItem.get(j).getColumna() == Columnes.get(col_id)) trobat=true;
      }
    }
    Atribut atrClau = i.getAtribut(j-1);
    Vector<String> vectorValorsClau = atrClau.getValorsString();
    boolean primerClau = true;
    String valorsClau ="";
    for (String s : vectorValorsClau) {
      if (primerClau) {
        valorsClau = s.substring(0, s.length()-2);
        primerClau = false;
      }
      else valorsClau += ";" + s;
    }
    atributsItemString.add(valorsClau);  

    //Afegim la resta de columnes
    for (Atribut a : atributsItem) {
      if (a == null) atributsItemString.add(" ");
        else if (!a.getColumna().getEsClau()) {
          atributsItemString.add(atrToString(a));
      }
    }
    return atributsItemString;

  }

  //Pre:
  //Post: Retorna el conjunt d'items no valorats per l'usuari PersonaActual
  public ArrayList<ArrayList<String>> getItemsNoValorats() {
    ArrayList<ArrayList<String>> itemsNoValorats = new ArrayList<ArrayList<String>>();
    UsuariActiu ua = PersonaActual.retornaUsuariActiu();
    Map<String, Valoracio> mapVals = ua.getValoracions(); //String es el ID del Item
    for (Map.Entry<String, Item> entry : Items.entrySet()) {
      if (!mapVals.containsKey(entry.getKey())) {//Si l'Item no ha estat valorat per PersonaActual
          itemsNoValorats.add(itemToArrayString(entry.getValue()));
      }
    }
    return itemsNoValorats;
  }

  //Pre:
  //Post: Retorna tots els items en forma de matriu d'strings
  public ArrayList<ArrayList<String>> getItems() {

    ArrayList<ArrayList<String>> totsItems = new ArrayList<ArrayList<String>>();
    for (Map.Entry<String, Item> entry : Items.entrySet()) {
       totsItems.add(itemToArrayString(entry.getValue()));
    }
    return totsItems;
  }

  //Pre:
  //Post: Retorna l'item identificat per id, però, sense la columna id
  public ArrayList<ArrayList<String>> getItemAModificar(String id) {

    ArrayList<ArrayList<String>> itemAModificar = new ArrayList<ArrayList<String>>();
    ArrayList<String> itemM = itemToArrayString(getItem(id));
    //li traiem l'id pq aquest no l'ha de poder modificar
    itemM.remove(0);
    itemAModificar.add(itemM);
    return itemAModificar;
  }

  //Pre:
  //Post: Retorna els IDs de tots els usuaris en forma de matriu d'strings, on es considera nomes una columna
  //que es IDUsuari
  public ArrayList<ArrayList<String>> getIDsUsuaris() {
    ArrayList<ArrayList<String>> totsUsers = new ArrayList<ArrayList<String>>();
    for (Map.Entry<Integer, UsuariFitxers> u : UsuarisRatings.entrySet()) {
      ArrayList<String> dadesUser = new ArrayList<String>();
      dadesUser.add(u.getKey().toString());
      totsUsers.add(dadesUser);
   }
    return totsUsers;
  }

  //Pre:
  //Post: Retorna una recomanacio d'items en forma de matriu d'strings d'items, on el nombre d'items recomanats esta indicat
  //a nSortida, i si items no es buit, es consideren nomes aquests items per la recomanacio, altrament es consideren tots
  public ArrayList<ArrayList<String>> getRecomanacioArrList(String[] items, int nSortida) throws ExcepcionsRecomanador {
    Vector<String> itemsRecom;
    if (items.length == 0) {
      itemsRecom = demanaRecomanacio1(nSortida);
    }
    else {
      Vector<String> itemsAOrdenar = new Vector<String>(Arrays.asList(items));
      itemsRecom = demanaRecomanacio2(itemsAOrdenar, nSortida);
    }
    ArrayList<ArrayList<String>> itemsRecomanats = new ArrayList<ArrayList<String>>();
    for (String s : itemsRecom) {
      Item i = Items.get(s);
      itemsRecomanats.add(itemToArrayString(i));
    }
    return itemsRecomanats;
  }

  //Pre:
  //Post: Dona l'username de l'usuari actual en forma d'string
  public String getUsernameUsuari() {
    UsuariActiu ua = PersonaActual.retornaUsuariActiu();
    return ua.getUsername();
  }

  //Pre:
  //Post: Dona els usuaris de ratings en forma de matriu d'strings
  public ArrayList<ArrayList<String>> getUsersRatingsID() {
    ArrayList<ArrayList<String>> usersID = new ArrayList<ArrayList<String>>();
    for (Map.Entry<Integer, UsuariFitxers> entry : UsuarisRatings.entrySet()) {
      ArrayList<String> userID = new ArrayList<String>(1);
      userID.add(String.valueOf(entry.getKey()));
      usersID.add(userID);
    }
    return usersID;
  }

//Pre:
//Post: Retorna els items no valorats per l'user indicat a userID
public ArrayList<ArrayList<String>> getItemsNoValoratsUserRatings(String userID) {
    int userIDInt = Integer.parseInt(userID);
    ArrayList<ArrayList<String>> itemsNoValorats = new ArrayList<ArrayList<String>>();
    Usuari u = UsuarisRatings.get(userIDInt);
    Map<String,Valoracio> valoracionsU = u.getValoracions();
    
    for (Map.Entry<String, Item> entry : Items.entrySet()) {
      if (!valoracionsU.containsKey(entry.getKey())) {
        ArrayList<String> item = itemToArrayString(entry.getValue());
        itemsNoValorats.add(item);
      }
    }
    return itemsNoValorats;

}
//Pre:
//Post: Retorna l'historic de recomanacions de l'usuari PersonaActual en forma de matriu d'strings.
public ArrayList<ArrayList<String>> getHistoricRecomanacio() {
  UsuariActiu u = PersonaActual.retornaUsuariActiu();
  ArrayList<ArrayList<String>> historicRecomanacions = new ArrayList<ArrayList<String>>();
  Vector<Recomanacio> recomanacions = u.getRecomanacions();
  for (Recomanacio r : recomanacions) {
    String idRec = String.valueOf(r.getId());
    TreeSet<Valoracio> vals = r.getResultat();
    for (Valoracio val : vals) {
      ArrayList<String> itemString = itemToArrayString(val.getItem());
      itemString.add(0, idRec);
      historicRecomanacions.add(itemString); 
    }
  }
  return historicRecomanacions;
}

//Pre:
//Post: Retorna cert si l'usuari actual te valoracions, altrament fals.
public boolean userTeValoracions() {
  UsuariActiu u = PersonaActual.retornaUsuariActiu();
  return u.getValoracions().size() > 0;
}


}

//Classe implementada per Joel Cardona, Simon Oliva, Marina Alapont, Daniel Pulido. 
