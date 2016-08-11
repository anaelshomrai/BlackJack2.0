package Resources;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 * This class handle with the game language. this class sets and received the
 * properties file according to the selected language.
 *
 * @author ANI
 */
public class LocalizationUtil {

    /**
     * the resource Bundle
     */
    public static ResourceBundle localizedResourceBundle;

    /**
     * Sets the resource bundle
     *
     * @param localizedResourceBundle the resource bundle to set
     */
    public static void setLocalizedResourceBundle(ResourceBundle localizedResourceBundle) {
        LocalizationUtil.localizedResourceBundle = localizedResourceBundle;
    }

    /**
     * Returns the ResourceBundle of the welcome screen in English.
     *
     * @return the ResourceBundle
     */
    public static ResourceBundle getBundleWelcomeScreenEN() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiWelcomeScreen");
        return localizedResourceBundle;
    }

    /**
     * Returns the ResourceBundle of the welcome screen in Hebrew.
     *
     * @return the ResourceBundle
     */
    public static ResourceBundle getBundleWelcomeScreenIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiWelcomeScreen_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    /**
     * Returns the ResourceBundle of the signUpScreen in English.
     *
     * @return the ResourceBundle
     */
    public static ResourceBundle getBundleSignUpScreenEN() {
        localizedResourceBundle = ResourceBundle.getBundle("UiSignUp");
        return localizedResourceBundle;
    }

    /**
     * Returns the ResourceBundle of the signUpScreen in Hebrew.
     *
     * @return the ResourceBundle
     */
    public static ResourceBundle getBundleSignUpScreenIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiSignUp_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    /**
     * Returns the ResourceBundle of the userHome in Hebrew.
     *
     * @return the ResourceBundle
     */
    public static ResourceBundle getBundleHomeIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiHome_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    /**
     * Returns the ResourceBundle of the ListOfUsers in Hebrew.
     *
     * @return the ResourceBundle
     */
    public static ResourceBundle getBundleListOfUsersIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiListOfUsers_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    /**
     * Returns the ResourceBundle of the score Table in Hebrew.
     *
     * @return the ResourceBundle
     */
    public static ResourceBundle getBundleScoreTableIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiScoreTable_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    /**
     * Returns the ResourceBundle of the game in Hebrew.
     *
     * @return the ResourceBundle
     */
    public static ResourceBundle getBundleGameIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiGame_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    /**
     * Returns the ResourceBundle of the inputValidation warning labels in
     * English.
     *
     * @return the ResourceBundle
     */
    public static ResourceBundle getBundleInputValidation() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiInputValidation");
        return localizedResourceBundle;
    }

    /**
     * Returns the ResourceBundle of the inputValidation warning labels in
     * Hebrew.
     *
     * @return the ResourceBundle
     */
    public static ResourceBundle getBundleInputValidationIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiInputValidation_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    /**
     * This method change the JOption pane to Hebrew.
     */
    public static void changeOptionPane_iw() {
        ResourceBundle bundle = ResourceBundle
                .getBundle("Resources.JOptionPane_iw", new Locale("iw"));
        UIManager.put("OptionPane.yesButtonText", bundle.getString("Yes"));
        UIManager.put("OptionPane.noButtonText", bundle.getString("No"));
        UIManager.put("OptionPane.cancelButtonText", bundle.getString("Cancel"));
        UIManager.put("OptionPane.okButtonText", bundle.getString("Ok"));
    }

    /**
     * This method change the JOption pane to English.
     */
    public static void changeOptionPane_en() {
        ResourceBundle bundle = ResourceBundle
                .getBundle("Resources.JOptionPane_iw", new Locale("iw"));
        UIManager.put("OptionPane.yesButtonText", "Yes");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.cancelButtonText", "Cancel");
        UIManager.put("OptionPane.okButtonText", "Ok");
    }

    /**
     *
     * @param message the object to insert to the JOptionPane
     * @return true if OK was chosen, false if CACNCEL was chosen.
     */
    public static boolean changePaswword_iw(Object[] message) {

        changeOptionPane_iw();
        message[0] = "סיסמא קודמת";
        message[2] = "סיסמא חדשה";
        int option = JOptionPane.showConfirmDialog(null, message, "שינוי סיסמא",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * Returns the value of Yes Option or the Cancel Option according to the user
     * choice.
     * 
     * @return a number representing the option chosen.
     */
    public static int exitDialog() {
        int confirmed;
        confirmed = JOptionPane.showConfirmDialog(null,
                "האם אתה בטוח שאתה רוצה לצאת?", "יציאה מהתוכנית",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmed == JOptionPane.YES_OPTION) {
            return JOptionPane.YES_OPTION;
        } else {
            return JOptionPane.CANCEL_OPTION;
        }

    }
}
