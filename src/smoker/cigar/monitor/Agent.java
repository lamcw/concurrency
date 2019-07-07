package smoker.cigar.monitor;

import java.util.HashMap;
import java.util.Map;

import smoker.Ingredient;

class Agent implements Runnable {
    private Table table;
    private final Map<TableIngredient, Smoker> smokerMap;

    Agent(Table table, Map smokerMap) {
        this.table = table;
        this.smokerMap = smokerMap;
    }

    public synchronized void putIngredients() {
        TableIngredient randIngredient = TableIngredient.random(smokerMap.keySet());
        table.setIngredient(randIngredient);

        Ingredient in1 = randIngredient.getFirst();
        Ingredient in2 = randIngredient.getSecond();
        System.out.println("Agent put " + in1.name() + " and " + in2.name());

        Smoker smoker = smokerMap.get(randIngredient);
        synchronized (smoker) {
            smoker.notify();
        }

        try {
            wait();
        } catch (InterruptedException e) {
            System.err.println("Agent stopped");
        }
    }

    @Override
    public void run() {
        while (true) {
            putIngredients();
        }
    }
}
