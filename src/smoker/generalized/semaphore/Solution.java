package smoker.generalized.semaphore;

import java.util.concurrent.Semaphore;
import java.util.EnumMap;

import smoker.Ingredient;

class Solution {
    private static volatile int numTobacco = 0;
    private static volatile int numPaper = 0;
    private static volatile int numMatch = 0;

    public static void main(String[] args) {
        Semaphore tobacco = new Semaphore(0, true);
        Semaphore paper = new Semaphore(0, true);
        Semaphore match = new Semaphore(0, true);

        Semaphore tobaccoSem = new Semaphore(0, true);
        Semaphore paperSem = new Semaphore(0, true);
        Semaphore matchSem = new Semaphore(0, true);

        Semaphore pusherMutex = new Semaphore(1, true);

        Runnable tobaccoPusher = () -> {
            while (true) {
                try {
                    tobacco.acquire();
                    pusherMutex.acquire();
                } catch (InterruptedException e) {
                    // skip
                }
                if (numPaper > 0) {
                    numPaper--;
                    matchSem.release();
                } else if (numMatch > 0) {
                    numMatch--;
                    paperSem.release();
                } else {
                    numTobacco++;
                }
                pusherMutex.release();
            }
        };
        Runnable paperPusher = () -> {
            while (true) {
                try {
                    paper.acquire();
                    pusherMutex.acquire();
                } catch (InterruptedException e) {
                    // skip
                }
                if (numTobacco > 0) {
                    numTobacco--;
                    matchSem.release();
                } else if (numMatch > 0) {
                    numMatch--;
                    tobaccoSem.release();
                } else {
                    numPaper++;
                }
                pusherMutex.release();
            }
        };
        Runnable matchPusher = () -> {
            while (true) {
                try {
                    match.acquire();
                    pusherMutex.acquire();
                } catch (InterruptedException e) {
                    // skip
                }
                if (numPaper > 0) {
                    numPaper--;
                    tobaccoSem.release();
                } else if (numTobacco > 0) {
                    numTobacco--;
                    paperSem.release();
                } else {
                    numMatch++;
                }
                pusherMutex.release();
            }
        };

        Thread tobaccoPusherThread = new Thread(tobaccoPusher);
        Thread paperPusherThread = new Thread(matchPusher);
        Thread matchPusherThread = new Thread(paperPusher);

        Thread tobaccoSmokerThread = new Thread(new Smoker("tobacco", tobaccoSem));
        Thread paperSmokerThread = new Thread(new Smoker("paper", paperSem));
        Thread matchSmokerThread = new Thread(new Smoker("match", matchSem));

        EnumMap<Ingredient, Semaphore> semMap = new EnumMap<Ingredient, Semaphore>(Ingredient.class);
        semMap.put(Ingredient.TOBACCO, tobacco);
        semMap.put(Ingredient.PAPER, paper);
        semMap.put(Ingredient.MATCH, match);

        Thread agentThread = new Thread(new Agent(semMap));

        tobaccoPusherThread.start();
        paperPusherThread.start();
        matchPusherThread.start();

        tobaccoSmokerThread.start();
        paperSmokerThread.start();
        matchSmokerThread.start();

        agentThread.start();
    }
}
