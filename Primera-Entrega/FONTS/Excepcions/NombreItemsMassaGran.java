package Excepcions;

public class NombreItemsMassaGran extends ExcepcionsRecomanador {

    public String getTipus() {
        return "NombreItemsMassaGran";
    }

    public NombreItemsMassaGran() {
        super();
    }

    public NombreItemsMassaGran(Integer nItems, Integer nItemsMax) {
        super("El nombre d'items introduit es: "+nItems+ " i el maxim: " + nItemsMax + " introdueixi un nombre inferior a aquest");
    }

    
    public NombreItemsMassaGran(String u, Integer nItems, Integer nItemsMax) {
        super("El nombre d'items introduit per al usuari "+ u+" es: "+nItems+ " i el maxim: " + nItemsMax + " introdueixi un nombre inferior a aquest");
    }
    
}
