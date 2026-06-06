import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class HealthMatrix_1 {
    static ArrayList<Medicine> medList = new ArrayList<>();
    static final String MED_FILE = "medicines.txt";
    
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        User user = new User();
        user.loadData();
        loadMedicines();

        while (true) {
            System.out.println("\n\n"); 
            System.out.println(ANSI_CYAN + "\t\t\t=========================================" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "\t\t\t|      " + ANSI_YELLOW + "Welcome to HEALTH MATRIX" + ANSI_CYAN + "         |" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "\t\t\t=========================================" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "\t\t\t|" + ANSI_GREEN + "            1. Register                " + ANSI_CYAN + "|" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "\t\t\t|" + ANSI_GREEN + "            2. Login                   " + ANSI_CYAN + "|" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "\t\t\t|" + ANSI_GREEN + "            3. Exit                    " + ANSI_CYAN + "|" + ANSI_RESET);
            System.out.println(ANSI_CYAN + "\t\t\t=========================================" + ANSI_RESET);
            System.out.print("\t\t\t  " + ANSI_YELLOW + "Choose an option: " + ANSI_RESET);
            
            int choice = sc.nextInt();
            if (choice == 1) user.register(sc);
            else if (choice == 2) {
                if (user.login(sc)) mainMenu(sc, user);
                else System.out.println("\t\t\t  [!] Login Failed!");
            } else if (choice == 3) break;
        }
    }

//main menu

    static void mainMenu(Scanner sc, User user) {
        while (true) {
            System.out.println(ANSI_CYAN + "\n\t\t\t--- MAIN MENU ---" + ANSI_RESET);
            System.out.println("\t\t\t1. BMI Calculator & Suggestion");
            System.out.println("\t\t\t2. Medicine/Insulin Reminder");
            System.out.println("\t\t\t3. Blood Pressure Tracker");
            System.out.println("\t\t\t4. Food & Exercise Guides");
            System.out.println("\t\t\t5. Search Medicine Info");
            System.out.println("\t\t\t6. Logout");
            System.out.print("\t\t\tChoose a feature: ");
            int c = sc.nextInt();

            if (c == 1) showBMI(user);
            else if (c == 2) medicineReminder(sc);
            else if (c == 3) bloodPressureTracker(sc, user);
           else if (c == 4) foodAndExerciseGuides(sc);    
            else if (c == 6) break;
            else System.out.println(ANSI_YELLOW + "\t\t\t[ INFO ] This feature is under development." + ANSI_RESET);
        }
    }

    static void saveMedicines() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(MED_FILE))) {
            for (Medicine m : medList) {
                writer.write(m.name + "," + m.time + "," + m.relation + "," + m.stock + "\n");
            }
        } catch (IOException e) {}
    }

    static void loadMedicines() {
        File file = new File(MED_FILE);
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] p = line.split(",");
                    medList.add(new Medicine(p[0], p[1], p[2], Integer.parseInt(p[3])));
                }
            } catch (Exception e) {}
        }
    }

//medicineReminder

    static void medicineReminder(Scanner sc) {
        while (true) {
            System.out.println(ANSI_CYAN + "\n\t\t\t--- MEDICINE REMINDER ---" + ANSI_RESET);
          System.out.println("\t\t\t1. Add Medicine\n\t\t\t2. View Schedule\n\t\t\t3. Take Medicine\n\t\t\t4. Edit Stock\n\t\t\t5. Remove Medicine\n\t\t\t6. Back");
            System.out.print("\t\t\tSelect: ");
            int mChoice = sc.nextInt();

            if (mChoice == 1) {
                System.out.print("\t\t\tName: "); String name = sc.next();
                System.out.print("\t\t\tTime: "); String time = sc.next();
                System.out.print("\t\t\t1.Before Meal / 2.After Meal: ");
                String rel = (sc.nextInt() == 1) ? "Before Meal" : "After Meal";
                System.out.print("\t\t\tStock: "); int stock = sc.nextInt();
                medList.add(new Medicine(name, time, rel, stock));
                saveMedicines();
            } 
            else if (mChoice == 2) {
                for (Medicine m : medList) 
                    m.displayMed();
            } 
            else if (mChoice == 3) {
                System.out.println("\t\t\tChoose medicine index:");
                for (int i = 0; i < medList.size(); i++) 
                    System.out.println("\t\t\t" + (i+1) + ". " + medList.get(i).name);
                int take = sc.nextInt() - 1;
                if (take >= 0 && take < medList.size()) {
                    medList.get(take).stock--; saveMedicines();
                    System.out.println("\t\t\tStock updated!");
                }
            } else if (mChoice == 4) {
                System.out.println("\t\t\tChoose medicine index to edit:");
                for (int i = 0; i < medList.size(); i++) 
                    System.out.println("\t\t\t" + (i+1) + ". " + medList.get(i).name);
                int editIdx = sc.nextInt() - 1;
                if (editIdx >= 0 && editIdx < medList.size()) {
                    System.out.print("\t\t\tNew Time: "); 
                    medList.get(editIdx).time = sc.next();
                    System.out.print("\t\t\tNew Stock: "); 
                    medList.get(editIdx).stock = sc.nextInt();
                    saveMedicines();
                }
            } else if (mChoice == 5) {
                System.out.println("\t\t\tChoose index to REMOVE:");
                for (int i = 0; i < medList.size(); i++)
                     System.out.println("\t\t\t" + (i+1) + ". " + medList.get(i).name);
                int delIdx = sc.nextInt() - 1;
                if (delIdx >= 0 && delIdx < medList.size()) {
                    medList.remove(delIdx); saveMedicines();
                    System.out.println("\t\t\tRemoved successfully!");
                }
            } else if (mChoice == 6) break;
        }
    }

//BMI

    static void showBMI(User user) {
        double bmi = user.weight / (user.height * user.height);
        String category = (bmi < 18.5) ? "Underweight" : (bmi < 25) ? "Normal Weight" : (bmi < 30) ? "Overweight" : "Obese";

        System.out.println("\n\t\t\t=======================================");
        System.out.println("\t\t\t  " + ANSI_YELLOW +  " HEALTH MATRIX BMI ANALYZER "  + ANSI_RESET  );
        System.out.println("\t\t\t======================================");
        System.out.printf("\t\t\t  Current BMI : %.2f\n", bmi);
        System.out.println("\t\t\t  Status      : " + category);
        System.out.println("\t\t\t======================================");

        System.out.println("\n\t\t\t[ BMI RANGE INDICATOR ]");
        System.out.println("\t\t\t------------------------------------");
        System.out.print("\t\t\tUnderweight  (Below 18.5)    : "); 
        System.out.println(category.equals("Underweight") ? "[ YOUR BMI ]" : "[          ]");
        System.out.print("\t\t\tNormal Weight  (18.5-25)     : "); 
        System.out.println(category.equals("Normal Weight") ? "[ YOUR BMI ]" : "[          ]");
        System.out.print("\t\t\tOverweight  (25-30)          : "); 
        System.out.println(category.equals("Overweight") ? "[ YOUR BMI ]" : "[          ]");
        System.out.print("\t\t\tObese    (Above 30)          : "); 
        System.out.println(category.equals("Obese") ? "[ YOUR BMI ]" : "[          ]");
        
        System.out.println("\n\t\t\t[ QUICK DIET GUIDE ]");
        System.out.println("\t\t\t-----------------------------------------");
        if (category.equals("Underweight")) 
            System.out.println("\t\t\t>> Increase intake of nutritious high-calorie foods (milk, nuts, eggs, rice)\n\t\t\t>> Follow a regular eating schedule and light strength exercises.");
        else if (category.equals("Normal Weight")) 
            System.out.println("\t\t\t>>Maintain a balanced diet with all food groups\n\t\t\t>> Do regular physical activity (30 to 60 minutes daily)");
        else if (category.equals("Overweight")) 
            System.out.println("\t\t\t>> Reduce junk food and sugary items; control portion size\n\t\t\t>> Increase exercise and daily movement (walking, jogging)");
        else System.out.println(">> Follow a strict healthy diet plan (low fat, low sugar)\n\t\t\t>> Consult a doctor and do regular intensive physical activity and Avoid all processed foods.");
        System.out.println("\t\t\t-----------------------------------------");
    }


// 3.Bp

    static void bloodPressureTracker(Scanner sc, User user) {
        System.out.println(ANSI_CYAN + "\n\t\t\t--- BLOOD PRESSURE TRACKER ---" + ANSI_RESET);
        System.out.print("\t\t\tEnter Systolic BP (top number 120): ");
        int systolic = sc.nextInt();
        System.out.print("\t\t\tEnter Diastolic BP (bottom number 80): ");
        int diastolic = sc.nextInt();

        String status;

         if (systolic < 120 && diastolic < 80) {
    status = "Normal";
} 
         else if ((systolic >= 120 && systolic <= 129) && diastolic < 80) {
         status = "Elevated";
         } 
         else if ((systolic >= 130 && systolic <= 139) || (diastolic >= 80 && diastolic <= 89)) {
         status = "High Blood Pressure (Stage 1)";
         } 
         else {
    
         status = "High Blood Pressure (Stage 2 / Crisis)";
         }         

        System.out.println("\t\t\t-----------------------------------------");
        System.out.println("\t\t\t Reading Result: " + ANSI_YELLOW + status + ANSI_RESET);
        System.out.println("\t\t\t-----------------------------------------");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("bp_history.txt", true))) {
            writer.write(systolic + "," + diastolic + "," + status + "," + java.time.LocalDate.now() + "\n");
            System.out.println("\t\t\t[ SUCCESS ] Reading saved to bp_history.txt!");
        } catch (IOException e) {
            System.out.println("\t\t\t[ ERROR ] Failed to save data to file.");
        }
    }

// 4.food guide

    static void foodAndExerciseGuides(Scanner sc) {
        System.out.println(ANSI_CYAN + "\n\t\t\t--- NUTRITION & EXERCISE GUIDE ---" + ANSI_RESET);

        System.out.print("\t\t\tEnter Weight (kg): ");
        double manualWeight = sc.nextDouble();
        System.out.print("\t\t\tEnter Height (meters): ");
        double manualHeight = sc.nextDouble();

        double bmi = manualWeight / (manualHeight * manualHeight);
        String category = (bmi < 18.5) ? "Underweight" : (bmi < 25) ? "Normal" : (bmi < 30) ? "Overweight" : "Obese";

        System.out.println("\t\t\t===============================================================");
        System.out.printf("\t\t\t[ Calculated Metrics ] Target BMI: %.2f (%s)\n", bmi, category);
        System.out.println("\t\t\t===============================================================");
       
        if (category.equals("Underweight")) {
            System.out.println("\t\t\t" + ANSI_YELLOW + " NUTRITION OBJECTIVE: CALORIC SURPLUS & MUSCLE GAIN" + ANSI_RESET);
            System.out.println("\t\t\t   DAILY TARGETS : Focus on energy-dense, high-protein whole foods.");
            System.out.println("\t\t\t   MACRO FOCUS   : Milk, nuts, peanut butter, eggs, and lean chicken.");
            System.out.println("\t\t\t   MEAL TIMING   : Eat 5-6 smaller meals per day; never skip breakfast.");
            System.out.println("\t\t\t   AVOID         : Empty calories (soda, chips) and excessive running.");
            System.out.println("\t\t\t---------------------------------------------------------------");
            System.out.println("\t\t\t" + ANSI_GREEN + " EXERCISE PROTOCOL: HYPERTROPHY & STRENGTH" + ANSI_RESET);
            System.out.println("\t\t\t   STRATEGY     : 30-45 mins of progressive resistance or weight training.");
            System.out.println("\t\t\t   FREQUENCY    : 3 days per week to allow deep muscle recovery.");
        } 
        else if (category.equals("Normal")) {
            System.out.println("\t\t\t" + ANSI_YELLOW + " NUTRITION OBJECTIVE: FITNESS MAINTENANCE & ENERGIZATION" + ANSI_RESET);
            System.out.println("\t\t\t   DAILY TARGETS : Clean, evenly balanced micronutrient distribution.");
            System.out.println("\t\t\t   MACRO FOCUS   : Leafy greens, colorful vegetables, fish, and oats.");
            System.out.println("\t\t\t   HYDRATION    : Drink 1 glass of water every 2 hours (2.5 - 3L total).");
            System.out.println("\t\t\t   AVOID         : Late-night heavy snacking and prolonged sitting gaps.");
            System.out.println("\t\t\t---------------------------------------------------------------");
            System.out.println("\t\t\t" + ANSI_GREEN + " EXERCISE PROTOCOL: FUNCTIONAL ENDURANCE" + ANSI_RESET);
            System.out.println("\t\t\t   STRATEGY     : Combination of brisk walking, cycling, or swimming.");
            System.out.println("\t\t\t   FREQUENCY    : 150 minutes of active movement spread across the week.");
        } 
        else if (category.equals("Overweight")) {
            System.out.println("\t\t\t" + ANSI_YELLOW + " NUTRITION OBJECTIVE: CALORIC DEFICIT & METABOLIC RESET" + ANSI_RESET);
            System.out.println("\t\t\t   DAILY TARGETS : High fiber, low-carb intake to maximize fat burning.");
            System.out.println("\t\t\t   MACRO FOCUS   : Salads, boiled lentils, egg whites, and citrus fruits.");
            System.out.println("\t\t\t   PORTION CTRL  : Use smaller plates; reduce rice/roti intake by 30%.");
            System.out.println("\t\t\t   AVOID         : White sugar, carbonated beverages, and deep-fried items.");
            System.out.println("\t\t\t---------------------------------------------------------------");
            System.out.println("\t\t\t" + ANSI_GREEN + " EXERCISE PROTOCOL: FAT LOSS & CARDIOVASCULAR" + ANSI_RESET);
            System.out.println("\t\t\t   STRATEGY     : 40 mins of steady brisk walking, jogging, or skipping.");
            System.out.println("\t\t\t   FREQUENCY    : 4-5 days per week to optimize caloric expenditure.");
        } 
        else {
            System.out.println("\t\t\t" + ANSI_YELLOW + " NUTRITION OBJECTIVE: THERAPEUTIC WEIGHT REDUCTION" + ANSI_RESET);
            System.out.println("\t\t\t   DAILY TARGETS : Low-fat, zero-refined-sugar nutritional layout.");
            System.out.println("\t\t\t   MACRO FOCUS   : High fiber vegetables, cucumbers, clear soups, green tea.");
            System.out.println("\t\t\t   INTERVENTION  : Intermittent fasting layout or strict meal windowing.");
            System.out.println("\t\t\t   AVOID         : All fast food, bakery pastries, bakery loaves, and ghee.");
            System.out.println("\t\t\t---------------------------------------------------------------");
            System.out.println("\t\t\t" + ANSI_GREEN + " EXERCISE PROTOCOL: LOW-IMPACT KINETICS" + ANSI_RESET);
            System.out.println("\t\t\t   STRATEGY     : Low-impact walking or water aerobics (protects knee joints).");
            System.out.println("\t\t\t   FREQUENCY    : Start with 20 mins daily, gradually scaling up intensity.");
        }
        
        System.out.println("\t\t\t===============================================================");
    }
}