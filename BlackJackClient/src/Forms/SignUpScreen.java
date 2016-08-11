package Forms;

import Resources.GameUtil;
import Resources.InputValidation;
import Resources.LocalizationUtil;
import Resources.RoundedBorder;
import Users.RegularUser;
import blackjackclient.ConnectionUtil;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.io.IOException;
;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

/**
 * This class is the the form that shows when a user want to sign in.
 *
 * @author ANI
 */


public class SignUpScreen extends javax.swing.JFrame {

    WelcomeScreen previous = null;
    private boolean[] inputValidation = new boolean[4];
    String language = "";
    boolean serverUp = true;

    /**
     * This constructor initializes a signUpScreen frame.
     */
    public SignUpScreen() {

        initComponents();
        initMyComponents();
        btnSignUp.setOpaque(false);
        btnSignUp.setContentAreaFilled(false);
        btnSignUp.setBorder(new RoundedBorder(50));
        btnSignUp.setForeground(Color.BLACK);
    }

    /**
     * This constructor initializes a signUpScreen frame. and sets the previous
     * form.
     *
     * @param previous the previous form.
     */
    public SignUpScreen(WelcomeScreen previous) {
        this();
        this.previous = previous;
        LocalizationUtil.setLocalizedResourceBundle(LocalizationUtil.getBundleInputValidation());
    }

    /**
     * This constructor initializes a signUpScreen frame. sets the previous form
     * and Hebrew as a language if selected.
     *
     * @param previous the previous form
     * @param language the language selected
     */
    public SignUpScreen(WelcomeScreen previous, String language) {
        this(previous);
        this.language = language;
        if (language.equals("iw")) {
            changeToHebrew();
            LocalizationUtil.setLocalizedResourceBundle(LocalizationUtil.getBundleInputValidationIW());
        }
    }

    /**
     * This Method change the look of the radio buttons, and sets the frame icon.
     */
    private void initMyComponents() {
        rdbFemale.setOpaque(false);
        rdbFemale.setContentAreaFilled(false);
        rdbFemale.setBorderPainted(false);

        rdbMale.setOpaque(false);
        rdbMale.setContentAreaFilled(false);
        rdbMale.setBorderPainted(false);

        GameUtil.setIcon(this);
    }

    /**
     * If Hebrew selected we change the language of the frame.
     */
    public final void changeToHebrew() {
        LocalizationUtil.localizedResourceBundle
                = ResourceBundle.getBundle("Resources.UiSignUp_iw", new Locale("iw"));
        updateCaptions();
        initHebrew();
        LocalizationUtil.changeOptionPane_iw();
    }

    /**
     * If Hebrew selected we change the direction to RTL.
     */
    public void initHebrew() {
        rdbFemale.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        rdbFemale.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        rdbMale.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        rdbMale.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        labFirstName.setBounds(280, 160, 90, 20);
        labLastName.setBounds(280, 200, 90, 20);
        labGender.setBounds(280, 240, 90, 20);
        labUserName.setBounds(280, 280, 90, 20);
        labPassword.setBounds(280, 320, 90, 20);
        labFirstName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labLastName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labGender.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labUserName.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        labPassword.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        txtFirstName.setHorizontalAlignment(SwingConstants.RIGHT);
        txtLastName.setHorizontalAlignment(SwingConstants.RIGHT);
        txtUserName.setHorizontalAlignment(SwingConstants.RIGHT);
        txtPassword.setHorizontalAlignment(SwingConstants.RIGHT);
        labValidFirstName.setHorizontalAlignment(SwingConstants.CENTER);
        labValidLastName.setHorizontalAlignment(SwingConstants.CENTER);
        labValidUserName.setHorizontalAlignment(SwingConstants.CENTER);
        labValidPassword.setHorizontalAlignment(SwingConstants.CENTER);

    }

    /**
     * This method change the language of the frame.
     */
    public void updateCaptions() {
        labTitle.setText(LocalizationUtil.localizedResourceBundle
                .getString("labTitle"));
        labUserName.setText(LocalizationUtil.localizedResourceBundle
                .getString("labUserName"));
        labPassword.setText(LocalizationUtil.localizedResourceBundle
                .getString("labPassword"));
        labGender.setText(LocalizationUtil.localizedResourceBundle
                .getString("labGender"));
        btnSignUp.setText(LocalizationUtil.localizedResourceBundle
                .getString("btnSignUp"));
        rdbFemale.setText(LocalizationUtil.localizedResourceBundle
                .getString("rdbFemale"));
        rdbMale.setText(LocalizationUtil.localizedResourceBundle
                .getString("rdbMale"));
        labFirstName.setText(LocalizationUtil.localizedResourceBundle
                .getString("labFirstName"));
        labLastName.setText(LocalizationUtil.localizedResourceBundle
                .getString("labLastName"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroupGender = new javax.swing.ButtonGroup();
        labTitle = new javax.swing.JLabel();
        labLastName = new javax.swing.JLabel();
        labFirstName = new javax.swing.JLabel();
        labGender = new javax.swing.JLabel();
        labUserName = new javax.swing.JLabel();
        labPassword = new javax.swing.JLabel();
        txtFirstName = new javax.swing.JTextField();
        txtLastName = new javax.swing.JTextField();
        txtUserName = new javax.swing.JTextField();
        txtPassword = new javax.swing.JTextField();
        rdbFemale = new javax.swing.JRadioButton();
        rdbMale = new javax.swing.JRadioButton();
        btnSignUp = new javax.swing.JButton();
        labSignture = new javax.swing.JLabel();
        labValidFirstName = new javax.swing.JLabel();
        labValidLastName = new javax.swing.JLabel();
        labValidUserName = new javax.swing.JLabel();
        labBack = new javax.swing.JLabel();
        labValidPassword = new javax.swing.JLabel();
        labBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("BlackJack ANI");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        labTitle.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        labTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labTitle.setText("Sign Up");
        getContentPane().add(labTitle);
        labTitle.setBounds(100, 50, 240, 70);

        labLastName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labLastName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labLastName.setText("Last Name:");
        getContentPane().add(labLastName);
        labLastName.setBounds(60, 200, 90, 17);

        labFirstName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labFirstName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labFirstName.setText("First Name:");
        getContentPane().add(labFirstName);
        labFirstName.setBounds(60, 160, 90, 17);

        labGender.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labGender.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labGender.setText("Gender:");
        getContentPane().add(labGender);
        labGender.setBounds(60, 240, 70, 17);

        labUserName.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labUserName.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labUserName.setText("User Name:");
        getContentPane().add(labUserName);
        labUserName.setBounds(60, 280, 90, 17);

        labPassword.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        labPassword.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labPassword.setText("Password:");
        getContentPane().add(labPassword);
        labPassword.setBounds(60, 320, 80, 17);

        txtFirstName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtFirstNameKeyReleased(evt);
            }
        });
        getContentPane().add(txtFirstName);
        txtFirstName.setBounds(160, 160, 130, 23);

        txtLastName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtLastNameKeyReleased(evt);
            }
        });
        getContentPane().add(txtLastName);
        txtLastName.setBounds(160, 200, 130, 23);

        txtUserName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtUserNameKeyReleased(evt);
            }
        });
        getContentPane().add(txtUserName);
        txtUserName.setBounds(160, 280, 130, 23);

        txtPassword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPasswordKeyReleased(evt);
            }
        });
        getContentPane().add(txtPassword);
        txtPassword.setBounds(160, 320, 130, 23);

        btnGroupGender.add(rdbFemale);
        rdbFemale.setText("Female");
        rdbFemale.setHideActionText(true);
        rdbFemale.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        getContentPane().add(rdbFemale);
        rdbFemale.setBounds(150, 240, 70, 23);

        btnGroupGender.add(rdbMale);
        rdbMale.setText("Male");
        getContentPane().add(rdbMale);
        rdbMale.setBounds(230, 240, 60, 23);

        btnSignUp.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        btnSignUp.setText("Sign Up");
        btnSignUp.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSignUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSignUpActionPerformed(evt);
            }
        });
        getContentPane().add(btnSignUp);
        btnSignUp.setBounds(130, 370, 170, 40);

        labSignture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/signature.png"))); // NOI18N
        getContentPane().add(labSignture);
        labSignture.setBounds(360, 10, 64, 70);

        labValidFirstName.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        labValidFirstName.setForeground(java.awt.Color.red);
        labValidFirstName.setMaximumSize(new java.awt.Dimension(7, 15));
        labValidFirstName.setMinimumSize(new java.awt.Dimension(7, 60));
        labValidFirstName.setPreferredSize(new java.awt.Dimension(7, 15));
        getContentPane().add(labValidFirstName);
        labValidFirstName.setBounds(120, 185, 280, 14);

        labValidLastName.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        labValidLastName.setForeground(java.awt.Color.red);
        labValidLastName.setMaximumSize(new java.awt.Dimension(7, 15));
        labValidLastName.setMinimumSize(new java.awt.Dimension(7, 60));
        labValidLastName.setPreferredSize(new java.awt.Dimension(7, 15));
        getContentPane().add(labValidLastName);
        labValidLastName.setBounds(120, 225, 280, 14);

        labValidUserName.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        labValidUserName.setForeground(java.awt.Color.red);
        labValidUserName.setMaximumSize(new java.awt.Dimension(7, 15));
        labValidUserName.setMinimumSize(new java.awt.Dimension(7, 60));
        labValidUserName.setPreferredSize(new java.awt.Dimension(7, 15));
        getContentPane().add(labValidUserName);
        labValidUserName.setBounds(105, 305, 315, 14);

        labBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back2.png"))); // NOI18N
        labBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labBackMouseClicked(evt);
            }
        });
        getContentPane().add(labBack);
        labBack.setBounds(10, 450, 64, 64);

        labValidPassword.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        labValidPassword.setForeground(java.awt.Color.red);
        labValidPassword.setMaximumSize(new java.awt.Dimension(7, 15));
        labValidPassword.setMinimumSize(new java.awt.Dimension(7, 60));
        labValidPassword.setPreferredSize(new java.awt.Dimension(7, 15));
        getContentPane().add(labValidPassword);
        labValidPassword.setBounds(105, 345, 315, 14);

        labBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/signupback_1.png"))); // NOI18N
        getContentPane().add(labBackground);
        labBackground.setBounds(0, 0, 440, 540);

        setSize(new java.awt.Dimension(445, 565));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    /**
     * When the user clicks finish we check if every boolean is ture for each of
     * the input needed to be checked. if one is false, a JOptionPane is presented.
     * else, we send the information to the server, destroy this frame and go 
     * back to the previous frame.
     */
    private void btnSignUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSignUpActionPerformed
        for (boolean b : inputValidation) {
            if (b == false) {
                if (this.language.equals("iw")) {
                    JOptionPane.showMessageDialog(null, "השדות אינם מלאים",
                            "שגיאה", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Information",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                return;
            }
        }
        if (!rdbFemale.isSelected() && !rdbMale.isSelected()) {
            if (this.language.equals("iw")) {
                JOptionPane.showMessageDialog(null, "יש לסמן מין", "שגיאה",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Please choose gender",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            return;
        }

        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String userName = txtUserName.getText();
        String password = txtPassword.getText();
        String gender = "";
        if (rdbFemale.isSelected()) {
            gender = rdbFemale.getText();
        } else if (rdbMale.isSelected()) {
            gender = rdbMale.getText();
        }
        RegularUser u = new RegularUser();
        u.signUp(firstName, lastName, gender, userName, password);
        if (this.language.equals("iw")) {
            JOptionPane.showMessageDialog(null, "נרשמת בהצלחה",
                    "הפעולה הסתיימה", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Sign Up Successfully",
                    "Action Complete", JOptionPane.INFORMATION_MESSAGE);
        }
        this.dispose();
        previous.setVisible(true);
    }//GEN-LAST:event_btnSignUpActionPerformed
    /**
     * In each letter pressed we check if the letter is a digit.
     * if the letter is a digit then we shows a warning label.
     */
    private void txtFirstNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFirstNameKeyReleased
        String firstName = txtFirstName.getText();
        labValidFirstName.setText(InputValidation.checkFirstName(firstName));
        if (labValidFirstName.getText().equals("")) {
            inputValidation[0] = new Boolean(true);
        }
    }//GEN-LAST:event_txtFirstNameKeyReleased
    /**
     * In each letter pressed we check if the letter is a digit.
     * if the letter is a digit then we shows a warning label.
     */
    private void txtLastNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLastNameKeyReleased
        String lastName = txtLastName.getText();
        labValidLastName.setText(InputValidation.checkLastName(lastName));
        if (labValidLastName.getText().equals("")) {
            inputValidation[1] = new Boolean(true);
        }
    }//GEN-LAST:event_txtLastNameKeyReleased
    /**
     * In each letter pressed we check if the user name is already exist or if
     * the length is smaller then 4 letters or longer then 20.
     */
    private void txtUserNameKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUserNameKeyReleased
        try {
            String userName = txtUserName.getText();
            ConnectionUtil myConnection = new ConnectionUtil();
            serverUp = myConnection.openConnection();
            if (!serverUp) {
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            }

            myConnection.checkIfUserNameTaken(userName);

            String answer = (String) myConnection.getOis().readObject();
            myConnection.closeConnection();
            if (answer.equals("")) {
                labValidUserName.setText("");
                inputValidation[2] = new Boolean(true);
            } else {
                labValidUserName.setText(LocalizationUtil.localizedResourceBundle
                        .getString(answer));
            }

        } catch (IOException ex) {
            Logger.getLogger(SignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_txtUserNameKeyReleased
    /**
     * In each letter pressed we check if the password is smaller then 6 or longer
     * then 20.
     */
    private void txtPasswordKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPasswordKeyReleased
        String password = txtPassword.getText();
        labValidPassword.setText(InputValidation.checkPassword(password));
        if (labValidPassword.getText().equals("")) {
            inputValidation[3] = new Boolean(true);
        }
    }//GEN-LAST:event_txtPasswordKeyReleased
    /**
     * When the user try to exit the application this method is triggered. the
     * user must confirm the exit, then the we exit the application.
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (!serverUp) {
            if (this.language.equals("iw")) {
                JOptionPane.showMessageDialog(null, "השרת בתחזוקה כרגע.. נסה מאוחר יותר\n ניתן לשחק כאורח", "שגיאה",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "The server is under"
                        + " maintenance!\nPlease try again later\n"
                        + "In the meantime you can play as a guest", "Error",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            this.dispose();
            new WelcomeScreen().setVisible(true);
        } else {
            int confirmed = JOptionPane.NO_OPTION;
            if (this.language.equals("iw")) {
                confirmed = LocalizationUtil.exitDialog();
            } else {
                confirmed = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit the program?",
                        "Exit Program",
                        JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            }
            if (confirmed == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        }
    }//GEN-LAST:event_formWindowClosing
    /**
     * if back is clicked we goes back to the previous frame.
     */
    private void labBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labBackMouseClicked
        this.dispose();
        previous.setVisible(true);
    }//GEN-LAST:event_labBackMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btnGroupGender;
    private javax.swing.JButton btnSignUp;
    private javax.swing.JLabel labBack;
    private javax.swing.JLabel labBackground;
    private javax.swing.JLabel labFirstName;
    private javax.swing.JLabel labGender;
    private javax.swing.JLabel labLastName;
    private javax.swing.JLabel labPassword;
    private javax.swing.JLabel labSignture;
    private javax.swing.JLabel labTitle;
    private javax.swing.JLabel labUserName;
    private javax.swing.JLabel labValidFirstName;
    private javax.swing.JLabel labValidLastName;
    private javax.swing.JLabel labValidPassword;
    private javax.swing.JLabel labValidUserName;
    private javax.swing.JRadioButton rdbFemale;
    private javax.swing.JRadioButton rdbMale;
    private javax.swing.JTextField txtFirstName;
    private javax.swing.JTextField txtLastName;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtUserName;
    // End of variables declaration//GEN-END:variables

}
