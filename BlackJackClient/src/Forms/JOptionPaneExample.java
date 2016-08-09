package Forms;

import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class JOptionPaneExample {

    int answer;

    public JOptionPaneExample(TheGame t) {
        answer = JOptionPane.showConfirmDialog(t,
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

        ImageIcon loading = new ImageIcon("./src/img/loading.gif");
        JLabel loadingImg = new JLabel();
        loadingImg.setIcon(loading);
        loadingImg.setHorizontalAlignment(JLabel.CENTER);
        panel.add(loadingImg, BorderLayout.CENTER);

        return panel;
    }
}
