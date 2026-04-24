import javax.swing.*;
import java.awt.*;

public class SuccessDialog extends JDialog {
    public SuccessDialog(JFrame parent) {
        super(parent, "Success", true);
        setSize(350, 250);
        setLocationRelativeTo(parent);
        setUndecorated(true); 
   
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1)); 
        panel.setLayout(null);
        getContentPane().add(panel);

        // green sign
        JLabel checkIcon = new JLabel("<html><font color='green'>✓</font></html>");
        checkIcon.setFont(new Font("SansSerif", Font.BOLD, 60));
        checkIcon.setBounds(145, 20, 60, 70);
        panel.add(checkIcon);

        JLabel titleLabel = new JLabel("Congratulations!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 153, 0));
        titleLabel.setBounds(25, 100, 300, 30);
        panel.add(titleLabel);

        JLabel msgLabel = new JLabel("Your account has been created successfully.", SwingConstants.CENTER);
        msgLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        msgLabel.setForeground(Color.BLACK);
        msgLabel.setBounds(25, 130, 300, 20);
        panel.add(msgLabel);

        JButton okBtn = new JButton("OK") {
           
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); 
                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        okBtn.setBackground(new Color(168, 85, 247)); 
        okBtn.setForeground(Color.WHITE);
        okBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        okBtn.setBounds(115, 180, 120, 40);
        okBtn.setBorderPainted(false);
        okBtn.setContentAreaFilled(false);
        okBtn.setFocusable(false);
        okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        okBtn.addActionListener(e -> dispose());
        panel.add(okBtn);
    }
}