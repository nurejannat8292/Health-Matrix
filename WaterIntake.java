import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

public class WaterIntake extends JFrame {
    private int currentML = 0;
    private final int goalML = 2700;
    private int selectedAmount = 200;
    private WaterProgressPanel progressPanel;
    private JLabel statusLabel;
    private JPanel selectorRow;

    public WaterIntake() {
        setTitle("Water Tracker");
        setSize(420, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        setContentPane(new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(230, 255, 230),
                        0, getHeight(), new Color(200, 240, 255)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        });
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        topPanel.setOpaque(false);

        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 22));
        backBtn.setBorderPainted(false);
        backBtn.setContentAreaFilled(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());
        topPanel.add(backBtn);

        add(topPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        progressPanel = new WaterProgressPanel();
        progressPanel.setPreferredSize(new Dimension(300, 300));
        progressPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        statusLabel = new JLabel("Daily Goal: 2700 ml");
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        statusLabel.setForeground(Color.DARK_GRAY);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(progressPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(statusLabel);

        add(mainPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setOpaque(false);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));

        selectorRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        selectorRow.setOpaque(false);

        addSelectionLabel("100");
        addSelectionLabel("200");
        addSelectionLabel("300");

        JLabel mlUnit = new JLabel("ml");
        mlUnit.setAlignmentX(Component.CENTER_ALIGNMENT);
        mlUnit.setForeground(Color.GRAY);

        JButton drinkBtn = new JButton("Drink Now");
        drinkBtn.setPreferredSize(new Dimension(220, 55));
        drinkBtn.setMaximumSize(new Dimension(220, 55));
        drinkBtn.setBackground(new Color(110, 190, 110));
        drinkBtn.setForeground(Color.WHITE);
        drinkBtn.setFont(new Font("SansSerif", Font.BOLD, 18));
        drinkBtn.setFocusable(false);
        drinkBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        drinkBtn.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 40, 40);
                super.paint(g2, c);
            }
        });

        drinkBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                drinkBtn.setBackground(new Color(90, 170, 90));
            }

            public void mouseExited(MouseEvent e) {
                drinkBtn.setBackground(new Color(110, 190, 110));
            }
        });

        drinkBtn.addActionListener(e -> {
            currentML += selectedAmount;
            Toolkit.getDefaultToolkit().beep();
            updateProgress();
        });

        controlPanel.add(selectorRow);
        controlPanel.add(mlUnit);
        controlPanel.add(Box.createVerticalStrut(25));
        controlPanel.add(drinkBtn);

        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void addSelectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setCursor(new Cursor(Cursor.HAND_CURSOR));

        lbl.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                selectedAmount = Integer.parseInt(text);
                refreshSelectorUI(text);
            }
        });

        selectorRow.add(lbl);
        refreshSelectorUI("200");
    }

    private void refreshSelectorUI(String selectedText) {
        for (Component c : selectorRow.getComponents()) {
            if (c instanceof JLabel) {
                JLabel l = (JLabel) c;
                if (l.getText().equals(selectedText)) {
                    l.setFont(new Font("SansSerif", Font.BOLD, 28));
                    l.setForeground(Color.WHITE);
                    l.setOpaque(true);
                    l.setBackground(new Color(100, 180, 100));
                    l.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                } else {
                    l.setFont(new Font("SansSerif", Font.BOLD, 20));
                    l.setForeground(Color.GRAY);
                    l.setOpaque(false);
                }
            }
        }
        selectorRow.repaint();
    }

    private void updateProgress() {
        int angle = (int) ((currentML / (double) goalML) * 360);
        progressPanel.setAngle(Math.min(angle, 360));
        statusLabel.setText("You drank " + currentML + " ml today");
    }

    class WaterProgressPanel extends JPanel {
        private int angle = 0;

        public void setAngle(int a) {
            this.angle = a;
            repaint();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int size = 220;
            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            g2.setColor(new Color(240, 240, 240));
            g2.setStroke(new BasicStroke(30, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawOval(x, y, size, size);

            g2.setColor(new Color(180, 255, 180, 80));
            g2.setStroke(new BasicStroke(40));
            g2.draw(new Arc2D.Double(x, y, size, size, 90, -angle, Arc2D.OPEN));

            GradientPaint gp = new GradientPaint(x, y, new Color(126, 188, 89),
                    x + size, y + size, new Color(60, 140, 60));
            g2.setPaint(gp);
            g2.setStroke(new BasicStroke(30, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.draw(new Arc2D.Double(x, y, size, size, 90, -angle, Arc2D.OPEN));

            String p = (int) ((angle / 360.0) * 100) + "%";
            g2.setFont(new Font("SansSerif", Font.BOLD, 40));
            g2.setColor(new Color(40, 40, 40));
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(p, (getWidth() - fm.stringWidth(p)) / 2, getHeight() / 2 + 10);

           int topWidth = 50;
           int bottomWidth = 35;
           int height = 60;

           int centerX = getWidth() / 2;
           int topX = centerX - topWidth / 2;
           int bottomX = centerX - bottomWidth / 2;
           int topY = getHeight() / 2 + 20;
           int bottomY = topY + height;

           Polygon glass = new Polygon();
           glass.addPoint(topX, topY); // top left
           glass.addPoint(topX + topWidth, topY); // top right
           glass.addPoint(bottomX + bottomWidth, bottomY); // bottom right
           glass.addPoint(bottomX, bottomY); // bottom left

           g2.setColor(new Color(80, 80, 80, 180));
           g2.setStroke(new BasicStroke(2f));
           g2.drawPolygon(glass);

           double progress = angle / 360.0;
           int waterHeight = (int)(height * progress);

           Shape oldClip = g2.getClip();
           g2.setClip(glass);

           GradientPaint water = new GradientPaint(
           0, bottomY - waterHeight, new Color(120, 200, 255),
           0, bottomY, new Color(0, 140, 255)
           );
           g2.setPaint(water);
           g2.fillRect(topX, bottomY - waterHeight, topWidth, waterHeight);
           g2.setClip(oldClip);
           g2.setColor(new Color(255, 255, 255, 120));
           g2.drawLine(topX + 10, topY + 5, bottomX + 10, bottomY - 5);
          }
      }
}