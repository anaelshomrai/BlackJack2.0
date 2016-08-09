
package blackjack;

import blackjack.Utils.RANK;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ANI
 */
public class Hand implements Serializable{
    
    private List<Card> hand = new ArrayList<Card>();
    private int bet;
    
    public void AddCard(Card card)
    {
        hand.add(card);
    }
    
   public int getValue()
   {
       int value = 0;
       // looping throgh the hand to sum value
       for (Card c : hand)
       {
           value+=c.getValue();
       }
       
       // iterating again throgh hand to check if we have ace in hand,
       // and we over 21 points(lost) then minus 10 to value
       // ace can be 11 or 1
       for (Card c: hand)
       {
           if ((c.getRank() == RANK.ACE) && value > 21)
               value-=10;
       }
      return value; 
   }
    
    public Card getCard(int index)
    {
        return hand.get(index);
    }

    @Override
    public String toString() {
       String s = "";
       for (int i = 0 ; i< hand.size(); i++)
       {
           s += hand.get(i);
           if (i != hand.size() -1) //it's not the last card
               s += ", ";
       }
       s += " (" + getValue() + ")";
       return s;
    }
    
    public int getBet()
    {
        return bet;
    }
    
    public void setBet(int bet)
    {
        this.bet = bet;
    }
    
    public List<Card> getCards()
    {
        return this.hand;
    }
    
    public boolean isBust()
    {
        return (getValue() > 21);
    }
    
    public boolean isBlackjack()
    {
        return (getValue() == 21) && (hand.size() == 2);
    }
    
    public boolean isSplit()
    {
        if (hand.size() != 2)
            return false;
        else if ((hand.get(0).getRank() == hand.get(1).getRank()))
            return true;
        
        return false;
    }
    
    public int getWinner(Hand other)
    {
        // seraching for blackjack
        if (isBlackjack() && !other.isBlackjack())
            return 1;
        
        // checking if anyone is over 21
        if (isBust())
            return 0;
        else if (other.isBust())
            return 1;
        
        if (getValue() == other.getValue())
            return -1;
        
        return (getValue() > other.getValue())? 1:0;
    }
    
    public int getSize()
    {
        return hand.size();
    }
    
    public void Clear()
    {
        hand.clear();
    }
    
    public Hand split()
    {
        Hand newHand = new Hand();
        newHand.AddCard(hand.get(1));
        newHand.setBet(bet);
        hand.remove(1);
        return newHand;
    }
    
}
