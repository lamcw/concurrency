package smoker.cigar.semaphore;

import java.util.concurrent.Semaphore;
import java.util.EnumMap;

import smoker.Ingredient;

class Solution {
    private static volatile Boolean isTobacco = false;
    private static volatile Boolean isPaper = false;
    private static volatile Boolean isMatch = false;

    public static void main(String[] args) {
        Semaphore tobaccoSem = new Semaphore(0, true);
        Semaphore paperSem = new Semaphore(0, true);
        Semaphore matchSem = new Semaphore(0, true);

        Semaphore tobacco = new Semaphore(0, true);
        Semaphore paper = new Semaphore(0, true);
        Semaphore match = new Semaphore(0, true);

        Semaphore agentSemaphore = new Semaphore(1, true);

        Semaphore pusherMutex = new Semaphore(1, true);

        Runnable tobaccoPusher = () -> {
            while (true) {
                try {
                    tobacco.acquire();
                    pusherMutex.acquire();
                } catch (InterruptedException e) {
                    // no handling required
                }
                if (isPaper) {
                    isPaper = false;
                    matchSem.release();
                } else if (isMatch) {
                    isMatch = false;
                    paperSem.release();
                } else {
                    isTobacco = true;
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
                if (isPaper) {
                    isPaper = false;
                    tobaccoSem.release();
                } else if (isTobacco) {
                    isTobacco = false;
                    paperSem.release();
                } else {
                    isMatch = true;
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
                if (isTobacco) {
                    isTobacco = false;
                    matchSem.release();
                } else if (isMatch) {
                    isMatch = false;
                    tobaccoSem.release();
                } else {
                    isPaper = true;
                }
                pusherMutex.release();
            }
        };
        Thread tobaccoPusherThread = new Thread(tobaccoPusher);
        Thread paperPusherThread = new Thread(matchPusher);
        Thread matchPusherThread = new Thread(paperPusher);

        Thread tobaccoSmokerThread = new Thread(new Smoker("tobacco", tobaccoSem, agentSemaphore));
        Thread paperSmokerThread = new Thread(new Smoker("paper", paperSem, agentSemaphore));
        Thread matchSmokerThread = new Thread(new Smoker("match", matchSem, agentSemaphore));

        EnumMap<Ingredient, Semaphore> semMap = new EnumMap<Ingredient, Semaphore>(Ingredient.class);
        semMap.put(Ingredient.TOBACCO, tobacco);
        semMap.put(Ingredient.PAPER, paper);
        semMap.put(Ingredient.MATCH, match);

        Thread agentThread = new Thread(new Agent(agentSemaphore, semMap));

        tobaccoPusherThread.start();
        paperPusherThread.start();
        matchPusherThread.start();

        tobaccoSmokerThread.start();
        paperSmokerThread.start();
        matchSmokerThread.start();

        agentThread.start();
    }
}
