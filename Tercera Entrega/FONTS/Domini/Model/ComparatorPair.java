package Domini.Model;
import java.util.*;

public class ComparatorPair implements Comparator<Pair<Double,Item>> {

    //Pre: Cap dels elements es buit
    //Post: retorna si 1 si el primer element de p1 es menor al primer element de p2, -1 en cas que
    //el primer element de p2 sigui major al primer element de p1 o 0 en cas de ser iguals.
    public int compare(Pair<Double, Item> p1, Pair<Double, Item> p2) {
        if (p1.getFirst() < p2.getFirst()) return 1;
        else if (p1.getFirst() > p2.getFirst()) return -1;
        return 0;
    }
}

//Classe feta per Daniel Pulido