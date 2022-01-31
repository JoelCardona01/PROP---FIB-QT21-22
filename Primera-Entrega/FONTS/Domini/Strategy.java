package Domini;
import java.util.*;
import Excepcions.*;

public interface Strategy {
    //ua::UsuariActiu es l'usuari al qual se li fara la recomanacio de diversos items
    //items::Map<String, Item> es el conjunt de tots els items presents al sistema
    //k::int es un parametre necessari pels algorismes de filtrat
    //post: Retorna un Set de Valoració on s'indica les valoracions que se li recomanen a l'usuari
    //"ua" predim que faria, en ordre descendent per valoracions  
    public  TreeSet<Valoracio> Filtering (UsuariActiu ua, Map<String,Item> items) throws ExcepcionsRecomanador; // si hacemos algun cambio acordaos de retocar el diagrama de clases

    public String getNom();

}

//Classe feta per Daniel Pulido