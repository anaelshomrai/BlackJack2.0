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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.Serializable;
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
 *
 * @author Anael
 */
public class TheGame extends JFrame implements ActionListener, Serializable {

    private static final String BET = "BET";
    private static final String HIT = "HIT";
    private static final String STAY = "STAY";
    private static final String DOUBLE_DOWN = "DOUBLE_DOWN";
    private static final String SPLIT = "SPLIT";
    private static final String SOUND = "SOUND";

    User player;
    UserHome previous;

    JFrame frame = new JFrame("Blackjack");
    List<JLabel> player1Cards = new ArrayList<>();
    List<JLabel> player2Cards = new ArrayList<>();
    List<JLabel> myCards = new LinkedList<>();
    List<JLabel> mySplitCards;
    List<JLabel> player1SplitCards;
    List<JLabel> player2SplitCards;

    JPanel mySplitCardsPanel;
    JPanel myCardsPanel;
    JPanel player1CardsPanel;
    JPanel player2CardsPanel;
    JPanel player1SplitCardsPanel;
    JPanel player2SplitCardsPanel;
    List<JLabel> dealrCards = new ArrayList<>();
    JPanel statusPanel;
    JPanel actionPanel;
    JPanel betPanel;
    JButton betButton;
    JButton hitButton;
    JButton stayButton;
    JButton doubleDownButton;
    JButton sound;
    JButton splitButton;
    JTextField betAmountTextField;
    JLabel cashLabel;
    JLabel winsLabel;
    JLabel totalCardsLabel;

    JLabel myName;
    JLabel player1Name;
    JLabel player2Name;

    private JLabel systemMessage;
    JPanel infoPanel;

    JPanel dealerCardsPanel;

    private Utils.GameState state = Utils.GameState.BETTING;
    private Utils.GameState nextState;
    private Hand dealerHand = new Hand();
    private Hand currentHand;
    private int currentHandIndex = 0;
    private List<Hand> playerHands = new ArrayList<>();
    String language = "";

    private static ImageIcon cardBackImage = new ImageIcon("./src/img/cards/back.png");

    private Deck deck;
    private int playerCash = 10000;
    private static int MIN_BET = 5;
    private static int MAX_BET = 500;
    private int playerWins = 0;
    private int playerId = 0;
    private boolean finished = false;

    private static int dealerX;
    private static int dealerY;
    private static int myX;
    private int mySplitX;
    private static int myY;
    private static Point player1Position;
    private static Point player2Position;

    private static int width = 170;
    private static int height = 190;
    private ConnectionUtil myConnection;
    private GameData dataResponse;
    private CardsDealt cardsDealt = new CardsDealt();
    private List<Card> cards = new ArrayList<>();
    private List<Card> splitCards = new ArrayList<>();

    private boolean playSounds;
    public boolean gameOn = false;
    private boolean blackjack = false;
    private int numOfPlayers;
    private long myId;
    private long player1Id;
    private long player2Id;
    boolean gameOf2 = false;
    boolean changeGame = false;
    BoardPanel boardPanel;
    int option = 99;
    private Thread winningPopup;

    JLabel timeLabel;
    int time;
    Timer timer;
    String stage = "";

    public void initTimer() {
        timeLabel = new JLabel();
        timer = new Timer(1000, this);
        timer.setActionCommand("TIMER");
        Font timeFont = new Font("Courier", Font.BOLD, 12);
        timeLabel.setFont(timeFont);
    }

    public void startTimer() {
        time = 20;
        timer.start();
    }

    public void restart() {
        timer = new Timer(1000, this);
        timer.setActionCommand("TIMER");
        time = 20;
        timer.start();
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    //private int numOfPlayers = 1;
    public TheGame() {
        initFrame();
    }

    public static void main(String[] args) {
        new TheGame().initializeGUI();
    }

    //The one
    public TheGame(User player, UserHome previous) {
        this.previous = previous;
        this.player = player;
        this.playerId = player.getId();
        this.playerCash = player.getBalance();
        this.playerWins = player.getWins();
        this.myName = new JLabel(player.getUserName());

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
                option = new MyJOptionPane(this).getAnswer();
                if (option == JOptionPane.OK_OPTION) {
                    myConnection.continueToGame();
                } else if (option == JOptionPane.CANCEL_OPTION) {
                    myConnection.exitGame(player);
                    myConnection.closeConnection();
                    previous.setVisible(true);
                    setVisible(false);
                    dispose();
                }
            } else {
                myConnection.continueToGame();
            }

//            if (!isStarted) {
            dataResponse
                    = (GameData) myConnection.getOis().readObject();
            numOfPlayers = dataResponse.getPlayerNum();
            isStarted = dataResponse.isGameStart();
//            }
            gameOn = true;
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

        } catch (IOException ex) {
            Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public TheGame(User player, UserHome previous, String language) {
        this(player, previous);
        this.language = language;
    }

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

    public void waitForYourTurn() {
        Thread thread = new Thread(new Listener());
        thread.start();
    }

    public void waitForYourTurn2() {
        Thread thread = new Thread(new Listener2());
        thread.start();
    }

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

        //deal cards -> server call
//        dealCards();
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

        //here the game really start
        // updateState(TheGame.GameState.BETTING);
        //frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                player.setWins(playerWins);
                player.setBalance(playerCash);
                myConnection.closeConnection();
                previous.setVisible(true);
                updateUser();
                frame.dispose();
            }
        });

        if (this.language.equals("iw")) {
            changeToHebrew();
        }
    }

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

        //deal cards -> server call
//        dealCards();
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
        myName.setForeground(new java.awt.Color(0, 0, 0));

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
        // updateState(TheGame.GameState.BETTING);
        //frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                player.setWins(playerWins);
                player.setBalance(playerCash);
                myConnection.closeConnection();
                previous.setVisible(true);
                updateUser();
                frame.dispose();
            }
        });

        if (this.language.equals("iw")) {
            changeToHebrew();
        }
    }

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

    public void initInfoPanel() {
        infoPanel = new JPanel();
        systemMessage = new JLabel("");
        initTimer();

        systemMessage.setVerticalAlignment(CENTER);
        infoPanel.setLayout(new java.awt.GridBagLayout());
        infoPanel.setOpaque(true);

        infoPanel.add(systemMessage);
        infoPanel.add(timeLabel);

        frame.add(infoPanel);
        infoPanel.setBounds(0, 0, 800, 20);
    }

    public void initStatusPanel() {
        statusPanel = new JPanel();
        cashLabel = new JLabel();
        winsLabel = new JLabel();
        totalCardsLabel = new JLabel();

        statusPanel.setLayout(new FlowLayout());
        statusPanel.setOpaque(true);

        cashLabel.setText("Cash :" + playerCash);
        statusPanel.add(cashLabel);

        winsLabel.setText("Wins:" + playerWins);
        statusPanel.add(winsLabel);

//        totalCardsLabel.setText("Total: ");
//        statusPanel.add(totalCardsLabel);
        frame.add(statusPanel);
        statusPanel.setBounds(590, 670, 200, 30);
    }

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

        frame.add(actionPanel);
        actionPanel.setBounds(130, 670, 450, 30);
    }

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
        betAmountTextField.setActionCommand("BET_AMOUNT");
        betPanel.add(betAmountTextField);

        betButton.setText("Bet");
        betPanel.add(betButton);

        frame.add(betPanel);
        betPanel.setBounds(1, 670, 120, 30);
    }

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

    public void initPlayersJLabels() {
        myCards.add(new JLabel());
        myCards.add(new JLabel());
        myCards.get(1).setBounds(20, 0, 170, 190);
        myCards.get(1).setName("");
        myCards.get(0).setBounds(0, 0, 170, 190);
        myCards.get(0).setName("");

        myCardsPanel.add(myCards.get(1));
        myCardsPanel.add(myCards.get(0));

        myX = 40;
        myY = 0;

        player1Cards.add(new JLabel());
        player1Cards.add(new JLabel());
        player1Cards.get(1).setBounds(20, 0, 170, 190);
        player1Cards.get(1).setName("");
        player1Cards.get(0).setBounds(0, 0, 170, 190);
        player1Cards.get(0).setName("");

        player1CardsPanel.add(player1Cards.get(1));
        player1CardsPanel.add(player1Cards.get(0));

        if (!gameOf2) {
            player2Cards.add(new JLabel());
            player2Cards.add(new JLabel());
            player2Cards.get(1).setBounds(20, 0, 170, 190);
            player2Cards.get(1).setName("");
            player2Cards.get(0).setBounds(0, 0, 170, 190);
            player2Cards.get(0).setName("");

            player2CardsPanel.add(player2Cards.get(1));
            player2CardsPanel.add(player2Cards.get(0));
            player2Position = new Point(40, 0);

        }
        player1Position = new Point(40, 0);
    }

    public void initDealer() {
        dealerCardsPanel = new JPanel();
        dealerCardsPanel.setLayout(null);

        initDealerJLabels();

        frame.add(dealerCardsPanel);
        dealerCardsPanel.setBounds(290, 30, 260, 290);
        dealerCardsPanel.setOpaque(false);

    }

    public void initDealerJLabels() {

        dealrCards.add(new JLabel());
        dealrCards.add(new JLabel());

        dealerCardsPanel.add(dealrCards.get(1));
        dealrCards.get(1).setBounds(20, 10, width, height); //back
        dealerCardsPanel.add(dealrCards.get(0));
        dealrCards.get(0).setBounds(0, 10, width, height); //exposeF
        dealerX = 40;
        dealerY = 10;
    }

    public void dealCards() {

        initDealerJLabels();
        initPlayersJLabels();
        initSplit();

        int counter = 0;
        for (int i = 0; i < 2; i++) {
            Card c = dealCard(myCards.get(counter));
            playerHands.get(currentHandIndex).AddCard(c);
            cards.add(c);
            counter++;
        }

        if (dealerHand.getSize() == 0) {
            dealerHand.AddCard(dealCard(dealrCards.get(0)));
            dealerHand.AddCard(dealCard(dealrCards.get(1), false));
        }

        currentHand = playerHands.get(currentHandIndex);

    }

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

    private static ImageIcon getCardImage(Card card) {
        return Utils.cardImages.get(card.getSuit())[card.getRank().ordinal()];
    }

    private Card dealCard(JLabel card) {
        return dealCard(card, true);
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
                dealrCards.get(1).setIcon(new javax.swing.ImageIcon(backCard.getImage()));

                if (dealrCards.size() == 2) {
                    Collections.reverse(dealrCards);
                }

                // Draw until 17+.
                while (dealerHand.getValue() < 17) {
                    JLabel lab = new JLabel();
                    dealerHand.AddCard(dealCard(lab));
                    Collections.reverse(dealrCards);
                    dealrCards.add(lab);
                    lab.setBounds(dealerX, dealerY, width, height);
                    dealerX += 20;
                    Collections.reverse(dealrCards);
                    dealerCardsPanel.add(lab);

                }
                dealerCardsPanel.removeAll();
                for (JLabel l : dealrCards) {
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
                int total = currentHand.getValue();

                if (this.language.equals("iw")) {
                    totalCardsLabel.setText(String.valueOf(total));
                } else {
                    totalCardsLabel.setText(String.valueOf(total));
                }

                if (currentHand.isBlackjack() == true) {
                    updateState(Utils.GameState.RESOLVE);
                    blackjack = true;
//                    myConnection.setBlackjack(blackjack);
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
                    dealrCards.get(1).setIcon(new javax.swing.ImageIcon(backCard1.getImage()));
                }

                dispenseWinnings();

                // Start Over
                //updateState(TheGame.GameState.BETTING);
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

    private void dispenseWinnings() {
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
     * @param nextState
     */
    public void updateState(Utils.GameState nextState) {
        this.nextState = nextState;
        play();
    }

    //Action Events
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals(BET)) {
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
                    clearGame();

                    stage = "OK";
                    nextTurn();
                    betAmountTextField.setEnabled(false);
                    playerCash -= betAmount;
                    currentHand.setBet(betAmount);

                    betButton.setEnabled(false);
                    setFinished(true);
                    myConnection.setDone(true);
                }

            } catch (NumberFormatException e) {
                if (this.language.equals("iw")) {
                    systemMessage.setText("ההימור חייב להיות מספר...");
                } else {
                    systemMessage.setText("Bet must be an integer value...");
                }

            }
        } else if (ae.getActionCommand().equals(HIT)) {
            //
            // Deal one card to current hand, check for bust
            // or blackjack, if not, allow the player to keep hitting
            // otherwise, go to DEALER state or next hand.
            //
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
                playerHands.get(currentHandIndex).AddCard(c);
                cards.add(c);
                Collections.reverse(myCards);
                myCards.add(labToAdd);
                labToAdd.setBounds(myX, myY, width, height);
                myX += 20;
                Collections.reverse(myCards);

                myCardsPanel.removeAll();
                for (JLabel l : myCards) {
                    myCardsPanel.add(l);
                }

                currentHand = playerHands.get(currentHandIndex);
                int total = currentHand.getValue();
                if (this.language.equals("iw")) {
                    totalCardsLabel.setText(String.valueOf(total));
                } else {
                    totalCardsLabel.setText(String.valueOf(total));
                }

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
                playerHands.get(currentHandIndex).AddCard(c);
                splitCards.add(c);
                Collections.reverse(mySplitCards);
                mySplitCards.add(labToAdd);
                labToAdd.setBounds(mySplitX, 10, width, height);
                mySplitX += 20;
                Collections.reverse(mySplitCards);

                mySplitCardsPanel.removeAll();
                for (JLabel l : mySplitCards) {
                    mySplitCardsPanel.add(l);
                }
                currentHand = playerHands.get(currentHandIndex);
                int total = currentHand.getValue();

                if (this.language.equals("iw")) {
                    totalCardsLabel.setText(String.valueOf(total));
                } else {
                    totalCardsLabel.setText(String.valueOf(total));
                }

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

        } else if (ae.getActionCommand().equals(STAY)) {
            // Go to DEALER_AI state or next hand.
            //

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

        } else if (ae.getActionCommand().equals(DOUBLE_DOWN)) {
//
            // Deduct the hand's bet amount from player's available cash,
            // deal one card to the current hand and go to the DEALER_AI
            // state or next hand.
            //
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
                playerHands.get(currentHandIndex).AddCard(c);
                cards.add(c);
                Collections.reverse(myCards);
                myCards.add(labToAdd);
                labToAdd.setBounds(myX, myY, width, height);
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
                playerHands.get(currentHandIndex).AddCard(c);
                splitCards.add(c);
                Collections.reverse(mySplitCards);
                mySplitCards.add(labToAdd);
                labToAdd.setBounds(mySplitX, 10, width, height);
                mySplitX += 20;
                Collections.reverse(mySplitCards);

                mySplitCardsPanel.removeAll();
                for (JLabel l : mySplitCards) {
                    mySplitCardsPanel.add(l);
                }
                currentHand = playerHands.get(currentHandIndex);
                int total = currentHand.getValue();

                if (this.language.equals("iw")) {
                    totalCardsLabel.setText(String.valueOf(total));
                } else {
                    totalCardsLabel.setText(String.valueOf(total));
                }

                myCardsPanel.revalidate();
                frame.validate();
                frame.repaint();

            }

            currentHand = playerHands.get(currentHandIndex);
            int total = currentHand.getValue();

            if (this.language.equals("iw")) {
                totalCardsLabel.setText(String.valueOf(total));
            } else {
                totalCardsLabel.setText(String.valueOf(total));
            }

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
        } else if (ae.getActionCommand().equals(SPLIT)) {
            // Split hand, deduct the new hand's bet
            // amount from the players cash and push
            // into the playerHands list.
            //
            time += 10;
            Hand newHand = currentHand.split();
            //after spliting hand, i got 1 card in current and one in the new
            playerCash -= currentHand.getBet(); // bets are doubled
            playerHands.add(newHand);
            myConnection.setSplit(true);
            split();
        } else if (ae.getActionCommand().equals(SOUND)) {
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
        } else if (ae.getActionCommand().equals("TIMER")) {
            if (time == 0) {
                timeLabel.setText(" Time Left : " + 0);
                timer.stop();
                nextTurn();
            } else {
                time--;
                timeLabel.setText(" Time Left : " + time);
                frame.repaint();
            }
        } else if (ae.getActionCommand().equals("BET_AMOUNT")) {
            betAmountTextField.setText("");
        }

    }

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
        dealrCards.clear();
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

    public void split() {
        mySplitCards.add(new JLabel());
        mySplitCards.get(0).setIcon(myCards.get(1).getIcon());
        mySplitCards.get(0).setBounds(0, 10, width, height);
        splitCards.add(cards.get(1));
        myCards.get(1).setIcon(new ImageIcon());
        myX -= 20;
        mySplitX = 20;
    }

    public void enableHitting() {
        if (currentHand.isBlackjack() == false) {
            updateState(Utils.GameState.HITTING);
        }
    }

    public void updatePlayer1(CardsDealt cardsToAdd) {
        int i = 2;
        List<Card> theCards = cardsToAdd.getCards();
        int howManyCardsAdded = theCards.size() - 2;

        while (howManyCardsAdded > 0) {
            JLabel labToAdd = new JLabel();
            Card c = theCards.get(i);
            labToAdd.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
            player1Cards.add(labToAdd);
            labToAdd.setBounds(player1Position.x, player1Position.y, width, height);
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
            labToAdd.setBounds(player1Position.x, player1Position.y, width, height);
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
            labToAdd.setBounds(player1Position.x, 10, width, height);
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

    public void updatePlayer2(CardsDealt cardsToAdd) {
        int i = 2;
        List<Card> theCards = cardsToAdd.getCards();
        int howManyCardsAdded = theCards.size() - 2;

        while (howManyCardsAdded > 0) {
            JLabel labToAdd = new JLabel();
            Card c = theCards.get(i);
            labToAdd.setIcon(new javax.swing.ImageIcon(getCardImage(c).getImage()));
            player2Cards.add(labToAdd);
            labToAdd.setBounds(player2Position.x, 0, width, height);
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
            labToAdd.setBounds(player2Position.x, player2Position.y, width, height);
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
            labToAdd.setBounds(player2Position.x, 10, width, height);
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

    public void updateUser() {
        previous.finishUpdatingGame(player);
    }

    private class Listener implements Runnable {

        boolean player1Updated = false;
        boolean player2Updated = false;

        public void setBet() {
            try {
                //wait for my turn to place bet
                dataResponse
                        = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateState(GameState.BETTING);
        }

        public void setCards() {
            try {
                // wait for dealing cards and seeting other players cards
                dataResponse
                        = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            numOfPlayers = dataResponse.getPlayerNum();
            deck = dataResponse.getDeck();
            boolean exist = false;
            if (dataResponse.getDealerCards().size() > 0) {
                dealerHand.AddCard(dataResponse.getDealerCards().get(0));
                dealerHand.AddCard(dataResponse.getDealerCards().get(1));
                exist = true;
            }
            updateState(dataResponse.getState());
            if (exist) {
                dealrCards.get(0).setIcon(new javax.swing.ImageIcon(getCardImage(dealerHand.getCard(0)).getImage()));
                dealrCards.get(1).setIcon(new javax.swing.ImageIcon(cardBackImage.getImage()));
            }
        }

        public void setPlayersCards() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
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

        public void setDeck() {
            try {
                // make sure i have the current deck
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            deck = dataResponse.getDeck();
            numOfPlayers = dataResponse.getPlayerNum();
        }

        public void setMove() {
            long id;

            //int players = numOfPlayers;
            int i = 0;
            while (i < 3) {
                try {
                    // next move, hit
                    dataResponse = (GameData) myConnection.getOis().readObject();
                } catch (IOException ex) {
                    Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                //players = dataResponse.getPlayerNum();
                deck = dataResponse.getDeck();
                id = dataResponse.getMyId(); //its not myID
                if (dataResponse.getRequestCode() == GameData.YOUR_TURN) {
                    if (blackjack) {
                        myConnection.nextMove(cards, deck);
                        //click(stayButton, 1);

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

        public void finishGame() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateState(dataResponse.getState());
        }

        public void checkingGameStatus() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
                gameOn = dataResponse.isGameStart();
                changeGame = dataResponse.isChangeGame();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (changeGame) {
                clearGame();
                changeTo2PlayersGUI();
                frame.revalidate();
                frame.repaint();
            } else if (gameOn) {
                deck = dataResponse.getDeck();
                //game on = true            }
            } else {
                JOptionPane.showMessageDialog(frame, "There isn't enought players in the game!\nTry again later or play vs dealer\n");
                systemMessage.setText("Game Over");
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }

        public void check() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            deck = dataResponse.getDeck();
            myId = dataResponse.getMyId();
            // player1Id = dataResponse.getPlayer1Id();
            //player2Id = dataResponse.getPlayer2Id();
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
                    Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
                }

                checkingGameStatus();
            }

        }
    }

    private class Listener2 implements Runnable {

        boolean player1Updated = false;

        public void setBet() {
            try {
                //wait for my turn to place bet
                dataResponse
                        = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateState(GameState.BETTING);
        }

        public void setCards() {
            try {
                // wait for dealing cards and seeting other players cards
                dataResponse
                        = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            numOfPlayers = dataResponse.getPlayerNum();
            deck = dataResponse.getDeck();
            boolean exist = false;
            if (dataResponse.getDealerCards().size() > 0) {
                dealerHand.AddCard(dataResponse.getDealerCards().get(0));
                dealerHand.AddCard(dataResponse.getDealerCards().get(1));
                exist = true;
            }
            updateState(dataResponse.getState());
            if (exist) {
                dealrCards.get(0).setIcon(new javax.swing.ImageIcon(getCardImage(dealerHand.getCard(0)).getImage()));
                dealrCards.get(1).setIcon(new javax.swing.ImageIcon(cardBackImage.getImage()));
            }
        }

        public void setPlayersCards() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            numOfPlayers = dataResponse.getPlayerNum();
            deck = dataResponse.getDeck();
            List<CardsDealt> cardsDealt = dataResponse.getCardsDealt();
            int size = cardsDealt.size();
            if (size == 1) {
                setOtherCards(cardsDealt.get(0));
            }

        }

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

        public void setDeck() {
            try {
                // make sure i have the current deck
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            deck = dataResponse.getDeck();
            numOfPlayers = dataResponse.getPlayerNum();
        }

        public void setMove() {
            long id;

            //int players = numOfPlayers;
            int i = 0;
            while (i < 2) {
                try {
                    // next move, hit
                    dataResponse = (GameData) myConnection.getOis().readObject();
                } catch (IOException ex) {
                    Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                //players = dataResponse.getPlayerNum();
                deck = dataResponse.getDeck();
                id = dataResponse.getMyId(); //its not myID
                if (dataResponse.getRequestCode() == GameData.YOUR_TURN) {
                    if (blackjack) {
                        myConnection.nextMove(cards, deck);
                        //click(stayButton, 1);

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

        public void finishGame() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateState(dataResponse.getState());
        }

        public void checkingGameStatus() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
                gameOn = dataResponse.isGameStart();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (gameOn) {
                deck = dataResponse.getDeck();
                //game on = true            }
            } else {
                JOptionPane.showMessageDialog(frame, "There isn't enought players in the game!\nTry again later or play vs dealer\n");
                systemMessage.setText("Game Over");
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }

        public void check() {
            try {
                dataResponse = (GameData) myConnection.getOis().readObject();
            } catch (IOException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(TheGame.class.getName()).log(Level.SEVERE, null, ex);
                }

                checkingGameStatus();
                changeGame = false;
            }
        }
    }

}
