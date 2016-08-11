package Forms;

import Resources.GameUtil;
import Resources.LocalizationUtil;
import Users.User;
import blackjack.BoardPanel;
import blackjack.Card;
import blackjack.Deck;
import blackjack.Hand;
import blackjack.HandPanel;
import blackjack.Utils;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
 * This class is the frame shown when a user plays as a guest or verses the
 * dealer. this class is responsible for managing the game throughout the stages
 * of the game. this game consist the dealer and the player only. possible game
 * state: Betting : Player places bet. Dealing: New cards are dealt. Hitting:
 * Player is prompted to either hit, stay, double down or split. Dealer: Flip
 * hidden card and draw until the total value of the dealers hand is equal to 17
 * or more. Resolve: Flips the dealers cards, dealer hits til 17+, the winner is
 * revealed.
 *
 * @author ANI
 */
public class Game implements ActionListener {

    /**
     * constant action command for bet button.
     */
    private static final String BET = "BET";
    /**
     * constant action command for hit button.
     */
    private static final String HIT = "HIT";
    /**
     * constant action command for stay button.
     */
    private static final String STAY = "STAY";
    /**
     * constant action command for double down button.
     */
    private static final String DOUBLE_DOWN = "DOUBLE_DOWN";
    /**
     * constant action command for split button.
     */
    private static final String SPLIT = "SPLIT";
    /**
     * constant action command for sound button.
     */
    private static final String SOUND = "SOUND";

    //
    // GUI Components
    //
    /**
     * the frame of the game.
     */
    private JFrame frame = new JFrame("Blackjack");
    /**
     * the button for hit another card.
     */
    private JButton hitButton;
    /**
     * the button for stay (don't take another card and ends the turn).
     */
    private JButton stayButton;
    /**
     * the button for double down, hit one more card and double the bet.
     */
    private JButton doubleDownButton;
    /**
     * the button for split, will enable if the hand can be split (two first
     * card has the same value).
     */
    private JButton splitButton;
    /**
     * the label in the info panel, will show the direction for that the player
     * needs to do at every stage.
     */
    private JLabel systemMessage;
    /**
     * the textField for taking the input of a player bet.
     */
    private JTextField betAmountTextField;
    /**
     * the button for bet, at each start of a game.
     */
    private JButton betButton;
    /**
     * the panel for the player cards.
     */
    private JPanel playersCardsPanel;
    /**
     * the panel for the dealer cards.
     */
    private JPanel dealersCardsPanel;
    /**
     * the label for the avaliable cash of a player.
     */
    private JLabel playerCashLabel;
    /**
     * the label for the amount of players winning.
     */
    private JLabel playerWinsLabel;
    /**
     * the button for enable/disable the sound.
     */
    private JButton sound;

    /**
     * represent if sounds are disabled/enabled. default is play sound.
     */
    private boolean playSounds = true;

    //
    // Variables
    //
    /**
     * the state of the game.
     */
    private Utils.GameState state = Utils.GameState.BETTING;
    /**
     * the next state of the game.
     */
    private Utils.GameState nextState;
    /**
     * dealer hand, containing the cards.
     */
    private Hand dealerHand = new Hand();
    /**
     * player current playing hand, containing the cards.
     */
    private Hand currentHand;
    /**
     * current index of the playing hand (in case we have a split hand).
     */
    private int currentHandIndex = 0;
    /**
     * current index of the playing hand (in case we have a split hand).
     */
    private List<Hand> playerHands = new ArrayList<>();
    /**
     * the deck of cards.
     */
    private Deck deck;
    /**
     * the player cash if he plays as a guest.
     */
    private int playerCash = 10000;
    /**
     * minimum bet for starting a game.
     */
    private static int MIN_BET = 5;
    /**
     * maximum bet for starting a game.
     */
    private static int MAX_BET = 500;
    /**
     * number of players winning.
     */
    private int playerWins = 0;
    /**
     * the id of the player.
     */
    private int playerId = 0;
    /**
     * the previous form if the user logged in and play verses the dealer.
     */
    private UserHome previous = null;
    /**
     * the language chosen.
     */
    private String language = "";
    /**
     * the previous form if the user plays as a guest.
     */
    private WelcomeScreen guestPrev = null;
    /**
     * the user logged in.
     */
    private User user;

    /**
     * Default constructor Initialize a Game form with a new deck.
     *
     */
    public Game() {
        deck = new Deck();
        playerHands.add(new Hand()); // Default Hand.              
    }

    /**
     * Constructor Initialize a Game form with the userHome as previous form.
     *
     * @param prev the previous form
     */
    public Game(UserHome prev) {
        this();
        previous = prev;
    }

    /**
     * Constructor Initialize a Game form with the welcomeScreen as previous
     * form.
     *
     * @param prev the previous form
     */
    public Game(WelcomeScreen prev) {
        this();
        guestPrev = prev;
    }

    /**
     * Constructor Initialize a Game form with the welcomeScreen as previous
     * form.
     *
     * @param prev the previous form
     * @param lang language of the game form
     */
    public Game(WelcomeScreen prev, String lang) {
        this(prev);
        this.language = lang;

    }

    /**
     * Constructor Initialize a Game form with the UserHome as previous form,
     * and sets the user.
     *
     * @param u the user
     * @param w the previous userHome
     */
    public Game(User u, UserHome w) {
        this(w);
        user = u;
        this.playerId = u.getId();
        this.playerCash = u.getBalance();
        this.playerWins = u.getWins();
    }

    /**
     * Constructor Initialize a Game form with the UserHome as previous form,
     * and sets the user.
     *
     * @param u the user
     * @param w the previous userHome
     * @param lang language of the game form
     */
    public Game(User u, UserHome w, String lang) {
        this(w);
        user = u;
        this.playerId = u.getId();
        this.playerCash = u.getBalance();
        this.playerWins = u.getWins();
        this.language = lang;
    }

    /**
     * This method Initialize GUI components.
     */
    public void initializeGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setResizable(false);

        GameUtil.setIcon(frame);
        frame.setTitle("BlackJack ANI");

        // initiliaze sound icon
        sound = new JButton();
        sound.setActionCommand(SOUND);
        sound.setIcon(new ImageIcon("./src/img/audio.png"));
        sound.setOpaque(false);
        sound.setContentAreaFilled(false);
        sound.setFocusPainted(false);
        sound.setBorderPainted(false);
        sound.setFocusable(false);
        sound.addActionListener(this);
        sound.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        sound.setLayout(null);
        sound.setBounds(745, 30, 48, 48);
        playSounds = true;
        frame.add(sound);

        //
        // Initialize Board Panel
        //
        BoardPanel boardPanel = new BoardPanel("./src/img/backgroundGameOffline.png");
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
        betButton = createButton("Bet", BET);

        betPanel.add(betAmountTextField);
        betPanel.add(betButton);        

        boardPanel.add(betPanel, createConstraints(0, 6, 1, 2, 0, 0, GridBagConstraints.HORIZONTAL, true));

        //
        // Initialize Action Panel
        //
        JPanel actionPanel = new JPanel();
        actionPanel.setOpaque(false);

        hitButton = createButton("Hit", HIT);
        actionPanel.add(hitButton);

        stayButton = createButton("Stay", STAY);
        actionPanel.add(stayButton);

        doubleDownButton = createButton("Double Down", DOUBLE_DOWN);
        actionPanel.add(doubleDownButton);

        splitButton = createButton("Split", SPLIT);
        actionPanel.add(splitButton);

        boardPanel.add(actionPanel, createConstraints(1, 6, 4, 2, 0, 0, GridBagConstraints.HORIZONTAL, true));

        //
        // Initialize Info Panel
        //
        JPanel infoPanel = new JPanel();
        playerCashLabel = new JLabel(" Player Cash: $ " + Integer.toString(playerCash));
        playerCashLabel.setFont(new java.awt.Font("Arial", 1, 12));
        infoPanel.add(playerCashLabel);

        playerWinsLabel = new JLabel(" Wins: 0 ");
        playerWinsLabel.setFont(new java.awt.Font("Arial", 1, 12));
        infoPanel.add(playerWinsLabel);

        boardPanel.add(infoPanel, createConstraints(5, 6, 1, 2, 0, 0, GridBagConstraints.HORIZONTAL, true));

        //
        // Initialize System Message Panel
        //
        JPanel systemMessagePanel = new JPanel();
        if (this.language.equals("iw")) {
            systemMessage = new JLabel("בחר סכום ולחץ על המר כדי להתחיל");
        } else {
            systemMessage = new JLabel("Enter an amount and press \"Bet\" to begin.");
        }
        systemMessage.setFont(new java.awt.Font("Arial", 1, 13));
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

        boardPanel.add(playersCardsPanel, createConstraints(0, 4, 6, 2, 1, 1, GridBagConstraints.BOTH, false));

        //
        // Update Timer
        //
        Timer t = new Timer(40, this);
        t.start();
        
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        updateState(Utils.GameState.BETTING);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (playerId != 0) {
                    user.setWins(playerWins);
                    user.setBalance(playerCash);
                    previous.setVisible(true);
                    previous.finishUpdatingGame(user);
                    previous.setVisible(true);
                } else if (guestPrev != null) {
                    guestPrev.setVisible(true);
                }
                frame.dispose();
            }
        });

        if (this.language.equals("iw")) {
            changeToHebrew();
        }
    }

    /**
     * changing the form to Hebrew
     */
    public void changeToHebrew() {
        LocalizationUtil.localizedResourceBundle = LocalizationUtil.getBundleGameIW();
        updateCaptions();
        LocalizationUtil.changeOptionPane_iw();
    }

    /**
     * changing all the buttons to Hebrew
     */
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

        switch (state) {
            case BETTING:
                betButton.setEnabled(true);
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                doubleDownButton.setEnabled(false);
                splitButton.setEnabled(false);
                break;
            case DEALER:
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

                updateState(Utils.GameState.RESOLVE);
                break;
            case DEALING:
                betButton.setEnabled(false);
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                doubleDownButton.setEnabled(false);
                splitButton.setEnabled(false);

                GameUtil.playShuffle();
                dealCard(currentHand);
                dealCard(dealerHand);
                dealCard(currentHand);
                dealCard(dealerHand, false);

                if (currentHand.isBlackjack() == true) {
                    updateState(Utils.GameState.RESOLVE);
                } else {
                    updateState(Utils.GameState.HITTING);
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

                determineWinner();

                // Start Over
                updateState(Utils.GameState.BETTING);
                break;
            default:
                break;
        }

        if (this.language.equals("iw")) {

            playerCashLabel.setText("קופה: $" + playerCash);
            playerWinsLabel.setText("ניצחונות: " + playerWins);
        } else {
            playerCashLabel.setText("Cash: $" + playerCash);
            playerWinsLabel.setText("Wins: " + playerWins);
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
        button.setFont(new java.awt.Font("Arial", 1, 12));

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
    private void updateState(Utils.GameState nextState) {
        this.nextState = nextState;
    }

    /**
     * Determines the winner of each hand and dispenses the winnings.
     */
    private void determineWinner() {
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
     * insert it into target hand. this method will be used for the second card
     * of the dealer because the card needs to be hidden. for all the other
     * cards the second method will be used with the faceUp as true.
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
            hand.addCard(newCard);
        }
    }

    /**
     * @see #dealCard(blackjack.Hand, boolean)
     */
    private void dealCard(Hand hand) {
        dealCard(hand, true);
    }

    /**
     * Handles GUI button events.
     *
     * @param arg0 the event
     */
    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (BET.equals(arg0.getActionCommand())) {
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
                    updateState(Utils.GameState.DEALING);
                }

            } catch (NumberFormatException e) {
                if (this.language.equals("iw")) {
                    systemMessage.setText("ההימור חייב להיות מספר...");
                } else {
                    systemMessage.setText("Bet must be an integer value...");
                }
                return;
            }
        } else if (HIT.equals(arg0.getActionCommand())) {
            //
            // Deal one card to current hand, check for bust
            // or blackjack, if not, allow the player to keep hitting
            // otherwise, go to DEALER state or next hand.
            //
            GameUtil.playShuffle();
            dealCard(currentHand);
            if (!currentHand.isBust() && !currentHand.isBlackjack()) {
                updateState(Utils.GameState.HITTING);
            } else if (currentHandIndex == (playerHands.size() - 1)) {
                updateState(Utils.GameState.DEALER);
            } else {
                currentHandIndex++;
            }

        } else if (STAY.equals(arg0.getActionCommand())) {
            //
            // Go to DEALER_AI state or next hand.
            //
            if (currentHandIndex == (playerHands.size() - 1)) {
                updateState(Utils.GameState.DEALER);
            } else {
                currentHandIndex++;
            }
        } else if (DOUBLE_DOWN.equals(arg0.getActionCommand())) {
            //
            // Deduct the hand's bet amount from player's available cash,
            // deal one card to the current hand and go to the DEALER
            // state or next hand.
            //
            playerCash -= currentHand.getBet();
            currentHand.setBet(currentHand.getBet() * 2);
            dealCard(currentHand);

            if (currentHandIndex == (playerHands.size() - 1)) {
                updateState(Utils.GameState.DEALER);
            } else {
                currentHandIndex++;
            }

        } else if (SPLIT.equals(arg0.getActionCommand())) {
            //
            // Split hand, deduct the new hand's bet
            // amount from the players cash and push
            // into the playerHands list.
            //
            Hand newHand = currentHand.split();
            playerCash -= currentHand.getBet();
            playerHands.add(newHand);
        } else if (SOUND.equals(arg0.getActionCommand())) {
            if (playSounds) {
                playSounds = false;
                //sound.setIcon(new ImageIcon("./src/img/mute.png"));
                sound.setIcon(new ImageIcon("./src/img/noAudio.png"));

            } else {
                playSounds = true;
                //sound.setIcon(new ImageIcon("./src/img/unmute.png"));
                sound.setIcon(new ImageIcon("./src/img/audio.png"));
            }
            sound.revalidate();
        }

        update();
    }
}
