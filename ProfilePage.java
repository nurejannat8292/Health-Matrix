import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ProfilePage extends JFrame {
    private JTextField nameF, weightF, heightF;
    private String currentUser;
    private Color themeColor = new Color(168, 85, 247); 

    public ProfilePage(String userName) {
        this.currentUser = userName;
        setTitle("Health Matrix - Profile");
        setSize(400, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);
        addHeader(userName);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(null);
        contentPanel.setOpaque(false);
        contentPanel.setBounds(0, 80, 400, 470);
        add(contentPanel);

        addAvatar(contentPanel, userName);
        addInputs(contentPanel);

        JButton updateBtn = new JButton("Update Profile");
        updateBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setBackground(themeColor);
        updateBtn.setBounds(50, 360, 300, 45);
        updateBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        updateBtn.setFocusable(false);
        updateBtn.setBorderPainted(false);
        
        updateBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { updateBtn.setBackground(themeColor.darker()); }
            public void mouseExited(MouseEvent e) { updateBtn.setBackground(themeColor); }
        }); 
        updateBtn.addActionListener(e -> updateData());
        contentPanel.add(updateBtn);
        loadUserData();
        setVisible(true);
    }

    private void addHeader(String userName) {
        JButton backBtn = new JButton("←") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(245, 245, 245)); 
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        backBtn.setBounds(25, 25, 40, 40);
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setFocusable(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());
        add(backBtn);

        JLabel title = new JLabel("Your Profile");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setBounds(85, 33, 150, 25);
        add(title);
    }

    private void addAvatar(JPanel panel, String name) {
        JLabel avatarLabel = new JLabel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(240, 240, 240)); // Circle color
                g2.fillOval(0, 0, getWidth(), getHeight());
                
                String initial = (name != null && !name.isEmpty()) ? name.substring(0,1).toUpperCase() : "?";
                g2.setColor(themeColor);
                g2.setFont(new Font("SansSerif", Font.BOLD, 30));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(initial)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(initial, x, y);
                
                g2.dispose();
            }
        };
        avatarLabel.setBounds(160, 20, 80, 80);
        panel.add(avatarLabel);
    }

    private void addInputs(JPanel panel) {
  
        autoAddLabelAndField(panel, "Full Name", 130, nameF = new JTextField());
        autoAddLabelAndField(panel, "Current Weight (kg)", 200, weightF = new JTextField());
        autoAddLabelAndField(panel, "Current Height (meters)", 270, heightF = new JTextField());
    }

    private void autoAddLabelAndField(JPanel panel, String labelText, int y, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.PLAIN, 12));
        label.setForeground(Color.GRAY);
        label.setBounds(50, y, 200, 15);
        panel.add(label);

        field.setBounds(50, y + 20, 300, 35);
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(new MatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
        field.setBackground(Color.WHITE);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                field.setBorder(new MatteBorder(0, 0, 2, 0, themeColor));
            }
            public void focusLost(FocusEvent e) {
                field.setBorder(new MatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
            }
        });
        panel.add(field);
    }

    private void loadUserData() {
        File file = new File("health_data.txt");       
        if (!file.exists()) {
            return; 
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); 
                
                if (data.length >= 4 && data[0].trim().equalsIgnoreCase(currentUser.trim())) {
                    nameF.setText(data[0]);
                    weightF.setText(data[2]); 
                    heightF.setText(data[3]); 
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void updateData() {
        String newName = nameF.getText().trim();
        String newWeight = weightF.getText().trim();
        String newHeight = heightF.getText().trim();

        if (newName.isEmpty() || newWeight.isEmpty() || newHeight.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }
       
        JOptionPane.showMessageDialog(this, "Profile Updated Successfully!");
        dispose();
    }
}