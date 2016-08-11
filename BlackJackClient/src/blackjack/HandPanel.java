package blackjack;

import static blackjack.Utils.clubsImages;
import static blackjack.Utils.diamondsImages;
import static blackjack.Utils.heartsImages;
import static blackjack.Utils.spadesImages;
import java.awt.Graphics;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

// Drawing Hands of cards to the screen.

/**
 *
 * @author Anael
 */
public class HandPanel extends JPanel implements Serializable{

    private List<Hand> targetHands;

    private static Image cardBackImage = new ImageIcon("./src/img/cards/back.png").getImage();

    private static final Map<Utils.SUIT, ImageIcon[]> CARD_IMAGE;
    

    static {
        Map<Utils.SUIT, ImageIcon[]> map = new HashMap<>();
        map.put(Utils.SUIT.SPADES, spadesImages);
        map.put(Utils.SUIT.HEARTS, heartsImages);
        map.put(Utils.SUIT.DIAMONDS, diamondsImages);
        map.put(Utils.SUIT.CLUBS, clubsImages);
        CARD_IMAGE = Collections.unmodifiableMap(map);
    }

    // only one hand

    /**
     *
     * @param targetHand
     */
    public HandPanel(Hand targetHand) {
        this.targetHands = new ArrayList<>();
        this.targetHands.add(targetHand);
    }

    // More than one hand

    /**
     *
     * @param targetHands
     */
    public HandPanel(List<Hand> targetHands) {
        this.targetHands = targetHands;
    }

    /**
     * Paint each card in the hand.
     * @param g
     */
    public void paintComponent(Graphics g) {
        for (int y = 0; y < targetHands.size(); y++) {
            List<Card> cards = targetHands.get(y).getCards();
            for (int x = 0; x < cards.size(); x++) {

                // If the card is hidden, draw the back image. Otherwise, 
                //draw it's corresponding face.
                if (cards.get(x).isHidden()) 
                {
                    g.drawImage(cardBackImage, (x * 20) + ((800 / 
                            (targetHands.size() + 1)) * (y + 1)) - 75, 10, null);
                } else {
                    g.drawImage(getCardImage(cards.get(x)).getImage(), (x * 20) + ((800 / 
                            (targetHands.size() + 1)) * (y + 1)) - 75, 10, null);
                }
            }
        }
    }

    private static ImageIcon getCardImage(Card card) {
        return CARD_IMAGE.get(card.getSuit())[card.getRank().ordinal()];
    }
}
