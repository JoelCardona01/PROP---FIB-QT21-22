package Domini.Model;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;


public class AtributCategoric extends Atribut {

    //Public attributes
    private HashSet<String> Categories;
    
    //Builder
    //Pre:
    //Post: Constructora d'atribut
    public AtributCategoric(Item i, Columna c, String s) {
        super(i, c);
        Inicialitza(s);
    }

    //Pre:
    //Post: Constructora d'atribut
    public AtributCategoric(String s) {
        Inicialitza(s);
    }

    //Pre:
    //Post: Constructora d'atribut
    public AtributCategoric(Item i, Columna c, HashSet<String> hS) {
        super(i,c);
        Categories = hS;
    }

    //Pre:
    //Post: Retorna el tipus d'atribut (En aquest cas "AtributCategoric")
    public String getTipus() {
        return "AtributCategoric";
    }

    //Pre:
    //Post: Retorna els valors de les categories en un vector<String> 
    public Vector<String> getValorsString(){
        if (Categories.size()==0) return null;
        Vector<String> ret = new Vector<String>();
        for(String i : Categories){
            ret.add(i);
        }
        return ret;
    }

    //Pre:
    //Post: S'inicialitzen les categories de l'atribut
    private void Inicialitza(String s) {
        Categories = new HashSet<String>();
        Categories.add(s);
    }

    //Getters i setters
    //Pre:
    //Post: Es retorna una de les categories del hashset de categories
    public String getCategoria() {
        //if (Categories.size() != 1) //Activa excepció (o no) "Hi ha més d'un element"
        Iterator<String> itr = Categories.iterator();
        return itr.next().toString();
    }

    //Pre:
    //Post: Retorna el valor de la categoria
    public String getValue() {
        return getCategoria();
    }

    //Pre:
    //Post: S'afegeix el valor introduir per parametre al hashset de categorie
    public void afegirValor(String s) {
        Categories.add(s);
    }

    //Pre:
    //Post: S'elimina l'element identificat amb s de l'atribut categories
    public void eliminaValor (String s){
        if(Categories.contains(s)) Categories.remove(s);
    }

    //Pre:
    //Post: Es canvien els valors de les categories per els introduits per parametres
    public void canviaValors(Vector<String> vals){
        Categories = new HashSet<String>();
        for (String v : vals) Categories.add(v);
    }

    //Pre:
    //Post: Es retorna el conjunt de categories de l'atribut.
    public HashSet<String> getSet() {
        return Categories;
    }
    
//Classe feta per Daniel Pulido
}