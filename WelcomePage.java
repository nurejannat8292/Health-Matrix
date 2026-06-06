import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JFrame {

    private final Color LIGHT_GREEN = new Color(160, 230, 185);    

    private final Color BORDER_GRAY = new Color(150, 175, 160);                 
    private final Color TEXT_DARK = new Color(40, 55, 45);

    public WelcomePage() {
        setTitle("Health Matrix");
        setSize(400, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new GridBagLayout()); 
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("<html><div style='text-align: center;'>" +
             "<font size='5' color='#55665c'>Welcome to</font><br><br>" +
             "<font size='7' color='#1a241e'><b>Health Matrix</b></font><br><br>" +
             "<font size='4' color='#445248'>Your Personal Wellness Companion</font></div></html>", 
             SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 40, 50, 40);
        add(title, gbc);

        gbc.insets = new Insets(12, 50, 12, 50); 
        gbc.ipady = 20; 

        JButton logBtn = createModernButton("Log in", LIGHT_GREEN, TEXT_DARK, BORDER_GRAY);
        gbc.gridy = 1;
        add(logBtn, gbc);

        JButton regBtn = createModernButton("Registration", LIGHT_GREEN, TEXT_DARK, BORDER_GRAY);
        gbc.gridy = 2;
        add(regBtn, gbc);

        JButton exitBtn = createModernButton("Exit", new Color(245, 245, 245), TEXT_DARK, BORDER_GRAY);
        gbc.gridy = 3;
        add(exitBtn, gbc);

        logBtn.addActionListener(e -> { new LoginPage(); dispose(); });
        regBtn.addActionListener(e -> { new RegisterPage(); dispose(); });
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private JButton createModernButton(String text, Color bg, Color fg, Color borderColor) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                g2.setColor(borderColor);
                g2.setStroke(new BasicStroke(1.5f)); 
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);

                g2.setColor(getForeground());
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 4;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusable(false);
        btn.setBackground(bg);
        btn.setForeground(fg); 
        btn.setFont(new Font("SansSerif", Font.BOLD, 16)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        new WelcomePage();
    }

    class GradientPanel extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            int w = getWidth();
            int h = getHeight();

            GradientPaint gp = new GradientPaint(
                    0, 0, new Color(195, 235, 210), 
                    0, h, new Color(250, 253, 251) 
            );
            g2d.setPaint(gp);
            g2d.fillRect(0, 0, w, h);
        }
    }
}