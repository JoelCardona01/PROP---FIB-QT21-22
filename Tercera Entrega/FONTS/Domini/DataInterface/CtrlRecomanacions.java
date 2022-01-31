package Domini.DataInterface;

import java.util.HashMap;
import java.util.Vector;

import Domini.Model.Pair;

public interface CtrlRecomanacions {
    public void saveRecomanacions (int usuariId, HashMap<Integer,Vector<Pair<String, Double>>> Recomanacions);
    public HashMap<Integer,Vector<Pair<String, Double>>> readRecomanacions(int userId);
}
//Classe implementada per Marina Alapont