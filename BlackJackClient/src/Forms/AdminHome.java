package Forms;

import Resources.LocalizationUtil;
import Users.Admin;
import Users.User;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import javax.swing.JMenu;

/**
 * This class is the admin version of user home. the form display in addition of
 * the user options, an admin option, that allows the admin to remove players,
 * or promote a regular user to be an admin.
 *
 * @author ANI
 */
public class AdminHome extends UserHome {

    JMenu mnuAdminOptions = new JMenu();
    boolean isHebrew = false;

    /**
     * This constructor initializes an AdminHome frame.
     *
     * @param player the user who logged in
     * @param previous the previous form
     */
    public AdminHome(User player, WelcomeScreen previous) {
        super(player, previous);
        initComponents();
        initMyComponents();
    }

    /**
     * This constructor initializes an AdminHome frame in Hebrew.
     *
     * @param player the user who logged in
     * @param previous the previous form
     * @param lang the language selected
     */
    public AdminHome(User player, WelcomeScreen previous, String lang) {
        super(player, previous, lang);
        initComponents();
        initMyComponents();
        this.language = lang;
        if (lang.equals("iw")) {
            LocalizationUtil.localizedResourceBundle
                    = LocalizationUtil.getBundleHomeIW();
            mnuAdminOptions.setText(LocalizationUtil.localizedResourceBundle.getString("mnuAdminOptions"));
            super.changeToHebrew();
        }
    }

    /**
     * This method initialize new components. a new menu item, admin options.
     * and the listener for the menu item.
     */
    private void initMyComponents() {
        addListeners();
        mnuAdminOptions.setText("Admin Options");
        mnuAdminOptions.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mnuAdminOptions.setToolTipText("Click for more options");
        menuBar.add(mnuAdminOptions);
        setJMenuBar(menuBar);
    }

    /**
     * This method add listener for the admin options, in case the admin click
     * on this option.
     */
    private void addListeners() {
        mnuAdminOptions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuAdminOptionsMouseClicked(evt);
            }
        });

    }

    /**
     * This method is triggered when the user clicked on admin options.
     */
    private void mnuAdminOptionsMouseClicked(MouseEvent evt) {
        this.setVisible(false);
        ListOfUsers listOfUsers;
        if (this.language.equals("iw")) {
            listOfUsers = new ListOfUsers(this, (Admin) player, this.language);
        } else {
            listOfUsers = new ListOfUsers(this, (Admin) player);
        }
        listOfUsers.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 673, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 356, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(632, 435));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
