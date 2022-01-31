package Domini.Model;
import java.util.HashSet;
import java.util.Vector;

import Excepcions.AtributsDiferents;
public abstract class Atribut {
    
    //Class attributes
    private Item item;
    private Columna col;

    public Atribut() {}

    //Pre: La columa i l'item existeixen
    //Post: Creadora d'Atribut
    public Atribut(Item item, Columna col) {
        this.item = item;
        this.col = col;
    }
    
    //Pre:
    //Post: Es comprova l'excepcio. En cas que la condicio del if sigui certa aleshores s'activa l'excepcio
    protected boolean AtributsIguals(Atribut a) {
        return (getTipus().equals(a.getTipus()));
    }
    
    //Metode asbtracte
    public abstract String getValue();

    //Metode asbtracte
    public abstract Vector<String> getValorsString();

    //Pre: 
    //Post: Es retorna la columna de la que forma part l'atribut
    public Columna getColumna() {
        return col;
    }
    
    //Pre:
    //Post: Es retorna l'item del que forma part l'atribut
    public Item getItem() {
        return item;
    }

    //Metode abstracte
    public abstract HashSet<String> getSet();

    //Metode abstracte
    public abstract String getTipus();

    //Setters
    //Metode abstracte
    public abstract void afegirValor(String s);

    //Metode abstracte
    public abstract void canviaValors(Vector<String> vals);

    //Metode abstracte
    public abstract void eliminaValor(String s);

    //Operacions
    //Pre:
    //Post: Es comparen dos atributs i retorna un double segons la similitud entre els atributs
    public double ComparaAtributs(Atribut a) throws AtributsDiferents {
        HashSet<String> thisSet = getSet();
        HashSet<String> aSet = a.getSet();
        Double puntuacio = 0.;
        double penalitzacio = 3;
        double maxSim = 5;
        if (thisSet.size() < aSet.size()) {
            for (String s : thisSet) {
                if (aSet.contains(s)) puntuacio+=maxSim;
                else puntuacio=Math.max(0.,puntuacio-penalitzacio);
            }
        }
        else {
            for (String s : aSet) {
                if (thisSet.contains(s)) puntuacio+=maxSim;
                else puntuacio=Math.max(0.,puntuacio-penalitzacio);
            }
        }
        puntuacio = Math.min(maxSim, puntuacio/thisSet.size());
        return puntuacio * (Math.min(aSet.size(), thisSet.size())/Math.max(aSet.size(), thisSet.size()));
    }

    //Pre:
    //Post: Es retorna el valor minim de la columna del atribut
    public double getMinCol() {
        return col.getMinCol();
    }

    //Pre:
    //Post: Es retorna el valor minim de la columna del atribut
    public double getMaxCol() {
        return col.getMaxCol();
    }


}

//Classe feta per Daniel Pulido