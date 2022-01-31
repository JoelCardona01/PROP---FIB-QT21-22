package Domini;
import Excepcions.PuntuacioNoValida;

public class Valoracio {

    private double puntuacio; 
    private UsuariActiu ua;
    private Item item;
    private boolean esPredictiva;
    private static Double minPuntuacio;
    private static Double maxPuntuacio;


    private void comprovaPuntuacio(double punt)  throws PuntuacioNoValida{
        if (minPuntuacio != null && maxPuntuacio != null) {
            if (punt <minPuntuacio || punt >maxPuntuacio) throw new PuntuacioNoValida();
        }
    }

    public Valoracio (Double val, Item item, UsuariActiu UsrActiu) throws PuntuacioNoValida{
        comprovaPuntuacio(val);
        esPredictiva = false;
        puntuacio = val;
        this.item = item;
        ua = UsrActiu;
    }

    public Valoracio(Double val) throws PuntuacioNoValida{
        comprovaPuntuacio(val);
        esPredictiva = false;
        puntuacio = val;
    }

    public void AssignaPredictiva(boolean b) {
        esPredictiva = b;
    }

    public static void setMinPunt(double d) {
        minPuntuacio = d;
    }

    public static void setMaxPunt(double d) {
        maxPuntuacio = d;
    }

    public static double getMinPunt() {
        return minPuntuacio;
    }

    public static double getMaxPunt() {
        return maxPuntuacio;
    }

    public Double getPuntuacio() {
        return puntuacio;
    }

    public String getIdItem(){
        return item.getId();
    }

    public UsuariActiu getUsuariActiu(){
        return ua;
    }

    public Item getItem(){
        return item;
    }

    public boolean esPredictiva() {
        return esPredictiva;
    }

    public void modificaPuntuacio (Double newpuntuacio) throws PuntuacioNoValida{
        comprovaPuntuacio(newpuntuacio);
        puntuacio = newpuntuacio;
    }
}


//Classe programada per: Marina Alapont, Joel Cardona, Daniel Pulido