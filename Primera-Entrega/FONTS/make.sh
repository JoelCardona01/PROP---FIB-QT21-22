javac --release 11 Domini/*.java ; 
javac --release 11 DriversStubs/*.java ; 
javac --release 11 Presentacio/*.java ; 
javac --release 11 Dades/*.java ; 
javac --release 11 ControladorsDomini/*.java ;
javac --release 11 Excepcions/*.java;
mv Domini/*.class ../EXE/CLASS/Domini;
mv DriversStubs/*.class ../EXE/CLASS/DriversStubs;
mv Presentacio/*.class ../EXE/CLASS/Presentacio;
mv Dades/*.class ../EXE/CLASS/Dades;
mv ControladorsDomini/*.class ../EXE/CLASS/ControladorsDomini;
mv Excepcions/*.class ../EXE/CLASS/Excepcions;

