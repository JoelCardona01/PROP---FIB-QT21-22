package Domini;
import java.util.HashSet;
import java.util.Iterator;
import Excepcions.AtributsDiferents;

public class AtributPropietat extends Atribut {

    //Public attributes
    private HashSet<Boolean> Propietats;
    //Builder

    public AtributPropietat(Item i, Columna c, boolean b) {
        super(i,c);
        Inicialitza(b);
    }

    public AtributPropietat(Item i, Columna c, HashSet<Boolean> hS) {
        super(i,c);
        Propietats = hS;
    }

    public AtributPropietat(boolean b) {
        Inicialitza(b);
    }

    //Operacions privades
    public String getTipus() {
        return "AtributPropietat";
    }

    private void Inicialitza(boolean b) {
        Propietats = new HashSet<Boolean>();
        Propietats.add(b);
    }

    //Getters
    
    public boolean getPropietat() {
        //if (Propietats.size() != 1) //Activa excepció (o no  xd) "Hi ha més d'un element"
        Iterator<Boolean> itr = Propietats.iterator();
        return itr.next();
    }

    public String getValue() {
        Boolean aux = getPropietat();
        return aux.toString();
    }

    public void afegirPropietat(boolean b) {
        Propietats.add(b);
    }

    public void afegirValor(String s) {
        Boolean aux = Boolean.parseBoolean(s);
        afegirPropietat(aux);
    }

    public HashSet<String> getSet() {
        HashSet<String> Set = new HashSet<String>();
        for (Boolean b : Propietats) {
            Set.add(Boolean.toString(b));
        }
        return Set;
    }
    //Operacions públiques
    public double ComparaAtributs(Atribut a) throws AtributsDiferents{
        super.ComprobaExcepcioAtributsDiferents(a);
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


}