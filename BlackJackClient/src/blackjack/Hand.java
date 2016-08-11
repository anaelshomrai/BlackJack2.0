package blackjack;

import blackjack.Utils.RANK;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a hand of cards in the game. Using this class we can add
 * cards, get all cards in hand, get the value of all cards, checking if a split
 * is possible, if this hand has blackjack or if the hand is bust (cards value
 * got over 21), and who is the winner. although we can have up to 3 players the
 * only rival is the dealer.
 *
 * @author ANI
 */
public class Hand implements Serializable {

    /**
     * the hand containing the cards
     */
    private List<Card> hand = new ArrayList<>();
    /**
     * the bet entered by the user
     */
    private int bet;

    /**
     * This method add a card to the end of the list of cards.
     * 
     * @param card the card to add to the list of cards.
     */
    public void addCard(Card card) {
        hand.add(card);
    }

    /**
     * This method iterating through the hand and summing the cards value.
     * then, iterating through the hand again to check if we ace an ace in the
     * hand - if we do, and we over 21 points (Bust), then the ace equals 1
     * instead of 11.
     * 
     * @return the value of the list cards.
     */
    public int getValue() {
        int value = 0;
        for (Card c : hand) {
            value += c.getValue();
        }

        for (Card c : hand) {
            if ((c.getRank() == RANK.ACE) && value > 21) {
                value -= 10;
            }
        }
        return value;
    }

    /**
     * This method returns the card in the given index in the list of cards.
     * 
     * @param index the index of the card
     * @return the card in the given index
     */
    public Card getCard(int index) {
        return hand.get(index);
    }

    /**
     * This method returns a string representing this class.
     * 
     * @return a string representing this class.
     */
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < hand.size(); i++) {
            s += hand.get(i);
            if (i != hand.size() - 1) //it's not the last card
            {
                s += ", ";
            }
        }
        s += " (" + getValue() + ")";
        return s;
    }

    /**
     * Returns the bet entered by the user.
     *  
     * @return the bet
     */
    public int getBet() {
        return bet;
    }

    /**
     * Sets the bet entered by the user.
     * 
     * @param bet the bet
     */
    public void setBet(int bet) {
        this.bet = bet;
    }

    /**
     * Returns the list of cards in this hand.
     * 
     * @return the list of cards.
     */
    public List<Card> getCards() {
        return this.hand;
    }

    /**
     * Returns if the value of the hand is over 21, or below.
     * 
     * @return if the cards are bust
     */
    public boolean isBust() {
        return (getValue() > 21);
    }

    /**
     * Returns if this hand has blackjack.
     * BlackJack is when you get straight away 2 cards that equals to 21 points.
     * 
     * @return if this hand has blackjack
     */
    public boolean isBlackjack() {
        return (getValue() == 21) && (hand.size() == 2);
    }

    /**
     * Return if the hand can be split. true - if we have 2 card with the same
     * value.
     * 
     * @return if the hand can be split.
     */
    public boolean isSplit() {
        if (hand.size() != 2) {
            return false;
        } else if ((hand.get(0).getRank() == hand.get(1).getRank())) {
            return true;
        }

        return false;
    }

    /**
     * Returns a number representing the winner.
     * 0 - if my hand is bust (over 21 points). Depends, if the dealer bust too.
     * 1 - if the dealer hand is bust. Win
     * -1 - if my hand and the dealer hand have the same value. Push
     * 
     * @param other the dealer hand.
     * @return number representing the winner.
     */
    public int getWinner(Hand other) {
        // seraching for blackjack
        if (isBlackjack() && !other.isBlackjack()) {
            return 1;
        }

        // checking if anyone is over 21
        if (isBust()) {
            return 0;
        } else if (other.isBust()) {
            return 1;
        }

        if (getValue() == other.getValue()) {
            return -1;
        }

        return (getValue() > other.getValue()) ? 1 : 0;
    }

    /**
     * Returns the size of the cards list.
     * 
     * @return the size of the cards in the hand.
     */
    public int getSize() {
        return hand.size();
    }

    /**
     * This method removes all cards from the hand.
     */
    public void Clear() {
        hand.clear();
    }

    /**
     * This method triggered after a split was made by the user, we create
     * a new hand, adding the second card, setting the same bet as the first hand
     * and removes the card from the first hand.
     * 
     * @return the new Hand after the split
     */
    public Hand split() {
        Hand newHand = new Hand();
        newHand.addCard(hand.get(1));
        newHand.setBet(bet);
        hand.remove(1);
        return newHand;
    }

}
