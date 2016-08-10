package Forms;

import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MyJOptionPane {

    int answer = 99;

    public MyJOptionPane(TheGame t) {
        answer = JOptionPane.showConfirmDialog(t,
                getPanel(),
                "Waiting..",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    }

    public MyJOptionPane() {
        answer = JOptionPane.showConfirmDialog(null,
                getPanel(),
                "Waiting..",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
    }

    public int getAnswer() {
        return answer;
    }

    private JPanel getPanel() {
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

    public static void main(String[] args) {
        new MyJOptionPane();

    }
}
