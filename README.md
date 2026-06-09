# Health Matrix

A Java-based personal health management system that includes both a console interface and a modern graphical user interface (GUI) built with Swing.

This application helps users track and manage several aspects of their health, such as BMI, blood pressure, medication schedules, daily water intake, nutrition guidance, and more.

## Features

- User registration and login system with data persistence
- BMI calculator with detailed results, visual indicators, and personalized suggestions
- Blood pressure tracker with reading history and automatic category classification
- Medicine management system (add, edit, track stock and reminders)
- Food and exercise recommendations based on user profile
- Daily water intake tracker
- Personal health profile dashboard

The GUI features a clean, modern design with gradient panels and easy-to-use components.

## Project Structure

```
Health-Matrix/
├── HealthMatrix_1.java          # Console-based version
├── WelcomePage.java             # Application entry point (GUI)
├── LoginPage.java
├── RegisterPage.java
├── Dashboard.java
├── ProfilePage.java
├── BMICalc.java
├── BPTracker.java
├── WaterIntake.java
├── FoodGuide.java
├── MedicineRecord.java
├── User.java                    # User data model
├── Medicine.java                # Medicine data model
├── GradientPanel.java           # Custom UI component
├── SuccessDialog.java
├── health_data.txt              # User data storage
├── medicines.txt                # Medicine records
├── bp_history.txt               # Blood pressure history
└── README.md
```

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Any Java IDE (recommended: IntelliJ IDEA, Eclipse, or VS Code)

### How to Run

**GUI Version (Recommended):**
1. Clone the repository:
   ```bash
   git clone https://github.com/nurejannat8292/Health-Matrix.git
   cd Health-Matrix
   ```

2. Compile the project:
   ```bash
   javac *.java
   ```

3. Start the application:
   ```bash
   java WelcomePage
   ```

**Console Version:**
```bash
javac HealthMatrix_1.java
java HealthMatrix_1
```

## How to Use

1. **Registration**: Create an account with your name, a 6-digit password, weight, and height.
2. **Login**: Use your credentials to access the dashboard.
3. From the dashboard, you can:
   - Calculate your BMI and receive diet/exercise tips
   - Log blood pressure readings
   - Manage your medications
   - Track daily water intake
   - View personalized food and exercise guides
   - Check your profile

All your data is saved locally in text files, so your information persists between sessions.

## Technologies Used

- Java
- Swing (for the graphical interface)
- AWT (for custom UI elements)
- File handling for data storage

## Contributing

Feel free to fork the project and submit pull requests if you'd like to add new features or improvements.

## Author

**Nure Jannat**  
[GitHub Profile](https://github.com/nurejannat8292)
