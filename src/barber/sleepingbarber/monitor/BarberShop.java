package barber.sleepingbarber.monitor;

import java.util.HashSet;
import java.util.Set;

class BarberShop {
    private final static int MAX_CUSTOMERS = 4;
    private int customers;
    private Barber barber;

    BarberShop() {
        barber = new Barber();
        customers = 0;
    }

    Barber getBarber() {
        return barber;
    }

    synchronized boolean isFull() {
        return customers == MAX_CUSTOMERS;
    }

    synchronized void enterShop() {
        customers++;
    }

    synchronized void leaveShop() {
        customers--;
    }

    public static void main(String[] args) {
        BarberShop barberShop = new BarberShop();
        Thread barberThread = new Thread(barberShop.getBarber());
        barberThread.start();

        while (true) {
            Customer customer = new Customer(barberShop);
            new Thread(customer).start();
        }
    }
}
