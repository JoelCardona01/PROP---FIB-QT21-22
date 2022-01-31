package Dades;

import java.io.*;
import java.util.*;

public class CtrlRatingsFile {

    //Apliquem el patró singleton
    private static CtrlRatingsFile singletonObject;

    //Pre:
    //Post: Retorna la instancia de CtrlRatingsFile, si no existeix cap CtrlRatingsFile es crea.
    public static CtrlRatingsFile getInstance(){
        if(singletonObject == null) singletonObject = new CtrlRatingsFile();
        return singletonObject;
    }

    //Constructora (privada)
    private CtrlRatingsFile() {}

    //Per llegir totes les valoracions d'un fitxer
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

}
