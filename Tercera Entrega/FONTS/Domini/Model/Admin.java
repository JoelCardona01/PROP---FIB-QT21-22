package Domini.Model;


public class Admin extends Persona {
     private String contrasenya;
     private String username;

     //Pre:
     //Post: Creadora d'admin
     public Admin(int userId, String username,  String password){ 
          super(userId);
          contrasenya=password;
          this.username = username;
     }

     //Pre:
     //Post: Creadora d'admin
     public Admin(String username){
          super(0);
          this.username = username;
     }

     //Pre:
     //Post: Retorna la contrasenya del Administrador
     public String getConstrasenya(){
          return contrasenya;
     }

     //Pre:
     //Post: Canvia la contrasenya de l'usuari
     public void canviaContrasenya(String contra){
          contrasenya = contra;
     }

}


//Classe programada per: Marina Alapont