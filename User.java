import java.io.*;
import java.util.Scanner;

public class User {
    public String name, password;
    public double weight, height;
    public boolean isRegistered = false;

    public void loadData() {
        File file = new File("health_data.txt");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line = reader.readLine();
                if (line != null) {
                    String[] parts = line.split(",");
                    name = parts[0]; password = parts[1];
                    weight = Double.parseDouble(parts[2]); 
                    height = Double.parseDouble(parts[3]);
                    isRegistered = true;
                }
            } catch (Exception e) {}
        }
    }

    public void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("health_data.txt"))) {
            writer.write(name + "," + password + "," + weight + "," + height);
        } catch (IOException e) {}
    }

    //Registration input

    public void register(Scanner sc) {
        System.out.println("\n\t\t\t--- Registration ---");
        System.out.print("\t\t\tEnter Name: "); 
        name = sc.next();
        while (true) {
            System.out.print("\t\t\tSet 6-digit Password: ");
            String p = sc.next();
            if (p.length() == 6) { password = p; break; }
            else System.out.println("\t\t\tError: Must be 6 digits!");
        }
        System.out.print("\t\t\tWeight (kg): "); weight = sc.nextDouble();
        System.out.print("\t\t\tHeight (meters): "); height = sc.nextDouble();
        isRegistered = true;
        saveData();
        System.out.println("\t\t\tAccount created successfully!");
    }
    
    //Login input
    
    public boolean login(Scanner sc) {
        System.out.println("\n\t\t\t--- Login ---");
        if (!isRegistered) {
            System.out.println("\t\t\t[!] No user registered yet.");
            return false;
        }
        System.out.print("\n\t\t\tEnter Name: ");
        String n = sc.next();
        System.out.print("\t\t\tEnter Password: "); 
        String p = sc.next();
        return (n.equals(name) && p.equals(password));
    }
}