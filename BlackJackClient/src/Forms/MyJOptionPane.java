package Forms;

import Users.User;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * In this class we design our own JOptionPane. Two new JOptionPane, in Hebrew
 * and in English: waiting - the JOptionPane that appear when a player click on
 * play online profile - the JOptionPane that appear when a user click on
 * profile
 *
 * @author ANI
 */
public class MyJOptionPane {

    /**
     * Return a design panel in Hebrew that will be used in a JOptionPane, at
     * the start of a game online.
     *
     * @return the panel for waiting to people
     */
    public static JPanel getWaitingIWPanel() {
        JPanel panel = new JPanel();
        BorderLayout bl = new BorderLayout();
        bl.setVgap(15);
        panel.setLayout(bl);
        JLabel waiting = new JLabel("ממתין לשחקנים..");
        waiting.setFont(new java.awt.Font("Tahoma", 1, 20));
        waiting.setHorizontalAlignment(JLabel.CENTER);
        panel.add(waiting, BorderLayout.NORTH);

        JLabel explain = new JLabel("לחץ אישור כדי להמשיך");
        explain.setFont(new java.awt.Font("Tahoma", 1, 12));
        explain.setHorizontalAlignment(JLabel.CENTER);
        panel.add(explain, BorderLayout.SOUTH);

        ImageIcon loading = new ImageIcon("./src/img/loading.gif");
        JLabel loadingImg = new JLabel();
        loadingImg.setIcon(loading);
        loadingImg.setHorizontalAlignment(JLabel.CENTER);
        panel.add(loadingImg, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Return a design panel in English that will be used in a JOptionPane, at
     * the start of a game online.
     *
     * @return the panel for waiting to people
     */
    public static JPanel getWaitingENPanel() {
        JPanel panel = new JPanel();
        BorderLayout bl = new BorderLayout();
        bl.setVgap(15);
        panel.setLayout(bl);
        JLabel waiting = new JLabel("Waiting For Players..");
        waiting.setFont(new java.awt.Font("Tahoma", 1, 20));
        waiting.setHorizontalAlignment(JLabel.CENTER);
        panel.add(waiting, BorderLayout.NORTH);

        JLabel explain = new JLabel("Click OK to continue");
        explain.setFont(new java.awt.Font("Tahoma", 1, 12));
        explain.setHorizontalAlignment(JLabel.CENTER);
        panel.add(explain, BorderLayout.SOUTH);

        ImageIcon loading = new ImageIcon("./src/img/loading.gif");
        JLabel loadingImg = new JLabel();
        loadingImg.setIcon(loading);
        loadingImg.setHorizontalAlignment(JLabel.CENTER);
        panel.add(loadingImg, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Return a design panel in English that will be used in a JOptionPane, when
     * a user click on profile.
     *
     * @param user the user
     * @return the panel for waiting to people
     */
    public static JPanel getProfileEN(User user) {
        JPanel panel = new JPanel();
        GridLayout grid = new GridLayout(6, 6);
        grid.setVgap(5);
        panel.setLayout(grid);
        String type = (user.getPermission() == 1 ? "Admin" : "User");

        JLabel profile = new JLabel("My Profile");
        profile.setFont(new java.awt.Font("Tahoma", Font.ITALIC, 18));
        profile.setForeground(new java.awt.Color(0, 102, 255));
        ImageIcon genderPic = null;
        if (user.getGender().equals("Male")) {
            genderPic = new ImageIcon("./src/img/boyIcon.png");
        } else if (user.getGender().equals("Female")) {
            genderPic = new ImageIcon("./src/img/girlIcon.png");
        }
        JLabel profileD = new JLabel();
        profileD.setIcon(genderPic);

        JLabel acountType = new JLabel("Account Type:  ");
        acountType.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        JLabel acountTypeD = new JLabel(type);
        acountTypeD.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        acountTypeD.setForeground(new java.awt.Color(0, 0, 102));

        JLabel about = new JLabel("User Name: ");
        about.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        JLabel aboutD = new JLabel(user.getUserName());
        aboutD.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        aboutD.setForeground(new java.awt.Color(0, 0, 102));

        JLabel fullName = new JLabel("Name: ");
        fullName.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        JLabel fullNameD = new JLabel(user.getFirstName() + " " + user.getLastName());
        fullNameD.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        fullNameD.setForeground(new java.awt.Color(0, 0, 102));

        JLabel winning = new JLabel("Wins: ");
        winning.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        JLabel winningD = new JLabel(String.valueOf(user.getWins()));
        winningD.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        winningD.setForeground(new java.awt.Color(0, 0, 102));

        JLabel cash = new JLabel("Cash: ");
        cash.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        JLabel cashD = new JLabel(user.getBalance() + "$");
        cashD.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        cashD.setForeground(new java.awt.Color(0, 0, 102));

        panel.add(profile);
        panel.add(profileD);
        panel.add(acountType);
        panel.add(acountTypeD);
        panel.add(about);
        panel.add(aboutD);
        panel.add(fullName);
        panel.add(fullNameD);
        panel.add(winning);
        panel.add(winningD);
        panel.add(cash);
        panel.add(cashD);
        return panel;
    }

    /**
     * Return a design panel in Hebrew that will be used in a JOptionPane, when
     * a user click on profile.
     *
     * @param user the user
     * @return the panel for waiting to people
     */
    public static JPanel getProfileIW(User user) {
        JPanel panel = new JPanel();
        GridLayout grid = new GridLayout(6, 6);
        grid.setVgap(5);
        panel.setLayout(grid);
        String type = (user.getPermission() == 1 ? "אדמין" : "משתמש");

        JLabel profile = new JLabel("הפרופיל שלי");
        profile.setFont(new java.awt.Font("Tahoma", Font.ITALIC, 18));
        profile.setForeground(new java.awt.Color(0, 102, 255));
        ImageIcon genderPic = null;
        if (user.getGender().equals("זכר")) {
            genderPic = new ImageIcon("./src/img/boyIcon.png");
        } else if (user.getGender().equals("נקבה")) {
            genderPic = new ImageIcon("./src/img/girlIcon.png");
        }
        JLabel profileD = new JLabel();
        profileD.setIcon(genderPic);

        JLabel acountType = new JLabel("סוג חשבון:  ");
        acountType.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        JLabel acountTypeD = new JLabel(type);
        acountTypeD.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        acountTypeD.setForeground(new java.awt.Color(0, 0, 102));

        JLabel about = new JLabel("שם משתמש: ");
        about.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        JLabel aboutD = new JLabel(user.getUserName());
        aboutD.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        aboutD.setForeground(new java.awt.Color(0, 0, 102));

        JLabel fullName = new JLabel("שם: ");
        fullName.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        JLabel fullNameD = new JLabel(user.getFirstName() + " " + user.getLastName());
        fullNameD.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        fullNameD.setForeground(new java.awt.Color(0, 0, 102));

        JLabel winning = new JLabel("נצחונות: ");
        winning.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        JLabel winningD = new JLabel(String.valueOf(user.getWins()));
        winningD.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        winningD.setForeground(new java.awt.Color(0, 0, 102));

        JLabel cash = new JLabel("כסף: ");
        cash.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        JLabel cashD = new JLabel(user.getBalance() + "$");
        cashD.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14));
        cashD.setForeground(new java.awt.Color(0, 0, 102));

        panel.add(profileD);
        panel.add(profile);
        panel.add(acountTypeD);
        panel.add(acountType);
        panel.add(aboutD);
        panel.add(about);
        panel.add(fullNameD);
        panel.add(fullName);
        panel.add(winningD);
        panel.add(winning);
        panel.add(cashD);
        panel.add(cash);
        return panel;
    }

}
