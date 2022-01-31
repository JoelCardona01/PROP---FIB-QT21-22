package Excepcions;

public class ContrasenyaIncorrecte extends ExcepcionsRecomanador{

    public String getTipus() {
        return "ContrasenyaIncorrecte";
    }

    public ContrasenyaIncorrecte(String username, String contrasenya) {
        super("La contrasenya introduida no es correcta per al usuari " + username);
    }


    
}

