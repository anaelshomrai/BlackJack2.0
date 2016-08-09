package DataUtil;

import Users.User;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is one of the objects that the client and the server can read and
 * write. the class contains information relevant for all the information except
 * the information about the game itself. that is to say, when a user preform
 * login, and all action regarding to his account and needed to be updated or be
 * checked against the database.
 *
 * @author Anael
 */
public class ConnectionData implements Serializable {

    private static final long serialVersionUID = 14533685464896L;

    /**
     * constant requestCode for login
     */
    public static final int LOGIN = 1;

    /**
     * constant requestCode for sign up
     */
    public static final int ADD_USER = 2;

    /**
     * constant requestCode for checking if user name meet the conditions
     */
    public static final int CHECK_USER_NAME = 3;

    /**
     * constant requestCode for removing account of a user
     */
    public static final int REMOVE_ACCOUNT = 4;

    /**
     * constant requestCode for changing password
     */
    public static final int CHANGE_PASSWORD = 5;

    /**
     * constant requestCode for changing permission
     */
    public static final int CHANGE_PERMISSION = 6;

    /**
     * constant requestCode to get a list of all users
     */
    public static final int GET_USERS = 7;

    /**
     * constant requestCode to get a specific user
     */
    public static final int GET_USER = 8;

    /**
     * constant requestCode to get a list of all scores
     */
    public static final int GET_SCORES = 9;

    /**
     * constant requestCode to update a user score
     */
    public static final int UPDATE_USER = 10;

    /**
     * constant requestCode to exit the application
     */
    public static final int EXIT = -1;

    private int requestCode;
    private int status = 0;
    private List<User> users = new ArrayList<User>();
    private List<Score> scores = new ArrayList<Score>();
    private User user;
    private String[] information;
    private boolean userExist = false;
    private String userNameCheck;
    private int id;

    /**
     * Returns the status of the request
     *
     * @return the status of the request
     */
    public int getStatus() {
        return status;
    }

    /**
     * Sets the status of the request
     *
     * @param status the status of the request
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * Returns the user name to check. when user sign up we need to check if his
     * chosen user name meets the requirements.
     *
     * @return the user name to check
     */
    public String getUserNameCheck() {
        return userNameCheck;
    }

    /**
     * Sets the user name answer after the check. empty String - if the user
     * name meets the requirements.
     *
     * @param userNameCheck the user name check answer
     */
    public void setUserNameCheck(String userNameCheck) {
        this.userNameCheck = userNameCheck;
    }

    /**
     * Returns the request code given by the user.
     *
     * @return the requsetCode
     */
    public int getRequestCode() {
        return requestCode;
    }

    /**
     * Sets the request code.
     *
     * @param requestCode the request code
     */
    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    /**
     * Returns the information the user entered.
     *
     * @return the information the user entered one of two situation: 1 - user
     * name and password for login 2 - id of the user and the new password for
     * changing password
     */
    public String[] getInformation() {
        return information;
    }

    /**
     * Sets the information.
     *
     * @param info the information the user entered
     */
    public void setInformation(String[] info) {
        this.information = info;
    }

    /**
     * Returns if the user exist in the database/list when performing login
     * request.
     *
     * @return if user exist in the database/list
     */
    public boolean isUserExist() {
        return userExist;
    }

    /**
     * Sets if the user exist in the database/list when performing login
     * request.
     *
     * @param userExist if user exist in the database/list.
     */
    public void setUserExist(boolean userExist) {
        this.userExist = userExist;
    }

    /**
     * Return the user when searching for a user by his id.
     * 
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user when searching for a user by hid id.
     * null - if the user wasn't found.
     * 
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Return the id of the user to search.
     * 
     * @return the user id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the user id to search.
     * 
     * @param id the user id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns a list of users. only avaliable for admin.
     * 
     * @return list of users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Sets the user list after an admin request has been made.
     * 
     * @param users the user list
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }

    /**
     * Returns the list of users score (number of winning, balance).
     * 
     * @return list of users score
     */
    public List<Score> getScores() {
        return scores;
    }

    /**
     * Sets the list of users score (number of winning, balance).
     * 
     * @param scores list of users score
     */
    public void setScores(List<Score> scores) {
        this.scores = scores;
    }
}
