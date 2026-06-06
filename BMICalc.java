import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.RoundRectangle2D;

public class BMICalc extends JFrame {
    private double currentBMI = 0;
    private String currentStatus = "--";
    private JTextField weightField, heightField, ageField;
    private JButton maleBtn, femaleBtn;
    private GaugePanel gaugePanel;
    private JLabel bmiResultBox, statusResultBox;
    private JPanel chartPanel;
    

    private final Color COLOR_UNDERWEIGHT = new Color(52, 152, 219); 
    private final Color COLOR_NORMAL = new Color(46, 204, 113);      
    private final Color COLOR_OVERWEIGHT = new Color(241, 196, 15);   
    private final Color COLOR_OBESE = new Color(231, 76, 60);         
    
    private final Color COLOR_PRIMARY = new Color(16, 185, 129);     
    private final Color TEXT_DARK = new Color(31, 41, 55);            
    private final Color TEXT_MUTED = new Color(107, 114, 128);        
    private final Color BUTTON_BG = new Color(243, 244, 246);         

    public BMICalc() {
        setTitle("BMI Calculator");
        setSize(430, 920); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        JPanel mainWrapper = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, new Color(244, 247, 246), 0, getHeight(), new Color(230, 242, 237));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        mainWrapper.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 24));
        backBtn.setForeground(TEXT_DARK);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());
        
        JLabel titleLabel = new JLabel("BMI Calculator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(TEXT_DARK);
        
        headerPanel.add(backBtn, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(Box.createHorizontalStrut(40), BorderLayout.EAST);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JPanel genderPanel = new JPanel(new GridLayout(1, 2, 16, 0));
        genderPanel.setOpaque(false);
        genderPanel.setMaximumSize(new Dimension(400, 48));

        maleBtn = new PillButton("Male ♂");
        femaleBtn = new PillButton("Female ♀");
        
        maleBtn.addActionListener(e -> toggleGender(true));
        femaleBtn.addActionListener(e -> toggleGender(false));
        
        genderPanel.add(maleBtn);
        genderPanel.add(femaleBtn);
        toggleGender(true);

        contentPanel.add(Box.createVerticalStrut(15)); 
        contentPanel.add(genderPanel);
        contentPanel.add(Box.createVerticalStrut(15)); 

        ageField = new JTextField("25");
        weightField = new JTextField("70");
        heightField = new JTextField("175");

        contentPanel.add(createStepperCard("AGE", ageField, 1));
        contentPanel.add(Box.createVerticalStrut(12)); 
        contentPanel.add(createStepperCard("WEIGHT (kg)", weightField, 1));
        contentPanel.add(Box.createVerticalStrut(12));
        contentPanel.add(createStepperCard("HEIGHT (cm)", heightField, 1));

        gaugePanel = new GaugePanel();
        contentPanel.add(gaugePanel);

        JPanel resultContainer = new JPanel(new GridLayout(1, 2, 16, 0));
        resultContainer.setOpaque(false);
        resultContainer.setMaximumSize(new Dimension(400, 75));
        bmiResultBox = createOutputLabel("BMI", "0.0");
        statusResultBox = createOutputLabel("Status", "--");
        resultContainer.add(bmiResultBox);
        resultContainer.add(statusResultBox);
        contentPanel.add(resultContainer);
        contentPanel.add(Box.createVerticalStrut(15));

        chartPanel = new JPanel();
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));
        chartPanel.setOpaque(false);
        updateChart();
        contentPanel.add(chartPanel);
  
        contentPanel.add(Box.createVerticalStrut(15));

        JButton calcBtn = new JButton("Calculate BMI") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        calcBtn.setFont(new Font("SansSerif", Font.BOLD, 16));
        calcBtn.setBackground(COLOR_PRIMARY);
        calcBtn.setForeground(Color.WHITE);
        calcBtn.setPreferredSize(new Dimension(400, 46)); 
        calcBtn.setContentAreaFilled(false);
        calcBtn.setBorderPainted(false);
        calcBtn.setFocusPainted(false);
        calcBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        calcBtn.addActionListener(e -> calculate());

        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 0, 5, 0)); 
        footerPanel.add(calcBtn, BorderLayout.CENTER);

        mainWrapper.add(headerPanel, BorderLayout.NORTH);
        mainWrapper.add(contentPanel, BorderLayout.CENTER);
        mainWrapper.add(footerPanel, BorderLayout.SOUTH);

        add(mainWrapper);
        setVisible(true);
    }

    private class PillButton extends JButton {
        public PillButton(String text) {
            super(text);
            setFont(new Font("SansSerif", Font.BOLD, 14));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            g2.setColor(getBackground());
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
            
            if (getBackground() == Color.WHITE) {
                g2.setColor(new Color(218, 230, 224));
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Double(0.75, 0.75, getWidth() - 1.5, getHeight() - 1.5, getHeight(), getHeight()));
            }
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private JPanel createStepperCard(String labelText, JTextField field, double step) {
        JPanel card = new JPanel(new BorderLayout()) {

            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(new Color(0, 40, 20, 10));
                g2.fill(new RoundRectangle2D.Double(0, 2, getWidth(), getHeight() - 2, 16, 16));
                
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight() - 2, 16, 16));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(8, 20, 10, 16));
        card.setMaximumSize(new Dimension(400, 60));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        label.setForeground(new Color(130, 145, 138)); 

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        controls.setOpaque(false);

        JButton minus = createStepBtn("−");
        JButton plus = createStepBtn("+");

        field.setFont(new Font("SansSerif", Font.BOLD, 18));
        field.setForeground(TEXT_DARK);
        field.setBorder(null);
        field.setOpaque(false);
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setPreferredSize(new Dimension(50, 25));

        minus.addActionListener(e -> updateValue(field, -step));
        plus.addActionListener(e -> updateValue(field, step));

        controls.add(minus);
        controls.add(field);
        controls.add(plus);

        card.add(label, BorderLayout.WEST);
        card.add(controls, BorderLayout.EAST);
        return card;
    }

    private JButton createStepBtn(String text) {
        JButton btn = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        btn.setForeground(TEXT_DARK);
        btn.setPreferredSize(new Dimension(32, 32));
        btn.setBackground(BUTTON_BG);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(null);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void updateValue(JTextField field, double delta) {
        try {
            double val = Double.parseDouble(field.getText());
            field.setText(String.valueOf((int)(val + delta)));
        } catch (Exception e) {}
    }

    private void toggleGender(boolean male) {
        
        maleBtn.setBackground(male ? COLOR_PRIMARY : Color.WHITE);
        maleBtn.setForeground(male ? Color.WHITE : TEXT_DARK);
        femaleBtn.setBackground(!male ? COLOR_PRIMARY : Color.WHITE);
        femaleBtn.setForeground(!male ? Color.WHITE : TEXT_DARK);
    }

    private JLabel createOutputLabel(String title, String value) {
        JLabel label = new JLabel("<html><center><font color='#718096' face='SansSerif'><b>" + title + "</b></font><br><font size='5' color='#1F2937'><b>" + value + "</b></font></center></html>", SwingConstants.CENTER) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));
                g2.setColor(new Color(237, 242, 240));
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        label.setOpaque(false);
        return label;
    }

    private void calculate() {
        try {
            double w = Double.parseDouble(weightField.getText());
            double h = Double.parseDouble(heightField.getText()) / 100.0;
            currentBMI = w / (h * h);
            
            if (currentBMI < 18.5) currentStatus = "Underweight";
            else if (currentBMI < 25) currentStatus = "Healthy";
            else if (currentBMI < 30) currentStatus = "Overweight";
            else currentStatus = "Obese";

            bmiResultBox.setText("<html><center><font color='#718096'><b>BMI</b></font><br><font size='6' color='#1F2937'><b>" + String.format("%.1f", currentBMI) + "</b></font></center></html>");
            statusResultBox.setText("<html><center><font color='#718096'><b>Status</b></font><br><font size='5' color='#1F2937'><b>" + currentStatus + "</b></font></center></html>");
            gaugePanel.repaint();
            updateChart();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers");
        }
    }

    private void updateChart() {
        chartPanel.removeAll();
        chartPanel.setBorder(new EmptyBorder(10, 4, 0, 4));
        chartPanel.add(createRow("Underweight", "< 18.5", COLOR_UNDERWEIGHT, currentBMI > 0 && currentBMI < 18.5));
        chartPanel.add(Box.createVerticalStrut(8));
        chartPanel.add(createRow("Healthy", "18.5 - 24.9", COLOR_NORMAL, currentBMI >= 18.5 && currentBMI < 25));
        chartPanel.add(Box.createVerticalStrut(8));
        chartPanel.add(createRow("Overweight", "25.0 - 29.9", COLOR_OVERWEIGHT, currentBMI >= 25 && currentBMI < 30));
        chartPanel.add(Box.createVerticalStrut(8));
        chartPanel.add(createRow("Obese", "> 30.0", COLOR_OBESE, currentBMI >= 30));
        chartPanel.revalidate();
    }

    private JPanel createRow(String category, String range, Color dotColor, boolean highlight) {
        JPanel p = new JPanel(new BorderLayout());
        p.setOpaque(false);
        p.setMaximumSize(new Dimension(400, 24));
        
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);
        
        JLabel dot = new JLabel("● ");
        dot.setFont(new Font("SansSerif", Font.PLAIN, 14));
        dot.setForeground(dotColor);
        
        JLabel catLabel = new JLabel(category);
        catLabel.setFont(new Font("SansSerif", highlight ? Font.BOLD : Font.PLAIN, 14));
        catLabel.setForeground(highlight ? TEXT_DARK : new Color(75, 85, 99));
        
        left.add(dot);
        left.add(catLabel);
        
        JLabel rangeLabel = new JLabel(range);
        rangeLabel.setFont(new Font("SansSerif", highlight ? Font.BOLD : Font.PLAIN, 13));
        rangeLabel.setForeground(highlight ? TEXT_DARK : TEXT_MUTED);
        
        p.add(left, BorderLayout.WEST);
        p.add(rangeLabel, BorderLayout.EAST);
        return p;
    }

    private class GaugePanel extends JPanel {
        public GaugePanel() {
            setPreferredSize(new Dimension(400, 165));
            setOpaque(false);
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int w = 200, h = 200;
            int x = (getWidth() - w) / 2;
            int y = 20; 

            g2.setStroke(new BasicStroke(14, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            g2.setColor(COLOR_UNDERWEIGHT); g2.draw(new Arc2D.Double(x, y, w, h, 180, -43, Arc2D.OPEN));
            g2.setColor(COLOR_NORMAL);      g2.draw(new Arc2D.Double(x, y, w, h, 134, -43, Arc2D.OPEN));
            g2.setColor(COLOR_OVERWEIGHT);  g2.draw(new Arc2D.Double(x, y, w, h, 88, -43, Arc2D.OPEN));
            g2.setColor(COLOR_OBESE);       g2.draw(new Arc2D.Double(x, y, w, h, 42, -43, Arc2D.OPEN));

            double minBMI = 10, maxBMI = 40;
            double clampedBMI = Math.min(Math.max(currentBMI, minBMI), maxBMI);
            double angle = 180 - (clampedBMI - minBMI) * (180.0 / (maxBMI - minBMI));
            double rad = Math.toRadians(angle);
            
            int cx = getWidth() / 2;
            int cy = y + h / 2;
            int length = 76;
            int nx = (int) (cx + length * Math.cos(rad));
            int ny = (int) (cy - length * Math.sin(rad));

            g2.setColor(TEXT_DARK);
            g2.setStroke(new BasicStroke(4, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(cx, cy, nx, ny);
            
            g2.setColor(TEXT_DARK);
            g2.fillOval(cx - 10, cy - 10, 20, 20);
            g2.setColor(Color.WHITE);
            g2.fillOval(cx - 4, cy - 4, 8, 8);
            
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(BMICalc::new);
    }
}