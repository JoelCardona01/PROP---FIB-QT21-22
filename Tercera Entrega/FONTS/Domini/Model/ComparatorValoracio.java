package Domini.Model;

import java.util.Comparator;

public class ComparatorValoracio implements Comparator<Valoracio> {
    
    //Pre: Cap dels elements es buit
    //Post: Retorna 1 si la puntuacio o1 es menor a o2 o si l'idItem o1 es menor al idItem o2.
    //Retorna -1 si la puntuacio o1 es major a o2 o si l'idItem o1 es major al idItem o2.
    //Retorna 0 si la puntuacio o1 es igual a o2 i si l'idItem o1 es igual al idItem o2.
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
                int idUs1 = o1.getUsuari().getUserId();
                int idUs2 = o2.getUsuari().getUserId();
                int compIdUs = 1;
                if(idUs1 < (idUs2)) compIdUs = -1;
                if (compIdUs == -1) return 1;
                else if (compIdUs == 1) return -1;
            }
        }
        return 0;

    }
}
    //Classe feta per Joel Cardona i Daniel Pulido