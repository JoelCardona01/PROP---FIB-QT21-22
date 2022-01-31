package Excepcions;

public class ItemNoExisteix extends ExcepcionsRecomanador{

    public String getTipus() {
        return "ItemNoExisteix";
    }

    public ItemNoExisteix() {
        super("L'item no existeix");
    }

    public ItemNoExisteix(String s) {
        super("L'item amb id "+ s + " no existeix");
    }
    
}
