package Domini.Model;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import Excepcions.*;
public class Recomanacio {

    private Strategy str;
    private Usuari usuari; 
    private TreeSet<Valoracio> resultat; 
    private int id;
    
    //Pre: l'Usuari existeix, l'usuari no te cap recomanacio identificada amb id
    //Post: Creadora de recomanacio
    public Recomanacio(int id, Usuari user, TreeSet<Valoracio> r){
        usuari=user;
        resultat=r;
        this.id=id;
    }
    
    //Pre: l'Usuari existeix, l'usuari no te cap recomanacio identificada amb id
    //Post: Creadora de recomanacio
    public Recomanacio(int id, String estrategia, String estrategiaDistancia, int k) {
        resultat = new TreeSet<Valoracio>(new ComparatorValoracio());
        if (estrategia.equals("CollaborativeFiltering")) str = new CollaborativeFiltering(estrategiaDistancia, k);
        else if (estrategia.equals("ContentBasedFiltering")) str = new ContentBasedFiltering(k);
        else if (estrategia.equals ("HybridApproaches")) str = new HybridApproaches(estrategiaDistancia, k);
        this.id=id;

    }

    //Pre:
    //Post: Es retorna l'usuari de la recomanacio
    public Usuari getUsuari(){
        return usuari;
    }

    //Pre:
    //Post: Es retorna el resultat de la recomanacio
    public TreeSet<Valoracio> getResultat(){
        return resultat;
    }

    //Pre:
    //Post: Es retorna el nom de l'estrategia del filtering usat
    public String getEstrategia(){
        return str.getNom();
    }

    //Pre:
    //Post: Es retorna l'id
    public int getId(){
        return id;
    }
 
    //Pre: L'usuari u existeix, el conjunt d'items te almenys un element, n > 0
    //Post: Es retornen els ids dels items de la recomanacio feta per al usuari u, ordenats de millor a pitjor
    public  Vector<String> getRecomanacio(Usuari u, Map<String,Item> items, int n) throws ExcepcionsRecomanador {
        Vector<String> retorn = new Vector<String>(); //retornarem un vector de strings que seran els id dels items, estaran en ordre descendent
        usuari = u;
        TreeSet<Valoracio> aux = str.Filtering(u, items);
        if (u.retornaUsuariActiu()!=null) u.retornaUsuariActiu().afegirRecomanacio(this);
        int cont = 0;
        Iterator<Valoracio> it = aux.iterator();
        Iterator <Valoracio> it2 = aux.iterator();
        while (it.hasNext() && it2.hasNext() && cont < n) { //agafem del resultat dels algoritmes els n items millors, ordenadament.
            ++cont;
            resultat.add(it2.next());
            retorn.add(it.next().getIdItem());
        }
        return  retorn;
    }
      

}

//Classe programada per Marina Alapont, Daniel Pulido 
