package barber.sleepingbarber.semaphore;

import java.util.concurrent.Semaphore;

class Barber implements Runnable {
    private Semaphore customer;
    private Semaphore barber;
    private Semaphore customerDone;
    private Semaphore barberDone;

    Barber(Semaphore customer, Semaphore barber, Semaphore customerDone, Semaphore barberDone) {
        this.customer = customer;
        this.barber = barber;
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
                barber.release();
                cutHair();
                customerDone.acquire();
                barberDone.release();
            } catch (InterruptedException e) {
                System.err.println("Closing the shop");
            }
        }
    }
}
