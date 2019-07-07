package smoker.cigar.monitor;

import java.util.Set;
import java.util.Random;

import smoker.Ingredient;

class TableIngredient {
    private final Ingredient in1;
    private final Ingredient in2;

    TableIngredient(Ingredient in1, Ingredient in2) {
        this.in1 = in1;
        this.in2 = in2;
    }

    Ingredient getFirst() {
        return in1;
    }

    Ingredient getSecond() {
        return in2;
    }

    static TableIngredient random(Set<TableIngredient> set) {
        Random rand = new Random();
        return (TableIngredient) set.toArray()[rand.nextInt(set.size())];
    }

    @Override
    public String toString() {
        return "(" + in1.name() + ", " + in2.name() + ")";
    }

    @Override
    public int hashCode() {
        return (in1.name() + in2.name()).hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o instanceof TableIngredient) {
            TableIngredient other = (TableIngredient) o;
            return in1.equals(other.in1) && in2.equals(other.in2);
        }

        return false;
    }
}
