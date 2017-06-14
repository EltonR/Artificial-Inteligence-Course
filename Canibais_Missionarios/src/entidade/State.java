package entidade;

import java.util.Objects;

public class State {

    private int missionariesLeft;
    private int cannibalsLeft;
    private int missionariesRight;
    private int cannibalsRight;
    private String boatSide;
    private State father;
    private Operator generatorOperator;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final State other = (State) obj;
        if (this.missionariesLeft != other.missionariesLeft) {
            return false;
        }
        if (this.cannibalsLeft != other.cannibalsLeft) {
            return false;
        }
        if (this.missionariesRight != other.missionariesRight) {
            return false;
        }
        if (this.cannibalsRight != other.cannibalsRight) {
            return false;
        }
        if (!Objects.equals(this.boatSide, other.boatSide)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (boatSide.equals("esq")) {
            return "Estado <" + missionariesLeft + ", " + cannibalsLeft + ", " + boatSide + ">";
        } else {
            return "Estado <" + missionariesRight + ", " + cannibalsRight + ", " + boatSide + ">";
        }
    }

    //Constructor
    public State() {
        missionariesLeft = missionariesRight = cannibalsRight = cannibalsLeft = 0;
        boatSide = "esq";
        father = null;
    }

    /**
     * Checks if the current state is a valid state.
     *
     * @return true if valid, false if not.
     */
    public boolean isValid() {
        return ((cannibalsLeft <= missionariesLeft || missionariesLeft == 0) && (cannibalsRight <= missionariesRight || missionariesRight == 0));
    }

    /**
     * Checks if a state already exists on the tree. If not, it recursively
     * compares it to the father. If it has no father, it is the initial node.
     *
     * @param state the state being compared.
     * @return true if it is a new node, false if not.
     */
    public boolean isNewNode(State state) {
        if (father == null) {
            return true;
        }
        if (father.equals(state)) {
            return false;
        }
        return father.isNewNode(state);
    }

    /**
     * Tests if the current state can generate a new valid state with a given
     * operator.
     *
     * @param operador the operator of the test.
     * @return a new state if it can generate, null if not.
     */
    public State expandNode(Operator operador) {
        State e = new State();
        if (boatSide.equals("esq")) {
            if ((cannibalsLeft >= operador.getNumCannibals()) && (missionariesLeft >= operador.getNumMissionaries())) {
                e.setCannibalsRight(cannibalsRight + operador.getNumCannibals());
                e.setCannibalsLeft(cannibalsLeft - operador.getNumCannibals());
                e.setMissionariesRight(missionariesRight + operador.getNumMissionaries());
                e.setMissionariesLeft(missionariesLeft - operador.getNumMissionaries());
                e.setBoatSide("dir");
            } else {
                return null;
            }
        } else {  //lado_barco.equals("dir")
            if ((cannibalsRight >= operador.getNumCannibals()) && (missionariesRight >= operador.getNumMissionaries())) {
                e.setCannibalsRight(cannibalsRight - operador.getNumCannibals());
                e.setCannibalsLeft(cannibalsLeft + operador.getNumCannibals());
                e.setMissionariesRight(missionariesRight - operador.getNumMissionaries());
                e.setMissionariesLeft(missionariesLeft + operador.getNumMissionaries());
                e.setBoatSide("esq");
            } else {
                return null;
            }
        }
        e.setFather(this);
        return e;
    }

    /**
     * Prints the solution trace on reverse (from children to father).
     */
    public void prinSolution() {
        if (father != null) {
            father.prinSolution();
        }
        System.out.println(generatorOperator);
        System.out.println(this) ;
    }

    //Getters e Setters
    public int getMissionariesLeft() {
        return missionariesLeft;
    }

    public void setMissionariesLeft(int missionariesLeft) {
        this.missionariesLeft = missionariesLeft;
    }

    public int getCannibalsLeft() {
        return cannibalsLeft;
    }

    public void setCannibalsLeft(int cannibalsLeft) {
        this.cannibalsLeft = cannibalsLeft;
    }

    public int getMissionariesRight() {
        return missionariesRight;
    }

    public void setMissionariesRight(int missionariesRight) {
        this.missionariesRight = missionariesRight;
    }

    public int getCannibalsRight() {
        return cannibalsRight;
    }

    public void setCannibalsRight(int cannibalsRight) {
        this.cannibalsRight = cannibalsRight;
    }

    public String getBoatSide() {
        return boatSide;
    }

    public void setBoatSide(String boatSide) {
        this.boatSide = boatSide;
    }

    public State getFather() {
        return father;
    }

    public void setFather(State pai) {
        this.father = pai;
    }

    public Operator getGeneratorOperator() {
        return generatorOperator;
    }

    public void setGeneratorOperator(Operator operadorGerador) {
        this.generatorOperator = operadorGerador;
    }

}
