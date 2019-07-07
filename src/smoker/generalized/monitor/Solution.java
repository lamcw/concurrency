package smoker.generalized.monitor;

import java.util.EnumMap;

import smoker.Ingredient;

class Solution {
    public static void main(String[] args) {
        ScoreBoard scoreBoard = new ScoreBoard();

        Smoker tobaccoSmoker = new Smoker("tobacco", Ingredient.PAPER, Ingredient.MATCH);
        Smoker paperSmoker = new Smoker("paper", Ingredient.TOBACCO, Ingredient.MATCH);
        Smoker matchSmoker = new Smoker("match", Ingredient.TOBACCO, Ingredient.PAPER);

        EnumMap<Ingredient, Smoker> smokerEnumMap = new EnumMap<>(Ingredient.class);
        smokerEnumMap.put(Ingredient.TOBACCO, tobaccoSmoker);
        smokerEnumMap.put(Ingredient.PAPER, paperSmoker);
        smokerEnumMap.put(Ingredient.MATCH, matchSmoker);

        Pusher tobaccoPusher = new Pusher(Ingredient.TOBACCO, scoreBoard, smokerEnumMap);
        Pusher paperPusher = new Pusher(Ingredient.PAPER, scoreBoard, smokerEnumMap);
        Pusher matchPusher = new Pusher(Ingredient.MATCH, scoreBoard, smokerEnumMap);

        EnumMap<Ingredient, Pusher> pusherEnumMap = new EnumMap<>(Ingredient.class);
        pusherEnumMap.put(Ingredient.TOBACCO, tobaccoPusher);
        pusherEnumMap.put(Ingredient.PAPER, paperPusher);
        pusherEnumMap.put(Ingredient.MATCH, matchPusher);

        Agent agent = new Agent(pusherEnumMap);

        tobaccoPusher.setPushers(pusherEnumMap);
        paperPusher.setPushers(pusherEnumMap);
        matchPusher.setPushers(pusherEnumMap);

        new Thread(tobaccoSmoker).start();
        new Thread(paperSmoker).start();
        new Thread(matchSmoker).start();

        new Thread(tobaccoPusher).start();
        new Thread(paperPusher).start();
        new Thread(matchPusher).start();

        new Thread(agent).start();
    }
}
