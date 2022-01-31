package Domini.Model;

//import java.util.ResourceBundle.Control;
public abstract class Persona {

    private int userId; //Identificador de l'usuari
        
        //Pre: No hi ha cap Persona identificada amb l'identificador userId
        //Post: Creadora de Persona identificada amb l'userId
        public Persona (int userId){  //Creadora que no s'usarà fins a la següent entrega
            this.userId=userId;
        }

        //Pre: 
        //Post: Es retorna l'userId
        public int getUserId(){
            return userId;
        }

        //Pre:
        //Post: Retorna null
        public UsuariActiu retornaUsuariActiu(){
            return null;
        }

         


}

//Classe programada per: Marina Alapont