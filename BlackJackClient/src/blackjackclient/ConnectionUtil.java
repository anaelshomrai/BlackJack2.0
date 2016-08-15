package blackjackclient;

import DataUtil.ConnectionData;
import DataUtil.GameData;
import Users.User;
import blackjack.Card;
import blackjack.Deck;
import blackjack.Hand;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handle the client requests to the server, when a client needs to
 * communicate with the server, he create an instance of this class create new
 * connection to the server and preform the method he needed. the method set the
 * request code according to the request then sends the data to the server. the
 * data sends to the server is a ConnectionData or a GameData. ConnectionData -
 * will be used through the application basic requests. GameData - will be used
 * through a game.
 *
 * @see DataUtil.ConnectionData
 * @see DataUtil.GameData
 * @author ANI
 */
public class ConnectionUtil {

    // the ip address of the server hosting the java server project
    //public static final String HOST_ADDRESS = "192.168.1.19";
    /**
     * the IP address of the server hosting the java server project
     */
    public static final String HOST_ADDRESS = "localhost";

    /**
     * the PORT of the server
     */
    public static final int HOST_PORT = 44444;

    /**
     * the socket
     */
    private Socket clientSocket;
    /**
     * the output stream of the socket
     */
    private OutputStream output;
    /**
     * the object output stream of the socket
     */
    private ObjectOutputStream oos;
    /**
     * the input stream of the socket
     */
    private InputStream input;
    /**
     * the object input stream of the socket
     */
    private ObjectInputStream ois;
    /**
     * the ConnectionData to pass to the server
     */
    private ConnectionData theData;
    /**
     * the GameData to pass to the server
     */
    private GameData gameData;
    /**
     * status 0 means only one object was sent. server can stop reading.
     */
    private int status = 0;
    /**
     * if more cards added to a player during a game.
     */
    private boolean cardsAdded = false;
    /**
     * if a player made a split.
     */
    private boolean split = false;
    /**
     * if a player has blackjack
     */
    private boolean blackjack = false;

    /**
     * Returns the socket.
     *
     * @return the socket
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * Sets the socket
     *
     * @param clientSocket the socket
     */
    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    /**
     * Returns the output stream.
     *
     * @return the output stream
     */
    public OutputStream getOutput() {
        return output;
    }

    /**
     * Sets the output stream.
     *
     * @param output the output stream
     */
    public void setOutput(OutputStream output) {
        this.output = output;
    }

    /**
     * Returns the object output stream.
     *
     * @return the object output stream
     */
    public ObjectOutputStream getOos() {
        return oos;
    }

    /**
     * Returns the object output stream.
     *
     * @param oos the object output stream
     */
    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    /**
     * Returns the input stream.
     *
     * @return the input stream
     */
    public InputStream getInput() {
        return input;
    }

    /**
     * Sets the input stream.
     *
     * @param input the input stream
     */
    public void setInput(InputStream input) {
        this.input = input;
    }

    /**
     * Returns the object input stream.
     *
     * @return the object input stream
     */
    public ObjectInputStream getOis() {
        return ois;
    }

    /**
     * Sets the object input stream.
     *
     * @param ois the object input stream
     */
    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    /**
     * This method sets 1 if there is still data to send, 0 if this is the last
     * object we send.
     *
     * @param status the status of the output stream
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Return if a player got more cards at his turn.
     *
     * @return if cards added after a player turn
     */
    public boolean isCardsAdded() {
        return cardsAdded;
    }

    /**
     * Sets if a player got more cards at his turn.
     *
     * @param cardsAdded if cards added after a player turn
     */
    public void setCardsAdded(boolean cardsAdded) {
        this.cardsAdded = cardsAdded;
    }

    /**
     * Returns if a player made a split.
     *
     * @return if a player made a split
     */
    public boolean isSplit() {
        return split;
    }

    /**
     * Sets if a player made a split.
     *
     * @param split if a player made a split
     */
    public void setSplit(boolean split) {
        this.split = split;
    }

    /**
     * Returns if a player has blackjack.
     *
     * @param isBlackjack if a player has blackjack
     */
    public void setBlackjack(boolean isBlackjack) {
        this.blackjack = isBlackjack;
    }

    /**
     * This method create a new connection to the server, and initialize the
     * output/input streams. the method returns true for a successful connection
     * and false if the connection or the input/output stream couldn't
     * establish.
     *
     * @return true if a connection made successfully. false - if we couldn't
     * find the server or the server is not running.
     */
    public boolean openConnection() {
        try {
            setClientSocket(new Socket(HOST_ADDRESS, HOST_PORT));
            setOutput(clientSocket.getOutputStream());
            setOos(new ObjectOutputStream(output));

            setInput(clientSocket.getInputStream());
            setOis(new ObjectInputStream(input));
        } catch (UnknownHostException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    /**
     * This method closes the connection.
     */
    public void closeConnection() {
        try {
            theData = new ConnectionData();
            theData.setStatus(0);
            theData.setRequestCode(ConnectionData.EXIT);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();

            ois.close();
            input.close();
            oos.close();
            output.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called when a new user signs up.
     *
     * @param newUser the user to add to the dataBase
     */
    public void addNewUser(User newUser) {
        try {
            theData = new ConnectionData();
            theData.setUser(newUser);
            theData.setStatus(this.status);
            theData.setRequestCode(ConnectionData.ADD_USER);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called when a user try to login.
     *
     * @param info two strings, userName and password
     */
    public void CheckIfUserExist(String[] info) {
        try {
            theData = new ConnectionData();
            theData.setInformation(info);
            theData.setStatus(this.status);
            theData.setRequestCode(ConnectionData.LOGIN);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * The method is called when a user signs up.
     *
     * @param userName the user name
     */
    public void checkIfUserNameTaken(String userName) {
        try {
            theData = new ConnectionData();
            theData.setUserNameCheck(userName);
            theData.setStatus(this.status);
            theData.setRequestCode(ConnectionData.CHECK_USER_NAME);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called when a user delete his account.
     *
     * @param user the user to remove
     */
    public void removeAccount(User user) {
        try {
            theData = new ConnectionData();
            theData.setUser(user);
            theData.setStatus(this.status);
            theData.setRequestCode(ConnectionData.REMOVE_ACCOUNT);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called when a user change his password.
     *
     * @param info two strings, the user id and the new password
     */
    public void changePassword(String[] info) {
        try {
            // [0] = id , [1] = new password
            theData = new ConnectionData();
            theData.setInformation(info);
            theData.setStatus(this.status);
            theData.setRequestCode(ConnectionData.CHANGE_PASSWORD);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called when an admin change permission of a regular user.
     *
     * @param userId the id of the user
     */
    public void changePermission(int userId) {
        try {
            theData = new ConnectionData();
            theData.setId(userId);
            //theData.setStatus(1);
            theData.setRequestCode(ConnectionData.CHANGE_PERMISSION);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This method is called when an admin open the list of user.
     */
    public void getAllUsers() {
        try {
            theData = new ConnectionData();
            theData.setRequestCode(ConnectionData.GET_USERS);
            theData.setStatus(this.status);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called when an admin try to change regular permission, or
     * to delete a user.
     *
     * @param userId the id of the user.
     */
    public void findUserById(int userId) {
        try {
            theData = new ConnectionData();
            theData.setStatus(this.status);
            theData.setId(userId);
            theData.setRequestCode(ConnectionData.GET_USER);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called in order to initialize the high score table.
     */
    public void getAllScores() {
        try {
            theData = new ConnectionData();
            theData.setRequestCode(ConnectionData.GET_SCORES);
            theData.setStatus(this.status);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called at the start of an online game. Time
     *
     * @param user the user
     */
    public void waitingForPlayers(User user) {
        try {
            this.clientSocket.setSoTimeout(300000);
            gameData = new GameData();
            gameData.setUser(user);
            gameData.setRequestCode(GameData.WAITING_FOR_PLAYERS);
            gameData.setStatus(1);
            oos.writeObject(gameData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called when the player finished placing his bet.
     *
     * @param isDone if the user finished his turn
     */
    public void setDone(boolean isDone) {
        try {
            gameData = new GameData();
            gameData.setDone(isDone);
            oos.writeObject(gameData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called after the first two cards in the game was dealt,
     * the first player also pulls the dealer cards and then all other players
     * sets these cards.
     *
     * @param cards the list of 2 cards received at the beginning of the game
     * @param deck the deck of cards after 2 cards pulled
     * @param dealerHand the cards of the dealer
     */
    public void setCardsNDeck(List<Card> cards, Deck deck, Hand dealerHand) {
        try {
            this.clientSocket.setSoTimeout(0);
            gameData = new GameData();
            gameData.setCards(cards);
            gameData.setDeck(deck);
            gameData.addDealerCard(dealerHand.getCard(0));
            gameData.addDealerCard(dealerHand.getCard(1));
            gameData.setBlackjack(blackjack);
            gameData.setRequestCode(GameData.GET_CARDS);
            oos.writeObject(gameData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        setBlackjack(false);
    }

    /**
     * This method is called in order to make sure everyone is playing with the
     * same deck.
     *
     * @param deck the deck of cards
     */
    public void setDeck(Deck deck) {
        try {
            gameData = new GameData();
            gameData.setDeck(deck);
            oos.writeObject(gameData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called when a player finished is turn. the cards list, if
     * player HIT will contain the new cards. the deck of cards will contain the
     * latest deck.
     *
     * @param cards the list of cards
     * @param deck the deck of cards
     */
    public void nextMove(List<Card> cards, Deck deck) {
        try {
            gameData = new GameData();
            gameData.setDone(true);
            gameData.setCards(cards);
            gameData.setDeck(deck);
            gameData.setMoreCardsAdded(isCardsAdded());
            oos.writeObject(gameData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        setCardsAdded(false);
    }

    /**
     * splitCards will contain list of cards after a split has made.
     *
     * @param splitCards the list of split cards
     * @param cards the list of cards
     * @param deck the deck of cards
     * @see #nextMove(java.util.List, blackjack.Deck)
     */
    public void nextMove(List<Card> splitCards, List<Card> cards, Deck deck) {
        try {
            gameData = new GameData();
            gameData.setDone(true);
            gameData.setSplit(split);
            gameData.setSplitCards(splitCards);
            gameData.setCards(cards);
            gameData.setDeck(deck);
            gameData.setMoreCardsAdded(isCardsAdded());
            oos.writeObject(gameData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        setCardsAdded(false);
    }

    /**
     * This method is called after a game, when we need to update the winnings
     * and the balance of a user.
     *
     * @param user the user to update
     */
    public void updateUser(User user) {
        try {
            theData = new ConnectionData();
            theData.setUser(user);
            theData.setRequestCode(ConnectionData.UPDATE_USER);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called if a user click Cancel in the JOptionPane of
     * waiting to people.
     *
     * @param user the user that closed the game
     */
    public void exitGame(User user) {
        try {
            gameData = new GameData();
            gameData.setStatus(0);
            gameData.setRequestCode(GameData.EXIT_GAME);
            gameData.setUser(user);
            oos.writeObject(gameData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called if a player forced out of the game because of a
     * time-out error, or if he exit the game in the middle.
     *
     * @param user the user that closed the game
     */
    public void exitGameByForce(User user) {
        try {
            theData = new ConnectionData();
            theData.setStatus(1);
            theData.setRequestCode(ConnectionData.EXIT_GAME);
            theData.setUser(user);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called if a user click OK in the JOptionPane of waiting to
     * people.
     */
    public void continueToGame() {
        try {
            gameData = new GameData();
            gameData.setRequestCode(GameData.CONTINUE);
            gameData.setStatus(0);
            oos.writeObject(gameData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
