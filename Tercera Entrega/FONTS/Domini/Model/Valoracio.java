package Domini.Model;

import Domini.ControladorsDomini.CtrlDomini;
import Excepcions.ExcepcionsRecomanador;
import Excepcions.PuntuacioNoValida;
import  Domini.DataInterface.FactoriaCtrl;


public class Valoracio {

    private double puntuacio; 
    private Usuari u;
    private Item item;
    private boolean esPredictiva;
  

    //Pre: 
    //Post: Es comprova que la puntuacio pasada per parametre es troba dins del rang de puntuacioMaxima-puntuacioMinima del sistema.
    private void comprovaPuntuacio(double punt)  throws ExcepcionsRecomanador{
        FactoriaCtrl fc = FactoriaCtrl.getInstance();
        CtrlDomini cd = fc.getCtrlDomini();
        if (cd.getPuntuacioMinima() != null && cd.getPuntuacioMaxima() != null) {
            if (punt <cd.getPuntuacioMinima() || punt >cd.getPuntuacioMaxima()) throw new PuntuacioNoValida();
        }
    }

    //Constructora de Valoracio, on passen per parametre la puntuacio, el item puntuat i l'usuari que ha fet la valoracio (o per el qual s'ha predit).
    //Per defecte s'assigna predictiva false i, en cas de que sigui predictiva, mes endavant es canvia.
    public Valoracio (Double val, Item item, Usuari Usr) throws ExcepcionsRecomanador{
        comprovaPuntuacio(val);
        esPredictiva = false;
        puntuacio = val;
        this.item = item;
        u = Usr;
    }

    //Pre:
    //Post: es retorna la puntuacio de la valoracio.
    public Double getPuntuacio() {
        return puntuacio;
    }

    //Pre:
    //Post: es retorna l'id de l'item valorat.
    public String getIdItem(){
        return item.getId();
    }

    //Pre:
    //Post: es retorna l'usuari que ha fet la valoracio o pel qual s'ha predit.
    public Usuari getUsuari(){
        return u;
    }

    //Pre:
    //Post: es retorna l'objecte Item sobre el qual es fa la valoracio.
    public Item getItem(){
        return item;
    }

    //Pre: 
    //Post: es retorna el valor del boolea esPredictiva.
    public boolean esPredictiva() {
        return esPredictiva;
    }

    //Pre: 
    //Post: s'indica que la valoracio es predictiva, canviant l'atribut esPredictiva a true.
    public void AssignaPredictiva(boolean b) {
        esPredictiva = b;
    }

    //Pre:
    //Post: es canvia el valor de puntuacio de la valoracio per el valor passat per parametre en cas que estigui dins del rang correcte.
    public void modificaPuntuacio (Double newpuntuacio) throws ExcepcionsRecomanador{
        comprovaPuntuacio(newpuntuacio);
        puntuacio = newpuntuacio;
    }
}


//Classe programada per: Marina Alapont, Joel Cardona, Daniel Pulido