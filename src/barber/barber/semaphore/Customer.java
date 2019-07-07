package barber.barber.semaphore;

import java.util.concurrent.Semaphore;

class Customer implements Runnable {
    private Semaphore mutex;
    private Semaphore customer;
    private Semaphore barber;
    private Semaphore customerDone;
    private Semaphore barberDone;

    Customer(Semaphore mutex, Semaphore customer, Semaphore barber, Semaphore customerDone, Semaphore barberDone) {
        this.mutex = mutex;
        this.customer = customer;
        this.barber = barber;
        this.customerDone = customerDone;
        this.barberDone = barberDone;
    }

    void getHairCut() {
        System.out.println("Customer getting haircut.");
    }

    void balk() {
        System.out.println("Barber shop full. Balk.");
    }

    @Override
    public void run() {
        while (true) {
            try {
                mutex.acquire();
                if (BarberShop.customers == BarberShop.N_CUSTOMERS) {
                    mutex.release();
                    balk();
                    break;
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
            }
        }
    }
}
