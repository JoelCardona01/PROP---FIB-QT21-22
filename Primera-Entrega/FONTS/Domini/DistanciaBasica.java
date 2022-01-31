package Domini;

import java.util.Vector;

public class DistanciaBasica implements StrategyDistancia {
    private String nom = "Distancia Basica";
    
    public String getNom(){
        return nom;
    }

    //Pre: coordenades1 i coordenades2 son les coordenades en el sistema de punts agafat per dos usuaris.
    //Pre: coordenades1.size() == coordenades2.size() == nombre d'items del sistema
    //Funcio feta per Marina Alapont
    public Double distancia(Vector<Double> coordenades1, Vector<Double> coordenades2) {
        double suma=0;
        double count=0;
        if (coordenades1.size()==0 || coordenades2.size()==0) return null; //una persona sense valoracions no esta en el sistema de dimensions i per tant no es pot calcular la distancia a un altre.
        else if (coordenades1.size()!=coordenades2.size()) return null; //per la pre no hauria de passar mai.
        for (int i=0; i<coordenades1.size(); ++i) { //Kmeans asegura sempre que coordenades1.size==coordenades2.size i que aixo equival a el nombre d'items de la base de dades
             Double val1=coordenades1.get(i);
             Double val2=coordenades2.get(i);
             double dist = Math.abs(val1-val2);
             suma+=dist; //suma acumula totes les diferencies de valoracions
             ++count;
          }
         double distancia= (suma/count);   //calcula la distancia mitjana dels usuari

       return distancia;
    } 
}
//Classe feta per Marina Alapont i Joel Cardona