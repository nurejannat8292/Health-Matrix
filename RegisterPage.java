import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterPage extends JFrame {
    private JTextField nameF, weightF, heightF;
    private JPasswordField passF;

    public RegisterPage() {
        setTitle("Health Matrix - Sign Up");
        setSize(400, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        JButton backBtn = new JButton() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 245, 245)); 
                g2.fillOval(0, 0, getWidth(), getHeight());
                
                g2.setColor(Color.BLACK);
                g2.setStroke(new BasicStroke(2f));
                g2.drawLine(12, 20, 20, 12);
                g2.drawLine(12, 20, 28, 20);
                g2.drawLine(12, 20, 20, 28);
                g2.dispose();
            }
        };
        backBtn.setBounds(25, 25, 40, 40);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusable(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> { new WelcomePage(); dispose(); });
        add(backBtn);

        // Title & Subtitle
        JLabel title = new JLabel("Create account");
        title.setFont(new Font("SansSerif", Font.BOLD, 28));
        title.setBounds(40, 85, 250, 40);
        add(title);

        JLabel subTitle = new JLabel("<html><font color='black'>Already have an account?</font> <font color='#A855F7'><u>Login</u></font></html>");
        subTitle.setBounds(40, 130, 250, 20);
        subTitle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        subTitle.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) { new LoginPage(); dispose(); }
        });
        add(subTitle);

        // Input 
        addInputs();

        JButton signUpBtn = new JButton("Sign up →");
        signUpBtn.setBackground(new Color(168, 85, 247));
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        signUpBtn.setBounds(200, 520, 140, 50);
        signUpBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpBtn.addActionListener(e -> performRegistration());
        add(signUpBtn);

        setVisible(true);
    }

    private void addInputs() {
    // Name
    JLabel nameIcon = new JLabel("👤");
    nameIcon.setBounds(40, 200, 30, 45);
    add(nameIcon);
    nameF = createStyledField("Enter Name", 200);
    add(nameF);

    // Password
    JLabel passIcon = new JLabel("🔒");
    passIcon.setBounds(40, 270, 30, 45);
    add(passIcon);
    
    String passHint = "Password";
    passF = new JPasswordField(passHint);
    passF.setEchoChar((char) 0);
    passF.setForeground(Color.GRAY);
    passF.setBounds(75, 270, 265, 45);
    passF.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
    
    passF.addFocusListener(new FocusAdapter() {
        public void focusGained(FocusEvent e) {
            String currentPass = new String(passF.getPassword());
            if (currentPass.equals(passHint)) {
                passF.setText("");
                passF.setEchoChar('●');
                passF.setForeground(Color.BLACK);
            }
        }
        public void focusLost(FocusEvent e) {
            if (passF.getPassword().length == 0) {
                passF.setText(passHint);
                passF.setEchoChar((char) 0);
                passF.setForeground(Color.GRAY);
            }
        }
    });
    add(passF);

    // Weight
    JLabel weightIcon = new JLabel("⚖");
    weightIcon.setBounds(40, 340, 30, 45);
    add(weightIcon);
    weightF = createStyledField("Weight (kg)", 340);
    add(weightF);

    // Height
    JLabel heightIcon = new JLabel("📏");
    heightIcon.setBounds(40, 410, 30, 45);
    add(heightIcon);
    heightF = createStyledField("Height (meters)", 410);
    add(heightF);

    setupFieldEvents();
}

    private JTextField createStyledField(String hint, int y) {
        JTextField tf = new JTextField(hint);
        tf.setBounds(75, y, 265, 45);
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

    private void setupFieldEvents() {
        nameF.addActionListener(e -> passF.requestFocus());
        
        passF.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (new String(passF.getPassword()).equals("Password")) {
                    passF.setText(""); passF.setEchoChar('●'); passF.setForeground(Color.BLACK);
                }
            }
        });
        
        passF.addActionListener(e -> weightF.requestFocus());
        weightF.addActionListener(e -> heightF.requestFocus());
     
    }
private void performRegistration() {
 
    String name = nameF.getText();
    String pass = new String(passF.getPassword());
    String weight = weightF.getText();
    String height = heightF.getText();

    if (name.equals("Name") || pass.equals("Password") || 
        weight.equals("Weight (kg)") || height.equals("Height (meters)") ||
        name.isEmpty() || pass.isEmpty()) {
        
        JOptionPane.showMessageDialog(this, "Please fill all fields correctly!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        return; 
    }

User user = new User();
user.name = name;
user.password = pass;

user.weight = Double.parseDouble(weight); 
user.height = Double.parseDouble(height); 

user.saveData(); 
    SuccessDialog dialog = new SuccessDialog(this);
    dialog.setVisible(true);
    
    new LoginPage();
    dispose();
}
}