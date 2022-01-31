package Excepcions;

@SuppressWarnings("serial") //La classe RunTimeException té un atribut que aquesta classe no te sentit que l'usi 

public abstract class ExcepcionsRecomanador extends Exception{

    public abstract String getTipus();

    public ExcepcionsRecomanador() {
        super();
    }

    public ExcepcionsRecomanador(String s) {
        super(s);
    }
}