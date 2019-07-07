package smoker.cigar.monitor;

class Table {
    private TableIngredient tableIngredient;

    synchronized TableIngredient getIngredient() {
        return tableIngredient;
    }

    synchronized void setIngredient(TableIngredient ingredient) {
        tableIngredient = ingredient;
    }

    synchronized boolean isEmpty() {
        return tableIngredient == null;
    }
}
