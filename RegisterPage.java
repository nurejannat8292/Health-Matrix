import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterPage extends JFrame {
    private JTextField nameF, weightF, heightF;
    private JPasswordField passF;
    private JCheckBox showPass; 
    private JComboBox<String> unitCombo;

    public RegisterPage() {
        setTitle("Health Matrix - Sign Up");
        setSize(450, 780); 
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
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawLine(14, 22, 22, 14);
                g2.drawLine(14, 22, 30, 22);
                g2.drawLine(14, 22, 22, 30);
                g2.dispose();
            }
        };
        backBtn.setBounds(40, 30, 44, 44);
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusable(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> { new WelcomePage(); dispose(); });
        add(backBtn);

        JLabel title = new JLabel("Create account");
        title.setFont(new Font("SansSerif", Font.BOLD, 32));
        title.setBounds(40, 95, 350, 45);
        add(title);

        JLabel subTitle = new JLabel("<html><font color='black'>Already have an account?</font> <font color='#A855F7'><u>Login</u></font></html>");
        subTitle.setFont(new Font("SansSerif", Font.PLAIN, 15));
        subTitle.setBounds(40, 145, 350, 25);
        subTitle.setCursor(new Cursor(Cursor.HAND_CURSOR));
        subTitle.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) { new LoginPage(); dispose(); }
        });
        add(subTitle);

        addInputs();
        RoundedButton signUpBtn = new RoundedButton("Sign up →", 25);
        signUpBtn.setBackground(new Color(34, 197, 94)); 
        signUpBtn.setForeground(Color.WHITE);
        signUpBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        signUpBtn.setBounds(240, 600, 160, 50);
        signUpBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpBtn.addActionListener(e -> performRegistration());
        add(signUpBtn);

        setVisible(true);
    }

    private void addInputs() {
        Font iconFont = new Font("SansSerif", Font.PLAIN, 20);

        JLabel nameIcon = new JLabel("👤");
        nameIcon.setFont(iconFont);
        nameIcon.setBounds(40, 200, 35, 50);
        add(nameIcon);
        nameF = createStyledField("Enter Name", 200, 320);
        add(nameF);

        JLabel passIcon = new JLabel("🔒");
        passIcon.setFont(iconFont);
        passIcon.setBounds(40, 280, 35, 50);
        add(passIcon);
        
        String passHint = "Password";
        passF = new JPasswordField(passHint);
        passF.setEchoChar((char) 0);
        passF.setForeground(Color.GRAY);
        passF.setFont(new Font("SansSerif", Font.PLAIN, 16));
        passF.setBounds(85, 280, 320, 50);
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

        showPass = new JCheckBox("Show Password");
        showPass.setFont(new Font("SansSerif", Font.PLAIN, 13));
        showPass.setBounds(85, 335, 150, 32);
        showPass.setBackground(Color.WHITE);
        showPass.setFocusable(false);
        showPass.addActionListener(e -> {
            String currentPass = new String(passF.getPassword());
            if (showPass.isSelected()) {
                passF.setEchoChar((char) 0);
            } else {
                if (!currentPass.equals("Password")) {
                    passF.setEchoChar('●');
                }
            }
        });
        add(showPass);

        JLabel weightIcon = new JLabel("⚖");
        weightIcon.setFont(iconFont);
        weightIcon.setBounds(40, 390, 35, 50);
        add(weightIcon);
        weightF = createStyledField("Weight (kg)", 390, 320);
        add(weightF);

        JLabel heightIcon = new JLabel("📏");
        heightIcon.setFont(iconFont);
        heightIcon.setBounds(40, 480, 35, 50);
        add(heightIcon);
        
        heightF = createStyledField("Height (cm)", 480, 210); 
        add(heightF);

        String[] units = {"cm", "meters", "ft/in"};
        unitCombo = new JComboBox<>(units);
        unitCombo.setBounds(305, 480, 100, 48);
        unitCombo.setFont(new Font("SansSerif", Font.PLAIN, 15));
        unitCombo.setBackground(Color.WHITE);
        unitCombo.setFocusable(false);
        
        unitCombo.addActionListener(e -> {
            String selected = (String) unitCombo.getSelectedItem();
            String currentText = heightF.getText();

            if (currentText.isEmpty() || heightF.getForeground() == Color.GRAY || 
                currentText.startsWith("Height") || currentText.equals("foot")) {
                
                if ("ft/in".equals(selected)) {
                    heightF.setText("foot");
                } else {
                    heightF.setText("Height (" + selected + ")");
                }
                heightF.setForeground(Color.GRAY);
            }
        });
        add(unitCombo);

        setupFieldEvents();
    }

    private JTextField createStyledField(String hint, int y, int width) {
        JTextField tf = new JTextField(hint);
        tf.setBounds(85, y, width, 50);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 16));
        tf.setForeground(Color.GRAY);
        tf.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));
        
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                String text = tf.getText();
                if (text.startsWith("Enter") || text.startsWith("Weight") || text.startsWith("Height") || text.equals("foot")) {
                    tf.setText("");
                    tf.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    String selected = (String) unitCombo.getSelectedItem();
                    if (tf == heightF && "ft/in".equals(selected)) {
                        tf.setText("foot");
                    } else {
                        tf.setText(hint);
                    }
                    tf.setForeground(Color.GRAY);
                }
            }
        });
        return tf;
    }

    private void setupFieldEvents() {
        nameF.addActionListener(e -> passF.requestFocus());
        passF.addActionListener(e -> weightF.requestFocus());
        weightF.addActionListener(e -> heightF.requestFocus());
    }

    private void performRegistration() {
        String name = nameF.getText();
        String pass = new String(passF.getPassword());
        String weight = weightF.getText();
        String heightStr = heightF.getText();
        String selectedUnit = (String) unitCombo.getSelectedItem();

        if (name.equals("Enter Name") || pass.equals("Password") || 
            weight.equals("Weight (kg)") || heightStr.startsWith("Height") || heightStr.equals("foot") ||
            name.isEmpty() || pass.isEmpty()) {
            
            JOptionPane.showMessageDialog(this, "Please fill all fields correctly!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return; 
        }

        StringBuilder errorMsg = new StringBuilder();
        if (pass.length() < 6) {
            errorMsg.append("- Must be at least 6 characters long.\n");
        }
        if (!pass.matches(".*\\d.*")) {
            errorMsg.append("- Must contain at least one numerical digit (0-9).\n");
        }
        if (!pass.matches(".*[!@#$%^&*(),.?\":{}|<>_+\\-=\\[\\]\\\\].*")) {
            errorMsg.append("- Must contain at least one special character (!@#$ etc.).\n");
        }

        if (errorMsg.length() > 0) {
            JOptionPane.showMessageDialog(this, 
                "Your password does not meet our security rules:\n\n" + errorMsg.toString(), 
                "Weak Password Security", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            double finalHeightInMeters = 0;
            double rawHeight = Double.parseDouble(heightStr);

            switch (selectedUnit) {
                case "cm":
                    finalHeightInMeters = rawHeight / 100.0;
                    break;
                case "meters":
                    finalHeightInMeters = rawHeight;
                    break;
                case "ft/in":
                    int feet = (int) rawHeight;
                    double inches = Math.round((rawHeight - feet) * 100);
                    finalHeightInMeters = ((feet * 12) + inches) * 0.0254;
                    break;
            }

            User user = new User();
            user.name = name;
            user.password = pass;
            user.weight = Double.parseDouble(weight); 
            user.height = finalHeightInMeters; 
            user.saveData(); 

            SuccessDialog dialog = new SuccessDialog(this);
            dialog.setVisible(true);
            
            new LoginPage();
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter numeric values with correct format rules for weight and height properties!", "Format Data Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class RoundedButton extends JButton {
        private final int radius;

        public RoundedButton(String label, int radius) {
            super(label);
            this.radius = radius;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isArmed()) {
                g2.setColor(getBackground().darker());
            } else {
                g2.setColor(getBackground());
            }
            
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            super.paintComponent(g2);
            g2.dispose();
        }
    }
}