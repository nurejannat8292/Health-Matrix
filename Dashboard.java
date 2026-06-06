import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {
    public Dashboard(String userName) {
        setTitle("Health Matrix - Dashboard");
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        GradientPanel mainPanel = new GradientPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 25, 30, 25));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel welcomeLabel = new JLabel("Welcome Back, " + userName);
        welcomeLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(50, 100, 50)); 
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton profileIconBtn = new JButton("👤") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 245, 245)); 
                g2.fillOval(0, 0, getWidth() - 1, getHeight() - 1);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        profileIconBtn.setFont(new Font("SansSerif", Font.PLAIN, 24)); 
        profileIconBtn.setPreferredSize(new Dimension(55, 55)); 
        profileIconBtn.setMargin(new Insets(0, 0, 0, 0)); 
        profileIconBtn.setContentAreaFilled(false);
        profileIconBtn.setBorderPainted(false);
        profileIconBtn.setFocusable(false);
        profileIconBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        profileIconBtn.addActionListener(e -> {
    new ProfilePage(userName); 
});

        JPanel iconContainer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        iconContainer.setOpaque(false);
        iconContainer.add(profileIconBtn);
        
        topPanel.add(iconContainer, BorderLayout.EAST);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 20, 20));
        buttonPanel.setOpaque(false);

        JButton bmiBtn = createCardButton("BMI Calculator", "⚖", new Color(200, 220, 155));
        bmiBtn.addActionListener(e -> {
            try {
                new BMICalc(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "BMI Calculator class not found!");
            }
        });
        buttonPanel.add(bmiBtn);

        JButton bpBtn = createCardButton("BP Tracker", "❤️", new Color(255, 210, 210));
        bpBtn.addActionListener(e -> {
          try {
                new BPTracker(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "BP Tracker class not found!");
            }
        });
        buttonPanel.add(bpBtn);

        JButton waterBtn = createCardButton("Water Intake", "💧", new Color(190, 225, 255));
        waterBtn.addActionListener(e -> {
            try {
                new WaterIntake(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Water Intake class not found!");
            }
        });
        buttonPanel.add(waterBtn);

        JButton foodBtn = createCardButton("Food Guide", "🍎", new Color(255, 220, 180));
        foodBtn.addActionListener(e -> {
           try {
                new FoodGuide(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Food guide class not found!");
            }
        });
        buttonPanel.add(foodBtn);
        JButton medBtn = createCardButton("Medicine ", "💊", new Color(220, 200, 255));
        medBtn.addActionListener(e -> {
            try {
                new MedicineRecord(); 
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, " Medicine Record class not found!");
            }
        });
        buttonPanel.add(medBtn);
        JButton logoutBtn = createCardButton("LogOut ", "🚪", new Color(220, 220, 220));
        logoutBtn.addActionListener(e -> {
            new LoginPage(); 
            dispose();
        });
        buttonPanel.add(logoutBtn);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
        
        revalidate();
        repaint();
    }

    private JButton createCardButton(String text, String icon, Color bgColor) {
        JButton btn = new JButton("<html><center><font size='6'>" + icon + "</font><br><br>" 
                                    + "<font size='4'><b>" + text + "</b></font></center></html>") {
           
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 35, 35);
                
                g2.setColor(new Color(160, 160, 160)); 
                g2.setStroke(new BasicStroke(1.8f)); 
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 35, 35);
                
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBackground(bgColor);
        btn.setForeground(new Color(60, 60, 60));
        btn.setFocusable(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        new Dashboard("User").setVisible(true);
    }
}