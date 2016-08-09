package blackjack;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Draws background image on the board.
 */
public class BoardPanel extends JPanel {

    private ImageIcon backgroundImage;

    public BoardPanel(String backgroundImage) {
        this.backgroundImage = new ImageIcon(backgroundImage);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(backgroundImage.getImage(), 0, 0, null);
    }
}
