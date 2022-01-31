package Domini;
import java.util.*;

public class ComparatorPair implements Comparator<Pair<Double,Item>> {

    public int compare(Pair<Double, Item> p1, Pair<Double, Item> p2) {
        if (p1.getFirst() < p2.getFirst()) return 1;
    else if (p1.getFirst() > p2.getFirst()) return -1;
        return 0;
    }
}

//Classe feta per Daniel Pulido