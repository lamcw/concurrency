package barber.hilzer.semaphore;

import java.util.concurrent.Semaphore;

class Barber implements Runnable {
    private BarberShop barberShop;

    private Semaphore customer1;
    private Semaphore customer2;
    private Semaphore mutex;
    private Semaphore barber;
    private Semaphore payment;
    private Semaphore receipt;

    Barber(BarberShop barberShop, Semaphore customer1, Semaphore customer2, Semaphore mutex, Semaphore barber,
            Semaphore payment, Semaphore receipt) {
        this.barberShop = barberShop;
        this.customer1 = customer1;
        this.customer2 = customer2;
        this.mutex = mutex;
        this.barber = barber;
        this.payment = payment;
        this.receipt = receipt;
    }

    private void cutHair() {
        System.out.println("Barber cutting hair.");
    }

    private void acceptPayment() {
        System.out.println("Barber accepting payment.");
    }

    @Override
    public void run() {
        while (true) {
            try {
                customer1.acquire();
                mutex.acquire();
                Semaphore sofaSemaphore = barberShop.semFromSofaQueue();
                sofaSemaphore.release();
                sofaSemaphore.acquire();
                mutex.release();
                sofaSemaphore.release();

                customer2.acquire();
                mutex.acquire();
                Semaphore chairSemaphore = barberShop.semFromChairQueue();
                mutex.release();
                chairSemaphore.release();

                barber.release();
                cutHair();

                payment.acquire();
                acceptPayment();
                receipt.release();
            } catch (InterruptedException e) {
            }
        }
    }
}
