package smoker.generalized.monitor;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Random;

import smoker.Ingredient;

class Agent implements Runnable {
    private final EnumMap<Ingredient, Pusher> pusherMap;

    Agent(EnumMap<Ingredient, Pusher> pusherMap) {
        this.pusherMap = pusherMap;
    }

    private synchronized void putIngredients() {
        Ingredient in1 = Ingredient.getRandom();
        Random rand = new Random();
        EnumSet<Ingredient> ingredientEnumSet = EnumSet.complementOf(EnumSet.of(in1));
        int setSize = ingredientEnumSet.size();
        Ingredient in2 = (Ingredient) ingredientEnumSet.toArray()[rand.nextInt(setSize)];

        System.out.println("Agent put " + in1.name() + " and " + in2.name());

        Pusher pusher1 = pusherMap.get(in1);
        Pusher pusher2 = pusherMap.get(in2);

        synchronized (pusher1) {
            pusher1.notify();
        }

        synchronized (pusher2) {
            pusher2.notify();
        }
    }

    @Override
    public void run() {
        while (true) {
            putIngredients();
        }
    }
}
