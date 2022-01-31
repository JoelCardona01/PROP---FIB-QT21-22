# FUNCIONALITATS DRIVER

Les comandes han d'anar a linies diferents. Cada parametre de la comanda ha d'anar separada per espais
**< parametres obligatoris > [parametres opcionals]**

***Per el bon funcionament s'ha començar per les seguents comandes;***
1.  **readItems** < filename >
2.  **readRatings** < filename >
3.  **readKnown** < filename >
4.  **readUnknown** < filename > 

Un cop realitzades aquestes comandes, es pot utiltzar qualsevol comanda d'aquesta llista

## Funcionalitats persona:
**newUser** < username > [password] _Crea un nou usuari del tipus "known"_

**getUser** < username > _Retorna el nom d'usuari d'un usuari, si no existeix s'activa una excepcio._

**getValoracions** < username > _Retorna un conjunt de parelles d'identificador d'items i la nota que l'usuari va assignar al item_

**modificarValoracio** < username > < itemid > < nota > _Modifica la nota de la valoracio de l'usuari i l'item introduits_

## Funcionalitats recomanacions:
**recomanacioCollaborative** < userId > < nElements > [itemId1] .. [itemIdn] _retorna un conjunt de recomacions per a l'usuari userId_

**recomanacioContentBased** < userId > < nElements > [itemId1] .. [itemIdn] _retorna un conjunt de recomacions per a l'usuari userId_

## Funcionalitats valoracio:
**newValoracio** < idUser > < idItem > < nota > _Crea una nova valoracio per al usuari identificat amb idUser, i l'item identficat amb idItem i la nota introduida_

## Funcionalitats distancia de vectors
### els elements VecxelementnumElx son doubles i cada vector ha de tenir el mateix nombre d'elements
**distanciaBasica** < numEl > [Vec1element1] .. [Vec1elementnumEl1] [Vec2element1] .. [Vec2elementnumEl1]

**distanciaEuclidiana**  < numEl > [Vec1element1] .. [Vec1elementnumEl1] [Vec2element1] .. [Vec2elementnumEl1]

## Finalitzar el driver:
**end**