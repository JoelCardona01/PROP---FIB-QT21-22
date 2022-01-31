package Domini.Model;

import java.util.HashMap;

public class DistanciaPonderada implements StrategyDistancia{
 
    private String nom = "DistanciaPonderada";
    
    public String getNom(){
        return nom;
    }

    //Pre: Per parametre ens entren, per cada usuari, les valoracions que si ha fet, on String es el id del item i double la puntuacio que li dona.
    //Pre: Si un dels dos representa un un centroide, ha de ser coordenades 2. 
    //Post: es retorna la distancia euclidiana entre els dos usuari tinguent en compte les seves valoracions. Es pondera 
    //      segons el nombre de valoracions no comunes.
    public Double distancia(HashMap<String,Double> coordenades1, HashMap<String,Double> coordenades2) {
        int comuns = 0;
        double suma=0.0;
  
            for (HashMap.Entry<String, Double> c : coordenades1.entrySet()){
                if (!coordenades2.containsKey(c.getKey()));
                else {
                    ++comuns;
                    Double val1=c.getValue();
                    Double val2=coordenades2.get(c.getKey());
                    double dist = (val1-val2)*(val1-val2);
                    suma+=dist; 
                }
            }
        int nocomuns = coordenades2.size()-comuns + coordenades1.size()-comuns;

        return Math.sqrt(suma)*((Math.log10(nocomuns)/(Math.log10(2))));
    } 
}
//Classe feta per Marina Alapont


