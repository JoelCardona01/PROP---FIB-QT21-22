package Excepcions;

public class PersonaActualNoValida extends ExcepcionsRecomanador{

    public String getTipus() {
        return "PersonaActualNoValida";
    }

    public PersonaActualNoValida(String tipusIncorrecte, String tipusCorrecte) {
        super("La persona actual que ha iniciat sessio es de tipus "+ tipusIncorrecte+ " i per dur a terme aquesta funcionalitat hauria de ser de tipus "+tipusCorrecte);
    }
    
    
}
