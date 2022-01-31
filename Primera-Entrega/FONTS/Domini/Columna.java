package Domini;
import java.util.*;
public class Columna {
    private int numCol;
    private String nomCol;
    private Double maxCol; //Indica, en cas que els atributs siguin AtributNumeric, el valor màxim de la columna
    private Double minCol; //Indica, en cas que els atributs siguin AtributNumeric, el valor mínim de la columna
    private Boolean esClau;
    private HashSet<Atribut> atributs;

    public Columna() {
        esClau = false;
        atributs = new HashSet<Atribut>();
    }
    
    public Columna(int numCol, String nomCol) {
        esClau = false;
        this.numCol = numCol;
        this.nomCol = nomCol;
        atributs = new HashSet<Atribut>();
    }

    public Columna(int numCol, String nomCol, HashSet<Atribut> atributs) {
        esClau = false;
        this.numCol = numCol;
        this.nomCol = nomCol;
        if (atributs != null) this.atributs = atributs;
        else this.atributs = new HashSet<Atribut>();
    }

    public boolean getEsClau() {
        return esClau;
    }

    public int getNumCol() {
        return numCol;
    }

    public double getMaxCol() {
        return maxCol;
    }

    public double getMinCol() {
        return minCol;
    }

    public String getNomCol() {
        return nomCol;
    }

    public void setMinCol(double d) {
        minCol = d;
    }

    public void setMaxCol(double d) {
        maxCol = d;
    }

    public void setEsClau(boolean b) {
        esClau = b;
    }

    public void afegirAtribut(Atribut a) {
        atributs.add(a);
        if (a.getTipus().equals("AtributNumeric")) {
            double valor = Double.parseDouble(a.getValue());
            if (maxCol == null) maxCol = valor;
            else maxCol = Math.max(valor,maxCol);
            if (minCol == null) minCol = valor;
            else minCol = Math.min(valor,minCol);
        }
    }


}