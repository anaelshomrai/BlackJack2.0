package Users;

import DataUtil.ConnectionData;
import Inteface.IAdmin;
import blackjackclient.ConnectionUtil;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ANI
 */
public class Admin extends User implements IAdmin {

    public Admin() {
    }

    public Admin(int id, String firstName, String lastName, String gender,
            String userName, String password) {
        super(id, firstName, lastName, gender, userName, password, 1); // 1 is admin
    }

    public Admin(String firstName, String lastName, String gender,
            String userName, String password, int wins, int balance) {
        super(firstName, lastName, gender, userName, password, 1, wins, balance); // 1 is admin
    }

    @Override
    public void signUp(String firstName, String lastName, String gender,
            String userName, String password) {
        Admin admin = new Admin(firstName, lastName,
                gender, userName, password, 0, 100000);
        ConnectionUtil myConnection = new ConnectionUtil();
        myConnection.addNewUser(admin);
        myConnection.closeConnection();
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.getPassword().equals(oldPassword)) {
            this.setPassword(newPassword);

            ConnectionUtil myConnection = new ConnectionUtil();
            myConnection.openConnection();
            myConnection.changePassword(new String[]{String.valueOf(this.getId()), newPassword});
            myConnection.closeConnection();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void removeAccount(User user) {
        ConnectionUtil myConnection = new ConnectionUtil();
        myConnection.openConnection();
        myConnection.removeAccount(user);
        myConnection.closeConnection();
    }

    @Override
    public void changePermission(int userId) {
        ConnectionUtil myConnection = new ConnectionUtil();
        myConnection.openConnection();
        myConnection.changePermission(userId);
        myConnection.closeConnection();

    }

    @Override
    public void update() {
        ConnectionUtil myConnection = new ConnectionUtil();
        myConnection.openConnection();
        myConnection.updateUser(this);
        myConnection.closeConnection();
    }

}
