package Domini.Model;
public class Pair <T1,T2>{
    public T1 first;
    public T2 second;
    
    public Pair() {}
    
    //Pre:
    //Post: Creadora de pair
    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
    
    //Pre:
    //Post: Es retorna el primer element de la parella
    public T1 getFirst() {
        return first;
    }

    //Pre:
    //Post: Es retorna el segon element de la parella
    public T2 getSecond() {
        return second;
    }
}

//Classe feta per Joel Cardona i Daniel Pulido