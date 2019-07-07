package smoker.cigar.semaphore;

import java.util.concurrent.Semaphore;

class Smoker implements Runnable {
    private String name;
    private Semaphore ingredientSemaphore;
    private Semaphore agentSemaphore;

    Smoker(String name, Semaphore ingredientSemaphore, Semaphore agentSemaphore) {
        this.name = name;
        this.ingredientSemaphore = ingredientSemaphore;
        this.agentSemaphore = agentSemaphore;
    }

    private void makeCigar() {
        System.out.println(name + " smoker is making cigarette");
    }

    private void smoke() {
        System.out.println(name + " smoker is smoking.");
    }

    @Override
    public void run() {
        while (true) {
            try {
                ingredientSemaphore.acquire();
            } catch (InterruptedException e) {
                // skip
            }
            makeCigar();
            agentSemaphore.release();
            smoke();
        }
    }
}
