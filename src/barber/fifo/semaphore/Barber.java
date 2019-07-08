package barber.fifo.semaphore;

import java.util.concurrent.Semaphore;

class Barber implements Runnable {
    private BarberShop barberShop;

    private Semaphore customer;
    private Semaphore mutex;
    private Semaphore customerDone;
    private Semaphore barberDone;

    Barber(BarberShop barberShop, Semaphore customer, Semaphore mutex, Semaphore customerDone, Semaphore barberDone) {
        this.barberShop = barberShop;
        this.customer = customer;
        this.mutex = mutex;
        this.customerDone = customerDone;
        this.barberDone = barberDone;
    }

    void cutHair() {
        System.out.println("Barber cutting hair.");
    }

    @Override
    public void run() {
        while (true) {
            try {
                customer.acquire();
                mutex.acquire();
                Semaphore sem = barberShop.getCustomer();
                mutex.release();
                sem.release();
                cutHair();
                customerDone.acquire();
                barberDone.release();
            } catch (InterruptedException e) {
            }
        }
    }
}
