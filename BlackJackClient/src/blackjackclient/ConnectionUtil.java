/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Anael
 */
public class ConnectionUtil {

    // the ip address of the server hosting the java server project
    //public static final String HOST_ADDRESS = "192.168.1.19";
    public static final String HOST_ADDRESS = "localhost";
    public static final int HOST_PORT = 44444;

    private Socket clientSocket;
    private OutputStream output;
    private ObjectOutputStream oos;
    private InputStream input;
    private ObjectInputStream ois;
    private ConnectionData theData;
    private GameData gameData;
    private int status = 0;
    private boolean cardsAdded = false;
    private boolean split = false;
    private boolean blackjack = false;

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public OutputStream getOutput() {
        return output;
    }

    public void setOutput(OutputStream output) {
        this.output = output;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    public InputStream getInput() {
        return input;
    }

    public void setInput(InputStream input) {
        this.input = input;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    public boolean openConnection() {
        try {
            setClientSocket(new Socket(HOST_ADDRESS, HOST_PORT));
            setOutput(clientSocket.getOutputStream());
            setOos(new ObjectOutputStream(output));

            setInput(clientSocket.getInputStream());
            setOis(new ObjectInputStream(input));
            theData = null;
            theData = new ConnectionData();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public void closeConnection() {
        try {

            theData.setStatus(0);
//            theData.setRequestCode(ConnectionData.EXIT);
//            oos.writeObject(theData);
//            oos.flush();
//            oos.reset();

            ois.close();
            input.close();
            oos.close();
            output.close();
            clientSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addNewUser(User newUser) {
        try {
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

    public void CheckIfUserExist(String[] user) {
        try {
            theData.setInformation(user);
            theData.setStatus(this.status);
            theData.setRequestCode(ConnectionData.LOGIN);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void checkIfUserNameTaken(String userName) {
        try {
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

    public void removeAccount(User user) {
        try {
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

    public void changePassword(String[] info) {
        try {
            // [0] = id , [1] = new password
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

    public void changePermission(int userId) {
        try {
            theData.setId(userId);
            theData.setStatus(this.status);
            theData.setRequestCode(ConnectionData.CHANGE_PERMISSION);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getAllUsers() {
        try {
            theData.setRequestCode(ConnectionData.GET_USERS);
            theData.setStatus(this.status);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void findUserById(int userId) {
        try {
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

    public void getAllScores() {
        try {
            theData.setRequestCode(ConnectionData.GET_SCORES);
            theData.setStatus(this.status);
            oos.writeObject(theData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void waitingForPlayers(User user) {
        try {
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

    public void setStatus(int status) {
        this.status = status;
    }

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

    public void setCardsNDeck(List<Card> cards, Deck deck, Hand dealerHand) {
        try {
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

    public void setDeck(Deck deck) {
        try {
            gameData = new GameData();
            gameData.setDeck(deck);
            gameData.setRequestCode(GameData.DONE);
            oos.writeObject(gameData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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

    public boolean isCardsAdded() {
        return cardsAdded;
    }

    public void setCardsAdded(boolean cardsAdded) {
        this.cardsAdded = cardsAdded;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }

    public void setBlackjack(boolean isBlackjack) {
        this.blackjack = isBlackjack;
    }

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

    public void enterGame(User user) {
        try {
            gameData = new GameData();
            gameData.setUser(user);
            gameData.setRequestCode(GameData.ENTER_GAME);
            oos.writeObject(gameData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void exitGame(User user) {
        try {
            gameData = new GameData();
            gameData.setRequestCode(GameData.EXIT_GAME);
            gameData.setUser(user);
            oos.writeObject(gameData);
            oos.flush();
            oos.reset();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
