# Vending Machine

The exercise proposed is to design a **Vending Machine** which:

- Accepts **coins** of 0.05€, 0.10€, 0.20€, 0.50€, 1€, 2€ 
- Allow user to **select products** Coke(1.50€), Pepsi(1.45€), Water(0.90€)
- Allow user to take refund by **canceling** the request.
- Return selected product and **remaining change** if any
- Allow **reset operation** for vending machine **supplier**.

## Constraints
 
- Databases are not allowed. Save the state in memory.
- Use the minimum number of external libraries (except for testing libraries, use as many as you usually do)

## My approach

Thinking about what a vending machine is and how it works we realize that a user begins an operation and no one else performs any action until the first one finishes the operation. On the one hand, it means that we should not worry about concurrency. On the other hand, the design must reflect that behavior, so I decided my vending machine instances were mutable.

Taking a look at the reset operation. The exercise statement says that this action belongs to the supplier, and makes sense. Remembering the SRP, which states that a software module must have only one reason to change, if we add this operation into the vending machine contract we are going to violate it, because there would be two different sort of users consuming the same module. For that reason, I have created the `Resetable` trait within the supplier module, and add an action in the `Supplier` api to let them to reset a vending machine. 

When a user finally selects a product after inserting some coins, the vending machine has to do calculations to figure out how many coins has to give in return. This sort of calculations should be decoupled in their own modules, and later be used by the vending machine following the DIP. In turn, the vending machine approach will fulfill the Open/Closed Principle, because the same implementation could be extended by another implementation of the `CoinCalculator`   