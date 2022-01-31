package Domini.DataInterface;

import Domini.Model.Pair;

public interface CtrlEstat {
    public void saveState(int kkmeans, int knearests, String estrategiaDistanica, String estrategiaFiltering, Double puntmin, Double puntmax, int ultimUserId, int ultimItemId, int ultimIDRecomanacio);
    public Pair<Integer, Integer> getKsBD();
    public Pair<String, String> getEstrategiesBD();
    public Pair<Double, Double> getIntervalPuntuacionsBD();
    public int getUltimID();
    public int getUltimIDItem();
    public int getUltimIDRecomanacio();

}
//Classe implementada per Marina Alapont i Joel Cardona