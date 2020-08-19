# ARSW-LAB-2

## Simón Marín y Davor Cortés


# Part I - Before finishing class
### Thread control with wait/notify. Producer/consumer
* Check the operation of the program and run it. While this occurs, run jVisualVM and check the CPU consumption of the corresponding process. Why is this consumption? Which is the responsible class? 
    Este consumo indevido se debe a que el productor es bastante lento y después de añadir un elemento hace uso de un Sleep, lo que lo hace que el consumidor en un momento solo elimine un elemento de la lista.
* Make the necessary adjustments so that the solution uses the CPU more efficiently, taking into account that - for now - production is slow and consumption is fast. Verify with JVisualVM that the CPU consumption is reduced. 
* Make the producer now produce very fast, and the consumer consumes slow. Taking into account that the producer knows a Stock limit (how many elements he should have, at most in the queue), make that limit be respected. Review the API of the collection used as a queue to see how to ensure that this limit is not exceeded. Verify that, by setting a small limit for the 'stock', there is no high CPU consumption or errors.