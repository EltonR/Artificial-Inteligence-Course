package entity;

import java.awt.Point;

public class MyPoint extends Point {

    boolean open;       //Keeps track wheter the node is opened (expanded) or not.
    boolean obstacle;   //If the node is an obstacle...
    boolean visited;    //If the node was already visited
    double gvalue;      //Value of the g function.
    double hvalue;      //Value of the h function.
    MyPoint father;

    public MyPoint() {
        open = false;
        obstacle = false;
        visited = false;
        father = null;
    }

    @Override
    public String toString() {
        return "{" + x + "," + y + "}";
    }

    /**
     * Prints the current Path based on father's recursion
     */
    public void printStack() {
        if (father != null) {
            father.printStack();
        }
        System.out.print("->" + this);
    }

    /**
     * Prints the final path.
     */
    public int printFinalStack(int cost) {
        if (father != null) {
            father.printFinalStack(cost + 1);
        }
        if (father == null) {
            System.out.println("Total cost: " + (cost));
        }
        System.out.print("->" + this);
        return cost;
    }

    /**
     * As this class extends from Point, we set x and y from the Point Class.
     *
     * @param x the x axis value
     * @param y the y axis value
     */
    public MyPoint(int x, int y) {
        super(x, y);
    }

    /**
     * Calculates the value of G function
     * @param initial the initial point.
     */
    public void calculateGvalue(MyPoint initial) {
        gvalue = Math.abs(this.x - initial.x) + Math.abs(this.y - initial.y);
    }

    /**
     * Calculates the value of F function
     * @param target the Final point.
     */
    public void calculateHvalue(MyPoint target) {
        hvalue = Math.sqrt(Math.pow((x - target.x), 2) + Math.pow((y - target.y), 2));
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isObstacle() {
        return obstacle;
    }

    public void setObstacle(boolean obstacle) {
        this.obstacle = obstacle;
    }

    public double getGvalue() {
        return gvalue;
    }

    public void setGvalue(double gvalue) {
        this.gvalue = gvalue;
    }

    public double getHvalue() {
        return hvalue;
    }

    public void setHvalue(float hvalue) {
        this.hvalue = hvalue;
    }

    public double getFvalue() {
        return gvalue + hvalue;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public MyPoint getFather() {
        return father;
    }

    public void setFather(MyPoint father) {
        this.father = father;
    }

}
