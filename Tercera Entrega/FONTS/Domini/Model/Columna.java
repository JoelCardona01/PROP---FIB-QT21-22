package Domini.Model;
import java.util.*;
public class Columna {
    private int numCol;
    private String nomCol;
    private Double maxCol; //Indica, en cas que els atributs siguin AtributNumeric, el valor màxim de la columna
    private Double minCol; //Indica, en cas que els atributs siguin AtributNumeric, el valor mínim de la columna
    private Boolean esClau;
    private HashSet<Atribut> atributs;

    //Pre:
    //Creadora de columna
    public Columna() {
        esClau = false;
        atributs = new HashSet<Atribut>();
    }
    
    //Pre:
    //Creadora de columna
    public Columna(int numCol, String nomCol) {
        esClau = false;
        this.numCol = numCol;
        this.nomCol = nomCol;
        atributs = new HashSet<Atribut>();
    }

    //Pre:
    //Creadora de columna
    public Columna(int numCol, String nomCol, HashSet<Atribut> atributs) {
        esClau = false;
        this.numCol = numCol;
        this.nomCol = nomCol;
        if (atributs != null) this.atributs = atributs;
        else this.atributs = new HashSet<Atribut>();
    }

    //Pre:
    //Post: Retorna cert si la columna es clau, o fals si l columna no ho es
    public boolean getEsClau() {
        return esClau;
    }

    //Pre:
    //Post: Retorna el numero de columna
    public int getNumCol() {
        return numCol;
    }

    //Pre:
    //Post: Retorna el valor mes gran de tots els atributs de la columna
    public double getMaxCol() {
        return maxCol;
    }

    //Pre:
    //Post: Retorna el valor mes petit de tots els atributs de la columna
    public double getMinCol() {
        return minCol;
    }

    //Pre:
    //Post: Es retorna el nom de la columna
    public String getNomCol() {
        return nomCol;
    }

    //Pre:  d < minCol
    //Post: S'actualitza el valor minim de la columna
    public void setMinCol(double d) {
        minCol = d;
    }

    //Pre: d>maxCol
    //Post: S'actualitza el valor maxim de la columna
    public void setMaxCol(double d) {
        maxCol = d;
    }

    //Pre:
    //Post: S'actualitza el valor esClau amb el rebut per parametre
    public void setEsClau(boolean b) {
        esClau = b;
    }

    //Pre:
    //Post: S'afegeix al hashset l'atribut rebut per parametre
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