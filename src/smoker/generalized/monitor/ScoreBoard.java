package smoker.generalized.monitor;

import java.util.EnumMap;

import smoker.Ingredient;

// Calling it a "ScoreBoard" instead of Table because the book uses "scoreboard"
class ScoreBoard {
    private EnumMap<Ingredient, Integer> countEnumMap;

    ScoreBoard() {
        countEnumMap = new EnumMap<>(Ingredient.class);
        countEnumMap.put(Ingredient.TOBACCO, 0);
        countEnumMap.put(Ingredient.PAPER, 0);
        countEnumMap.put(Ingredient.MATCH, 0);
    }

    synchronized boolean hasIngredient(Ingredient ingredient) {
        return getCount(ingredient) > 0;
    }

    synchronized Integer getCount(Ingredient ingredient) {
        Integer ret = countEnumMap.get(ingredient);

        if (ret == null) {
            return 0;
        }

        return ret;
    }

    synchronized boolean takeIngredient(Ingredient ingredient) {
        Integer count = getCount(ingredient);

        if (count <= 0) {
            return false;
        }

        count--;
        countEnumMap.replace(ingredient, count);

        return true;
    }

    synchronized void putIngredient(Ingredient ingredient) {
        Integer count = getCount(ingredient);
        count++;
        countEnumMap.replace(ingredient, count);
    }

    synchronized boolean isEmpty() {
        return countEnumMap.get(Ingredient.TOBACCO) == 0 && countEnumMap.get(Ingredient.PAPER) == 0
                && countEnumMap.get(Ingredient.MATCH) == 0;
    }
}
