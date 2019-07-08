package barber.fifo.semaphore;

import java.util.concurrent.Semaphore;

class Customer implements Runnable {
    private BarberShop barberShop;
    private Semaphore sem;
    private Semaphore mutex;
    private Semaphore customer;
    private Semaphore customerDone;
    private Semaphore barberDone;

    private String id;

    Customer(BarberShop barberShop, Semaphore mutex, Semaphore customer, Semaphore customerDone, Semaphore barberDone) {
        sem = new Semaphore(0, true);
        this.barberShop = barberShop;
        this.mutex = mutex;
        this.customer = customer;
        this.customerDone = customerDone;
        this.barberDone = barberDone;
    }

    private void getHairCut() {
        System.out.println("Customer " + id + " getting haircut.");
    }

    private void balk() {
        System.out.println("Barber shop full. Customer " + id + " balk.");
    }

    @Override
    public void run() {
        id = String.valueOf(Thread.currentThread().getId());
        try {
            mutex.acquire();
            if (barberShop.isFull()) {
                mutex.release();
                balk();
                return;
            }
            barberShop.addCustomer(sem);
            mutex.release();

            customer.release();
            sem.acquire();

            getHairCut();

            customerDone.release();
            barberDone.acquire();
        } catch (InterruptedException e) {
        }
    }
}
