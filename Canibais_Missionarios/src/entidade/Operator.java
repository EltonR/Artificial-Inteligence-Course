package entidade;

public class Operator {

    private int numMissionaries;
    private int numCannibals;

    /**
     * Operator Class.
     *
     * @param numMissionaries number of missionaries
     * @param numCannibals number of cannibals
     */
    public Operator(int numMissionaries, int numCannibals) {
        this.numMissionaries = numMissionaries;
        this.numCannibals = numCannibals;
    }

    @Override
    public String toString() {
        return "<" + numMissionaries + ", " + numCannibals + '>';
    }

    public int getNumMissionaries() {
        return numMissionaries;
    }

    public void setNumMissionaries(int mis) {
        this.numMissionaries = mis;
    }

    public int getNumCannibals() {
        return numCannibals;
    }

    public void setNumCannibals(int can) {
        this.numCannibals = can;
    }

}
