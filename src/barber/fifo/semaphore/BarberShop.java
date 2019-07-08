package barber.fifo.semaphore;

import java.util.concurrent.Semaphore;
import java.util.Queue;
import java.util.ArrayDeque;

class BarberShop {
    private final static int MAX_CUSTOMERS = 4;
    private volatile Queue queue;

    BarberShop() {
        queue = new ArrayDeque<Semaphore>(MAX_CUSTOMERS);
    }

    boolean isFull() {
        return queue.size() == MAX_CUSTOMERS;
    }

    void addCustomer(Semaphore sem) {
        queue.offer(sem);
    }

    Semaphore getCustomer() {
        return (Semaphore) queue.poll();
    }

    public static void main(String[] args) {
        BarberShop barberShop = new BarberShop();
        Semaphore mutex = new Semaphore(1, true);
        Semaphore customer = new Semaphore(0, true);
        Semaphore customerDone = new Semaphore(0, true);
        Semaphore barberDone = new Semaphore(0, true);

        Barber b = new Barber(barberShop, customer, mutex, customerDone, barberDone);
        Thread barberThread = new Thread(b);
        barberThread.start();

        while (true) {
            Customer c = new Customer(barberShop, mutex, customer, customerDone, barberDone);
            new Thread(c).start();
        }
    }
}
