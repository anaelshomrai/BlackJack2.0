package DataBase;

/**
 * This class contains constant variables that used to connect to the database.
 * 
 * @author ANI
 */
public class DBUtils {
    /**
     * the URL address to connect to the database, if database isn't exist
     * then create one.
     */
    public static final String DB_URL
            = "jdbc:derby://localhost:1527/BLACKJACK;create=true";

    /**
     * the driver class for connecting derby database.
     */
    public static final String DB_DRIVER_CLASS
            = "org.apache.derby.jdbc.ClientDriver";

    /**
     * the user name for connecting the database.
     */
    public static final String DB_USER = "blackjack";

    /**
     * the password for connection the database.
     */
    public static final String DB_PASSWORD = "blackjack";
}
