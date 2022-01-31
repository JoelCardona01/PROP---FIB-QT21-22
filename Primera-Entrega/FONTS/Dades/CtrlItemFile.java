package Dades;


import java.io.*;
import java.util.*;


public class CtrlItemFile{

  private static CtrlItemFile singletonObject;

  //Pre:
  //Post: Retorna la instancia de CtrlItemFile, si no existeix cap CtrlItemFile es crea.
  public static CtrlItemFile getInstance(){
    if(singletonObject == null)
      singletonObject = new CtrlItemFile(){

      };
    return singletonObject;
  }

  private CtrlItemFile(){  }

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
      
}
//Classe implementada per Joel Cardona