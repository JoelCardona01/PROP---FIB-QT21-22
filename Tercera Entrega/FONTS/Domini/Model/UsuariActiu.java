package Domini.Model;

import java.util.Vector;



public class UsuariActiu extends Usuari {
         private String contrasenya;
         private String username;
         private Vector<Recomanacio> recomanacionsRetornades;
         
         //Pre: No existeix cap Usuari identificat amb userId. No existeix cap usuari amb username username
         //Post: Creadora d'usuari actiu
         public UsuariActiu(int userId, String username, String pwd){ 
            super(userId);
            this.username = username;
            contrasenya=pwd;
            recomanacionsRetornades= new Vector<Recomanacio>();
         }

         //Pre: No existeix cap Usuari identificat amb userId.
         //Post: Creadora d'usuari actiu
         public UsuariActiu(int userId){
            super(userId);
         }

         //Pre:
         //Post: Es retorna la propia instancia
         public UsuariActiu retornaUsuariActiu() {
            return this;
         }
         
         //Pre:
         //Post: Es retorna l'username de l'usuari actiu
         public String getUsername(){
            return username;
         }
         
         //Pre:
         //Post: Es retorna la contrasenya de l'usuari actiu
         public String getContrasenya(){
            return contrasenya;
         }

         //Pre:
         //Post: Es retornen les recomanacions fetes per al usuari actiu
         public Vector <Recomanacio> getRecomanacions(){
            return recomanacionsRetornades;
         }

         //Pre: 0 <= i
         //Post: S'obte la recomanacio iessima feta per al usuari actiu
         public Recomanacio getRecomanacioIessima(int i) {
            return recomanacionsRetornades.get(i);
         }

         //Pre:
         //Post: S'afegeix la recomanacio al conjunt de recomanacions de l'usuari actiu
         public void afegirRecomanacio(Recomanacio r) {
            recomanacionsRetornades.add(r);
         }

         //Pre: No existeix cap usuari actiu amb username nomUsuari
         //Post: Es canvia l'username de l'usuari actiu per nomUsuari
         public void canviaUsername(String nomUsuari){
            username=nomUsuari;
         }
         
         //Pre:
         //Post: Es canvia la contrasenya de l'usuari actiu
         public void canviaContrasenya(String contra){
            contrasenya = contra;
         }


}

//Classe programada per Marina Alapont