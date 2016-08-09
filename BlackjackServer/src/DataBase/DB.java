package DataBase;

import DataUtil.Score;
import Users.Admin;
import Users.RegularUser;
import Users.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class consists methods to communicate with the database. This class
 * initialize only once, Implementing Singleton pattern. In addition of saving
 * data in the database, the data also store a list of the updated data.
 *
 * @author ANI
 */
public class DB {

    private List<User> users = new ArrayList<>();

    /**  Will initialize only once */
    private static DB instance = null;

    /** The last id inserted to users table in the database */
    public static int lastId;
    /** The connection to the database */
    private Connection connection = null;
    /** the statement used in queries */
    private Statement statement = null;

    /**
     * @return the lastId inserted to users table
     */
    public static int getLastId() {
        return lastId;
    }

    /**
     * This constructor will be activate only once, create the connection to the database and
     * initialize table if needed
     */
    private DB() {
        createConnection();
        checkIfTableExist();
    }

    /**
     * Checking if tables exist, else create them
     */
    private void checkIfTableExist() {
        boolean found = false;
        createConnection();
        try {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "USERS", null);
            if (!tables.next()) {
                closeConnection();
                initUsersTable();
            }

        } catch (SQLException e) {
            System.out.println(e.getCause());
        }
        System.out.println("\n\n\n\n");
        found = false;
        createConnection();
        try {
            DatabaseMetaData dbm = connection.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "SCORE", null);
            if (!tables.next()) {
                closeConnection();
                initScoreTable();
            }
        } catch (SQLException e) {
            System.out.println(e.getCause());
        }
    }

    /**
     * execute the SQL for creating the users table
     */
    private void initUsersTable() {
        String createUsersTable = "CREATE TABLE BLACKJACK.USERS ("
                + " ID INTEGER generated by default as identity (start with 1,"
                + " increment by 1) not null,"
                + " FIRSTNAME VARCHAR(20) NOT NULL,"
                + "LASTNAME VARCHAR(20) NOT NULL,"
                + " GENDER VARCHAR(10) NOT NULL,"
                + "USERNAME VARCHAR(20) NOT NULL,"
                + " PASSWORD VARCHAR(20) NOT NULL,"
                + "PERMISSION INTEGER NOT NULL,"
                + "CONSTRAINT USERS_PK_ID PRIMARY KEY (ID))";
        createConnection();
        try {
            statement = connection.createStatement();
            statement.executeUpdate(createUsersTable);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        closeConnection();
        fillUsers();
    }

    /**
     * execute the SQL for creating the score table
     */
    private void initScoreTable() {
        String createUsersTable = "CREATE TABLE BLACKJACK.SCORE ("
                + " ID INTEGER generated by default as identity (start with 1,"
                + " increment by 1) not null,"
                + " USERID INTEGER NOT NULL,"
                + "WINS INTEGER NOT NULL,"
                + " BALANCE INTEGER NOT NULL,"
                + "CONSTRAINT SCORE_PK_ID PRIMARY KEY (ID),"
                + "CONSTRAINT USERS_FK_USERID FOREIGN KEY (USERID)"
                + " REFERENCES BLACKJACK.USERS(ID))";
        createConnection();
        try {
            statement = connection.createStatement();
            statement.executeUpdate(createUsersTable);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        closeConnection();
        fillScore();
    }

    /**
     * if the score table wasn't exist, then we fill the table with some data
     */
    private void fillScore() {
        createConnection();
        String fillScoreTable = "INSERT INTO BLACKJACK.SCORE (USERID,WINS,BALANCE)"
                + " VALUES(?,?,?)";
        try {
            PreparedStatement stat
                    = connection.prepareStatement(fillScoreTable);
            stat.setInt(1, 1);
            stat.setInt(2, 20);
            stat.setInt(3, 14500);
            stat.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            PreparedStatement stat
                    = connection.prepareStatement(fillScoreTable);
            stat.setInt(1, 2);
            stat.setInt(2, 6);
            stat.setInt(3, 18550);
            stat.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            PreparedStatement stat
                    = connection.prepareStatement(fillScoreTable);
            stat.setInt(1, 3);
            stat.setInt(2, 3);
            stat.setInt(3, 8500);
            stat.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            PreparedStatement stat
                    = connection.prepareStatement(fillScoreTable);
            stat.setInt(1, 4);
            stat.setInt(2, 11);
            stat.setInt(3, 11111);
            stat.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            PreparedStatement stat
                    = connection.prepareStatement(fillScoreTable);
            stat.setInt(1, 5);
            stat.setInt(2, 24);
            stat.setInt(3, 25500);
            stat.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        closeConnection();
    }

    /**
     * if the users table wasn't exist, then we fill the table with some data
     */
    private void fillUsers() {
        PreparedStatement stat = null;
        createConnection();
        String fillUsersTable = "INSERT INTO BLACKJACK.USERS (FIRSTNAME,LASTNAME"
                + ",GENDER,USERNAME,PASSWORD,PERMISSION) VALUES(?,?,?,?,?,?)";
        try {
            stat = connection.prepareStatement(fillUsersTable);
            stat.setString(1, "Anael");
            stat.setString(2, "Shomrai");
            stat.setString(3, "Female");
            stat.setString(4, "anael");
            stat.setString(5, "123456");
            stat.setInt(6, 1);
            stat.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            stat = connection.prepareStatement(fillUsersTable);
            stat.setString(1, "דנה");
            stat.setString(2, "כהן");
            stat.setString(3, "נקבה");
            stat.setString(4, "דנה");
            stat.setString(5, "123456");
            stat.setInt(6, 2);
            stat.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            stat = connection.prepareStatement(fillUsersTable);
            stat.setString(1, "Idan");
            stat.setString(2, "Zalman");
            stat.setString(3, "Male");
            stat.setString(4, "idan");
            stat.setString(5, "123456");
            stat.setInt(6, 1);
            stat.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            stat = connection.prepareStatement(fillUsersTable);
            stat.setString(1, "Rotem");
            stat.setString(2, "Levi");
            stat.setString(3, "Female");
            stat.setString(4, "rotem");
            stat.setString(5, "123456");
            stat.setInt(6, 2);
            stat.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            stat = connection.prepareStatement(fillUsersTable, stat.RETURN_GENERATED_KEYS);
            stat.setString(1, "Nadav");
            stat.setString(2, "Zalman");
            stat.setString(3, "Male");
            stat.setString(4, "nadav");
            stat.setString(5, "123456");
            stat.setInt(6, 1);
            int result = stat.executeUpdate();
            if (result == 0) {
                System.out.println("Failed in inserting users");
            }
            ResultSet generatedKeys = stat.getGeneratedKeys();
            if (generatedKeys.next()) {
                lastId = generatedKeys.getInt(1);
            } else {
                System.out.println("NO ID OBTAINED");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        closeConnection();

    }

    /**
     * @return the DB instance, created only once
     */
    public static DB getInstance() {
        if (instance == null) {
            instance = new DB();
        }
        return instance;
    }

    /**
     * @return list of users
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Connect to the database
     */
    public void createConnection() {
        try {
            Class.forName(DBUtils.DB_DRIVER_CLASS);
            connection = DriverManager.getConnection(
                    DBUtils.DB_URL, DBUtils.DB_USER, DBUtils.DB_PASSWORD);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * close connection to database
     */
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                System.out.println("statement couldnt close");
            }
        } catch (SQLException e) {
            System.out.println("Connection didnt close");

        }
    }

    /**
     * This method add the specific user to the database, the list and saves
     * the id of the user inserted to the database.
     * 
     * @param u user to add to users table
     */
    public void addUser(User u) {
        PreparedStatement stat = null;
        String updateUser = "INSERT INTO BLACKJACK.USERS (FIRSTNAME,LASTNAME,GENDER"
                + ",USERNAME,PASSWORD,PERMISSION) VALUES (?,?,?,?,?,?)";
        createConnection();
        try {
            stat = connection.prepareStatement(updateUser, stat.RETURN_GENERATED_KEYS);
            stat.setString(1, u.getFirstName());
            stat.setString(2, u.getLastName());
            stat.setString(3, u.getGender());
            stat.setString(4, u.getUserName());
            stat.setString(5, u.getPassword());
            stat.setInt(6, u.getPermission());
            int result = stat.executeUpdate();
            if (result == 0) {
                System.out.println("Failed in inserting users");
            }
            ResultSet generatedKeys = stat.getGeneratedKeys();
            if (generatedKeys.next()) {
                lastId = generatedKeys.getInt(1);
            } else {
                System.out.println("NO ID OBTAINED");
            }
            u.setId(lastId);
            users.add(u);
        } catch (SQLException e) {
            System.out.println("error in add user");
            System.out.println(e.getSQLState());
            System.out.println(e.getCause());
        }
        this.createWinsNBalance(u.getId());
        closeConnection();
    }

    /**
     * Sets balance and wins to users list
     */
    public void initBalanceAndWins() {
        ResultSet resultSet = null;
        this.createConnection();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM BLACKJACK.SCORE");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("USERID");
                int wins = resultSet.getInt("WINS");
                int balance = resultSet.getInt("BALANCE");

                for (User u : users) {
                    if (id == u.getId()) {
                        u.setWins(wins);
                        u.setBalance(balance);
                    }
                }

            }
        } catch (SQLException e) {
            System.out.println("Error in initBalanceAndWins");
        }
        this.closeConnection();
    }

    /**
     * Create users list
     */
    public void initUsers() {
        ResultSet resultSet = null;
        this.createConnection();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM BLACKJACK.USERS");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String firstName = resultSet.getString("FIRSTNAME");
                String lastName = resultSet.getString("LASTNAME");
                String gender = resultSet.getString("GENDER");
                String userName = resultSet.getString("USERNAME");
                String password = resultSet.getString("PASSWORD");
                int permission = resultSet.getInt("PERMISSION");
                if (permission == 1) // admin
                {
                    Admin a = new Admin(id, firstName, lastName, gender, userName, password);
                    users.add(a);
                } else if (permission == 2) // user
                {
                    RegularUser ru = new RegularUser(id, firstName, lastName, gender, userName, password);
                    users.add(ru);
                }

            }
        } catch (SQLException e) {
            System.out.println("Error in init users");
        }
        this.closeConnection();
        initBalanceAndWins();

    }

    /**
     * This method update the list of users with data received,
     * then send to function to update the tables in the database
     * 
     * @param id id of the user
     * @param wins wins of the user
     * @param balance the balance
     * @see #updateUserInDB(int, int, int) 
     */
    public void updateUser(int id, int wins, int balance) {
        for (User u : users) {
            if (u.getId() == id) {
                u.setBalance(balance);
                u.setWins(wins);
            }
        }
        updateUserInDB(id, wins, balance);
    }

    /**
     * This method open a connection and update the database tables
     * 
     * @param userId the id of the user to update in the database
     * @param wins the wins of the user to update in the database
     * @param balance the balance of the user to update in the database
     */
    public void updateUserInDB(int userId, int wins, int balance) {
        PreparedStatement stat = null;
        String updateWinsNBalance = "UPDATE BLACKJACK.SCORE SET WINS = ?,"
                + "BALANCE = ? WHERE USERID = ?";
        this.createConnection();
        try {
            stat = connection.prepareStatement(updateWinsNBalance);
            stat.setInt(1, wins);
            stat.setInt(2, balance);
            stat.setInt(3, userId);
            int result = stat.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in insert score and wins");
            System.out.println(e.getSQLState());
            System.out.println(e.getCause());
        }
        this.closeConnection();
    }

    /**
     * This method create the initial field in score table for a new user
     * according to the given user id.
     * 
     * @param userId the id of the user
     */
    public void createWinsNBalance(int userId) {
        PreparedStatement stat = null;
        String updateUser = "INSERT INTO BLACKJACK.SCORE (USERID, WINS, BALANCE) "
                + "VALUES (?, 0, 10000)";
        createConnection();
        try {
            stat = connection.prepareStatement(updateUser);
            stat.setInt(1, userId);
            stat.executeUpdate();
        } catch (SQLException e) {
            System.out.println("error in on sign up");
            System.out.println(e.getSQLState());
            System.out.println(e.getCause());
        }

        closeConnection();
    }

    /**
     * This method checks if user exist according to the given user name and password
     * 
     * @param userName the user name to search
     * @param password the password to search
     * @return
     */
    public User login(String userName, String password) {
        User found = null;
        for (User u : users) {
            if (u.getUserName().equals(userName)
                    && u.getPassword().equals(password)) {
                found = u;
                break;
            }
        }
        return found;
    }

    /**
     * This method change the password of the user 
     * 
     * @param userId the id of the user that changing password
     * @param newPassword the new password that will be updated
     */
    public void changePassword(int userId, String newPassword) {
        PreparedStatement stat = null;
        String updatePassword
                = "UPDATE BLACKJACK.USERS SET PASSWORD = ? WHERE ID = ?";
        createConnection();
        try {
            stat = connection.prepareStatement(updatePassword);
            stat.setString(1, newPassword);
            stat.setInt(2, userId);
            stat.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getCause());
        }
        for (User u : users) {
            if (u.getId() == userId) {
                u.setPassword(newPassword);
            }
        }

        closeConnection();
    }

    /**
     * This method remove the user received from the data base and users list
     * 
     * @param u the user to delete
     */
    public void removeAccount(User u) {
        PreparedStatement stat = null;
        createConnection();
        try {
            users.remove(u);
            String deleteUser = "DELETE FROM BLACKJACK.SCORE WHERE USERID = ?";
            stat = connection.prepareStatement(deleteUser);
            stat.setInt(1, u.getId());
            stat.executeUpdate();
            deleteUser = "DELETE FROM BLACKJACK.USERS WHERE ID = ?";
            stat = connection.prepareStatement(deleteUser);
            stat.setInt(1, u.getId());
            stat.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getCause());
        }
        closeConnection();

    }

    /**
     * This method search a user by his id
     * if found, return the user.
     * else, return null
     * 
     * @param id id of user to search
     * @return the user if found, null if he doesn't exist
     */
    public User findUserById(int id) {
        for (User u : users) {
            if (u.getId() == id) {
                return u;
            }
        }
        return null;
    }

    /**
     * This method changing the permission field for the id received.
     * This can be done only from an admin,
     * who change a regular user to an admin
     * 
     * @param id the if of the user for changing his permission
     */
    public void changePermission(int id) {
        PreparedStatement stat = null;
        String changePermission = "UPDATE BLACKJACK.USERS SET PERMISSION = 1 WHERE ID = ?";
        this.createConnection();
        try {
            stat = connection.prepareStatement(changePermission);
            stat.setInt(1, id);
            stat.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error in  change permission");
            System.out.println(e.getSQLState());
            System.out.println(e.getCause());
        }
        this.closeConnection();
        for (User u : users) {
            if (u.getId() == id) {
                u.setPermission(1);
                break;
            }
        }

    }
    
    /**
     * This method received a user name and checks if it is
     * already take, or too short/long.
     * String UserNameTaken - if the user name already taken
     * String UserNameLegnth - if the user name is too short/long
     * String "" (empty) - if the user name is good to go
     * 
     * @param userName user name that will be checked
     * @return an empty string if user name avaliable
     * or a string with the error
     */
    public String checkUserName(String userName) {
        for (User u : users) {
            if (u.getUserName().equals(userName)) {
                return "UserNameTaken";
            }
        }
        if (userName.length() > 20 || userName.length() < 4) {
            return "UserNameLegnth";
        } else if (userName.isEmpty()) {
            return "Empty";
        }
        return "";
    }

    /**
     * This method check if a user exist according to the received details
     * if exist, return the user.
     * else, return null;
     * 
     * @param user index[0] is the user name, index[1] is the password
     * @return the user if found, null if doesn't exist
     */
    public User checkUserExist(String[] user) {
        User found = null;
        for (User u : users) {
            if (u.getUserName().equals(user[0]) //username
                    && u.getPassword().equals(user[1])) { //password
                found = u;
                break;
            }
        }
        return found;
    }

    /**
     *
     * @return a list of the class Score
     * @see DataUtil.Score
     */
    public List<Score> getAllScores() {
        List<Score> scores = new ArrayList<Score>();
        String sql = "SELECT * FROM BLACKJACK.SCORE";

        ResultSet resultSet = null;
        this.createConnection();
        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            while (resultSet.next()) {
                int id = resultSet.getInt("USERID");
                int wins = resultSet.getInt("WINS");
                int balance = resultSet.getInt("BALANCE");

                String userName = "";
                for (User u : users) {
                    if (u.getId() == id) {
                        userName = u.getUserName();
                        break;
                    }
                }

                scores.add(new Score(userName, wins, balance));

            }
        } catch (SQLException e) {
            System.out.println("Error in init users");
        }
        this.closeConnection();
        return scores;
    }

}