package Domini.Model;

import java.util.HashMap;

public class DistanciaMitjana implements StrategyDistancia {
    private String nom = "DistanciaMitjana";
    
    public String getNom(){
        return nom;
    }

    //Pre: coordenades1 i coordenades2 son les coordenades en el sistema de punts agafat per dos usuaris.
    //Pre: coordenades1.size()!=0 && coordenades2.size()!=0
    //Post: es retorna la distancia euclidiana entre els dos usuari tinguent en compte les seves valoracions comunes.
    //Funcio feta per Marina Alapont
    public Double distancia(HashMap<String,Double> coordenades1, HashMap<String,Double> coordenades2) {
        double suma=0.0;
        double count=0;
        if (coordenades1.size()==0 || coordenades2.size()==0) return null;
            for (HashMap.Entry<String, Double> c : coordenades1.entrySet()){
                if (coordenades2.containsKey(c.getKey())){
                    ++count;
                    Double val1=c.getValue();
                    Double val2=coordenades2.get(c.getKey());
                    double dist = Math.abs(val1-val2);
                    suma+=dist; 
                }
            
            }

         double distancia= (suma/count);   //calcula la distancia mitjana dels usuari

       return distancia;
    } 
}
//Classe feta per Marina Alapont i Joel Cardona