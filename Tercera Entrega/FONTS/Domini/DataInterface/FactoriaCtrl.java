package Domini.DataInterface;

import Domini.ControladorsDomini.CtrlDomini;
import Dades.*;
import Excepcions.ExcepcionsRecomanador;

//Aquesta classe creara les instancies de cada interficie per a les clases de dades (redueix el acoplament)
public class FactoriaCtrl {
    private static FactoriaCtrl factoriaCtrl;
    private CtrlItem ItemCtrl;
    private CtrlRating RatingsCtrl;
    private CtrlUsuariActiu UsuariActiuCtrl;
    private CtrlEstat EstatCtrl;
    private CtrlClusters ClustersCtrl;
    private CtrlDomini DominiCtrl;  
    private CtrlRecomanacions RecomanacionsCtrl;

    private FactoriaCtrl(){ }

    //Funcio feta per: Joel Cardona
    //Pre:
    //Post: Retorna la instancia de factoriaCtrl, si no existeix cap factoriaCtrl es crea.
    public static FactoriaCtrl getInstance(){
        if(factoriaCtrl == null)
            factoriaCtrl = new FactoriaCtrl(){
        
        };
        return factoriaCtrl;
    }

    //Funcio feta per: Joel Cardona
    //Pre:
    //Post: Retorna la instancia de CtrlDomini, si no existeix cap CtrlDomini es crea.
    public CtrlDomini getCtrlDomini() throws ExcepcionsRecomanador{
        if(DominiCtrl == null) DominiCtrl = new CtrlDomini();
        return DominiCtrl;
    }

    //Funcio feta per: Joel Cardona
    //Pre:
    //Post: Retorna la instancia de CtrlItem, si no existeix cap CtrlItem es crea.
    public CtrlItem getCtrlItem(){
        if(ItemCtrl == null) ItemCtrl = new CtrlItemFile();
        return ItemCtrl;
    }

    //Funcio feta per: Joel Cardona
    //Pre:
    //Post: Retorna la instancia de CtrlRating, si no existeix cap CtrlRating es crea.
    public CtrlRating getCtrlRating(){
        if(RatingsCtrl == null) RatingsCtrl = new CtrlRatingsFile();
        return RatingsCtrl;
    }
    
    //Funcio feta per: Joel Cardona
    //Pre:
    //Post: Retorna la instancia de CtrlUsuariActiu, si no existeix cap CtrlUsuariActiu es crea.
    public CtrlUsuariActiu getCtrlUsuariActiu(){
        if(UsuariActiuCtrl == null) UsuariActiuCtrl = new CtrlUsuarisActiusFile();
        return UsuariActiuCtrl;
    }

    //Funcio feta per: Joel Cardona
    //Pre:
    //Post: Retorna la instancia de CtrlEstat, si no existeix cap CtrlEstat es crea.
    public CtrlEstat getCtrlEstat(){
        if(EstatCtrl == null) EstatCtrl = new CtrlEstatFile();
        return EstatCtrl;
    }

    //Funcio feta per: Joel Cardona
    //Pre:
    //Post: Retorna la instancia de CtrlClusters, si no existeix cap CtrlClusters es crea.
    public CtrlClusters getCtrlClusters() {
        if(ClustersCtrl == null) ClustersCtrl = new CtrlClustersFile();
        return ClustersCtrl;
    }

    //Funcio feta per: Joel Cardona
    //Pre:
    //Post: Retorna la instancia de CtrlRecomanacions, si no existeix cap CtrlRecomanacions es crea.
    public CtrlRecomanacions getCtrlRecomanacions() {
        if(RecomanacionsCtrl == null) RecomanacionsCtrl = new CtrlRecomanacionsFile();
        return RecomanacionsCtrl;
    }
}

//Classe implementada per Joel Cardona