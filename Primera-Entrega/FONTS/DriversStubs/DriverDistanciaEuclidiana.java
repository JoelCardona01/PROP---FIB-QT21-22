package DriversStubs;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

import Domini.DistanciaEuclidiana;

public class DriverDistanciaEuclidiana {
    private DistanciaEuclidiana de = new DistanciaEuclidiana();
    
    public void getNomTest(){
        System.out.println("El nom de l'estrategia de calcul de distancia de vectors es "+de.getNom()+"\n");
    }
    
    public void distanciaTest(){
        
        Scanner sc = new Scanner(System.in);
        List<String> aux = new ArrayList<String>();

        System.out.println("Introdueix els elements (Doubles) del primer vector separat per espais");

        aux.add(sc.nextLine());
        String v1String[] = aux.get(0).split(" ");
        Vector<Double> v1 = new Vector<Double>(); 
        for(String a : v1String){
            v1.add(Double.valueOf(a));
        }
        
        System.out.println("Introdueix el nombre d'elements del segon vector");
        aux.add(sc.nextLine());
        String v2String[] = aux.get(1).split(" ");
        Vector<Double> v2 = new Vector<Double>(); 
        for(String a : v2String){
            v2.add(Double.valueOf(a));
        }

        Double result = de.distancia(v1, v2);
        System.out.println("La distancia euclidiana calculada es: " + result+ "\n");
    }
    public static void main(String[] args) {
        System.out.println("Driver de la clase distancia euclidiana");
        DriverDistanciaEuclidiana det = new DriverDistanciaEuclidiana();
        Scanner sc = new Scanner(System.in);
        int func = 0;
        while (func != 3){
            System.out.println("Selecciona una de les funcionalitats:\n1- Obte nom de l'estrategia\n2- Calcular distancia euclidiana de dos vectors de doubles\n3- Finalitzar el driver");
            func = sc.nextInt();
            switch(func){
                case 1: 
                    det.getNomTest();
                    break;
                case 2: 
                    det.distanciaTest();
                    break;
                case 3:
                    System.out.println("Finalizant driver");
                    sc.close();
                    break;
                default:
                    System.out.println("Funcionalitat equivocada");
                    break;
            }
        }

    }    
}
