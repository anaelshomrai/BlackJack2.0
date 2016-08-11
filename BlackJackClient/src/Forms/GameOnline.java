package Forms;

import DataUtil.CardsDealt;
import DataUtil.GameData;
import Resources.GameUtil;
import Resources.LocalizationUtil;
import Users.User;
import blackjack.BoardPanel;
import blackjack.Card;
import blackjack.Deck;
import blackjack.Hand;
import blackjack.Utils;
import blackjack.Utils.GameState;
import static blackjack.Utils.clubsImages;
import static blackjack.Utils.diamondsImages;
import static blackjack.Utils.heartsImages;
import static blackjack.Utils.spadesImages;
import blackjackclient.ConnectionUtil;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import static javax.swing.SwingConstants.CENTER;
import javax.swing.Timer;

/**
 * This class is the frame shown when a user plays online with 2 or 3 players.
 * this class is responsible for managing the game throughout the stages of the
 * game. this game consist 2 or 3 players and the dealer. possible game state:
 * Betting : Player places bet. Dealing: New cards are dealt. Hitting: Player is
 * prompted to either hit, stay, double down or split. Dealer: Flip hidden card
 * and draw until the total value of the dealers hand is equal to 17 or more.
 * Resolve: Flips the dealers cards, dealer hits til 17+, the winner is
 * revealed.
 *
 * The server decide according to the available players which game to start, a
 * game of 2 players and a dealer or a game of 3 players and a dealer. then a
 * thread is created with the appropriate class.
 *
 * @author ANI
 */
public class GameOnline extends JFrame implements ActionListener, Serializable {

    // Constant for the events
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
    /**
     * constant action command for timer events.
     */
    private static final String TIMER = "TIMER";

    //
    // GUI Components
    //
    /**
     * the frame of the game.
     */
    private JFrame frame = new JFrame("Blackjack");
    // labels   
    /**
     * list of labels containing the icons of player one cards.
     */
    private List<JLabel> player1Cards = new ArrayList<>();
    /**
     * list of labels containing the icons of player two cards.
     */
    private List<JLabel> player2Cards = new ArrayList<>();
    /**
     * list of labels containing the icons of this player cards.
     */
    private List<JLabel> myCards = new LinkedList<>();
    /**
     * list of labels containing the icons of this player split cards.
     */
    private List<JLabel> mySplitCards;
    /**
     * list of labels containing the icons of player one split cards.
     */
    private List<JLabel> player1SplitCards;
    /**
     * list of labels containing the icons of player two split cards.
     */
    private List<JLabel> player2SplitCards;
    /**
     * list of labels containing the icons of dealer two split cards.
     */
    private List<JLabel> dealerCards = new ArrayList<>();
    /**
     * the label for the avaliable cash of this player.
     */
    private JLabel cashLabel;
    /**
     * the label for the amount of players winning.
     */
    private JLabel winsLabel;
    /**
     * the label for this player user name.
     */
    private JLabel myName;
    /**
     * the label for player one user name.
     */
    private JLabel player1Name;
    /**
     * the label for player two user name.
     */
    private JLabel player2Name;
    /**
     * the label in the info panel, will show the direction for that the player
     * needs to do at every stage.
     */
    private JLabel systemMessage;

    // panels
    /**
     * the panel for the split cards of this player.
     */
    private JPanel mySplitCardsPanel;
    /**
     * the panel for the cards of this player.
     */
    private JPanel myCardsPanel;
    /**
     * the panel for the cards of player one.
     */
    private JPanel player1CardsPanel;
    /**
     * the panel for the cards of player two.
     */
    private JPanel player2CardsPanel;
    /**
     * the panel for the split cards of player one.
     */
    private JPanel player1SplitCardsPanel;
    /**
     * the panel for the split cards of player two.
     */
    private JPanel player2SplitCardsPanel;
    /**
     * the panel for status of the player during the game. include the winning
     * label and the cash label.
     */
    private JPanel statusPanel;
    /**
     * the panel the action button. include hit,stay,double down and split.
     */
    private JPanel actionPanel;
    /**
     * the panel for the bet button and the bet amount text field.
     */
    private JPanel betPanel;
    /**
     * the panel for presenting direction for the player in each state of the
     * game.
     */
    private JPanel infoPanel;
    /**
     * the panel for the dealer cards.
     */
    private JPanel dealerCardsPanel;
    /**
     * the panel of the frame, contain the background.
     */
    private BoardPanel boardPanel;

    // buttons
    /**
     * the button for bet, at each start of a game.
     */
    private JButton betButton;
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
     * the button for enable/disable the sound.
     */
    private JButton sound;
    /**
     * the button for split, will enable if the hand can be split (two first
     * card has the same value).
     */
    private JButton splitButton;

    /**
     * the textField for taking the input of a player bet.
     */
    private JTextField betAmountTextField;
    /**
     * the player logged in.
     */
    private User player;
    /**
     * the previous form.
     */
    private UserHome previous;

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
     * the language chosen.
     */
    private String language = "";

    /**
     * image icon with the hidden card.
     */
    private static ImageIcon cardBackImage = new ImageIcon("./src/img/cards/back.png");

    // Game variables
    /**
     * the deck of cards.
     */
    private Deck deck;
    /**
     * the player cash if he plays as a guest.
     */
    private int playerCash;
    /**
     * minimum bet for starting a game.
     */
    private static int MIN_BET = 10;
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
     * the x angle of the last dealer card.
     */
    private static int dealerX;
    /**
     * the y angle of the last dealer card.
     */
    private static int dealerY;
    /**
     * the x angle of the last card of this player.
     */
    private static int myX;
    /**
     * the y angle of the last card of this player.
     */
    private static int myY;
    /**
     * the x angle of the last split card of this player.
     */
    private int mySplitX;
    /**
     * point of the last card of player one.
     */
    private static Point player1Position;
    /**
     * point of the last card of player two.
     */
    private static Point player2Position;
    /**
     * width of a card image.
     */
    private static final int CARD_WIDTH = 170;
    /**
     * width of a card height.
     */
    private static final int CARD_HEIGHT = 190;
    /**
     * the connection for sending the server data.
     */
    private ConnectionUtil myConnection;
    /**
     * the data received from the server..
     */
    private GameData dataResponse;
    /**
     * list of cards dealt.
     */
    private List<Card> cards = new ArrayList<>();
    /**
     * list of split cards.
     */
    private List<Card> splitCards = new ArrayList<>();
    /**
     * represent if sounds are disabled/enabled. default is play sound.
     */
    private boolean playSounds;

    /**
     * is game started
     */
    public boolean gameOn = false;
    /**
     * is this player has a blackjack
     */
    private boolean blackjack = false;
    /**
     * number of players in this game.
     */
    private int numOfPlayers;
    /**
     * my thread id in the server side.
     */
    private long myId;
    /**
     * player one thread id in the server side.
     */
    private long player1Id;
    /**
     * player two thread id in the server side.
     */
    private long player2Id;
    /**
     * is this a game of 2 players. (this player and another one).
     */
    private boolean gameOf2 = false;
    /**
     * is this was a game of 3 player.
     */
    private boolean changeGame = false;
    /**
     * option of the JOptionPane.
     */
    private int option;
    /**
     * thread for the winning pop-up.
     */
    private Thread winningPopup;
    /**
     * a label that show how much time left.
     */
    private JLabel timeLabel;
    /**
     * the time for a player to make his move.
     */
    private int time;
    /**
     * the timer for going to the next player if the player hasn't done his move
     * within the give time.
     */
    private Timer timer;
    /**
     * stage of the game.
     */
    private String stage = "";

    /**
     * Initialize a new Timer Object. will cause events every 1 second. will be
     * used to go on to the next player.
     */
    public void initTimer() {
        timeLabel = new JLabel();
        timer = new Timer(1000, this);
        timer.setActionCommand(TIMER);
        Font timeFont = new Font("Courier", Font.BOLD, 12);
        timeLabel.setFont(timeFont);
        timeLabel.setForeground(Color.darkGray);
    }

    /**
     * Starts the timer for the first time.
     */
    public void startTimer() {
        time = 20;
        timer.start();
    }

    /**
     * Start the timer again.
     */
    public void restart() {
        timer = new Timer(1000, this);
        timer.setActionCommand(TIMER);
        time = 20;
        timer.start();
    }

    /**
     * Constructor create a new game, setting player details. here we wait to
     * get an answer from the server that the game has started. the player needs
     * to click OK to wait for players until at least 2 is in the queue for
     * starting the game (including this player), or Cancel if he don't want to
     * wait. when we receive from the server that the game has started, we also
     * gets the number of players in this game, and setting the players details
     * as well as this player. then we create the appropriate GUI for the amount
     * of players. and creating the thread for managing the game states.
     *
     * @param player this player
     * @param previous the previous form
     * @param language the language selected
     */
    public GameOnline(User player, UserHome previous, String language) {
        this.previous = previous;
        this.player = player;
        this.playerId = player.getId();
        this.playerCash = player.getBalance();
        this.playerWins = player.getWins();
        this.myName = new JLabel(player.getUserName());
        if (!language.equals("")) {
            this.language = language;
        }
        try {
            playerHands.add(new Hand());

            myConnection = new ConnectionUtil();
            myConnection.openConnection();
            myConnection.waitingForPlayers(player);
            dataResponse
                    = (GameData) myConnection.getOis().readObject();

            numOfPlayers = dataResponse.getPlayerNum();
            boolean isStarted = dataResponse.isGameStart();

            if (isStarted == false) {
                if (this.language.equals("iw")) {
                    LocalizationUtil.changeOptionPane_iw();
                    option = JOptionPane.showConfirmDialog(this,
                            MyJOptionPane.getWaitingIWPanel(),
                            "ממתין..",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE);

                } else {
                    option = JOptionPane.showConfirmDialog(this,
                            MyJOptionPane.getWaitingENPanel(),
                            "Waiting..",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE);
                }
                if (option == JOptionPane.OK_OPTION) {
                    myConnection.continueToGame();
                } else if (option == JOptionPane.CANCEL_OPTION) {
                    myConnection.setStatus(1);
                    myConnection.exitGame(player);
                    myConnection.closeConnection();
                    previous.setVisible(true);
                    setVisible(false);
                    dispose();
                }
            } else {
                myConnection.continueToGame();
            }

            dataResponse
                    = (GameData) myConnection.getOis().readObject();
            numOfPlayers = dataResponse.getPlayerNum();
            gameOn = dataResponse.isGameStart();
            this.deck = dataResponse.getDeck();
            myId = dataResponse.getMyId();
            if (numOfPlayers == 2) {
                player1Id = dataResponse.getPlayer1Id();
                player1Name = new JLabel(dataResponse.getPlayer1Name());
                initializeGUI2();
            } else {
                player1Id = dataResponse.getPlayer1Id();
                player2Id = dataResponse.getPlayer2Id();
                player1Name = new JLabel(dataResponse.getPlayer1Name());
                player2Name = new JLabel(dataResponse.getPlayer2Name());
                initializeGUI();
            }

        } catch (SocketTimeoutException ex) {
            Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            myConnection.closeConnection();
            previous.setVisible(true);
            previous.showExitDialog();
            frame.dispose();
        } catch (IOException ex) {
            Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initialize the cards Images, calling for initializing the frame
     * components for a game of 3 players, and creating the thread.
     */
    public void initializeGUI() {
        gameOf2 = false;
        Utils.map.put(Utils.SUIT.SPADES, spadesImages);
        Utils.map.put(Utils.SUIT.HEARTS, heartsImages);
        Utils.map.put(Utils.SUIT.DIAMONDS, diamondsImages);
        Utils.map.put(Utils.SUIT.CLUBS, clubsImages);
        Utils.cardImages = Collections.unmodifiableMap(Utils.map);
        initFrame();
        waitForYourTurn();

    }

    /**
     * Initialize the cards Images, calling for initializing the frame
     * components for a game of 2 players, and creating the thread.
     */
    public void initializeGUI2() {
        gameOf2 = true;
        if (changeGame) {
            myName.setText(player.getUserName());
        }
        Utils.map.put(Utils.SUIT.SPADES, spadesImages);
        Utils.map.put(Utils.SUIT.HEARTS, heartsImages);
        Utils.map.put(Utils.SUIT.DIAMONDS, diamondsImages);
        Utils.map.put(Utils.SUIT.CLUBS, clubsImages);
        Utils.cardImages = Collections.unmodifiableMap(Utils.map);
        initFrame2();
        waitForYourTurn2();

    }

    /**
     * Creating new Thread that will run as long as this game of 3 is on.
     */
    public void waitForYourTurn() {
        Thread thread = new Thread(new GameOf3Listener());
        thread.start();
    }

    /**
     * Creating new Thread that will run as long as this game of 2 is on.
     */
    public void waitForYourTurn2() {
        Thread thread = new Thread(new GameOf2Listener());
        thread.start();
    }

    /**
     * Initializing the frame components for a game of 3 players.
     */
    public void initFrame() {

        frame.setSize(new Dimension(800, 725));
        frame.setPreferredSize(new Dimension(800, 725));
        frame.setResizable(false);

        String file = "./src/img/card-symbols_bsmall.png";
        ImageIcon img = new ImageIcon(file);
        frame.setIconImage(img.getImage());
        frame.setTitle("BlackJack ANI");

        //init dealer
        initDealer();

        //init players
        initPlayers();

        //init betPanel
        initBetPanel();

        //init actionPanel
        initActionPanel();

        //init StatusPanel
        initStatusPanel();

        //init messgae panel may be gone
        initInfoPanel();

        boardPanel = new BoardPanel("./src/img/background.png");
        boardPanel.setLayout(null);
        boardPanel.setSize(new Dimension(800, 700));
        boardPanel.setPreferredSize(new Dimension(800, 700));

        boardPanel.add(myName);
        myName.setBounds(290, 635, 80, 30);
        myName.setFont(new java.awt.Font("Tahoma", 1, 18));
        myName.setForeground(new java.awt.Color(246, 246, 60));

        boardPanel.add(player1Name);
        player1Name.setBounds(30, 635, 80, 30);
        player1Name.setFont(new java.awt.Font("Tahoma", 1, 18));
        player1Name.setForeground(new java.awt.Color(0, 0, 0));

        boardPanel.add(player2Name);
        player2Name.setBounds(550, 635, 80, 30);
        player2Name.setFont(new java.awt.Font("Tahoma", 1, 18));
        player2Name.setForeground(new java.awt.Color(0, 0, 0));

        sound = new JButton();
        sound.setActionCommand(SOUND);
        sound.setIcon(new ImageIcon("./src/img/audio.png"));
        sound.setOpaque(false);
        sound.setContentAreaFilled(false);
        sound.setBorderPainted(false);
        sound.addActionListener(this);
        sound.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        boardPanel.add(sound);
        sound.setBounds(745, 30, 48, 48);
        playSounds = true;

        frame.add(boardPanel);
        setExitListener();

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        if (this.language.equals("iw")) {
            changeToHebrew();
        }
    }

    /**
     * Set the default operation when this frame closed. when a player exit the
     * game after the game has started, we update his wins and balance. if we
     * found that after 2 minutes there isn't enough players we dispose this
     * frame and show exit pop-up.
     */
    private void setExitListener() {

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                playSounds = false;
                player.setWins(playerWins);
                player.setBalance(playerCash);
                myConnection.closeConnection();
                previous.setVisible(true);
                updateUser();
                frame.dispose();
            }
        });
    }

    /**
     * Initializing the frame components for a game of 2 players.
     */
    public void initFrame2() {
        gameOf2 = true;
        frame.setSize(new Dimension(800, 725));
        frame.setPreferredSize(new Dimension(800, 725));
        frame.setResizable(false);

        String file = "./src/img/card-symbols_bsmall.png";
        ImageIcon img = new ImageIcon(file);
        frame.setIconImage(img.getImage());
        frame.setTitle("BlackJack ANI");

        //init dealer
        initDealer();

        //init players
        initPlayers();

        //init betPanel
        initBetPanel();

        //init actionPanel
        initActionPanel();

        //init StatusPanel
        initStatusPanel();

        //init messgae panel may be gone
        initInfoPanel();

        boardPanel = new BoardPanel("./src/img/background.png");
        boardPanel.setLayout(null);
        boardPanel.setSize(new Dimension(800, 700));
        boardPanel.setPreferredSize(new Dimension(800, 700));

        boardPanel.add(myName);
        myName.setBounds(460, 635, 80, 30);
        myName.setFont(new java.awt.Font("Tahoma", 1, 18));
        myName.setForeground(new java.awt.Color(246, 246, 60));

        boardPanel.add(player1Name);
        player1Name.setBounds(200, 635, 80, 30);
        player1Name.setFont(new java.awt.Font("Tahoma", 1, 18));
        player1Name.setForeground(new java.awt.Color(0, 0, 0));

        sound = new JButton();
        sound.setActionCommand(SOUND);
        sound.setIcon(new ImageIcon("./src/img/audio.png"));
        sound.setOpaque(false);
        sound.setContentAreaFilled(false);
        sound.setBorderPainted(false);
        sound.addActionListener(this);
        sound.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        boardPanel.add(sound);
        sound.setBounds(745, 30, 48, 48);
        playSounds = true;

        frame.add(boardPanel);

        //here the game really start
        // updateState(GameOnline.GameState.BETTING);
        //frame.pack();
        setExitListener();

        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        if (this.language.equals("iw")) {
            changeToHebrew();
        }
    }

    /**
     * Change the frame from a 3 players game to 2 players game, and create the
     * thread for handle a game of 2.
     */
    public void changeTo2PlayersGUI() {
        boardPanel.remove(player2Name);
        myName.setBounds(460, 635, 80, 30);
        player1Name.setBounds(200, 635, 80, 30);
        myCardsPanel.setBounds(460, 460, 260, 200);
        mySplitCardsPanel.setBounds(460, 280, 260, 200);
        player1CardsPanel.setBounds(200, 460, 260, 200);
        player1SplitCardsPanel.setBounds(200, 280, 260, 200);
        waitForYourTurn2();
    }

    /**
     * Initializing the info panel components. contains the label of the game
     * directions
     */
    public void initInfoPanel() {
        infoPanel = new JPanel();
        systemMessage = new JLabel("");
        initTimer();

        systemMessage.setVerticalAlignment(CENTER);
        systemMessage.setFont(new java.awt.Font("Arial", 1, 13));
        infoPanel.setLayout(new java.awt.GridBagLayout());
        infoPanel.setOpaque(true);

        infoPanel.add(systemMessage);
        infoPanel.add(timeLabel);

        frame.add(infoPanel);
        infoPanel.setBounds(0, 0, 800, 20);
    }

    /**
     * Initializing the status panel components. contains cash label and the
     * winning label.
     */
    public void initStatusPanel() {
        statusPanel = new JPanel();
        cashLabel = new JLabel();
        winsLabel = new JLabel();

        statusPanel.setLayout(new FlowLayout());
        statusPanel.setOpaque(true);

        cashLabel.setText("Cash :" + playerCash);
        cashLabel.setFont(new java.awt.Font("Arial", 1, 14));
        statusPanel.add(cashLabel);

        winsLabel.setText("Wins:" + playerWins);
        winsLabel.setFont(new java.awt.Font("Arial", 1, 14));
        statusPanel.add(winsLabel);

        frame.add(statusPanel);
        statusPanel.setBounds(590, 670, 200, 30);
    }

    /**
     * Initializing the action panel components. contains the avaliable action
     * in a blackjack game: hit button, stay button, double down button and
     * split button.
     */
    public void initActionPanel() {
        actionPanel = new JPanel();
        hitButton = new JButton();
        hitButton.setActionCommand(HIT);
        hitButton.addActionListener(this);
        hitButton.setEnabled(false);

        stayButton = new JButton();
        stayButton.setActionCommand(STAY);
        stayButton.addActionListener(this);
        stayButton.setEnabled(false);

        doubleDownButton = new JButton();
        doubleDownButton.setActionCommand(DOUBLE_DOWN);
        doubleDownButton.addActionListener(this);
        doubleDownButton.setEnabled(false);

        splitButton = new JButton();
        splitButton.setActionCommand(SPLIT);
        splitButton.addActionListener(this);
        splitButton.setEnabled(false);

        actionPanel.setLayout(new GridLayout());
        actionPanel.setOpaque(false);

        hitButton.setText("Hit");
        hitButton.setFont(new java.awt.Font("Arial", 1, 12));
        actionPanel.add(hitButton);

        stayButton.setText("Stay");
        stayButton.setFont(new java.awt.Font("Arial", 1, 12));
        actionPanel.add(stayButton);

        doubleDownButton.setText("Double Down");
        doubleDownButton.setFont(new java.awt.Font("Arial", 1, 12));
        actionPanel.add(doubleDownButton);

        splitButton.setText("Split");
        splitButton.setFont(new java.awt.Font("Arial", 1, 12));
        actionPanel.add(splitButton);

        frame.add(actionPanel);
        actionPanel.setBounds(130, 670, 450, 30);
    }

    /**
     * Initializing the bet panel components. contains the bet button and the
     * text field for the bet input from the player.
     */
    public void initBetPanel() {
        betPanel = new JPanel();
        betAmountTextField = new JTextField();

        betButton = new JButton();
        betButton.setActionCommand(BET);
        betButton.addActionListener(this);
        betButton.setEnabled(false);

        betPanel.setBackground(new java.awt.Color(0, 0, 0));
        betPanel.setOpaque(false);
        betPanel.setLayout(new java.awt.GridLayout());

        betAmountTextField.setText("Amount");
        betAmountTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                betAmountTextField.setText("");
            }
        });
        betPanel.add(betAmountTextField);

        betButton.setText("Bet");
        betButton.setFont(new java.awt.Font("Arial", 1, 12));
        betPanel.add(betButton);

        frame.add(betPanel);
        betPanel.setBounds(1, 670, 120, 30);
    }

    /**
     * Initializing the panels of the players.
     */
    public void initPlayers() {
        myCardsPanel = new JPanel();
        myCardsPanel.setLayout(null);
        myCardsPanel.setOpaque(false);

        player1CardsPanel = new JPanel();
        player1CardsPanel.setLayout(null);
        player1CardsPanel.setOpaque(false);

        if (!gameOf2) {
            player2CardsPanel = new JPanel();
            player2CardsPanel.setLayout(null);
            player2CardsPanel.setOpaque(false);
        }
        mySplitCardsPanel = new JPanel();
        mySplitCardsPanel.setLayout(null);
        mySplitCardsPanel.setOpaque(false);

        player1SplitCardsPanel = new JPanel();
        player1SplitCardsPanel.setLayout(null);
        player1SplitCardsPanel.setOpaque(false);

        if (!gameOf2) {
            player2SplitCardsPanel = new JPanel();
            player2SplitCardsPanel.setLayout(null);
            player2SplitCardsPanel.setOpaque(false);
        }

        initPlayersJLabels();
        initSplit();

        frame.add(myCardsPanel);
        myCardsPanel.setBounds(290, 460, 260, 200);

        frame.add(mySplitCardsPanel);
        mySplitCardsPanel.setBounds(290, 280, 260, 200);

        frame.getContentPane().add(player1CardsPanel);
        player1CardsPanel.setBounds(30, 460, 260, 200);

        frame.getContentPane().add(player1SplitCardsPanel);
        player1SplitCardsPanel.setBounds(30, 280, 260, 200);

        if (!gameOf2) {
            frame.getContentPane().add(player2CardsPanel);
            player2CardsPanel.setBounds(550, 460, 260, 200);

            frame.getContentPane().add(player2SplitCardsPanel);
            player2SplitCardsPanel.setBounds(550, 280, 260, 200);
        } else {
            myCardsPanel.setBounds(460, 460, 260, 200);

            mySplitCardsPanel.setBounds(460, 280, 260, 200);

            player1CardsPanel.setBounds(200, 460, 260, 200);

            player1SplitCardsPanel.setBounds(200, 280, 260, 200);

        }
    }

    /**
     * Initializing the list of labels of the players. contains the card images.
     */
    public void initPlayersJLabels() {
        myCards.add(new JLabel());
        myCards.add(new JLabel());
        myCards.get(1).setBounds(20, 0, CARD_WIDTH, CARD_HEIGHT);
        myCards.get(1).setName("");
        myCards.get(0).setBounds(0, 0, CARD_WIDTH, CARD_HEIGHT);
        myCards.get(0).setName("");

        myCardsPanel.add(myCards.get(1));
        myCardsPanel.add(myCards.get(0));

        myX = 40;
        myY = 0;

        player1Cards.add(new JLabel());
        player1Cards.add(new JLabel());
        player1Cards.get(1).setBounds(20, 0, CARD_WIDTH, CARD_HEIGHT);
        player1Cards.get(1).setName("");
        player1Cards.get(0).setBounds(0, 0, CARD_WIDTH, CARD_HEIGHT);
        player1Cards.get(0).setName("");

        player1CardsPanel.add(player1Cards.get(1));
        player1CardsPanel.add(player1Cards.get(0));

        if (!gameOf2) {
            player2Cards.add(new JLabel());
            player2Cards.add(new JLabel());
            player2Cards.get(1).setBounds(20, 0, CARD_WIDTH, CARD_HEIGHT);
            player2Cards.get(1).setName("");
            player2Cards.get(0).setBounds(0, 0, CARD_WIDTH, CARD_HEIGHT);
            player2Cards.get(0).setName("");

            player2CardsPanel.add(player2Cards.get(1));
            player2CardsPanel.add(player2Cards.get(0));
            player2Position = new Point(40, 0);

        }
        player1Position = new Point(40, 0);
    }

    /**
     * Initializing the panel of the dealer.
     */
    public void initDealer() {
        dealerCardsPanel = new JPanel();
        dealerCardsPanel.setLayout(null);

        initDealerJLabels();

        frame.add(dealerCardsPanel);
        dealerCardsPanel.setBounds(290, 30, 260, 290);
        dealerCardsPanel.setOpaque(false);

    }

    /**
     * Initializing the list of labels of the dealer. contains the card images.
     */
    public void initDealerJLabels() {

        dealerCards.add(new JLabel());
        dealerCards.add(new JLabel());

        dealerCardsPanel.add(dealerCards.get(1));
        dealerCards.get(1).setBounds(20, 10, CARD_WIDTH, CARD_HEIGHT); //back
        dealerCardsPanel.add(dealerCards.get(0));
        dealerCards.get(0).setBounds(0, 10, CARD_WIDTH, CARD_HEIGHT); //exposeF
        dealerX = 40;
        dealerY = 10;
    }

    /**
     * This method deal the cards at the DEALING stage, when dealing 2 cards. if
     * the dealer cards hasn't dealt we deal the card his two cards.
     */
    public void dealCards() {
        initDealerJLabels();
        initPlayersJLabels();
        initSplit();

        int counter = 0;
        for (int i = 0; i < 2; i++) {
            Card c = dealCard(myCards.get(counter));
            playerHands.get(currentHandIndex).addCard(c);
            cards.add(c);
            counter++;
        }

        if (dealerHand.getSize() == 0) {
            dealerHand.addCard(dealCard(dealerCards.get(0)));
            dealerHand.addCard(dealCard(dealerCards.get(1), false));
        }

        currentHand = playerHands.get(currentHandIndex);

    }

    /**
     * Draw one card from the deck, if it's null reshuffle the deck, otherwise
     * insert it into target hand. this method will be used for the second card
     * of the dealer because the card needs to be hidden. for all the other
     * cards the second method will be used with the faceUp as true.
     */
    private Card dealCard(JLabel card, boolean faceUp) {
        Card newCard = deck.drawCard(faceUp);
        if (newCard == null) // Reshuffle if we are out of cards.
        {
            deck.reshuffle();
            dealCard(card, faceUp);
        } else if (faceUp) {
            card.setIcon(new javax.swing.ImageIcon(getCardImage(newCard).getImage()));
        } else {
            card.setIcon(new javax.swing.ImageIcon(cardBackImage.getImage()));
        }
        return newCard;
    }

    /**
     * This method received a card and return his image.
     *
     * @param card the card
     * @return the image of the card
     */
    private static ImageIcon getCardImage(Card card) {
        return Utils.cardImages.get(card.getSuit())[card.getRank().ordinal()];
    }

    /**
     * @see #dealCard(JLabel, boolean)
     */
    private Card dealCard(JLabel card) {
        return dealCard(card, true);
    }

    /**
     * Change the game language to Hebrew.
     */
    public void changeToHebrew() {
        LocalizationUtil.localizedResourceBundle = LocalizationUtil.getBundleGameIW();
        updateCaptions();
        revalidate();
        repaint();
    }

    /**
     * Change the buttons captions to Hebrew.
     */
    public void updateCaptions() {
        betButton.setText(LocalizationUtil.localizedResourceBundle.getString("betButton"));
        hitButton.setText(LocalizationUtil.localizedResourceBundle.getString("hitButton"));
        stayButton.setText(LocalizationUtil.localizedResourceBundle.getString("stayButton"));
        doubleDownButton.setText(LocalizationUtil.localizedResourceBundle.getString("doubleDownButton"));
        splitButton.setText(LocalizationUtil.localizedResourceBundle.getString("splitButton"));
    }

    /**
     * Updates the state of the game and refreshes the GUI. This method enable
     * and disable the buttons according to the state, and sets the system
     * message (of the info panel).
     */
    public void play() {

        state = nextState; // Update our state, first.
        currentHand = playerHands.get(currentHandIndex);

        switch (state) {
            case BETTING:
                betButton.setEnabled(true);
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                doubleDownButton.setEnabled(false);
                splitButton.setEnabled(false);
                betAmountTextField.setEnabled(true);

                if (this.language.equals("iw")) {
                    systemMessage.setText("בחר סכום ולחץ על המר כדי להתחיל");
                } else {
                    systemMessage.setText("Enter an amount and press \"Bet\" to begin.");
                }
                startTimer();
                stage = BET;
                break;
            case DEALER:
                betButton.setEnabled(false);
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                doubleDownButton.setEnabled(false);
                splitButton.setEnabled(false);

                // Flip hidden card.
                dealerHand.getCard(1).reveal();
                ImageIcon backCard = getCardImage(dealerHand.getCards().get(1));
                dealerCards.get(1).setIcon(new javax.swing.ImageIcon(backCard.getImage()));

                if (dealerCards.size() == 2) {
                    Collections.reverse(dealerCards);
                }

                // Draw until 17+.
                while (dealerHand.getValue() < 17) {
                    JLabel lab = new JLabel();
                    dealerHand.addCard(dealCard(lab));
                    Collections.reverse(dealerCards);
                    dealerCards.add(lab);
                    lab.setBounds(dealerX, dealerY, CARD_WIDTH, CARD_HEIGHT);
                    dealerX += 20;
                    Collections.reverse(dealerCards);
                    dealerCardsPanel.add(lab);

                }
                dealerCardsPanel.removeAll();
                for (JLabel l : dealerCards) {
                    dealerCardsPanel.add(l);
                }

                dealerCardsPanel.revalidate();

                updateState(Utils.GameState.RESOLVE);
                break;
            case DEALING:
                betButton.setEnabled(false);
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                doubleDownButton.setEnabled(false);
                splitButton.setEnabled(false);

                if (playSounds) {
                    GameUtil.playShuffle();
                }

                dealCards();

                if (currentHand.isBlackjack() == true) {
                    updateState(Utils.GameState.RESOLVE);
                    blackjack = true;
                }
                myConnection.setCardsNDeck(cards, deck, dealerHand);

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

                if (cards.size() == 2) {
                    if (currentHand.isSplit()) {
                        splitButton.setEnabled(true);
                    } else {
                        splitButton.setEnabled(false);
                    }
                }
                restart();
                stage = HIT;
                break;
            case RESOLVE:
                betButton.setEnabled(false);
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                doubleDownButton.setEnabled(false);
                splitButton.setEnabled(false);

                // If we came straight to resolve without hitting dealer ai (blackjack or bust)
                // reveal the hidden card.
                if (dealerHand.getCard(1).isHidden() == true) {
                    dealerHand.getCard(1).reveal();
                    ImageIcon backCard1 = getCardImage(dealerHand.getCards().get(1));
                    dealerCards.get(1).setIcon(new javax.swing.ImageIcon(backCard1.getImage()));
                }

                determineWinner();

                break;
            default:
                break;
        }

        if (this.language.equals("iw")) {

            cashLabel.setText("קופה: $" + playerCash);
            winsLabel.setText("ניצחונות: " + playerWins);
        } else {
            cashLabel.setText("Cash: $" + playerCash);
            winsLabel.setText("Wins: " + playerWins);
        }
        frame.repaint();

    }

    /**
     * Determines the winner of each hand and dispenses the winnings.
     */
    private void determineWinner() {
        //
        // For each hand the player has, determine
        // a winner and give the appropriate payout.
        //
        int win = 0;
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
                    String winLabel = "You win hand [" + (x + 1) + "] !";
                    if (win > 0) {
                        winLabel = "You win hand[1] and hand[2] !";
                    }
                    if (this.language.equals("iw")) {
                        systemMessage.setText("ניצחת!!!");
                    } else {
                        systemMessage.setText(winLabel);
                    }
                    if (playSounds) {
                        winningPopup = new Thread(new WinningPopup());
                        winningPopup.start();
                    }
                    win = 1;

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
     * Sets the next game state.
     *
     * @param nextState the next state of the game
     */
    public void updateState(Utils.GameState nextState) {
        this.nextState = nextState;
        play();
    }

    //Action Events
    /**
     * Handles GUI button events. BET - Make sure the player has enough cash, is
     * within the bet domain of this table, set-up hands. This is the first
     * stage of the game, we ensure that the hands are empty before dealing
     * begins. HIT - Deal one card to current hand, check for bust or blackjack,
     * if not allow the player to keep hitting otherwise the turn is finished.
     * STAY - player finish his turn. DOUBLE_DOWN - Deduct bet amount from
     * player's available cash, deal one card to the current hand and finish the
     * turn. SPLIT - Split hand, deduct the new hand bet amount from the players
     * cash SOUND - Disable/Enable sounds in the game. TIMER - if time ends in
     * BET stage, then the player place an automatic bet of 15, if time ends in
     * HIT stage, then the player does an automatic STAY. BET_AMOUNT - If bet
     * amount text field clicked, clean the text.
     *
     * @param ae the event
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (ae.getActionCommand()) {
            case BET:
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
                        clearGame();

                        stage = "OK";
                        nextTurn();
                        betAmountTextField.setEnabled(false);
                        playerCash -= betAmount;
                        currentHand.setBet(betAmount);

                        betButton.setEnabled(false);
                        myConnection.setDone(true);
                    }

                } catch (NumberFormatException e) {
                    if (this.language.equals("iw")) {
                        systemMessage.setText("ההימור חייב להיות מספר...");
                    } else {
                        systemMessage.setText("Bet must be an integer value...");
                    }

                }
                break;
            case HIT:
                if (currentHandIndex == 0) { //first Hand

                    if (myCards.size() == 2) {
                        Collections.reverse(myCards);
                    }
                    myConnection.setCardsAdded(true);
                    if (playSounds) {
                        GameUtil.playShuffle();
                    }
                    JLabel labToAdd = new JLabel();
                    Card c = dealCard(labToAdd);
                    playerHands.get(currentHandIndex).addCard(c);
                    cards.add(c);
                    Collections.reverse(myCards);
                    myCards.add(labToAdd);
                    labToAdd.setBounds(myX, myY, CARD_WIDTH, CARD_HEIGHT);
                    myX += 20;
                    Collections.reverse(myCards);

                    myCardsPanel.removeAll();
                    for (JLabel l : myCards) {
                        myCardsPanel.add(l);
                    }

                    currentHand = playerHands.get(currentHandIndex);

                    myCardsPanel.revalidate();
                    frame.validate();
                    frame.repaint();

                } else if (currentHandIndex == 1) {
                    if (mySplitCards.size() == 2) {
                        Collections.reverse(mySplitCards);
                    }
                    myConnection.setCardsAdded(true);
                    if (playSounds) {
                        GameUtil.playShuffle();
                    }
                    JLabel labToAdd = new JLabel();
                    Card c = dealCard(labToAdd);
                    playerHands.get(currentHandIndex).addCard(c);
                    splitCards.add(c);
                    Collections.reverse(mySplitCards);
                    mySplitCards.add(labToAdd);
                    labToAdd.setBounds(mySplitX, 10, CARD_WIDTH, CARD_HEIGHT);
                    mySplitX += 20;
                    Collections.reverse(mySplitCards);

                    mySplitCardsPanel.removeAll();
                    for (JLabel l : mySplitCards) {
                        mySplitCardsPanel.add(l);
                    }
                    currentHand = playerHands.get(currentHandIndex);

                    myCardsPanel.revalidate();
                    frame.validate();
                    frame.repaint();

                }
                if (!currentHand.isBust() && !currentHand.isBlackjack() && currentHand.getValue() < 21) {
                    updateState(Utils.GameState.HITTING);
                } else if (currentHandIndex == (playerHands.size() - 1)) { //if we dont have a split/we are in the last hand
                    betButton.setEnabled(false);
                    hitButton.setEnabled(false);
                    stayButton.setEnabled(false);
                    doubleDownButton.setEnabled(false);
                    splitButton.setEnabled(false);
                    if (playerHands.size() == 1) {
                        myConnection.nextMove(cards, deck);
                    } else {
                        myConnection.nextMove(splitCards, cards, deck);
                    }
                    stage = "OK";
                    nextTurn();

                } else {
                    currentHandIndex++;
                    updateState(Utils.GameState.HITTING);
                }
                break;
            case STAY:
                if (currentHandIndex == (playerHands.size() - 1)) {
                    betButton.setEnabled(false);
                    hitButton.setEnabled(false);
                    stayButton.setEnabled(false);
                    doubleDownButton.setEnabled(false);
                    splitButton.setEnabled(false);
                    if (playerHands.size() == 1) {
                        myConnection.nextMove(cards, deck);
                    } else {
                        myConnection.nextMove(splitCards, cards, deck);
                    }
                    stage = "OK";
                    nextTurn();
                } else {
                    currentHandIndex++;
                    updateState(Utils.GameState.HITTING);
                }
                break;
            case DOUBLE_DOWN:

                if (currentHandIndex == 0) { //first Hand

                    if (myCards.size() == 2) {
                        Collections.reverse(myCards);
                    }
                    myConnection.setCardsAdded(true);
                    if (playSounds) {
                        GameUtil.playShuffle();
                    }
                    JLabel labToAdd = new JLabel();
                    Card c = dealCard(labToAdd);
                    playerHands.get(currentHandIndex).addCard(c);
                    cards.add(c);
                    Collections.reverse(myCards);
                    myCards.add(labToAdd);
                    labToAdd.setBounds(myX, myY, CARD_WIDTH, CARD_HEIGHT);
                    myX += 20;
                    Collections.reverse(myCards);

                    myCardsPanel.removeAll();
                    for (JLabel l : myCards) {
                        myCardsPanel.add(l);
                    }

                    myCardsPanel.revalidate();
                    frame.validate();
                    frame.repaint();

                } else if (currentHandIndex == 1) { //second hand
                    if (mySplitCards.size() == 2) {
                        Collections.reverse(mySplitCards);
                    }
                    myConnection.setCardsAdded(true);
                    GameUtil.playShuffle();
                    JLabel labToAdd = new JLabel();
                    Card c = dealCard(labToAdd);
                    playerHands.get(currentHandIndex).addCard(c);
                    splitCards.add(c);
                    Collections.reverse(mySplitCards);
                    mySplitCards.add(labToAdd);
                    labToAdd.setBounds(mySplitX, 10, CARD_WIDTH, CARD_HEIGHT);
                    mySplitX += 20;
                    Collections.reverse(mySplitCards);

                    mySplitCardsPanel.removeAll();
                    for (JLabel l : mySplitCards) {
                        mySplitCardsPanel.add(l);
                    }
                    currentHand = playerHands.get(currentHandIndex);

                    myCardsPanel.revalidate();
                    frame.validate();
                    frame.repaint();

                }
                currentHand = playerHands.get(currentHandIndex);
                if (currentHandIndex == (playerHands.size() - 1)) {
                    betButton.setEnabled(false);
                    hitButton.setEnabled(false);
                    stayButton.setEnabled(false);
                    doubleDownButton.setEnabled(false);
                    splitButton.setEnabled(false);
                    if (playerHands.size() == 1) {
                        myConnection.nextMove(cards, deck);
                    } else {
                        myConnection.nextMove(splitCards, cards, deck);

                    }
                    stage = "OK";
                    nextTurn();
                } else {
                    currentHandIndex++;
                }
                break;
            case SPLIT:
                time += 10;
                Hand newHand = currentHand.split();
                //after spliting hand, i got 1 card in current and one in the new
                playerCash -= currentHand.getBet(); // bets are doubled
                playerHands.add(newHand);
                myConnection.setSplit(true);
                split();
                break;
            case SOUND:
                if (playSounds) {
                    playSounds = false;
                    sound.setIcon(new ImageIcon("./src/img/noAudio.png"));

                } else {
                    playSounds = true;
                    sound.setIcon(new ImageIcon("./src/img/audio.png"));
                }
                sound.revalidate();
                break;
            case TIMER:
                if (time == 0) {
                    if (this.language.equals("iw")) {
                        timeLabel.setText("זמן נותר : " + 0);
                    } else {
                        timeLabel.setText(" Time Left : " + 0);
                    }
                    timer.stop();
                } else {
                    time--;
                    if (this.language.equals("iw")) {
                        timeLabel.setText(" זמן נותר: " + time);
                    } else {
                        timeLabel.setText(" Time Left : " + time);
                    }
                    frame.repaint();
                }
                break;
            default:
                break;
        }

    }

    /**
     * Play an automatic move for the player, if the time ends and stage isn't
     * "OK".
     */
    public void nextTurn() {

        switch (stage) {
            case BET:
                betAmountTextField.setText("15");
                betButton.doClick();
                timeLabel.setText("");
                repaint();
                break;
            case HIT:
                stayButton.doClick();
                timeLabel.setText("");
                repaint();
                break;
            case "OK":
                timer.stop();
                timeLabel.setText("");
                repaint();
                stage = "";
                break;
        }
    }

    /**
     * Clear JPanel,JLabel,cards lists. set everything from start for the new
     * game.
     */
    public void clearGame() {
        //clear panels          
        myCardsPanel.removeAll();
        mySplitCardsPanel.removeAll();
        player1CardsPanel.removeAll();
        player1SplitCardsPanel.removeAll();

        if (!gameOf2) {
            player2CardsPanel.removeAll();
            player2SplitCardsPanel.removeAll();
        }
        //clear playerHand
        playerHands.clear();
        playerHands.add(new Hand());
        currentHandIndex = 0;
        currentHand = playerHands.get(currentHandIndex);

        // clear dealr hand and cards,panel
        dealerHand.Clear();
        dealerCards.clear();
        dealerCardsPanel.removeAll();

        // clear players cards jlabels
        myCards.clear();
        mySplitCards.clear();
        player1Cards.clear();
        player1SplitCards.clear();
        if (!gameOf2) {
            player2Cards.clear();
            player2SplitCards.clear();
        }

        // clear cards card
        cards.clear();
        splitCards.clear();
        //clearing booleans
        blackjack = false;

    }

    /**
     * Initialize the split cards panel for the players.
     */
    public void initSplit() {

        mySplitCards = new ArrayList<>();

        mySplitCards.add(new JLabel());
        mySplitCards.add(new JLabel());
        mySplitCards.get(1).setBounds(20, 10, 170, 190);
        mySplitCards.get(1).setName("");
        mySplitCards.get(0).setBounds(0, 10, 170, 190);
        mySplitCards.get(0).setName("");

        mySplitCardsPanel.add(mySplitCards.get(1));
        mySplitCardsPanel.add(mySplitCards.get(0));

        player1SplitCards = new ArrayList<>();

        if (!gameOf2) {
            player2SplitCards = new ArrayList<>();
        }
        mySplitX = 0;

    }

    /**
     * If current player made a split, Split the hand.
     */
    public void split() {
        mySplitCards.add(new JLabel());
        mySplitCards.get(0).setIcon(myCards.get(1).getIcon());
        mySplitCards.get(0).setBounds(0, 10, CARD_WIDTH, CARD_HEIGHT);
        splitCards.add(cards.get(1));
        myCards.get(1).setIcon(new ImageIcon());
        myX -= 20;
        mySplitX = 20;
    }

    /**
     * If it's this player turn.
     */
    public void enableHitting() {
        if (currentHand.isBlackjack() == false) {
            updateState(Utils.GameState.HITTING);
        }
    }

    /**
     * This method update the player one with the new cards.
     *
     * @param cardsToAdd the cards to add to player one panel.
     */
    public void updatePlayer1(CardsDealt cardsToAdd) {
        int i = 2;
        List<Card> theCards = cardsToAdd.getCards();
        int howManyCardsAdded = theCards.size() - 2;

        while (howManyCardsAdded > 0) {
            JLabel labToAdd = new JLabel();
            Card c = theCards.get(i);
            labToAdd.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
            player1Cards.add(labToAdd);
            labToAdd.setBounds(player1Position.x, player1Position.y, CARD_WIDTH, CARD_HEIGHT);
            player1Position.x += 20;
            howManyCardsAdded--;
            i++;
        }
        Collections.reverse(player1Cards);

        player1CardsPanel.removeAll();
        for (JLabel l : player1Cards) {
            player1CardsPanel.add(l);
        }

        player1CardsPanel.validate();
        frame.validate();
        frame.repaint();

    }

    /**
     * This method update the player one with the new cards.
     *
     * @param cardsToAdd the cards to add to player one panel.
     * @param splitCardsToAdd the split cards
     */
    public void updatePlayer1(CardsDealt cardsToAdd, CardsDealt splitCardsToAdd) { //if there is a split
        player1Position.x -= 20;
        int i = 2;
        List<Card> theCards = cardsToAdd.getCards();
        List<Card> theSplitCards = splitCardsToAdd.getCards();
        int howManyCardsAdded = theCards.size() - 2;
        int howManySplitCardsAdded = theSplitCards.size();

        while (howManyCardsAdded > 0) {
            JLabel labToAdd = new JLabel();
            Card c = theCards.get(i);
            labToAdd.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
            player1Cards.add(labToAdd);
            labToAdd.setBounds(player1Position.x, player1Position.y, CARD_WIDTH, CARD_HEIGHT);
            player1Position.x += 20;
            howManyCardsAdded--;
            i++;
        }
        Collections.reverse(player1Cards);

        player1CardsPanel.removeAll();
        for (JLabel l : player1Cards) {
            player1CardsPanel.add(l);
        }

        player1CardsPanel.validate();

        player1Position.x = 0;
        i = 0;
        while (howManySplitCardsAdded > 0) {
            JLabel labToAdd = new JLabel();
            Card c = theSplitCards.get(i);
            labToAdd.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
            player1SplitCards.add(labToAdd);
            labToAdd.setBounds(player1Position.x, 10, CARD_WIDTH, CARD_HEIGHT);
            player1Position.x += 20;
            howManySplitCardsAdded--;
            i++;
        }
        Collections.reverse(player1SplitCards);

        player1SplitCardsPanel.removeAll();
        for (JLabel l : player1SplitCards) {
            player1SplitCardsPanel.add(l);
        }

        player1SplitCardsPanel.validate();
        frame.validate();
        frame.repaint();

    }

    /**
     * This method update the player two with the new cards.
     *
     * @param cardsToAdd the cards to add to player two panel.
     */
    public void updatePlayer2(CardsDealt cardsToAdd) {
        int i = 2;
        List<Card> theCards = cardsToAdd.getCards();
        int howManyCardsAdded = theCards.size() - 2;

        while (howManyCardsAdded > 0) {
            JLabel labToAdd = new JLabel();
            Card c = theCards.get(i);
            labToAdd.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
            player2Cards.add(labToAdd);
            labToAdd.setBounds(player2Position.x, 0, CARD_WIDTH, CARD_HEIGHT);
            player2Position.x += 20;
            howManyCardsAdded--;
            i++;
        }
        Collections.reverse(player2Cards);

        player2CardsPanel.removeAll();
        for (JLabel l : player2Cards) {
            player2CardsPanel.add(l);
        }

        player2CardsPanel.validate();
        frame.validate();
        frame.repaint();

    }

    /**
     * This method update the player two with the new cards.
     *
     * @param cardsToAdd the cards to add to player two panel.
     * @param splitCardsToAdd the split cards
     */
    public void updatePlayer2(CardsDealt cardsToAdd, CardsDealt splitCardsToAdd) {
        player2Position.x -= 20;
        int i = 2;
        List<Card> theCards = cardsToAdd.getCards();
        List<Card> theSplitCards = splitCardsToAdd.getCards();
        int howManyCardsAdded = theCards.size() - 2;
        int howManySplitCardsAdded = theSplitCards.size();

        while (howManyCardsAdded > 0) {
            JLabel labToAdd = new JLabel();
            Card c = theCards.get(i);
            labToAdd.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
            player2Cards.add(labToAdd);
            labToAdd.setBounds(player2Position.x, player2Position.y, CARD_WIDTH, CARD_HEIGHT);
            player2Position.x += 20;
            howManyCardsAdded--;
            i++;
        }
        Collections.reverse(player2Cards);

        player2CardsPanel.removeAll();
        for (JLabel l : player2Cards) {
            player2CardsPanel.add(l);
        }

        player2CardsPanel.validate();

        player2Position.x = 0;
        i = 0;
        while (howManySplitCardsAdded > 0) {
            JLabel labToAdd = new JLabel();
            Card c = theSplitCards.get(i);
            labToAdd.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
            player2SplitCards.add(labToAdd);
            labToAdd.setBounds(player2Position.x, 10, CARD_WIDTH, CARD_HEIGHT);
            player2Position.x += 20;
            howManySplitCardsAdded--;
            i++;
        }
        Collections.reverse(player2SplitCards);

        player2SplitCardsPanel.removeAll();
        for (JLabel l : player2SplitCards) {
            player2SplitCardsPanel.add(l);
        }

        player2SplitCardsPanel.validate();
        frame.validate();
        frame.repaint();

    }

    /**
     * This method is called if the game ended, or player exit the game.
     */
    public void updateUser() {
        previous.finishUpdatingGame(player);
    }

    /**
     * This class handle the game state. the run method run as long as the game
     * running. listens for the server, placing bet when its this players turn,
     * updating other players card, and checking if the game still running at
     * the end of each game.
     */
    private class GameOf3Listener implements Runnable {

        boolean player1Updated = false;
        boolean player2Updated = false;

        /**
         * When it's this player turn, the game state changes to BETTING, to
         * place bet.
         */
        public void setBet() {
            try {
                //wait for my turn to place bet
                dataResponse
                        = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateState(GameState.BETTING);
        }

        /**
         * When it's this player turn, the game state change to dealing to get
         * the first two cards.
         */
        public void setCards() {
            try {
                // wait for dealing cards and seeting other players cards
                dataResponse
                        = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }
            numOfPlayers = dataResponse.getPlayerNum();
            deck = dataResponse.getDeck();
            boolean exist = false;
            if (dataResponse.getDealerCards().size() > 0) {
                dealerHand.addCard(dataResponse.getDealerCards().get(0));
                dealerHand.addCard(dataResponse.getDealerCards().get(1));
                exist = true;
            }
            updateState(dataResponse.getState());
            if (exist) {
                dealerCards.get(0).setIcon(new javax.swing.ImageIcon(getCardImage(dealerHand.getCard(0)).getImage()));
                dealerCards.get(1).setIcon(new javax.swing.ImageIcon(cardBackImage.getImage()));
            }
        }

        /**
         * After every player gets their cards, the server send a list of
         * players cards
         */
        public void setPlayersCards() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }
            numOfPlayers = dataResponse.getPlayerNum();
            deck = dataResponse.getDeck();
            List<CardsDealt> cardsDealt = dataResponse.getCardsDealt();
            int size = cardsDealt.size();
            if (size < 2) {
                setOtherCards(cardsDealt.get(0));
            }
            setOtherCards(cardsDealt);
        }

        /**
         * Update the two players cards.
         */
        public void setOtherCards(List<CardsDealt> otherCards) {
            if (numOfPlayers == 3) {
                int player1;
                int player2;
                if (otherCards.get(0).getId() == player1Id) {
                    player1 = 0;
                    player2 = 1;
                } else {
                    player1 = 1;
                    player2 = 0;
                }
                int i = 0;
                for (JLabel label : player1Cards) {
                    Card c = otherCards.get(player1).getCards().get(i);
                    label.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
                    i++;
                }

                i = 0;
                for (JLabel label : player2Cards) {
                    Card c = otherCards.get(player2).getCards().get(i);
                    label.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
                    i++;
                }
            } else { // if player disconnected 
                int i = 0;
                for (JLabel label : player1Cards) {
                    Card c = otherCards.get(0).getCards().get(i);
                    label.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
                    i++;
                }
            }
        }

        /**
         * Update only one player cards (if one player disconnect)
         */
        public void setOtherCards(CardsDealt otherCards) {
            int i = 0;
            if (otherCards.getId() == player1Id) {
                for (JLabel label : player1Cards) {
                    Card c = otherCards.getCards().get(i);
                    label.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
                    i++;
                }
            } else {
                i = 0;
                for (JLabel label : player2Cards) {
                    Card c = otherCards.getCards().get(i);
                    label.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
                    i++;
                }
            }
        }

        /**
         * Ensures the deck is the same as the server deck.
         */
        public void setDeck() {
            try {
                // make sure i have the current deck
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }
            deck = dataResponse.getDeck();
            numOfPlayers = dataResponse.getPlayerNum();
        }

        /**
         * According to the game queue, we update the players or make our move.
         */
        public void setMove() {
            long id;

            int i = 0;
            while (i < 3) {
                try {
                    // next move, hit
                    dataResponse = (GameData) myConnection.getOis().readObject();
                } catch (IOException ex) {
                    Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
                }
                deck = dataResponse.getDeck();
                id = dataResponse.getMyId(); //its not myID
                if (dataResponse.getRequestCode() == GameData.YOUR_TURN) {
                    if (blackjack) {
                        myConnection.nextMove(cards, deck);
                    } else {
                        enableHitting();
                    }
                } else if (id == player1Id) {
                    player1Updated = true;
                    if (dataResponse.isMoreCardsAdded()) {
                        if (dataResponse.isSplit()) {
                            updatePlayer1(dataResponse.getCardsDealt().get(0), dataResponse.getCardsDealt().get(1));
                        } else {
                            updatePlayer1(dataResponse.getCardsDealt().get(0));
                        }
                    }
                } else if (id == player2Id) {
                    player2Updated = true;
                    if (dataResponse.isMoreCardsAdded()) {
                        if (dataResponse.isSplit()) {
                            updatePlayer2(dataResponse.getCardsDealt().get(0), dataResponse.getCardsDealt().get(1));
                        } else {
                            updatePlayer2(dataResponse.getCardsDealt().get(0));
                        }
                    }
                }
                i++;
            }
        }

        /**
         * Go to DEALER state, reveal cards and determine winner.
         */
        public void finishGame() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateState(dataResponse.getState());
        }

        /**
         * Checking if a new game can start. if one player has disconnected, we
         * move to a 2 players game.
         */
        public void checkingGameStatus() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
                gameOn = dataResponse.isGameStart();
                changeGame = dataResponse.isChangeGame();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (changeGame) {
                clearGame();
                changeTo2PlayersGUI();
                frame.revalidate();
                frame.repaint();
            } else if (gameOn) {
                deck = dataResponse.getDeck();
            } else {
                JOptionPane.showMessageDialog(frame, "There isn't enought players in the game!\nTry again later or play vs dealer\n");
                systemMessage.setText("Game Over");
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }

        /**
         * Sets the deck and this player id.
         */
        public void check() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }
            deck = dataResponse.getDeck();
            myId = dataResponse.getMyId();
        }

        @Override
        public synchronized void run() {

            while (gameOn) {
                check();

                setBet();

                setCards();

                setPlayersCards();

                setDeck();

                setMove();

                setDeck();

                finishGame();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
                }

                checkingGameStatus();
            }

        }
    }

    /**
     * This class handle the game state. the run method run as long as the game
     * running. listens for the server, placing bet when its this players turn,
     * updating other players card, and checking if the game still running at
     * the end of each game.
     */
    private class GameOf2Listener implements Runnable {

        boolean player1Updated = false;

        /**
         * When it's this player turn, the game state changes to BETTING, to
         * place bet.
         */
        public void setBet() {
            try {
                //wait for my turn to place bet
                dataResponse
                        = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateState(GameState.BETTING);
        }

        /**
         * When it's this player turn, the game state change to dealing to get
         * the first two cards.
         */
        public void setCards() {
            try {
                // wait for dealing cards and seeting other players cards
                dataResponse
                        = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }
            numOfPlayers = dataResponse.getPlayerNum();
            deck = dataResponse.getDeck();
            boolean exist = false;
            if (dataResponse.getDealerCards().size() > 0) {
                dealerHand.addCard(dataResponse.getDealerCards().get(0));
                dealerHand.addCard(dataResponse.getDealerCards().get(1));
                exist = true;
            }
            updateState(dataResponse.getState());
            if (exist) {
                dealerCards.get(0).setIcon(new javax.swing.ImageIcon(getCardImage(dealerHand.getCard(0)).getImage()));
                dealerCards.get(1).setIcon(new javax.swing.ImageIcon(cardBackImage.getImage()));
            }
        }

        /**
         * After every player gets their cards, the server send a list of the
         * other player cards.
         */
        public void setPlayersCards() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }
            numOfPlayers = dataResponse.getPlayerNum();
            deck = dataResponse.getDeck();
            List<CardsDealt> cardsDealt = dataResponse.getCardsDealt();
            int size = cardsDealt.size();
            if (size == 1) {
                setOtherCards(cardsDealt.get(0));
            }

        }

        /**
         * Update the one player cards.
         */
        public void setOtherCards(CardsDealt otherCards) {
            int i = 0;
            if (otherCards.getId() == player1Id) {
                for (JLabel label : player1Cards) {
                    Card c = otherCards.getCards().get(i);
                    label.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
                    i++;
                }
            }
        }

        /**
         * Sets the deck and this player id.
         */
        public void setDeck() {
            try {
                // make sure i have the current deck
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }
            deck = dataResponse.getDeck();
            numOfPlayers = dataResponse.getPlayerNum();
        }

        /**
         * According to the game queue, we update the players or make our move.
         */
        public void setMove() {
            long id;

            int i = 0;
            while (i < 2) {
                try {
                    // next move, hit
                    dataResponse = (GameData) myConnection.getOis().readObject();
                } catch (IOException ex) {
                    Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
                }
                deck = dataResponse.getDeck();
                id = dataResponse.getMyId(); //its not myID
                if (dataResponse.getRequestCode() == GameData.YOUR_TURN) {
                    if (blackjack) {
                        myConnection.nextMove(cards, deck);

                    } else {
                        enableHitting();
                    }
                } else if (id == player1Id) {
                    player1Updated = true;
                    if (dataResponse.isMoreCardsAdded()) {
                        if (dataResponse.isSplit()) {
                            updatePlayer1(dataResponse.getCardsDealt().get(0), dataResponse.getCardsDealt().get(1));
                        } else {
                            updatePlayer1(dataResponse.getCardsDealt().get(0));
                        }
                    }
                }
                i++;
            }
        }

        /**
         * Go to DEALER state, reveal cards and determine winner.
         */
        public void finishGame() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateState(dataResponse.getState());
        }

        /**
         * Checking if a new game can start. if one player has disconnected, we
         * move to a 2 players game.
         */
        public void checkingGameStatus() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
                gameOn = dataResponse.isGameStart();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (gameOn) {
                deck = dataResponse.getDeck();
            } else {
                JOptionPane.showMessageDialog(frame, "There isn't enought players in the game!\nTry again later or play vs dealer\n");
                systemMessage.setText("Game Over");
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }

        /**
         * Sets the deck and this player id.
         */
        public void check() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
            }
            deck = dataResponse.getDeck();
            myId = dataResponse.getMyId();
            player1Id = dataResponse.getPlayer1Id();
            player1Name.setText(dataResponse.getPlayer1Name());
        }

        @Override
        public void run() {
            while (gameOn || changeGame) {
                check();

                setBet();

                setCards();

                setPlayersCards();

                setDeck();

                setMove();

                setDeck();

                finishGame();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GameOnline.class.getName()).log(Level.SEVERE, null, ex);
                }

                checkingGameStatus();
                changeGame = false;
            }
        }
    }
}
