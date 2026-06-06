import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BPTracker extends JFrame {
    private JLabel statusLabel, adviceLabel;
    private JTextField sysField, diaField, dateField, timeField;
    private JPanel indicatorPanel;

    public BPTracker() {
        setTitle("Health Matrix - BP Expert");
        setSize(450, 750); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());
 
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setForeground(new Color(100, 116, 139));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());
        topPanel.add(backBtn);
        add(topPanel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 50));

        JLabel title = new JLabel("Blood Pressure Log");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(30, 41, 59));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContent.add(title);
        mainContent.add(Box.createVerticalStrut(25));

        JPanel inputGrid = new JPanel(new GridLayout(4, 2, 8, 15));
        inputGrid.setBackground(Color.WHITE);
        inputGrid.setMaximumSize(new Dimension(350, 200));

        sysField = createStyledInput("120");
        diaField = createStyledInput("80");
        dateField = createStyledInput(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        timeField = createStyledInput(new SimpleDateFormat("hh:mm a").format(new Date()));

        Font labelFont = new Font("SansSerif", Font.BOLD, 14);
        Color labelColor = new Color(71, 85, 105);

        inputGrid.add(createStyledLabel("Systolic:", labelFont, labelColor));
        inputGrid.add(sysField);
        inputGrid.add(createStyledLabel("Diastolic:", labelFont, labelColor));
        inputGrid.add(diaField);
        inputGrid.add(createStyledLabel("Date:", labelFont, labelColor));
        inputGrid.add(dateField);
        inputGrid.add(createStyledLabel("Time:", labelFont, labelColor));
        inputGrid.add(timeField);

        mainContent.add(inputGrid);
        mainContent.add(Box.createVerticalStrut(25));

        indicatorPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
        };
        indicatorPanel.setOpaque(false);
        indicatorPanel.setMaximumSize(new Dimension(350, 10));
        indicatorPanel.setBackground(new Color(241, 245, 249));
        mainContent.add(indicatorPanel);

        statusLabel = new JLabel("Status: Ready", SwingConstants.CENTER);
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContent.add(Box.createVerticalStrut(12));
        mainContent.add(statusLabel);

        adviceLabel = new JLabel("Input readings to see status", SwingConstants.CENTER);
        adviceLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        adviceLabel.setForeground(new Color(148, 163, 184));
        adviceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContent.add(adviceLabel);
        mainContent.add(Box.createVerticalStrut(30));

        JButton checkBtn = new RoundedButton("Check BP Status", new Color(168, 85, 247));
        checkBtn.addActionListener(e -> processBPData());

        JButton saveBtn = new RoundedButton("Save Record", new Color(239, 68, 68));
        saveBtn.addActionListener(e -> {
            try {
                int s = Integer.parseInt(sysField.getText());
                int d = Integer.parseInt(diaField.getText());
                saveToFile(s, d);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Enter valid numbers!");
            }
        });

        JButton viewBtn = new RoundedButton("See Records", new Color(59, 130, 246));
        viewBtn.addActionListener(e -> openRecordWindow());

        JButton graphBtn = new RoundedButton("View Graph Trends", new Color(16, 185, 129));
        graphBtn.addActionListener(e -> openGraphWindow());

        mainContent.add(checkBtn);
        mainContent.add(Box.createVerticalStrut(12));
        mainContent.add(saveBtn);
        mainContent.add(Box.createVerticalStrut(12));
        mainContent.add(viewBtn);
        mainContent.add(Box.createVerticalStrut(12));
        mainContent.add(graphBtn);

        add(mainContent, BorderLayout.CENTER);
        setVisible(true);
    }

    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    private JTextField createStyledInput(String text) {
        JTextField tf = new JTextField(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.setColor(new Color(226, 232, 240));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        tf.setOpaque(false);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 14));
        tf.setForeground(new Color(51, 65, 85));
        tf.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        tf.setBackground(new Color(250, 250, 250));
        return tf;
    }

    private void processBPData() {
        try {
            int sys = Integer.parseInt(sysField.getText());
            int dia = Integer.parseInt(diaField.getText());

            if (sys >= 180 || dia > 120) {
                setUI("HYPERTENSIVE CRISIS", new Color(153, 27, 27), "EMERGENCY! Consult doctor.");
            } else if (sys < 90 || dia < 60) {
                setUI("LOW BP", new Color(29, 78, 216), "Drink fluids.");
            } else if (sys >= 140 || dia > 90) {
                setUI("STAGE 2", new Color(220, 38, 38), "Medical attention needed.");
            } else if (sys >= 130 || dia > 80) {
                setUI("STAGE 1", new Color(217, 119, 6), "Consult doctor.");
            } else if (sys > 120) {
                setUI("ELEVATED", new Color(202, 138, 4), "Lifestyle care needed.");
            } else {
                setUI("NORMAL", new Color(22, 163, 74), "All good!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter valid numbers!");
        }
    }
    private void setUI(String status, Color color, String advice) {
        statusLabel.setText(status);
        statusLabel.setForeground(color);
        indicatorPanel.setBackground(color);
        adviceLabel.setText(advice);
        repaint();
    }
    private void saveToFile(int s, int d) {
        try (FileWriter fw = new FileWriter("bp_history.txt", true)) {
            fw.write(dateField.getText() + " | " + timeField.getText() + " | " +
                    s + "/" + d + " | " + statusLabel.getText() + "\n");
            JOptionPane.showMessageDialog(this, "Record Saved!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving file!");
        }
    }

    private void openRecordWindow() {
        JFrame frame = new JFrame("BP Records");
        frame.setSize(400, 500);
        frame.setLocationRelativeTo(this);
        frame.getContentPane().setBackground(Color.WHITE);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        area.setForeground(new Color(51, 65, 85));

        try (BufferedReader br = new BufferedReader(new FileReader("bp_history.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                area.append(line + "\n");
            }
        } catch (Exception e) {
            area.setText("No records found!");
        }

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        frame.add(scrollPane);
        frame.setVisible(true);
    }

    private void openGraphWindow() {
        List<Integer> systolicData = new ArrayList<>();
        List<Integer> diastolicData = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader("bp_history.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 3) {
                    String date = parts[0].trim();
                    String[] bp = parts[2].trim().split("/");
                    if (bp.length == 2) {
                        dates.add(date);
                        systolicData.add(Integer.parseInt(bp[0].trim()));
                        diastolicData.add(Integer.parseInt(bp[1].trim()));
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No records found to generate graph!");
            return;
        }

        if (systolicData.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Not enough data entries to map.");
            return;
        }

        JFrame graphFrame = new JFrame("BP History Trends");
        graphFrame.setSize(620, 480);
        graphFrame.setLocationRelativeTo(this);
        graphFrame.add(new GraphPanel(systolicData, diastolicData, dates));
        graphFrame.setVisible(true);
    }

    class GraphPanel extends JPanel {
        private final List<Integer> sysList;
        private final List<Integer> diaList;
        private final List<String> dateList;
        private final int padding = 50;
        private final int labelPadding = 30;

        public GraphPanel(List<Integer> sysList, List<Integer> diaList, List<String> dateList) {
            this.sysList = sysList;
            this.diaList = diaList;
            this.dateList = dateList;
            setBackground(Color.WHITE);
        }
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            int minDataValue = 40; 
            int maxDataValue = 200; 
            
            double xScale = ((double) width - (2 * padding) - labelPadding) / (sysList.size() > 1 ? sysList.size() - 1 : 1);
            double yScale = ((double) height - (2 * padding) - labelPadding) / (maxDataValue - minDataValue);

            int graphTop = padding;
            int graphBottom = height - padding - labelPadding;
            int graphLeft = padding + labelPadding;
            int graphRight = width - padding;

            int y140 = (int) ((maxDataValue - 140) * yScale + padding);
            g2.setColor(new Color(254, 242, 242)); 
            g2.fillRect(graphLeft, graphTop, graphRight - graphLeft, y140 - graphTop);

            int y120 = (int) ((maxDataValue - 120) * yScale + padding);
            g2.setColor(new Color(255, 251, 235)); 
            g2.fillRect(graphLeft, y140, graphRight - graphLeft, y120 - y140);
    
            int y80 = (int) ((maxDataValue - 80) * yScale + padding);
            g2.setColor(new Color(240, 253, 250)); 
            g2.fillRect(graphLeft, y120, graphRight - graphLeft, y80 - y120);

            g2.setColor(new Color(240, 246, 255)); 
            g2.fillRect(graphLeft, y80, graphRight - graphLeft, graphBottom - y80);

            List<Point> sysPoints = new ArrayList<>();
            List<Point> diaPoints = new ArrayList<>();
            for (int i = 0; i < sysList.size(); i++) {
                int xCoord = (int) (i * xScale + graphLeft);
                int ySysCoord = (int) ((maxDataValue - sysList.get(i)) * yScale + padding);
                int yDiaCoord = (int) ((maxDataValue - diaList.get(i)) * yScale + padding);
                sysPoints.add(new Point(xCoord, ySysCoord));
                diaPoints.add(new Point(xCoord, yDiaCoord));
            }

            int gridCount = 8;
            for (int i = 0; i <= gridCount; i++) {
                int bpVal = minDataValue + (i * (maxDataValue - minDataValue) / gridCount);
                int y = (int) ((maxDataValue - bpVal) * yScale + padding);
                
                g2.setColor(new Color(226, 232, 240));
                g2.setStroke(new BasicStroke(1f));
                g2.drawLine(graphLeft, y, graphRight, y);
                
                g2.setColor(new Color(100, 116, 139));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 11));
                g2.drawString(String.valueOf(bpVal), padding - 15, y + 4);
            }

            g2.setColor(new Color(71, 85, 105));
            g2.drawLine(graphLeft, graphBottom, graphLeft, graphTop);
            g2.drawLine(graphLeft, graphBottom, graphRight, graphBottom);

            g2.setStroke(new BasicStroke(3.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            g2.setColor(new Color(147, 51, 234));
            for (int i = 0; i < sysPoints.size() - 1; i++) {
                g2.drawLine(sysPoints.get(i).x, sysPoints.get(i).y, sysPoints.get(i + 1).x, sysPoints.get(i + 1).y);
            }

            g2.setColor(new Color(37, 99, 235));
            for (int i = 0; i < diaPoints.size() - 1; i++) {
                g2.drawLine(diaPoints.get(i).x, diaPoints.get(i).y, diaPoints.get(i + 1).x, diaPoints.get(i + 1).y);
            }


            for (int i = 0; i < sysPoints.size(); i++) {
                g2.setColor(Color.WHITE);
                g2.fillOval(sysPoints.get(i).x - 6, sysPoints.get(i).y - 6, 12, 12);
                g2.setColor(new Color(147, 51, 234));
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawOval(sysPoints.get(i).x - 6, sysPoints.get(i).y - 6, 12, 12);

                g2.setColor(Color.WHITE);
                g2.fillOval(diaPoints.get(i).x - 6, diaPoints.get(i).y - 6, 12, 12);
                g2.setColor(new Color(37, 99, 235));
                g2.drawOval(diaPoints.get(i).x - 6, diaPoints.get(i).y - 6, 12, 12);

                if (sysPoints.size() < 7 || i % (sysPoints.size() / 4 + 1) == 0) {
                    g2.setColor(new Color(71, 85, 105));
                    g2.setFont(new Font("SansSerif", Font.BOLD, 10));
                    g2.drawString(dateList.get(i), sysPoints.get(i).x - 22, height - padding + 15);
                }
            }

            g2.setFont(new Font("SansSerif", Font.BOLD, 12));
 
            g2.setColor(new Color(147, 51, 234));
            g2.fillOval(padding + 10, 16, 10, 10);
            g2.setColor(new Color(30, 41, 59));
            g2.drawString("Systolic (Upper Reading)", padding + 25, 25);

            g2.setColor(new Color(37, 99, 235));
            g2.fillOval(padding + 200, 16, 10, 10);
            g2.setColor(new Color(30, 41, 59));
            g2.drawString("Diastolic (Lower Reading)", padding + 215, 25);

            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            g2.setColor(new Color(185, 28, 28));
            g2.drawString("■ High BP Zone", width - padding - 100, padding - 20);
            g2.setColor(new Color(21, 128, 61));
            g2.drawString("■ Normal Range", width - padding - 100, padding - 6);
        }
    }
    class RoundedButton extends JButton {
        private Color baseColor;

        public RoundedButton(String label, Color color) {
            super(label);
            this.baseColor = color;
            setFont(new Font("SansSerif", Font.BOLD, 15));
            setForeground(Color.WHITE);
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isPressed()) {
                g2.setColor(baseColor.darker());
            } else if (getModel().isRollover()) {
                g2.setColor(baseColor.brighter());
            } else {
                g2.setColor(baseColor);
            }
            
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
            
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BPTracker::new);
    }
}