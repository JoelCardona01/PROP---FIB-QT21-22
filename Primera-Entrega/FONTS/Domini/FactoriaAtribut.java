package Domini;
import java.util.*;

public class FactoriaAtribut {

    //could we do this as an interface that is afterwards implemented by Atribut????


    private boolean isNumeric(String s) {

        boolean tePuntDecimal = false;

        for (int i = 0; i < s.length(); ++i) {
            int asciiValue = s.codePointAt(i);

            if(asciiValue == '.') {
                //if there is more than 1 decimal point or it is at the begginning or end of the string, we return false
                if (tePuntDecimal || i == 0 || i+1 == s.length()) return false;
                tePuntDecimal = true;
            }
            //for other characters, return false if it is not a digit
            else if (asciiValue < '0' || asciiValue > '9') return false;
        }
        return true;
    }

    public Atribut creaAtribut(Item i, Columna nCol, HashSet<String> s) {
        Iterator<String> it = s.iterator();
        String sol = it.next();
        if (s.size() == 1 && sol.equals("True")) return new AtributPropietat(i, nCol, true);                 //returns Atribut_propietat
        if (s.size() == 1 && sol.equals("False")) return new AtributPropietat(i, nCol, false);               //returns Atribut_propietat

        //transform from String to Double: Double.parseDouble("900.1");
        if (s.size() == 1 && isNumeric(sol)) {
            AtributNumeric a = new AtributNumeric(i, nCol, Double.parseDouble(sol));
            nCol.afegirAtribut(a);
            return a; 
        }     //returns Atribut_numeric
        return new AtributCategoric(i, nCol, s);                                         //returns Atribut_categotic
    }
}