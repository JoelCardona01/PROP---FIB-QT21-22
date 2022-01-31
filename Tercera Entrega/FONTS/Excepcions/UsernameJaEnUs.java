package Excepcions;

public class UsernameJaEnUs extends ExcepcionsRecomanador{

    public String getTipus() {
        return "UsernameJaEnUs";
    }

    public UsernameJaEnUs(String username) {
        super("El nom d'usuari "+ username+ " ja esta en us. Escull un altre");
    }

}
