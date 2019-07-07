package barber.barber.semaphore;

import java.util.concurrent.Semaphore;

class BarberShop {
    final static int N_CUSTOMERS = 4;
    volatile static int customers = 0;

    public static void main(String[] args) {
        Semaphore mutex = new Semaphore(1, true);
        Semaphore customer = new Semaphore(0, true);
        Semaphore barber = new Semaphore(0, true);
        Semaphore customerDone = new Semaphore(0, true);
        Semaphore barberDone = new Semaphore(0, true);

        Barber b = new Barber(customer, barber, customerDone, barberDone);
        Thread barberThread = new Thread(b);
        barberThread.start();

        while (true) {
            new Thread(new Customer(mutex, customer, barber, customerDone, barberDone)).start();
        }
    }
}
