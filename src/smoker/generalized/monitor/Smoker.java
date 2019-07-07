package smoker.generalized.monitor;

import smoker.Ingredient;

class Smoker implements Runnable {
    private final String name;
    private Ingredient in1;
    private Ingredient in2;

    Smoker(String name, Ingredient in1, Ingredient in2) {
        this.name = name;
        this.in1 = in1;
        this.in2 = in2;
    }

    private synchronized void smoke() {
        try {
            wait();
        } catch (InterruptedException e) {
            // skip
        }

        System.out.println(this + " making " + in1.name() + " and " + in2.name());
        System.out.println(this + " smoking with " + in1.name() + " and " + in2.name());
    }

    @Override
    public void run() {
        while (true) {
            smoke();
        }
    }

    @Override
    public String toString() {
        return name + " smoker";
    }
}
