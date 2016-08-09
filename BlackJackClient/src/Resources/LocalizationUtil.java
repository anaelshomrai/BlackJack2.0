package Resources;

import java.awt.Color;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

/**
 *
 * @author ANI
 */
public class LocalizationUtil {

    public static ResourceBundle localizedResourceBundle;

    public static void setLocalizedResourceBundle(ResourceBundle localizedResourceBundle) {
        localizedResourceBundle = localizedResourceBundle;
    }

    public static ResourceBundle getBundleWelcomeScreenEN() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiWelcomeScreen");
        return localizedResourceBundle;
    }

    public static ResourceBundle getBundleWelcomeScreenIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiWelcomeScreen_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    public static ResourceBundle getBundleSignUpScreenEN() {
        localizedResourceBundle = ResourceBundle.getBundle("UiSignUp");
        return localizedResourceBundle;
    }

    public static ResourceBundle getBundleSignUpScreenIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiSignUp_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    public static ResourceBundle getBundleHomeIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiHome_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    public static ResourceBundle getBundleListOfUsersIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiListOfUsers_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    public static ResourceBundle getBundleScoreTableIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiScoreTable_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    public static ResourceBundle getBundleGameIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiGame_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    public static ResourceBundle getBundleInputValidation() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiInputValidation");
        return localizedResourceBundle;
    }

    public static ResourceBundle getBundleInputValidationIW() {
        localizedResourceBundle = ResourceBundle
                .getBundle("Resources.UiInputValidation_iw", new Locale("iw"));
        return localizedResourceBundle;
    }

    public static void changeOptionPane_iw() {
        ResourceBundle bundle = ResourceBundle
                .getBundle("Resources.JOptionPane_iw", new Locale("iw"));
        UIManager.put("OptionPane.yesButtonText", bundle.getString("Yes"));
        UIManager.put("OptionPane.noButtonText", bundle.getString("No"));
        UIManager.put("OptionPane.cancelButtonText", bundle.getString("Cancel"));
        UIManager.put("OptionPane.okButtonText", bundle.getString("Ok"));
    }

    public static void changeOptionPane_en() {
        ResourceBundle bundle = ResourceBundle
                .getBundle("Resources.JOptionPane_iw", new Locale("iw"));
        UIManager.put("OptionPane.yesButtonText", "Yes");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.cancelButtonText", "Cancel");
        UIManager.put("OptionPane.okButtonText", "Ok");
    }

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

    public static int exitDialog() {
//        UIManager.put("OptionPane.background", Color.CYAN);
//         UIManager.put("Panel.background", Color.CYAN);

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
