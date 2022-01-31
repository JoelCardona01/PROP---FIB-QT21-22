package Excepcions;

public class BaseDeDadesBuida  extends ExcepcionsRecomanador{
    
        public String getTipus() {
            return "BaseDeDadesBuida";
        }
    
        public BaseDeDadesBuida(String fitxer) {
            super("La base de dades esta buida." +"No s'ha carregat el fitxer "+ fitxer +". Cal que l'administrador carregui els fichers necessaris. Amb la base de dades buida, el sistema no funciona correctament.");
        }
    
    
      
}
