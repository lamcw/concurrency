# File Structure
The `src` directory contains all the Java source code. `promela` directory
contains Promela source and output.

```
src
├── barber
│   ├── fifo
│   │   ├── monitor
│   │   │   ├── Barber.java
│   │   │   ├── BarberShop.java
│   │   │   └── Customer.java
│   │   └── semaphore
│   │       ├── Barber.java
│   │       ├── BarberShop.java
│   │       └── Customer.java
│   ├── hilzer
│   │   ├── monitor
│   │   │   ├── Barber.java
│   │   │   ├── BarberShop.java
│   │   │   └── Customer.java
│   │   └── semaphore
│   │       ├── Barber.java
│   │       ├── BarberShop.java
│   │       └── Customer.java
│   └── sleepingbarber
│       ├── monitor
│       │   ├── Barber.java
│       │   ├── BarberShop.java
│       │   └── Customer.java
│       └── semaphore
│           ├── Barber.java
│           ├── BarberShop.java
│           └── Customer.java
└── smoker
    ├── cigar
    │   ├── monitor
    │   │   ├── Agent.java
    │   │   ├── Smoker.java
    │   │   ├── Solution.java
    │   │   ├── TableIngredient.java
    │   │   └── Table.java
    │   └── semaphore
    │       ├── Agent.java
    │       ├── Smoker.java
    │       └── Solution.java
    ├── generalized
    │   ├── monitor
    │   │   ├── Agent.java
    │   │   ├── Pusher.java
    │   │   ├── ScoreBoard.java
    │   │   ├── Smoker.java
    │   │   └── Solution.java
    │   └── semaphore
    │       ├── Agent.java
    │       ├── Smoker.java
    │       └── Solution.java
    └── Ingredient.java
promela
├── cigar-smoker.out
├── cigar-smoker.pml
├── fifo-barber.out
├── fifo-barber.pml
├── generalized-smoker.out
├── generalized-smoker.pml
├── hilzer-barber.out
├── hilzer-barber.pml
├── include
│   └── sem.h
├── sleeping-barber.out
└── sleeping-barber.pml
```

For the smoker problems, run the Solution class. For barbershop problems, run
the BarberShop class.

# Build

Run the build script to build all Java files.

```console
$ ./build
$ ./build clean  # Remove all artifacts
```

# Running the solution
Prepend `-cp` before class lists. For example, if you want to run solution for
the sleeping barber problem:
```console
$ java -cp bin barber.sleepingbarber.semaphore.BarberShop
```
