package Excepcions;

public class IdUsuariRepetit extends ExcepcionsRecomanador {
    public String getTipus() {
        return "IdUsuariRepetit";
    }

    public IdUsuariRepetit(String id) {
        super("L'identificador "+ id+ " ja esta atorgat. Canvia el fitxer per tal de donar un altre id que no estigui en el sistema.");
    }
}
