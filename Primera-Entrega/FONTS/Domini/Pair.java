package Domini;
public class Pair <T1,T2>{
    public T1 first;
    public T2 second;
    
    public Pair() {}
    
    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
    public T1 getFirst() {
        return first;
    }
    public T2 getSecond() {
        return second;
    }
}

//Classe feta per Joel Cardona i Daniel Pulido