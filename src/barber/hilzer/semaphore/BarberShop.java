package barber.hilzer.semaphore;

import java.util.concurrent.Semaphore;
import java.util.Queue;
import java.util.ArrayDeque;

class BarberShop {
    private final static int MAX_CUSTOMERS = 20;
    private int customers = 0;
    // queue1
    private Queue sofaQueue;
    // queue2
    private Queue chairQueue;

    BarberShop() {
        sofaQueue = new ArrayDeque<Semaphore>();
        chairQueue = new ArrayDeque<Semaphore>();
        customers = 0;
    }

    boolean isFull() {
        return customers == MAX_CUSTOMERS;
    }

    Semaphore semFromSofaQueue() {
        return (Semaphore) sofaQueue.poll();
    }

    Semaphore semFromChairQueue() {
        return (Semaphore) chairQueue.poll();
    }

    void putSofaQueue(Semaphore sem) {
        sofaQueue.offer(sem);
    }

    void putChairQueue(Semaphore sem) {
        chairQueue.offer(sem);
    }

    void incCustomers() {
        customers++;
    }

    void decCustomers() {
        customers--;
    }

    public static void main(String[] args) {
        BarberShop barberShop = new BarberShop();

        Semaphore mutex = new Semaphore(1, true);
        Semaphore sofa = new Semaphore(4, true);
        Semaphore customer1 = new Semaphore(0, true);
        Semaphore customer2 = new Semaphore(0, true);
        Semaphore barber = new Semaphore(0, true);
        Semaphore payment = new Semaphore(0, true);
        Semaphore receipt = new Semaphore(0, true);

        Barber b = new Barber(barberShop, customer1, customer2, mutex, barber, payment, receipt);
        Thread barberThread = new Thread(b);
        barberThread.start();

        while (true) {
            Customer c = new Customer(barberShop, mutex, customer1, customer2, sofa, payment, receipt);
            new Thread(c).start();
        }
    }
}
