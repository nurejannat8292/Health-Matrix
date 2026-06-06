import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.SimpleDateFormat;

public class MedicineRecord extends JFrame {
    private JTextField nameField, stockField, startDateField, endDateField;
    private JRadioButton beforeBtn, afterBtn;
    private JComboBox<String> frequencyBox; 
    private JTable medTable;
    private DefaultTableModel tableModel;
    private final Color successColor = new Color(46, 204, 113);
    private final Color dangerColor = new Color(231, 76, 60);
    private final Color inputBgColor = new Color(248, 249, 250);
    private final Color borderColor = new Color(220, 224, 230);

    public MedicineRecord() {
        setTitle("Health Matrix - Medicine Schedule");
        setSize(580, 780); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        topPanel.setBackground(Color.WHITE);
        JButton backBtn = new JButton("← Back");
        backBtn.setFont(new Font("SansSerif", Font.PLAIN, 14));
        backBtn.setForeground(Color.GRAY);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());
        topPanel.add(backBtn);
        add(topPanel, BorderLayout.NORTH);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(Color.WHITE);
        mainContent.setBorder(BorderFactory.createEmptyBorder(0, 40, 20, 40));


        JLabel title = new JLabel("Medicine Inventory");
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(new Color(44, 62, 80));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainContent.add(title);
        mainContent.add(Box.createVerticalStrut(25)); 

        JPanel inputGrid = new JPanel(new GridLayout(5, 2, 15, 18)); 
        inputGrid.setBackground(Color.WHITE);
        inputGrid.setMaximumSize(new Dimension(500, 240));

        nameField = createStyledInput("Medicine Name");
        stockField = createStyledInput("Total Stock");
        startDateField = createStyledInput("Start Date");
        endDateField = createStyledInput("End Date");

        String[] frequencies = {"1 time (Daily)", "2 times (Morning-Night)", "3 times (M-A-N)", "Once a Week"};
        frequencyBox = new JComboBox<>(frequencies) {

            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }

            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
        };
        
        frequencyBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI()); 
        frequencyBox.setOpaque(false);
        frequencyBox.setBackground(inputBgColor);
        frequencyBox.setFont(new Font("SansSerif", Font.PLAIN, 13));
        frequencyBox.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));

        nameField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) { if(e.getKeyCode() == KeyEvent.VK_ENTER) stockField.requestFocus(); }
        });

        startDateField.setEditable(false);
        startDateField.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { showDatePicker(startDateField); } });
        endDateField.setEditable(false);
        endDateField.addMouseListener(new MouseAdapter() { public void mouseClicked(MouseEvent e) { showDatePicker(endDateField); } });

        Font labelFont = new Font("SansSerif", Font.BOLD, 13);
        Color labelColor = new Color(127, 140, 141);

        inputGrid.add(createStyledLabel("Name", labelFont, labelColor)); inputGrid.add(nameField);
        inputGrid.add(createStyledLabel("Stock", labelFont, labelColor)); inputGrid.add(stockField);
        inputGrid.add(createStyledLabel("Frequency", labelFont, labelColor)); inputGrid.add(frequencyBox);
        inputGrid.add(createStyledLabel("Start Date", labelFont, labelColor)); inputGrid.add(startDateField);
        inputGrid.add(createStyledLabel("End Date", labelFont, labelColor)); inputGrid.add(endDateField);

        mainContent.add(inputGrid);
        mainContent.add(Box.createVerticalStrut(15));

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
        radioPanel.setBackground(Color.WHITE);
        radioPanel.setMaximumSize(new Dimension(500, 35));
        
        beforeBtn = new JRadioButton("Before Meal", true);
        afterBtn = new JRadioButton("After Meal");
        ButtonGroup group = new ButtonGroup();
        group.add(beforeBtn); group.add(afterBtn);
        
        Font radioFont = new Font("SansSerif", Font.PLAIN, 13);
        beforeBtn.setFont(radioFont); afterBtn.setFont(radioFont);
        beforeBtn.setBackground(Color.WHITE); afterBtn.setBackground(Color.WHITE);
        
        radioPanel.add(createStyledLabel("Meal Relation:  ", labelFont, labelColor));
        radioPanel.add(beforeBtn); 
        radioPanel.add(Box.createHorizontalStrut(15));
        radioPanel.add(afterBtn);
        mainContent.add(radioPanel);
        mainContent.add(Box.createVerticalStrut(20)); 

        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        btnPanel.setBackground(Color.WHITE);
        btnPanel.setMaximumSize(new Dimension(500, 40));
        
        JButton addBtn = new RoundedButton("Add Schedule", successColor);
        addBtn.addActionListener(e -> saveMedicine());
        
        JButton delBtn = new RoundedButton("Remove Selected", dangerColor);
        delBtn.addActionListener(e -> deleteMedicine());
        
        btnPanel.add(addBtn); btnPanel.add(delBtn);
        mainContent.add(btnPanel);
        mainContent.add(Box.createVerticalStrut(25)); 

        String[] columns = {"Start", "End", "Name", "Frequency", "Meal", "Stock"};
        tableModel = new DefaultTableModel(columns, 0);
        medTable = new JTable(tableModel);
        medTable.setRowHeight(35);
        medTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        medTable.setSelectionBackground(new Color(236, 240, 241));
        medTable.setSelectionForeground(Color.BLACK);
        medTable.setShowGrid(false);
        medTable.setIntercellSpacing(new Dimension(0, 0));
        
        JTableHeader header = medTable.getTableHeader();
        header.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.setBackground(new Color(242, 244, 245));
        header.setForeground(new Color(120, 130, 140));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, borderColor));

        JScrollPane scrollPane = new JScrollPane(medTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(borderColor, 1));
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainContent.add(scrollPane);

        add(mainContent, BorderLayout.CENTER);
        loadMedicines();
        setVisible(true);
    }

    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font);
        lbl.setForeground(color);
        return lbl;
    }

    private JTextField createStyledInput(String hint) {
        JTextField tf = new JTextField(hint) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }

            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(borderColor);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
            }
        };
        tf.setOpaque(false);
        tf.setBackground(inputBgColor);
        tf.setForeground(Color.GRAY);
        tf.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
        
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) { if(tf.getText().equals(hint)) { tf.setText(""); tf.setForeground(Color.BLACK); } }
            public void focusLost(FocusEvent e) { if(tf.getText().isEmpty()) { tf.setText(hint); tf.setForeground(Color.GRAY); } }
        });
        return tf;
    }

    private void showDatePicker(JTextField target) {
        JSpinner s = new JSpinner(new SpinnerDateModel());
        s.setEditor(new JSpinner.DateEditor(s, "dd-MM-yyyy"));
        if (JOptionPane.showOptionDialog(null, s, "Set Date", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, null, null) == JOptionPane.OK_OPTION) {
            target.setText(new SimpleDateFormat("dd-MM-yyyy").format(s.getValue()));
            target.setForeground(Color.BLACK);
        }
    }

    private void saveMedicine() {
        String name = nameField.getText();
        String stock = stockField.getText();
        String freq = frequencyBox.getSelectedItem().toString();
        String start = startDateField.getText();
        String end = endDateField.getText();
        String rel = beforeBtn.isSelected() ? "Before" : "After";

        if(name.equals("Medicine Name") || start.equals("Start Date") || end.equals("End Date")) return;

        tableModel.addRow(new Object[]{start, end, name, freq, rel, stock});
        saveToFile();
        
        nameField.setText("Medicine Name"); nameField.setForeground(Color.GRAY);
        stockField.setText("Total Stock"); stockField.setForeground(Color.GRAY);
    }

    private void deleteMedicine() {
        int row = medTable.getSelectedRow();
        if (row != -1) { tableModel.removeRow(row); saveToFile(); }
    }

    private void saveToFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("medicines.txt"))) {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < tableModel.getColumnCount(); j++) {
                    sb.append(tableModel.getValueAt(i, j)).append(j == tableModel.getColumnCount()-1 ? "" : ",");
                }
                bw.write(sb.toString() + "\n");
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void loadMedicines() {
        tableModel.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader("medicines.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length == 6) tableModel.addRow(d); 
            }
        } catch (IOException e) { }
    }

    class RoundedButton extends JButton {
        private Color baseColor;

        public RoundedButton(String label, Color baseColor) {
            super(label);
            this.baseColor = baseColor;
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g);
            g2.dispose();
        }
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}
        SwingUtilities.invokeLater(MedicineRecord::new);
    }
}