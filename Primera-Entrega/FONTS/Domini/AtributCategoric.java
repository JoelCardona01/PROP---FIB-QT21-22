package Domini;
import java.util.HashSet;
import java.util.Iterator;


public class AtributCategoric extends Atribut {

    //Public attributes
    private HashSet<String> Categories;
    //Builder
    public AtributCategoric(Item i, Columna c, String s) {
        super(i, c);
        Inicialitza(s);
    }

    public AtributCategoric(String s) {
        Inicialitza(s);
    }

    public AtributCategoric(Item i, Columna c, HashSet<String> hS) {
        super(i,c);
        Categories = hS;
    }

    //Operacions privades

    public String getTipus() {
        return "AtributCategoric";
    }

    private void Inicialitza(String s) {
        Categories = new HashSet<String>();
        Categories.add(s);
    }

    //Getters
    public String getCategoria() {
        //if (Categories.size() != 1) //Activa excepció (o no) "Hi ha més d'un element"
        Iterator<String> itr = Categories.iterator();
        return itr.next().toString();
    }

    public String getValue() {
        return getCategoria();
    }

    public void afegirCategoria(String s) {
        Categories.add(s);
    }

    public void afegirValor(String s) {
        afegirCategoria(s);
    }

    public HashSet<String> getSet() {
        return Categories;
    }
    
//Classe feta per Daniel Pulido
}