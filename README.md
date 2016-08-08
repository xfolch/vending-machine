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

Thinking about what a vending machine is and how it works I realized that when a user begins an operation no one else performs any action until they finish such operation. On the one hand, it means that I should not worry about concurrency. On the other hand, the design must reflect such behavior, so my vending machine instances are mutable.

Taking a look at the reset operation, the exercise statement claims that this action belongs to the supplier, and it makes sense. So, adding this operation into the vending machine contract would violate the SRP. Remembering what SRP says: a software module must have only one reason to change. That means the module must have only one "changer". In this case, the consumer and the supplier are two different "changer". For that reason, I created the `Resetable` trait within the supplier module, and added an action into `Supplier` API to let them, as owners of the domain, be able to reset a vending machine. 

When a user finally selects a product after inserting some coins, the vending machine has to do some calculations to figure out how many coins has to give in return. This sort of calculations should be decoupled from the vending machine and go in their own modules. In turn, embracing this approach makes the vending machine fulfills the Open/Closed Principle, since mixing different implementations of `CoinCalculator` could extend the vending machine behavior.
   
### Some thoughts

- **Working with immutable objects**

    Apart from my decision about designing vending machines as mutable objects, the rest of my approach embraces immutability at large. This decision helps me not only to maintain the state, but also easing in debugging.

- **Collection java libraries are not cool**

    I had to make defensive copies all the time to guarantees the immutability. It is not ideal in terms of performance, but lets me to get my model immutable.    

- **Using factory methods instead of constructors as much as possible**

    The main advantage of factory methods is that you can name them, which eases the readability of your code and reveals intention. On the other hand, factory method lets you to return whatever you want. That means you can return a new instance, a singleton instance (such as empty instance) or a monad like `Optional` or `Try`.

- **Decorator pattern is your friend**

    Decorator pattern as a design approach for inheritance lets the code be much simpler.
    
- **Package-private access**

    This design decision restricts the access to the implementation classes. Only the factories are public and are the single entry point where to get implementation instances

## Execution

You can build the project by running:

```
./gradlew build
```

This automatically downloads Gradle and builds the project, including running the tests.
