package Excepcions;

public class ItemNoEsDeUnknown extends ExcepcionsRecomanador{

    public String getTipus() {
        return "ItemJaValorat";
    }

    public ItemNoEsDeUnknown() {
        super("L'item no consta en el fitxer de test per al usuari donat");
    }

    public ItemNoEsDeUnknown(String s, int u) {
        super("L'item amb id "+ s + " no consta al fitxer de test 'unknown' per al usuari " + u+". No hi ha cap valoracio amagada del usuari donat amb aquest item." );
    }
    
}

