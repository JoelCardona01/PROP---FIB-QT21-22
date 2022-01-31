package Dades;
import  Domini.DataInterface.CtrlRating;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import Domini.Model.Pair;

public class CtrlRatingsFile implements CtrlRating{
    public CtrlRatingsFile(){ }

    //Funcio feta per Simon Oliva
    //Pre: Existeix el fitxer del tipus ratings amb nom filename.
    //Post: Retorna totes les linies del fitxer de ratings en una llista de strings. Cada element de la llista es fins un salt de linia.
    public List<String> getAll(String filename) throws FileNotFoundException{
        LinkedList<String> valoracions = new LinkedList<String>();
    
        FileReader fr = new FileReader("../../DATA/" + filename);
        Scanner scan = new Scanner(fr);
    
        while(scan.hasNextLine()) valoracions.add(new String(scan.nextLine()));
    
        scan.close();
        return valoracions;
    }

    //Funcio feta per Marina Alapont i Joel Cardona
    //Pre: 
    //Post: Escriu en el fitxer json corresponent el contingut que li passen per parametre referent a tota la informacio associada als usuaris de Ratings i es seves valoracions.
    public void saveRatings(Vector<Integer> usrs, Vector<Integer> nVals, Vector<String> items, Vector<Double> puntuacio){
        JSONArray cjtUsuaris = new JSONArray();
        int valoracionsRecorregudes = 0;
        for (int i = 0; i < usrs.size(); i++){
            JSONObject usuari = new JSONObject();
            usuari.put("Username", usrs.get(i)); //Afeguim l'usuari iessim
            JSONArray cjtValoracions = new JSONArray();
            for(int j = valoracionsRecorregudes; j < valoracionsRecorregudes+nVals.get(i); ++j){
                JSONObject itempunt = new JSONObject();
                itempunt.put("IdItem", items.get(j));
                itempunt.put("Puntuacio", puntuacio.get(j));
                cjtValoracions.add(itempunt); 
            }
            valoracionsRecorregudes+=nVals.get(i);
            usuari.put("Valoracions",cjtValoracions);
            cjtUsuaris.add(usuari);
        }

        try (FileWriter file = new FileWriter("../../DATA/usuarisRatings.json")){
            file.write(cjtUsuaris.toJSONString());
            file.flush();
        } catch (IOException e){
        }
    }
    //Funcio feta per Marina Alapont i Joel Cardona
    //Pre: 
    //Post: Llegeix la informacio del fitxer usuarisRatings.json i la guarda en un Map per passar-li al controlador de Domini.
    //La key del map es el Id d'usuari, el vector de string, double son les valoracions que ha fet l'usuari
    public Map<Integer, Vector< Pair <String, Double> > > readRatings(){
        JSONParser jsP = new JSONParser();
        Map<Integer, Vector< Pair<String, Double> > > DadesRatings = new HashMap<Integer, Vector< Pair<String, Double> > >();
        try (FileReader rd = new FileReader("../../DATA/usuarisRatings.json")){
            
            JSONArray CjtUsuaris = (JSONArray) jsP.parse(rd);

            for (int i = 0; i < CjtUsuaris.size(); ++i){
                JSONObject next = (JSONObject) CjtUsuaris.get(i); //Obtenim l'objecte de l'usuari iessim
                int usrId = ((Long)next.get("Username")).intValue();
                Vector<Pair<String, Double> > Vals = new Vector<Pair<String, Double>>();
                JSONArray valoracions = (JSONArray) next.get("Valoracions");
                //Recorrem sobre les valoracions del usuari
                for (int j = 0; j < valoracions.size(); ++j){
                    JSONObject valoracioIessima = (JSONObject) valoracions.get(j);
                    String IdItem = (String) valoracioIessima.get("IdItem");
                    Double puntuacio = (Double) valoracioIessima.get("Puntuacio");
                    Pair<String, Double> valAct = new Pair<String, Double>();
                    valAct.first = IdItem; valAct.second = puntuacio;
                    Vals.add(valAct);
                }
                DadesRatings.put(usrId, Vals);
            }
        }
        catch (IOException e){
        }
        catch (ParseException e) {
        }
        return DadesRatings;
        
    }

}

//Classe implementada per Joel Cardona i Marina Alapont