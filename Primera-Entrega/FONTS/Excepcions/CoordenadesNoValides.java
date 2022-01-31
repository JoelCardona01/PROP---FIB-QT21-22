package Excepcions;

public class CoordenadesNoValides extends ExcepcionsRecomanador{

    public String getTipus() {
        return "CoordenadesNoValides";
    }

    public CoordenadesNoValides() {
        super("Les coordenades introduide per a fer el calcul de les distancies entre dos usuaris son incorrectes. Asseguris de que tenen el mateix nombre de doubles i que no on buides." );
    }
    
}