package DataUtil;

import Users.User;
import blackjackServer.ConnectionThread;
import java.util.Objects;

/**
 * This class helps the server maintain the necessary details when running a
 * game. the class contains a ConnectionThread, a filed indicating if a certain
 * player has a blackjack, if the client has disconnected and who is the user
 * playing in this game/connected to server.
 *
 * @see blackjackServer.ConnectionThread
 * @author Anael
 */
public class GameThread {

    /**
     * the ConnectionThread of the client
     */
    private ConnectionThread connectionThread;
    /**
     * if the player has blackjack
     */
    private boolean blackjack;
    /**
     * if the client has disconnected from the game
     */
    private boolean disconnected;
    /**
     * the user playing the game
     */
    private User user;

    /**
     * This constructor initialize a GameThread object, and set the
     * ConnectionThread
     *
     * @param ct the connectionThread
     */
    public GameThread(ConnectionThread ct) {
        this.connectionThread = ct;
    }

    /**
     * This constructor initialize a GameThread object, set the ConnectionThread
     * and the user.
     *
     * @param ct the ConnectionThread
     * @param u the user
     */
    public GameThread(ConnectionThread ct, User u) {
        this.connectionThread = ct;
        user = u;
    }

    /**
     * Returns the ConnectionThread
     *
     * @return the ConnectionThread
     * @see blackjackServer.ConnectionThread
     */
    public ConnectionThread getConnectionThread() {
        return connectionThread;
    }

    /**
     * Sets the ConnectionThread
     *
     * @param connectionThread the ConnectionThread
     * @see blackjackServer.ConnectionThread
     */
    public void setConnectionThread(ConnectionThread connectionThread) {
        this.connectionThread = connectionThread;
    }

    /**
     * Returns if the player has a blackjack.
     *
     * @return if the player has a blackjack
     */
    public boolean isBlackjack() {
        return blackjack;
    }

    /**
     * Sets if the player has a blackjack.
     *
     * @param blackjack if the player has a blackjack
     */
    public void setBlackjack(boolean blackjack) {
        this.blackjack = blackjack;
    }

    /**
     * Returns if the client disconnected from the game.
     *
     * @return if the client disconnected from the game.
     */
    public boolean isDisconnected() {
        return disconnected;
    }

    /**
     * Sets if the client disconnected from the game.
     *
     * @param disconnected if the client disconnected from the game.
     */
    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    /**
     * Returns the user playing the game.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user playing the game.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.connectionThread);
        hash = 61 * hash + Objects.hashCode(this.user);
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameThread) {
            GameThread gameThread = (GameThread) obj;
            if (gameThread.user.equals(this.user)
                    && gameThread.connectionThread.equals(this.connectionThread)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
