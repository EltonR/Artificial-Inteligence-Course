package controle;

import entidade.State;
import entidade.Operator;
import gui.Panel;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Menu extends javax.swing.JFrame {

    private int numMisssionaries;
    private int numCannibals;
    private int numOperators;
    private State targetState;
    private State initialState;
    private ArrayList<State> listOfValidStates;
    private ArrayList<Operator> listOfPossibleOperations;

    public Menu() {
        initComponents();
    }

    /**
     * Generates a list of all possible operations (boat movements) based on its
     * seats. This list is stored at "listOfPossibleOperations".
     *
     * @param numOp Number of operations (boat seats).
     */
    private void generatePossibleOperators(int numOp) {
        listOfPossibleOperations = new ArrayList<>();
        for (int i = 0; i < numMisssionaries + 1; i++) {
            for (int j = 0; j < numCannibals + 1; j++) {
                if (i + j <= numOp && (i + j) > 0) {
                    listOfPossibleOperations.add(new Operator(i, j));
                }
            }
        }
        System.out.println("Lista de operadores válidos (" + listOfPossibleOperations.size() + "):");
        for (int i = 0; i < listOfPossibleOperations.size(); i++) {
            System.out.println(listOfPossibleOperations.get(i));
        }
        System.out.println("");
    }

    /**
     * Generates all the possible states and filters the not valid. This list is
     * stored at "listOfValidStates".
     */
    private void generatePossibleValidStates() {
        listOfValidStates = new ArrayList<>();
        for (int i = 0; i < numCannibals + 1; i++) {
            for (int j = 0; j < numMisssionaries + 1; j++) {
                State e1 = new State();
                e1.setCannibalsRight(i);
                e1.setCannibalsLeft(numCannibals - i);
                e1.setMissionariesRight(j);
                e1.setMissionariesLeft(numMisssionaries - j);
                e1.setBoatSide("esq");
                if (e1.isValid()) {
                    listOfValidStates.add(e1);
                }
                State e2 = new State();
                e2.setCannibalsRight(i);
                e2.setCannibalsLeft(numCannibals - i);
                e2.setMissionariesRight(j);
                e2.setMissionariesLeft(numMisssionaries - j);
                e2.setBoatSide("dir");
                if (e2.isValid()) {
                    listOfValidStates.add(e2);
                }
            }
        }
        System.out.println("Estados válidos (" + listOfValidStates.size() + "):");
        for (int i = 0; i < listOfValidStates.size(); i++) {
            System.out.println(listOfValidStates.get(i));
        }
        System.out.println("");
    }

    /**
     * Starts the animation on Graphics2d.
     *
     * @param estadoFinal The final state (solution).
     */
    private void startAnimation(State estadoFinal) {
        ArrayList<Operator> lista = new ArrayList<>();
        while (estadoFinal.getFather() != null) {
            lista.add(estadoFinal.getGeneratorOperator());
            estadoFinal = estadoFinal.getFather();
        }

        JFrame frame = new JFrame();
        frame.setTitle("Erasch's Missionaries and Cannibals");
        frame.setSize(900, 700);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                Menu.this.setVisible(true);
            }
        });

        frame.setVisible(true);
        Panel painel = new Panel();
        Container contentPane = frame.getContentPane();
        contentPane.add(painel);
        painel.setState(initialState);
        painel.repaint();

        int i = lista.size() - 1;

        /**
         * On every mouse click there will be a Thread making the movement to
         * left or to right, according the operation.
         */
        frame.addMouseListener(new MouseAdapter() {
            boolean acionado = false; //Blocks many threads

            @Override
            public void mousePressed(MouseEvent e) {
                if (!acionado) {
                    acionado = true;
                    Runnable r = new Runnable() {
                        public void run() {
                            if (lista.size() > 0) {
                                painel.setOperador(lista.get(lista.size() - 1));
                                if (painel.getState().getBoatSide().equals("dir")) {
                                    painel.getState().setCannibalsRight(painel.getState().getCannibalsRight() - painel.getOperador().getNumCannibals());
                                    painel.getState().setMissionariesRight(painel.getState().getMissionariesRight() - painel.getOperador().getNumMissionaries());
                                } else {
                                    painel.getState().setCannibalsLeft(painel.getState().getCannibalsLeft() - painel.getOperador().getNumCannibals());
                                    painel.getState().setMissionariesLeft(painel.getState().getMissionariesLeft() - painel.getOperador().getNumMissionaries());
                                }
                                try {
                                    if (painel.getState().getBoatSide().equals("esq")) {
                                        while (painel.goRight()) {
                                            Thread.sleep(10); // Interval between frame refresh. The minor the interval, the quicker the boat will go.
                                        }
                                        painel.getState().setBoatSide("dir"); // on complete, set the new boat state.
                                    } else {
                                        while (painel.goLeft()) {
                                            Thread.sleep(10);
                                        }
                                        painel.getState().setBoatSide("esq");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Erro: " + e.getMessage());
                                }
                                if (painel.getState().getBoatSide().equals("dir")) {
                                    painel.getState().setCannibalsRight(painel.getState().getCannibalsRight() + painel.getOperador().getNumCannibals());
                                    painel.getState().setMissionariesRight(painel.getState().getMissionariesRight() + painel.getOperador().getNumMissionaries());
                                } else {
                                    painel.getState().setCannibalsLeft(painel.getState().getCannibalsLeft() + painel.getOperador().getNumCannibals());
                                    painel.getState().setMissionariesLeft(painel.getState().getMissionariesLeft() + painel.getOperador().getNumMissionaries());
                                }
                                painel.setOperador(null);
                                painel.repaint();
                                lista.remove(lista.size() - 1);
                            }
                            acionado = false;
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();
                }
            }
        });
    }

    /**
     * Expands all the valid states of a current state, based on the possible
     * operations.
     *
     * @param estadoAtual the actual state.
     * @param listaOperadores a list of all possible valid operations of all
     * states.
     * @return a list of valid states.
     */
    private ArrayList<State> expandState(State estadoAtual, ArrayList<Operator> listaOperadores) {
        ArrayList<State> lista = new ArrayList<>();
        System.out.println("Novos estados a partir de " + estadoAtual + ": ");
        for (int i = 0; i < listaOperadores.size(); i++) {
            State e = estadoAtual.expandNode(listaOperadores.get(i));
            if (e != null && e.isValid() && e.isNewNode(e)) {
                e.setGeneratorOperator(listaOperadores.get(i));
                lista.add(e);
                System.out.println(e + " " + listaOperadores.get(i));
            }
        }
        System.out.println("");
        return lista;
    }

    /**
     * Finds the solution based on the depth and starts the animation if it
     * finds it.
     *
     * @param estado the initial state.
     */
    private boolean findSolution(State estado) {
        if (estado.equals(targetState)) {
            System.out.println(">>>>>>>>>> SOLUTION >>>>>>>>");
            estado.prinSolution();
            targetState = null;
            startAnimation(estado);
            return true;
        }
        if (targetState == null) {
            return true;
        }
        ArrayList<State> listaEstados = expandState(estado, listOfPossibleOperations);
        boolean encontrou = false;
        if (listaEstados.size() > 0) {
            for (State e : listaEstados) {
                encontrou = findSolution(e);
            }
        }
        return encontrou;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Erasch's Missionaries and Cannibals");

        jLabel1.setText("Number of Missionaries");

        jLabel2.setText("Number of Cannibals");

        jTextField1.setText("3");

        jTextField2.setText("3");

        jButton1.setText("Start");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel3.setText("Number of Operations");

        jTextField3.setText("2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addComponent(jLabel3))
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(115, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        numMisssionaries = Integer.valueOf(jTextField1.getText());
        numCannibals = Integer.valueOf(jTextField2.getText());
        numOperators = Integer.valueOf(jTextField3.getText());
        targetState = new State();
        targetState.setCannibalsRight(numCannibals);
        targetState.setMissionariesRight(numMisssionaries);
        targetState.setBoatSide("dir");
        generatePossibleOperators(numOperators);
        generatePossibleValidStates();
        System.out.println("Estado Alvo: " + targetState);
        initialState = new State();
        initialState.setMissionariesLeft(numMisssionaries);
        initialState.setCannibalsLeft(numCannibals);
        initialState.setBoatSide("esq");
        this.setVisible(false);
        boolean encontrou = findSolution(initialState);
        if (!encontrou) {
            this.setVisible(true);
            JOptionPane.showMessageDialog(this, "Solução Não Encontrada!", "Erro", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables

}
