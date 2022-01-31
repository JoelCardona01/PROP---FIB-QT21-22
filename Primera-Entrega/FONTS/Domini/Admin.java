package Domini;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import Excepcions.ExcepcionsRecomanador;

public class Admin extends Persona {
     private Vector<Recomanacio> RecomanacionsAdmin; 

     //Pre:
     //Post: Creadora d'admin
     public Admin(String username, String password){  //Creadora que no s'usarà fins a la següent entrega
          super(username, password);
     }

     //Pre:
     //Post: Creadora d'admin
     public Admin(String username){
          super(username); 
     }

     //Pre: L'usuari rebut per parametres existeix, els items del conjunt d'items existeixen.
     //Post: Retorna un conjunt de n Strings que son els ids dels items ordenats de millors a pitjors 
     public  Vector<String> getRecomanacio(Recomanacio r, UsuariActiu ua, Map<String,Item> cjtItems, int n) throws ExcepcionsRecomanador{
          return r.getRecomanacio(ua, cjtItems, n);
     }

     //Pre: L'usuari ua existeix, un conjunt de recomancions existents i el conjunt d'usuaris Unknown
     //Post: Retorna el valor de les recomanacions
     public Double getValoracioRecomanacio(Recomanacio r, UsuariActiu ua, Vector<String> recomanacio, HashMap <String, UsuariActiu> Unknown) throws ExcepcionsRecomanador{
          Double DCG = r.valoraRecomanacio(recomanacio, ua, Unknown);
          return DCG;
     }
}


//Classe programada per: Marina Alapont