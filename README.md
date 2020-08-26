# JAVA IMMORTALS

> Arquitectura de Software
>
> Simon Marín - Davor Cortés
>

Este laboratorio tiene como fin que el estudiante conozca y aplique conceptos propios de la programación concurrente, además de estrategias que eviten condiciones de carrera.

## Requisites

+ Java SE JDK 8
+ Maven
+ Git


## Compile and Run

En la consola de comandos ingresar el siguiente comando para compilar el proyecto y ejecutar las pruebas:
```
mvn package
```

Para ejecutar específicamente las clases principales de los ejercicios, se debe ingresar el siguiente comando:

+ Para el juego de los inmortales

```
mvn exec:java -Dexec.mainClass="edu.eci.arsw.highlandersim.ControlFrame"
```

+ Para productores y consumidores

```
mvn exec:java -Dexec.mainClass="edu.eci.arst.concprg.prodcons.StartProduction"
```


# Part I - Before finishing class
### Thread control with wait/notify. Producer/consumer
* Check the operation of the program and run it. While this occurs, run jVisualVM and check the CPU consumption of the corresponding process. Why is this consumption? Which is the responsible class? 
    * Este consumo indevido se debe a que el productor es bastante lento y después de añadir un elemento hace uso de un Sleep, lo que lo hace que el consumidor en un momento solo elimine un elemento de la lista.
    
    ![imgr1](https://media.discordapp.net/attachments/412414196781940760/747957196687409282/unknown.png?width=916&height=475)

* Make the necessary adjustments so that the solution uses the CPU more efficiently, taking into account that - for now - production is slow and consumption is fast. Verify with JVisualVM that the CPU consumption is reduced. 

    * Se ajustó el codigo para que el productor genere elementos mas rapidamente, mediante una sincronización y quitando un sleep.
    ![imgr2](https://cdn.discordapp.com/attachments/412414196781940760/747957673345155079/unknown.png)

* Make the producer now produce very fast, and the consumer consumes slow. Taking into account that the producer knows a Stock limit (how many elements he should have, at most in the queue), make that limit be respected. Review the API of the collection used as a queue to see how to ensure that this limit is not exceeded. Verify that, by setting a small limit for the 'stock', there is no high CPU consumption or errors.
    
    * se añadió un limite en la cola de 30 elementos, además se añadió un sleep en el consumidor para un lento consumo
    ![imgr3](https://cdn.discordapp.com/attachments/412414196781940760/747963411593297920/unknown.png)



# Part II
## Synchronization and Dead-Locks.

1. Review the “highlander-simulator” program, provided in the edu.eci.arsw.highlandersim package. This is a game in which:
    * You have N immortal players. 
    * Each player knows the remaining N-1 player.
    * Each player permanently attacks some other immortal. The one who first attacks subtracts M life points from his opponent, and increases his own life points by the same amount. 
    * The game could never have a single winner. Most likely, in the end there are only two left, fighting indefinitely by removing and adding life points. 
2. Review the code and identify how the functionality indicated above was implemented. Given the intention of the game, an invariant should be that the sum of the life points of all players is always the same (of course, in an instant of time in which a time increase / reduction operation is not in process ). For this case, for N players, what should this value be?

    La invariante es que la Sumatoria de la vida de los inmortales es igual a una constante de vida (DEFAULT_IMMORTAL_HEALTH) multiplicada por el numero de inmortales.
    
3. Run the application and verify how the ‘pause and check’ option works. Is the invariant fulfilled?

    Al correr el programa por primera vez se puede apreciar que no se cumple la invariante
    ![img1](https://cdn.discordapp.com/attachments/412414196781940760/747841949607919818/unknown.png)
    
    Ejemplo con DEFAULT_IMMORTAL_HEALTH = 100 y 3 inmortales.
    
4. A first hypothesis that the race condition for this function (pause and check) is presented is that the program consults the list whose values ​​it will print, while other threads modify their values. To correct this, do whatever is necessary so that, before printing the current results, all other threads are paused. Additionally, implement the ‘resume’ option.
5. Check the operation again (click the button many times). Is the invariant fulfilled or not ?.

    El programa nos arroja una excepción, ya que en alguna otra parte del codigo se está accediendo al arreglo de inmortales y se está generando una condición de carrera.
    
6. Identify possible critical regions in regards to the fight of the immortals. 
Implement a blocking strategy that avoids race conditions. Remember that if you need to use two or more ‘locks’ simultaneously, you can use nested synchronized blocks:

    * Las regiones criticas identificadas son en el metodo fight y en las partes donde se accede a algún atributo de el arreglo de inmortales.
7. After implementing your strategy, start running your program, and pay attention to whether it comes to a halt. If so, use the jps and jstack programs to identify why the program stopped.
    
    * El programa se bloquea totalmente, existe un error en la sincronización que causó un deadlock. 

8. Consider a strategy to correct the problem identified above (you can review Chapter 15 of Java Concurrency in Practice again).
9. Once the problem is corrected, rectify that the program continues to function consistently when 100, 1000 or 10000 immortals are executed. If in these large cases the invariant begins to be breached again, you must analyze what was done in step 4.

    * Se definió una mejor sincronización en el metodo fight que permitió la validez de la invariante en nuestro programa.
    
    ![expl1](https://cdn.discordapp.com/attachments/412414196781940760/747966376336949319/unknown.png)
    
    Ejemplo con 1000 inmortales
    
    
10. An annoying element for the simulation is that at a certain point in it there are few living 'immortals' making failed fights with 'immortals' already dead. It is necessary to suppress the immortal dead of the simulation as they die. 
    * Analyzing the simulation operation scheme, could this create a race condition? Implement the functionality, run the simulation and see what problem arises when there are many 'immortals' in it. Write your conclusions about it in the file ANSWERS.txt. 
    * Correct the previous problem WITHOUT using synchronization, since making access to the shared list of immortals sequential would make simulation extremely slow. 
11. To finish, implement the STOP option.
