package entity;

import java.util.Random;

public class Astar {

    private MyPoint[][] grid;
    private MyPoint target;
    private MyPoint current;
    private MyPoint initial;
    private int numberOfObstacles;
    private int gridSize;

    /**
     * Constructor of the algorithm.
     *
     * @param start the starting point.
     * @param target the target point.
     * @param gridSize the grid size.
     * @param obstaclePercentage the percent of cells that will be an obstacle
     * (rounded to floor).
     */
    public Astar(MyPoint start, MyPoint target, int gridSize, int obstaclePercentage) {
        initial = start;
        grid = new MyPoint[gridSize][gridSize];     //grid creation
        this.target = target;
        this.gridSize = gridSize;
        numberOfObstacles = ((gridSize * gridSize) * obstaclePercentage) / 100;
        generateGridPoints();
        current = grid[start.x][start.y];
        current.setOpen(true);
        current.setVisited(true);
        calculateHvalues();
        calculateGvalues();
        generateObstaclePoints();
    }

    /**
     * Marks all the neighbors (if not obstacle) of the current cell as open.
     */
    public void expandCurrentCell() {
        if (current.x - 1 >= 0 && !grid[current.x - 1][current.y].isOpen() && !grid[current.x - 1][current.y].isObstacle()) {
            grid[current.x - 1][current.y].setOpen(true);
            grid[current.x - 1][current.y].setFather(current);
        }
        if (current.y - 1 >= 0 && !grid[current.x][current.y - 1].isOpen() && !grid[current.x][current.y - 1].isObstacle()) {
            grid[current.x][current.y - 1].setOpen(true);
            grid[current.x][current.y - 1].setFather(current);
        }
        if (current.x + 1 < gridSize && !grid[current.x + 1][current.y].isOpen() && !grid[current.x + 1][current.y].isObstacle()) {
            grid[current.x + 1][current.y].setOpen(true);
            grid[current.x + 1][current.y].setFather(current);
        }
        if (current.y + 1 < gridSize && !grid[current.x][current.y + 1].isOpen() && !grid[current.x][current.y + 1].isObstacle()) {
            grid[current.x][current.y + 1].setOpen(true);
            grid[current.x][current.y + 1].setFather(current);
        }
    }

    /**
     * Chooses the next current cell based on open neighbors. Doesn't allows
     * cells already visited.
     */
    public void chooseNext() {
        double fvalue = -1;
        int x = current.x, y = current.y;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j].isOpen() && !grid[i][j].isVisited()) {
                    if (fvalue < 0 || grid[i][j].getFvalue() < fvalue) {
                        fvalue = grid[i][j].getFvalue();
                        current = grid[i][j];
                    }
                }
            }
        }
        grid[current.x][current.y].setVisited(true); // marks current as visited
    }

    /**
     * Calculates the G Function to all the cells on the grid.
     */
    private void calculateGvalues() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j].calculateGvalue(initial);
            }
        }
    }

    /**
     * Calculates the H Function to all the cells on the grid.
     */
    private void calculateHvalues() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j].calculateHvalue(target);
            }
        }
    }

    /**
     * Generates all the MyPoints (cells) of the grid.
     */
    private void generateGridPoints() {
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = new MyPoint(i, j);
            }
        }
    }

    /**
     * Generates all the MyPoints (cells) that are an obstacle.
     */
    private void generateObstaclePoints() {
        Random r = new Random(System.currentTimeMillis());
        for (int i = 0; i < numberOfObstacles; i++) {
            int x = r.nextInt(gridSize);
            int y = r.nextInt(gridSize);
            if ((current.x == x && current.y == y) || (target.x == x && target.y == y) || grid[x][y].isObstacle()) {
                i--;
            } else {
                grid[x][y].setObstacle(true);
            }
        }
    }

    public MyPoint[][] getGrid() {
        return grid;
    }

    public void setGrid(MyPoint[][] grid) {
        this.grid = grid;
    }

    public MyPoint getTarget() {
        return target;
    }

    public void setTarget(MyPoint target) {
        this.target = target;
    }

    public MyPoint getCurrent() {
        return current;
    }

    public void setCurrent(MyPoint current) {
        this.current = current;
    }

    public MyPoint getInitial() {
        return initial;
    }

    public void setInitial(MyPoint initial) {
        this.initial = initial;
    }

    public int getNumberOfObstacles() {
        return numberOfObstacles;
    }

    public void setNumberOfObstacles(int numberOfObstacles) {
        this.numberOfObstacles = numberOfObstacles;
    }

}
