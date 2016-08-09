package DataUtil;

import java.io.Serializable;

/**
 * This class helps organize score table in a list
 *
 * @author ANI
 */
public class Score implements Comparable, Serializable {

    /** Will store the user name */
    private String userName;

    /** Will store the user wins */
    private int wins;

    /** Will store the user balance */
    private int balance;

    /**
     * @return String of this class details
     */
    @Override
    public String toString() {
        return userName + " " + wins + " " + balance;
    }

    /** Default constructor. Initializes an empty Score object. */
    public Score() {}

    /**
     *
     * @param userName user name
     * @param wins user wins
     * @param balance user balance
     */
    public Score(String userName, int wins, int balance) {
        this.userName = userName;
        this.wins = wins;
        this.balance = balance;
    }

    /**
     * Returns the user name.
     * 
     * @return the user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the user name.
     * 
     * @param userName the user name to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Return user wins.
     * 
     * @return user wins
     */
    public int getWins() {
        return wins;
    }

    /**
     * Sets the user wins.
     * 
     * @param wins set the user wins
     */
    public void setWins(int wins) {
        this.wins = wins;
    }

    /**
     * Return the user balance.
     * 
     * @return the user balance
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Sets the user balance.
     * 
     * @param balance sets the user balance.
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * 
     * @param o object to compare
     * @return a number smaller then 0 - if this balance is greater
     * a number greater then 0 - if the o balance is greater
     * 0 - if o is not an instance of this class.
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof Score) {
            int compare = ((Score) o).getBalance();
            /* For Descending order*/
            return compare - this.getBalance();
        } else {
            return 0;
        }
    }

}
