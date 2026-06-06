import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class FoodGuide extends JFrame {
    private JTextField bmiField;
    private JPanel contentArea;

    private final Color PRIMARY_GREEN = new Color(16, 185, 129); 
    private final Color BG_LIGHT = new Color(240, 253, 250);      
    private final Color TEXT_DARK = new Color(6, 78, 59);        
    private final Color CARD_BORDER = new Color(209, 250, 229);    
    private final Color TEXT_MUTED = new Color(100, 116, 139);    

    public FoodGuide() {
        setTitle("Health Matrix - Personal Coach");
        setSize(540, 850);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_LIGHT);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(222, 247, 236)),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));

        JLabel title = new JLabel("Personal Health Coach");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(TEXT_DARK);

        JButton backBtn = new JButton("←");
        backBtn.setFont(new Font("SansSerif", Font.BOLD, 24));
        backBtn.setForeground(PRIMARY_GREEN);
        backBtn.setContentAreaFilled(false);
        backBtn.setBorderPainted(false);
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.addActionListener(e -> dispose());

        header.add(backBtn, BorderLayout.WEST);
        header.add(title, BorderLayout.CENTER);
        add(header, BorderLayout.NORTH);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setOpaque(false);
        topContainer.setBorder(BorderFactory.createEmptyBorder(25, 30, 10, 30));

        JPanel inputCard = new JPanel(new GridBagLayout()) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.setColor(CARD_BORDER);
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
                g2.dispose();
            }
        };
        inputCard.setOpaque(false);
        inputCard.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));
        
        JLabel lbl = new JLabel("Enter BMI:");
        lbl.setFont(new Font("SansSerif", Font.BOLD, 16));
        lbl.setForeground(TEXT_MUTED);

        bmiField = new JTextField(5);
        bmiField.setFont(new Font("SansSerif", Font.BOLD, 24));
        bmiField.setHorizontalAlignment(JTextField.CENTER);
        bmiField.setForeground(TEXT_DARK);
        bmiField.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_GREEN));
        bmiField.setOpaque(false);

        JButton analyzeBtn = new JButton("Generate Plan") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), getHeight(), getHeight()));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        styleButton(analyzeBtn);
        analyzeBtn.addActionListener(e -> generatePlan());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 12, 0, 12);
        gbc.fill = GridBagConstraints.VERTICAL;

        inputCard.add(lbl, gbc);
        inputCard.add(bmiField, gbc);
        inputCard.add(analyzeBtn, gbc);
        topContainer.add(inputCard);

        contentArea = new JPanel();
        contentArea.setLayout(new BoxLayout(contentArea, BoxLayout.Y_AXIS));
        contentArea.setOpaque(false);
        contentArea.setBorder(BorderFactory.createEmptyBorder(15, 30, 30, 30));

        JScrollPane scroll = new JScrollPane(contentArea);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_LIGHT);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        JPanel mainLayout = new JPanel(new BorderLayout());
        mainLayout.setOpaque(false);
        mainLayout.add(topContainer, BorderLayout.NORTH);
        mainLayout.add(scroll, BorderLayout.CENTER);

        add(mainLayout, BorderLayout.CENTER);
        showInitialPlaceholder();       
        setVisible(true);
    }

    private void styleButton(JButton btn) {
        btn.setBackground(PRIMARY_GREEN);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusable(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setOpaque(false); 
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void showInitialPlaceholder() {
        contentArea.removeAll();
        JLabel hintLabel = new JLabel("Please enter your BMI above to generate your customized routine blueprint.");
        hintLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));
        hintLabel.setForeground(TEXT_MUTED);
        hintLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        contentArea.add(Box.createVerticalStrut(80));
        contentArea.add(hintLabel);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private void generatePlan() {
        try {
            double bmi = Double.parseDouble(bmiField.getText().trim());
            if (bmi <= 0 || bmi > 60) throw new IllegalArgumentException();
            
            contentArea.removeAll();
            
            int calories, proteinGrams, carbGrams, fatGrams;
            String status, exerciseRoutine, foodPriorities, foodAvoid;
            Color statusColor;

            if (bmi < 18.5) {
                status = "Underweight Range";
                statusColor = new Color(2, 132, 199);
                calories = 2650; proteinGrams = 150; carbGrams = 340; fatGrams = 75;
                
                exerciseRoutine = "• <b>Strength Focus:</b> 3-4 days/week Compound Lifting.<br>" +
                                  "• <b>Routine:</b> Barbell Squats (3x8), Bench Press (3x8), Dumbbell Rows (3x10).<br>" +
                                  "• <b>Cardio Rule:</b> Restrict to under 10-15 minutes light walking to preserve calories.";
                
                foodPriorities = "• High-density wholesome carbs (Oats, brown rice, sweet potatoes).<br>" +
                                 "• Healthy calorie fats (Almonds, peanut butter, whole eggs, avocados).<br>" +
                                 "• Clean lean mass building proteins (Chicken breast, fish, dairy).";
                                 
                foodAvoid =      "• <b>Empty junk food fats:</b> Deep-fried fast food, commercial bakery items.<br>" +
                                 "• <b>Cardio-meal pairing:</b> Avoid drinking large glasses of water immediately *before* meals (fills stomach prematurely).";
            } else if (bmi <= 24.9) {
                status = "Healthy Balanced Range";
                statusColor = PRIMARY_GREEN;
                calories = 2100; proteinGrams = 135; carbGrams = 230; fatGrams = 70;
                
                exerciseRoutine = "• <b>Balanced Hybrid Track:</b> 3-5 days active movement combination.<br>" +
                                  "• <b>Routine:</b> Resistance weight-training 3 days + Moderate Cardio 2 days.<br>" +
                                  "• <b>Target:</b> Aim for 8,000 to 10,000 steps consistently per day.";
                
                foodPriorities = "• Complex structural daily carbs (Quinoa, whole wheat grains, local vegetables).<br>" +
                                 "• High-quality protein recovery building blocks (Lean chicken, eggs, lentils, chickpeas).<br>" +
                                 "• Hydration balance: 2.5 - 3 Liters of pure fresh water daily.";
                                 
                foodAvoid =      "• Highly processed sugar alternatives and commercial sugary mocktails.<br>" +
                                 "• Excessive late-night heavy snacking or deep processed vegetable oils.";
            } else {
                status = "Overweight Range";
                statusColor = new Color(225, 29, 72);
                calories = 1650; proteinGrams = 145; carbGrams = 140; fatGrams = 55;
                
                exerciseRoutine = "• <b>Fat Loss Acceleration:</b> 4-5 days dynamic energy expenditure tracking.<br>" +
                                  "• <b>Cardio:</b> 30-45 mins Brisk Walking or cycling at a steady pace daily.<br>" +
                                  "• <b>HIIT Circuit:</b> 2 days/week bodyweight circuits (Jumping jacks, mountain climbers, planks).";
                
                foodPriorities = "• Volumetric filling foods (Spinach, cabbage, broccoli, cucumbers, crisp leafy greens).<br>" +
                                 "• Low glycemic index fiber sources (Lentils, red beans, clear vegetable broths).<br>" +
                                 "• Satiety builders: Lean white fish cuts and egg whites.";
                                 
                foodAvoid =      "• <b>Refined Sugar & Simple Carbs:</b> White breads, pastries, aerated sodas.<br>" +
                                 "• <b>Hidden Calories:</b> Creamy processed salad dressings, mayo, commercial milk teas, and packaged junk chips.";
            }
            
            buildPlanCards(status, statusColor, calories, proteinGrams, carbGrams, fatGrams, exerciseRoutine, foodPriorities, foodAvoid);
            
            contentArea.revalidate();
            contentArea.repaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please enter a realistic, valid numerical BMI value (e.g., 23.4)", "Input Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void buildPlanCards(String status, Color accent, int calories, int p, int c, int f, String exercise, String prioritize, String avoid) {

        addStepCard("DIAGNOSIS BLUEPRINT", "Assessment: " + status, 
            "The system calculated your personalized health journey roadmap below based on your entered metric data.", accent);

        String macroHTML = 
            "<div style='margin-bottom: 8px;'>Your daily optimal macro target budget to hit:</div>" +
            "<span style='font-size: 20px; font-weight: bold; color: #064E3B;'>" + calories + " kcal</span> / daily goal" +
            "<br><br>" +
            "<table style='width: 100%; font-family: SansSerif; font-size: 11px;'>" +
            "  <tr><td><b>Protein:</b></td><td><span style='color: #10B981; font-weight:bold;'>" + p + "g</span></td><td>• Muscle protection</td></tr>" +
            "  <tr><td><b>Carbs:</b></td><td><span style='color: #F59E0B; font-weight:bold;'>" + c + "g</span></td><td>• Sustainable Energy</td></tr>" +
            "  <tr><td><b>Fats:</b></td><td><span style='color: #EF4444; font-weight:bold;'>" + f + "g</span></td><td>• Hormonal health</td></tr>" +
            "</table>";
        addStepCard("DAILY NUTRIENT ROUTINE", "Calorie & Macro Breakdown", macroHTML, PRIMARY_GREEN);

        addStepCard("EXERCISE ROUTINE SUGGESTIONS", "Target Workouts & Weekly Goals", exercise, new Color(79, 70, 229));

        addStepCard("SUGGESTED FOODS TO PRIORITIZE", "Items to Add to Your Diet", prioritize, PRIMARY_GREEN);

        addStepCard("FOODS AND HABITS TO AVOID", "Items to Restrict completely", avoid, new Color(220, 38, 38));
    }

    private void addStepCard(String label, String title, String body, Color accent) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 16, 16));

                g2.setColor(accent);
                g2.fillRect(0, 0, 5, getHeight());
                
                g2.setColor(new Color(230, 242, 237)); 
                g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, 16, 16));
                g2.dispose();
            }
        };
        card.setLayout(new BorderLayout());
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 20));

        JLabel l = new JLabel(label);
        l.setFont(new Font("SansSerif", Font.BOLD, 11));
        l.setForeground(accent);

        JLabel t = new JLabel(title);
        t.setFont(new Font("SansSerif", Font.BOLD, 16));
        t.setForeground(TEXT_DARK);

        JLabel b = new JLabel("<html><body style='width: 330px; color: #475569; font-family: SansSerif; font-size: 11px; line-height: 14px;'>" + body + "</body></html>");

        JPanel textGroup = new JPanel();
        textGroup.setLayout(new BoxLayout(textGroup, BoxLayout.Y_AXIS));
        textGroup.setOpaque(false);
        textGroup.add(l);
        textGroup.add(Box.createVerticalStrut(4));
        textGroup.add(t);
        textGroup.add(Box.createVerticalStrut(8));
        textGroup.add(b);

        card.add(textGroup, BorderLayout.CENTER);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(card, BorderLayout.CENTER);
        wrapper.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        
        contentArea.add(wrapper);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FoodGuide());
    }
}