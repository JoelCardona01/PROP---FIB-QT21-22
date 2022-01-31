package Domini;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import Excepcions.ExcepcionsRecomanador;

//import java.util.ResourceBundle.Control;
public abstract class Persona {
        private String Usuari;  /*Una persona s'identifica per el seu Usuari, les persones que provenen del fitxer d'informació otorgat per el admin
                                tindran un username numeric, el indicat als fitxers. Els usuaris que usin l'aplicació activament, tindran username que escullin quan es registrin.*/
        private String Constrasenya;  //la funcionalitat de login i register no s'implementa la primera entrega, per tant treballarem de moment sempre amb usuaris que el admin introdueixi a la base de dades i per tant no tindran contrasenya.

        public Persona (String username, String pwd){  //Creadora que no s'usarà fins a la següent entrega
            Usuari=username;
            Constrasenya=pwd;
        }
        
        public Persona (String username){   
            Usuari=username;
        }

        public String getUsuari(){
            return Usuari;
        }

        public String getConstrasenya(){
            return Constrasenya;
        }

        //SETTERS
        public void canviaNom(String nom){
            Usuari = nom;
        }

        public void canviaContrasenya(String contra){
            Constrasenya = contra;
        }

        public abstract Double getValoracioRecomanacio(Recomanacio r, UsuariActiu ua, Vector<String> items, HashMap <String, UsuariActiu> Unkown) throws ExcepcionsRecomanador;

        public abstract Vector<String> getRecomanacio(Recomanacio r, UsuariActiu ua, Map<String, Item> cjtItems, int nItemsSortida) throws ExcepcionsRecomanador;
         



}

//Classe programada per: Marina Alapont