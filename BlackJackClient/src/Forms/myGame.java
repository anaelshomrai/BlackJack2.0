/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Forms;

import blackjack.Card;
import blackjack.Deck;
import blackjack.Hand;
import blackjack.Utils;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import org.netbeans.lib.awtextra.AbsoluteLayout;

/**
 *
 * @author Anael
 */
public class myGame extends javax.swing.JFrame {

    private myGame.GameState state = myGame.GameState.BETTING;
    private myGame.GameState nextState;
    private Hand dealerHand = new Hand();
    private Hand currentHand;
    private int currentHandIndex = 0;
    private List<Hand> playerHands = new ArrayList<>();
    List<JLabel> players = new ArrayList<>();
    JPanel cardsPanel;
    JLabel background = new JLabel("");
    List<JLabel> dealrHand = new ArrayList<>();
    JPanel statusPanel;
    JPanel actionPanel;
    JPanel betPanel;
    JButton betButton;
    JButton hitButton;
    JButton stayButton;
    JButton doubleDownButton;
    JButton splitButton;
    JTextField betAmountTextField;
    JLabel cashLabel;
    JLabel winsLabel;
    JLabel totalCardsLabel;

    private static Map<Utils.SUIT, Image[]> cardImages = null;

    private static Image cardBackImage = new ImageIcon("./src/img/cards/back.png").getImage();

    private static Image[] spadesImages = {
        new ImageIcon("./src/img/cards/ace_of_spades.png").getImage(),
        new ImageIcon("./src/img/cards/2_of_spades.png").getImage(),
        new ImageIcon("./src/img/cards/3_of_spades.png").getImage(),
        new ImageIcon("./src/img/cards/4_of_spades.png").getImage(),
        new ImageIcon("./src/img/cards/5_of_spades.png").getImage(),
        new ImageIcon("./src/img/cards/6_of_spades.png").getImage(),
        new ImageIcon("./src/img/cards/7_of_spades.png").getImage(),
        new ImageIcon("./src/img/cards/8_of_spades.png").getImage(),
        new ImageIcon("./src/img/cards/9_of_spades.png").getImage(),
        new ImageIcon("./src/img/cards/10_of_spades.png").getImage(),
        new ImageIcon("./src/img/cards/jack_of_spades.png").getImage(),
        new ImageIcon("./src/img/cards/queen_of_spades.png").getImage(),
        new ImageIcon("./src/img/cards/king_of_spades.png").getImage()
    };

    private static Image[] heartsImages = {
        new ImageIcon("./src/img/cards/ace_of_hearts.png").getImage(),
        new ImageIcon("./src/img/cards/2_of_hearts.png").getImage(),
        new ImageIcon("./src/img/cards/3_of_hearts.png").getImage(),
        new ImageIcon("./src/img/cards/4_of_hearts.png").getImage(),
        new ImageIcon("./src/img/cards/5_of_hearts.png").getImage(),
        new ImageIcon("./src/img/cards/6_of_hearts.png").getImage(),
        new ImageIcon("./src/img/cards/7_of_hearts.png").getImage(),
        new ImageIcon("./src/img/cards/8_of_hearts.png").getImage(),
        new ImageIcon("./src/img/cards/9_of_hearts.png").getImage(),
        new ImageIcon("./src/img/cards/10_of_hearts.png").getImage(),
        new ImageIcon("./src/img/cards/jack_of_hearts.png").getImage(),
        new ImageIcon("./src/img/cards/queen_of_hearts.png").getImage(),
        new ImageIcon("./src/img/cards/king_of_hearts.png").getImage()
    };

    private static Image[] diamondsImages = {
        new ImageIcon("./src/img/cards/ace_of_diamonds.png").getImage(),
        new ImageIcon("./src/img/cards/2_of_diamonds.png").getImage(),
        new ImageIcon("./src/img/cards/3_of_diamonds.png").getImage(),
        new ImageIcon("./src/img/cards/4_of_diamonds.png").getImage(),
        new ImageIcon("./src/img/cards/5_of_diamonds.png").getImage(),
        new ImageIcon("./src/img/cards/6_of_diamonds.png").getImage(),
        new ImageIcon("./src/img/cards/7_of_diamonds.png").getImage(),
        new ImageIcon("./src/img/cards/8_of_diamonds.png").getImage(),
        new ImageIcon("./src/img/cards/9_of_diamonds.png").getImage(),
        new ImageIcon("./src/img/cards/10_of_diamonds.png").getImage(),
        new ImageIcon("./src/img/cards/jack_of_diamonds.png").getImage(),
        new ImageIcon("./src/img/cards/queen_of_diamonds.png").getImage(),
        new ImageIcon("./src/img/cards/king_of_diamonds.png").getImage()
    };

    private static Image[] clubsImages = {
        new ImageIcon("./src/img/cards/ace_of_clubs.png").getImage(),
        new ImageIcon("./src/img/cards/2_of_clubs.png").getImage(),
        new ImageIcon("./src/img/cards/3_of_clubs.png").getImage(),
        new ImageIcon("./src/img/cards/4_of_clubs.png").getImage(),
        new ImageIcon("./src/img/cards/5_of_clubs.png").getImage(),
        new ImageIcon("./src/img/cards/6_of_clubs.png").getImage(),
        new ImageIcon("./src/img/cards/7_of_clubs.png").getImage(),
        new ImageIcon("./src/img/cards/8_of_clubs.png").getImage(),
        new ImageIcon("./src/img/cards/9_of_clubs.png").getImage(),
        new ImageIcon("./src/img/cards/10_of_clubs.png").getImage(),
        new ImageIcon("./src/img/cards/jack_of_clubs.png").getImage(),
        new ImageIcon("./src/img/cards/queen_of_clubs.png").getImage(),
        new ImageIcon("./src/img/cards/king_of_clubs.png").getImage()
    };

    private Deck deck;
    private int playerCash = 10000;
    private static int MIN_BET = 5;
    private static int MAX_BET = 500;
    private int playerWins = 0;
    private int dealerWins = 0;
    private int playerId = 0;

    private enum GameState {
        BETTING,
        DEALING,
        HITTING,
        DEALER_AI,
        RESOLVE
    }

    public myGame() {
        initComponents();
        myInit(3);

    }

    public void myInit(int numOfPlayers) {
        Map<Utils.SUIT, Image[]> map = new HashMap<Utils.SUIT, Image[]>();
        map.put(Utils.SUIT.SPADES, spadesImages);
        map.put(Utils.SUIT.HEARTS, heartsImages);
        map.put(Utils.SUIT.DIAMONDS, diamondsImages);
        map.put(Utils.SUIT.CLUBS, clubsImages);
        cardImages = Collections.unmodifiableMap(map);
        int x = 0;
        cardsPanel = new JPanel();
        cardsPanel.setLayout(null);
        int cnt = 0;

        for (int i = 1; i <= numOfPlayers; i++) {
            for (int j = 1; j <= 2; j++) {
                players.add(new JLabel());
            }
            cardsPanel.add(players.get(cnt));

            players.get(cnt).setBounds(x, 10, 120, 170);
            players.get(cnt).setName("");

            cardsPanel.add(players.get(cnt + 1));

            players.get(cnt + 1).setBounds(x + 20, 0, 170, 190);
            players.get(cnt + 1).setName("");

            cnt += 2;
            x += 260;
        }
        this.add(cardsPanel);
        cardsPanel.setBounds(10, 360, 780, 200);
        cardsPanel.setOpaque(false);

        dealrHand.add(new JLabel());
        dealrHand.add(new JLabel());

        add(dealrHand.get(1));
        dealrHand.get(1).setBounds(320, 40, 120, 170); //back
        add(dealrHand.get(0));
        dealrHand.get(0).setBounds(300, 40, 120, 170); //expose

//        dealrHand.get(0).setBounds(300, 40, 0, 0); //expose
//        dealrHand.get(1).setBounds(320, 40, 0, 0); //back
        deck = new Deck();
        dealCards();

        //panels 
        betPanel = new JPanel();
        betAmountTextField = new JTextField();
        betButton = new JButton();

        betPanel.setBackground(new java.awt.Color(0, 0, 0));
        betPanel.setOpaque(false);
        betPanel.setLayout(new java.awt.GridLayout());

        betAmountTextField.setText("Amount");
        betPanel.add(betAmountTextField);

        betButton.setText("Bet");
        betPanel.add(betButton);

        getContentPane().add(betPanel);
        betPanel.setBounds(0, 560, 120, 40);

        actionPanel = new JPanel();
        hitButton = new JButton();
        stayButton = new JButton();
        doubleDownButton = new JButton();
        splitButton = new JButton();

        actionPanel.setLayout(new java.awt.GridLayout());
        actionPanel.setOpaque(false);

        hitButton.setText("Hit");
        actionPanel.add(hitButton);

        stayButton.setText("Stay");
        actionPanel.add(stayButton);

        doubleDownButton.setText("Double Down");
        actionPanel.add(doubleDownButton);

        splitButton.setText("Split");
        actionPanel.add(splitButton);

        getContentPane().add(actionPanel);
        actionPanel.setBounds(130, 560, 450, 40);

        statusPanel = new JPanel();
        cashLabel = new JLabel();
        winsLabel = new JLabel();
        totalCardsLabel = new JLabel();

        statusPanel.setLayout(new java.awt.GridLayout());
        statusPanel.setOpaque(false);

        cashLabel.setText("Cash :");
        statusPanel.add(cashLabel);

        winsLabel.setText("Wins:");
        statusPanel.add(winsLabel);

        totalCardsLabel.setText("Total: ");
        statusPanel.add(totalCardsLabel);

        getContentPane().add(statusPanel);
        statusPanel.setBounds(590, 560, 210, 40);

        background.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/felt.png")));
        getContentPane().add(background);
        background.setBounds(0, 0, 800, 600);

        revalidate();
        repaint();
    }

    public void dealCards() {
        for (JLabel h : players) {
            dealCard(h);
        }
        dealCard(dealrHand.get(0));
        dealCard(dealrHand.get(1), false);

    }

    private void dealCard(JLabel hand, boolean faceUp) {
        Card newCard = deck.drawCard(faceUp);
        if (newCard == null) // Reshuffle if we are out of cards.
        {
            deck.reshuffle();
            dealCard(hand, faceUp);
        } else if (faceUp) {
            hand.setIcon(new javax.swing.ImageIcon(getCardImage(newCard)));
        } else {
            hand.setIcon(new javax.swing.ImageIcon(cardBackImage));
        }
    }

    private static Image getCardImage(Card card) {
        return cardImages.get(card.getSuit())[card.getRank().ordinal()];
    }

    private void dealCard(JLabel hand) {
        dealCard(hand, true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        infoPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(null);
        getContentPane().add(infoPanel);
        infoPanel.setBounds(0, 0, 800, 20);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/mute.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.setOpaque(false);
        getContentPane().add(jButton1);
        jButton1.setBounds(753, 30, 40, 30);

        jToggleButton1.setText("jToggleButton1");
        getContentPane().add(jToggleButton1);
        jToggleButton1.setBounds(300, 600, 105, 23);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(460, 380, 66, 22);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/background.png"))); // NOI18N
        getContentPane().add(jLabel3);
        jLabel3.setBounds(10, 0, 800, 590);

        setBounds(0, 0, 816, 677);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(myGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(myGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(myGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(myGame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new myGame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel infoPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
}
