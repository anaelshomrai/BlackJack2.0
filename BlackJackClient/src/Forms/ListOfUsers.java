package Forms;

import DataUtil.ConnectionData;
import Resources.GameUtil;
import Resources.LocalizationUtil;
import Users.User;
import Resources.RoundedBorder;
import Users.Admin;
import blackjackclient.ConnectionUtil;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * This class present a table with all users. only admin can see this form, and
 * preform actions on regular users, such as remove their account or promote
 * them to be admins.
 *
 * @author ANI
 */
public class ListOfUsers extends javax.swing.JFrame {

    String language = "";
    DefaultTableModel dtm;
    private static int id;
    private static String userName = null;
    AdminHome previous = null;
    Admin admin;

    /**
     * Initialize a new ListOfUsers form, with the previous forma and the admin
     * who opened this form.
     *
     * @param previous the previous form
     * @param admin the admin
     */
    public ListOfUsers(AdminHome previous, Admin admin) {
        initComponents();
        this.previous = previous;
        this.admin = admin;
        initTable();

    }

    /**
     * Initialize a new ListOfUsers form, with the previous form, the admin who
     * opened this form and the language selected.
     *
     * @param previous the previous form
     * @param admin the admin
     * @param lang the language selected
     */
    public ListOfUsers(AdminHome previous, Admin admin, String lang) {
        initComponents();
        this.previous = previous;
        this.admin = admin;
        initTable();
        if (lang.equals("iw")) {
            LocalizationUtil.changeOptionPane_iw();
            this.language = lang;
            LocalizationUtil.localizedResourceBundle
                    = LocalizationUtil.getBundleListOfUsersIW();
            updateCaption();
        }
    }

    /**
     * Update the text to Hebrew in case this is the chosen language.
     */
    private void updateCaption() {
        Vector columnsName = new Vector();
        columnsName.addElement(LocalizationUtil.localizedResourceBundle
                .getString("Id"));
        columnsName.addElement(LocalizationUtil.localizedResourceBundle
                .getString("FirstName"));
        columnsName.addElement(LocalizationUtil.localizedResourceBundle
                .getString("LastName"));
        columnsName.addElement(LocalizationUtil.localizedResourceBundle
                .getString("Gender"));
        columnsName.addElement(LocalizationUtil.localizedResourceBundle
                .getString("UserName"));
        dtm.setColumnIdentifiers(columnsName);
        btnChangePermission.setText(LocalizationUtil.localizedResourceBundle
                .getString("btnChangePermission"));
        btnRemoveUser.setText(LocalizationUtil.localizedResourceBundle
                .getString("btnRemoveUser"));
        usersTable.removeColumn(usersTable.getColumnModel().getColumn(0));
    }

    /**
     * Initialize the table with the users details, by sending the server
     * request to pull out from the database all the users.
     */
    private void initTable() {
        jScrollPane1.setOpaque(false);
        jScrollPane1.getViewport().setOpaque(false);
        usersTable.setShowGrid(false);

        GameUtil.setIcon(this);
        dtm = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //All cells can't be edited
                return false;
            }
        };
        Vector columnsName = new Vector();
        columnsName.addElement("Id");
        columnsName.addElement("First Name");
        columnsName.addElement("Last Name");
        columnsName.addElement("Gender");
        columnsName.addElement("User Name");
        dtm.setColumnIdentifiers(columnsName);

        try {
            ConnectionUtil myConnection = new ConnectionUtil();
            myConnection.openConnection();
            myConnection.setStatus(1);
            myConnection.getAllUsers();

            ConnectionData dataResponse
                    = (ConnectionData) myConnection.getOis().readObject();
            myConnection.closeConnection();

            List<User> users = dataResponse.getUsers();

            for (User u : users) {
                Vector dataRows = new Vector();
                int id = u.getId();
                String firstName = u.getFirstName();
                String lastName = u.getLastName();
                String gender = u.getGender();
                String usertName = u.getUserName();

                dataRows.addElement(id);
                dataRows.addElement(firstName);
                dataRows.addElement(lastName);
                dataRows.addElement(gender);
                dataRows.addElement(usertName);

                dtm.addRow(dataRows);

            }

        } catch (IOException ex) {
            Logger.getLogger(WelcomeScreen.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(WelcomeScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        usersTable.setModel(dtm);

        usersTable.setBackground(
                new java.awt.Color(204, 204, 255));
        usersTable.getTableHeader()
                .setReorderingAllowed(false);
        usersTable.removeColumn(usersTable.getColumnModel().getColumn(0));

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        usersTable = new javax.swing.JTable();
        btnChangePermission = new javax.swing.JButton();
        btnRemoveUser = new javax.swing.JButton();
        labBack = new javax.swing.JLabel();
        labBackground = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("BlackJack ANI - ADMIN");
        setName("listOfUsersFrame"); // NOI18N
        setPreferredSize(new java.awt.Dimension(620, 520));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        getContentPane().setLayout(null);

        usersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        usersTable.setSelectionForeground(new java.awt.Color(0, 0, 0));
        usersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                usersTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(usersTable);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 50, 510, 440);

        btnChangePermission.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnChangePermission.setText("Change Permission");
        btnChangePermission.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnChangePermission.setFocusPainted(false);
        btnChangePermission.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChangePermissionActionPerformed(evt);
            }
        });
        getContentPane().add(btnChangePermission);
        btnChangePermission.setBounds(280, 10, 230, 33);

        btnRemoveUser.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        btnRemoveUser.setText("Remove User");
        btnRemoveUser.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRemoveUser.setFocusPainted(false);
        btnRemoveUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveUserActionPerformed(evt);
            }
        });
        getContentPane().add(btnRemoveUser);
        btnRemoveUser.setBounds(30, 10, 230, 33);

        labBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/back.png"))); // NOI18N
        labBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        labBack.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                labBackMouseClicked(evt);
            }
        });
        getContentPane().add(labBack);
        labBack.setBounds(550, 420, 70, 70);

        labBackground.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/21.jpg"))); // NOI18N
        getContentPane().add(labBackground);
        labBackground.setBounds(0, 0, 620, 500);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents
    /**
     * This method saves the id of the clicked user. will be used to preform
     * change permission and remove account.
     */
    private void usersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_usersTableMouseClicked
        int selectedRow = usersTable.getSelectedRow();
        id = 0;
        if (selectedRow != -1) {
            usersTable.setSelectionBackground(Color.WHITE);
            id = (int) usersTable.getModel().getValueAt(usersTable.getSelectedRow(), 0);
            userName = (String) usersTable.getValueAt(selectedRow, 3);
        }
    }//GEN-LAST:event_usersTableMouseClicked

    /**
     * With the id selected, we check the user is not an admin, and promote him
     * to be an admin.
     */
    private void btnChangePermissionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChangePermissionActionPerformed
        if (usersTable.getSelectedRow() != -1) {
            try {
                ConnectionUtil myConnection = new ConnectionUtil();
                myConnection.openConnection();

                myConnection.findUserById(id);
                myConnection.setStatus(0);

                ConnectionData dataResponse
                        = (ConnectionData) myConnection.getOis().readObject();

                User u = dataResponse.getUser();
                if (u != null) {
                    if (u.getPermission() == 1) {
                        if (this.language.equals("iw")) {
                            JOptionPane.showMessageDialog(null,
                                    "לא ניתן לשנות אדמין!", "אופציה שגויה",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "You cant change an admin!", "Invalid Option",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        int option;
                        if (this.language.equals("iw")) {
                            option = JOptionPane.showConfirmDialog(null,
                                    "האם אתה בטוח שאתה רוצה לשנות את "
                                    + userName + " לאדמין?", "שינוי הרשאה",
                                    JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
                        } else {
                            option = JOptionPane.showConfirmDialog(null,
                                    "Are you sure you want to make " + userName
                                    + " admin?", "Change Permission",
                                    JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
                        }
                        if (option == JOptionPane.OK_OPTION) {
                            admin.changePermission(u.getId());
                        }

                    }
                    myConnection.closeConnection();
                }
            } catch (IOException ex) {
                Logger.getLogger(ListOfUsers.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ListOfUsers.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null,
                    "Please select user !", "Change Permission",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnChangePermissionActionPerformed
    /**
     * With the id selected, we check the user is not an admin, and delete his
     * account.
     */
    private void btnRemoveUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveUserActionPerformed
        if (usersTable.getSelectedRow() != -1) {
            try {
                ConnectionUtil myConnection = new ConnectionUtil();
                myConnection.openConnection();

                myConnection.findUserById(id);

                ConnectionData dataResponse
                        = (ConnectionData) myConnection.getOis().readObject();
                User u = dataResponse.getUser();

                if (u != null) {
                    if (u.getPermission() == 1) {
                        if (this.language.equals("iw")) {
                            JOptionPane.showMessageDialog(null,
                                    "לא ניתן למחוק אדמין!!", "אופציה שגויה",
                                    JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null,
                                    "You cant delete an admin!", "Invalid Option",
                                    JOptionPane.ERROR_MESSAGE);
                        }

                    } else {
                        int option;
                        if (this.language.equals("iw")) {
                            option = JOptionPane.showConfirmDialog(null,
                                    "האם אתה בטוח שברצונך למחוק את "
                                    + userName + " ?", "הסרת משתמש",
                                    JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
                        } else {
                            option = JOptionPane.showConfirmDialog(null,
                                    "Are you sure you want to delete "
                                    + userName + " ?", "Delete User",
                                    JOptionPane.OK_CANCEL_OPTION,
                                    JOptionPane.WARNING_MESSAGE);
                        }
                        if (option == JOptionPane.OK_OPTION) {

                            admin.removeAccount(u);

                            dtm.removeRow(usersTable.getSelectedRow());
                        }
                    }
                }

                myConnection.closeConnection();
            } catch (IOException ex) {
                Logger.getLogger(ListOfUsers.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ListOfUsers.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (this.language.equals("iw")) {
            JOptionPane.showMessageDialog(null, "בחר משתמש!", "הסרת משתמש",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Please select user !",
                    "Delete User",
                    JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_btnRemoveUserActionPerformed
    /**
     * When the user try to exit the application this method is triggered. the
     * user must confirm the exit, then the we exit the application.
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
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
    }//GEN-LAST:event_formWindowClosing
    /**
     * if back is clicked we goes back to the previous frame.
     */
    private void labBackMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_labBackMouseClicked
        this.dispose();
        previous.setVisible(true);
    }//GEN-LAST:event_labBackMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnChangePermission;
    private javax.swing.JButton btnRemoveUser;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labBack;
    private javax.swing.JLabel labBackground;
    private javax.swing.JTable usersTable;
    // End of variables declaration//GEN-END:variables
}
