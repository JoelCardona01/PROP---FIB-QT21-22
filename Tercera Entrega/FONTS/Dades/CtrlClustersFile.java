package Dades;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.ItemList;
import org.json.simple.parser.*;

import Domini.DataInterface.CtrlClusters;

//Aquesta classe s'encarrega de emmagatzemar i llegir dades dels fitxers clusters.json i centroides.json
public class CtrlClustersFile implements CtrlClusters{

    //Funcio feta per Joel Cardona
    //Pre: 
    //Post: Es guarden les dades rebudes per parametre al fitxer clusters.json situat al directori DATA
    public void saveClusters(Vector<Vector<Integer>> clusters) {
        JSONObject jsonClusterI  = new JSONObject();    //Creem l'objecte JSON a on estara tota la informacio
        for (int i = 0; i < clusters.size(); ++i){  //Per a cada cluster
            JSONArray cjtClustersI = new JSONArray();   //Cada cluster te una array d'ids d'usuaris
            for(int j = 0; j < clusters.get(i).size(); ++j){
                cjtClustersI.add(clusters.get(i).get(j)); //Afegim el id usuari del cluster al conjunt d'usuaris del cluster iessim
            }
            jsonClusterI.put("Clutser "+String.valueOf(i), cjtClustersI); //Ho guardem al objecte
        }
        try (FileWriter file = new FileWriter("../../DATA/clusters.json")){ //Guardem les dades al fitxer clusters.json
            file.write(jsonClusterI.toJSONString());
            file.flush();
        } catch (IOException e){
        }
        
    }

    //Funcio feta per Joel Cardona
    //Pre: 
    //Post: Es guarden les dades rebudes per parametre al fitxer centroides.json situat al directori DATA
    public void saveCentroides(HashMap<String, Double>[] centoids) {
        JSONObject jsonCentoidsI  = new JSONObject(); //Creem l'objecte JSON a on estara tota la informacio
        for (int i = 0; i < centoids.length; ++i){  //Per a cada centroide
            JSONArray cjtCentroidsI = new JSONArray();   //Creem l'array on afegirem la informacio del centroide iessim
            for(HashMap.Entry<String, Double>  j : centoids[i].entrySet()){
                JSONObject item = new JSONObject(); //Creem un objecte de la array iessima
                item.put("ItemId", j.getKey()); //Afegim l'itemId
                item.put("Puntuacio", j.getValue());    //Afegim la puntuacio
                cjtCentroidsI.add(item); //Afegim la informacio al centroide
            }
            jsonCentoidsI.put("Centoide "+String.valueOf(i), cjtCentroidsI); //Ho guardem al objecte
        }
        try (FileWriter file = new FileWriter("../../DATA/centroides.json")){   //Guardem les dades al fitxer centroides.json
            file.write(jsonCentoidsI.toJSONString());
            file.flush();
        } catch (IOException e){
        }
                
        
    }

    //Funcio feta per Joel Cardona
    //Pre: 
    //Post: Retorna les dades dels clusters guardats al fitxer clusters.json del subdirectori DATA. 
    public Vector<Vector<Integer>> readClusters() {
        JSONParser jsP = new JSONParser();
        Vector<Vector<Integer> > DadesClusters = new Vector<Vector<Integer> > ();
        try (FileReader rd = new FileReader("../../DATA/clusters.json")){   //Llegim tot el fitxer
                
            JSONObject CjtClusters = (JSONObject) jsP.parse(rd);    
    
            for(Iterator iterator = CjtClusters.keySet().iterator(); iterator.hasNext();) { //Iterem sobre els diferents clusters
                Vector<Integer> ClusterI = new Vector<Integer>();   //Per a cada cluster creem el vector de Integer
                String nom = (String) iterator.next();  
                JSONArray next = (JSONArray) CjtClusters.get(nom); //Obtenim l'objecte de l'usuari iessim
                for(int i = 0; i < next.size(); ++i){
                    int usrId = ((Long)next.get(i)).intValue(); //Llegim l'userId iessim
                    ClusterI.add(usrId);    //Afegir l'userId al cluster que li pertoca
                }
                DadesClusters.add(ClusterI);    
            }
        }
        catch (IOException e){
        }
        catch (ParseException e) {
        }
        return DadesClusters;        
    }

    //Funcio feta per Joel Cardona
    //Pre:
    //Post: Retorna les dades dels centroides guardades al fitxer centroides.json del subdirectori DATA
    public HashMap<String, Double>[] readCentoides() {
        JSONParser jsP = new JSONParser();
        HashMap<String, Double>[] DadesCentroides = new HashMap[0];
        try (FileReader rd = new FileReader("../../DATA/centroides.json")){ //llegim del fitxer centroides.json
                
            JSONObject CjtCentorides = (JSONObject) jsP.parse(rd);
            int k = 0;
            for(Iterator iterator = CjtCentorides.keySet().iterator(); iterator.hasNext();) { //Recorrem els noms dels centroides
                iterator.next();
                ++k; //Per saber quants centroides existeixen
            }
            DadesCentroides = new HashMap[k];   //Creem el HashMap on hi posarem les dades
            for(int i = 0; i < k; ++i) DadesCentroides[i] = new HashMap<String, Double>();  //Inicialitzem el hashMap iessim
            int nCentroide = 0;
            for(Iterator iterator = CjtCentorides.keySet().iterator(); iterator.hasNext();) {
                String nom = (String) iterator.next();  //Obtenim el nom del centroide
                JSONArray next = (JSONArray) CjtCentorides.get(nom); //Obtenim les dades que hi ha en el centroide 
                for(int i = 0; i < next.size(); ++i){   //Recorrem les valoracions del centroide
                    JSONObject valoracioIessima = (JSONObject) next.get(i); 
                    double puntuacioIessima = (Double)valoracioIessima.get("Puntuacio");  //Obtenim la puntuacio de la valoracio iessima del cluster
                    String itemID = (String)valoracioIessima.get("ItemId"); //Obtenim la itemId de la valoracio iessima del clutser
                    DadesCentroides[nCentroide].put(itemID, puntuacioIessima);  //Afegim les dades al Map que retornarem
                }
                ++nCentroide;
            }
        }
        catch (IOException e){
            
        }
        catch (ParseException e) {
        }
        return DadesCentroides; 
    }
    
}

//Classe implementada per Joel Cardona