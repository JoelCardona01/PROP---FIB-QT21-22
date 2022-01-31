package Domini.Model;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import Excepcions.AtributsDiferents;

public class AtributNumeric extends Atribut {

    //Attributes
    private HashSet<Double> Valors;

    //Builder
    //Pre:
    //Post: Constructora d'atribut
    public AtributNumeric(Double d) {
        Inicialitza(d);
    }
    
    //Pre:
    //Post: Constructora d'atribut
    public AtributNumeric(Item i, Columna c, HashSet<Double> hS) {
        super(i,c);
        Valors = hS;
    }

    //Pre:
    //Post: Constructora d'atribut
    public AtributNumeric(Item i, Columna c, Double d) {
        super(i,c);
        Inicialitza(d);
    }

    //Pre:
    //Post: Retorna el tipus d'atribut (En aquest cas "AtributNumeric")
    public String getTipus() {
        return "AtributNumeric";
    }

    //Pre:
    //Post: S'inicialitzen les categories de l'atribut
    private void Inicialitza(Double d) {
        Valors = new HashSet<Double>();
        Valors.add(d);
    }

    //Pre:
    //Post: Es retorna un nombre proporcional a la distancia entre dos nombres
    private double comparaValors(double v1, double v2, double max, double min) {
        return 1-((Math.abs(v1-v2)/Math.abs((max-min))));
    }

    //Getters

    //Pre:
    //Post: Retorna el valor de l'atribut
    public double getValor() {
        //if (Valor.size() != 1) //Activa excepció (o no  xd) "Hi ha més d'un element"
        Iterator<Double> itr = Valors.iterator();
        return itr.next();
    }

    //Pre:
    //Post: Retorna el valor de l'atribut
    public String getValue() {
        Double aux = getValor();
        return aux.toString();
    }

    //Pre:
    //Post: Retorna el conjunt de valors de l'atribut en un vector de string
    public Vector<String> getValorsString(){
        if (Valors.size()==0);
        Vector<String> ret = new Vector<String>();
        for(Double i : Valors){
            ret.add(i.toString());
        }
        return ret;
    }

    //Pre:
    //Post: Es canvien els valors de les categories per els introduits per parametres
    public void canviaValors(Vector<String> vals){
        Valors = new HashSet<Double>();
        for (String v : vals) Valors.add(Double.valueOf(v));

    }

    //Pre: v esta en format Double
    //Post: S'afegeix el valor introduir per parametre al hashset de categorie
    public void afegirValor(double v) {
        Valors.add(v);
    }

    //Pre:
    //Post: S'elimina l'element identificat amb s de l'atribut categories
    public void eliminaValor (String v) {
        Double aux=Double.parseDouble(v);
        if (Valors.contains(aux)) Valors.remove(aux);
    }
    
    //Pre: s esta en format String
    //Post: S'afegeix el valor introduir per parametre al hashset de categorie
    public void afegirValor(String s) {
        Double aux =Double.parseDouble(s);
        afegirValor(aux);
    }

    public double ComparaAtributs(Atribut a) throws AtributsDiferents{
        if (AtributsIguals(a)) {
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
        else return super.ComparaAtributs(a);
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