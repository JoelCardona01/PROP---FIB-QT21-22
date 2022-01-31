package Excepcions;

public class UsuariNoExisteix extends ExcepcionsRecomanador{

    public String getTipus() {
        return "UsuariNoExisteix";
    }

    public UsuariNoExisteix(String idUs) {
        super("L'usuari amb identificat amb "+ idUs+ "no existeix");
    }
    
    
}
