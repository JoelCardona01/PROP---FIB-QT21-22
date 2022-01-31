package Domini;

//import java.io.*;
import java.util.*;
//import java.lang.*;
import Excepcions.AtributsDiferents;
public class Item{
    private String id;
    private Vector<Atribut> atributs;
    private HashSet<Valoracio> valoracions;
    private HashSet<Recomanacio> recomenacions;
    

    public Item(){}

    //Crea un Item amb un conjunt d'atributs i la columna on esta l'id
    public Item(String id, Vector<Atribut> atrs){
        atributs = new Vector<Atribut>();
        this.id = id;
        atributs = atrs;
        valoracions = new HashSet<Valoracio>();
    }
    
    public Item(String id) {
      atributs = new Vector<Atribut>();
      valoracions = new HashSet<Valoracio>();
      this.id = id;
    }


    public void afegirAtributs(Vector<Atribut> a) {
      atributs = a;
    }

    public void afegirValoracio(Valoracio val){
      valoracions.add(val);
    }

    public void afegirRecomanacio(Recomanacio rec){
      recomenacions.add(rec);
    }
    
    //Els dos items tenen les mateixes columnes
    public double ComparaItem(Item comparat) throws AtributsDiferents {
        double similitutMitjana = 0.0;
        int contador = 0;
        for(int i = 0; i < atributs.size(); i++){ 
          if(atributs.get(i) != null && comparat.getAtribut(i) != null && !atributs.get(i).esClau()) {
            similitutMitjana+=atributs.get(i).ComparaAtributs(comparat.getAtribut(i));
            contador++;
          }
          else if ((atributs.get(i) == null && comparat.getAtribut(i) != null) || (atributs.get(i) != null && comparat.getAtribut(i) == null)) {
            similitutMitjana += 2.5;
            contador++;
          }
        }
        return (similitutMitjana/(contador));
    }

    public double ComparaItemEucl(Item comparat) throws AtributsDiferents {
      double distItem = 0; //Distancia sobre 5 per fer-ho mes comode
      int contador = 0;
      for (int i = 0; i<atributs.size(); i++) {
        if(atributs.get(i) != null && comparat.getAtribut(i) != null && !atributs.get(i).esClau()) {
          double similitud = atributs.get(i).ComparaAtributs(comparat.getAtribut(i));
          similitud = (1-(similitud/5))*5;
          distItem += Math.pow(similitud, 2);
          contador++;
        }
        else if ((atributs.get(i) == null && comparat.getAtribut(i) != null) || (atributs.get(i) != null && comparat.getAtribut(i) == null)) {
          distItem += Math.pow(2.5, 2);
          contador++;
        }
      }
      distItem = Math.sqrt(distItem);
      return (1-((distItem)/(contador*Math.sqrt(contador*25))))*5;
    }
    
    public Atribut getAtribut(int i){
        return atributs.get(i);
    }

    //Retorna el id del item
    
    public String getId(){
        return id;
    }

    public HashSet<Valoracio> getValoracios(){
      return valoracions;
    }

    public HashSet<Recomanacio> getRecomanacions(){
      return recomenacions;
    }

};
