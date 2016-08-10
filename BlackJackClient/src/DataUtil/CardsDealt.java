package DataUtil;

import Users.User;
import blackjack.Card;
import blackjack.Deck;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used by the server to set the cards dealt each round,
 * the user who play, thread id, and the deck of cards after pulling his cards.
 * @author ANI
 */
public class CardsDealt implements Serializable{

    /** The user */
    private User user;
    /** The thread id of the user */
    private long id;
    /** The cards list of the user */
    private List<Card> cards = new ArrayList<>();
    /** The deck of the user, after pulling his cards. */
    private Deck deck;
    
    private boolean blackjack = false;

    /** Default constructor. Initializes an empty cardsDealt object. */
    public CardsDealt() { }

    /**
     *
     * @param user the user who playing
     * @param id the id of the user thread
     * @param cards the cards of the user
     */
    public CardsDealt(User user,int id, List<Card> cards) {
        setUser(user);
        setId(id);
        setCards(cards);
    }

    /**
     * Returns the user
     * 
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the user with the specified user
     * 
     * @param user set the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the thread id
     * 
     * @return the id of the thread
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the thread id
     * 
     * @param id set the thread id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Returns a list of cards
     * 
     * @return list of cards
     * @see blackjack.Card
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Sets the cards list
     * 
     * @param cards set the cards list
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }
    
    /**
     * Appends the card to the end of the cards list
     * 
     * @param card cards to be appended to card list
     */
    public void addCard(Card card){
        this.cards.add(card);
    }

    /**
     * Returns the deck
     * 
     * @return deck the deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Sets the deck
     * 
     * @param deck set the deck
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public boolean isBlackjack() {
        return blackjack;
    }

    public void setBlackjack(boolean blackjack) {
        this.blackjack = blackjack;
    }
    
    

}
