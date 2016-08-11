package blackjackServer;

/**
 * This is the entry point for the server side application, here we initialize
 * a new server,
 * 
 * @author ANI
 */
public class Main {
    /** the port of the server */
    public final static int PORT = 44444;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BlackJackServer myServer = new BlackJackServer(PORT);
        myServer.handleRequests();
    }

}
