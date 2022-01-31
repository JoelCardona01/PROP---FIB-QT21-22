package Domini.DataInterface;

import Domini.Model.Pair;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public interface CtrlRating {
    public List<String> getAll(String filename) throws FileNotFoundException;
    public void saveRatings(Vector<Integer> usrs, Vector<Integer> nVals, Vector<String> items, Vector<Double> puntuacio);
    public Map<Integer, Vector< Pair <String, Double> > > readRatings();
}

//Classe implementada per Joel Cardona