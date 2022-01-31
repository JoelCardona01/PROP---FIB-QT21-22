package Excepcions;

@SuppressWarnings("serial") //La classe RunTimeException té un atribut que aquesta classe no te sentit que l'usi 

public class AtributsDiferents extends ExcepcionsRecomanador{

    public String getTipus() {
        return "AtributsDiferents";
    }

    public AtributsDiferents() {
        super("Per comparar atributs aquests han de ser del mateix tipus.\nPer exemple, si tens un AtributNumeric nomes el pots comparar amb un altre Atribut que sigui AtributNumeric");
    }

    public AtributsDiferents(String tipus1, String tipus2) {
        super("Estas comparant atributs de tipus diferent, haurien de ser del mateix tipus.\nConcretament estas comparant " + tipus1 + " amb " + tipus2);
    }
}
