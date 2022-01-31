package Domini;
import java.util.HashSet;
import java.util.Iterator;

import Excepcions.AtributsDiferents;

public class AtributNumeric extends Atribut {

    //Attributes
    private HashSet<Double> Valors;
    //Builder

    public AtributNumeric(Double d) {
        Inicialitza(d);
    }
    
    public AtributNumeric(Item i, Columna c, HashSet<Double> hS) {
        super(i,c);
        Valors = hS;
    }

    public AtributNumeric(Item i, Columna c, Double d) {
        super(i,c);
        Inicialitza(d);
    }

    //Operacions privades

    public String getTipus() {
        return "AtributNumeric";
    }

    private void Inicialitza(Double d) {
        Valors = new HashSet<Double>();
        Valors.add(d);
    }

    private double comparaValors(double v1, double v2, double max, double min) {
        return 1-((Math.abs(v1-v2)/Math.abs((max-min))));
    }

    //Getters

    public double getValor() {
        //if (Valor.size() != 1) //Activa excepció (o no  xd) "Hi ha més d'un element"
        Iterator<Double> itr = Valors.iterator();
        return itr.next();
    }

    public String getValue() {
        Double aux = getValor();
        return aux.toString();
    }

    public void afegirValor(double v) {
        Valors.add(v);
    }
    //Pre: s esta en format Double
    public void afegirValor(String s) {
        Double aux =Double.parseDouble(s);
        afegirValor(aux);
    }

    public double ComparaAtributs(Atribut a) throws AtributsDiferents{
        super.ComprobaExcepcioAtributsDiferents(a);
        double max = super.getMaxCol();
        double min = super.getMinCol();
        HashSet<String> SetS = a.getSet();
        int total = Valors.size() * SetS.size();
        double maxSim = 5;
        double res = 0;
        for (String s : SetS) {
            for (Double d : Valors) {
                res += maxSim*comparaValors(d, Double.parseDouble(s), max, min);
            }
        }
        return res/total;
    }

    public HashSet<String> getSet() {
        HashSet<String> Set = new HashSet<String>();
        for (double d : Valors) {
            Set.add(Double.toString(d));
        }
        return Set;
    }
}

//Classe feta per Daniel Pulido