package smoker.generalized.semaphore;

import java.util.concurrent.Semaphore;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Random;

import smoker.Ingredient;

class Agent implements Runnable {
    private EnumMap<Ingredient, Semaphore> ingredientSemEnumMap;

    Agent(EnumMap<Ingredient, Semaphore> ingredientSemEnumMap) {
        this.ingredientSemEnumMap = ingredientSemEnumMap;
    }

    @Override
    public void run() {
        while (true) {
            Ingredient in1 = Ingredient.getRandom();

            EnumSet<Ingredient> ingredientEnumSet = EnumSet.complementOf(EnumSet.of(in1));
            Random rand = new Random();
            int setSize = ingredientEnumSet.size();
            Ingredient in2 = (Ingredient) ingredientEnumSet.toArray()[rand.nextInt(setSize)];

            Semaphore in1Semaphore = ingredientSemEnumMap.get(in1);
            Semaphore in2Semaphore = ingredientSemEnumMap.get(in2);

            System.out.println("Agent placed " + in1.name() + " and " + in2.name());
            in1Semaphore.release();
            in2Semaphore.release();
        }
    }
}
