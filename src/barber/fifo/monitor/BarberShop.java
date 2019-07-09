package barber.fifo.monitor;

import java.util.Queue;
import java.util.ArrayDeque;

class BarberShop {
    final static int MAX_CUSTOMERS = 4;
    final static int MAX_THREADS = 50;
    private int customers;
    private Queue queue;
    private Barber barber;

    BarberShop() {
        barber = new Barber(this);
        queue = new ArrayDeque<Customer>(MAX_CUSTOMERS);
        customers = 0;
    }

    Barber getBarber() {
        return barber;
    }

    synchronized Customer getNextCustomer() {
        return (Customer) queue.poll();
    }

    synchronized boolean isFull() {
        return customers == MAX_CUSTOMERS;
    }

    synchronized boolean isEmpty() {
        return customers == 0;
    }

    synchronized void enterShop(Customer customer) {
        customers++;
        queue.offer(customer);
        synchronized (barber) {
            barber.notify();
        }
    }

    synchronized void leaveShop() {
        customers--;
    }

    public static void main(String[] args) {
        BarberShop barberShop = new BarberShop();
        Thread barberThread = new Thread(barberShop.getBarber());
        barberThread.start();

        for (int i = 0; i < MAX_THREADS; i++) {
            Customer customer = new Customer(barberShop);
            new Thread(customer).start();
        }
    }
}
