package Domini;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import Excepcions.ExcepcionsRecomanador;

public class UsuariActiu extends Persona {
         private Map <String,Valoracio> ValoracionsUsuari = new HashMap<String,Valoracio>();  //string es el id del item.

         public UsuariActiu(String username, String pwd){ //Creadora que no s'usarà fins a la següent entrega
            super(username, pwd);
         }

         public UsuariActiu(String username){
            super(username);
         }
         
         public Map<String, Valoracio> getValoracions(){
            return ValoracionsUsuari; 
         }

         public void setValoracions(Map <String,Valoracio> m){
            ValoracionsUsuari=m;
         }

         public void afegirValoracio(Valoracio v){
            ValoracionsUsuari.put(v.getIdItem(), v);
         }
         
         public void modificaValoracio (String iditem, Double newpuntuacio)throws ExcepcionsRecomanador{ 
               Valoracio v1 = ValoracionsUsuari.get(iditem);
               v1.modificaPuntuacio(newpuntuacio); //es modifica el valor de la puntuació
               ValoracionsUsuari.put(v1.getIdItem(), v1); // no se si fa falta per actualitzar el nou valor
         } 
         
         public void eliminaValoracio (String iditem){
             ValoracionsUsuari.remove(iditem);
         } 

         /*S'usa a Collaborative Filtering. Programat per: Simón Oliva*/
         public Vector<Double> getCoordenades(Map<String,Item> allitems){

            int ndimensions = allitems.size();
            double coord;
            double pmin = Valoracio.getMinPunt();
            //System.out.println("puntMIN: " + pmin);
            double pmax = Valoracio.getMaxPunt();
            //System.out.println("puntMAX: " + pmax);
            double dif = pmax-pmin;

            Vector<Double> coordenades = new Vector<Double>(ndimensions);

            Iterator<HashMap.Entry<String,Item>> it = allitems.entrySet().iterator();
            for (int i = 0; i < ndimensions; ++i) {

               String itemID = it.next().getKey();

               // si l'usuari ha valorat un item, la coordenada serà la puntuació que li hagi donat
               if (ValoracionsUsuari.containsKey(itemID)) coord = ValoracionsUsuari.get(itemID).getPuntuacio();
               else {
               // si no l'ha valorat, serà un nombre random entre les puntuacions mínima i màxima
               coord = pmin + Math.random()*dif;
               }
               coordenades.add(coord);
            }
            //System.out.println("Retornem les coordenades amb tantes dimensions: " + coordenades.size());
            return coordenades;
         }

         public Vector<String> getRecomanacio(Recomanacio r, UsuariActiu ua, Map<String, Item> cjtItems, int nItemsSortida) throws ExcepcionsRecomanador {
            //Per a aquesta implementació no hi ha codi. Si més no, és trivial, seria com al de admin pero amb ua = a self
            return null;
         }

         public Double getValoracioRecomanacio(Recomanacio r, UsuariActiu ua, Vector<String> items,  HashMap<String, UsuariActiu> Unkown)
               throws ExcepcionsRecomanador {
            // No fa res
            return null;
         }


}

//Classe programada per Marina Alapont excepte els metodes que indiquen lo contrari