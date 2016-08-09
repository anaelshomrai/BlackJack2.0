
package Users;

import java.io.Serializable;

/**
 *
 * @author ANI
 */
public abstract class User implements Serializable{
   
    private int id;
    private String firstName;
    private String lastName;
    private String gender;
    private String userName;
    private String password;
    private int balance = 0;
    private int wins = 0;
    private int permission;
    
    public User() {}
    
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
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////Get/Set////////////////////////////////////////////////////////////

    public void setId(int id) {
        this.id = id;
    }
      
    
    
      public int getId()
      {
          return this.id;
      }
      
      
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getPermission() {
        return permission;
    }

    public void setPermission(int permission) {
        this.permission = permission;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)){
            return false;
        }
        User u = (User) obj;
        return this.id == u.getId();
    }
    
    
    
    
    //////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return "First Name: " + this.firstName + " Last Name: " + this.lastName
     + " Gender: " + gender + " User Name: " + this.userName + " Password: " + password;
    }
    public abstract void signUp(String firstName,String lastName,String gender,
            String userName,String password);
    
    public abstract boolean changePassword(String oldPassword,String newPassword);
    public abstract void removeAccount(User user);
    public abstract void update();    
}
