
package Users;

import java.io.Serializable;

/**
 * This class is an abstract class. a user can be a regular user or an admin.
 * This class represent the method and the fields of a standard user.
 *
 * @author ANI
 */
public abstract class User implements Serializable{
   
    /** The id of the user */
    private int id;
    /** The first name of the user */
    private String firstName;
    /** The last name of the user */
    private String lastName;
    /** The gender of the user */   
    private String gender;
    /** The user name of the user */
    private String userName;
    /** The password of the user */
    private String password;
    /** The balance of the user */
    private int balance = 0;
    /** The number of wins of the user */
    private int wins = 0;
    /** The permission (Admin-1/RegularUser-2) of the user */
    private int permission;
    
    /**  Default constructor. Initializes an empty User object. */
    public User() {}
    
    /**
     * This constructor initializes a User object.
     *
     * @param id the id of the User
     * @param firstName the first name of the User
     * @param lastName the last name of the User
     * @param gender the gender of the User
     * @param userName the user name of the User
     * @param password the password of the User
     * @param permission the password of the User
     */
    public User(int id,String firstName,String lastName,String gender, String userName,
            String password, int permission)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.userName = userName;
        this.password = password;
        this.permission = permission;
        this.id = id;
    }
      
    /**
     * This constructor initializes a User object.
     *
     * @param firstName the first name of the User
     * @param lastName the last name of the User
     * @param gender the gender of the User
     * @param userName the user name of the User
     * @param password the password of the User
     * @param permission the permission of the User
     * @param wins the wins of the User
     * @param balance the balance of the User
     */
    public User(String firstName,String lastName,String gender, String userName,
            String password, int permission,int wins,int balance)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.userName = userName;
        this.password = password;
        this.permission = permission;
        this.wins = wins;
        this.balance = balance;
    }
    
    
    ///////////////////////////////////////////////////////////////////////////
    ////////////////////////////Get/Set////////////////////////////////////////

    /**
     * Sets the user id.
     * 
     * @param id the user id to set
     */

    public void setId(int id) {
        this.id = id;
    }
      
    /**
     * Returns the user id.
     * 
     * @return the user id
     */
    public int getId()
      {
          return this.id;
      }
      
    /**
     * Return the user first name.
     * 
     * @return the user first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     *Sets the user first name
     * 
     * @param firstName the user first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Return the user last name.
     * 
     * @return the user last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *Sets the user last name.
     * 
     * @param lastName the user last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Return the user gender.
     * 
     * @return the user gender
     */
    public String getGender() {
        return gender;
    }

    /**
     *Sets the user gender.
     * 
     * @param gender the user gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Return the user user name.
     * 
     * @return the user user name
     */
    public String getUserName() {
        return userName;
    }

    /**
     *Sets the user name.
     * 
     * @param userName the user name
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Return the user password.
     * 
     * @return the user password
     */
    public String getPassword() {
        return password;
    }
    /**
     *Sets the password.
     * 
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Return the user balance.
     * 
     * @return the user balance
     */
    public int getBalance() {
        return balance;
    }

    /**
     *Sets the balance.
     * 
     * @param balance the balance
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    /**
     * Return the user winnings.
     * 
     * @return the user winnings
     */
    public int getWins() {
        return wins;
    }

    /**
     *Sets the winnings.
     * 
     * @param wins the winnings
     */
    public void setWins(int wins) {
        this.wins = wins;
    }

    /**
     * Return the user winnings.
     * 
     * @return the permission of the user (Admin - 1/RegularUser - 2)
     */
    public int getPermission() {
        return permission;
    }

    /**
     *Sets the permission.
     * 
     * @param permission the permission
     */
    public void setPermission(int permission) {
        this.permission = permission;
    }

    /**
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    /**
     *
     * @param obj the object
     * @return false - is object isn't an instance of User
     * ture - this id and object id are equals
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)){
            return false;
        }
        User u = (User) obj;
        return this.id == u.getId();
    }
    
    
    //////////////////////////////////////////////////////////////////////////////////////

    /**
     *
     * @return a String represents this class
     */

    @Override
    public String toString() {
        return "First Name: " + this.firstName + " Last Name: " + this.lastName
     + " Gender: " + gender + " User Name: " + this.userName + " Password: " + password;
    }

    /**
     * This is an abstract method. will be used in derived classes for creating
     * and inserting a new user into the database/list.
     *
     * @param firstName the first name of the User
     * @param lastName the last name of the User
     * @param gender the gender of the User
     * @param userName the user name of the User
     * @param password the password of the User
     */
    public abstract void signUp(String firstName,String lastName,String gender,
            String userName,String password);
    
    /**
     * This method replace the old password with the new password received.
     *
     * @param oldPassword the old password of the User
     * @param newPassword the new password of the User to replace the old
     * password
     * @return true - if the old password is the current password, and password
     * replaced successfully. false - if the old password doesn't match the
     * current password. the password will not change.
     */
    public abstract boolean changePassword(String oldPassword,String newPassword);

    /**
     * This method deletes the Admin from the database/list
     *
     * @param user the Admin
     */
    public abstract void removeAccount(User user);

    /**
     * This method update the Admin in the database/list.
     */
    public abstract void update();    
}
