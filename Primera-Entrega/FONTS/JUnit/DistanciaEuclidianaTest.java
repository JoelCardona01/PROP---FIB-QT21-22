package JUnit;
import Domini.DistanciaEuclidiana;
import Domini.StrategyDistancia;

import java.util.Vector;

import java.io.*;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runners.*;

public class DistanciaEuclidianaTest {
	
	@Test
    public void distanciaVectorsRandom() {
        DistanciaEuclidiana DistanciaEuclidiana = new DistanciaEuclidiana();
        Vector<Double> a1 = new Vector<Double>();
        Vector<Double> a2 = new Vector<Double>();
        a1.add(3.5); a1.add(5.1); a1.add(2.9);
        a2.add(1.0); a2.add(3.6); a2.add(5.6);
        
        Double result = DistanciaEuclidiana.distancia(a1, a2);

        result = Math.round(result*100.0)/100.0; //Arrodonim a 2 decimals
        assertEquals(3.97, result, 0);
    }

    @Test 
    public void distanciaElementsIguals() {
        DistanciaEuclidiana DistanciaEuclidiana = new DistanciaEuclidiana();
        Vector<Double> a1 = new Vector<Double>();
        Vector<Double> a2 = new Vector<Double>();

        a1.add(18.0); 
        a2.add(18.0);
        
        Double result = DistanciaEuclidiana.distancia(a1, a2);

        assertEquals(0.0, result,0);

    }

    @Test 
    public void distanciaElementsIgualsDesordenats() {
        DistanciaEuclidiana DistanciaEuclidiana = new DistanciaEuclidiana();
        Vector<Double> a1 = new Vector<Double>();
        Vector<Double> a2 = new Vector<Double>();

        a1.add(5.0); a1.add(2.0);
        a2.add(2.0); a2.add(5.0);
        
        Double result = DistanciaEuclidiana.distancia(a1, a2);
        result = Math.round(result*100.0)/100.0; //Arrodonim a 2 decimals

        assertEquals(4.24, result,0);

    }
    
    @Test
    public void distanciaVectorsBuits() {
        DistanciaEuclidiana DistanciaEuclidiana = new DistanciaEuclidiana();
        Vector<Double> a1 = new Vector<Double>();
        Vector<Double> a2 = new Vector<Double>();

        
        Double result = DistanciaEuclidiana.distancia(a1, a2);
        assertNull(result);
    }
    
    @Test
    public void distanciaVectorsEspaisDiferents() {
        DistanciaEuclidiana DistanciaEuclidiana = new DistanciaEuclidiana();
        Vector<Double> a1 = new Vector<Double>();
        Vector<Double> a2 = new Vector<Double>();

        a1.add(3.0);

        Double result = DistanciaEuclidiana.distancia(a1, a2);
        assertNull(result);
    }

}
//Classe implentada per Joel Cardona