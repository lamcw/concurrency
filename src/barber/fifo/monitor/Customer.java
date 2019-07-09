package barber.fifo.monitor;

class Customer implements Runnable {
    private String id;
    private BarberShop barberShop;

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
        barberShop.enterShop(this);
        return true;
    }

    private synchronized void leaveShop() {
        barberShop.leaveShop();
        System.out.println("Customer " + id + " left shop.");
    }

    @Override
    public void run() {
        id = String.valueOf(Thread.currentThread().getId());
        try {
            if (!joinShop()) {
                return;
            }

            synchronized (this) {
                wait();
            }

            getHairCut();
            leaveShop();
        } catch (InterruptedException e) {
        }
    }
}
