package blackjack;

import blackjack.Utils.RANK;
import blackjack.Utils.SUIT;
import java.io.Serializable;

/**
 * This class represent a card. every card card has a SUIT and a RANK,
 * and a field indicating if he is hidden.
 * all cards starting when they are hidden, and reveal when a card is pulled
 * except from one card of the dealer in the start of the game that remain
 * hidden until the game state changes to DELAER.
 * 
 * @author ANI
 */
public class Card implements Serializable{
    
    private SUIT suit;
    private RANK rank;
    private boolean hidden;
    
    /**
     * This constructor initializes a Card object.
     * Each card has a RANK and a Suit.
     * by Default, the card isn't hidden.
     * 
     * @param suit enum who represent the shape of the card
     * @param rank enum who represent the number of the card
     * @see blackjack.Utils.RANK
     * @see blackjack.Utils.SUIT
     */
    public Card(SUIT suit, RANK rank) {
	this.suit = suit;
	this.rank = rank;
	this.hidden = true;
   }
    
    /**
     * Returns the SUIT of the card.
     *
     * @return the SUIT of this card
     * @see blackjack.Utils.SUIT
     */
    public SUIT getSuit()
    {
        return suit;
    }
    
    /**
     * Returns the RANK of the card.
     *
     * @return the RANK of this card
     * @see blackjack.Utils.RANK
     */
    public RANK getRank()
    {
        return rank;
    }
    
    /**
     * Change status of the card to revealed.
     * At the start of the game, the dealer has one hidden card,
     * after the players played, the card is reveal.
     */
    public void reveal()
    {
         hidden = false;
    }
    
    /**
     * Sets if the card is hidden.
     * 
     * @return the card status, hidden or revealed.
     * hidden - the players doesn't see the card.
     */
    public boolean isHidden()
    {
        return hidden;
    }
    
    /**
     * This method is used when creating a new deck.
     * 
     * @param i the SUIT
     * @param j the RANK
     * @return card with this SUIT and Rank
     * @see blackjack.Deck
     */
    public static Card createCard(int i, int j)
    {
        SUIT suit = SUIT.values()[i];
        RANK rank = RANK.values()[j];
        return new Card(suit,rank);
    }
    
    /**
     * This method is used to sum cards value in a player hand.
     * 
     * @return a number represent the value of a card
     * or 0 is the card is hidden
     */
    public int getValue()
    {
        int value = 0;
        
        if (hidden) return value; // don't count hidden cards
        
        switch(rank)
        {
            case ACE:  value = 11;
            break;
            case KING: value = 10;
            break;
            case QUEEN: value = 10;
            break;
            case JACK: value = 10;
            break;
            case TEN: value = 10;
            break;
            case NINE: value = 9;
            break;
            case EIGHT: value = 8;
            break;
            case SEVEN: value = 7;
            break;
            case SIX: value = 6;
            break;
            case FIVE: value = 5;
            break;
            case FOUR: value = 4;
            break;
            case THREE: value = 3;
            break;
            case TWO: value = 2;
            break;
            
            default: System.out.println("Invalid card!");
            break;
        }
        return value;
        }

    /**
     *
     * @return a String representing this class
     */
    @Override
    public String toString() {
        if (!hidden)
        return suit.toString() + " " + rank.toString();
        else
            return "Hidden";
    }
    }


