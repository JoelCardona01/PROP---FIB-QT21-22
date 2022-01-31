javac -cp .:lib/json-simple-1.1.1.jar --release 11 Excepcions/*.java;
javac -cp .:lib/json-simple-1.1.1.jar --release 11 Domini/ControladorsDomini/*.java ; 
javac -cp .:lib/json-simple-1.1.1.jar --release 11 Domini/DataInterface/*.java ; 
javac -cp .:lib/json-simple-1.1.1.jar --release 11 Domini/Model/*.java ; 
javac -cp .:lib/json-simple-1.1.1.jar --release 11 --module-path lib/javafx-sdk-17.0.1_Ubuntu/lib --add-modules javafx.controls,javafx.fxml Presentacio/Controladors/*.java;
javac -cp .:lib/json-simple-1.1.1.jar --release 11 --module-path lib/javafx-sdk-17.0.1_Ubuntu/lib --add-modules javafx.controls,javafx.fxml Presentacio/Main.java;
javac -cp .:lib/json-simple-1.1.1.jar --release 11 Dades/*.java ; 
mv Domini/Model/*.class ../EXE/CLASS/Domini/Model;
mv Domini/ControladorsDomini/*.class ../EXE/CLASS/Domini/ControladorsDomini;
mv Domini/DataInterface/*.class ../EXE/CLASS/Domini/DataInterface;
mv Presentacio/*.class ../EXE/CLASS/Presentacio;
mv Dades/*.class ../EXE/CLASS/Dades;
mv Presentacio/Controladors/*.class ../EXE/CLASS/Presentacio/Controladors
mv Excepcions/*.class ../EXE/CLASS/Excepcions;

