package Presentacio;
import java.io.FileNotFoundException;
import Excepcions.*;

import java.io.*;
import java.util.*;

import Domini.Pair;


public class VistaTerminal {
 
    private ControladorPresentacio ctrlP;
    private Scanner s;
    public VistaTerminal(ControladorPresentacio cp){
        ctrlP=cp;
        s = new Scanner(System.in);
    };

    //primera funcio de totes que s'executarà
    public void inicialitzaTerminal(){
        System.out.println("\n ##################### BENVINGUT AL SISTEMA RECOMANADOR ##################### \n");
        System.out.println("\nLa configuracio actual del programa es la seguent:");
        System.out.println(" Estrategia de Filtering: " + ctrlP.demanaEstrategiaActual());
        System.out.println(" Estrategia per al calcul de les distancies: "+ ctrlP.demanaDistanciaActual());
        System.out.println(" Parametre k del k-means: " + ctrlP.demanaKkmeans());
        System.out.println(" Parametre k del k-nearest: " + ctrlP.demanaKknearest() + "\n");
        //calcula clusters. 

        repFitxers();
        preguntaMiniMax();
        System.out.println("\nS'esta calculant els clusters per l'estrategia Collaborative Filtering. En qualsevol moment podra recalcular-los escollint l'opcio corresponent al menu.");
        try {
            ctrlP.recalculaClusters();
        } catch (CoordenadesNoValides e1) { //No hauria de passar mai, ja que kmweans s'assegura de passar coordenades valides
            System.out.println("ERROR: "+e1.toString());
        }

        System.out.println("\nA continuacio pot observar el menu del nostre sistema.");
        mostraMenu(); 
        esperaseleccio();

    }

    public void escullAltresFuncions(){
        System.out.print("\n\nVol dur a terme alguna altre funcionalitat? Entri 's' per si, entri qualsevol altre cosa en cas contrari \n");
        String resp= s.next();
        if (resp.equals("s")){
            mostraMenu();
            esperaseleccio();
        }
    }

    public void repFitxers(){
        System.out.println("\nIntrodueixi, un per un, en aquest ordre i en linies diferents, el nom sense l'extensio .csv dels fitxers seguents: \n Fitxer amb els items \n Fitxer amb els ratings \n Fitxer de test amb dades conegudes ('Known') \n Fitxer de test amb dades desconegudes ('unknown')");
        repPrimerFitxer();
        repSegonFitxer();
        repTercerFitxer();
        repQuartFitxer();
    }

    public void repPrimerFitxer() {
        System.out.println("\nEntra el nom del primer fitxer esmentat anteriorment");
        String e= s.nextLine();
        try {
            ctrlP.informaFitxerItems(e);
        } catch (FileNotFoundException e1) {
            System.out.println("ERROR: El fitxer no existeix"); //Aixo no ha de passar mai aqui, pero com que l'excepcio salta a Valoracio s'ha de capturar sempre que s'usi la classe
           repPrimerFitxer();
        }
    }

    public void repSegonFitxer() {
        System.out.println("\nEntra el nom del segon fitxer esmentat anteriorment");
        String e= s.nextLine();
        try {
            ctrlP.informaFitxerRatings(e);
        } catch (FileNotFoundException e1) {
            System.out.println("ERROR: El fitxer no existeix.");
            repSegonFitxer();
        }
        catch (PuntuacioNoValida e3) {
            System.out.println("ERROR: La puntuacio no es valida."); //Aixo no ha de passar mai aqui, pero com que l'excepcio salta a Valoracio s'ha de capturar sempre que s'usi la classe

        }
        catch (Exception e2) {
            System.out.println("ERROR: Error en el sistema.");

        }
    }

    public void repTercerFitxer() {
        System.out.println("\nEntra el nom del tercer fitxer esmentat anteriorment");
        String e= s.nextLine();
        try {
            ctrlP.informaFitxerKnown(e);
        } catch (FileNotFoundException e1) {
            System.out.println("ERROR: El fitxer no existeix");
            repTercerFitxer();
        }
        catch (PuntuacioNoValida e3) {
            System.out.println("ERROR: La puntuació no es valida."); //Aixo no ha de passar mai aqui, pero com que l'excepcio salta a Valoracio s'ha de capturar sempre que s'usi la classe

        }
        catch (Exception e2) {
            System.out.println("ERROR: Error en el sistema.");

        }
    }

    public void repQuartFitxer() {
        System.out.println("\nEntra el nom del quart fitxer esmentat anteriorment");
        String e= s.nextLine();
        try {
            ctrlP.informaFitxerUnKnown(e);
        } catch (FileNotFoundException e1) {
            System.out.println("ERROR: El fitxer no existeix");
            repQuartFitxer();
        }
        catch (PuntuacioNoValida e3) {
            System.out.println("ERROR: La puntuació no es valida."); //Aixo no ha de passar mai aqui, pero com que l'excepcio salta a Valoracio s'ha de capturar sempre que s'usi la classe

        }
        catch (Exception e2) {
            System.out.println("ERROR: Error en el sistema.");
        }
    }

    public void preguntaMiniMax(){
        Double minim = ctrlP.demanaMin();
        Double maxim = ctrlP.demanaMax();
        System.out.println("\nS'ha trobat, a traves dels fitxers entrats, que el rang de puntuacio va de: "+minim+ " a: "+maxim);
        System.out.println("Son aquestes dades correctes? En cas afirmatiu entri 's', en cas negatiu entri qualsevol alte cosa");
        String entrada = s.next();
        if(!entrada.equals("s")){
            entraMinMax();
        }

    }

    public void entraMinMax(){
        System.out.println("\nEntri els dos nombres corresponents al minim i al maxim del rang de valors de puntuacio, en linies diferents i en aquest ordre.");
        double min = s.nextDouble();
        double max = s.nextDouble();
        try {
            ctrlP.canviaMiniMax(min, max);
        } catch (RangNoValid e) {
            System.out.println("ERROR: "+ e.toString());
            entraMinMax();
        }
    }

    public void mostraMenu(){
        System.out.println("\n0. Informacio sobre cada opcio del menu \n");
        System.out.println("1. Demanar recomanacio per un usuari \n");
        System.out.println("2. Demanar recomanacio per un usuari i per uns items donats \n");
        System.out.println("3. Comprovar la qualitat de una o mes recomanacions\n");
        System.out.println("4. Consultar i/o canviar l'estrategia de recomanacio\n");
        System.out.println("5. Consultar i/o canviar l'estrategia del calcul de distancies\n");
        System.out.println("6. Consultar i/o canviar els parametres k dels algorismes\n"); 
        System.out.println("7. Recalcular clusters k-means\n");
        System.out.println("8. Sortir");

    }

    void esperaseleccio(){
        System.out.println("\nEsculli una funcionalitat del menu a dur a terme, indicant a la terminal el seu numero corresponent");
        int ent = s.nextInt();
        if(ent==0) MesInfo();
        else if (ent ==1) escollit1();
        else if (ent ==2) escollit2();
        else if (ent ==3) escollit3();
        else if (ent ==4) escollit4();
        else if (ent ==5) escollit5();
        else if (ent==6) escollit6();
        else if (ent==7) escollit7();
        else if (ent==8) escollit8();
        else { 
            System.out.println("\nERROR: L'entrada no correspon amb una opcio valida");
            esperaseleccio();
        }

    }

    public void MesInfo(){
        System.out.println("\nHa escollit rebre mes informacio sobre les diferents opcions del menu");
        System.out.println("\n1. Demanar recomanacio per un usuari. \nAquesta opcio permet demanar una recomanacio per a un usuari concret indicant el seu identificador (que es el nom d'usuari) i el nombre d'items que es vol a la sortida." + 
        "\nCal indicar aquests parametres en linies diferents, cal que l'usuari existeixi i que el nombre d'items que es vol a la sortida sigui menor o igual al nombre d'items que consta en el sistema."+
        "\nLa recomanacio es dura a terme amb les estrategies configurades en aquell moment i amb els parametres k establerts en aquell moment."+
        "\nLa recomanacio estudiara tots els items del sistema.");
        System.out.println("\n2. Demanar recomanacio per un usuari i per uns items donats.\n"+
        "Aquesta opcio permet demanar una recomanacio per a un usuari concret i tinguent com a items d'estudi un conjunt concret."+
        "\nCal indicar l'identificador del usuari (que es el nom d'usuari) i el nombre d'items que es vol a la sortida, el nombre d'items que s'entrara i finalment els ids "+
        "d'aquests items."+ 
        "\nCal indicar tots aquests parametres en linies diferents, cal que l'usuari existeixi, que el nombre d'items que es vol a la sortida sigui menor o igual al nombre"+
        "d'items que s'entra, i a la vegada que aquest nombre d'items entrats sigui menor o igual al total d'items, i finalment que aquests items entrats existeixin."+
        "\nLa recomanacio es dura a terme amb les estrategies configurades en aquell moment i amb els parametres k establerts en aquell moment."+
        "\nLa recomanacio estudiara nomes els items entrats.");
        System.out.println("\n3. Comprovar la qualitat de una o mes recomanacions\n"+
        "Aquesta opcio permet provar la qualitat de les recomanacions i per tant provar el funcionament dels algorismes, usant com entrada un fitxer al que hem anomenat"+
        "inputqueries.txt. La terminal especifica quins requisits ha de tenir aquest fitxer i demana que s'entri el nom del fitxer amb l'extensio inclosa per poder llegir-lo."+
        "\nBasicament el que es fa es demanar n recomanacions per n usuaris diferents del estil recomanacions de l'opcio 2 del menu. Per aquestes recomanacions es calcula"+
        " un DCG promig, que indica lo be que s'ha ordenat en mitjana els items a predir."+
        "\nAquesta funcionalitat necessita les dades de known i les de unknown, de manera que els usuaris entrats per demanar les recomanacions pertanyen a aquests dos fitxers,"+
        "i la resta d'informacio a entrar es coherent tambe amb aquests fitxers."+
        "El fitxer ha de seguir el format: \nnusuaris a demanar recomanacions \n(per cada usuari de nusuaris:) \nidUsuari numValoracionsConegudes numValoracionsDesconegudes numItemsARetornar"+
        "\ntantes linies amb parelles <idItem puntuacio> com numValoracionsConegudes"+
        "\ntantes linies amb iditem com numValoracionsDesconegudes"+
        "\nPots trobar un exemple del fitxer inputqueries al directori DATA.");

        System.out.println("\n4. Consultar i/o canviar l'estrategia de recomanacio\n"+
        "Aquesta opcio permet consultar quina estrategia de filtering hi ha configurada en aquell moment, i si es vol, permet canviar l'estrategia.\n"+
        "\n5. Consultar i/o canviar l'estrategia del calcul de distancies\n"+
        "Aquesta opcio permet consultar quina estrategia de calcul de distancies entre usuaris hi ha configurada en aquell moment, i si es vol, permet canviar l'estrategia.\n"+
        "\n6. Consultar i/o canviar els parametres k dels algorismes\n"+
        "Aquesta opcio permet consultar quins parametres k hi ha configurats en aquell moment per als alogrismes, i si es vol, permet canviar aquests valors.");
        System.out.println("\n7. Recalcular clusters k-means\n"+
        "Aquesta opcio recalcula els clusters de k-means, usant el valor k configurat en aquell moment. El calcul de les particions no es fa per cada cop que es demana"+
        "una recomanacio ja que es bastant costos. Es per aixo que sempre que es vulgui canviar els clusters i recalcular-los es pot escollir aquesta opcio. Els clusters"+
        "es calculen per primer cop al inici d'execucio del programa.\n"+
        "\n8. Sortir\n"+
        "Permet abandonar el sistema recomanador i acabar l'execucio.");
        escullAltresFuncions();
    }

    public void indicaTecnincaRecomanacio(){
        String estrategia=ctrlP.demanaEstrategiaActual();
        System.out.println("\nL'estrategia actual que usa el sistema es: " + estrategia + ". Si vol canviar l'estrategia entri 's' en la terminal, 'n' en cas contrari");
        String resp = s.next();
        if (resp.equals("s")) canviaEstrategia();
        else if (!resp.equals("n") && !resp.equals("s")) {
            System.out.println("ERROR: L'entrada no es valida");
            indicaTecnincaRecomanacio();
        }
    }

    public void canviaEstrategia(){
        String estrategia=ctrlP.demanaEstrategiaActual();
        System.out.println("\nEscull d'entre les seguents opcions, escribint a la terminal el numero de l'opcio escollida\n 1. Collaborative Filtering\n 2.Content Based Filtering \n ");
        int eleccio = s.nextInt();
        if (eleccio ==1) {
            if (estrategia.equals("CollaborativeFiltering")) System.out.println("Aquesta estrategia ja es l'actual");
            else ctrlP.demanaCanviEstrategia("CollaborativeFiltering");
        }
        else if (eleccio ==2) {
            if (estrategia.equals("ContentBasedFiltering")) System.out.println("Aquesta estrategia ja es l'actual");
            else ctrlP.demanaCanviEstrategia ("ContentBasedFiltering");
        } 
        else { 
            System.out.println("ERROR: L'opcio escollida no es cap de les valides");
            canviaEstrategia();
        }
    }

    public void indicaTecnincaDistancies(){
        String estrategia=ctrlP.demanaDistanciaActual();
        System.out.println("\nL'estrategia actual que usa el sistema per al calcul de les distancies es: " + estrategia + ". Si vol canviar l'estrategia entri 's' en la terminal, 'n' en cas contrari");
        String resp = s.next();
        if (resp.equals("s")) canviaDistancies();
        else if (!resp.equals("n") && !resp.equals("s")) {
            System.out.println("ERROR: L'entrada no es valida");
            indicaTecnincaDistancies();
        }
    }

    public void canviaDistancies(){
        String estrategia=ctrlP.demanaDistanciaActual();
        System.out.println("\nEsculli d'entre les seguents opcions, escribint a la terminal el numero de l'opcio escollida\n 1. Distancia basica\n 2.Distancia Euclidiana \n ");
        int eleccio = s.nextInt();
        if (eleccio ==1) {
            if (estrategia.equals("Distancia Basica")) System.out.println("Aquesta estrategia ja es l'actual");
            else ctrlP.demanaCanviDistancia("Distancia Basica");
        }
        else if (eleccio ==2) {
            if (estrategia.equals("Distancia Euclidiana")) System.out.println("Aquesta estrategia ja es l'actual");
            else ctrlP.demanaCanviDistancia ("Distancia Euclidiana");
        } 
        else { 
            System.out.println("ERROR: L'opcio escollida no es cap de les valides");
            canviaDistancies();
        }
    }

    public void indicaParametresK(){
        int k1=ctrlP.demanaKkmeans();
        int k2=ctrlP.demanaKknearest();
        System.out.println("\nEls parametres k actuals que usa el sistema son: \n Per a kmeans: " + k1 + "\n Per a knearest: " + k2 +"\nSi vol canviar l'estrategia entri 's' en la terminal, 'n' en cas contrari");
        String resp = s.next();
        if (resp.equals("s")) canviaParametresK();
        else if (!resp.equals("n") && !resp.equals("s")) {
            System.out.println("ERROR: L'entrada no es valida");
            indicaParametresK();
        }
    }

    public void canviaParametresK(){
        System.out.println("\nEsculli d'entre les seguents opcions, escribint a la terminal el numero de l'opcio escollida, quin dels dos parametres vol canviar\n 1.K de k-means\n 2.K de k-nearest \n ");
        int eleccio = s.nextInt();
        if (eleccio ==1) {
           System.out.println("Entri el nou nombre per al parametre k");
           int e = s.nextInt();
           try {
             ctrlP.canviaKKmeans(e);
           } catch (KMassaGran e1) {
                System.out.println("ERROR: " + e1.toString());
               canviaParametresK();
            }
            try {
              ctrlP.recalculaClusters();
            } catch (CoordenadesNoValides e1) { //No hauria de passar mai, ja que kmweans s'assegura de passar coordenades valides
            System.out.println("ERROR: "+e1.toString());
            }
        }
        else if (eleccio ==2) {
            System.out.println("Entri el nou nombre per al parametre k");
            int e = s.nextInt();
            try {
                ctrlP.canviaKKnearest(e);
            } catch (KMassaGran e1) {
                System.out.println("ERROR: " + e1.toString());
                canviaParametresK();
            }
        } 
        else { 
            System.out.println("ERROR: L'opcio escollida no es cap de les valides");
            canviaParametresK();
        }
    }



    public void escollit1(){ 
        System.out.println("Ha escollit l'opcio 1\n");
        System.out.println("Indiqui en linies diferents l'identificador de l'usuari per al que vol la recomanacio i el nombre d'items recomenats que desitja a la sortida");
        System.out.println("Pot trobar un exemple d'entrada en el fitxer 'README' \n" );
        
        String idUsuari= s.next();
        int nItemsSortida = s.nextInt(); //mirar que realment sigui un numero! --> potser amb java.util.InputMismatchException. si fica una lletra peta sol perjava, pero podem agafar l'excepcio per fer un cout mes entenedor?
        Vector<String> resultat = new Vector<String>();
        try{
        resultat = ctrlP.informaPeticio1(idUsuari,nItemsSortida); 
        }
        catch(UsuariNoExisteix e){
           System.out.println("ERROR: " + e.toString());
        }
        catch(NombreItemsMassaGran e){
            System.out.println("ERROR: No hi ha tants items no valorats com el nombre introduit d'items que es vol com a resultat." +e.toString());
        }
        catch(PuntuacioNoValida e){
            System.out.println("ERROR: "+ e.toString());
        }
        catch (Exception e){
            System.out.println("S'ha trobat un error en el sistema");
        }
        for (String i : resultat){
            System.out.print(i+" ");
        }
        escullAltresFuncions();
    }

    public void escollit2(){ //mirar de controlar el error de que el usuari no existeix, de que el nombre d'items de la sortida que vols sigui mes petit o igual al nombre de items que entres i de que els items no els te valorats. A ctrl domini
        System.out.println("Ha escollit l'opcio 2\n");
        System.out.println("\nIndiqui en linies diferents l'identificador de l'usuari per al que vol la recomanacio, el nombre d'items recomenats que desitja que es retorni, el nombre d'items que entrara i tambe en linies diferents indiqui els id dels items sobre els que operar");
        System.out.println("Pot trobar un exemple d'entrada en el fitxer 'README' \n" );
        String idUsuari= s.next();
        int nItemsSortida = s.nextInt(); //comprovar que realment sigui int (mirar com es fa a escollit1)
        int nItems= s.nextInt(); //idem que per l'altre interface
        if (nItemsSortida>nItems) {
            System.out.println("ERROR: el numero d'items que es vol com a resultat de la recomanacio es major que el numero d'items del conjunt a valorar.");
            mostraMenu();
            esperaseleccio();
        }
        Vector<String> it= new Vector<String>();
        int cont=0;
        while (cont<nItems) {
            String read=s.next();
            it.add(read);
            ++cont;
        }
        Vector<String> resultat = new Vector<String>();
        try {
        resultat = ctrlP.informaPeticio2(idUsuari, it, nItemsSortida);
        }
        catch(UsuariNoExisteix e){
           System.out.println("ERROR: " + e.toString());
        }
        catch(NombreItemsMassaGran e){
            System.out.println("ERROR: No hi ha tants items no valorats com el nombre introduit d'items que es vol com a resultat." +e.toString());
        }
        catch(ItemNoExisteix e){
            System.out.println("ERROR: " + e.toString());
        }
        catch(ItemJaValorat e) {
            System.out.println("ERROR: " + e.toString());
        }
        catch(PuntuacioNoValida e){
            System.out.println("ERROR: "+ e.toString());
        }
        catch (Exception e){
            System.out.println("S'ha trobat un error en el sistema");
        }
        for (String i : resultat){
            System.out.print(i+" ");
        }
        escullAltresFuncions();
    }



    public void escollit3(){ //comprovar que el usuari existeix (i esta al fitxer known) que els items existeixen i no es ha valorat ja el usuari(i estan al fitxer unkown), que el nombre d'items a predir la valoracio es menor o igual al nombre de items no valorats per el usuari(de unknown) i que els nombres d'items que es vol a la sortida sigui menor o igual al nombre de items que s'entren
        System.out.println("Entri per la terminal el nom d'un fitxer que contingui el seguent: \n");
        System.out.println("\nCal indicar primerament el nombre d'usuaris per els quals es vol recomanacions \n");
        System.out.println("Per cada usuari cal entrar l'informacio seguent en una linia a part: \n");
        System.out.println("4 nombres en una mateixa linia; el primer ha de fer renferencia a l'identificador de l'usuari (tret del fitxer known) per el qual es vol la recomanacio,"+
                            " el seguent ha de ser el nombre de valoracions conegudes de l'usuari, seguit del nombre d'items amb valoracions desconegudes per l'usuari que entrara i finalment"+
                            " l'ultim nombre ha de ser el nombre d'items recomenats que vol que retorni el sistema \n");
        System.out.println("Seguit d'aquesta linia s'espera tantes linies com items valorats, compostes per l'identificador del item i la puntuacio que l'usuari li ha donat. Aquestes informacions s'han d'extreure del fitxer known \n");
        System.out.println("I finalment s'espera tantes linies com items no valorats per l'usuari que es troben al fitxer unknown, a cada linia s'hi ha  d'indicar l'identificador de l'item \n");
        System.out.println("Cal que ubiqui el fitxer a la carpeta 'DATA'. Pot trobar un exemple de l'entrada en el fitxer 'README' i un fitxer ja preparat anomentat 'inputqueries.txt', que es troba a la carpeta DATA. \n" );
        String file =s.next();
        try {
        llegeixInputQueries(file); 
        }
        catch(FileNotFoundException e1){
            System.out.println("ERROR: El fitxer no existeix o no es troba a la carpeta 'DATA'");
            escullAltresFuncions();

        }
        catch(UsuariNoExisteix e){
            System.out.println("ERROR: " + e.toString()+ " Comprovi que aquest usuari estigui realment en el fitxer de test");
        }
         catch(NombreItemsMassaGran e){
             System.out.println("ERROR: No hi ha tants items no valorats per l'usuari al fitxer de test 'unknown' com el nombre introduit d'items que es vol com a resultat." +e.toString());
         }
         catch(ItemNoExisteix e){
             System.out.println("ERROR: " + e.toString());
         }
          catch (ItemJaValorat e){
             System.out.println("ERROR: " + e.toString()+ "Comprovi que la parella (usuari, item) NO estigui tant a Known com a Unknown.");
         }
         
        catch(ItemNoEsDeUnknown e){
            System.out.println("ERROR: " + e.toString());
        }
    
        catch(PuntuacioNoValida e){
            System.out.println("ERROR: "+ e.toString());
        }

         catch (Exception e){
             System.out.println(e.toString());
         }

         escullAltresFuncions();
    }
 
    public void escollit4(){
        indicaTecnincaRecomanacio();
        escullAltresFuncions();
    }
    
    public void escollit5(){
        indicaTecnincaDistancies();
        escullAltresFuncions();
    }

    public void escollit6(){
        indicaParametresK();
        escullAltresFuncions();
    }

    public void escollit7(){
        try {
            ctrlP.recalculaClusters();
        } catch (CoordenadesNoValides e1) { //No hauria de passar mai, ja que kmweans s'assegura de passar coordenades valides
          System.out.println("ERROR: "+e1.toString());
        }
        System.out.println("S'han recalculat els clusters");
        escullAltresFuncions();
    }

    public void escollit8(){
        System.out.println("Gracies per usar el noestre sistema");
        System.exit(0);
    }


    public void llegeixInputQueries (String filename) throws Exception{
        LinkedList<String> dadesQueries = new LinkedList<String>();
        FileReader fr = null;
        try {
            fr = new FileReader("../../DATA/"+filename);       
        }
        catch(FileNotFoundException e1){
            System.out.println("El fitxer no existeix");
        }
        
        Scanner scan = new Scanner(fr);

        while(scan.hasNextLine()) dadesQueries.add(new String(scan.nextLine()));

        //Despres de emmagatzemar totes les dades
        int nUsuaris = Integer.valueOf(dadesQueries.get(0));  //Nombre de queries a processar

        Vector<String> UsuarisAValorar = new Vector<String>(nUsuaris); //vector amb tots els usuaris a valora_recomanacio
        Vector<Vector<String>> Items = new Vector<Vector<String>>(); //vector amb el conjunt de items a valorar. Items[i] conté tots els items a predir per UsuarisAValorar[i]
        Vector<Integer> nItemsARetornar = new Vector<Integer>(); //vector amb el nombre d'items a retornar que es vol per cada recomanacio. nItemsARetornar[i] correspont al nombre a retornar pe rla valoració sobre l'usuari UsuarisAValorar[i]
        for(int i = 1; i < dadesQueries.size(); i++){ //comencem x 1 pq la primera es el nº de queries
            
            //valors = { "userid, lengthknown, lengthunknown, Q"}  
            String valors[] = dadesQueries.get(i).split(" ");
            UsuarisAValorar.add(valors[0]);
            int lenghtknown = Integer.valueOf(valors[1]);   //Assignem el valor de lenght known
            int lengthunknown = Integer.valueOf(valors[2]); //Assingem el valor de lenght unknown
            int q = Integer.valueOf(valors[3]); //Assignem la Q (nombre de recomanacions a fer)
            nItemsARetornar.add(q);
            ++i; //avancem en la llista (\n)
            Vector<String> ItemsPredir = new Vector<String>(); 
   
            for (int j = 0; j < lenghtknown; ++j) ++i; //"llegim" totes les dades known, com que ja les tenim guardades, no les tractem
            for (int j = 0; j < lengthunknown; ++j){  //guardem quins son els items de Unknown que es vol predir la valoracio i calcular-ne la qualitat (podria ser que no fossin tots els de unknown per aquell usuari)
                ItemsPredir.add(dadesQueries.get(i)); 
                ++i;
            }
            --i;
            Items.add(ItemsPredir);
        }
        Pair <Vector<Vector<String>>,Double> result = new Pair<Vector<Vector<String>>,Double>();

        result = ctrlP.informaPeticio3(UsuarisAValorar, Items, nItemsARetornar);
        //nomes es fa System.out.println del DCG pero es retorna pair per si es vol que retorni tambe totes les recomanacions fetes. De moment s'ha tret per no saturar la terminal.
        System.out.println(result.second);
        escullAltresFuncions();
       
    }


}

//classe programada per Marina Alapont excepte la lectura de la funció llegeixInputQueris, feta pel Joel Cardona.