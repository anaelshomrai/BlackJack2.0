package Forms;

import Resources.GameUtil;
import javax.swing.ImageIcon;

/**
 * This frame create a short pop-up frame that indicates a player won the round.
 * This frame destroy itself.
 * 
 * @author ANI
 */
public class WinningPopup extends javax.swing.JFrame implements Runnable {

    /**
     * This constructor initializes the frame.
     */
    public WinningPopup(){
        this.setUndecorated(true);
        GameUtil.winPot();
        initComponents();
        this.setResizable(false);
        String file = "./src/img/card-symbols_bsmall.png";
        ImageIcon img = new ImageIcon(file);
        this.setIconImage(img.getImage());
    }
    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("winningPopupFrame"); // NOI18N
        setPreferredSize(new java.awt.Dimension(490, 300));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/ezgif.com-gif-maker.gif"))); // NOI18N
        jLabel1.setPreferredSize(new java.awt.Dimension(496, 328));
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 490, 300);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        this.dispose();
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables

    /**
     * This runs when an instance created, the frame shows for 7000 milliseconds
     * then destroy the frame.
     */
    @Override
    public void run() {
        WinningPopup winPopup = this;
        winPopup.setVisible(true);
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
            @Override
            public void run() {
                winPopup.dispose();

            }
        },
                7000
        );

    }
}
