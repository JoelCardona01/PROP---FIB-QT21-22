package Dades;
import  Domini.DataInterface.CtrlEstat;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.json.simple.ItemList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import Domini.Model.Pair;


public class CtrlEstatFile implements CtrlEstat{

  //Funcio feta per: Joel Cardona i Marina Alapont
  //Pre: 
  //Post: Guarda en un fitxer json tota la configuració feta per l'administrador del sistema recomenador.
  public void saveState(int kkmeans, int knearests, String estrategiaDistanica, String estrategiaFiltering, Double puntmin, Double puntmax, int ultimUserId, int ultimItemId, int ultimRecomanacioId){
    JSONObject dadesEstat = new JSONObject();
    dadesEstat.put("KKmeans", kkmeans);
    dadesEstat.put("KNearests", knearests);
    dadesEstat.put("Estrategia Distancia", estrategiaDistanica);
    dadesEstat.put("Estrategia Filtering", estrategiaFiltering);
    if(puntmin == null) dadesEstat.put("Puntuacio minima", " ");
    else dadesEstat.put("Puntuacio minima", puntmin);
    if(puntmax == null) dadesEstat.put("Puntuacio maxima", puntmax);
    else dadesEstat.put("Puntuacio maxima", puntmax);
    dadesEstat.put("Ultim userId", ultimUserId);
    dadesEstat.put("Ultim itemId", ultimItemId);
    dadesEstat.put("Ultim recomanacioId", ultimRecomanacioId);

    try (FileWriter file = new FileWriter("../../DATA/configRecomanador.json")){
      file.write(dadesEstat.toJSONString());
      file.flush();
    } catch (IOException e){
    }
  }

  //Funcio feta per: Joel Cardona i Marina Alapont
  //Pre: 
  //Post: Obte la informacio de les k guardades en el fitxer de configRecomanador.json. La primera k del pair fa referencia a kkmeans i la segona a kknearests
  public Pair<Integer, Integer> getKsBD(){
    Pair<Integer, Integer> ks = new Pair<Integer, Integer>();
    JSONParser jsP = new JSONParser();
    try (FileReader rd = new FileReader("../../DATA/configRecomanador.json")){         
        JSONObject config = (JSONObject) jsP.parse(rd);
        int kkmeans = ((Long)config.get("KKmeans")).intValue();
        int kknearests = ((Long)config.get("KNearests")).intValue();
        ks.first = kkmeans; ks.second = kknearests;
    }
    catch (IOException e){
    }
    catch (ParseException e) {
    }
    return ks;
  }

  //Funcio feta per: Joel Cardona i Marina Alapont
  //Pre: 
  //Post: Obte la informacio de les estrategies guardades en el fitxer de configRecomanador.json. El primer string fa 
  //referencia a la estrategia distancia i el segon string fa referencia a la estrategia de filtering.
  public Pair<String, String> getEstrategiesBD(){
    Pair<String, String> estrategies = new Pair<String, String>();
    JSONParser jsP = new JSONParser();
    try (FileReader rd = new FileReader("../../DATA/configRecomanador.json")){         
        JSONObject config = (JSONObject) jsP.parse(rd);
        String estrategiaDistancia = (String)config.get("Estrategia Distancia");
        String estrategiaFiltering = (String)config.get("Estrategia Filtering");
        estrategies.first = estrategiaDistancia; estrategies.second = estrategiaFiltering;
    }
    catch (IOException e){
    }
    catch (ParseException e) {
    }
    return estrategies;
  }

  //Funcio feta per: Joel Cardona i Marina Alapont
  //Pre: 
  //Post: Obte la informacio de la puntuacio maxima i puntuacio minima que tenen les valoracions.
  //Aquesta informacio es llegeix del fitxer de configRecomanador.json. El primer string fa 
  //referencia a la puntuacio minima i el segon fa referencia a la puntuacio maxima.
  public Pair<Double, Double> getIntervalPuntuacionsBD(){
    Pair<Double, Double> punts = new Pair<Double, Double>();
    JSONParser jsP = new JSONParser();
    try (FileReader rd = new FileReader("../../DATA/configRecomanador.json")){         
        JSONObject config = (JSONObject) jsP.parse(rd);
        Double pmin;
        if(config.get("Puntuacio minima").equals(" "))  pmin = null;
        else pmin = (Double)config.get("Puntuacio minima");
        Double pmax;
        if(config.get("Puntuacio maxima").equals(" "))  pmax = null;
        else pmax = (Double)config.get("Puntuacio maxima");
        punts.first = pmin; punts.second = pmax;
    }
    catch (IOException e){
    }
    catch (ParseException e) {
    }
    return punts;
  }

  //Funcio feta per: Joel Cardona i Marina Alapont
  //Pre: 
  //Post: Retorna l'ultim UserId assignat segons el fitxer configRecomanador.json
  public int getUltimID(){
    JSONParser jsP = new JSONParser();
    int ultimId = 0;
    try (FileReader rd = new FileReader("../../DATA/configRecomanador.json")){         
        JSONObject config = (JSONObject) jsP.parse(rd);
        ultimId = ((Long)config.get("Ultim userId")).intValue();
    }
    catch (IOException e){
    }
    catch (ParseException e) {
    }
    return ultimId;
  }

  //Funcio feta per: Joel Cardona i Marina Alapont
  //Pre: 
  //Post: Retorna l'ultim ItemId assignat segons el fitxer configRecomanador.json
  public int getUltimIDItem(){
    JSONParser jsP = new JSONParser();
    int ultimIdItem = 0;
    try (FileReader rd = new FileReader("../../DATA/configRecomanador.json")){         
        JSONObject config = (JSONObject) jsP.parse(rd);
        ultimIdItem = ((Long)config.get("Ultim itemId")).intValue();
    }
    catch (IOException e){
    }
    catch (ParseException e) {
    }
    return ultimIdItem;
  }

  //Funcio feta per: Marina Alapont i Joel Cardona
  //Pre: 
  //Post: Retorna l'ultima recomanacioId assignat segons el fitxer configRecomanador.json
  public int getUltimIDRecomanacio(){
    JSONParser jsP = new JSONParser();
    int ultimIdRecomanacio = 0;
    try (FileReader rd = new FileReader("../../DATA/configRecomanador.json")){         
        JSONObject config = (JSONObject) jsP.parse(rd);
        ultimIdRecomanacio = ((Long)config.get("Ultim recomanacioId")).intValue();
    }
    catch (IOException e){
    }
    catch (ParseException e) {
    }
    return ultimIdRecomanacio;
  }
}
//Classe implementada per Joel Cardona i Marina Alapont

