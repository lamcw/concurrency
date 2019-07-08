package barber.sleepingbarber.semaphore;

import java.util.concurrent.Semaphore;

class BarberShop {
    final static int MAX_CUSTOMERS = 4;
    volatile int customers;

    BarberShop() {
        customers = 0;
    }

    void incCustomers() {
        customers++;
    }

    void decCustomers() {
        customers--;
    }

    boolean isFull() {
        return customers == MAX_CUSTOMERS;
    }

    public static void main(String[] args) {
        BarberShop barberShop = new BarberShop();

        Semaphore mutex = new Semaphore(1, true);
        Semaphore customer = new Semaphore(0, true);
        Semaphore barber = new Semaphore(0, true);
        Semaphore customerDone = new Semaphore(0, true);
        Semaphore barberDone = new Semaphore(0, true);

        Barber b = new Barber(customer, barber, customerDone, barberDone);
        Thread barberThread = new Thread(b);
        barberThread.start();

        while (true) {
            Customer c = new Customer(barberShop, mutex, customer, barber, customerDone, barberDone);
            new Thread(c).start();
        }
    }
}
