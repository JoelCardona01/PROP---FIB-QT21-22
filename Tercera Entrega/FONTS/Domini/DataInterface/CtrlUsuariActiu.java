package Domini.DataInterface;

import Domini.Model.Pair;

import java.util.Vector;
import Excepcions.ExcepcionsRecomanador;

public interface CtrlUsuariActiu {
    public Pair<Integer, Vector<Pair<String, Double> >> getUsuariActiu(String usrname, String password) throws ExcepcionsRecomanador;
    public void saveUser(int userid, String username, String contrasenya, Vector<Pair<String, Double> >ValoracionsUsuari);
    public Boolean existeixUsuari(String username);
    public Boolean existeixUsuari(int userId);
    public void eliminarUsuari(int userId);
    public void eliminaValoracioItem(String idItem);
    public void eliminarTotesValoracions();
}

//Classe implementada per Joel Cardona i Marina Alapont