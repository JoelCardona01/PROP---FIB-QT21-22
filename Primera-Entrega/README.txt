----------------------------IMPORTANT LLEGIR AQUEST FITXER ABANS D'USAR EL PROGRAMA!---------------------------------

----COM COMPILAR I EXECUTAR----

Trobaras dins de la carpeta FONTS d'aquest directori un archiu anomenat make.sh o make.bat (depenent de si estas usant linux o windows hauras 
d'executar un o l'altre, .sh per linux .bat per windows).
A la terminal s'ha de executar, dins del directori FONTS, ./make.sh o ./make.bat i aixo generara tots els .class per les classes del programa, 
els drivers i els test amb JUnit. Si al executar ./make.sh o ./make.bat surten dues Notes a la terminal es normal. El programa funciona igual.

Els fichers creats pel make es guardaran automaticament a la carpeta EXE d'aquest directori.

EXE conte la carpeta CLASS, on els .class aniran a parar.
EXE tambe conte altres carpetes, que contenen diferents executables per provar els dirvers creats. 
No hi ha un driver per classe ja que el tutor ens va comentar que un sol driver general que llegis comandes seria correcte. Aquest driver 
es pot provar executant DriverComplet.sh o DriverComplet.bat, ambdos arxius situats a EXE.

L'excutable que permetra usar el prgrama estara directament dins d'EXE, amb nom Main i amb extensio .sh o .bat.

Per exectuar els fitxers .sh o .bat nomes cal que et coloquis desde la terminal a la carpeta on es troba el fitxer i entris: ./NomFitxer.sh o 
./NomFitxer.bat. Si no funciona per falta de permisos has d'executar a la terminal : chmod +x *.sh o chmod +x *.bat

 

----COM USAR EL PROGRAMA----
Nomes exectuar el programa es mostra per pantalla un seguit de missatges. Aquests informen sobre la configuracio inicial del recomanador i 
demana l'entrada necessaria per poder començar a usar el sistema.
Com be veuras que diu un dels missatges, cal entrar en el ordre que demana i sense extensio .csv els 4 fitxers de dades inicials (items, ratings.db,
ratings.test.known, ratings.test.unknown). Al directori DATA podras trobar ja uns fitxers que corresponen a aquesta entrada.

Despres d'haver llegit els fitxer i haver configruat el rang de puntuacions del sistema, es mostra un menu com el seguent: 


0. Informacio sobre cada opcio del menu

1. Demanar recomanacio per un usuari

2. Demanar recomanacio per un usuari i per uns items donats

3. Comprovar la qualitat de una o mes recomanacions

4. Consultar i/o canviar l'estrategia de recomanacio

5. Consultar i/o canviar l'estrategia del calcul de distancies

6. Consultar i/o canviar els parametres k dels algorismes

7. Recalcular clusters k-means

8. Sortir

Per a escollir una de les funcionalitats nomes cal entrar un dels numeros a la terminal. 
Des d'aquest punt s'anira guiant el us del sistema amb missatges per terminal, i en acabar una funcionalitat es podra tornar a visualitzar el menu i tornar a escollir.
Es important que, si el sistema et demana un enter per terminal, el que s'indtrodueixi sigui realment un enter.
En qualsevol moment es pot para l'execucio total del programa prement ctrl+c.

----EXPLICACIO DE LES OPCIONS DEL MENU----

0. Informacio sobre cada opcio del menu. --> Mostra per pantalla el mateix contingut que aquest apartat.

1. Demanar recomanacio per un usuari.
    Aquesta opcio permet demanar una recomanacio per a un usuari concret indicant el seu identificador (que es el nom d'usuari) i el nombre d'items que es vol a la
    sortida. Cal indicar aquests parametres en linies diferents, cal que l'usuari existeixi i que el nombre d'items que es vol a la sortida sigui menor o igual al nombre
    d'items que consta en el sistema.
    La recomanacio es dura a terme amb les estrategies configurades en aquell moment i amb els parametres k establerts en aquell moment.
    La recomanacio estudiara tots els items del sistema.
    Un exemple d'entrada es: 
    143
    5

2. Demanar recomanacio per un usuari i per uns items donats.
    Aquesta opcio permet demanar una recomanacio per a un usuari concret i tinguent com a items d'estudi un conjunt concret.
    Cal indicar l'identificador del usuari (que es el nom d'usuari) i el nombre d'items que es vol a la sortida, el nombre d'items que s'entrara i finalment els ids 
    d'aquests items. 
    Cal indicar tots aquests parametres en linies diferents, cal que l'usuari existeixi, que el nombre d'items que es vol a la sortida sigui menor o igual al nombre
    d'items que s'entra, i a la vegada que aquest nombre d'items entrats sigui menor o igual al total d'items, i finalment que aquests items entrats existeixin.
    La recomanacio es dura a terme amb les estrategies configurades en aquell moment i amb els parametres k establerts en aquell moment.
    La recomanacio estudiara nomes els items entrats.
    Un exemple d'entrada es: 
    143
    3
    5
    296
    480
    111
    185
    292
    
3. Comprovar la qualitat de una o mes recomanacions
    Aquesta opcio permet provar la qualitat de les recomanacions i per tant provar el funcionament dels algorismes, usant com entrada un fitxer al que hem anomenat
    inputqueries.txt. La terminal especifica quins requisits ha de tenir aquest fitxer i demana que s'entri el nom del fitxer amb l'extensio inclosa per poder llegir-lo.
    Basicament el que es fa es demanar n recomanacions per n usuaris diferents del estil recomanacions de l'opcio 2 del menu. Per aquestes recomanacions es calcula
    un DCG promig, que indica lo be que s'ha ordenat en mitjana els items a predir. 
    Aquesta funcionalitat necessita les dades de known i les de unknown, de manera que els usuaris entrats per demanar les recomanacions pertanyen a aquests dos fitxers,
    i la resta d'informacio a entrar es coherent tambe amb aquests fitxers. 
    El fitxer ha de seguir el format:
    nusuaris a demanar recomanacions
    (per cada usuari de nusuaris:)
    idUsuari numValoracionsConegudes numValoracionsDesconegudes numItemsARetornar
    tantes linies amb parelles <idItem puntuacio> com numValoracionsConegudes
    tantes linies amb iditem com numValoracionsDesconegudes
    
    Pots trobar un exemple del fitxer inputqueries al directori DATA.
    
4. Consultar i/o canviar l'estrategia de recomanacio
    Aquesta opcio permet consultar quina estrategia de filtering hi ha configurada en aquell moment, i si es vol, permet canviar l'estrategia.

5. Consultar i/o canviar l'estrategia del calcul de distancies
    Aquesta opcio permet consultar quina estrategia de calcul de distancies entre usuaris hi ha configurada en aquell moment, i si es vol, permet canviar l'estrategia.


6. Consultar i/o canviar els parametres k dels algorismes
    Aquesta opcio permet consultar quins parametres k hi ha configurats en aquell moment per als alogrismes, i si es vol, permet canviar aquests valors.
    
7. Recalcular clusters k-means
    Aquesta opcio recalcula els clusters de k-means, usant el valor k configuart en aquell moment. El calcul de les particions no es fa per cada cop que es demana
    una recomanacio ja que es bastant costos. Es per aixo que sempre que es vulgui canviar els clusters i recalcularlos es pot escollir aquesta opcio. Els clusters
    es calculen per primer cop al inici d'execucio del programa.

8. Sortir
    Permet abandonar el sistema recomanador i acabar l'execucio.
    
----DRIVERS I PROVES----
No s'ha relitzat un driver per cada clase ja que el tutor ens va dir que un driver general que uses com entrada un fitxer amb comandes que permetesin provar totes les
clases rellevants era suficient. Els drivers que ja teniem funcionant en aquell moment els hem deixat com a metodes de prova extra. 
En quant al driver gran, s'anomena DriverComplet. Es pot executar desde el directori EXE i el fitxer que rep com entrada ha d'estar situat al directori DATA.
A DATA es pot trobar un tutorial de com fer aquest fitxer d'entrada i tambe es pot trobar un exemple, el que hem usat nosaltres per les proves.

Les proves de JUnit s'han fet sobre les classes DistanciaBasica i DistanciaEuclidiana. Per poder fer-ho s'han usat les llibreries seguents:
Junit4.13.2
Hamcrest-core-1.3

----ANOTACIONS I DESCISONS PRESES---- 

S'ha decidit per aquesta entrega implementar algunes de les funcionalitats de l'Admin (tinguent en compte que era obligatori implementar els algorismes de filtering).
Encara no s'ha implementat la posibilitat de que els usuaris usin la app. Es per aixo que, com que nomes una persona la usa i es de tipus admin, no s'ha implementat el sistema 
de login ni les creadores relacionades amb aquest.

S'ha decidit tambe que per aquesta entrega nosaltres no guardarem  l'estat del programa en fichers, pero si ho farem per la proxima entrega. 

En quant als algorismes, s'ha seguit el enunciat publicat a la pagina de l'assignatura i si hi havia coses que el enunciat no especificava com s'havia de fer o quin 
criteri s'havia de utilitzar hem acabat usant criteri propi i sentit comu.

Per als algorismes sha considerat que nomes s'usen com a usuaris d'estudi aquells que estan a ratings.db (ho vam preguntar al tutor i vam arribar a aquesta desicio).

Tots els documents .txt i el que es mostra en pantalla esta escrit sense accents, dieresis ni simbols extranys, per evitar problemes.

Nomes aquells directoris rellevants tenen index.txt. No s'ha fet index.txt per exemple dins de cada subdirectori de fonts ja que nomes hi ha .java que son autoexplicatius.
Tampoc s'ha fet index dins de la carpeta CLASS de EXE per la mateixa rao, son tot .class i es autoexplicatiu.

