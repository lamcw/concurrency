package smoker;

import java.util.Random;

public enum Ingredient {
    TOBACCO,
    PAPER,
    MATCH;

    public static Ingredient getRandom() {
        Ingredient[] v = values();
        Random rand = new Random();
        return v[rand.nextInt(v.length)];
    }
}
