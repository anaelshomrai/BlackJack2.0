package blackjack;

import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 * This class contains variables used during the game.
 * SUIT - contains the 4
 * suits of a card.
 * RANK - contains the 13 values available in a deck of cards.
 * GameState - contains 5 states avaliable in the game.
 * map - when a game created, this map will initialize with a suit, and a list 
 * of card images with this suit.
 * cardImages - after initialize the map, we will use this cardImages to
 * get the map as unmodifiableMap (read-only). spadesImages, heartsImages,
 * diamondsImages and clubsImages - list containing the address for the card
 * images sorted according to the suit.
 *
 * @author ANI
 */
public class Utils {

    /**
     * 4 shapes of a card
     */
    public enum SUIT {

        /**
         * spade shape
         */
        SPADES,
        /**
         * heart shape
         */
        HEARTS,
        /**
         * diamond shape
         */
        DIAMONDS,
        /**
         * clubs shape
         */
        CLUBS
    }

    /**
     * 13 values of a cards. special rules in blackjack: ACE - equals 1,11.
     * JACK,QUEEN,KING - equals 10.
     */
    public enum RANK {

        /**
         * ace value (1 or 11)
         */
        ACE,
        /**
         * two value
         */
        TWO,
        /**
         * three value
         */
        THREE,
        /**
         * four value
         */
        FOUR,
        /**
         * five value
         */
        FIVE,
        /**
         * six value
         */
        SIX,
        /**
         * seven value
         */
        SEVEN,
        /**
         * eight value
         */
        EIGHT,
        /**
         * nine value
         */
        NINE,
        /**
         * ten value
         */
        TEN,
        /**
         * jack value (10)
         */
        JACK,
        /**
         * queen value (10)
         */
        QUEEN,
        /**
         * king value (10)
         */
        KING
    }

    /**
     * 5 game states avaliable in a game of blackjack.
     */
    public enum GameState {

        /**
         * betting state, players must enter a bet according to the rules.
         */
        BETTING,
        /**
         * dealing state, player gets 2 cards.
         */
        DEALING,
        /**
         * hitting state, player makes his move (hit,stay,double down,split)
         */
        HITTING,
        /**
         * dealer state, dealer hit cards until he get minimum of 17.
         */
        DEALER,
        /**
         * resolve state, this is the final state in a blackjack game, we find
         * out who is the winner.
         */
        RESOLVE
    }

    /**
     * When a game created, this map will initialize with a suit, and a list of card
     * images with this suit.
     */
    public static Map<Utils.SUIT, ImageIcon[]> map = new HashMap<>();

    /**
     * After initialize the map, we will use this cardImages to
     * get the map as unmodifiableMap (read-only). spadesImages, heartsImages,
     * diamondsImages and clubsImages - list containing the address for the card
     * images sorted according to the suit.
     */
    public static Map<Utils.SUIT, ImageIcon[]> cardImages = null;

    /**
     * list of all the spades card images
     */
    public static ImageIcon[] spadesImages = {
        new ImageIcon("./src/img/cards/ace_of_spades.png"),
        new ImageIcon("./src/img/cards/2_of_spades.png"),
        new ImageIcon("./src/img/cards/3_of_spades.png"),
        new ImageIcon("./src/img/cards/4_of_spades.png"),
        new ImageIcon("./src/img/cards/5_of_spades.png"),
        new ImageIcon("./src/img/cards/6_of_spades.png"),
        new ImageIcon("./src/img/cards/7_of_spades.png"),
        new ImageIcon("./src/img/cards/8_of_spades.png"),
        new ImageIcon("./src/img/cards/9_of_spades.png"),
        new ImageIcon("./src/img/cards/10_of_spades.png"),
        new ImageIcon("./src/img/cards/jack_of_spades.png"),
        new ImageIcon("./src/img/cards/queen_of_spades.png"),
        new ImageIcon("./src/img/cards/king_of_spades.png")
    };

    /**
     * list of all the hearts card images
     */
    public static ImageIcon[] heartsImages = {
        new ImageIcon("./src/img/cards/ace_of_hearts.png"),
        new ImageIcon("./src/img/cards/2_of_hearts.png"),
        new ImageIcon("./src/img/cards/3_of_hearts.png"),
        new ImageIcon("./src/img/cards/4_of_hearts.png"),
        new ImageIcon("./src/img/cards/5_of_hearts.png"),
        new ImageIcon("./src/img/cards/6_of_hearts.png"),
        new ImageIcon("./src/img/cards/7_of_hearts.png"),
        new ImageIcon("./src/img/cards/8_of_hearts.png"),
        new ImageIcon("./src/img/cards/9_of_hearts.png"),
        new ImageIcon("./src/img/cards/10_of_hearts.png"),
        new ImageIcon("./src/img/cards/jack_of_hearts.png"),
        new ImageIcon("./src/img/cards/queen_of_hearts.png"),
        new ImageIcon("./src/img/cards/king_of_hearts.png")
    };

    /**
     * list of all the diamonds card images
     */
    public static ImageIcon[] diamondsImages = {
        new ImageIcon("./src/img/cards/ace_of_diamonds.png"),
        new ImageIcon("./src/img/cards/2_of_diamonds.png"),
        new ImageIcon("./src/img/cards/3_of_diamonds.png"),
        new ImageIcon("./src/img/cards/4_of_diamonds.png"),
        new ImageIcon("./src/img/cards/5_of_diamonds.png"),
        new ImageIcon("./src/img/cards/6_of_diamonds.png"),
        new ImageIcon("./src/img/cards/7_of_diamonds.png"),
        new ImageIcon("./src/img/cards/8_of_diamonds.png"),
        new ImageIcon("./src/img/cards/9_of_diamonds.png"),
        new ImageIcon("./src/img/cards/10_of_diamonds.png"),
        new ImageIcon("./src/img/cards/jack_of_diamonds.png"),
        new ImageIcon("./src/img/cards/queen_of_diamonds.png"),
        new ImageIcon("./src/img/cards/king_of_diamonds.png")
    };

    /**
     * list of all the clubs card images
     */
    public static ImageIcon[] clubsImages = {
        new ImageIcon("./src/img/cards/ace_of_clubs.png"),
        new ImageIcon("./src/img/cards/2_of_clubs.png"),
        new ImageIcon("./src/img/cards/3_of_clubs.png"),
        new ImageIcon("./src/img/cards/4_of_clubs.png"),
        new ImageIcon("./src/img/cards/5_of_clubs.png"),
        new ImageIcon("./src/img/cards/6_of_clubs.png"),
        new ImageIcon("./src/img/cards/7_of_clubs.png"),
        new ImageIcon("./src/img/cards/8_of_clubs.png"),
        new ImageIcon("./src/img/cards/9_of_clubs.png"),
        new ImageIcon("./src/img/cards/10_of_clubs.png"),
        new ImageIcon("./src/img/cards/jack_of_clubs.png"),
        new ImageIcon("./src/img/cards/queen_of_clubs.png"),
        new ImageIcon("./src/img/cards/king_of_clubs.png")
    };
}
