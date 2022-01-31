package Domini;
import java.util.HashSet;
import Excepcions.AtributsDiferents;
public abstract class Atribut {
    
    //Class attributes
    private Item item;
    private Columna col;

    public Atribut() {}

    public Atribut(Item item, Columna col) {
        this.item = item;
        this.col = col;
    }
    
    protected void ComprobaExcepcioAtributsDiferents(Atribut a) throws AtributsDiferents {
        if (!getTipus().equals(a.getTipus())) throw new AtributsDiferents(getTipus(), a.getTipus());
    }
    //Getters
    public abstract String getValue();

    public boolean esClau() {
        return  col.getEsClau();
    }

    public Item getItem() {
        return item;
    }

    public abstract HashSet<String> getSet();

    public abstract String getTipus();
    //Setters
    public abstract void afegirValor(String s);

    //Operacions
    public double ComparaAtributs(Atribut a) throws AtributsDiferents {
        ComprobaExcepcioAtributsDiferents(a);
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

    public double getMinCol() {
        return col.getMinCol();
    }

    public double getMaxCol() {
        return col.getMaxCol();
    }

}

//Classe feta per Daniel Pulido