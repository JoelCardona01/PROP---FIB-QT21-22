package DriversStubs;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

import ControladorsDomini.CtrlDomini;
import Dades.CtrlItemFile;
import Dades.CtrlRatingsFile;
import Domini.DistanciaBasica;
import Domini.DistanciaEuclidiana;
import Domini.Item;
import Domini.UsuariActiu;
import Domini.Valoracio;
import Excepcions.ExcepcionsRecomanador;
import Excepcions.PuntuacioNoValida;

public class DriverComplet {

    private CtrlDomini cd = CtrlDomini.getInstance();
    

    public static void main(String[] args) throws Exception{
        System.out.println("Driver complert\nIntrodueix el nom del fitxer (Situat al directori DATA del projecte) amb la extensio de les comandes a executar, tens el fitxer 'TutorialDriver.txt' amb totes les comandes possibles\n");
        DriverComplet dC = new DriverComplet();
        dC.cd.inicialitzar();

        Scanner sc = new Scanner(System.in);
        String filename = sc.nextLine();    //Llegeixo l'adreca del fitxer
        FileReader fr = new FileReader("../../DATA/"+filename);
        Scanner scan = new Scanner(fr);
        LinkedList<String> comandes = new LinkedList<String>();

        while(scan.hasNextLine()) comandes.add(new String(scan.nextLine()));   //Per cada linea de comandes s'hi afegeix a la llista amb totes les comandes
        for (String i : comandes){
            String array[] = i.split(" "); //Separem les diferents comandes en una array.
            switch(array[0]){
                case "readItems":
                    dC.TestLecturaItems(array[1]); break;
                case "readRatings":
                    dC.TestLecturaRatings(array[1]); break;
                case "readKnown":    
                    dC.TestLecturaKnown(array[1]); break;
                case "readUnknown":    
                    dC.TestLecturaUnknown(array[1]); break;
                case "recomanacioContentBased":
                    if(array.length <= 3)    dC.TestRecomanacioContentBased(array[1], Integer.valueOf(array[2]));
                    else dC.TestRecomanacioContentBased(array); break;
                case "recomanacioCollaborative":
                    if(array.length <= 3)    dC.TestRecomanacioCollaborative(array[1], Integer.valueOf(array[2]));
                    else dC.TestRecomanacioCollaborative(array); break;
                case "newUser":
                    dC.TestcreaUsuari(array[1], array[2]); break;
                case "getUser":
                    dC.TestgetUsuari(array[1]); break;
                case "getValoracions":
                    dC.TestgetValoracions(array[1]); break;
                case "newValoracio":
                    dC.TestcreaValoracio(array[1], array[2], Double.valueOf(array[3])); break;
                case "modificarValoracio":
                    dC.TestmodificarValoracio(array[1], array[2], Double.valueOf(array[3])); break;
                case "distanciaBasica":
                    dC.TestdistanciaBasica(array); break;
                case "distanciaEuclidiana":
                    dC.TestdistanciaEuclidiana(array); break;
                case "exit":
                    System.out.println("Finalitzant el driver..."); break;
            }
        }
    }

    public void TestLecturaItems(String filename){
        System.out.println("S'esta provant la lectura del fixter ITEMS:");
        Boolean haFallat = false;
        try{
            cd.llegeixAllItems(filename);
        }
        catch (FileNotFoundException e){
            System.out.println("El fitxer items introudit no exiteix\n");
            haFallat = true;
        }
        if (!haFallat) System.out.println("S'ha llegit el fitxer Items correctament\n");
    }

    public void TestLecturaRatings(String filename) throws Exception{
        System.out.println("S'esta provant la lectura del fitxer "+filename+" del estil ratings");
        cd.informaRatings(filename);
        System.out.println("S'ha llegit el fitxer correctament\n");
    }

    public void TestLecturaKnown(String filename) throws Exception{
        System.out.println("S'esta provant la lectura del fitxer Known");
        cd.informaKnown(filename);
        System.out.println("S'ha llegit el fitxer correctament\n");
    }

    public void TestLecturaUnknown(String filename) throws Exception{
        System.out.println("S'esta provant la lectura del fitxer Unknown");
        cd.informaUnknown(filename);
        System.out.println("S'ha llegit el fitxer correctament\n");
    }

    public void TestRecomanacioContentBased(String nomUsuari, int nElements) throws ExcepcionsRecomanador{
        System.out.println("S'esta provant la recomanacio amb content based:");
        cd.canviaEstrategia("ContentBasedFiltering");
        Vector<String> recom = cd.demanaRecomanacio1(nomUsuari, nElements);
        System.out.println("Els items retornats amb content based filtering son: ");
        for (String item : recom){
            System.out.print(item+" ");
        }
        System.out.println("\n");
    }

    public void TestRecomanacioContentBased(String parametres[]) throws ExcepcionsRecomanador{
        System.out.println("S'esta provant la recomanacio amb content based:");
        cd.canviaEstrategia("ContentBasedFiltering");
        Vector<String> itemsDonats = new Vector<String>();
        for(int i = 3; i < parametres.length; ++i) itemsDonats.add(parametres[i]);
        int nElements = Integer.valueOf(parametres[2]);
        String username = parametres[1];
        Vector<String> recom = cd.demanaRecomanacio2(username, itemsDonats,nElements);
        System.out.println("Els items retornats amb content based filtering son: ");
        for (String item : recom){
            System.out.print(item+" ");
        }
        System.out.println("\n");
    }

    public void TestRecomanacioCollaborative(String nomUsuari, int nElements) throws ExcepcionsRecomanador{
        System.out.println("S'esta provant la recomanacio amb collaborative filtering:");
        cd.canviaEstrategia("CollaborativeFiltering");
        cd.recalculaClusters();
        Vector<String> recom = cd.demanaRecomanacio1(nomUsuari, nElements);
        System.out.println("Els items retornats aplicant collaborative filtering son: ");
        for (String item : recom){
            System.out.print(item+" ");
        }
        System.out.println("\n");
    }

    public void TestRecomanacioCollaborative(String parametres[]) throws ExcepcionsRecomanador{
        System.out.println("S'esta provant la recomanacio amb collaborative filtering:");
        cd.canviaEstrategia("CollaborativeFiltering");
        cd.recalculaClusters();
        

        Vector<String> itemsDonats = new Vector<String>();
        for(int i = 3; i < parametres.length; ++i) itemsDonats.add(parametres[i]);
        int nElements = Integer.valueOf(parametres[2]);
        String username = parametres[1];
        Vector<String> recom = new Vector<String>();
        try{ 
            recom = cd.demanaRecomanacio2(username, itemsDonats,nElements);
        }
        catch (ExcepcionsRecomanador e){
            System.out.println("que vulguis");
        }
        System.out.println("Els items retornats aplicant collaborative filtering son: ");
        for (String item : recom){
            System.out.print(item+" ");
        }
        System.out.println("\n");
    }
    
    public void TestcreaUsuari(String usr, String psw){
        System.out.println("S'esta creant el nou usuari");
        if(cd.getUsuari(usr) != null) System.out.println("Ja existeix un usuari amb username "+usr+"\n");
        else{
            UsuariActiu ua = new UsuariActiu(usr, psw);
            cd.afegirUsuari(ua);
            System.out.println("S'ha afegit l'usuari al controlador de domini\n");
        }
    }

    public void TestgetUsuari(String username){
        System.out.println("Buscant l'usuari amb username "+username);
        UsuariActiu ua = cd.getUsuari(username);
        if(ua == null)System.out.println("No exiteix cap usuari amb username "+username+"\n");
        else System.out.println("S'ha trobat l'usuari amb nom "+ua.getUsuari()+"\n");
    }

    public void TestgetValoracions(String username){
        System.out.println("Provant la funcionalitat de obtenir les valoracions que ha realitzat l'usuari "+username+"\nLes seves valoracions son les seguents:");
        UsuariActiu ua = cd.getUsuari(username);
        Map<String, Valoracio> a = ua.getValoracions();
        if(a.size() == 0) System.out.println("L'usuari amb id "+ua.getUsuari()+" no ha realitzat cap valoracio");
        for (HashMap.Entry<String, Valoracio> u : a.entrySet()) {
            System.out.print("Item "+u.getValue().getIdItem()+" nota "+u.getValue().getPuntuacio()+" ");
        }
        System.out.println("\n");
    }

    public void TestcreaValoracio(String username, String itemId, Double nota) throws PuntuacioNoValida{
        System.out.println("S'esta creant la valoracio amb l'usuari "+username+" i l'item "+itemId);
        Item it = cd.getItem(itemId);
        UsuariActiu ua = cd.getUsuari(username);
        Valoracio val = new Valoracio(nota, it, ua);
        ua.afegirValoracio(val);
        it.afegirValoracio(val);
        System.out.println("S'ha creat la nova valoracio amb els parametres anteriors i s'ha asociat amb el usuari i el item\n");
    }

    public void TestmodificarValoracio(String usr, String itemId, Double notanova) throws ExcepcionsRecomanador{
        System.out.println("S'esta intentant modificar la nota de la valoracio feta per l'usuari "+usr+" a l'item "+itemId);
        if(cd.getUsuari(usr) == null) System.out.println("L'usuari "+usr+ " no existeix!");
        else{
            if(cd.getItem(itemId) == null) System.out.println("L'item amb id "+itemId+" no exiteix!\n");
            else{
                UsuariActiu ua = cd.getUsuari(usr);
                ua.modificaValoracio(itemId, notanova);
                System.out.println("La valoracio s'ha modificat correctament\n");
            }
        }
    }

    public void TestdistanciaBasica(String parametres[]){
        System.out.println("S'esta provant el calcul de la distancia basica");
        int nE1 = Integer.valueOf(parametres[1]);   
        Vector<Double> v1 = new Vector<Double>();
        Vector<Double> v2 = new Vector<Double>();
        for (int i = 2; i < nE1+2; ++i){
            v1.add(Double.valueOf(parametres[i]));
        }
        for(int i = 2+nE1; i < nE1+2+nE1; i++){
            v2.add(Double.valueOf(parametres[i]));
        }
        DistanciaBasica db = new DistanciaBasica();
        Double result = db.distancia(v1, v2);
        System.out.println("La distancia calculada es "+result+"\n");
    }

    public void TestdistanciaEuclidiana(String parametres[]){
        System.out.println("S'esta provant el calcul de la distancia euclidiana");
        int nE1 = Integer.valueOf(parametres[1]);   
        Vector<Double> v1 = new Vector<Double>();
        Vector<Double> v2 = new Vector<Double>();
        for (int i = 2; i < nE1+2; ++i){
            v1.add(Double.valueOf(parametres[i]));
        }
        for(int i = 2+nE1; i < nE1+2+nE1; i++){
            v2.add(Double.valueOf(parametres[i]));
        }
        DistanciaEuclidiana de= new DistanciaEuclidiana();
        Double result = de.distancia(v1, v2);
        System.out.println("La distancia calculada es "+result+"\n");
    }
}
//Clase implementada per Joel Cardona
