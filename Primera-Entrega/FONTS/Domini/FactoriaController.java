package Domini;
import ControladorsDomini.*;

public class FactoriaController{

    private static FactoriaController singletonFactory;
    private FactoriaController(){}
    private CtrlDomini DominiCtrl;  
    
    public static FactoriaController getInstance(){
        if(singletonFactory == null) singletonFactory = new FactoriaController();
        return singletonFactory;
    }

    //NOMES ES POT CRIDAR DESDE PRESENTACIO I UN (1) COP SI ES POSSIBLE!!!
    public void CrearControladorDomini(){
        DominiCtrl = CtrlDomini.getInstance();
    }

    public CtrlDomini getControladorDomini(){
        return DominiCtrl;
    }
    
}