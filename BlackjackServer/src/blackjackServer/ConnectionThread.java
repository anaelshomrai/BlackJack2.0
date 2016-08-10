package blackjackServer;

import DataUtil.ConnectionData;
import DataBase.DB;
import DataUtil.GameData;
import Users.User;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class handle the client threads, when a client sends a request, this
 * class handle the request.
 *
 * @author Anael
 */
public class ConnectionThread extends Thread {

    /**
     * client socket
     */
    private Socket socket;
    /**
     * client input stream
     */
    private InputStream input;
    /**
     * client object input stream
     */
    private ObjectInputStream ois;
    /**
     * client output stream
     */
    private OutputStream output;
    /**
     * client object output stream
     */
    private ObjectOutputStream oos;
    /**
     * instance of the database
     */
    private final DB db = DB.getInstance();
    /**
     * the server which the client is connected to
     */
    private BlackJackServer server;

    private boolean gameOn = false;

    /**
     * This constructor initialize a new ConnectionThread object and sets client
     * socket.
     *
     * @param socket the clients socket created by the server
     */
    public ConnectionThread(Socket socket) {
        this.socket = socket;
    }

    /**
     * This constructor initialize a new ConnectionThread object, and sets the
     * client socket the server.
     *
     * @param socket the client socket
     * @param server the server who accepted this client socket
     */
    public ConnectionThread(Socket socket, BlackJackServer server) {
        this(socket);
        this.server = server;
    }

    /**
     * Returns the client socket.
     *
     * @return the client socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Sets the client socket.
     *
     * @param socket the client socket
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * Returns the client input stream.
     *
     * @return the client input stream
     */
    public InputStream getInput() {
        return input;
    }

    /**
     * Sets the client input stream.
     *
     * @param input the client input stream
     */
    public void setInput(InputStream input) {
        this.input = input;
    }

    /**
     * Returns the client object input stream.
     *
     * @return the client object input stream
     */
    public ObjectInputStream getOis() {
        return ois;
    }

    /**
     * Sets the client object input stream.
     *
     * @param ois the client object input stream.
     */
    public void setOis(ObjectInputStream ois) {
        this.ois = ois;
    }

    /**
     * Returns the client output stream.
     *
     * @return the client output stream
     */
    public OutputStream getOutput() {
        return output;
    }

    /**
     * Sets the client output stream.
     *
     * @param output the client output stream
     */
    public void setOutput(OutputStream output) {
        this.output = output;
    }

    /**
     * Returns the client object output stream.
     *
     * @return the client object output stream
     */
    public ObjectOutputStream getOos() {
        return oos;
    }

    /**
     * Sets the client object output stream.
     *
     * @param oos the client object output stream
     */
    public void setOos(ObjectOutputStream oos) {
        this.oos = oos;
    }

    /**
     * This method handle the client requests, there are two types of object the
     * client can send and receive, ConnectionData and GameData. When receiving
     * a ConnectionData, this method handle the request according to the
     * RequestCode. When receiving a GameData , this method wait for a 3 players,
     * but if a certain time has passed, checking if there is 2 players and
     * activate a game of 2.
     *
     * @see DataUtil.ConnectionData
     * @see DataUtil.GameData
     */
    @Override
    public synchronized void run() {
        try {
            output = socket.getOutputStream();
            oos = new ObjectOutputStream(output);
            input = socket.getInputStream();
            ois = new ObjectInputStream(input);

            while (true) {
                Object obj = ois.readObject();
                if (obj instanceof ConnectionData) {
                    ConnectionData requestData = (ConnectionData) obj;
                    ConnectionData responseData = new ConnectionData();

                    switch (requestData.getRequestCode()) {

                        case ConnectionData.LOGIN:
                            String[] existingUser = requestData.getInformation();
                            User u = db.checkUserExist(existingUser);
                            if (existingUser == null) {
                                responseData.isUserExist();
                            } else {
                                responseData.setUser(u);
                            }
                            oos.writeObject(responseData);
                            oos.flush();
                            oos.reset();
                            break;

                        case ConnectionData.ADD_USER:
                            User newUser = requestData.getUser();
                            db.addUser(newUser);
                            break;

                        case ConnectionData.CHECK_USER_NAME:
                            String userName = requestData.getUserNameCheck();
                            String answer = db.checkUserName(userName);
                            responseData.setUserNameCheck(answer);
                            oos.writeObject(answer);
                            oos.flush();
                            oos.reset();
                            break;

                        case ConnectionData.REMOVE_ACCOUNT:
                            User toDelete = requestData.getUser();
                            db.removeAccount(toDelete);
                            break;

                        case ConnectionData.CHANGE_PASSWORD:
                            String[] info = requestData.getInformation();
                            int id = Integer.parseInt(info[0]);
                            String password = info[1];
                            db.changePassword(id, password);
                            break;

                        case ConnectionData.CHANGE_PERMISSION:
                            int userId = requestData.getId();
                            db.changePermission(userId);
                            break;

                        case ConnectionData.GET_USERS:
                            responseData.setUsers(db.getUsers());
                            oos.writeObject(responseData);
                            oos.flush();
                            oos.reset();
                            break;

                        case ConnectionData.GET_USER:
                            int idToFind = requestData.getId();
                            responseData.setUser(db.findUserById(idToFind));
                            oos.writeObject(responseData);
                            oos.flush();
                            oos.reset();
                            break;

                        case ConnectionData.GET_SCORES:
                            responseData.setScores(db.getAllScores());
                            oos.writeObject(responseData);
                            oos.flush();
                            oos.reset();
                            break;

                        case ConnectionData.UPDATE_USER:
                            User userToUpdate = requestData.getUser();
                            db.updateUser(userToUpdate.getId(),
                                    userToUpdate.getWins(),
                                    userToUpdate.getBalance());
                            break;

                        case ConnectionData.EXIT:
//                            server.threads.remove(this);
//                            ois.close();
//                            input.close();
//                            oos.close();
//                            output.close();
                            break;
                    }
                    if (requestData.getStatus() == 0) {
//                        ois.close();
//                        input.close();
//                        oos.close();
//                        output.close();
                        break;
                    }

                } else {
                    GameData requestData = (GameData) obj;
                    GameData responseData = new GameData();

                    switch (requestData.getRequestCode()) {

                        case GameData.WAITING_FOR_PLAYERS:
                            server.addPlayer(this, requestData.getUser());
                            int size = server.getPlayesrsSize();
                            if (size < 3) {

                                responseData.setPlayerNum(size);
                                responseData.setGameStart(false);
                                getOos().writeObject(responseData);
                                getOos().flush();
                                getOos().reset();

                                Thread check = new Thread(new checkPlayersSize());
                                check.start();
                            } else {
                                responseData.setPlayerNum(size);
                                responseData.setGameStart(true);
                                getOos().writeObject(responseData);
                                getOos().flush();
                                getOos().reset();
                                gameOn = true;
                                server.new HandleGameOf3().startGame();
                            }

                            System.out.println("clients awaits size: " + server.getPlayesrsSize());
                            break;

                        case GameData.EXIT_GAME:
                            System.out.println("before remove " + server.getPlayesrsSize());
                            server.removePlayer(this, requestData.getUser());
                            System.out.println("after remove " + server.getPlayesrsSize());
                            break;

                        case GameData.CONTINUE:
                            break;

                    }

                    if (requestData.getStatus() == 0) {
//                        ois.close();
//                        input.close();
//                        oos.close();
//                        output.close();
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Error creating output and input streams");
            ex.getStackTrace();
            Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
            //System.exit(1);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * This Class check every 1 minute if we have 2 players waiting for a game,
     * if we found there is 2 players waiting for the game, and a third player
     * hasn't join we start a game of 2 players.
     */
    public class checkPlayersSize implements Runnable {

        /**
         * Runs until a game start, game of 2 or game of 3.
         */
        @Override
        public void run() {
            while (!gameOn) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
                }
                int size = server.getPlayesrsSize();
                if (size < 3 && size == 2) {
                    gameOn = true;
                    server.new HandleGameOf2().startGame();
                }
            }
        }

    }

}
