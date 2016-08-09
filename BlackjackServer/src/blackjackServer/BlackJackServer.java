package blackjackServer;

import DataBase.DB;
import DataUtil.CardsDealt;
import DataUtil.GameData;
import DataUtil.GameThread;
import Users.User;
import blackjack.Deck;
import blackjack.Utils;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the server side application, it's responsible for creating a
 * server socket with the given port, listening for incoming connections and
 * pass them to a new thread. when a game is starting this class manages the
 * queue of the clients connected to a specific game and the game state.
 *
 * @author Anael
 */
public class BlackJackServer {

    /**
     * the server socket
     */
    private ServerSocket serverSocket;
    /**
     * is the server connected
     */
    private boolean isConnected = false;
    /**
     * an instance of the database
     */
    DB db = DB.getInstance();

    /**
     * list of all the clients connected to this server
     */
    protected Set<ConnectionThread> threads = new HashSet<>();

    /**
     * list of clients waiting for a game
     */
    protected List<GameThread> myQueue = new ArrayList<>();

    protected HandleGame handleGame = new HandleGame();

    /**
     * This constructor initialize the server with the given port,
     *
     * @param port the port of server
     */
    public BlackJackServer(int port) {
        try {
            serverSocket = new ServerSocket(port);
            db.initUsers();
        } catch (IOException ex) {
            Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("faild on listinig port " + port);
        }
        System.out.println("Server Started");
        System.out.println("Listening on port " + port);
        isConnected = true;
    }

    /**
     * This method runs while the server is connected, the server waits for
     * incoming connections, then send them to a new ConnectionThread.
     *
     * @see blackjackServer.ConnectionThread
     */
    public void handleRequests() {
        while (isConnected) { //while server is up
            while (true) {
                try {
                    Socket currentClient = serverSocket.accept();
                    System.out.println("Client has connect");
                    ConnectionThread currentConnection
                            = new ConnectionThread(currentClient, this);
                    currentConnection.start();
                    threads.add(currentConnection);
                    System.out.println("From blackjackServer threads list size is " + threads.size());
                } catch (IOException ex) {
                    Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * This method adds the client/user to the queue list.
     *
     * @param socket the client socket waiting for a game to start
     * @param user the user who holds the client socket
     */
    public void addPlayer(ConnectionThread socket, User user) {
        synchronized (this) {
            myQueue.add(new GameThread(socket, user));
            System.out.println("queue size " + myQueue.size());
            System.out.println("threads size " + threads.size());
        }

    }

    /**
     * Returns the size of players waiting for a game to start.
     *
     * @return the size of the queue.
     */
    public int getPlayesrsSize() {
        return myQueue.size();
    }

    public HandleGame getHandleGame() {
        return handleGame;
    }

    public void setHandleGame(HandleGame handleGame) {
        this.handleGame = handleGame;
    }

    void removePlayer(ConnectionThread connection, User user) {
        synchronized (this) {
            GameThread toRemove = new GameThread(connection, user);
            myQueue.remove(toRemove);
            threads.remove(connection);
            System.out.println("queue size " + myQueue.size());
            System.out.println("threads size " + threads.size());
        }
    }

    /**
     * This class handles a game when the queue reaches to 3 players. This class
     * is responsible for managing the game state, and the players in the game.
     */
    public class HandleGame {

        /**
         * a queue containing the 3 players of this game
         */
        List<GameThread> queue;
        /**
         * a game data object for sending objects to the clients
         */
        GameData responseData = new GameData();
        /**
         * a game data object for receiving objects from the clients
         */
        GameData requestData;
        /**
         * the server initialize a deck object
         */
        Deck deck = new Deck();
        /**
         * the size of the queue
         */
        int numOfPlayers;
        /**
         * number of players in the game (in cases client disconnected)
         */
        int stillInGame;
        /**
         * this game will run as long as the game is on
         */
        boolean gameOn = false;

        /**
         * This method called when an IOEception is thrown, meaning the client
         * has disconnected or closed this connection. the client is still in
         * the list, and wont be removed until the game ends, instead we mark
         * this client with a disconnected flag, and decrement the variable
         * stillInGame.
         *
         * @param c the client who disconnected from the game.
         */
        public void clientDisconnected(GameThread c) {
            System.out.println(c.getConnectionThread().getId() + c.getConnectionThread().getName() + " Has disconected");
            if (!c.isDisconnected()) {
                stillInGame--;
                c.setDisconnected(true);
            }

        }

        /**
         * This method is running as long the game is running, calling to the
         * methods according to the different states in a game.
         */
        public synchronized void startGame() {
            queue = new ArrayList<>(myQueue);
            numOfPlayers = queue.size();
            stillInGame = numOfPlayers;
            myQueue.clear();
            gameOn = true;
            // this section sets playersId and starting the game
            setPlayersIdAndDeck();

            while (gameOn) {
                setPlayersIdAndDeck();

                //setting bets
                setBets();

                //here we are dealing cards to everyone, after everybody placed there bets
                setCards();

                // make sure everyone plays with the same deck
                setDeck();

                // this section enable hit 
                setPlayersMove();

                // make sure everyone plays with the same deck
                setDeck();

                // go to dealer- finish game
                finishGame();

                checkingPlayers();
            }

        }

        /**
         * This method happens at the end of each game, to ensure there are
         * still 3 players in the game. if the game contains 3 players, the game
         * will start again. else, the game will be over.
         */
        public void checkingPlayers() {
            int count = 3;
            for (GameThread c : queue) {
                if (c.isDisconnected()) {
                    count--;
                    System.out.println("Players left in the game " + count);
                    gameOn = false;
                }
                c.setBlackjack(false);
            }
            responseData = new GameData();
            if (gameOn) {
                numOfPlayers = 3;
                stillInGame = 3;
                deck = new Deck();
                responseData.setGameStart(true);
                responseData.setDeck(deck);

            } else {
                responseData = new GameData();
                responseData.setGameStart(false);
            }
            for (GameThread c : queue) {
                if (!c.isDisconnected()) {
                    try {
                        c.getConnectionThread().getOos().writeObject(responseData);
                        c.getConnectionThread().getOos().flush();
                        c.getConnectionThread().getOos().reset();
                    } catch (IOException ex) {
                        Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                        clientDisconnected(c);
                        System.out.println("queue size " + stillInGame);
                    }
                }
            }
            GameThread first = queue.get(0);
            GameThread second = queue.get(1);
            GameThread third = queue.get(2);

            queue.set(0, second);
            queue.set(1, third);
            queue.set(2, first);
        }

        /**
         * This method sets the initial variables for starting the game.
         */
        public void setPlayersIdAndDeck() {
            responseData = new GameData();
            responseData.setDeck(deck);
            responseData.setPlayerNum(numOfPlayers);
            responseData.setGameStart(true);
            long id1 = queue.get(0).getConnectionThread().getId();
            long id2 = queue.get(1).getConnectionThread().getId();
            long id3 = queue.get(2).getConnectionThread().getId();

            String player1Name = queue.get(0).getUser().getUserName();
            String player2Name = queue.get(1).getUser().getUserName();
            String player3Name = queue.get(2).getUser().getUserName();

            for (GameThread c : queue) {
                responseData.setMyId(c.getConnectionThread().getId());
                if (c.getConnectionThread().getId() == id1) {
                    responseData.setPlayer1Id(id2);
                    responseData.setPlayer1Name(player2Name);
                    responseData.setPlayer2Id(id3);
                    responseData.setPlayer2Name(player3Name);
                } else if (c.getConnectionThread().getId() == id2) {
                    responseData.setPlayer1Id(id1);
                    responseData.setPlayer1Name(player1Name);
                    responseData.setPlayer2Id(id3);
                    responseData.setPlayer2Name(player3Name);
                } else if (c.getConnectionThread().getId() == id3) {
                    responseData.setPlayer1Id(id1);
                    responseData.setPlayer1Name(player1Name);
                    responseData.setPlayer2Id(id2);
                    responseData.setPlayer2Name(player2Name);
                }
                if (!c.isDisconnected()) {
                    try {
                        c.getConnectionThread().getOos().writeObject(responseData);
                        c.getConnectionThread().getOos().flush();
                        c.getConnectionThread().getOos().reset();

                    } catch (IOException ex) {
                        Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                        clientDisconnected(c);
                        System.out.println("queue size " + stillInGame);
                    }
                }
            }
        }

        /**
         * This method taking the bet from each player according to his place in
         * the queue.
         */
        public void setBets() {
            responseData = new GameData();
            for (GameThread c : queue) {
                try {
                    if (!c.isDisconnected()) {
                        responseData.setRequestCode(GameData.YOUR_TURN);
                        responseData.setPlayerNum(stillInGame);
                        c.getConnectionThread().getOos().writeObject(responseData);
                        c.getConnectionThread().getOos().flush();
                        c.getConnectionThread().getOos().reset();

                        requestData = (GameData) c.getConnectionThread().getOis().readObject();
                        requestData.isDone();
                    }

                } catch (IOException ex) {
                    Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                    clientDisconnected(c);
                    System.out.println("queue size " + stillInGame);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        /**
         * This method tells the clients they can go to DEALING state, then
         * receives from the clients the cards they have been dealt with, and
         * update the rest of the clients.
         */
        public void setCards() {
            List<CardsDealt> allCards = new ArrayList<>();
            responseData = new GameData();
            for (GameThread c : queue) {
                if (!c.isDisconnected()) {
                    responseData.setDeck(deck);
                    responseData.setState(Utils.GameState.DEALING);
                    try {
                        c.getConnectionThread().getOos().writeObject(responseData);
                        c.getConnectionThread().getOos().flush();
                        c.getConnectionThread().getOos().reset();

                        requestData = (GameData) c.getConnectionThread().getOis().readObject();
                        CardsDealt recived = new CardsDealt();
                        recived.setCards(requestData.getCards());
                        recived.setDeck(requestData.getDeck());
                        recived.setId(c.getConnectionThread().getId());
                        allCards.add(recived);
                        deck = requestData.getDeck();
                        if (requestData.isBlackjack()) {
                            c.setBlackjack(true);
                            stillInGame--;
                        }
                        responseData.setDeck(deck);
                        responseData.setDealerCards(requestData.getDealerCards());

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                        clientDisconnected(c);
                        System.out.println("queue size " + stillInGame);
                    }
                }

            }
            //this section sends the other players cards
            int i = 0;
            responseData = new GameData();
            List<CardsDealt> cardsToSend = new ArrayList<>();
            for (GameThread c : queue) {
                if (!c.isDisconnected()) {
                    for (CardsDealt cd : allCards) {
                        if (!(cd.getId() == c.getConnectionThread().getId())) {
                            cardsToSend.add(cd);
                        }
                    }
                    responseData.setCardsDealt(cardsToSend);
                    responseData.setPlayerNum(stillInGame);
                    try {
                        c.getConnectionThread().getOos().writeObject(responseData);
                        c.getConnectionThread().getOos().flush();
                        c.getConnectionThread().getOos().reset();
                    } catch (IOException ex) {
                        Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                        clientDisconnected(c);
                        System.out.println("queue size " + stillInGame);
                    }

                    cardsToSend.clear();
                }
            }

        }

        /**
         * This method ensure all players are playing with the same deck.
         */
        public void setDeck() {
            responseData = new GameData();
            responseData.setDeck(deck);
            responseData.setPlayerNum(stillInGame);
            for (GameThread c : queue) {
                if (!c.isDisconnected()) {
                    try {
                        c.getConnectionThread().getOos().writeObject(responseData);
                        c.getConnectionThread().getOos().flush();
                        c.getConnectionThread().getOos().reset();
                    } catch (IOException ex) {
                        Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                        clientDisconnected(c);
                        System.out.println("queue size " + stillInGame);
                    }
                }
            }

        }

        /**
         * This method tells the player he can go to HIITTING state according to
         * his place in the queue. when the player is done, he sends his current
         * state then we update all the other players in the game.
         */
        public void setPlayersMove() {
            List<CardsDealt> cardsToSend = new ArrayList<>();
            responseData = new GameData();
            CardsDealt cd = new CardsDealt();
            for (GameThread c : queue) {
                cardsToSend.clear();
                if (!c.isDisconnected()) { // if (!c.isDisconnected() && !c.isBlackjack()) {
                    responseData = new GameData();
                    responseData.setRequestCode(GameData.YOUR_TURN);
                    responseData.setPlayerNum(stillInGame);
                    responseData.setDeck(deck);
                    try {
                        c.getConnectionThread().getOos().writeObject(responseData);
                        c.getConnectionThread().getOos().flush();
                        c.getConnectionThread().getOos().reset();
                    } catch (IOException ex) {
                        Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                        clientDisconnected(c);
                        System.out.println("queue size " + stillInGame);
                    }

                    try {
                        requestData = (GameData) c.getConnectionThread().getOis().readObject();
                    } catch (IOException ex) {
                        Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                        clientDisconnected(c);
                        System.out.println("queue size " + stillInGame);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    responseData = new GameData();
                    responseData.setPlayerNum(stillInGame);
                    responseData.setMyId(c.getConnectionThread().getId());
                    if (requestData.isMoreCardsAdded()) {

                        cd.setId(c.getConnectionThread().getId());
                        cd.setDeck(requestData.getDeck());
                        cd.setCards(requestData.getCards());

                        cardsToSend.add(cd);

                        if (requestData.isSplit()) {
                            CardsDealt cdSplit = new CardsDealt();
                            cdSplit.setCards(requestData.getSplitCards());
                            cardsToSend.add(cdSplit);
                            responseData.setSplit(true);
                        }

                        responseData.setCardsDealt(cardsToSend);
                        responseData.setMoreCardsAdded(true);
                        responseData.setDeck(requestData.getDeck());
                        deck = requestData.getDeck();
                    } else {
                        responseData.setMoreCardsAdded(false);
                    }
                } else {
                    responseData = new GameData();
                    responseData.setPlayerNum(stillInGame);
                    responseData.setMoreCardsAdded(false);
                }

                for (GameThread curr : queue) {
                    if (!curr.isDisconnected()) {
                        if (curr.getConnectionThread().getId() != c.getConnectionThread().getId()) {
                            try {
                                curr.getConnectionThread().getOos().writeObject(responseData);
                                curr.getConnectionThread().getOos().flush();
                                curr.getConnectionThread().getOos().reset();
                            } catch (IOException ex) {
                                Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                                clientDisconnected(curr);
                                System.out.println("queue size " + stillInGame);
                            }
                        }
                    }
                }
            }

        }

        /**
         * After all players make their move, the dealers reveal his card and
         * hitting cards until he has at least 17.
         */
        public void finishGame() {
            responseData = new GameData();
            responseData.setState(Utils.GameState.DEALER);
            for (GameThread c : queue) {
                if (!c.isDisconnected()) {
                    responseData.setPlayerNum(stillInGame);
                    try {
                        c.getConnectionThread().getOos().writeObject(responseData);
                        c.getConnectionThread().getOos().flush();
                        c.getConnectionThread().getOos().reset();
                    } catch (IOException ex) {
                        Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                        clientDisconnected(c);
                        System.out.println("queue size " + stillInGame);
                    }
                }
            }
        }

        public void startGameOf2() {
            queue = new ArrayList<>(myQueue);
            numOfPlayers = queue.size();
            stillInGame = numOfPlayers;
            myQueue.clear();
            gameOn = true;
            // this section sets playersId and starting the game
            setPlayersIdAndDeck2();

            while (gameOn) {
                setPlayersIdAndDeck2();

                //setting bets
                setBets();

                //here we are dealing cards to everyone, after everybody placed there bets
                setCards();

                // make sure everyone plays with the same deck
                setDeck();

                // this section enable hit 
                setPlayersMove();

                // make sure everyone plays with the same deck
                setDeck();

                // go to dealer- finish game
                finishGame();

                checkingPlayers2();
            }
        }

        private void setPlayersIdAndDeck2() {
            responseData = new GameData();
            responseData.setDeck(deck);
            responseData.setPlayerNum(numOfPlayers);
            responseData.setGameStart(true);
            long id1 = queue.get(0).getConnectionThread().getId();
            long id2 = queue.get(1).getConnectionThread().getId();

            String player1Name = queue.get(0).getUser().getUserName();
            String player2Name = queue.get(1).getUser().getUserName();

            for (GameThread c : queue) {
                responseData.setMyId(c.getConnectionThread().getId());
                if (c.getConnectionThread().getId() == id1) {
                    responseData.setPlayer1Id(id2);
                    responseData.setPlayer1Name(player2Name);
                } else if (c.getConnectionThread().getId() == id2) {
                    responseData.setPlayer1Id(id1);
                    responseData.setPlayer1Name(player1Name);
                }
                if (!c.isDisconnected()) {
                    try {
                        c.getConnectionThread().getOos().writeObject(responseData);
                        c.getConnectionThread().getOos().flush();
                        c.getConnectionThread().getOos().reset();

                    } catch (IOException ex) {
                        Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                        clientDisconnected(c);
                        System.out.println("queue size " + stillInGame);
                    }
                }
            }
        }

        public void checkingPlayers2() {
            int count = 2;
            for (GameThread c : queue) {
                if (c.isDisconnected()) {
                    count--;
                    System.out.println("Players left in the game " + count);
                    gameOn = false;
                }
                c.setBlackjack(false);
            }
            responseData = new GameData();
            if (gameOn) {
                numOfPlayers = 2;
                stillInGame = 2;
                deck = new Deck();
                responseData.setGameStart(true);
                responseData.setDeck(deck);

            } else {
                responseData = new GameData();
                responseData.setGameStart(false);
            }
            for (GameThread c : queue) {
                if (!c.isDisconnected()) {
                    try {
                        c.getConnectionThread().getOos().writeObject(responseData);
                        c.getConnectionThread().getOos().flush();
                        c.getConnectionThread().getOos().reset();
                    } catch (IOException ex) {
                        Logger.getLogger(BlackJackServer.class.getName()).log(Level.SEVERE, null, ex);
                        clientDisconnected(c);
                        System.out.println("queue size " + stillInGame);
                    }
                }
            }
            GameThread first = queue.get(0);
            GameThread second = queue.get(1);

            queue.set(0, second);
            queue.set(1, first);
        }
    }

}
