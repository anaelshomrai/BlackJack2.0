package Users;

import DataUtil.ConnectionData;
import Inteface.IUserAction;
import blackjackclient.ConnectionUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a regular user, i.e. - not an admin. A regular user can
 * preform all methods implemented from the IUserAction interface.
 *
 * The methods here aren't implemented because this is the server side, And the
 * server doesn't make any personal changes. The class must be in here for the
 * serialization.
 *
 * @author ANI
 */
public class RegularUser extends User implements IUserAction {

    /**
     * Default constructor. Initializes an empty RegularUser object.
     */
    public RegularUser() {
    }

    /**
     * This constructor initializes an RegularUser object. You can identify a
     * RegularUser if his permission field is 2.
     *
     * @param id the id of the RegularUser
     * @param firstName the first name of the RegularUser
     * @param lastName the last name of the RegularUser
     * @param gender the gender of the RegularUser
     * @param userName the user name of the RegularUser
     * @param password the password of the RegularUser
     */
    public RegularUser(int id, String firstName, String lastName,
            String gender, String userName, String password) {
        super(id, firstName, lastName, gender, userName, password, 2);
        //2 is regular
    }

    /**
     * This constructor initializes a RegularUser object. You can identify an
     * RegularUser if is permission field is 1. This constructor is used when we
     * creating a list of users, containing the user details and his score
     * details.
     *
     * @param firstName the first name of the RegularUser
     * @param lastName the last name of the RegularUser
     * @param gender the gender of the RegularUser
     * @param userName the user name of the RegularUser
     * @param password the password of the RegularUser
     * @param wins the wins of the RegularUser
     * @param balance the balance of the RegularUser
     */
    public RegularUser(String firstName, String lastName,
            String gender, String userName, String password, int wins, int balance) {
        super(firstName, lastName, gender, userName, password, 2, wins, balance);
        //2 is regular
    }

    /**
     * This method create a new RegularUser object, by sending a request with
     * the details to the server.
     *
     * @param firstName the first name of the RegularUser
     * @param lastName the last name of the RegularUser
     * @param gender the gender of the RegularUser
     * @param userName the user name of the RegularUser
     * @param password the password of the RegularUser
     */
    @Override
    public void signUp(String firstName, String lastName, String gender,
            String userName, String password) {
        RegularUser user = new RegularUser(firstName, lastName,
                gender, userName, password, 0, 10000);

        ConnectionUtil myConnection = new ConnectionUtil();
        myConnection.openConnection();
        myConnection.addNewUser(user);
        myConnection.closeConnection();
    }

    /**
     * This method replace the old password with the new password received, by
     * sending the server a request.
     *
     * @param oldPassword the old password of the RegularUser
     * @param newPassword the new password of the RegularUser to replace the old
     * password
     * @return true - if the old password is the current password, and password
     * replaced successfully. false - if the old password doesn't match the
     * current password. the password will not change.
     */
    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.getPassword().equals(oldPassword)) {
            this.setPassword(newPassword);

            ConnectionUtil myConnection = new ConnectionUtil();
            myConnection.openConnection();
            myConnection.setStatus(1);
            myConnection.changePassword(new String[]{String.valueOf(this.getId()), newPassword});
            myConnection.closeConnection();
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method deletes the RegularUser from the database/list by sending the
     * server a request.
     *
     * @param user the RegularUser
     */
    @Override
    public void removeAccount(User user) {
        ConnectionUtil myConnection = new ConnectionUtil();
        myConnection.openConnection();
        myConnection.setStatus(1);
        myConnection.removeAccount(user);
        myConnection.closeConnection();
    }

    /**
     * This method update the RegularUser in the database/list by sending the
     * server a request.
     */
    @Override
    public void update() {
        ConnectionUtil myConnection = new ConnectionUtil();
        myConnection.openConnection();
        myConnection.setStatus(1);
        myConnection.updateUser(this);
        myConnection.closeConnection();
    }

}
