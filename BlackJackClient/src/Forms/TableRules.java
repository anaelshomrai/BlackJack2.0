
package Forms;

/**
 * This frame Shows an image of the game rules, according to the language.
 * @author ANI
 */
public class TableRules extends javax.swing.JFrame {

    /**
     * This constructor initializes a tableRules frame in English.
     */
    public TableRules() {
        initComponents();
    }
    
    /**
     * This constructor initializes a tableRules frame in Hebrew..
     * 
     * @param language the language selected.
     */
    public TableRules(String language) {
        initComponents();
         if (language.equals("iw"))
        {
          labTableRules.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/blackjack-card-valuesHE.png")));  
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        labTableRules = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(null);

        labTableRules.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/blackjack-card-values.jpg"))); // NOI18N
        getContentPane().add(labTableRules);
        labTableRules.setBounds(0, 0, 420, 610);

        setSize(new java.awt.Dimension(419, 600));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labTableRules;
    // End of variables declaration//GEN-END:variables
}
