cd .. ; #Directori EXE
cd .. ; #Directori subgrup-prop4-4
cd FONTS;  #Directori FONTS
javac Domini/DistanciaEuclidiana.java ; 
javac -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar JUnit/DistanciaEuclidianaTest.java ; 
java -cp .:lib/junit-4.13.2.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore JUnit.DistanciaEuclidianaTest
