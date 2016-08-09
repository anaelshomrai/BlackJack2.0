package Forms;

import Resources.GameUtil;
import Resources.LocalizationUtil;
import Users.User;
import blackjack.BoardPanel;
import blackjack.Card;
import blackjack.Deck;
import blackjack.Hand;
import blackjack.HandPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 *
 * @author ANI
 */
public class GameTest implements ActionListener, Serializable {

    //
    // GUI Components
    //
    JFrame frame = new JFrame("Blackjack");
    private JButton hitButton;
    private JButton stayButton;
    private JButton doubleDownButton;
    private JButton splitButton;
    private JLabel systemMessage;
    private JTextField betAmountTextField;
    private JButton betButton;
    JPanel playersCardsPanel;
    JPanel playersCardsPanel2;
    JPanel playersCardsPanel3;
    JPanel blankPanel;
    JPanel dealersCardsPanel;
    JLabel playerCashLabel;
    JLabel playerWinsLabel;
    JLabel dealerWinsLabel;
    JLabel status;

    //
    // Dealer Variables
    //
    private GameState state = GameState.BETTING;
    private GameState nextState;
    private Hand dealerHand = new Hand();
    private Hand currentHand;
    private Hand currentHand2;
    private Hand currentHand3;
    private int currentHandIndex = 0;
    private int currentHandIndex2 = 0;
    private int currentHandIndex3 = 0;
    private List<Hand> playerHands = new ArrayList<Hand>();
    private List<Hand> playerHands2 = new ArrayList<Hand>();
    private List<Hand> playerHands3 = new ArrayList<Hand>();

    private Deck deck;
    private int playerCash = 10000;
    private static int MIN_BET = 5;
    private static int MAX_BET = 500;
    private int playerWins = 0;
    private int dealerWins = 0;
    private int playerId = 0;
    //private UserHome previous = null;
    String language = "";
    //private WelcomeScreen guestPrev = null;

    /**
     * Each possible game state.
     *
     * Betting: Player places bet. Dealing: New cards are dealt. Hitting: Player
     * is prompted to either hit, stay, double down or split. Dealer AI: Flip
     * hidden card and draw until the total value of the dealers hand is equal
     * to 17 or more. Resolve: Flips the dealers cards, dealer hits til 17+,
     * points are tallied and payouts given.
     *
     */
    private enum GameState {
        BETTING,
        DEALING,
        HITTING,
        DEALER_AI,
        RESOLVE
    }

    public GameTest() {
        deck = new Deck();
        playerHands.add(new Hand()); // Default Hand.  
        playerHands2.add(new Hand());
        playerHands3.add(new Hand());

    }

    public GameTest(int numPlayers) {
        deck = new Deck();
        playerHands.add(new Hand()); // Default Hand.  
        playerHands2.add(new Hand());
        playerHands3.add(new Hand());

    }

//	public Game(UserHome prev) {
//                this();
//                previous = prev;          
//	}
//        
//        public Game(WelcomeScreen prev) {
//                this();
//                guestPrev = prev;          
//	}
//        
//        public Game(WelcomeScreen prev,String lang) {
//                this(prev);
//                this.language = lang;
//                        
//	}
//        
//        public Game(User u,UserHome w)  {
//                    this(w);
//                    this.playerId = u.getId();
//                    this.playerCash = u.getBalance();
//                    this.playerWins = u.getWins();
//	}
//        
//        public Game(User u,UserHome w, String lang)  {
//                this(w);
//                this.playerId = u.getId();
//                this.playerCash = u.getBalance();
//                this.playerWins = u.getWins();
//                this.language = lang;
//	}
    /**
     * Initialize GUI components.
     */
    public void initializeGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setResizable(false);

        String file = "./src/img/card-symbols_bsmall.png";
        ImageIcon img = new ImageIcon(file);
        frame.setIconImage(img.getImage());
        frame.setTitle("BlackJack ANI");

        //
        // Initialize Board Panel
        //
        BoardPanel boardPanel = new BoardPanel("./src/img/felt.png");
        boardPanel.setLayout(new GridBagLayout());
        boardPanel.setPreferredSize(new Dimension(800, 600));
        frame.add(boardPanel);

        //
        //
        // Initialize Bet Panel
        JPanel betPanel = new JPanel();
        betPanel.setLayout(new BoxLayout(betPanel, BoxLayout.X_AXIS));
        betPanel.setOpaque(false);

        betAmountTextField = new JTextField(5);
        betButton = createButton("Bet", "bet");

        betPanel.add(betAmountTextField);
        betPanel.add(betButton);

        boardPanel.add(betPanel, createConstraints(0, 6, 1, 2, 0, 0, GridBagConstraints.HORIZONTAL, true));

        //
        // Initialize Action Panel
        //
        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);

        hitButton = createButton("Hit", "hit");
        actionPanel.add(hitButton);

        stayButton = createButton("Stay", "stay");
        actionPanel.add(stayButton);

        doubleDownButton = createButton("Double Down", "double_down");
        actionPanel.add(doubleDownButton);

        splitButton = createButton("Split", "split");
        actionPanel.add(splitButton);

        boardPanel.add(actionPanel, createConstraints(1, 6, 4, 2, 0, 0, GridBagConstraints.HORIZONTAL, true));

        //
        // Initialize Info Panel
        //
        JPanel infoPanel = new JPanel();
        playerCashLabel = new JLabel("Player Cash: $" + Integer.toString(playerCash));
        infoPanel.add(playerCashLabel);

        playerWinsLabel = new JLabel("Wins: 0");
        infoPanel.add(playerWinsLabel);

        dealerWinsLabel = new JLabel("Loses: 0");
        infoPanel.add(dealerWinsLabel);

        status = new JLabel("Sum of cards in hand: 0");
        infoPanel.add(status);
        boardPanel.add(infoPanel, createConstraints(5, 6, 1, 2, 0, 0, GridBagConstraints.BOTH, true));

        //
        // Initialize System Message Panel
        //
        JPanel systemMessagePanel = new JPanel();
        if (this.language.equals("iw")) {
            systemMessage = new JLabel("בחר סכום ולחץ על המר כדי להתחיל");
        } else {
            systemMessage = new JLabel("Enter an amount and press \"Bet\" to begin.");
        }

        systemMessagePanel.add(systemMessage);

        boardPanel.add(systemMessagePanel, createConstraints(0, 0, 6, 1, 0, 0, GridBagConstraints.HORIZONTAL, false));

        //
        // Initialize Dealer Cards Panel
        //
        dealersCardsPanel = new HandPanel(dealerHand);
        dealersCardsPanel.setOpaque(true);

        boardPanel.add(dealersCardsPanel, createConstraints(0, 2, 6, 2, 1, 1, GridBagConstraints.BOTH, false));

        //
        // Initialize Player Cards Panel
        //
        playersCardsPanel = new HandPanel(playerHands);
        playersCardsPanel.setOpaque(true);
        playersCardsPanel2 = new HandPanel(playerHands2);
        playersCardsPanel2.setOpaque(true);
        playersCardsPanel3 = new HandPanel(playerHands3);
        playersCardsPanel3.setOpaque(true);

        JPanel playPanel = new JPanel();
        playPanel.setLayout(new GridLayout(1, 3, 10, 10));
        playPanel.setOpaque(false);

        playPanel.add(playersCardsPanel);
        playPanel.add(playersCardsPanel2);
        playPanel.add(playersCardsPanel3);

        boardPanel.add(playPanel, createConstraints(0, 4, 6, 2, 1, 1, GridBagConstraints.BOTH, false));

        //
        // Update Timer
        //
        Timer t = new Timer(40, this);
        t.start();

        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        updateState(GameState.BETTING);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                        if (playerId != 0)
//                        {
//                             DB db = DB.getInstance();
//                             db.updateUser(playerId, playerWins, playerCash); 
//                             previous.setVisible(true);
//                        }
//                        else if (guestPrev != null)
//                        {
//                            guestPrev.setVisible(true);
//                        }
                frame.dispose();
            }
        });

        if (this.language.equals("iw")) {
            changeToHebrew();
        }
    }

    public void changeToHebrew() {
        LocalizationUtil.localizedResourceBundle = LocalizationUtil.getBundleGameIW();
        updateCaptions();
        LocalizationUtil.changeOptionPane_iw();
    }

    public void updateCaptions() {
        betButton.setText(LocalizationUtil.localizedResourceBundle.getString("betButton"));
        hitButton.setText(LocalizationUtil.localizedResourceBundle.getString("hitButton"));
        stayButton.setText(LocalizationUtil.localizedResourceBundle.getString("stayButton"));
        doubleDownButton.setText(LocalizationUtil.localizedResourceBundle.getString("doubleDownButton"));
        splitButton.setText(LocalizationUtil.localizedResourceBundle.getString("splitButton"));
    }

    /**
     * Updates the state of the game and refreshes the GUI.
     */
    private void update() {
        state = nextState; // Update our state, first.
        currentHand = playerHands.get(currentHandIndex);
        currentHand2 = playerHands2.get(currentHandIndex2);
        currentHand3 = playerHands3.get(currentHandIndex3);

        int total = 0;
        for (Hand c : playerHands) {
            total = c.getValue();
        }
        if (this.language.equals("iw")) {
            status.setText("סהכ: " + total);
        } else {
            status.setText("Sum of cards: " + total);
        }

        switch (state) {
            case BETTING:
                betButton.setEnabled(true);
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                doubleDownButton.setEnabled(false);
                splitButton.setEnabled(false);
                break;
            case DEALER_AI:
                betButton.setEnabled(false);
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                doubleDownButton.setEnabled(false);
                splitButton.setEnabled(false);

                // Flip hidden card.
                dealerHand.getCard(1).reveal();

                // Draw until 17+.
                while (dealerHand.getValue() < 17) {
                    dealCard(dealerHand);
                }

                updateState(GameState.RESOLVE);
                break;
            case DEALING:
                betButton.setEnabled(false);
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                doubleDownButton.setEnabled(false);
                splitButton.setEnabled(false);

                GameUtil.playShuffle();

                dealCard(currentHand);
                dealCard(currentHand2);
                dealCard(currentHand3);
                dealCard(dealerHand);
                dealCard(currentHand);
                dealCard(currentHand2);
                dealCard(currentHand3);
                dealCard(dealerHand, false);

                if (currentHand.isBlackjack() == true) {
                    updateState(GameState.RESOLVE);
                } else {
                    updateState(GameState.HITTING);
                }

                break;
            case HITTING:
                if (this.language.equals("iw")) {
                    systemMessage.setText("בחר פעולה. [יד " + (currentHandIndex + 1) + "]");
                } else {
                    systemMessage.setText("Select an action. [Hand " + (currentHandIndex + 1) + "]");
                }

                betButton.setEnabled(false);
                hitButton.setEnabled(true);
                stayButton.setEnabled(true);

                if ((playerCash < currentHand.getBet()) || (currentHand.getSize() > 2)) {
                    doubleDownButton.setEnabled(false);
                } else {
                    doubleDownButton.setEnabled(true);
                }

                if (currentHand.isSplit()) {
                    splitButton.setEnabled(true);
                } else {
                    splitButton.setEnabled(false);
                }

                break;
            case RESOLVE:
                betButton.setEnabled(true);
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                doubleDownButton.setEnabled(false);
                splitButton.setEnabled(false);

                // If we came straight to resolve without hitting dealer ai (blackjack or bust)
                // reveal the hidden card.
                if (dealerHand.getCard(1).isHidden() == true) {
                    dealerHand.getCard(1).reveal();
                }

                dispenseWinnings();

                // Start Over
                updateState(GameState.BETTING);
                break;
            default:
                break;
        }

        if (this.language.equals("iw")) {

            playerCashLabel.setText("קופה: $" + playerCash);
            playerWinsLabel.setText("ניצחונות: " + playerWins);
            dealerWinsLabel.setText("הפסדים: " + dealerWins);
        } else {
            playerCashLabel.setText("Cash: $" + playerCash);
            playerWinsLabel.setText("Wins: " + playerWins);
            dealerWinsLabel.setText("Loses: " + dealerWins);
        }
        frame.repaint();
    }

    /**
     * Creates a new button with the specified name and actionCommand, also sets
     * enabled status and action listener to defaults.
     */
    private JButton createButton(String name, String actionCommand) {
        JButton button = new JButton(name);

        button.setActionCommand(actionCommand);
        button.setEnabled(false);
        button.addActionListener(this);

        return button;
    }

    /**
     * Constraints factory.
     */
    private GridBagConstraints createConstraints(int x, int y, int width, int height, float weightx, float weighty, int fill, boolean insets) {
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.gridx = x;
        constraints.gridy = y;
        constraints.gridwidth = width;
        constraints.gridheight = height;
        constraints.weightx = weightx;
        constraints.weighty = weighty;
        constraints.fill = fill;
        if (insets) {
            constraints.insets = new Insets(10, 10, 10, 10);
        }

        return constraints;
    }

    /**
     * Sets the next game state.
     */
    private void updateState(GameState nextState) {
        this.nextState = nextState;
    }

    /**
     * Determines the winner of each hand and dispenses the winnings.
     */
    private void dispenseWinnings() {
        //
        // For each hand the player has, determine
        // a winner and give the appropriate payout.
        //
        for (int x = 0; x < playerHands.size(); x++) {
            Hand hand = playerHands.get(x);

            switch (hand.getWinner(dealerHand)) {
                case 0: // Dealer Win
                    if (this.language.equals("iw")) {
                        systemMessage.setText("הדילר ניצח את היד הזו...");
                    } else {
                        systemMessage.setText("Dealer wins this hand...");
                    }
                    dealerWins++;
                    break;
                case 1: // Player Win
                    if (this.language.equals("iw")) {
                        systemMessage.setText("ניצחת!!!");
                    } else {
                        systemMessage.setText("You win this hand!");
                    }
                    Thread t = new Thread(new WinningPopup());
                    t.start();

                    playerCash += hand.getBet() * 2; // Give bet back and winnings.

                    // If the player had a blackjack, pay back extra 50% (Blackjack Pays 3:2)
                    if (currentHand.isBlackjack()) {
                        playerCash += hand.getBet() * 0.5;
                    }

                    playerWins++;
                    break;
                case -1: // Tie
                    if (this.language.equals("iw")) {
                        systemMessage.setText("תיקו!");
                    } else {
                        systemMessage.setText("Push!");
                    }
                    playerCash += hand.getBet(); // Give bet back.
                    break;
            }
        }

    }

    /**
     * Draw one card from the deck, if it's null reshuffle the deck, otherwise
     * insert it into target hand.
     */
    private void dealCard(Hand hand, boolean faceUp) {
        Card newCard = deck.drawCard(faceUp);
        if (newCard == null) // Reshuffle if we are out of cards.
        {
            if (this.language.equals("iw")) {
                systemMessage.setText("מערבב קלפים...");
            } else {
                systemMessage.setText("Reshuffling...");
            }
            deck.reshuffle();
            dealCard(hand, faceUp);
        } else {
            hand.AddCard(newCard);
        }
    }

    private void dealCard(Hand hand) {
        dealCard(hand, true);
    }

    /**
     * Handles GUI button events.
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        if ("bet".equals(arg0.getActionCommand())) {
            //
            // Make sure the player has enough cash, is within
            // the bet domain of this table, set-up hands, then 
            // go to DEALING state.
            //
            try {
                int betAmount = Integer.parseInt(betAmountTextField.getText());

                if (betAmount > playerCash) {
                    systemMessage.setText("You don't have enough cash...");
                } else if (betAmount < MIN_BET || betAmount > MAX_BET) {
                    if (this.language.equals("iw")) {
                        systemMessage.setText("ההימור חייב להיות בין " + MIN_BET + " ל " + MAX_BET + ".");
                    } else {
                        systemMessage.setText("Bet must be between " + MIN_BET + " and " + MAX_BET + ".");
                    }
                } else {
                    // This will ensure hands are empty before dealing begins
                    // and also allow us to keep last round's cards still displayed
                    // until a new bet has been given. This also needs to be done here
                    // to allow the bet amount to be properly set to the new hand.
                    playerHands.clear();
                    playerHands.add(new Hand());

                    currentHandIndex = 0;
                    currentHand = playerHands.get(currentHandIndex);

                    dealerHand.Clear();

                    playerCash -= betAmount;
                    currentHand.setBet(betAmount);
                    updateState(GameState.DEALING);
                }

            } catch (NumberFormatException e) {
                if (this.language.equals("iw")) {
                    systemMessage.setText("ההימור חייב להיות מספר...");
                } else {
                    systemMessage.setText("Bet must be an integer value...");
                }
                return;
            }
        } else if ("hit".equals(arg0.getActionCommand())) {
            //
            // Deal one card to current hand, check for bust
            // or blackjack, if not, allow the player to keep hitting
            // otherwise, go to DEALER_AI state or next hand.
            //
            GameUtil.playShuffle();
            dealCard(currentHand);
            if (!currentHand.isBust() && !currentHand.isBlackjack()) {
                updateState(GameState.HITTING);
            } else if (currentHandIndex == (playerHands.size() - 1)) {
                updateState(GameState.DEALER_AI);
            } else {
                currentHandIndex++;
            }

        } else if ("stay".equals(arg0.getActionCommand())) {
            //
            // Go to DEALER_AI state or next hand.
            //
            if (currentHandIndex == (playerHands.size() - 1)) {
                updateState(GameState.DEALER_AI);
            } else {
                currentHandIndex++;
            }
        } else if ("double_down".equals(arg0.getActionCommand())) {
            //
            // Deduct the hand's bet amount from player's available cash,
            // deal one card to the current hand and go to the DEALER_AI
            // state or next hand.
            //
            playerCash -= currentHand.getBet();
            currentHand.setBet(currentHand.getBet() * 2);
            dealCard(currentHand);

            if (currentHandIndex == (playerHands.size() - 1)) {
                updateState(GameState.DEALER_AI);
            } else {
                currentHandIndex++;
            }

        } else if ("split".equals(arg0.getActionCommand())) {
            //
            // Split hand, deduct the new hand's bet
            // amount from the players cash and push
            // into the playerHands list.
            //
            Hand newHand = currentHand.split();
            playerCash -= currentHand.getBet();
            playerHands.add(newHand);
        }

        update();
    }
}
