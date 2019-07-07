package smoker.cigar.monitor;

import smoker.Ingredient;

class Smoker implements Runnable {
    private final String name;
    private Table table;
    private Agent agent;

    Smoker(String name, Table table, Agent agent) {
        this.name = name;
        this.table = table;
        this.agent = agent;
    }

    Smoker(String name, Table table) {
        this.name = name;
        this.table = table;
    }

    void setAgent(Agent agent) {
        this.agent = agent;
    }

    private synchronized void smoke() throws InterruptedException {
        if (table.isEmpty()) {
            // wait for agent to put ingredient on table
            wait();
        }

        TableIngredient in = table.getIngredient();
        Ingredient in1 = in.getFirst();
        Ingredient in2 = in.getSecond();
        System.out.println(this + " making " + in1.name() + " and " + in2.name());
        System.out.println(this + " smoking with " + in1.name() + " and " + in2.name());

        synchronized (agent) {
            agent.notify();
        }

        wait();
    }

    @Override
    public void run() {
        while (true) {
            try {
                smoke();
            } catch (InterruptedException e) {
                System.err.println(this + " stopped smoking");
            }
        }
    }

    @Override
    public String toString() {
        return name + " smoker";
    }
}
