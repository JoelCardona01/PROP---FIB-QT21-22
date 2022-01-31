package Excepcions; 

public class ItemNoValorat extends ExcepcionsRecomanador{

    public String getTipus() {
        return "ItemNoValorat";
    }

    public ItemNoValorat() {
        super("L'item no ha estat valorat per l'usuari");
    }

    public ItemNoValorat(String s, int u) {
        super("L'item amb id "+ s + " no ha estat valorat per l'usuari amb id "+ u );
    }
    
}

