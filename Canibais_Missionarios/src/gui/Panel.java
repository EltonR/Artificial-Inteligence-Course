package gui;

import entidade.State;
import entidade.Operator;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class Panel extends JPanel {

    private State state;
    private Operator operator;

    //Boat position on x-axis. left = 200; right = 550;
    private int posXbarco = 200;
    private int veloc = 5;

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.DARK_GRAY);
        int pos = 0;

        //Draws the boat
        g.setColor(Color.black);
        g.fillArc(posXbarco, 335, 150, 100, 180, 180);
        g.fillRect(posXbarco + 50, 280, 8, 140);
        int[] velax = {posXbarco + 60, posXbarco + 100, posXbarco + 60};
        int[] velay = {360, 360, 300};
        g.fillPolygon(velax, velay, 3);
        g.fillOval(posXbarco + 43, 275, 20, 20);
        if (operator != null) {
            g.setColor(Color.white);
            for (int i = 0; i < operator.getNumMissionaries(); i++) {
                g.fillOval(posXbarco + pos, 367, 18, 18);
                pos += 18;
            }
            g.setColor(new Color(190, 10, 10));
            for (int i = 0; i < operator.getNumCannibals(); i++) {
                g.fillOval(posXbarco + pos, 367, 18, 18);
                pos += 18;
            }
        }

        //Draws left side missionairies
        pos = 180; //left side
        g.setColor(Color.white);
        for (int i = 0; i < state.getMissionariesLeft(); i++) {
            g.fillRect(pos, 365, 5, 35);
            g.fillOval(pos - 7, 350, 18, 18);
            pos -= 18;
        }
        //Draws left side cannibals
        for (int i = 0; i < state.getCannibalsLeft(); i++) {
            g.setColor(new Color(10, 130, 10));
            g.fillRect(pos, 365, 5, 35);
            g.setColor(new Color(190, 10, 10));
            g.fillOval(pos - 7, 350, 18, 18);
            pos -= 18;
        }
        //Draws right side missionairies
        pos = 720; //lado direito
        g.setColor(Color.white);
        for (int i = 0; i < state.getMissionariesRight(); i++) {
            g.fillRect(pos, 365, 5, 35);
            g.fillOval(pos - 7, 350, 18, 18);
            pos += 18;
        }
        //Draws right side cannibals
        for (int i = 0; i < state.getCannibalsRight(); i++) {
            g.setColor(new Color(10, 130, 10));
            g.fillRect(pos, 365, 5, 35);
            g.setColor(new Color(190, 10, 10));
            g.fillOval(pos - 7, 350, 18, 18);
            pos += 18;
        }

        //Draws the moon
        g.setColor(Color.WHITE);
        g.fillOval(600, 50, 40, 40);

        //Draws the river
        g.setColor(new Color(10, 10, 150));
        g.fillRect(200, 420, 500, 300);

        //Draws left side green bar
        g.setColor(new Color(10, 150, 10));
        g.fillRect(0, 400, 200, 300);

        //Draws right side green bar
        g.setColor(new Color(10, 150, 10));
        g.fillRect(700, 400, 200, 300);

    }

    /**
     * Tries to get move the boat to right
     * @return true if movement succeed, false if not.
     */
    public boolean goRight() {
        if (this.posXbarco < 550) {
            this.posXbarco += veloc;
        } else {
            state.setGeneratorOperator(new Operator(0, 0));
        }
        repaint();
        return (this.posXbarco < 550);
    }

    /**
     * Tries to get move the boat to left
     * @return true if movement succeed, false if not.
     */
    public boolean goLeft() {
        if (this.posXbarco > 200) {
            this.posXbarco -= veloc;
        } else {
            state.setGeneratorOperator(new Operator(0, 0));
        }
        repaint();
        return (this.posXbarco > 200);
    }

    
    //getters and setters
    public State getState() {
        return state;
    }

    public void setState(State estado) {
        this.state = estado;
    }

    public int getVeloc() {
        return veloc;
    }

    public void setVeloc(int veloc) {
        this.veloc = veloc;
    }

    public Operator getOperador() {
        return operator;
    }

    public void setOperador(Operator operador) {
        this.operator = operador;
    }

}
