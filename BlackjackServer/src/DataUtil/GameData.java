package DataUtil;

import Users.User;
import blackjack.Card;
import blackjack.Deck;
import blackjack.Utils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is one of the objects that client and the server can read and
 * write. the class contains information relevant only for the game itself. When
 * a game started this is the only object used.
 *
 * @author ANI
 */
public class GameData implements Serializable {

    private static final long serialVersionUID = 2789907821674L;

    /**
     * constant requestCode to waiting for players to enter the game.
     */
    public static final int WAITING_FOR_PLAYERS = 1;

    /**
     * constant requestCode when the server starts the game, i.e. - there are 3
     * players waiting for players.
     */
    public static final int GAME_START = 2;

    /**
     * constant requestCode for when a game is ended. (if one of the clients
     * disconnected).
     */
    public static final int GAME_END = 3;

    /**
     * constant requestCode for when a player wait to take his turn.
     */
    public static final int WAIT_FOR_YOUR_TURN = 4;

    /**
     * constant requestCode for when this the client turn (game state changes to
     * HITTING).
     */
    public static final int YOUR_TURN = 5;

    /**
     * constant requestCode
     */
    public static final int GET_CARDS = 6;

    /**
     * constant requestCode for when a player want to exit the game before the
     * game has started
     */
    public static final int EXIT_GAME = 9;

    /**
     * constant requestCode for when a player want to continue to the game and
     * keep waiting for players
     *
     */
    public static final int CONTINUE = 10;

    /**
     * constant requestCode for exiting the game.
     */
    /**
     * constant requestCode for when a thread is closed
     */
    public static final int EXIT = -1;

    /**
     * the number of the constant request code
     */
    private int requestCode;
    /**
     * if status = 0, that means no more object waiting to be read. if status =
     * 1, that means there is still an object in the input stream.
     */
    private int status = 0;
    /**
     * number of players in the game
     */
    private int playerNum;
    /**
     * if the game started, that means we have 2/3 players
     */
    private boolean gameStart = false;
    /**
     * the state of the game
     *
     * @see blackjack.Utils.GameState
     */
    private Utils.GameState state;
    /**
     * when a player finish his turn.
     */
    private boolean done;
    /**
     * deck of cards
     */
    private Deck deck;
    /**
     * list of player cards
     */
    private List<Card> cards = new ArrayList<>();
    /**
     * list of dealer cards the first players deals two cards to the dealer,
     * then the rest of the players get those cards.
     */
    private List<Card> dealerCards = new ArrayList<>();
    /**
     * list of player cards that dealt at their turn after the first 2.
     */
    private List<CardsDealt> cardsDealt = new ArrayList<>();
    /**
     * list of player cards, in case of a split.
     */
    private List<Card> splitCards;
    /**
     * if a player add cards in his turn
     */
    private boolean moreCardsAdded = false;
    /**
     * if a split made by a player
     */
    private boolean split = false;
    /**
     * if player has a blackjack
     */
    private boolean blackjack = false;
    /**
     * id of the first player
     */
    private long player1Id;
    /**
     * id of the second player
     */
    private long player2Id;
    /**
     * id of the current player
     */
    private long myId;
    /**
     * user is the player
     */
    private User user;
    /**
     * the name of the first player
     */
    private String player1Name;
    /**
     * the name of the second player
     */
    private String player2Name;
    /**
     * id the game went from 3 players to 2 players game
     */
    private boolean changeGame;

    /**
     * Returns the status.
     *
     * @return status of the input stream
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status. 0 - no more objects 1 - more objects on the way
     *
     * @param status status of the input stream
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Returns the game state.
     *
     * @return the state of the game
     */
    public Utils.GameState getState() {
        return state;
    }

    /**
     * Sets the state of the game.
     *
     * @param state the state of the game
     */
    public void setState(Utils.GameState state) {
        this.state = state;
    }

    /**
     * Returns a constant request code.
     *
     * @return the request code
     */
    public int getRequestCode() {
        return requestCode;
    }

    /**
     * Sets a constant request code.
     *
     * @param requestCode the request code
     */
    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    /**
     * Returns the number of players in the game.
     *
     * @return the number of players in the game
     */
    public int getPlayerNum() {
        return playerNum;
    }

    /**
     * Sets the number of players in the game.
     *
     * @param playerNum the number of players in the game
     */
    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    /**
     * Returns if the game started. the game will start when there are 3 players
     * in the queue.
     *
     * @return if the game started
     */
    public boolean isGameStart() {
        return gameStart;
    }

    /**
     * Sets if the game started.
     *
     * @param gameStart if the game started
     */
    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }

    /**
     * Returns if the player finished is turn.
     *
     * @return if the player finished is turn.
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Sets if the player finished is turn.
     *
     * @param done if the player finished is turn.
     */
    public void setDone(boolean done) {
        this.done = done;
    }

    /**
     * Returns the deck of cards.
     *
     * @return the deck of cards.
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Sets the deck of cards.
     *
     * @param deck the deck of cards.
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    /**
     * Returns list of cards.
     *
     * @return list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Sets list of cards.
     *
     * @param cards list of cards
     */
    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    /**
     * This method add the specific card into the end of the list of cards.
     *
     * @param card the card to add to the list of cards
     */
    public void addCard(Card card) {
        this.cards.add(card);
    }

    /**
     * Returns the dealer cards that have been dealt.
     *
     * @return the dealer cards
     */
    public List<Card> getDealerCards() {
        return dealerCards;
    }

    /**
     * Sets the dealer cards that have been dealt.
     *
     * @param cards the dealer cards
     */
    public void setDealerCards(List<Card> cards) {
        this.dealerCards = cards;
    }

    /**
     * This method add the specific card into the end of the list of dealer
     * cards.
     *
     * @param card the card to add to the list of cards
     */
    public void addDealerCard(Card card) {
        this.dealerCards.add(card);
    }

    /**
     * Returns list of cards dealt to the players.
     *
     * @return the list of the cardsDelat.
     */
    public List<CardsDealt> getCardsDealt() {
        return cardsDealt;
    }

    /**
     * Sets list of cards dealt to the players.
     *
     * @param cardsDealt the list of the cardsDelat.
     */
    public void setCardsDealt(List<CardsDealt> cardsDealt) {
        this.cardsDealt = cardsDealt;
    }

    /**
     * Returns if player hit card after the first 2 was dealt.
     *
     * @return if player received more cards at his turn.
     */
    public boolean isMoreCardsAdded() {
        return moreCardsAdded;
    }

    /**
     * sets if player hit card after the first 2 was dealt.
     *
     * @param moreCardsAdded if player received more cards at his turn.
     */
    public void setMoreCardsAdded(boolean moreCardsAdded) {
        this.moreCardsAdded = moreCardsAdded;
    }

    /**
     * Returns if a split made by a player.
     *
     * @return if a split made by a player
     */
    public boolean isSplit() {
        return split;
    }

    /**
     * Sets if a split made by a player.
     *
     * @param split if a split made by a player
     */
    public void setSplit(boolean split) {
        this.split = split;
    }

    /**
     * Returns list of the new cards after split has been made.
     *
     * @return list of cards.
     */
    public List<Card> getSplitCards() {
        return splitCards;
    }

    /**
     * Sets list of the new cards after split has been made.
     *
     * @param splitCards list of cards.
     */
    public void setSplitCards(List<Card> splitCards) {
        this.splitCards = splitCards;
    }

    /**
     * Returns if a player has a blackjack. this is important because if a
     * player has a blackjack straight away when the first 2 cards dealt. the
     * server needs to know that we can skip his turn.
     *
     * @return if player has a blackjack
     */
    public boolean isBlackjack() {
        return blackjack;
    }

    /**
     * Sets if a player has a blackjack. this is important because if a player
     * has a blackjack straight away when the first 2 cards dealt. the server
     * needs to know that we can skip his turn.
     *
     * @param blackjack if player has a blackjack
     */
    public void setBlackjack(boolean blackjack) {
        this.blackjack = blackjack;
    }

    /**
     * Returns the id of the user, who is currently playing.
     *
     * @return the id of the user
     */
    public long getMyId() {
        return myId;
    }

    /**
     * Sets the id of the user, who is currently playing.
     *
     * @param myId the id of the user
     */
    public void setMyId(long myId) {
        this.myId = myId;
    }

    /**
     * Returns the player 1 id, according to each player.
     *
     * @return the id of the player 1
     */
    public long getPlayer1Id() {
        return player1Id;
    }

    /**
     * Sets the player 1 id, according to each player.
     *
     * @param player1Id the id of the player 1
     */
    public void setPlayer1Id(long player1Id) {
        this.player1Id = player1Id;
    }

    /**
     * Returns the player 2 id, according to each player.
     *
     * @return the id of the player 2
     */
    public long getPlayer2Id() {
        return player2Id;
    }

    /**
     * Sets the player 2 id, according to each player.
     *
     * @param player2Id the id of the player 2
     */
    public void setPlayer2Id(long player2Id) {
        this.player2Id = player2Id;
    }

    /**
     * Returns the user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the name of the first player
     *
     * @return the name of the first player
     */
    public String getPlayer1Name() {
        return player1Name;
    }

    /**
     * Sets the name of the first player
     *
     * @param player1Name the name of the first player
     */
    public void setPlayer1Name(String player1Name) {
        this.player1Name = player1Name;
    }

    /**
     * Returns the name of the second player
     *
     * @return the name of the second player
     */
    public String getPlayer2Name() {
        return player2Name;
    }

    /**
     * Sets the name of the second player
     *
     * @param player2Name the name of the second player
     */
    public void setPlayer2Name(String player2Name) {
        this.player2Name = player2Name;
    }

    /**
     * Returns if the game has gone from 3 players game to 2 players game.
     *
     * @return if the game changed players number.
     */
    public boolean isChangeGame() {
        return changeGame;
    }

    /**
     * Sets if the game has gone from 3 players game to 2 players game.
     *
     * @param changeGame if the game changed players number.
     */
    public void setChangeGame(boolean changeGame) {
        this.changeGame = changeGame;
    }

}
