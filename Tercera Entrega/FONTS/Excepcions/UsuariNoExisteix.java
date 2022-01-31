package Excepcions;

public class UsuariNoExisteix extends ExcepcionsRecomanador{

    public String getTipus() {
        return "UsuariNoExisteix";
    }

    public UsuariNoExisteix(int idUs) {
        super("L'usuari amb identificat amb "+ idUs+ " no existeix");
    }

    public UsuariNoExisteix(String username) {
        super("L'usuari amb identificat amb el nom d'usuari "+ username+ " no existeix");
    }
    
    
}
