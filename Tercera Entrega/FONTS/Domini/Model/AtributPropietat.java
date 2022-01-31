package Domini.Model;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import Excepcions.AtributsDiferents;

public class AtributPropietat extends Atribut {

    //Public attributes
    private HashSet<Boolean> Propietats;

    //Builder
    //Pre:
    //Post: Constructora d'atribut
    public AtributPropietat(Item i, Columna c, boolean b) {
        super(i,c);
        Inicialitza(b);
    }

    //Pre:
    //Post: Constructora d'atribut
    public AtributPropietat(Item i, Columna c, HashSet<Boolean> hS) {
        super(i,c);
        Propietats = hS;
    }

    //Pre:
    //Post: Constructora d'atribut
    public AtributPropietat(boolean b) {
        Inicialitza(b);
    }

    //Operacions privades
    //Pre:
    //Post: Retorna el tipus d'atribut (En aquest cas "AtributPropietat")
    public String getTipus() {
        return "AtributPropietat";
    }


    //Pre:
    //Post: S'inicialitzen les categories de l'atribut
    private void Inicialitza(boolean b) {
        Propietats = new HashSet<Boolean>();
        Propietats.add(b);
    }

    //Getters
    
    //Pre:
    //Post: Es retornen les propietats del hashset de pripietats
    public boolean getPropietat() {
        Iterator<Boolean> itr = Propietats.iterator();
       // if(Propietats.size()==0) return false;
        return itr.next();
    }

    //Pre:
    //Post: Retorna el valor de propietat en format string
    public String getValue() {
        Boolean aux = getPropietat();
        return aux.toString();
    }

    //Pre:
    //Post: Retorna el conjunt propietats de la columna.
    public Vector<String> getValorsString(){
        if (Propietats.size()==0) return null;
        Vector<String> ret = new Vector<String>();
        for(Boolean i : Propietats){
            ret.add(i.toString());
        }
        return ret;
    }

    //Pre:
    //Post: S'afegeix la propietat introduir per parametre al hashset de categorie
    public void afegirPropietat(boolean b) {
        Propietats.add(b);
    }

    //Pre:
    //Post: S'afegeix el valor introduir per parametre al hashset de categorie
    public void afegirValor(String s) {
        Boolean aux = Boolean.parseBoolean(s);
        afegirPropietat(aux);
    }

    //Pre:
    //Post: S'elimina l'element identificat amb s de l'atribut categories
    public void eliminaValor (String s){
        Propietats = new HashSet<Boolean>();
    }

    //Pre:
    //Post: Es canvien els valors de les categories per els introduits per parametres
    public void canviaValors(Vector<String> vals){
        Propietats = new HashSet<Boolean>();
        for (String v : vals) {
            if (v.equals("true") || v.equals("True")) Propietats.add(true);
            else Propietats.add(false);
        } 
    }

    //Pre:
    //Post: Es retorna el conjunt de propietats de l'atribut.
    public HashSet<String> getSet() {
        HashSet<String> Set = new HashSet<String>();
        for (Boolean b : Propietats) {
            Set.add(Boolean.toString(b));
        }
        return Set;
    }

    //Pre: L'atribut a existeix
    //Post: Una vegada compara els atributs retorna un double.
    public double ComparaAtributs(Atribut a) throws AtributsDiferents{
        if (super.AtributsIguals(a)) {
            HashSet<String> setS = a.getSet();
            int totalTruesSetS = 0;
            int totalTruesSelf = 0;
            int total = setS.size()+Propietats.size();
            double maxSim = 5.;
            for (String s : setS) {
                if (s.equals("true")) totalTruesSetS++;
            }
            for (Boolean b : Propietats) {
                if (b) totalTruesSelf++;
            }
            if (total == 2) {
                if (totalTruesSelf == totalTruesSetS) return maxSim;
                else return -(maxSim/2);
            } 
            return maxSim*(1-(totalTruesSelf-totalTruesSetS)/total);
        }
        else return super.ComparaAtributs(a);
    }


}