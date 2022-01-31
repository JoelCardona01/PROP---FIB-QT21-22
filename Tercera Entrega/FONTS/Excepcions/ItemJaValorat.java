package Excepcions; 

public class ItemJaValorat extends ExcepcionsRecomanador{

    public String getTipus() {
        return "ItemJaValorat";
    }

    public ItemJaValorat() {
        super("L'item ja ha estat valorat");
    }

    public ItemJaValorat(String s, int u) {
        super("L'item amb id "+ s + " ja ha estat valorat per l'usuari amb usari "+ u );
    }
    
}

