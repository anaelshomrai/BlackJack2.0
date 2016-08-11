package Forms;

import Resources.GameUtil;
import Resources.LocalizationUtil;
import Users.User;
import blackjackclient.ConnectionUtil;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * This class is the the form that shows when a user log in. in this from user
 * can read the game rules, play a game online, play a game verses the dealer,
 * see information about his account, changing his password and removing his
 * account.
 *
 * @author ANI
 */
public class UserHome extends javax.swing.JFrame {

    User player = null;
    WelcomeScreen previous = null;
    String language = "";

    /**
     * This constructor initializes an AdminHome frame.
     */
    public UserHome() {
        initComponents();
        GameUtil.setIcon(this);
    }

    /**
     * This constructor initializes an AdminHome frame. Assigns the previous
     * form then call the default constructor.
     *
     * @param previous the previous form
     */
    public UserHome(WelcomeScreen previous) {
        this();
        this.previous = previous;
    }

    /**
     * This constructor initializes an AdminHome frame. Assigns the user, the
     * welcome label and call the next constructor.
     *
     * @param player the user
     * @param previous the previous form
     */
    public UserHome(User player, WelcomeScreen previous) {
        this(previous);
        this.player = player;
        labWelcomeUser.setText(labWelcomeUser.getText() + player.getFirstName()
                + " " + player.getLastName());
    }

    /**
     * This constructor initializes an AdminHome frame. Assigns the language
     * selected and call the next constructor.
     *
     * @param player the user
     * @param previous the previous form
     * @param lang the language selected
     */
    public UserHome(User player, WelcomeScreen previous, String lang) {
        this(player, previous);
        this.language = lang;
        if (lang.equals("iw")) {
            changeToHebrew();
        }
    }

    /**
     * if Hebrew selected, then we change the language of the form.
     */
    public void changeToHebrew() {
        LocalizationUtil.localizedResourceBundle
                = LocalizationUtil.getBundleHomeIW();
        updateCaptions();
        LocalizationUtil.changeOptionPane_iw();
    }

    /**
     * This method change all the components in the form to Hebrew.
     */
    public void updateCaptions() {
        btnPlayOnline.setText(LocalizationUtil.localizedResourceBundle
                .getString("btnPlayOnline"));
        btnPlayVsDealer.setText(LocalizationUtil.localizedResourceBundle
                .getString("btnPlay"));
        btnScoreTable.setText(LocalizationUtil.localizedResourceBundle
                .getString("btnScoreTable"));
        labWelcomeUser.setText(LocalizationUtil.localizedResourceBundle
                .getString("labWelcomeUser"));
        labWelcomeUser.setText(labWelcomeUser.getText() + player
                .getFirstName() + " " + player.getLastName());
        mnuAccount.setText(LocalizationUtil.localizedResourceBundle
                .getString("mnuAccount"));
        mnuItmAbout.setText(LocalizationUtil.localizedResourceBundle
                .getString("mnuItmAbout"));
        mnuItmChangePassword.setText(LocalizationUtil.localizedResourceBundle
                .getString("mnuItmChangePassword"));
        mnuItmRemoveAccount.setText(LocalizationUtil.localizedResourceBundle
                .getString("mnuItmRemoveAccount"));
        mnuItemGameRules.setText(LocalizationUtil.localizedResourceBundle
                .getString("mnuItemGameRules"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnPlayVsDealer = new javax.swing.JButton();
        btnScoreTable = new javax.swing.JButton();
        labWelcomeUser = new javax.swing.JLabel();
        labBack = new javax.swing.JLabel();
        btnPlayOnline = new javax.swing.JButton();
        labBackground = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        mnuAccount = new javax.swing.JMenu();
        mnuItmAbout = new javax.swing.JMenuItem();
        mnuItmChangePassword = new javax.swing.JMenuItem();
        mnuItmRemoveAccount = new javax.swing.JMenuItem();
        mnuItemGameRules = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        btnPlayVsDealer.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPlayVsDealer.setText("Play Vs Dealer");
        btnPlayVsDealer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPlayVsDealer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayVsDealerActionPerformed(evt);
            }
        });
        getContentPane().add(btnPlayVsDealer);
        btnPlayVsDealer.setBounds(20, 170, 200, 30);

        btnScoreTable.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnScoreTable.setText("Show High Score");
        btnScoreTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnScoreTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnScoreTableActionPerformed(evt);
            }
        });
        getContentPane().add(btnScoreTable);
        btnScoreTable.setBounds(20, 220, 200, 30);

        labWelcomeUser.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        labWelcomeUser.setForeground(new java.awt.Color(255, 255, 255));
        labWelcomeUser.setText("Welcome ");
        labWelcomeUser.setToolTipText("");
        getContentPane().add(labWelcomeUser);
        labWelcomeUser.setBounds(50, 50, 290, 30);

        labBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        labBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labBackMouseClicked(evt);
            }
        });
        getContentPane().add(labBack);
        labBack.setBounds(10, 280, 64, 70);

        btnPlayOnline.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        btnPlayOnline.setText("Play Online");
        btnPlayOnline.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnPlayOnline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPlayOnlineActionPerformed(evt);
            }
        });
        getContentPane().add(btnPlayOnline);
        btnPlayOnline.setBounds(20, 120, 200, 30);

        labBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cardBackground.jpg"))); // NOI18N
        labBackground.setMaximumSize(null);
        labBackground.setMinimumSize(null);
        labBackground.setPreferredSize(null);
        getContentPane().add(labBackground);
        labBackground.setBounds(0, -30, 632, 435);

        menuBar.setToolTipText("");

        mnuAccount.setBackground(new java.awt.Color(0, 0, 0));
        mnuAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/account.png"))); // NOI18N
        mnuAccount.setText("My Account");
        mnuAccount.setToolTipText("Click for account options");
        mnuAccount.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mnuAccount.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N

        mnuItmAbout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/about.png"))); // NOI18N
        mnuItmAbout.setText("Profile");
        mnuItmAbout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mnuItmAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItmAboutActionPerformed(evt);
            }
        });
        mnuAccount.add(mnuItmAbout);

        mnuItmChangePassword.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/password.png"))); // NOI18N
        mnuItmChangePassword.setText("Change Password");
        mnuItmChangePassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mnuItmChangePassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItmChangePasswordActionPerformed(evt);
            }
        });
        mnuAccount.add(mnuItmChangePassword);

        mnuItmRemoveAccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/removeAccount.png"))); // NOI18N
        mnuItmRemoveAccount.setText("Remove My Account");
        mnuItmRemoveAccount.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mnuItmRemoveAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItmRemoveAccountActionPerformed(evt);
            }
        });
        mnuAccount.add(mnuItmRemoveAccount);

        mnuItemGameRules.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/info-round-button_1.png"))); // NOI18N
        mnuItemGameRules.setText("Game Rules");
        mnuItemGameRules.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        mnuItemGameRules.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemGameRulesActionPerformed(evt);
            }
        });
        mnuAccount.add(mnuItemGameRules);

        menuBar.add(mnuAccount);

        setJMenuBar(menuBar);

        setSize(new java.awt.Dimension(632, 435));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Return the menu bar used in the admin home, for adding the new option.
     *
     * @return the menu bar
     */
    public JMenuBar getMenu() {
        return menuBar;
    }

    /**
     * When play verses dealer button is pressed, we hides this form, and
     * initialize a new Game offline.
     */
    private void btnPlayVsDealerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayVsDealerActionPerformed
        this.setVisible(false);
        Game game;
        if (this.language.equals("iw")) {
            game = new Game(player, this, this.language);
        } else {
            game = new Game(player, this);
        }
        game.initializeGUI();
    }//GEN-LAST:event_btnPlayVsDealerActionPerformed

    /**
     * When this button is clicked, we hides this form, and initialize a new
     * Game offline.
     */
    private void btnScoreTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnScoreTableActionPerformed
        this.setVisible(false);
        ScoreTable scoreTable;
        if (this.language.equals("iw")) {
            scoreTable = new ScoreTable(this, player, this.language);
        } else {
            scoreTable = new ScoreTable(this, player);
        }
        scoreTable.setVisible(true);
    }//GEN-LAST:event_btnScoreTableActionPerformed

    /**
     * When this button is clicked, a JOptionPane is presented, the user must
     * enter the old password, and the new password. if the old password was a
     * match, and the new password passed the validation rules, the password
     * changed, this frame disposed in order to login again with the new
     * password.
     */
    private void mnuItmChangePasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItmChangePasswordActionPerformed
        JTextField oldPassword = new JTextField();
        JTextField newPassword = new JPasswordField();
        Object[] message = {
            "Old Password:", oldPassword,
            "New Password:", newPassword
        };

        if (this.language.equals("iw")) {
            LocalizationUtil.changePaswword_iw(message);
            if ((newPassword.getText()).length() < 6
                    || newPassword.getText().length() > 20) {
                JOptionPane.showMessageDialog(null, "סיסמא לא חוקית! אורך סיסמא הוא מינמום 6 תווים, מקסימום 20 תווים");
            } else if (player.changePassword(oldPassword.getText(),
                    newPassword.getText())) {
                JOptionPane.showMessageDialog(null, "הסיסמא שונתה בהצלחה!");
                this.dispose();
                previous.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, "הסיסמא שגויה!");
            }
        } else {
            int option = JOptionPane.showConfirmDialog(null, message, "Change Password",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (option == JOptionPane.OK_OPTION) {
                if ((newPassword.getText()).length() < 6
                        || newPassword.getText().length() > 20) {
                    JOptionPane.showMessageDialog(null, "Invalid password (up to 20 letters, minimum 6)");
                } else if (player.changePassword(oldPassword.getText(), newPassword.getText())) {
                    JOptionPane.showMessageDialog(null, "Password Changed Successfully");
                    this.dispose();
                    previous.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Incorecct Password!");
                }
            }
        }
    }//GEN-LAST:event_mnuItmChangePasswordActionPerformed
    /**
     * When the user try to exit the application this method is triggered. the
     * user must confirm the exit, then the we exit the application.
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int confirmed;
        if (this.language.equals("iw")) {
            confirmed = LocalizationUtil.exitDialog();
        } else {
            confirmed = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to exit the program?", "Exit Program",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        }
        if (confirmed == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

    }//GEN-LAST:event_formWindowClosing
    /**
     * When this button is clicked, a JOptionPane with the user details is
     * presented.
     */
    private void mnuItmAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItmAboutActionPerformed

        if (this.language.equals("iw")) {
            JOptionPane.showMessageDialog(null,
                    MyJOptionPane.getProfileIW(player),
                    "Profile",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null,
                    MyJOptionPane.getProfileEN(player),
                    "Profile",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_mnuItmAboutActionPerformed
    /**
     * When this button is clicked, the user need to confirm that he is deleting
     * his account.
     */
    private void mnuItmRemoveAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItmRemoveAccountActionPerformed
        int confirmed;

        if (this.language.equals("iw")) {
            confirmed = JOptionPane.showConfirmDialog(null,
                    "האם אתה בטוח שברצונך למחוק את החשבון?", "מחיקת חשבון",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        } else {
            confirmed = JOptionPane.showConfirmDialog(null,
                    "Are you sure you want to remove your account?",
                    "Remove Account",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        }

        if (confirmed == JOptionPane.YES_OPTION) {
            if (this.language.equals("iw")) {
                JOptionPane.showMessageDialog(null,
                        "מקווים לראותך שוב בקרוב!!");
            } else {
                JOptionPane.showMessageDialog(null,
                        "Hope to see you soon!");
            }
            player.removeAccount(player);
            this.dispose();
            previous.setVisible(true);
        }
    }//GEN-LAST:event_mnuItmRemoveAccountActionPerformed
    /**
     * if back is clicked we goes back to the previous frame.
     */
    private void labBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labBackMouseClicked
        this.dispose();
        previous.setVisible(true);
    }//GEN-LAST:event_labBackMouseClicked
    /**
     * When this button is clicked, a new frame with the game rules is
     * presented, with the chosen language.
     */
    private void mnuItemGameRulesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemGameRulesActionPerformed
        if (this.language.equals("iw")) {
            new TableRules(language).setVisible(true);
        } else {
            new TableRules().setVisible(true);
        }
    }//GEN-LAST:event_mnuItemGameRulesActionPerformed
    /**
     * When the user chose to play online we hide this frame, and initialize new
     * game frame.
     */
    private void btnPlayOnlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPlayOnlineActionPerformed

        this.setVisible(false);

        GameOnline game = null;
        game = new GameOnline(player, this, this.language);

    }//GEN-LAST:event_btnPlayOnlineActionPerformed

    /**
     * This method receive the user to update in the dataBase after the game
     * finished.
     *
     * @param user the user to update in the dataBase.
     */
    public void finishUpdatingGame(User user) {
        ConnectionUtil myConnection = new ConnectionUtil();
        myConnection.openConnection();
        myConnection.exitGameByForce(player);
        myConnection.closeConnection();
        player.update();
    }

    /**
     * This method takes out the user from the waiting queue to a game, this
     * method is called if a player exit in a middle of a game or if the player
     * forced out of a game.
     *
     */
    public void showExitDialog() {
        JOptionPane.showMessageDialog(this, "There isn't enought players in the game!\nTry again later or play vs dealer\n");
        ConnectionUtil myConnection = new ConnectionUtil();
        myConnection.openConnection();
        myConnection.exitGameByForce(player);
        myConnection.closeConnection();

    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                UserHome frame = new UserHome();
//                frame.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton btnPlayOnline;
    protected javax.swing.JButton btnPlayVsDealer;
    protected javax.swing.JButton btnScoreTable;
    protected javax.swing.JLabel labBack;
    protected javax.swing.JLabel labBackground;
    protected javax.swing.JLabel labWelcomeUser;
    protected javax.swing.JMenuBar menuBar;
    protected javax.swing.JMenu mnuAccount;
    protected javax.swing.JMenuItem mnuItemGameRules;
    protected javax.swing.JMenuItem mnuItmAbout;
    protected javax.swing.JMenuItem mnuItmChangePassword;
    protected javax.swing.JMenuItem mnuItmRemoveAccount;
    // End of variables declaration//GEN-END:variables
}
