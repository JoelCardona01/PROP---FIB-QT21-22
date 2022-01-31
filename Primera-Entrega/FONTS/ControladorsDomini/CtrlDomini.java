package ControladorsDomini;

import java.io.*;
import java.util.*;

import Dades.*;
import Domini.*;
import Excepcions.*;
public class CtrlDomini{
    
    private CtrlItemFile ctrlItemFile;  //Ctrl de persistencia que llegeix del fitxer d'items
    private CtrlRatingsFile ctrlRatingsFile;  //Ctrl de persistencia que llegeix dels diferents fitxers de ratings
    private Persona PersonaActual; //indica quina persona ha fet login i doncs esta usant en aquest moment el programa.
    private int col_id; //Columna on esta l'id
    private int n_cols = 0; //Numero d'atributs totals que hi ha
    private String estrategia;  //Estrategia usada per les recomanacions
    private String estrategiaDistancia; //Estrategia usada per la distancia
    private int kkmeans;  //la variable per el kmeans
    private int kknearest;  //la variable per els k items mes propers
    private HashMap<String, Item> Items; //Map amb les dades dels Items on l'String és el id del item
    private Vector<Columna> Columnes;  //Vector amb les columnes
    private HashMap<String, UsuariActiu> Usuaris; //Conjunt d'usuaris de ratings.db
    private HashMap<String, UsuariActiu> UsuarisKnown;  //Conjunt d'usuaris de known
    private HashMap<String, UsuariActiu> UsuarisUnknown;  //Conjunt d'usuaris de unknown
    private Vector<UsuariActiu>[] clusters; //els cluster per l'algorsime collaborative filtering
    private Vector<Double>[] centroides;  //els centroides
    private static CtrlDomini singletonObject;

    //Pre:
    //Post: Es crea una instancia de domini.
    public CtrlDomini(){  
        inicialitzar();
    }

    //Pre:
    //Post: Retorna la instancia de Controlador Domini. Si no existeix cap instancia de CtrlDomini, es crea.
    public static CtrlDomini getInstance(){ //CtrlDomini es singleton
      if(singletonObject == null){
        singletonObject = new CtrlDomini();
      }
      return singletonObject;
    }

    //Pre:
    //Post: S'inicialitzen les variables necessaries.
    public void inicialitzar(){
        ctrlItemFile = CtrlItemFile.getInstance();
        ctrlRatingsFile = CtrlRatingsFile.getInstance();
        Items = new HashMap<String,Item>();
        Columnes = new Vector<Columna>();
        Usuaris = new HashMap<String, UsuariActiu>();
        UsuarisKnown = new HashMap<String, UsuariActiu>();
        UsuarisUnknown = new HashMap<String, UsuariActiu>();
        estrategia = "CollaborativeFiltering";  //Estrategia de recomanacions per defecte
        estrategiaDistancia = "Distancia Euclidiana"; //Estrategia de distancia per defecte
        kkmeans=3; //ficar segons veiem que va millor
        kknearest=3; //ficar segons veiem que va millor
    }

    //Pre: Es rep un nom d'usuari
    //Post: Es crea el usuari amb el nom rebut com a parametre i es retorna la instancia creada.
    public void iniciaInstancia(String string) { //quan es treballi amb log in es passarà també la contrasenya.
       if (string.equals("Admin")) PersonaActual= new Admin(string); //el Usuari de l'Administrador es sempre Admin
       else PersonaActual=new UsuariActiu(string);
    }
    
    //Pre: Es rep com a parametre un id d'item.
    //Post: Es retorna el item amb identificador id, si no existeix retorna null.
    public Item getItem(String id){ //Retorna el item identificat per 'id'
      return Items.get(id);
    }
  
    //Pre: 
    //Post: Es retorna el conjunt de tots els items.
    public HashMap<String, Item> getAllItems(){ //Retorna tot el conjunt d'items 
      return Items;
    }
  
    //Pre: Existeix una columna identificada amb numero nC.
    //Post: Retorna la columna identificada per nC.
    public Columna getColumna(int nC){  //Obte la columna nC
      return Columnes.get(nC);
    }
  
    //Pre:
    //Post: Retorna el conjunt de totes les columnes.
    public Vector<Columna> getAllColumnas(){
      return Columnes;
    }

    //Pre:
    //Post: Retorna el conjunt de tots els usuaris del conjunt Usuaris.
    public HashMap<String, UsuariActiu> getAllUsuaris() {
      return Usuaris;
    }

    //Pre:
    //Post: Retorna el conjunt de tots els usuaris del conjunt Usuaris Known.
    public HashMap<String, UsuariActiu> getAllUsuarisKnown() {
      return UsuarisKnown;
    }

    //Pre: Existeix un usuari identificat per id al conjunt d'Usuaris o al conjunt d'UsuarisKnown.
    //Post: Retorna la instancia del UsuariActiu.
    public UsuariActiu getUsuari(String id){
      if (Usuaris.containsKey(id)) return Usuaris.get(id);
      else if (UsuarisKnown.containsKey(id)) return UsuarisKnown.get(id);
      else return null;
    }

    //Pre: Existeix un usuari identificat per id al conjunt d'UsuarisKnown.
    //Post: Retorna la instancia del UsuariActiu.
    public UsuariActiu getUsuariKnown(String id){
      return UsuarisKnown.get(id);
    }

    //Pre: No existeix l'usuari a cap dels conjunts d'usuaris.
    //Post: S'afegeix l'usuari al conjunt d'Usuaris.
    public void afegirUsuari(UsuariActiu u){
       Usuaris.put(u.getUsuari(),u);
    }

    //Pre:
    //Post: Retorna el nom de l'estrategia de recomanacio actual.
    public String demanaEstrategiaActual(){
        return estrategia;
    }

    //Pre: El string rebut per parametre es CollaborativeFiltering o (Exclusiva) ContentBasedFiltering.
    //Post: Es canvia l'estrategia de recomanacio actual per la rebuda per parametre.
    public void canviaEstrategia(String e){
        estrategia=e;
    }

    //Pre:
    //Post: Retorna el nom de l'estrategia per al calcul de distancies utilitzada
    public String demanaDistanciaActual() {
      return estrategiaDistancia;
    }

    //Pre: El string rebut per parametre es DistanciaBasica o (Exclusiva) DistanciaEuclidiana.
    //Post: Es canvia l'estrategia de distancia actual per la rebuda per parametre.
    public void canviaEstrategiaDistancia(String e){
        estrategiaDistancia = e;
    }

    //Pre:
    //Post: Retorna el numero de k del kmeans.
    public int demanaKKmeans(){
      return kkmeans;
    }

    //Pre: Es rep per parametre un numero menor al numero d'usuaris del conjunt d'Usuaris.
    //Post: Es canvia la variable k de kmeans.
    public void canviaKKmeans(int k) throws KMassaGran{
      if (k>Usuaris.size()) throw new KMassaGran(k, Usuaris.size());
      kkmeans=k;
    }

    //Pre:
    //Post: Retorna el numero de k del knearest.
    public int demanaKKnearest(){
      return kknearest;
    }
    
    //Pre: Es rep per parametre un numero menor al numero d'items del conjunt d'Items.
    //Post: Es canvia la variable k de knearests
    public void canviaKKnearest(int k) throws KMassaGran{
      if (k>Items.size()) throw new KMassaGran(k, Items.size());
      kknearest=k;
    }

    //Pre: Es rep per parametre el nom del fitxer de Ratings.
    //Post: Es llegeix el fitxer i es guarden les dades necessaries.
    public void informaRatings(String nom) throws Exception {
      llegeixAllRatings(nom, Usuaris);
    }
    
    //Pre: Es rep per parametre el nom del fitxer de Ratings Known.
    //Post: Es llegeix el fitxer i es guarden les dades necessaries.
    public void informaKnown(String nom) throws Exception {
      llegeixAllRatings(nom, UsuarisKnown);
    }

    //Pre: Es rep per parametre el nom del fitxer de Ratings Unknown.
    //Post: Es llegeix el fitxer i es guarden les dades necessaries.
    public void informaUnknown(String nom) throws Exception {
      llegeixAllRatings(nom, UsuarisUnknown);
    }

    //Pre:
    //Post: Retorna el valor de la puntuacio mes baixa.
    public Double demanaMin(){
      return Valoracio.getMinPunt();
    }

    //Pre:
    //Post: Retorna el valor de la puntuacio mes alta.
    public Double demanaMax(){
      return Valoracio.getMaxPunt();
    }

    //Pre: min < max.
    //Post: Canvia el valor de la possible puntuacio maxima i minima.
    public void canviaMiniMax(Double min, Double max) throws RangNoValid{
      if (min>=max || Valoracio.getMaxPunt()>max || Valoracio.getMinPunt()<min) throw new RangNoValid(Valoracio.getMinPunt(),Valoracio.getMaxPunt());
      Valoracio.setMaxPunt(max);
      Valoracio.setMinPunt(min);
    }

    //Pre:
    //Post: Es recalculen els clusters.
    public void recalculaClusters() throws CoordenadesNoValides {
      CollaborativeFiltering cf = new CollaborativeFiltering(estrategiaDistancia,kkmeans);
      Pair< Vector<UsuariActiu>[] , Vector<Double>[] > result = cf.kmeans(Usuaris, Items);
      clusters = result.first;

      centroides = result.second;
  }

    //Pre:
    //Post: Retorna els clusters.
    public Vector<UsuariActiu>[] getClusters(){
        return clusters;
    }

    //Pre:
    //Post: Retorna els centroides.
    public Vector<Double>[] getCentroides(){
        return centroides;
    }
   
    /* Comentari respecte les dues següents funcions. Com que per a aquesta entrega només implementem les funcionalitats de l'administrador, 
    no tindrem mai una instancia de usuariactual com UsuariActiu i doncs si possessim aquestes operacions on tocaria segons les relacions del diagrama de classes(a la classe UsuariActiu),
    mai no es podrien provar per a aquesta primera entrega. En la proxima entrega, on si tinguem la funcionalitat de login i register i per tant poguem tenir usuarisactuals que siguin UsuariActiu,
    en aquestes dues funcions nomes es comprovara les excepcions, es crearan els objectes necessaris i es passarà la responsabilitat de demanar la recomanacio al UsuariActiu. */

    //Pre: Es reben com a parametre un id d'Usuari existent (Usuari o Known) i el nombre d'items recomanats esperats
    //Post: Es retorna el conjunt d'items recomanats ordenats de millor a pitjor
    public Vector<String> demanaRecomanacio1(String idUsuari, int nSortida) throws ExcepcionsRecomanador{
         int parametre;
         if (estrategia.equals("CollaborativeFiltering")) parametre= kkmeans;
         else parametre=kknearest;
         Recomanacio r= new Recomanacio(estrategia, estrategiaDistancia, parametre);
         UsuariActiu ua = getUsuari(idUsuari);  //Per aquesta entrega acceptem que es pugui demanar per qualsevol usuari del programa
         if (ua == null) throw new UsuariNoExisteix(idUsuari);
         int novalorats = Items.size() - ua.getValoracions().size();
         if (novalorats<nSortida) throw new NombreItemsMassaGran(nSortida, novalorats);

         Vector<String> result = r.getRecomanacio(ua, Items, nSortida);  // aqui realment s'hauria de cridar una funcio d'usuari actiu que fes el mateix, cridar r.getRecomanacio. Li passariem per parametre, r, ua, Items i nSortida.
         return result;
    }

    //Pre: Es reben com a parametre un id d'Usuari (Usuari o Known), els identificador dels items que volem ordenar i el nombre d'items recomanats esperats
    //Post: Es retorna el conjunt d'items donats ordenats de millor a pitjor
    public Vector<String> demanaRecomanacio2(String idUsuari, Vector<String> items, int nSortida) throws ExcepcionsRecomanador{
      int parametre;
      if (estrategia.equals("CollaborativeFiltering")) parametre= kkmeans;
      else parametre=kknearest;
      Recomanacio r= new Recomanacio(estrategia, estrategiaDistancia, parametre);
      
      UsuariActiu ua = getUsuari(idUsuari);  //Per aquesta entrega acceptem que es pugui demanar per qualsevol usuari del programa
      if (ua == null) throw new UsuariNoExisteix(idUsuari);
      int novalorats = Items.size() - ua.getValoracions().size();

      if (novalorats<nSortida) throw new NombreItemsMassaGran(nSortida, novalorats);
      Map<String, Item> cjtItems = new HashMap<String, Item>();

      for (String s : items){
        Item i = Items.get(s);
        if (i == null) throw new ItemNoExisteix(s); 
        if (ua.getValoracions().containsKey(s)) throw new ItemJaValorat(s,idUsuari);
        cjtItems.put(s, i); 
      }
      Vector<String> result = r.getRecomanacio(ua, cjtItems, nSortida) ;  // aqui realment s'hauria de cridar una funcio d'usuari actiu que fes el mateix, cridar r.getRecomanacio. Li passariem per parametre, r, ua, Items i nSortida.
      return result;
    }

    //Pre: Conjunt d'usuaris existents al conjunt d'Usuaris de Known, conjunt d'items existents, conjunt d'items de sortida per cada usuari.
    //Post: Retorna la valoracio DGC per cada usuari.
    public Pair <Vector<Vector<String>>,Double> valoraRecomanacio(Vector<String> UsuarisAValorar, Vector<Vector<String>> ItemsAValorar, Vector<Integer> nItemsSortida) throws ExcepcionsRecomanador {
      int parametre;
      if (estrategia.equals("CollaborativeFiltering")) parametre= kkmeans;
      else parametre=kknearest;

      Double DCGTotal=0.0;
      Vector<Vector<String>> recs = new Vector<Vector<String>>();

      for(int i=0; i<UsuarisAValorar.size(); ++i){
        Recomanacio r= new Recomanacio(estrategia, estrategiaDistancia, parametre);

        String idUsuari = UsuarisAValorar.get(i);
        UsuariActiu ua = UsuarisKnown.get(idUsuari);
        if (ua == null) throw new UsuariNoExisteix(UsuarisAValorar.get(i));

        int novalorats = UsuarisUnknown.get(idUsuari).getValoracions().size();
        if (novalorats<nItemsSortida.get(i)) throw new NombreItemsMassaGran(idUsuari, nItemsSortida.get(i), novalorats);
        Map<String, Item> cjtItems = new HashMap<String, Item>();

        for (String s : ItemsAValorar.get(i)){
          Item it = Items.get(s);
          if (it == null) throw new ItemNoExisteix(s); 
          else if (ua.getValoracions().containsKey(s)) throw new ItemJaValorat(s,UsuarisAValorar.get(i));
          if (!UsuarisUnknown.get(idUsuari).getValoracions().containsKey(s)) throw new ItemNoEsDeUnknown(s, idUsuari);
          cjtItems.put(s, it); 
        }

        Vector<String> recomanacio = PersonaActual.getRecomanacio(r, ua, cjtItems, nItemsSortida.get(i));
        recs.add(recomanacio);
        Double DCGParcial;
        DCGParcial = PersonaActual.getValoracioRecomanacio(r, ua, recomanacio, UsuarisUnknown);
        DCGTotal+= DCGParcial;
    }
      Pair <Vector<Vector<String>>,Double> result = new Pair<Vector<Vector<String>>,Double>();
      result.first=recs;
      result.second=DCGTotal/UsuarisAValorar.size();
      return result;
    }


    /*FUNCIONS DE LECTURA DE FITXERS*/

     //Funció implementada per: Simón
     //Pre: Existeix un fitxer amb nom igual que filename i es del tipus "Ratings".
     //Post: Llegeix tot el fitxer i guarda les dades al sistema.
     public void llegeixAllRatings(String filename, HashMap<String, UsuariActiu> cjtUsuaris) throws Exception{
      double puntMax = Double.MIN_VALUE; //fiquem el mínim (girat, sí) i en llegir ratings s'actualitza
      double puntMin = Double.MAX_VALUE; //fiquem el màxim (girat, sí) i en llegir ratings s'actualitza
      List<String> dadesRatings = ctrlRatingsFile.getAll(filename+".csv");

      //comencem per 1 perquè la primera fila sempre és el nom de les columnes "userId,itemId,rating"
      for (int i = 1; i < dadesRatings.size(); ++i) {

        /*retorna un array amb 3 strings que corresponen als valors separats per les comes:
        valors = {"userId","itemId","rating"}*/
        String valors[] = dadesRatings.get(i).split(",");

        //Si l'usuari encara no existeix, el crearem
        if (!cjtUsuaris.containsKey(valors[0])) {
          UsuariActiu u = new UsuariActiu(valors[0]);
          cjtUsuaris.put(valors[0], u);
        }

        double puntuacio = Double.parseDouble(valors[2]);

        //comprovem si hem d'actualitzar les puntuacions màxima o mínima (no es pot fer un if/else per com estan inicialitzats els valors)
        if (puntuacio < puntMin) puntMin = puntuacio;
        if (puntuacio > puntMax) puntMax = puntuacio;
        
        Valoracio rating = new Valoracio(puntuacio, Items.get(valors[1]), cjtUsuaris.get(valors[0]));
        cjtUsuaris.get(valors[0]).afegirValoracio(rating);
      }
      Valoracio.setMinPunt(puntMin);
      Valoracio.setMaxPunt(puntMax);
    }
    
  
    //La factoria d'atributs rep un Item, una Columna i un HashSet de Strings. Funcio implementada per Joel Cardona. 
    //Pre: Existeix el fitxer amb nom filename i conte la informacio de tots els items
    //Post: Crea totes les instancies d'items que existeixin en el fitxer.
    public void llegeixAllItems(String filename) throws FileNotFoundException{
      List<String> itemsdades = ctrlItemFile.getAll(filename+".csv");

      String conjunt_dades = itemsdades.get(0);
      String paraula_act = "";
      
      //detectar on esta el id i calcular quants atributs hi ha
      for (int j = 0; j < conjunt_dades.length(); j++){
          if (conjunt_dades.charAt(j) == ',' || j == conjunt_dades.length()-1){
          Columna actual = new Columna(n_cols, paraula_act);
          Columnes.add(actual);
          if (paraula_act.equals("id")) col_id = n_cols;
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

    
}

//Clase implementada per Joel Cardona, Simon Oliva, Marina Alapont. 
