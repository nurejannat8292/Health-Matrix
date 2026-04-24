import javax.swing.*;
import java.awt.*;

public class WelcomePage extends JFrame {
    public WelcomePage() {
        setTitle("Health Matrix");
        setSize(400, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new GridBagLayout());

        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new GridBagLayout()); 
      
        setContentPane(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 40, 5, 40);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title        
        JLabel title = new JLabel("<html><div style='text-align: center;'>" +
    "<font size='4' color='#808080'>Welcome to</font><br>" +
    "<font size='7' color='black'><b>Health Matrix</b></font><br>" +
    "<font size='3' color='#666666'>Your Personal Wellness Companion</font></div></html>", 
    SwingConstants.CENTER);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 40, 60, 40);
        add(title, gbc);

        // Buttons

        gbc.insets = new Insets(10, 50, 10, 50); 
        gbc.ipady = 15;

        JButton regBtn = createMinimalButton("Registration", new Color(191, 239, 255), Color.BLACK, true);
        gbc.gridy = 2;
        add(regBtn, gbc);

        JButton logBtn = createMinimalButton("Log in", Color.WHITE, Color.BLACK, true);
        gbc.gridy = 1;
        add(logBtn, gbc);

        JButton exitBtn = createMinimalButton("Exit", new Color(255, 204, 204), Color.RED, true);
        gbc.gridy = 3;
        add(exitBtn, gbc);

        // Actions
        logBtn.addActionListener(e -> { new LoginPage(); dispose(); });
        regBtn.addActionListener(e -> { new RegisterPage(); dispose(); });
        exitBtn.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    private JButton createMinimalButton(String text, Color bg, Color fg, boolean hasBorder) {
        JButton btn = new JButton(text) {
           
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Background
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                
                // Border for White Button 
                if(hasBorder) {
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                }

                // Text
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
        btn.setFont(new Font("SansSerif", Font.BOLD, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        new WelcomePage();
    }

    class GradientPanel extends JPanel {
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();    
        Color color1 = new Color(243, 232, 255); 
        Color color2 = new Color(255, 255, 255);
        GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);

        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}

}