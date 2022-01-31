package Excepcions;

public class KMassaGran extends ExcepcionsRecomanador {

    public String getTipus() {
        return "KMassaGran";
    }

    public KMassaGran() {
        super();
    }

    public KMassaGran(int k, int max) {
        super("K es massa gran. El parametre k introduit es: "+k+ " i el maxim que es pot introduir: " + max + ". Introdueixi un nombre inferior a aquest");
    }
    
}

