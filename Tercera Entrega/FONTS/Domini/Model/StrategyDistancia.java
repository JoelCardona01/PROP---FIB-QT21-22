package Domini.Model;

import java.util.HashMap;

public interface StrategyDistancia {
    //Retorna el nom de la estrategia que s'usa
    public String getNom();

    //Pre: coordenades 1 i coordenades 2 son el conjunt de valoracions de dos usuaris diferents.
    //Post: Retorna la distancia que hi ha entre els dos vectors de coordenades segons l'estrategia utilitzada
    public Double distancia(HashMap<String,Double> coordenades1, HashMap<String,Double>  coordenades2);

}
//Classe implementada per Joel Cardona