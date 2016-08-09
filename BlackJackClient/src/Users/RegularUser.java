package Users;

import DataUtil.ConnectionData;
import Inteface.IUserAction;
import blackjackclient.ConnectionUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ANI
 */
public class RegularUser extends User implements IUserAction {

    public RegularUser() {
    }

    public RegularUser(int id, String firstName, String lastName,
            String gender, String userName, String password) {
        super(id, firstName, lastName, gender, userName, password, 2);
//2 is regular
    }

    public RegularUser(String firstName, String lastName,
            String gender, String userName, String password, int wins, int balance) {
        super(firstName, lastName, gender, userName, password, 2, wins, balance);
//2 is regular
    }

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

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.getPassword().equals(oldPassword)) {
            this.setPassword(newPassword);

            ConnectionUtil myConnection = new ConnectionUtil();
            myConnection.openConnection();

            List<ConnectionData> data = new ArrayList<>();
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
        List<ConnectionData> data = new ArrayList<>();
        myConnection.removeAccount(user);
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
