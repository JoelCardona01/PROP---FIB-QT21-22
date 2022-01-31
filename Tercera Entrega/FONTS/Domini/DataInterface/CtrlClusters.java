package Domini.DataInterface;

import java.util.HashMap;
import java.util.Vector;

public interface CtrlClusters {
    public void saveClusters(Vector<Vector<Integer>> clusters);
    public void saveCentroides(HashMap<String, Double>[] centoids);
    public Vector<Vector<Integer>> readClusters();
    public HashMap<String, Double>[] readCentoides();

}
//Classe implementada per Joel Cardona