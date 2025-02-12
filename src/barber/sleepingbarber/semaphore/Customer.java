package barber.sleepingbarber.semaphore;

import java.util.concurrent.Semaphore;

class Customer implements Runnable {
    private BarberShop barberShop;

    private Semaphore mutex;
    private Semaphore customer;
    private Semaphore barber;
    private Semaphore customerDone;
    private Semaphore barberDone;

    private String id;

    Customer(BarberShop barberShop, Semaphore mutex, Semaphore customer, Semaphore barber, Semaphore customerDone, Semaphore barberDone) {
        this.barberShop = barberShop;
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
            if (barberShop.isFull()) {
                mutex.release();
                balk();
                return;
            }
            barberShop.incCustomers();
            mutex.release();

            customer.release();
            barber.acquire();
            getHairCut();
            customerDone.release();
            barberDone.acquire();

            mutex.acquire();
            barberShop.decCustomers();
            mutex.release();
        } catch (InterruptedException e) {
            System.err.println("Customer leaving");
        }
    }
}
