package Domini.Model;

import java.util.HashMap;
import java.util.Map;
import Excepcions.ExcepcionsRecomanador;
import Excepcions.ItemNoValorat;

public abstract class Usuari extends Persona {

        private Map <String,Valoracio> ValoracionsUsuari = new HashMap<String,Valoracio>();  //string es el id del item.
    
        //Pre: No existeix cap persona identificada per userId
        //Post: Es crea l'usuari identificat amb userId
        public Usuari(int userId){ 
            super(userId);
        }

        //Pre: 
        //Post: Es retorna el map de valoracions de l'usuari
        public Map<String, Valoracio> getValoracions(){
            return ValoracionsUsuari; 
        }
    
        //Pre:
        //Post: S'assignen les valoracions m al usuari 
        public void setValoracions(Map <String,Valoracio> m){
            ValoracionsUsuari=m;
        }
    
        //Pre:
        //Post: S'afegeix la valoracio v al conjunt de valoracionsUsuari
        public void afegirValoracio(Valoracio v){
            ValoracionsUsuari.put(v.getIdItem(), v);
        }
        
        //Pre: Existeix un item identificat per idItem
        //Post: Es modifica la valoracio feta per l'idItem amb la puntuacioNova
        public void canviarPuntuacio(String idItem, Double puntuacioNova) throws ExcepcionsRecomanador {
            if (!ValoracionsUsuari.containsKey(idItem)) throw new ItemNoValorat(idItem, super.getUserId());
            else  ValoracionsUsuari.get(idItem).modificaPuntuacio(puntuacioNova);
        }

        //Pre: Existeix un item identificat per idItem
        //Post: S'elimina la valoracio feta per l'idItem
        public void eliminarValoracio(String idItem) throws ItemNoValorat {
            if (!ValoracionsUsuari.containsKey(idItem))throw new ItemNoValorat(idItem, super.getUserId());
            else ValoracionsUsuari.remove(idItem);
        }
    
        /*S'usa a Collaborative Filtering. Programat per: Simón Oliva*/
        //Pre:
        //Post: Es retornen les coordenades de l'usuari
        public HashMap<String,Double> getCoordenades(){

            HashMap<String,Double> coordenades = new HashMap<String,Double>();
            for (HashMap.Entry<String, Valoracio> v : ValoracionsUsuari.entrySet()){
                coordenades.put(v.getKey(),v.getValue().getPuntuacio());
            }
            return coordenades;
        }
    
    
}
    
    //Classe programada per Marina Alapont excepte getCoordenades  

