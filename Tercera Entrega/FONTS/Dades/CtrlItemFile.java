package Dades;
import  Domini.DataInterface.CtrlItem;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import org.json.simple.ItemList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import Domini.Model.Pair;


public class CtrlItemFile implements CtrlItem{

  public CtrlItemFile(){  }

  //Funcio feta per: Joel Cardona
  //Pre: Existeix el fitxer items amb filename.
  //Post: Retorna totes les linies del fitxer d'items en una llista de strings. Cada element de la llista es fins un salt de linia.
  public List<String> getAll(String filename) throws FileNotFoundException{
    LinkedList<String> items = new LinkedList<String>();

    FileReader fr = new FileReader("../../DATA/"+filename);
    Scanner scan = new Scanner(fr);

    while(scan.hasNextLine()) items.add(new String(scan.nextLine()));

    scan.close();
    return items;

  }

  //Funcio feta per: Joel Cardona i Marina Alapont
  //Pre: 
  //Post: Guarda en un fitxer json tota la informació dels items que li passen per parametre.
  public void saveItems(Map<String, Pair<Vector<String>, Vector<Vector<String> > > > items){
    JSONArray cjtItems = new JSONArray();
    for(Map.Entry<String, Pair<Vector<String>, Vector<Vector<String>>>> i : items.entrySet()){
      JSONObject itemIessim = new JSONObject();
      itemIessim.put("id", i.getKey());
      for(int j = 0; j < i.getValue().first.size(); ++j){
        if(!i.getValue().first.get(j).equals("id")){
          JSONArray cjtAtributs = new JSONArray();
          for(int k = 0; k < i.getValue().second.get(j).size(); ++k){
            cjtAtributs.add(i.getValue().second.get(j).get(k));
          }
          itemIessim.put(i.getValue().first.get(j), cjtAtributs);
        }
      }
      cjtItems.add(itemIessim);
    }
    try (FileWriter file = new FileWriter("../../DATA/itemsDB.json")){
      file.write(cjtItems.toJSONString());
      file.flush();
    } catch (IOException e){
    }
  }
  
  //Funcio feta per: Joel Cardona
  //Pre: 
  //Post: Retorna en un Map tota la informació dels items que te guardats en el fitxer itemsBD.json.
  public Map<String, Pair<Vector<String>, Vector<Vector<String> > > > readItems(){
    JSONParser jsP = new JSONParser();
    Map<String, Pair<Vector<String>, Vector<Vector<String> > > > DadesItems = new HashMap<String, Pair<Vector<String>, Vector<Vector<String> > > >();
    try (FileReader rd = new FileReader("../../DATA/itemsDB.json")){      //Llegim de items.db situat al subdirectori DATA   
      JSONArray CjtItems = (JSONArray) jsP.parse(rd);
      for (int i = 0; i < CjtItems.size(); ++i){  //Recorrem els items
        int numCol = 0;
        String id = "";
        Vector <String> nomsCols = new Vector <String>();
        Vector<Vector <String> > Atributs = new Vector<Vector<String>>();
        JSONObject next = (JSONObject) CjtItems.get(i); //Obtenim l'objecte de l'usuari iessim
        for(Iterator iterator = next.keySet().iterator(); iterator.hasNext();) {  //Iterem sobre les diferents columnes que te cada item
          String nomColumna = (String) iterator.next(); //Obtenim el nom de la columna
          nomsCols.add(nomColumna); //L'afegim al vector de noms de columna
          if (nomColumna.equals("id")){ //Comprovem si la columna actual es id
            id = (String) next.get(nomColumna);
            Vector <String> AtributJessim = new Vector<String>();
            AtributJessim.add(id);
            Atributs.add(AtributJessim);
          } else {
            JSONArray valorsColumna = (JSONArray) next.get(nomColumna);
            if(valorsColumna.size()== 0) Atributs.add(new Vector<String>());  //En cas que no hi hagi cap element per aquesta columna, s'afegeix un vector<String> buit
            Vector <String> AtributJessim = new Vector<String>();
            for (int j = 0; j < valorsColumna.size(); ++j){
              AtributJessim.add((String) valorsColumna.get(j)); //S'afegeixen els diferents valors al vector
              if(j+1==valorsColumna.size()) Atributs.add(AtributJessim);
            }
          }
          ++numCol;
        }
        Pair<Vector<String>, Vector<Vector<String> > > itemIessim = new Pair<Vector<String>, Vector<Vector<String> > >();
        itemIessim.first = nomsCols; itemIessim.second = Atributs;
        DadesItems.put(id, itemIessim); //Afegim la informacio al conjunt de dades a retornar
      }
    }
    catch (IOException e){
    }
    catch (ParseException e) {
    }
    return DadesItems;
  }
      
}
//Classe implementada per Joel Cardona i Marina Alapont 