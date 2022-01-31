package Excepcions;

public class RangNoValid extends ExcepcionsRecomanador{

    public String getTipus() {
        return "RangNoValid";
    }

    public RangNoValid(Double min, Double max ) {
        super("El rang donat no es valid. Comprova que el minim sigui menor al maxim i que aquests valors incloguin al minim i maxim trobats pel progrma: "+ min+ "-" + max);
    }
    
    
}

