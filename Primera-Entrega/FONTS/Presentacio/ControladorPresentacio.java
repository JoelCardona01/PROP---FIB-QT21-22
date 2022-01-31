
package Presentacio;
import java.io.FileNotFoundException;
import java.util.Vector;

import Excepcions.CoordenadesNoValides;
import Excepcions.ExcepcionsRecomanador;
import Excepcions.KMassaGran;
import Excepcions.RangNoValid;
import ControladorsDomini.CtrlDomini;
import Domini.FactoriaController;
import Domini.Pair;

public class ControladorPresentacio {
    private VistaTerminal vt; 
    private CtrlDomini controladorDomini;
    //Per a aquesta entrega només hi ha una sola vista, en la proxima entrega de codi hi haura dues vistes, una per Administrador i l'altre per Usuari
    
    public ControladorPresentacio() throws FileNotFoundException {
        FactoriaController fc = FactoriaController.getInstance();
        fc.CrearControladorDomini();
        controladorDomini= fc.getControladorDomini();
		vt= new VistaTerminal(this);
        iniciaInstancia();
        vt.inicialitzaTerminal();
    
    }
    //Com que no hi ha login, la instancia de persona que estarà usant el programa serà de Usuari. En la següent entrega, això canviara depenent del usuari que ha fet login.
    public void iniciaInstancia() {
        controladorDomini.iniciaInstancia("Admin");
    }


    public void informaFitxerItems(String nom) throws FileNotFoundException{
        controladorDomini.llegeixAllItems(nom);
    }

    public void informaFitxerRatings(String nom) throws Exception{
        controladorDomini.informaRatings(nom);
    }

    public void informaFitxerKnown(String nom) throws Exception{
        controladorDomini.informaKnown(nom);
    }
    
    public void informaFitxerUnKnown(String nom) throws Exception{
        controladorDomini.informaUnknown(nom);
    }
    
    public Double demanaMin(){
        return controladorDomini.demanaMin();
    }

    public Double demanaMax(){
        return controladorDomini.demanaMax();
    }

    public void canviaMiniMax(Double min, Double max) throws RangNoValid{
        controladorDomini.canviaMiniMax(min,max);
    }
    public void recalculaClusters() throws CoordenadesNoValides {
        controladorDomini.recalculaClusters();
    }

    public String demanaEstrategiaActual(){
        return controladorDomini.demanaEstrategiaActual();
    }

    public void demanaCanviEstrategia(String nom){
        controladorDomini.canviaEstrategia(nom);
    }

    public int demanaKkmeans() {
        return controladorDomini.demanaKKmeans();
    }

    public int demanaKknearest() {
        return controladorDomini.demanaKKnearest();
    }

    public void canviaKKmeans(int e) throws KMassaGran {
        controladorDomini.canviaKKmeans(e);
    }

    public void canviaKKnearest(int e) throws KMassaGran {
        controladorDomini.canviaKKnearest(e);
    }

    public String demanaDistanciaActual() {
        return controladorDomini.demanaDistanciaActual();
    }

    public void demanaCanviDistancia(String e) {
        controladorDomini.canviaEstrategiaDistancia(e);
    }


    public Vector<String> informaPeticio1(String idUsuari,int nItemsSortida) throws ExcepcionsRecomanador{ 
       return controladorDomini.demanaRecomanacio1(idUsuari,nItemsSortida); 
    }
    
    public Vector<String> informaPeticio2(String idUsuari, Vector<String> items, int nItemsSortida)throws ExcepcionsRecomanador{
        return controladorDomini.demanaRecomanacio2(idUsuari,items,nItemsSortida); 
    }

    public Pair <Vector<Vector<String>>,Double> informaPeticio3(Vector<String> UsuarisAValorar, Vector<Vector<String>> ItemsAValorar, Vector<Integer> nItemsSortida)throws ExcepcionsRecomanador{
        return controladorDomini.valoraRecomanacio(UsuarisAValorar,ItemsAValorar,nItemsSortida); 
    }
    
   
  
    
}

//Classe programada per Marina Alapont