cd ..
cd ..
cd FONTS
javac Domini/DistanciaEuclidiana.java
javac -cp .;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar JUnit/DistanciaEuclidianaTest.java 
java -cp .;lib/junit-4.13.2.jar;lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore JUnit.DistanciaEuclidianaTest
