/**
 * Project:     Vehicular Cloud Real Time System
 * Class:       VCRTSGUI.java
 * Authors:     Shameka Williams, Farheen Mahmud, Jery Vizhnay, Bryan Benjamin, Hasan Mousa
 * Date:        October 7, 2024   
 * 
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.Properties;
public class VCRTSGUI {
	
    private JFrame frame;
    
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JRadioButton clientButton, ownerButton;
    private JPanel mainPanel, clientPanel, ownerPanel;
    private JTextField clientIdField, jobDurationField, jobDeadlineField;
    private JTextField ownerIdField, vehicleInfoField, residencyTimeField;
    private JButton submitButton;
    private User currentUser;
    private Server server;
    private CardLayout cardLayout;
  //Set up necessary components for GUI
    public VCRTSGUI() {
        frame = new JFrame("Vehicular Cloud Real Time System (VCRTS)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new CardLayout());
        server = new Server();
        createLoginScreen();
        createSignUpScreen();
        createMainPage();  // This will now display the main GUI page setup in VCRTS_GUI
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new VCRTSGUI();
    }

    // Login Screen
    private void createLoginScreen() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        // GBC for gui layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing between components

        Dimension fieldSize = new Dimension(200, 25);
        Dimension buttonSize = new Dimension(100, 25); 

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();
        usernameField.setPreferredSize(fieldSize);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(fieldSize);

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(buttonSize);
        loginButton.addActionListener(e -> loginUser());

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(buttonSize);
        signUpButton.addActionListener(e -> showSignUpScreen());

        // Layout components in the login panel to make sure that our login page is aligned with each other
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(loginButton, gbc);

        gbc.insets = new Insets(5, 105, 5, 5); // Offset the Sign Up button to the right of Login
        loginPanel.add(signUpButton, gbc);

        frame.add(loginPanel, "Login");

        // Set the exact size for the frame after adding the login panel
        frame.setSize(409, 292);
        frame.setResizable(false); // Lock the frame size
    }






    // Sign-Up Screen
    private void createSignUpScreen() {
        JPanel signUpPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL; // Allow text fields to stretch horizontally
        gbc.insets = new Insets(5, 5, 5, 5); // Spacing between components

        Dimension fieldSize = new Dimension(200, 25); // Set a fixed size for text fields

        // First Name field
        JLabel fNameLabel = new JLabel("First Name:");
        JTextField fNameField = new JTextField();
        fNameField.setPreferredSize(fieldSize);

        // Last Name field
        JLabel lNameLabel = new JLabel("Last Name:");
        JTextField lNameField = new JTextField();
        lNameField.setPreferredSize(fieldSize);

        // Username field
        JLabel usernameLabel = new JLabel("Username:");
        JTextField newUsernameField = new JTextField();
        newUsernameField.setPreferredSize(fieldSize);

        // Email field
        JLabel emailLabel = new JLabel("Email Address:");
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(fieldSize);

        // Date of Birth field with JDatePicker
        JLabel dobLabel = new JLabel("Date of Birth:");
        UtilDateModel dateModel = new UtilDateModel();
        Properties properties = new Properties();
        properties.put("text.today", "Today");
        properties.put("text.month", "Month");
        properties.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, properties);
        JDatePickerImpl dobPicker = new JDatePickerImpl(datePanel, new org.jdatepicker.impl.DateComponentFormatter());

        // Password field
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setPreferredSize(fieldSize);

        // Confirm Password field make sure to actually add password confirmation
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(fieldSize);

        // Register and Back buttons
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser(newUsernameField.getText(), newPasswordField.getText()));
        JButton backButton = new JButton("Back to Login");
        backButton.addActionListener(e -> showLoginScreen());

        // Layout components with GridBagConstraints
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_END;
        signUpPanel.add(fNameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_START;
        signUpPanel.add(fNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_END;
        signUpPanel.add(lNameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.anchor = GridBagConstraints.LINE_START;
        signUpPanel.add(lNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_END;
        signUpPanel.add(usernameLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_START;
        signUpPanel.add(newUsernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.LINE_END;
        signUpPanel.add(emailLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.LINE_START;
        signUpPanel.add(emailField, gbc);

        // Add Date of Birth label and picker
        gbc.gridx = 0; gbc.gridy = 4; gbc.anchor = GridBagConstraints.LINE_END;
        signUpPanel.add(dobLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.LINE_START;
        signUpPanel.add(dobPicker, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.anchor = GridBagConstraints.LINE_END;
        signUpPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 5; gbc.anchor = GridBagConstraints.LINE_START;
        signUpPanel.add(newPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.anchor = GridBagConstraints.LINE_END;
        signUpPanel.add(confirmPasswordLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.anchor = GridBagConstraints.LINE_START;
        signUpPanel.add(confirmPasswordField, gbc);

        // Place Register and Back buttons side by side
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        signUpPanel.add(buttonPanel, gbc);

        // Set the preferred size for the sign-up panel
        signUpPanel.setPreferredSize(new Dimension(409, 292));
        frame.add(signUpPanel, "SignUp");
    }



    // Main GUI from VCRTS_GUI
    private void createMainPage() {
        JPanel mainPagePanel = new JPanel(new GridBagLayout()); // Main panel with GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Spacing around components
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Role selection panel
        JPanel rolePanel = new JPanel(new FlowLayout());
        clientButton = new JRadioButton("Client");
        ownerButton = new JRadioButton("Owner");
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(clientButton);
        roleGroup.add(ownerButton);
        rolePanel.add(clientButton);
        rolePanel.add(ownerButton);

        // Main input panels for client and owner
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        clientPanel = createInputPanel("Client ID:", "Job Duration (hrs):", "Job Deadline:");
        ownerPanel = createInputPanel("Owner ID:", "Vehicle Info:", "Residency Time (hrs):");

        mainPanel.add(clientPanel, "Client");
        mainPanel.add(ownerPanel, "Owner");

        // Submit button
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> handleSubmission());

        clientButton.addActionListener(e -> cardLayout.show(mainPanel, "Client"));
        ownerButton.addActionListener(e -> cardLayout.show(mainPanel, "Owner"));

        // Layout each component using GridBagConstraints
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.CENTER;
        mainPagePanel.add(rolePanel, gbc);

        gbc.gridy = 1; gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0; // Fill remaining vertical space
        mainPagePanel.add(mainPanel, gbc);

        gbc.gridy = 2; gbc.fill = GridBagConstraints.NONE; gbc.weighty = 0; // Reset weight for the button
        mainPagePanel.add(submitButton, gbc);

        // Add the main page panel to the frame
        frame.add(mainPagePanel, "Main");
    }


    private JPanel createInputPanel(String label1, String label2, String label3) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createVerticalStrut(20));
        panel.add(createLabelFieldPanel(label1));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createLabelFieldPanel(label2));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createLabelFieldPanel(label3));
        panel.add(Box.createVerticalStrut(20));
        return panel;
    }

    private JPanel createLabelFieldPanel(String labelText) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(label, BorderLayout.WEST);

        JTextField field = new JTextField(20);
        panel.add(field, BorderLayout.CENTER);

        if (labelText.contains("Client")) clientIdField = field;
        if (labelText.contains("Job Duration")) jobDurationField = field;
        if (labelText.contains("Job Deadline")) jobDeadlineField = field;
        if (labelText.contains("Owner")) ownerIdField = field;
        if (labelText.contains("Vehicle Info")) vehicleInfoField = field;
        if (labelText.contains("Residency Time")) residencyTimeField = field;

        panel.setMaximumSize(new Dimension(400, 30));
        return panel;
    }

    private void handleSubmission() {
        if (clientButton.isSelected()) {
            handleClientSubmission();
        } else if (ownerButton.isSelected()) {
            handleOwnerSubmission();
        } else {
            JOptionPane.showMessageDialog(frame, "Please select a role: Client or Owner.");
        }
    }

    private void handleClientSubmission() {
        String data = String.format("%s,%s,%s,%s", clientIdField.getText(),
                                    jobDurationField.getText(),
                                    jobDeadlineField.getText(),
                                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        saveToFile(data, "vcrts_data_client.csv", "Client ID,Job Duration,Job Deadline,Timestamp");
        clearFields();
        JOptionPane.showMessageDialog(frame, "Client data submitted!");
    }

    private void handleOwnerSubmission() {
        String data = String.format("%s,%s,%s,%s", ownerIdField.getText(),
                                    vehicleInfoField.getText(),
                                    residencyTimeField.getText(),
                                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        saveToFile(data, "vcrts_data_owner.csv", "Owner ID,Vehicle Info,Residency Time,Timestamp");
        clearFields();
        JOptionPane.showMessageDialog(frame, "Owner data submitted!");
    }

    private void saveToFile(String data, String fileName, String header) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            boolean fileExists = new java.io.File(fileName).exists();
            if (!fileExists) {
                writer.write(header);
                writer.newLine();
            }
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving data to CSV file.");
        }
    }

    private void clearFields() {
        clientIdField.setText("");
        jobDurationField.setText("");
        jobDeadlineField.setText("");
        ownerIdField.setText("");
        vehicleInfoField.setText("");
        residencyTimeField.setText("");
    }

    private void loginUser() {
        if (server.accountFound(usernameField.getText(), new String(passwordField.getPassword()))) {
            currentUser = server.getUser(usernameField.getText());
            server.updateServer("Login", currentUser);
            showMainPage();
        } else {
            JOptionPane.showMessageDialog(frame, "Invalid credentials. Please try again or sign up.");
        }
    }

    private void registerUser(String username, String password) {
        if (!username.isEmpty() && !password.isEmpty() && !server.isUser(username)) {
            server.addUser(new User(username, password));
            JOptionPane.showMessageDialog(frame, "Account created successfully. Please log in.");
            showLoginScreen();
        } else {
            JOptionPane.showMessageDialog(frame, "Account creation failed. Try a different username.");
        }
    }

    private void showLoginScreen() {
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Login");
    }

    private void showSignUpScreen() {
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "SignUp");
    }

    private void showMainPage() {
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
    }
}
