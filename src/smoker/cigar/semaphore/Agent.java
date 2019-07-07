package smoker.cigar.semaphore;

import java.util.concurrent.Semaphore;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Random;

import smoker.Ingredient;

class Agent implements Runnable {
    private Semaphore agentSemaphore;
    private EnumMap<Ingredient, Semaphore> ingredientSemEnumMap;

    Agent(Semaphore agentSemaphore, EnumMap<Ingredient, Semaphore> ingredientSemEnumMap) {
        this.agentSemaphore = agentSemaphore;
        this.ingredientSemEnumMap = ingredientSemEnumMap;
    }

    @Override
    public void run() {
        while (true) {
            try {
                agentSemaphore.acquire();
            } catch (InterruptedException e) {
            }
            Ingredient in = Ingredient.getRandom();

            // get the other ingredient
            EnumSet<Ingredient> ingredientEnumSet = EnumSet.complementOf(EnumSet.of(in));
            Random rand = new Random();
            int setSize = ingredientEnumSet.size();
            Ingredient in2 = (Ingredient) ingredientEnumSet.toArray()[rand.nextInt(setSize)];

            assert in != in2;

            System.out.println("Agent placed " + in.name() + " and " + in2.name());
            ingredientSemEnumMap.get(in).release();
            ingredientSemEnumMap.get(in2).release();
        }
    }
}
