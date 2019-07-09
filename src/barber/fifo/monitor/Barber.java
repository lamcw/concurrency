package barber.fifo.monitor;

import java.util.Queue;

class Barber implements Runnable {
    BarberShop barberShop;

    Barber(BarberShop barberShop) {
        this.barberShop = barberShop;
    }

    private void cutHair() {
        System.out.println("Barber cutting hair.");
    }

    private synchronized void work() {
        try {
            wait();
        } catch (InterruptedException e) {
        }
        Customer customer = barberShop.getNextCustomer();
        synchronized (customer) {
            customer.notify();
        }
        cutHair();
    }

    @Override
    public void run() {
        while (true) {
            work();
        }
    }
}
