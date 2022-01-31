package Domini.Model;

//import java.io.*;
import java.util.*;
//import java.lang.*;
import Excepcions.AtributsDiferents;
public class Item{
    private String id;
    private Vector<Atribut> atributs;

    

    public Item(){}

    //Pre: No existeix cap item identificat amb id
    //Post: Crea un Item identificat amb id i un conjunt d'atributs i la columna on esta l'id
    public Item(String id, Vector<Atribut> atrs){
        atributs = new Vector<Atribut>();
        this.id = id;
        atributs = atrs;
    }
    
    //Pre: No existeix cap item identificat amb id
    //Post: Crea un Item identificat amb id
    public Item(String id) {
      atributs = new Vector<Atribut>();
      this.id = id;
    }

    //Pre: i >= 0
    //Post: Es retorna l'atribut de la possicio i del vector d'atributs
    public Atribut getAtribut(int i){
        if(atributs.get(i) != null) return atributs.get(i);
        else return null;
    }

    //Pre: i >= 0
    //Post: Es retorna el conjunt d'atributs com a un vector d'strings
    public Vector<String> getAtributString(int i){
      if(atributs.get(i)!=null) return atributs.get(i).getValorsString();
      else return new Vector<String>();
    }

    //Pre:
    //Post: Es retorna el vector d'atributs de l'item
    public Vector<Atribut> getAtributs(){
      return atributs;
    }
    
    //Pre: 
    //Post: Es retorna l'identificador de l'item
    public String getId(){
        return id;
    }

    //Pre: a.size() == Columnes.size()
    //Post:
    public void afegirAtributs(Vector<Atribut> a) {
      atributs = a;
    }

    //Pre: atributs.size() > numCol i numCol >= 0
    //Post: S'afegeix l'atribut a atributs[numCol]
    public void afegirAtribut(Atribut a, int numCol){
      atributs.set(numCol, a);
    }

    //Pre: L'item comparat existeix
    //Post: Es retorna un double en funcio de la similitud
    public double ComparaItem(Item comparat) throws AtributsDiferents {
        double similitutMitjana = 0.0;
        int contador = 0;
        for(int i = 0; i < atributs.size(); i++){ 
          if(atributs.get(i) != null && comparat.getAtribut(i) != null && !atributs.get(i).getColumna().getEsClau()) {
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

    //Pre: L'item comparat existeix
    //Post: Es retorna un double en funcio de la similitud
    public double ComparaItemEucl(Item comparat) throws AtributsDiferents {
      double distItem = 0; //Distancia sobre 5 per fer-ho mes comode
      int contador = 0;
      for (int i = 0; i<atributs.size(); i++) {
        if(atributs.get(i) != null && comparat.getAtribut(i) != null && !atributs.get(i).getColumna().getEsClau()) {
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
}

//Classe implementada per Daniel Pulido i Joel Cardona
