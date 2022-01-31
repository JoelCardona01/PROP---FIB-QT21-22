package Domini.Model;
import java.util.*;
import Excepcions.*;

public interface Strategy {
  
    //Pre: L'usuari u existeix, el map d'items conte almenys un item
    //Post: Retorna un Set de Valoració on s'indica les valoracions que se li recomanen a l'usuari
    public  TreeSet<Valoracio> Filtering (Usuari u, Map<String,Item> items) throws ExcepcionsRecomanador; // si hacemos algun cambio acordaos de retocar el diagrama de clases

    //Pre:
    //Post:
    public String getNom();

}

//Classe feta per Daniel Pulido