package gui;

import entity.Astar;
import entity.MyPoint;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Panel extends JPanel {

    private int WINDOW_SIZE;
    private final int CELL_SIZE;   //The size of a cell.
    private final int CELLS_PER_ROW;//Number of cells both in a row and in a column, since they have the same size.
    private Astar astar;    //Used to show the current state.

    /**
     * Paints the scene.
     * @param g Graphics passed to super class.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        g.setFont(new Font("Monospace", Font.PLAIN, 10));
        g.setColor(Color.BLACK);
        for (int i = 0; i < CELLS_PER_ROW; i++) {       //Travels trhough all the lines
            for (int j = 0; j < CELLS_PER_ROW; j++) {   //and columns...
                if (astar.getGrid()[i][j].isObstacle()) {     //draws obstacle
                    g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                } else if (astar.getGrid()[i][j].isOpen() && !astar.getGrid()[i][j].isVisited()) { //draws opened cells
                    g.setColor(Color.LIGHT_GRAY);
                    g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    g.setColor(Color.BLACK);
                }
                if (astar.getGrid()[i][j].isVisited()) { //draws visited cells
                    g.setColor(Color.GREEN);
                    g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    if (astar.getGrid()[i][j].equals(astar.getInitial())) {
                        g.setColor(Color.BLUE);
                        g.fillRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                    g.setColor(Color.BLACK);
                }
                g.drawRect(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE); //draws the grid
                if (astar.getGrid()[i][j].isOpen() && CELLS_PER_ROW <= 17) {
                    g.drawString("H: " + String.format("%.2f", astar.getGrid()[i][j].getHvalue()), (i * CELL_SIZE + 1), (j * CELL_SIZE + 11));
                    g.drawString("G: " + String.format("%.2f", astar.getGrid()[i][j].getGvalue()), (i * CELL_SIZE + 1), (j * CELL_SIZE + 22));
                    g.drawString("F: " + String.format("%.2f", astar.getGrid()[i][j].getFvalue()), (i * CELL_SIZE + 1), (j * CELL_SIZE + 32));
                }
            }
        }
        if (astar.getTarget().equals(astar.getCurrent())) {
            MyPoint current = astar.getCurrent();
            g.setColor(Color.BLUE);
            while (current.getFather() != null) {
                g.fillRect(current.x * CELL_SIZE, current.y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                current = current.getFather();
            }
        }
        g.setColor(Color.RED);
        g.fillRect(astar.getTarget().x * CELL_SIZE, astar.getTarget().y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        
    }

    /**
     * Constructor that calculates the number and size of the cells on the grid.
     * @param axisCells the number of cells in a row
     * @param astar the object of the algorithm
     */
    public Panel(int axisCells, Astar astar) {
        this.WINDOW_SIZE = 700;
        this.astar = astar;
        while (this.WINDOW_SIZE % axisCells != 0) { // Keeps lowering the number of cells until it gets an exact division.
            this.WINDOW_SIZE--;
        }
        CELL_SIZE = WINDOW_SIZE / axisCells;
        CELLS_PER_ROW = WINDOW_SIZE / CELL_SIZE;
    }

    public void setAstar(Astar astar) {
        this.astar = astar;
        repaint();
    }

}
