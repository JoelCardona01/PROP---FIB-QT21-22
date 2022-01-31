package Excepcions;

@SuppressWarnings("serial") //La classe Exception té un atribut que aquesta classe no te sentit que l'usi 

//CAMBIAR ENTRE 0 i 5 i posar que sigui la maxima valoracio i la mínima (entenc que de ratings)
public class PuntuacioNoValida extends ExcepcionsRecomanador {

    public String getTipus() {
        return "PuntuacioNoValida";
    }

    public PuntuacioNoValida() {
        super();
    }
    public PuntuacioNoValida(double max, double min) {
        super("La puntuacio ha de ser un real entre " + max + " i " +min +" ambdos inclosos");
    }

}
