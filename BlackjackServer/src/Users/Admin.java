package Users;

import Inteface.IAdmin;

/**
 * This class extends User class. user who is admin has more methods. An admin
 * can delete user, if he isn't an admin. An admin can promote a regular user to
 * an admin. Admins cant sign up. they must be added directly to the database,
 * or an admin promoted a regular user.
 *
 * The methods aren't implemented because this is the server side, And the
 * server doesn't make any personal changes. The class must be in here for the
 * serialization.
 *
 * @author ANI
 */
public class Admin extends User implements IAdmin {

    /**  Default constructor. Initializes an empty Admin object. */
    public Admin() {
    }

    /**
     * This constructor initializes an Admin object. You can identify an Admin,
     * if his permission field is 1.
     *
     * @param id the id of the Admin
     * @param firstName the first name of the Admin
     * @param lastName the last name of the Admin
     * @param gender the gender of the Admin
     * @param userName the user name of the Admin
     * @param password the password of the Admin
     */
    public Admin(int id, String firstName, String lastName, String gender,
            String userName, String password) {
        super(id, firstName, lastName, gender, userName, password, 1); // 1 is admin
    }

    /**
     * This constructor initializes an Admin object. You can identify an admin,
     * if is permission field is 1. This constructor is used when we creating a
     * list of users, containing the user details and his score details.
     *
     * @param firstName the first name of the Admin
     * @param lastName the last name of the Admin
     * @param gender the gender of the Admin
     * @param userName the user name of the Admin
     * @param password the password of the Admin
     * @param wins the wins of the Admin
     * @param balance the balance of the Admin
     */
    public Admin(String firstName, String lastName, String gender,
            String userName, String password, int wins, int balance) {
        super(firstName, lastName, gender, userName, password, 1, wins, balance); // 1 is admin
    }

    /**
     * This method sign up a new Admin.
     * In this application, currently an admin can't sign up.
     *
     * @param firstName the first name of the Admin
     * @param lastName the last name of the Admin
     * @param gender the gender of the Admin
     * @param userName the user name of the Admin
     * @param password the password of the Admin
     */
    @Override
    public void signUp(String firstName, String lastName, String gender,
            String userName, String password) {
        Admin admin = new Admin(firstName, lastName,
                gender, userName, password, 0, 100000);
    }

    /**
     * This method replace the old password with the new password received.
     *
     * @param oldPassword the old password of the Admin
     * @param newPassword the new password of the Admin to replace the old
     * password
     * @return true - if the old password is the current password, and password
     * replaced successfully. false - if the old password doesn't match the
     * current password. the password will not change.
     */
    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        return false;
    }

    /**
     * This method deletes the Admin from the database/list
     *
     * @param user the Admin
     */
    @Override
    public void removeAccount(User user) {

    }

    /**
     * This method change permission of the Admin if received.
     *
     * @param userId the id of the Admin
     */
    @Override
    public void changePermission(int userId) {

    }

    /**
     * This method update the Admin in the database/list.
     */
    @Override
    public void update() {

    }

}
