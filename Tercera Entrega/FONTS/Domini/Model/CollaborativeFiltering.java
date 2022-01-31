package Domini.Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import Domini.ControladorsDomini.CtrlDomini;
import Domini.DataInterface.FactoriaCtrl;
import Excepcions.CoordenadesNoValides;
import Excepcions.ExcepcionsRecomanador;



public class CollaborativeFiltering implements Strategy {

    private String nom;
    private StrategyDistancia StDist;
    static private int  k;
  

    public CollaborativeFiltering(String estrategiaDistancia, int ka) {
        nom = "CollaborativeFiltering";
        k = ka;
        if(estrategiaDistancia.equals("Distancia Euclidiana")) StDist = new DistanciaEuclidiana();
        else StDist = new DistanciaMitjana();
    }

    public String getNom () {
        return nom;
    }

    
    //Funcio implementada per: Marina Alapont Vidal.
    //Pre: items conté un subconjunt d'items del sistema tals que no han estat valorats per l'usuari u.
    //Post: retorna un TreeSet amb les Valoracions predites per els items del conjunt 'items' i l'usuari u, ordenades descendentment.
    public TreeSet<Valoracio> Filtering (Usuari u, Map<String,Item> items) throws ExcepcionsRecomanador{
        
        FactoriaCtrl fc = FactoriaCtrl.getInstance(); 
        CtrlDomini DominiCtrl = fc.getCtrlDomini();
        Vector<UsuariFitxers>[] clusters = DominiCtrl.getClusters();
        HashMap<String,Double>[] centroides = DominiCtrl.getCentroides();
        Double puntmin= DominiCtrl.getPuntuacioMinima();
        Double puntmax= DominiCtrl.getPuntuacioMaxima();

        int part= calculaParticio (u, centroides); // calculem de quina particio formaria par l'usuari u.
        Vector<UsuariFitxers> uparticio = clusters[part];
  
        TreeSet<Valoracio> result = new TreeSet<Valoracio>(new ComparatorValoracio()); 
        
        for (HashMap.Entry<String, Item> i : items.entrySet()) { //per tots els items de l'entrada

            Double puntuacioEstimada = SlopeOne(u, i.getValue(), uparticio, puntmax, puntmin); // deduim una puntuacio a traves de SlopeOne.
            //creem la valoracio predictiva i l'afegim al TreeSet resultat.
            Valoracio v= new Valoracio (puntuacioEstimada,i.getValue(),u);
            v.AssignaPredictiva(true);
            result.add(v);  
        }
        return result;      
    }

    //Funcio implementada per Marina Alapont.
    //Pre: 
    //Post: Retorna un integer que representa a quin cluster pertany l'usuari u segons la distancia als diferents centroides. 
    private int calculaParticio(Usuari u,HashMap<String,Double>[] centroides) {
        int ncentroides= centroides.length;
        double min= 1000000.0;
        int result=-1;
        for (int i=0; i<k; ++i){
           double dist= StDist.distancia(u.getCoordenades(), centroides[i]);
           if (min> dist){
               min=dist;
               result=i;
           }
        }
        if(result==-1) return (int) Math.random()%ncentroides;
        return result;
    }

    //Funcio implementada per Marina Alapont.
    //Pre: i no ha estat valorat per l'usuari u.
    //Post: Retorna la nota estimada que l'usuari donaria per l'item i.
    public Double SlopeOne(Usuari u, Item i,  Vector<UsuariFitxers> uparticio, Double puntmax, Double puntmin) {
            double puntuacioEstimada=0;
            double sumatori=0;
            double count=0;
            for (HashMap.Entry<String, Valoracio> iv : u.getValoracions().entrySet()) { //recorrem les valoracions del usuari ua
                double sumatoriparcial=0;
                double contaUsuaris=0;
                for (UsuariFitxers up : uparticio) {
                    if (up.getValoracions().containsKey(i.getId()) && up.getValoracions().containsKey(iv.getKey())){    //si l'usuari de la partició ha valorat els dos items donats.
                        double puntuacio1= up.getValoracions().get(i.getId()).getPuntuacio(); //agafo la puntuació de la valoració del usuari up a l'item i (no valorat per el usuari ua).
                        double puntuacio2= up.getValoracions().get(iv.getKey()).getPuntuacio(); //agafo la puntuació de la valoració del usuari up a l'item amb id iv.getkey() (si valorat per ua).
                        double resta=  puntuacio1-puntuacio2;
                        sumatoriparcial+=resta;
                        ++contaUsuaris;
                    }
                }
                if (contaUsuaris!=0.0){  //si al menys hi ha hagut una persona que ha valorat la parella d'items.
                    ++count; 
                    double desvMitjana = sumatoriparcial / contaUsuaris;
                    sumatori+= Math.max(puntmin, Math.min(puntmax,(desvMitjana + iv.getValue().getPuntuacio())));
                }
            }
            if (count != 0.0) puntuacioEstimada= sumatori/count; //si almenys una de les possibles parelles item no valorat (a predir puntuacio) i item si valorat ha estat valorada per almenys un usuari de uparticio.
            else puntuacioEstimada=Math.random()*(puntmax-puntmin)+puntmin; //en cas contrari, si no s'ha pogut predir una puntuacio a traves de slope one, generem una random.
            return puntuacioEstimada;
        }


    //Algorisme fet per: Simón Helmuth Oliva Stark.
    //Pre: nclusters <= allUsers.size()
    //Post: retorna Pair<clusters[], centroides[]> , on centroides[i] correspon a les coordenades del centroide del cluster clusters[i]
    public Pair<Vector<UsuariFitxers>[], HashMap<String, Double>[]> kmeans(HashMap<Integer, UsuariFitxers> allUsersFitxers, Map<String, Item> allItems) throws CoordenadesNoValides {
        
        //Creem tants clústers (de coordenades) buits com indica el paràmetre nclusters
        int nclusters = k;
        HashMap<Integer,HashMap<String,Double>>[] clusters = new HashMap[nclusters];
        for (int i=0; i<nclusters; ++i) clusters[i]= new HashMap<Integer, HashMap<String,Double>>(); //necessari crear un per un els maps, ara especificant que són de <Integer, Vector<Double>>

        //Assignem unes coordenades a cada usuari segons els ítems que tenim
        /*     < idUsuari , coordenades >      */
        HashMap<Integer,HashMap<String,Double>> CoordenadesUsuaris = new HashMap<Integer, HashMap<String,Double>>();
        for (HashMap.Entry<Integer, UsuariFitxers> u : allUsersFitxers.entrySet()) {
            CoordenadesUsuaris.put(u.getKey(), u.getValue().getCoordenades());
        }

        /*Creem un array de centroides on centroides[i] es correspon amb clusters[i]*/
        HashMap<String,Double>[] centroides = new HashMap[nclusters];
        
        //Alternativa random del càlcul de centroides
        CalculaCentroidesRandom(nclusters, CoordenadesUsuaris, centroides);

        Boolean canvi_de_cluster = true;
        //Reassignem usuaris a cada clúster i recalculem els centroides fins que cap usuari canvii de cluster
        
        int niteracions = 0;
        while (canvi_de_cluster && (niteracions <= 100)) { //fem com a màxim 100 iteracions per evitar caure en un bucle infinit o massa llarg
            canvi_de_cluster = false;     
            for (HashMap.Entry<Integer, HashMap<String,Double>> cu : CoordenadesUsuaris.entrySet()) {
                
                canvi_de_cluster = reassignaUsuari_Clusters(nclusters, clusters, centroides, canvi_de_cluster, cu);
                 
            }
            //recalculem centroides
            recalculaCentroides(allItems, nclusters, clusters, centroides, canvi_de_cluster);
            ++niteracions;
        }  

        //Preparar conjunt de retorn
        Pair<Vector<UsuariFitxers>[], HashMap<String, Double>[]> resposta =
            prepararConjuntRetorn(allUsersFitxers, nclusters, clusters, centroides);

        return resposta;
    }

    //Funció programada per Simon Helmuth Oliva (extreta automàticament de kmeans fent refactoring)
    //Pre: nclusters == clusters.size() == centroides.size()
    //Post: Cada usuari està en el clúster tal que el seu centroide li és el més proper
    //Post: Retorna true si algun usuari ha canviat de clúster
    private Boolean reassignaUsuari_Clusters(int nclusters, HashMap<Integer, HashMap<String, Double>>[] clusters,
            HashMap<String, Double>[] centroides, Boolean canvi_de_cluster,
            HashMap.Entry<Integer, HashMap<String, Double>> cu) throws CoordenadesNoValides {
                
        Double mindist = StDist.distancia(centroides[0], cu.getValue());
        if (mindist==null) throw new CoordenadesNoValides();
        Integer numCentroideMesProper = 0;
        
        for (int i = 1; i < nclusters; ++i) { //mirem quin dels centroides està més a prop
            Double dist = StDist.distancia(centroides[i], cu.getValue());
            if (dist < mindist) {
                mindist = dist;
                numCentroideMesProper = i;
            }                    
        }

        //si l'usuari està en un clúster que no és el més proper, l'esborrem d'allà
        Boolean trobat = false;
        for (int i = 0; (i < nclusters) && (!trobat); ++i) {
            if (clusters[i].containsKey(cu.getKey()) && (!numCentroideMesProper.equals(i))) {
                trobat = true;
                clusters[i].remove(cu.getKey());
            }       
        }
        //si no estava ja en el més proper, el col·loquem
        if (!clusters[numCentroideMesProper].containsKey(cu.getKey())) {
            clusters[numCentroideMesProper].put(cu.getKey(), cu.getValue());
            canvi_de_cluster = true;
        }
        return canvi_de_cluster;
    }

    /*Funció programada per Simon Helmuth Oliva (extreta automàticament de kmeans fent refactoring)
    //Pre: cret
    //Post: Retorna el resultat d'aplicar l'algorisme en el format desitjat: un Pair on l'array de
    //vectors correspon al conjunt de clústers amb els seus usuaris corresponents i un array de 
    //HashMap amb els centroides corresponents*/
    private Pair<Vector<UsuariFitxers>[], HashMap<String, Double>[]> prepararConjuntRetorn(
            HashMap<Integer, UsuariFitxers> allUsersFitxers, int nclusters,
            HashMap<Integer, HashMap<String, Double>>[] clusters, HashMap<String, Double>[] centroides) {
        Vector<UsuariFitxers>[] clustersUsuaris = new Vector[nclusters];
        for (int i=0; i<nclusters; ++i) {
            clustersUsuaris[i] = new Vector<UsuariFitxers>(clusters[i].size());

            for (HashMap.Entry<Integer, HashMap<String, Double>> u : clusters[i].entrySet()) {
                clustersUsuaris[i].add(allUsersFitxers.get(u.getKey()));
            }
        }
      
        Pair<Vector<UsuariFitxers>[], HashMap<String,Double>[]> resposta = new Pair <Vector<UsuariFitxers>[],HashMap<String,Double>[]>();
        resposta.first=clustersUsuaris;
        resposta.second= centroides;
        return resposta;
    }

    /*Funció programada per Simon Helmuth Oliva (extreta automàticament de kmeans fent refactoring)
    //Pre: cert
    //Post: Recalcula les coordenades dels centroides fent la mitjana de les coordenades dels usuaris
    //que conté cadascun*/
    private void recalculaCentroides(Map<String, Item> allItems, int nclusters,
            HashMap<Integer, HashMap<String, Double>>[] clusters, HashMap<String, Double>[] centroides,
            Boolean canvi_de_cluster) {
        if (canvi_de_cluster) {
            
            int ndimensions = allItems.size();

            Vector <String> Iditems = new Vector<String>();
            for (HashMap.Entry<String, Item> ite : allItems.entrySet()){
                Iditems.add(ite.getKey());
            }

            for(int i = 0; i < nclusters; ++i) {
                //inicialitzem el centroide a 0 en cada dimensió
                for (int j = 0; j < ndimensions; ++j) {
                    centroides[i].put(Iditems.get(j), 0.0);
                }
                //sumem totes les coordenades
                for (HashMap.Entry<Integer, HashMap<String, Double>> cu : clusters[i].entrySet()) {
                    for (int j = 0; j < ndimensions; ++j) {
                        if(cu.getValue().containsKey(Iditems.get(j))){
                            centroides[i].put(Iditems.get(j), centroides[i].get(Iditems.get(j)) + cu.getValue().get(Iditems.get(j)));
                        }
                        
                    }
                }
                //dividim pel nombre d'usuaris del clúster
                double nusuaris = clusters[i].size();
                if (nusuaris == 0) nusuaris = 1; //per no dividir entre 0
                for (int j = 0; j < ndimensions; ++j) {
                    centroides[i].put(Iditems.get(j), centroides[i].get(Iditems.get(j))/nusuaris);
                }                    
            }
        }
    }

    /*Funció programada per Simon Helmuth Oliva (extreta automàticament de kmeans fent refactoring)
    //Pre: cert
    //Post: Calcula les coordenades dels k centroides agafant les coordenades de k usuaris a l'atzar*/
    private void CalculaCentroidesRandom(int nclusters, HashMap<Integer, HashMap<String, Double>> CoordenadesUsuaris,
            HashMap<String, Double>[] centroides) {
        HashSet<Integer> usuarisAgafats = new HashSet<Integer>(); //Set on tenim els ids dels usuaris que ja formen part de un centroide
        for (int i=0; i<nclusters; ++i) {
            Iterator<HashMap.Entry<Integer, HashMap<String,Double>> > it = CoordenadesUsuaris.entrySet().iterator();    //Iterador que farem servir per accedir a un Usuari random
            int nUsuari = (int) (Math.random()*CoordenadesUsuaris.size()); //Agafem un numero aleatori que sera el possible centroide
            for (int j = 0; j < nUsuari; ++j) it.next(); //iterem sobre el map el nombre de vegades aleatori anterior
            int userIdRandom = it.next().getKey();  //Guardem l'id de l'usuari
            if(!usuarisAgafats.contains(userIdRandom) || usuarisAgafats.isEmpty()){ //Mirem que aquest usuari no sigui un centroide ja assignat
                centroides[i] = CoordenadesUsuaris.get(userIdRandom);   //Afegim les coordenades al centroide iessim
                usuarisAgafats.add(userIdRandom);   //Marquem com a marcat l'usuari agafat
            }
            else --i;   //En el cas que ja hagues sigut aquest usuari, fem un altre iteracio per a trobar un usuari nou
        }
    }

    


}
