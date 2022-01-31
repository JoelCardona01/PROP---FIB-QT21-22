package Domini;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import Excepcions.*;
public class Recomanacio {

    private Strategy str;
    private UsuariActiu usuari;
    private TreeSet<Valoracio> resultat; 
    

    public Recomanacio(String estrategia, String estrategiaDistancia, int k) {
        resultat = new TreeSet<Valoracio>();
        if (estrategia.equals("CollaborativeFiltering")) str = new CollaborativeFiltering(estrategiaDistancia, k);
        else if (estrategia.equals("ContentBasedFiltering")) str = new ContentBasedFiltering(k);
    }

    public UsuariActiu getUsuari(){
        return usuari;
    }

    public TreeSet<Valoracio> getResultat(){
        return resultat;
    }

    public String getEstrategia(){
        return str.getNom();
    }

 

    public  Vector<String> getRecomanacio(UsuariActiu ua, Map<String,Item> items, int n) throws ExcepcionsRecomanador {
        Vector<String> retorn = new Vector<String>(); //retornarem un vector de strings que seran els id dels items, estaran en ordre descendent
        usuari = ua;
        resultat= str.Filtering(ua, items); //ens arriba un TreeSet de valoracions, ordenades descendentment. Recordem que una valoració te un item, un usuari i una puntuació. En acest cas usuari = ua per totes les valoracions del treeset.
        int cont = 0;
        Iterator<Valoracio> it = resultat.iterator();
        while (it.hasNext() && cont < n) { //agafem del resultat dels algoritmes els n items millors, ordenadament.
            ++cont;
            retorn.add(it.next().getIdItem());
        }
        return  retorn;
    }

    //pre: els items del vector han d'estar ordenats per valoracio de forma decreixent
    //pre: l'UsuariActiu u apareix a UsuarisUnknown amb les puntuacions de test entre les seves valoracions
    public double valoraRecomanacio(Vector<String> itemsRecomanats, UsuariActiu u, HashMap<String, UsuariActiu> UsuarisUnknown) {
        
        String uID = u.getUsuari();
        Map<String, Valoracio> LT = UsuarisUnknown.get(uID).getValoracions(); //Conté les valoracions de test
       
        //en les següents entregues també valorarem l' IDCG per tal de poder mostrar un valor entre 0 i 1
        double dcg = 0;
        double reli;
        double numerador;
        double denominador;
        double log2 = Math.log(2);
  
        for (int i = 0; i < itemsRecomanats.size(); ++i) { //per a cada item recomanat per l'algorisme
  
          String itemID = itemsRecomanats.get(i);
  
          if (LT.containsKey(itemID)) reli = LT.get(itemID).getPuntuacio();
          else reli = 0;
  
          numerador = Math.pow(2.0, reli) - 1;
          denominador = Math.log(i + 2) / log2; //divisio per passar a base 2
  
          dcg += numerador/denominador;
        }
        return dcg;
      }
      

}

//Classe programada per Marina Alapont, Daniel Pulido i Simon Oliva
