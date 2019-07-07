package barber.sleepingbarber.semaphore;

import java.util.concurrent.Semaphore;

class Customer implements Runnable {
    private Semaphore mutex;
    private Semaphore customer;
    private Semaphore barber;
    private Semaphore customerDone;
    private Semaphore barberDone;

    private String id;

    Customer(Semaphore mutex, Semaphore customer, Semaphore barber, Semaphore customerDone, Semaphore barberDone) {
        this.mutex = mutex;
        this.customer = customer;
        this.barber = barber;
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
            if (BarberShop.customers == BarberShop.N_CUSTOMERS) {
                mutex.release();
                balk();
                return;
            }
            BarberShop.customers++;
            mutex.release();

            customer.release();
            barber.acquire();
            getHairCut();
            customerDone.release();
            barberDone.acquire();

            mutex.acquire();
            BarberShop.customers--;
            mutex.release();
        } catch (InterruptedException e) {
            System.err.println("Customer leaving");
        }
    }
}
