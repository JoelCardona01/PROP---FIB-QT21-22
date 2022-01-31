package DriversStubs;
import Domini.Admin;

import java.util.Scanner;

public class DriverAdmin {
    private Admin prova;

    public void TestCreadoraAdmin(){
        Scanner sc = new Scanner(System.in);
        String nom = sc.nextLine();
        String contra = sc.nextLine();
        prova = new Admin(nom, contra);
        System.out.println("S'ha creat una nova instancia de la classe Admin");
    }

    public void TestgetUsuari(){
        String result = prova.getUsuari();
        System.out.println("L'usuari es diu " + result+"\n");
    }

    public void TestgetConstrasenya(){
        String result = prova.getConstrasenya();
        System.out.println("La contrassenya es "+ result+"\n");
    }

    public void TestcanviaNom(){
        System.out.println("Introdueix el nom nou");
        Scanner sc = new Scanner(System.in);
        String nom = sc.nextLine();
        prova.canviaNom(nom);
        System.out.println("S'ha canviar el nom de l'administrador\n");
    }

    public void TestcanviaContrasenya(){
        System.out.println("Introdueix la nova contrasenya");
        Scanner sc = new Scanner(System.in);
        String nom = sc.nextLine();
        prova.canviaContrasenya(nom);
        System.out.println("S'ha canviar la contrasenya de l'administrador\n");
    }


    public static void main(String[] args) {
        DriverAdmin da = new DriverAdmin();
        System.out.println("Estas provant el driver de la classe Admin\n"); 
        System.out.println("Abans de provar la classe anem a crear un nou Administrador. Indica un usuari i una contrassenya en linies diferents");
        da.TestCreadoraAdmin();
        int func = 0;
        while(func != 5){
            System.out.println("Escull una de les funcionalitats a provar:\n"+
            "1- Test getUsuari\n2- Test getContrasenya\n3- Canviar nom de l'administrador\n4- Canviar contrasenya\n5- Finalitzar el driver");
            Scanner sc = new Scanner(System.in);
            func = sc.nextInt();
            

            switch (func){
                case 1:
                    da.TestgetUsuari();
                    break;
                
                case 2: 
                    da.TestgetConstrasenya();
                    break;
                case 3:
                    da.TestcanviaNom();
                    break;

                case 4:
                    da.TestcanviaContrasenya();
                    break;
                case 5:
                    System.out.println("Finalitzant el driver");
                    sc.close();
                    break;

                default:
                    System.out.println("La funcionalitat escollida no es correcta.");
                    break;
            }
        }
    }
}
//Driver implementat per Joel Cardona