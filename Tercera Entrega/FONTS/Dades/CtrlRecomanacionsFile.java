package Dades;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Domini.DataInterface.CtrlRecomanacions;
import Domini.Model.Pair;

public class CtrlRecomanacionsFile implements CtrlRecomanacions{

    //Funcio feta per: Marina Alapont
    //Pre: 
    //Post: Es guarden les recomanacions fetes per a l'usuari identificat amb usuariId al fitxer recomanacions.json situat al subdirectori DATA
    public void saveRecomanacions(int usuariId, HashMap<Integer, Vector< Pair<String, Double>>> Recomanacions) {
        JSONParser jsP = new JSONParser();
        JSONArray CjtUsuaris = new JSONArray();
        try (FileReader rd = new FileReader("../../DATA/recomanacions.json")){
            
            CjtUsuaris = (JSONArray) jsP.parse(rd);
            boolean trobat = false;
            for (int i = 0; i < CjtUsuaris.size() && !trobat; ++i){
                JSONObject next = (JSONObject) CjtUsuaris.get(i); //Obtenim l'objecte de l'usuari iessim
                int useridIessim = ((Long)next.get("userId")).intValue();  //Obtenim l'id de l'usuari iessim
                if(useridIessim == (usuariId)){    //Si l'userId coincideix
                    CjtUsuaris.remove(next); //L'esborrem pq despres l'afegirem 
                    trobat = true;  //Deixem de recorrer el vector
                }
            }
        }
        catch (IOException e){
            //no fem res
        }
        catch (ParseException e) {
            //no fem res
        }

        JSONObject nouUsuari = new JSONObject();    //Creem un nou objecte  JSON amb el usuari rebut per parametre
        nouUsuari.put("userId", usuariId);  //Guardem l'id de l'usuari
        JSONArray recomanacions = new JSONArray();    //Generem l'array de recomanacions de l'usuari
        for (HashMap.Entry<Integer, Vector<Pair<String, Double>>> r : Recomanacions.entrySet()){
            JSONObject contingutRecomanacio = new JSONObject();
            JSONArray recomanacioIessima = new JSONArray();
            for (Pair<String, Double> rec : r.getValue()){
                JSONObject rIessima = new JSONObject();
                rIessima.put("itemId",rec.first);
                rIessima.put("puntuacioPredita", rec.second);
                recomanacioIessima.add(rIessima);
            }
            contingutRecomanacio.put("Valoracions predites", recomanacioIessima);
            contingutRecomanacio.put("idRecomanacio", r.getKey());
            recomanacions.add(contingutRecomanacio);  //Ho afegim a la array de valoracions de l'usuari
        }
        nouUsuari.put("Recomanacions", recomanacions);  //Guardem l'array a l'usuari 
        CjtUsuaris.add(nouUsuari);  //Afegim l'usuari al conjunt d'usuaris
        try (FileWriter file = new FileWriter("../../DATA/recomanacions.json")){
            file.write(CjtUsuaris.toJSONString());  //Guardem la informacio al document situat a DATA/usuarisActius.json
            file.flush();
        } catch (IOException e){
            //no fem res
        }
        
    }

    //Funcio feta per: Marina Alapont
    //Pre: 
    //Post: Es retornen les recomanacions fetes per a l'usuari identificat amb userId
    public HashMap<Integer,Vector<Pair<String, Double>>> readRecomanacions(int userId) {
        JSONParser jsP = new JSONParser();
        JSONArray CjtUsuaris = new JSONArray();
        HashMap<Integer,Vector<Pair<String, Double>>> resultat = new HashMap<Integer,Vector<Pair<String, Double>>>();
        try (FileReader rd = new FileReader("../../DATA/recomanacions.json")){
            
            CjtUsuaris = (JSONArray) jsP.parse(rd);
            boolean trobat = false;
            for (int i = 0; i < CjtUsuaris.size() && !trobat; ++i){
                JSONObject next = (JSONObject) CjtUsuaris.get(i); //Obtenim l'objecte de l'usuari iessim
                int useridIessim = ((Long)next.get("userId")).intValue();  //Obtenim l'id de l'usuari iessim
                if(useridIessim == (userId)){    //Si l'userId coincideix
                    trobat = true;  //Deixem de recorrer el vector
                    JSONArray recomanacions = (JSONArray) next.get("Recomanacions");
                    for(int j = 0; j < recomanacions.size(); ++j){
                        Vector<Pair<String,Double>> RecomanacioIessima = new Vector<Pair<String,Double>>();
                        JSONObject contingut = (JSONObject) recomanacions.get(j);
                        JSONArray recIessima = (JSONArray) contingut.get("Valoracions predites");
                        int id = ((Long)contingut.get("idRecomanacio")).intValue();
                        for(int k = 0; k < recIessima.size(); ++k){
                            JSONObject aux = (JSONObject) recIessima.get(k); 
                            Pair<String, Double> rec = new Pair<String, Double>();
                            rec.second = (Double)aux.get("puntuacioPredita");
                            rec.first = (String) aux.get("itemId");
                            RecomanacioIessima.add(rec);
                        }
                        resultat.put(id, RecomanacioIessima);
                    }
                }
            }
        }
        catch (IOException e){
        }
        catch (ParseException e) {
        }
        return resultat;
    }
    
}

//Classe implementada per Marina Alapont