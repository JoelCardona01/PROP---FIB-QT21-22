package Domini;

import java.util.Vector;

import ControladorsDomini.CtrlDomini;
import Excepcions.CoordenadesNoValides;
import Excepcions.ExcepcionsRecomanador;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.Iterator;



public class CollaborativeFiltering implements Strategy {

    private String nom;
    private StrategyDistancia StDist;
    static private int  k;
  

    public CollaborativeFiltering(String estrategiaDistancia, int ka) {
        nom = "CollaborativeFiltering";
        k = ka;
        if(estrategiaDistancia.equals("Distancia Euclidiana")) StDist = new DistanciaEuclidiana();
        else StDist = new DistanciaBasica();
    }

    public String getNom () {
        return nom;
    }

    
    // Funcio Filtering feta per Marina Alapont Vidal
    public TreeSet<Valoracio> Filtering (UsuariActiu ua, Map<String,Item> items) throws ExcepcionsRecomanador{
        
        CtrlDomini DominiCtrl = CtrlDomini.getInstance(); 
        HashMap <String, Item> allitems = DominiCtrl.getAllItems();
        Vector<UsuariActiu>[] clusters = DominiCtrl.getClusters();
        Vector<Double>[] centroides = DominiCtrl.getCentroides();
        int part= calculaParticio (ua, centroides, allitems);
        Vector<UsuariActiu> uparticio = clusters[part];
  
        // D'aqui en endevant es Slope one, ja fare una funcio a part anomenada SlopeOne
        TreeSet<Valoracio> result = new TreeSet<Valoracio>(new ComparatorValoracio()); 
        for (HashMap.Entry<String, Item> i : items.entrySet()) { //per tots els items de l'entrada
            if (!ua.getValoracions().containsKey(i.getKey())){  //si el usuari ua no ha valorat el item
                double puntuacioEstimada=0;
                double sumatori=0;
                double count=0;
                for (HashMap.Entry<String, Valoracio> iv : ua.getValoracions().entrySet()) { //recorrem les valoracions del usuari ua
                    double sumatoriparcial=0;
                    double contaUsuaris=0;
                    for (UsuariActiu up : uparticio) {
                        if (up.getValoracions().containsKey(i.getKey()) && up.getValoracions().containsKey(iv.getKey())){    //si l'usuari de la partició ha valorat els dos items donats
                            double puntuacio1= up.getValoracions().get(i.getKey()).getPuntuacio(); //agafo la puntuació de la valoració del usuari up a l'item i (no valorat per el usuari ua)
                            double puntuacio2= up.getValoracions().get(iv.getKey()).getPuntuacio(); //agafo la puntuació de la valoració del usuari up a l'item amb id iv.getkey() (si valorat per ua)
                            double resta=  puntuacio1-puntuacio2;
                            sumatoriparcial+=resta;
                            ++contaUsuaris;
                        }
                    }
                    if (contaUsuaris!=0.0){
                        ++count; 
                        double desvMitjana = sumatoriparcial / contaUsuaris;
                        sumatori+= Math.max(Valoracio.getMinPunt(), Math.min(Valoracio.getMaxPunt(),(desvMitjana + iv.getValue().getPuntuacio())));
                    }
                }
                if (count != 0.0) puntuacioEstimada= sumatori/count;
                else puntuacioEstimada=2.5; //Si cap usuari ha valorat el item a predir o els items que si ha valorat el usuari ua
                
                Valoracio v= new Valoracio (puntuacioEstimada,i.getValue(),ua);
                v.AssignaPredictiva(true);
                result.add(v);
            } 
        }
        return result;
        
    }

    private int calculaParticio(UsuariActiu ua, Vector<Double>[] centroides, Map<String,Item> items) {
        double min= 1000000.0;
        int result=-1;
        for (int i=0; i<k; ++i){
           double dist= StDist.distancia(ua.getCoordenades(items), centroides[i]);
           if (min> dist){
               min=dist;
               result=i;
           }
        }
        return result;
    }

    //Algorisme fet per: Simón Helmuth Oliva Stark
    //pre: nclusters <= allUsers.size()
    //retorna Pair<clusters[], centroides[]> , on centroides[i] correspon a les coordenades del centroide del cluster clusters[i]
    public Pair< Vector<UsuariActiu>[] , Vector<Double>[] > kmeans(HashMap<String, UsuariActiu> allUsers, Map<String, Item> allItems) throws CoordenadesNoValides {
        
        //Creem tants clústers (de coordenades) buits com indica el paràmetre nclusters
        int nclusters = k;
        HashMap<String, Vector<Double>>[] clusters = new HashMap[nclusters];
        for (int i=0; i<nclusters; ++i) clusters[i]= new HashMap<String, Vector<Double>>(); //necessari crear un per un els maps, ara especificant que són de <String, Vector<Double>>

        //Assignem unes coordenades a cada usuari segons els ítems que tenim
        /*     < idUsuari , coordenades >      */
        HashMap<String, Vector<Double>> CoordenadesUsuaris = new HashMap<String, Vector<Double>>();
        for (HashMap.Entry<String, UsuariActiu> u : allUsers.entrySet()) {
            CoordenadesUsuaris.put(u.getKey(), u.getValue().getCoordenades(allItems));
        }

        /*Creem un array de centroides on centroides[i] es correspon amb clusters[i]*/
        Vector<Double>[] centroides = new Vector[nclusters];
        
        //Alternativa random del càlcul de centroides
        Iterator<HashMap.Entry<String,Vector<Double>> > it = CoordenadesUsuaris.entrySet().iterator();
        int maxSaltsPerIteracio = CoordenadesUsuaris.size()/nclusters;
        for (int i=0; i<nclusters; ++i) {
            //definim un nombre aleatori entre 0 i maxSaltsPerIteracio-1
            int nSalts = (int)(Math.random()*maxSaltsPerIteracio);
            for (int j = 0; j < nSalts; ++j) it.next(); //iterem sobre el map unes quantes vegades
            centroides[i] = it.next().getValue();
        }

        //Càlcul primers centroides no random:

        /*Iterator<HashMap.Entry<String,Vector<Double>> > it = CoordenadesUsuaris.entrySet().iterator(); //definim un iterador
        //no és molt random, però pot funcionar
        for (int i=0; i<nclusters; ++i) {
            centroides[i] = it.next().getValue();
        }
        */

        Boolean canvi_de_cluster = true;
        //Reassignem usuaris a cada clúster i recalculem els centroides fins que cap usuari canvii de cluster
        
        int niteracions = 0;
        while (canvi_de_cluster && (niteracions <= 100)) { //fem com a màxim 100 iteracions per evitar caure en un bucle infinit o massa llarg
            canvi_de_cluster = false;     
            for (HashMap.Entry<String, Vector<Double>> cu : CoordenadesUsuaris.entrySet()) {
                
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
                 
            }
            //recalculem centroides
            if (canvi_de_cluster) {
                int ndimensions = centroides[0].size();
                for(int i = 0; i < nclusters; ++i) {
                    //inicialitzem el centroide a 0 en cada dimensió
                    for (int j = 0; j < ndimensions; ++j) {
                        centroides[i].set(j, 0.0);
                    }
                    //sumem totes les coordenades
                    for (HashMap.Entry<String, Vector<Double>> cu : clusters[i].entrySet()) {
                        for (int j = 0; j < ndimensions; ++j) {
                            centroides[i].set(j, centroides[i].elementAt(j) + cu.getValue().elementAt(j));
                        }
                    }
                    //dividim pel nombre d'usuaris del clúster
                    double nusuaris = clusters[i].size();
                    if (nusuaris == 0) nusuaris = 1; //per no dividir entre 0
                    for (int j = 0; j < ndimensions; ++j) {
                        centroides[i].set(j, centroides[i].elementAt(j)/nusuaris);
                    }                    
                }
            }
            ++niteracions;
        }  

        //Preparar conjunt de retorn
        Vector<UsuariActiu>[] clustersUsuaris = new Vector[nclusters];
        for (int i=0; i<nclusters; ++i) {
            clustersUsuaris[i] = new Vector<UsuariActiu>(clusters[i].size());

            for (HashMap.Entry<String, Vector<Double>> u : clusters[i].entrySet()) {
                clustersUsuaris[i].add(allUsers.get(u.getKey()));
            }
        }
      
        Pair<Vector<UsuariActiu>[],Vector<Double>[]> resposta = new Pair <Vector<UsuariActiu>[],Vector<Double>[]>();
        resposta.first=clustersUsuaris;
        resposta.second= centroides;

        return resposta;
    }

    


}
