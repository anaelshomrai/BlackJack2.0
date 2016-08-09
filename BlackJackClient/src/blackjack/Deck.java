package blackjack;

import blackjack.Utils.SUIT;
import blackjack.Utils.RANK;
import java.io.Serializable;
import java.util.Collections;
import java.util.Stack;

/**
 * This class represents a deck of cards.
 * a deck contains 52 cards, 4 SUIT, 13 RANKS.
 * this class is used to store all cards avaliable in a stack, because it
 * represents a deck in the most realistic way. 
 * 
 * @author ANI
 */

public class Deck implements Serializable{

    /** the size of the deck */
    int deckSize = 52;
    /** all cards in the deck*/
    Stack<Card> deck = new Stack<>();

    /**
    * This constructor initializes a deck object, then fill the stack with the
    * cards.
    */
    public Deck() {
        initPack();
    }

    /**
     * This method called when creating a new deck.
     * after deck is full, we shuffle the deck of cards using Collection method.
     */
    public void initPack() {
        for (int i = 0; i < SUIT.values().length; i++) {
            for (int j = 0; j < RANK.values().length; j++) {
                deck.push(Card.createCard(i, j));
            }
        }
        Collections.shuffle(deck);
    }
    
    /**
     * This method draws one card from the deck and returns the cars.
     * faceUp will be false only when the dealer gets card at the beginning of
     * the game, because this is the only time in the game a card is hidden.
     * by default, this method will be called when faceUp is true.
     * 
     * @param faceUp is card hidden
     * @return the new card pulled from the deck.
     * or null if the deck is empty.
     */
    public Card drawCard(boolean faceUp) {
        if (deck.size() > 0) {
            Card newCard = deck.pop();
            if (faceUp) {
                newCard.reveal();
            }
            return newCard;
        } else {
            return null;
        }
    }
    
    /**
     * This method draws one card from the deck and returns the cars.
     * This method will be used most of the time.
     * 
     * @return the new card pulled from the deck.
     * or null if the deck is empty.
     * @see #drawCard(boolean)
     */
    public Card drawCard() {
        return drawCard(true);
    }

    /**
     * This method will be used if we run out of cards.
     */
    public void reshuffle() {
        initPack();
    }

}
