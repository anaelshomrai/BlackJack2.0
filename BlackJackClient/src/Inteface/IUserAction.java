
package Inteface;

import Users.User;

/**
 * This is an interface that all of the users must implement to be a User.
 * All the users must have a signUp, changePassword, removeAcount
 * and update methods to implement this interface.
 * 
 * @author ANI
 */
public interface IUserAction {
    
    /**
     * register the user to the database/list.
     * 
     * @param firstName the first name of the user
     * @param lastName the last name of the user
     * @param gender the gender of the user
     * @param userName the user name of the user
     * @param password the password of the user
     */
    public abstract void signUp(String firstName,String lastName,String gender,
            String userName,String password);

    /**
     * Changing user password.
     * 
     * @param oldPassword old password of the user, will be replaced
     * by the new password.
     * @param newPassword new password to replace the old password.
     * @return a boolean indicate if the change was made successfully 
     */
    public abstract boolean changePassword(String oldPassword,String newPassword);

    /**
     * Remove user from database/list.
     * 
     * @param user the user
     * @see Users.User
     */
    public abstract void removeAccount(User user);

    /**
     *  Update user in the database/list.
     */
    public abstract void update();

    
}
