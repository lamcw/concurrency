package barber.hilzer.semaphore;

import java.util.concurrent.Semaphore;

class Customer implements Runnable {
    private String id;
    private BarberShop barberShop;

    private Semaphore sem1;
    private Semaphore sem2;
    private Semaphore mutex;
    private Semaphore customer1;
    private Semaphore customer2;
    private Semaphore sofa;
    private Semaphore payment;
    private Semaphore receipt;

    Customer(BarberShop barberShop, Semaphore mutex, Semaphore customer1, Semaphore customer2, Semaphore sofa,
            Semaphore payment, Semaphore receipt) {
        sem1 = new Semaphore(0, true);
        sem2 = new Semaphore(0, true);
        this.barberShop = barberShop;
        this.mutex = mutex;
        this.customer1 = customer1;
        this.customer2 = customer2;
        this.sofa = sofa;
        this.payment = payment;
        this.receipt = receipt;
    }

    private void balk() {
        System.out.println("Barber shop full. Customer " + id + " balk.");
    }

    private void enterShop() {
        System.out.println("Customer " + id + " enters shop.");
    }

    private void sitOnSofa() {
        System.out.println("Customer " + id + " sits on sofa.");
    }

    private void getHairCut() {
        System.out.println("Customer " + id + " is getting haircut.");
    }

    private void pay() {
        System.out.println("Customer " + id + " pays.");
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
            barberShop.putSofaQueue(sem1);
            mutex.release();

            enterShop();
            customer1.release();
            sem1.acquire();

            sofa.acquire();
            sitOnSofa();
            sem1.release();
            mutex.acquire();
            barberShop.putChairQueue(sem2);
            mutex.release();
            customer2.release();
            sem2.acquire();
            sofa.release();

            getHairCut();

            pay();
            payment.release();
            receipt.acquire();

            mutex.acquire();
            barberShop.decCustomers();
            mutex.release();
        } catch (InterruptedException e) {
        }
    }
}
