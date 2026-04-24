import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginPage extends JFrame {
    private JTextField nameF;
    private JPasswordField passF;

    public LoginPage() {
        setTitle("Health Matrix - Login");
        setSize(400, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // Back Button 
        addBackButton();
        // Title
        JLabel title = new JLabel("Login");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setBounds(40, 90, 200, 50);
        add(title);

        //Subtitle
        JLabel subTitle = new JLabel("<html>Don't have an account? <font color='#A855F7'><u>Register</u></font></html>");
        subTitle.setBounds(40, 140, 250, 20);
        subTitle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        subTitle.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) { new RegisterPage(); dispose(); }
        });
        add(subTitle);

        // Username 
        JLabel userIcon = new JLabel("👤");
        userIcon.setFont(new Font("SansSerif", Font.PLAIN, 22)); 
        userIcon.setBounds(40, 210, 35, 45); 
        add(userIcon);

        nameF = createStyledField("Username", 210);
        add(nameF);

        //Password 
        JLabel passIcon = new JLabel("🔒");
        passIcon.setFont(new Font("SansSerif", Font.PLAIN, 22));
        passIcon.setBounds(40, 280, 35, 45);
        add(passIcon);

        passF = new JPasswordField("Password");
        passF.setEchoChar((char) 0);
        passF.setForeground(Color.GRAY);
        passF.setBounds(80, 280, 260, 45); 
        passF.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        add(passF);

        //Login Button
        JButton loginBtn = new JButton("Login →");
        loginBtn.setBackground(new Color(168, 85, 247));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginBtn.setBounds(200, 390, 140, 50);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.addActionListener(e -> performLogin());
        add(loginBtn);

        setupNavigation(loginBtn);

        setVisible(true);
    }

    private JTextField createStyledField(String hint, int y) {
        JTextField tf = new JTextField(hint);
        tf.setBounds(80, y, 260, 45); 
        tf.setForeground(Color.GRAY);
        tf.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(hint)) { tf.setText(""); tf.setForeground(Color.BLACK); }
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) { tf.setText(hint); tf.setForeground(Color.GRAY); }
            }
        });
        return tf;
    }

    private void addBackButton() {
        JButton backBtn = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 245, 245));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(15, 20, 23, 12);
                g2.drawLine(15, 20, 30, 20);
                g2.drawLine(15, 20, 23, 28);
                g2.dispose();
            }
        };
        backBtn.setBounds(25, 25, 40, 40);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusable(false);
        backBtn.addActionListener(e -> { new WelcomePage(); dispose(); });
        add(backBtn);
    }

    private void setupNavigation(JButton loginBtn) {
       
        nameF.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) passF.requestFocus();
            }
        });

        // Password Placeholder & Enter Key: Password -> Login

         passF.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (new String(passF.getPassword()).equals("Password")) {
                    passF.setText(""); passF.setEchoChar('●'); passF.setForeground(Color.BLACK);
                }
            }
        });

    }

 private void performLogin() {
    String n = nameF.getText();
    String p = new String(passF.getPassword());

    if (n.equals("Username") || p.equals("Password") || n.isEmpty() || p.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter both username and password!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        return;
    }

    boolean found = false;
    String matchedName = "";

    try (BufferedReader reader = new BufferedReader(new FileReader("health_data.txt"))) {
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(","); 
            
            if (data.length >= 2 && data[0].equals(n) && data[1].equals(p)) {
                found = true;
                matchedName = data[0];
                break; 
        }
    }}catch (IOException e) {
        JOptionPane.showMessageDialog(this, "Database file not found!", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (found) {
       
        JDialog d = new JDialog(this, "Success", true);
        d.setUndecorated(true);
        d.setSize(320, 220);
        d.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        panel.setLayout(null);

        JLabel icon = new JLabel("<html><font color='#22C55E'>✓</font></html>", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.BOLD, 55));
        icon.setBounds(0, 20, 320, 60);
        panel.add(icon);

        JLabel title = new JLabel("Login Successful!", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 18));
        title.setForeground(new Color(34, 197, 94));
        title.setBounds(0, 85, 320, 30);
        panel.add(title);

        JLabel hiMsg = new JLabel("Hi " + matchedName + ", Welcome back!", SwingConstants.CENTER);
        hiMsg.setFont(new Font("SansSerif", Font.PLAIN, 13));
        hiMsg.setBounds(0, 115, 320, 20);
        panel.add(hiMsg);

        JButton okBtn = new JButton("Continue") {
          
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(168, 85, 247));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth()-fm.stringWidth(getText()))/2, (getHeight()+fm.getAscent())/2-2);
                g2.dispose();
            }
        };
        okBtn.setBounds(100, 160, 120, 38);
        okBtn.setBorderPainted(false);
        okBtn.setContentAreaFilled(false);
        okBtn.setFocusable(false);
        okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okBtn.addActionListener(e -> d.dispose());
        panel.add(okBtn);

        d.add(panel);
        d.setVisible(true); 
       

        dispose(); 
    } else {
        
        JOptionPane.showMessageDialog(this, "Wrong Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
    }
 }}