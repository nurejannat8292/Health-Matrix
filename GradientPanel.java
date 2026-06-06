import javax.swing.*;
import java.awt.*;

public class GradientPanel extends JPanel {
   
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int w = getWidth();
        int h = getHeight();

        GradientPaint gp = new GradientPaint(
                0, 0, new Color(225, 240, 255),   
                w, h, new Color(250, 252, 255)   
        );

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}