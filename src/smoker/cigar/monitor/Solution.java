package smoker.cigar.monitor;

import java.util.Map;
import java.util.HashMap;

import smoker.Ingredient;

class Solution {
    public static void main(String[] args) {
        Table table = new Table();

        Smoker tobaccoSmoker = new Smoker("tobacco", table);
        Smoker paperSmoker = new Smoker("paper", table);
        Smoker matchSmoker = new Smoker("match", table);

        final Map<TableIngredient, Smoker> map = new HashMap<TableIngredient, Smoker>(Ingredient.values().length);
        map.put(new TableIngredient(Ingredient.PAPER, Ingredient.MATCH), tobaccoSmoker);
        map.put(new TableIngredient(Ingredient.MATCH, Ingredient.TOBACCO), paperSmoker);
        map.put(new TableIngredient(Ingredient.TOBACCO, Ingredient.PAPER), matchSmoker);

        Agent agent = new Agent(table, map);

        tobaccoSmoker.setAgent(agent);
        paperSmoker.setAgent(agent);
        matchSmoker.setAgent(agent);

        new Thread(tobaccoSmoker).start();
        new Thread(paperSmoker).start();
        new Thread(matchSmoker).start();

        new Thread(agent).start();
    }
}
