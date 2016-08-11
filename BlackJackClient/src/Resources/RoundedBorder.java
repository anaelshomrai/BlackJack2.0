
package Resources;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.Border;

/**
 *
 * @author ANI
 */
public class RoundedBorder implements Border {

    private int radius;

    /**
     *
     * @param radius
     */
    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    /**
     *
     * @param c
     * @return
     */
    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius+1, this.radius+1, this.radius+2, this.radius);
    }

    /**
     *
     * @return
     */
    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    /**
     *
     * @param c
     * @param g
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.drawRoundRect(x, y, width-1, height-1, radius, radius);
    }
}
