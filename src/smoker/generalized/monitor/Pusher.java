package smoker.generalized.monitor;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.ArrayList;

import smoker.Ingredient;

class Pusher implements Runnable {
    private Ingredient pushIngredient;
    private ScoreBoard scoreBoard;
    private EnumMap<Ingredient, Smoker> smokerEnumMap;

    Pusher(Ingredient pushIngredient, ScoreBoard scoreBoard, EnumMap smokerEnumMap) {
        this.pushIngredient = pushIngredient;
        this.scoreBoard = scoreBoard;
        this.smokerEnumMap = smokerEnumMap;
    }

    private EnumSet getIngredientComplement() {
        return EnumSet.complementOf(EnumSet.of(pushIngredient));
    }

    private Ingredient getUniqueIngredient(Ingredient in1, Ingredient in2) {
        return (Ingredient) EnumSet.complementOf(EnumSet.of(in1, in2)).toArray()[0];
    }

    private synchronized void push() {
        try {
            wait();
        } catch (InterruptedException e) {
        }

        List<Ingredient> otherIngredientList = new ArrayList(getIngredientComplement());
        Ingredient in1 = otherIngredientList.get(0);
        Ingredient in2 = otherIngredientList.get(1);

        if (scoreBoard.hasIngredient(in1)) {
            scoreBoard.takeIngredient(in1);
            Smoker s = smokerEnumMap.get(getUniqueIngredient(pushIngredient, in1));
            synchronized (s) {
                s.notify();
            }
        } else if (scoreBoard.hasIngredient(in2)) {
            scoreBoard.takeIngredient(in2);
            Smoker s = smokerEnumMap.get(getUniqueIngredient(pushIngredient, in2));
            synchronized (s) {
                s.notify();
            }
        } else {
            scoreBoard.putIngredient(pushIngredient);
        }
    }

    @Override
    public void run() {
        while (true) {
            push();
        }
    }

    @Override
    public String toString() {
        return pushIngredient.name() + " pusher";
    }
}
