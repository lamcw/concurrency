package barber.sleepingbarber.monitor;

class Customer implements Runnable {
    private BarberShop barberShop;
    private String id;

    Customer(BarberShop barberShop) {
        this.barberShop = barberShop;
    }

    private void getHairCut() {
        System.out.println("Customer " + id + " getting haircut.");
    }

    private void balk() {
        System.out.println("Barber shop full. Customer " + id + " balk.");
    }

    private synchronized boolean joinShop() {
        if (barberShop.isFull()) {
            balk();
            return false;
        }
        barberShop.enterShop();
        return true;
    }

    private synchronized void leaveShop() {
        barberShop.leaveShop();
        System.out.println("Customer " + id + " left shop.");
    }

    @Override
    public void run() {
        id = String.valueOf(Thread.currentThread().getId());
        Barber barber = barberShop.getBarber();
        try {
            if (!joinShop()) {
                return;
            }

            synchronized (barber) {
                // wakes up barber
                barber.notify();
                // wait for my turn
                barber.wait();
            }

            getHairCut();

            leaveShop();
        } catch (InterruptedException e) {
            System.err.println("Customer leaving");
        }
    }
}
