package Domini.DataInterface;

import Domini.Model.Pair;


import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public interface CtrlItem{
    public List<String> getAll(String filename) throws FileNotFoundException;
    public void saveItems(Map<String, Pair<Vector<String>, Vector<Vector<String> > > > items);
    public Map<String, Pair<Vector<String>, Vector<Vector<String> > > > readItems();
}
//Classe implementada per Joel Cardona