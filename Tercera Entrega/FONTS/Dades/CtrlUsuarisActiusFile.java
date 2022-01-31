package Dades;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import  Domini.DataInterface.CtrlUsuariActiu;
import Domini.Model.Pair;
import Excepcions.ContrasenyaIncorrecte;
import Excepcions.ExcepcionsRecomanador;
import Excepcions.UsuariNoExisteix;


public class CtrlUsuarisActiusFile implements CtrlUsuariActiu{ 

    //Funcio feta per: Joel Cardona.
    //Aquesta funcio es la que es crida quan es fa log in, de manera que comprova que el usuari amb username usrname existeix i la contrasenya es la correcte i alhora puja a memoria la seva info per tal de poder usar el sistma.
    //Pre: 
    //Post: Es retornen les valoracions de l'usuari identificat per usuari username i amb contrasenya password. Si no hi ha cap usuari amb identificador username, salta una excepcio.
    public Pair<Integer, Vector<Pair<String, Double> >  >getUsuariActiu(String usrname, String password) throws ExcepcionsRecomanador{ //Nomes cal que retornem les valoracions del usuari (ja tenim l'username i la password)
        JSONParser jsP = new JSONParser();
        Vector<Pair<String, Double> >  DadesUsuari = new Vector<Pair<String, Double> >();
        try (FileReader rd = new FileReader("../../DATA/usuarisActius.json")){
            
            JSONArray CjtUsuaris = (JSONArray) jsP.parse(rd);

            for (int i = 0; i < CjtUsuaris.size(); ++i){
                JSONObject next = (JSONObject) CjtUsuaris.get(i); //Obtenim l'objecte de l'usuari iessim
                int usrId = ((Long)next.get("userId")).intValue(); //Obtenim l'id de l'usuari 
                String username = (String) next.get("Username");    //Obtenim l'username de l'usuari
                String psw = (String) next.get("Password"); //Obtenim la contrasennya de l'usuari
                if(password.equals(psw) && username.equals(usrname)){   //Comprovem que la contrasenya coincideix amb l'userId
                    JSONArray valoracions = (JSONArray) next.get("Valoracions");
                    //Recorrem sobre les valoracions del usuari
                    for (int j = 0; j < valoracions.size(); ++j){
                        JSONObject valoracioIessima = (JSONObject) valoracions.get(j);  
                        String IdItem = (String) valoracioIessima.get("IdItem");    //Guardem l'item de la valoracio j-essima
                        Double puntuacio = (Double) valoracioIessima.get("Puntuacio");  //Guardem la puntuacio de la valoracio j-essima
                        Pair<String, Double> valAct = new Pair<String, Double>();   //Creem la tupla ItemId + Puntuacio de la valoracio j-essima
                        valAct.first = IdItem; valAct.second = puntuacio;   
                        DadesUsuari.add(valAct);  //Afegim la valoracio al vector
                    }
                    Pair<Integer, Vector<Pair<String, Double> > > result = new Pair<Integer, Vector<Pair<String, Double>>>();
                    result.first = usrId; result.second = DadesUsuari;
                    return result;
                }
                else if (username.equals(usrname) && !password.equals(psw)) throw new ContrasenyaIncorrecte(usrname, password); //Falta fer la excepcio de contrassenya incorrecte
            }
        }
        catch (IOException e){
            //no fem res
        }
        catch (ParseException e) {
            //no fem res
        }
        
        throw new UsuariNoExisteix(usrname); //Falta ferr una excepcio amb usuari i contrassenya
    }

    //Funcio feta per: Joel Cardona.
    //Pre: 
    //Post: afegeix en el fitxer json d'usuaris actius la informacio de l'usuari passat per parametre.
    public void saveUser(int userid, String username, String contrasenya, Vector<Pair<String, Double> >ValoracionsUsuari){
        JSONParser jsP = new JSONParser();
        JSONArray CjtUsuaris = new JSONArray();
        try (FileReader rd = new FileReader("../../DATA/usuarisActius.json")){
            
            CjtUsuaris = (JSONArray) jsP.parse(rd);
            boolean trobat = false;
            for (int i = 0; i < CjtUsuaris.size() && !trobat; ++i){
                JSONObject next = (JSONObject) CjtUsuaris.get(i); //Obtenim l'objecte de l'usuari iessim
                int useridIessim = ((Long)next.get("userId")).intValue();  //Obtenim l'id de l'usuari iessim
                if(useridIessim == (userid)){    //Si l'userId coincideix
                    CjtUsuaris.remove(next); //L'esborrem pq despres l'afegirem 
                    trobat = true;  //Deixem de recorrer el vector
                }
            }
        }
        catch (IOException e){
            //no fem res
        }
        catch (ParseException e) {
            //no fem res
        }

        JSONObject nouUsuari = new JSONObject();    //Creem un nou objecte  JSON amb el usuari rebut per parametre
        nouUsuari.put("userId", userid);  //Guardem l'id de l'usuari
        nouUsuari.put("Username", username);    //Guardem l'username de l'usuari
        nouUsuari.put("Password", contrasenya);   //Guardem la contrasenya de l'usuari
        JSONArray valoracions = new JSONArray();    //Generem l'array de valoracions de l'usuari
        for(Pair<String, Double> i : ValoracionsUsuari){
            JSONObject valoracioIessima = new JSONObject();
            valoracioIessima.put("IdItem", i.first);    //Guardem el id de l'item iessim valorat
            valoracioIessima.put("Puntuacio", i.second);    //Guardem la puntuacio de la valoracio iessima
            valoracions.add(valoracioIessima);  //Ho afegim a la array de valoracions de l'usuari
        }
        nouUsuari.put("Valoracions", valoracions);  //Guardem l'array a l'usuari 
        CjtUsuaris.add(nouUsuari);  //Afegim l'usuari al conjunt d'usuaris
        try (FileWriter file = new FileWriter("../../DATA/usuarisActius.json")){
            file.write(CjtUsuaris.toJSONString());  //Guardem la informacio al document situat a DATA/usuarisActius.json
            file.flush();
        } catch (IOException e){
            //no fem res
        }
        
    }

    //Funcio feta per: Marina Alapont
    //Pre: 
    //Post: Retorna cert si ja existeix un usuari amb username 'username'. Fals en cas contrari
    public Boolean existeixUsuari(String username) {
        JSONParser jsP = new JSONParser();
        JSONArray CjtUsuaris = new JSONArray();
        try (FileReader rd = new FileReader("../../DATA/usuarisActius.json")){
            
            CjtUsuaris = (JSONArray) jsP.parse(rd);
            for (int i = 0; i < CjtUsuaris.size(); ++i){
                JSONObject next = (JSONObject) CjtUsuaris.get(i); //Obtenim l'objecte de l'usuari iessim
                String usernameIessim = (String)next.get("Username");    //Obtenim l'id de l'usuari iessim
                if(usernameIessim.equals(username)){    //Si el nom d'usuari coincideix
                    return true; 
                }
            }
        }
        catch (IOException e){
            //no fem res
        }
        catch (ParseException e) {
            //no fem res
        }
        return false;
    }

    //Funcio feta per: Joel Cardona.
    //Pre: 
    //Post: Retorna cert si ja existeix un usuari amb userid 'userId'. Fals en cas contrari
    public Boolean existeixUsuari(int userId) {
        JSONParser jsP = new JSONParser();
        JSONArray CjtUsuaris = new JSONArray();
        try (FileReader rd = new FileReader("../../DATA/usuarisActius.json")){
            
            CjtUsuaris = (JSONArray) jsP.parse(rd);
            for (int i = 0; i < CjtUsuaris.size(); ++i){
                JSONObject next = (JSONObject) CjtUsuaris.get(i); //Obtenim l'objecte de l'usuari iessim
                int usernameIessim = ((Long)next.get("userId")).intValue();    //Obtenim l'id de l'usuari iessim
                if(usernameIessim == (userId)){    //Si el nom d'usuari coincideix
                    return true; 
                }
            }
        }
        catch (IOException e){
            //no fem res
        }
        catch (ParseException e) {
            //no fem res
        }
        return false;
    }

    //Funcio feta per: Marina Alapont.
    //Pre: existeix un usuariActiu amb identificador userId
    //Post: Elimina de la base de dades tota la informació relacionada amb el usuari passat per parametre
    public void eliminarUsuari(int userId) {
        JSONParser jsP = new JSONParser();
        JSONArray CjtUsuaris = new JSONArray();
        try (FileReader rd = new FileReader("../../DATA/usuarisActius.json")){
            
            CjtUsuaris = (JSONArray) jsP.parse(rd);
            boolean trobat = false;
            for (int i = 0; i < CjtUsuaris.size() && !trobat; ++i){
                JSONObject next = (JSONObject) CjtUsuaris.get(i); //Obtenim l'objecte de l'usuari iessim
                int useridIessim = ((Long)next.get("userId")).intValue();  //Obtenim l'id de l'usuari iessim
                if(useridIessim == (userId)){    //Si l'userId coincideix
                    CjtUsuaris.remove(next); //L'esborrem 
                    trobat = true;  //Deixem de recorrer el vector
                }
            }
        }
        catch (IOException e){
            //no fem res
        }
        catch (ParseException e) {
            //no fem res
        }

        try (FileWriter file = new FileWriter("../../DATA/usuarisActius.json")){
            file.write(CjtUsuaris.toJSONString()); 
            file.flush();
        } catch (IOException e){
            //no fem res
        }
    }

    //Funcio feta per: Marina Alapont.
    //Pre: 
    //Post: S'eliminen de la base de dades totes les valoracions de tots els usuaris per a l'item idItem
    public void eliminaValoracioItem(String idItem) {
        JSONParser jsP = new JSONParser();
        JSONArray CjtUsuaris = new JSONArray();
        try (FileReader rd = new FileReader("../../DATA/usuarisActius.json")){
            
            CjtUsuaris = (JSONArray) jsP.parse(rd);
            for (int i = 0; i < CjtUsuaris.size(); ++i){
                JSONObject next = (JSONObject) CjtUsuaris.get(i); //Obtenim l'objecte de l'usuari iessim
                JSONArray vals = (JSONArray)next.get("Valoracions");
                boolean trobat = false;
                for (int j=0; j<vals.size() && !trobat; ++j) {
                    JSONObject nextval = (JSONObject) vals.get(j);
                    String id = (String) nextval.get("IdItem");
                    if (id.equals(idItem)){
                        trobat=true;
                        vals.remove(j); 
                    } 
                }
            }
        }
        
        catch (IOException e){
            //no fem res
        }
        catch (ParseException e) {
            //no fem res
        }

        try (FileWriter file = new FileWriter("../../DATA/usuarisActius.json")){
            file.write(CjtUsuaris.toJSONString()); 
            file.flush();
        } catch (IOException e){
            //no fem res
        }
    }

    //Funcio feta per: Joel Cardona
    //Pre:
    //Post: S'eliminen de la base de dades totes les valoracions del tots els usuaris
    public void eliminarTotesValoracions() {
        JSONParser jsP = new JSONParser();
        JSONArray CjtUsuaris = new JSONArray();
        try (FileReader rd = new FileReader("../../DATA/usuarisActius.json")){
                
            CjtUsuaris = (JSONArray) jsP.parse(rd);
            for (int i = 0; i < CjtUsuaris.size(); ++i){    //Per a cada usuari
                JSONObject next = (JSONObject) CjtUsuaris.get(i); //Obtenim l'objecte de l'usuari iessim
                JSONArray vals =new JSONArray(); //Es crea una array de valoracions buida
                next.put("Valoracions", vals);
            }
        }
            
        catch (IOException e){
            //no fem res
        }
        catch (ParseException e) {
            //no fem res
        }
    
        try (FileWriter file = new FileWriter("../../DATA/usuarisActius.json")){
            file.write(CjtUsuaris.toJSONString()); 
            file.flush();
        } catch (IOException e){
            //no fem res
        }
    }
    

}

//Classe implementada per Joel Cardona i Marina Alapont 