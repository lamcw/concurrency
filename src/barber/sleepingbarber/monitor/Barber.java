package barber.sleepingbarber.monitor;

class Barber implements Runnable {
    private void cutHair() {
        System.out.println("Barber cutting hair.");
    }

    private synchronized void work() {
        try {
            // go to sleep. wait for customer to wake me up
            wait();
        } catch (InterruptedException e) {
        }
        // notify customer that I am ready
        notify();
        cutHair();
    }

    @Override
    public void run() {
        while (true) {
            work();
        }
    }
}
