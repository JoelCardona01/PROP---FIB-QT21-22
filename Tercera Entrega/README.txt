----------------------------IMPORTANT LLEGIR AQUEST FITXER ABANS D'USAR EL PROGRAMA!---------------------------------

----COM COMPILAR I EXECUTAR----

Trobaras dins de la carpeta FONTS d'aquest directori un archiu anomenat make.sh o make.bat (depenent de si estas usant linux o windows hauras 
d'executar un o l'altre, .sh per linux .bat per windows). En aquesta carpeta tambe trobaras makeAndEXE.bat o makeAndExe.sh (tambe depenent de si
utilitzes windows o linux).
A la terminal s'ha de executar, dins del directori FONTS, els .sh o .bat i aixo generara tots els .class per les classes del programa.
Si al executar els makes surten dues Notes a la terminal es normal. El programa funciona igual.
Si vols compilar i executar amb una mateixa comanda, executa makeAndEXE.exe o makeAndExe.sh

Els fichers creats pel make es guardaran automaticament a la carpeta EXE d'aquest directori.

EXE conte la carpeta CLASS, on els .class aniran a parar.
Tambe trobaras en CLASS/Presentacio/Vistes els fitxers .fxml corresponents a les vistes.

L'excutable que permetra usar el programa (sense haver de compilar de nou com passa amb ./makeAndEXE) estara directament dins d'EXE, amb nom Main i amb extensio .sh o .bat.

Per exectuar els fitxers .sh o .bat nomes cal que et coloquis desde la terminal a la carpeta on es troba el fitxer i entris: ./NomFitxer.sh o 
./NomFitxer.bat. Si no funciona per falta de permisos has d'executar a la terminal : chmod +x *.sh o chmod +x *.bat

 

----COM USAR EL PROGRAMA----

Per saber com utilitzar el programa, llegeix el fitxer ManualUsuari.pdf present a la carpeta DOCS. La informacio primordial sobre l'us, es que per iniciar sessio
com a usuari, has de registrar-te amb un usuari i contrasenya, i despres iniciar sessio amb aquesta parella, i, en cas que es vulgui iniciar sessio com a admin,
cal entrar amb usuari:"Admin" i contrasenya "adminadmin". Per el primer us del recomanador, has d'iniciar sessio com a administrador i carregar les dades a entrar
dades, el recomanador t'ajudara a arribar fins aqui. Concretament has de carregar items i ratings, path relatiu dels quals ja esta posat per defecte.
Un cop fet aixo, ja es pot utilitzar el recomanador amb normalitat, tant com administrador, com usuari. Per mes informacio, consulta el fitxer ManualUsuari.pdf.
En cas que el manual no resolgui els teus dubtes, no dubtis en contactar-nos als correus del document EQUIP.txt.

    
----DRIVERS I PROVES----

Trobaras un document, dins el directori DOCS, amb diferents jocs de prova on esta especificat les accions a dur a terme per el joc de proves i el resultat esperat.
Tambe hi ha un apartat resultats de testing on, despres de testejar diferents configuracions, es fa una conclusio de quina es la millor i quin estres pateix el sistema segons les
dades entrades o les peticions fetes.

----ANOTACIONS I DESCISONS PRESES---- 


En quant als algorismes, s'ha seguit el enunciat publicat a la pagina de l'assignatura i si hi havia coses que el enunciat no especificava com s'havia de fer o quin 
criteri s'havia de utilitzar hem acabat usant criteri propi i sentit comu.

Per als algorismes sha considerat que nomes s'usen com a usuaris d'estudi aquells que estan a ratings.db (ho vam preguntar al tutor i vam arribar a aquesta desicio).

Hem provat el programa amb totes les mides i tots els fitxers excepte Movielens 2250, ja que esta mal generat. 

Tots els documents .txt, els comentaris al codi i el text que es mostra a les vistes esta escrit sense accents, dieresis ni simbols extranys, per evitar problemes.

Atencio: el fitxer json configRecomanador.json i el fitxer json UsuarisActius.json no poden ser eliminats en cap moment. Si es vol buidar les dades, aquests dos fitxers s'han de deixar.
         Si es vol eliminar els usuaris actius cal borrar-los del UsuarisActius.json pero sempre deixant a l'administrador.
         
Atencio: Per utilitzar el sistema, que utilitza javafx, s'ha de tenir un sistema operatiu windows, o un sistema operatiu Linux amb arquitectura x64, altrament, 
	 en cas d'utilitzar MacOS o un sistema operatiu arm, s'ha de crear a la carpeta FONTS/lib un directori nou amb la llibreria per aquest SO, es pot trobar
	 aquesta carpeta a https://gluonhq.com/products/javafx/. Es recomana que aquesta carpeta es digui javafx-sdk-17.0.1_XX on XX sigui el SO+arquitectura o nomes
	 el SO. Despres, per poder compilar i executar, cal cambiar en el .sh (ja que els sistemes operatius no contemplats utilitzaran aquest) alla on digui 
	 javafx-sdk-17.0.1_Ubuntu, posar en comptes d'Ubuntu el "XX" esmentat abans. 
	 Si hi ha cap problema a l'hora d'executar o compilar el codi per alguna circunstancia d'aquest tipus, si us plau, no dubteu en contactar-nos als correus 
	 indicats a EQUIP.txt.

Nomes aquells directoris rellevants tenen index.txt. No s'ha fet index.txt per exemple dins de la carpeta CLASS de EXE ja que son tot .class i es autoexplicatiu.

