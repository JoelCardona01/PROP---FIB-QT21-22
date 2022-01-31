package Excepcions;

public class ItemBuit extends ExcepcionsRecomanador {

    @Override
    public String getTipus() {
        return "ItemBuit";
    }

    public ItemBuit() {
        super("L'item que es vol afegir no te cap valor per cap columna");
    }
    
}
