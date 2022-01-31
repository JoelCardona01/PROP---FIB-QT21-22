package Domini;

import java.util.Comparator;

public class ComparatorValoracio implements Comparator<Valoracio> {
    
    public int compare(Valoracio o1, Valoracio o2) {

        if (o1.getPuntuacio() < o2.getPuntuacio()) return 1;
        else if (o1.getPuntuacio() > o2.getPuntuacio()) return -1;
        else {
            String idIt1 = o1.getIdItem();
            String idIt2 = o2.getIdItem();
            //id1 < id2. si id1 > id2 retorna 1, si id1 < id2, -1, i si son iguals 0
            int compIdIt = idIt1.compareTo(idIt2);
            if (compIdIt == -1) return 1;
            else if (compIdIt == 1) return -1;
            else {
                String idUs1 = o1.getUsuariActiu().getUsuari();
                String idUs2 = o2.getUsuariActiu().getUsuari();
                int compIdUs = idUs1.compareTo(idUs2);
                if (compIdUs == -1) return 1;
                else if (compIdUs == 1) return -1;
            }
        }
        return 0;

    }
}
    //Classe feta per Joel Cardona i Daniel Pulido