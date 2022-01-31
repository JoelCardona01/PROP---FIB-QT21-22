package Domini.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.TreeSet;
import java.util.Vector;

import Domini.ControladorsDomini.CtrlDomini;
import  Domini.DataInterface.FactoriaCtrl;
import Excepcions.ExcepcionsRecomanador;

public class HybridApproaches implements Strategy {
    private String nom;
    private StrategyDistancia StDist;
    static private int kn;

    //Pre: k > 0
    //Post: Creadora de la classe HybridApproaches
    public HybridApproaches(String estrategiaDistancia, int k) {
        nom = "HybridApproaches";
        kn = k;
        if(estrategiaDistancia.equals("Distancia Euclidiana")) StDist = new DistanciaEuclidiana();
        else StDist = new DistanciaMitjana();
    }

    //Pre:
    //Post: Es retorna el nom de la funcio de filtering
    public String getNom() {
        return nom;
    }

    
    //Pre: Existeix l'usuari u i el map d'items NO es buit
    //Post: S'obte un conjunt ordenat per puntuacio de k items que l'usuari u no ha puntuat i son els que mes puntuacio els hi donara
    public TreeSet<Valoracio> Filtering (Usuari u, Map<String,Item> items) throws ExcepcionsRecomanador{
        
        FactoriaCtrl fc = FactoriaCtrl.getInstance(); 
        CtrlDomini DominiCtrl = fc.getCtrlDomini();
        HashMap <String, Item> allitems = DominiCtrl.getAllItems();
        Vector<UsuariFitxers>[] clusters = DominiCtrl.getClusters();
        HashMap<String,Double>[] centroides = DominiCtrl.getCentroides();
        Double puntmin= DominiCtrl.getPuntuacioMinima();
        Double puntmax= DominiCtrl.getPuntuacioMaxima();

        int part= calculaParticio (u, centroides);
        Vector<UsuariFitxers> uparticio = clusters[part];
  
        TreeSet<Valoracio> result = new TreeSet<Valoracio>(new ComparatorValoracio()); 
        for (HashMap.Entry<String, Item> i : items.entrySet()) { //per tots els items de l'entrada
            if (!u.getValoracions().containsKey(i.getKey())){  
                //si el usuari ua no ha valorat el item, Calculem la puntuació fent l'algorisme SlopeOne modificat en cas de no exit.
                double puntuacioEstimada=0;
                double sumatori=0;
                double count=0;
                for (HashMap.Entry<String, Valoracio> iv : u.getValoracions().entrySet()) { //recorrem les valoracions del usuari ua
                    double sumatoriparcial=0;
                    double contaUsuaris=0;
                    for (UsuariFitxers up : uparticio) {
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
                        sumatori+= Math.max(puntmin, Math.min(puntmax,(desvMitjana + iv.getValue().getPuntuacio())));
                    }
                }
                if (count != 0.0) puntuacioEstimada= sumatori/count;
                else{
                    puntuacioEstimada = hybrid(1,i.getValue(), allitems, u, uparticio, puntmin, puntmax);
                }
    
                Valoracio v= new Valoracio (puntuacioEstimada,i.getValue(),u);
                v.AssignaPredictiva(true);
                result.add(v);
            } 
        }
        return result;      
    }

    //Pre: nVegades >= 0, l'item it existeix, allItems conte tots els items del sistema, l'usuari u existeix, uparticio conte els usuaris de la particio del usuari
    //Pre: puntmin >= 0, puntmax >= 0 i puntmin < puntmax
    //Post: Es retorna un double que representa la nota que l'usuari u donara al item it
    private double hybrid(int nVegades, Item it, HashMap<String, Item> allItems, Usuari u, Vector<UsuariFitxers> uparticio, double puntmin, double puntmax) throws ExcepcionsRecomanador{
        PriorityQueue< Pair<Double,Item> > mesPropers = KNearests(it, allItems, kn, u);
        double result = 0.0;
        while(!mesPropers.isEmpty()) { //per tots els items de l'entrada
            Item iessim = mesPropers.poll().second;
            if (!u.getValoracions().containsKey(iessim.getId())){  //si el usuari ua no ha valorat el item
                double puntuacioEstimada=0;
                double sumatori=0;
                double count=0;
                for (HashMap.Entry<String, Valoracio> iv : u.getValoracions().entrySet()) { //recorrem les valoracions del usuari ua
                    double sumatoriparcial=0;
                    double contaUsuaris=0;
                    for (UsuariFitxers up : uparticio) {
                        if (up.getValoracions().containsKey(iessim.getId()) && up.getValoracions().containsKey(iv.getKey())){    //si l'usuari de la partició ha valorat els dos items donats
                            double puntuacio1= up.getValoracions().get(iessim.getId()).getPuntuacio(); //agafo la puntuació de la valoració del usuari up a l'item i (no valorat per el usuari ua)
                            double puntuacio2= up.getValoracions().get(iv.getKey()).getPuntuacio(); //agafo la puntuació de la valoració del usuari up a l'item amb id iv.getkey() (si valorat per ua)
                            double resta=  puntuacio1-puntuacio2;
                            sumatoriparcial+=resta;
                            ++contaUsuaris;
                        }
                    }
                    if (contaUsuaris!=0.0){
                        ++count; 
                        double desvMitjana = sumatoriparcial / contaUsuaris;
                        sumatori+= Math.max(puntmin, Math.min(puntmax,(desvMitjana + iv.getValue().getPuntuacio())));
                    }
                }
                if (count != 0.0) puntuacioEstimada= sumatori/count;
                else {
                    if(nVegades == 0) puntuacioEstimada = Math.random()*(puntmax-puntmin)+puntmin;
                    else puntuacioEstimada=hybrid(nVegades-1, it, allItems, u, uparticio, puntmin, puntmax);
                }

                result+=puntuacioEstimada;
            } 
        }
        return (result/kn);     
    }


    //Pre: L'usuari u existeix, centroides no es una array buida
    //Post: Es retorna el numero de la particio a la que pertany l'usuari u
    private int calculaParticio(Usuari u,HashMap<String,Double>[] centroides) {
        int ncentroides= centroides.length;
        double min= 1000000.0;
        int result=-1;
        for (int i=0; i< ncentroides; ++i){
           double dist= StDist.distancia(u.getCoordenades(), centroides[i]);
           if (min> dist){
               min=dist;
               result=i;
           }
        }
        if(result==-1) return (int) Math.random()%ncentroides;
        return result;
    }

    //Pre: L'item donat existeix, tots conte tots els items del sistema, kn > 0, l'usuari u existeix
    //Post: Es retorna un conjunt de kn items mes semblants al item donat de manera ordenada de millor a pitjor
    private PriorityQueue< Pair<Double,Item> > KNearests(Item donat, Map<String,Item> tots, int kn, Usuari u) throws ExcepcionsRecomanador{
        PriorityQueue< Pair<Double,Item> > queue = new PriorityQueue< Pair<Double,Item> >(new ComparatorPair());
        //Mirem per l'item la similitud amb els items "tots" i els fiquem a "queue" on estaran ordenats de mayor a menor.
        for(Map.Entry<String,Item> entry : tots.entrySet()){
            if(!u.getValoracions().containsKey(entry.getKey())){
                if(donat.getId() != entry.getValue().getId()){
                    double aux = donat.ComparaItem(entry.getValue());
                    Pair<Double, Item> act = new Pair<Double, Item>();
                    act.first = aux;
                    act.second = entry.getValue();
                    queue.add(act);
                }
            }
        }
        PriorityQueue< Pair<Double,Item> > knear = new PriorityQueue< Pair<Double,Item> >(new ComparatorPair());
        int cont = 0;
        while (!queue.isEmpty() && cont < kn){
            ++cont;
            knear.add(queue.poll());
        }

        return knear;
    }
    
}

//Classe feta per Joel Cardona i Marina Alapont