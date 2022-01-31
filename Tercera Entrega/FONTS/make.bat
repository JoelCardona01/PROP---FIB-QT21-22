javac -cp .;lib/json-simple-1.1.1.jar --release 11 Excepcions/*.java
javac -cp .;lib/json-simple-1.1.1.jar --release 11 Domini/ControladorsDomini/*.java 
javac -cp .;lib/json-simple-1.1.1.jar --release 11 Domini/DataInterface/*.java 
javac -cp .;lib/json-simple-1.1.1.jar --release 11 Domini/Model/*.java 
javac -cp .;lib/json-simple-1.1.1.jar --release 11 --module-path lib/javafx-sdk-17.0.1/lib --add-modules javafx.controls,javafx.fxml Presentacio/Controladors/*.java
javac -cp .;lib/json-simple-1.1.1.jar --release 11 --module-path lib/javafx-sdk-17.0.1/lib --add-modules javafx.controls,javafx.fxml Presentacio/Main.java
javac -cp .;lib/json-simple-1.1.1.jar  --release 11 Dades/*.java 
move Domini\Model\*.class ..\EXE\CLASS\Domini\Model
move Domini\ControladorsDomini\*.class ..\EXE\CLASS\Domini\ControladorsDomini
move Domini\DataInterface\*.class ..\EXE\CLASS\Domini\DataInterface
move Presentacio\*.class ..\EXE\CLASS\Presentacio
move Dades\*.class ..\EXE\CLASS\Dades
move Excepcions\*.class ..\EXE\CLASS\Excepcions
move Presentacio\Controladors\*.class ..\EXE\CLASS\Presentacio\Controladors