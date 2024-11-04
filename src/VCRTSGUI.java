/**
 * Project:     Vehicular Cloud Real Time System
 * Class:       VCRTSGUI.java
 * Authors:     Shameka Williams, Farheen Mahmud, Jery Vizhnay, Bryan Benjamin, Hasan Mousa
 * Date:        October 7, 2024   
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import java.util.List;
public class VCRTSGUI {
    private JFrame frame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JRadioButton clientButton, ownerButton;
    private JPanel mainPagePanel;
    private JPanel mainPanel, clientPanel, ownerPanel;
    private JTextField clientIdField, jobDurationField, jobDeadlineField;
    private JTextField ownerIdField, vehicleInfoField, residencyTimeField;
    private JButton submitButton;
    private User currentUser;
    private Server server;
    private VCController vcc; // VCController instance
    private CardLayout cardLayout;
    private String sessionToken; // Session token for authenticated actions
    private GridBagConstraints gbc;

    // Constructor to initialize GUI components
    public VCRTSGUI() throws IOException {
        frame = new JFrame("Vehicular Cloud Real Time System (VCRTS)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new CardLayout());
        ImageIcon logo = new ImageIcon("src/VCRTS_logo.png");
        frame.setIconImage(logo.getImage());
        server = new Server();
        
        vcc = new VCController(1, 2); // Initialize VCController with example ID=1 and redundancy level=2

        createWelcomeScreen();
        createLoginScreen();
        createSignUpScreen();
        createMainPage();

        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws IOException {
        new VCRTSGUI();
    }

    private void createWelcomeScreen() {
        JPanel welcomePanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5,5,5,5);

        Dimension fieldSize = new Dimension(200, 25);
        Dimension buttonSize = new Dimension(100, 25);
        
        JLabel welcomeMessage = new JLabel("Welcome to the Vehicular Cloud Real Time!");

        JLabel loginMessage = new JLabel("For Returning Users");
        JLabel signUpMessage = new JLabel("For New Users");

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setPreferredSize(buttonSize);
        signUpButton.addActionListener(e -> showSignUpScreen());

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(buttonSize);
        loginButton.addActionListener(e -> showLoginScreen());

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.LINE_END;
        welcomePanel.add(welcomeMessage, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_START;
        welcomePanel.add(loginMessage, gbc);

        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.LINE_END;
        welcomePanel.add(signUpMessage, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.LINE_START;
        welcomePanel.add(loginButton, gbc);

        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.LINE_END;
        welcomePanel.add(signUpButton, gbc);

        frame.add(welcomePanel, "Welcome");
        frame.setSize(409, 292);
        frame.setResizable(false);
    }

    private void createLoginScreen() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(5, 5, 5, 5);

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

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        loginPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(passwordField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        loginPanel.add(loginButton, gbc);

        gbc.insets = new Insets(5, 105, 5, 5);
        loginPanel.add(signUpButton, gbc);

        frame.add(loginPanel, "Login");
        frame.setSize(409, 292);
        frame.setResizable(false);
    }
    private void displayVCCJobsAndTimes() {
        String jobInfo = vcc.displayJobsAndCompletionTimes();
        JOptionPane.showMessageDialog(frame, jobInfo, "VCC Job Queue and Completion Times", JOptionPane.INFORMATION_MESSAGE);
    }
    private void createMainPage() {
        // Set frame size and layout
        frame.setSize(600, 500);
        mainPagePanel = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Role selection panel (Client / Owner)
        JPanel rolePanel = new JPanel(new FlowLayout());
        clientButton = new JRadioButton("Client");
        ownerButton = new JRadioButton("Owner");
        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(clientButton);
        roleGroup.add(ownerButton);
        rolePanel.add(clientButton);
        rolePanel.add(ownerButton);

        // Main input panels for client and owner roles with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        clientPanel = createInputPanel("Client ID:", "Job Duration (hrs):", "Job Deadline:");
        ownerPanel = createInputPanel("Owner ID:", "Vehicle Info:", "Residency Time (hrs):");

        mainPanel.add(clientPanel, "Client");
        mainPanel.add(ownerPanel, "Owner");

        // Set up role selection panel at the top
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;  // Span across both columns
        gbc.anchor = GridBagConstraints.CENTER;
        mainPagePanel.add(rolePanel, gbc);

        // Set up client/owner input panels
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;
        mainPagePanel.add(mainPanel, gbc);

        // Center Submit button directly under the input fields
        submitButton = new JButton("Submit");
        submitButton.addActionListener(e -> handleSubmissionWithVCCDisplay());
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weighty = 0;
        gbc.gridwidth = 2; // Span across both columns to center the button
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment for the button
        mainPagePanel.add(submitButton, gbc);

        // Center Test VCC Jobs button directly below the submit button
        JButton testVCCButton = new JButton("Test VCC Jobs");
        testVCCButton.addActionListener(e -> displayVCCJobsAndTimes());
        gbc.gridy = 3;
        mainPagePanel.add(testVCCButton, gbc);

        // Add main page panel to the frame
        frame.add(mainPagePanel, "Main");

        // Show clientPanel by default and handle role switching
        clientButton.addActionListener(e -> cardLayout.show(mainPanel, "Client"));
        ownerButton.addActionListener(e -> cardLayout.show(mainPanel, "Owner"));

        // Use pack() to resize the frame snugly around components and center it
        frame.pack();
        frame.setLocationRelativeTo(null); // Center the frame on the screen
    }


    // Helper method to create input panels for client and owner roles
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

    // Helper method to create labeled text fields for input panels
    private JPanel createLabelFieldPanel(String labelText) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(150, 25));
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(label, BorderLayout.WEST);

        JTextField field = new JTextField(20);
        panel.add(field, BorderLayout.CENTER);

        // Assign fields based on label for easier data access
        if (labelText.contains("Client"))
            clientIdField = field;
        if (labelText.contains("Job Duration"))
            jobDurationField = field;
        if (labelText.contains("Job Deadline"))
            jobDeadlineField = field;
        if (labelText.contains("Owner"))
            ownerIdField = field;
        if (labelText.contains("Vehicle Info"))
            vehicleInfoField = field;
        if (labelText.contains("Residency Time"))
            residencyTimeField = field;

        panel.setMaximumSize(new Dimension(400, 30));
        return panel;
    }


    private void handleSubmissionWithVCCDisplay() {
        // Example of validation for client ID, job duration, and job deadline fields
        String clientId = clientIdField.getText().trim();
        String jobDurationText = jobDurationField.getText().trim();
        String jobDeadline = jobDeadlineField.getText().trim();

        // Check if required fields for "Client" submission are filled
        if (clientButton.isSelected()) {
            if (clientId.isEmpty() || jobDurationText.isEmpty() || jobDeadline.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields for Client submission.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int duration = Integer.parseInt(jobDurationText);
                JobRequest job = new JobRequest(clientId, duration);

                // Submit job to VCController and display job info and completion times
                vcc.submitJob(job);
                String jobInfo = vcc.displayJobsAndCompletionTimes();
                JOptionPane.showMessageDialog(frame, jobInfo, "VCC Job Queue and Completion Times", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Job Duration must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        } 
        // Check if required fields for "Owner" submission are filled
        else if (ownerButton.isSelected()) {
            String ownerId = ownerIdField.getText().trim();
            String vehicleInfo = vehicleInfoField.getText().trim();
            String residencyTimeText = residencyTimeField.getText().trim();

            if (ownerId.isEmpty() || vehicleInfo.isEmpty() || residencyTimeText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields for Owner submission.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int residencyTime = Integer.parseInt(residencyTimeText);
                vcc.saveOwnerCarToFile(ownerId, vehicleInfo, residencyTime); // save owner data to csv weeee
                JOptionPane.showMessageDialog(frame, "Owner data submitted successfully!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Residency Time must be a valid integer.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        clearFields();
    }

    // Method to clear input fields after submission
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

    private void showMainPage() {
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
    }

    private void showLoginScreen() {
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Login");
    }

    private void showSignUpScreen() {
        ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "SignUp");
    }

    private void createSignUpScreen() {
    	JLabel dobLabel = new JLabel("Date of Birth:");
    	UtilDateModel dateModel = new UtilDateModel();
    	Properties properties = new Properties();
    	properties.put("text.today", "Today");
    	properties.put("text.month", "Month");
    	properties.put("text.year", "Year");
    	JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, properties);
    	JDatePickerImpl dobPicker = new JDatePickerImpl(datePanel, new org.jdatepicker.impl.DateComponentFormatter());
    	JPanel signUpPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        Dimension fieldSize = new Dimension(200, 25);

        JLabel fNameLabel = new JLabel("First Name:");
        JTextField fNameField = new JTextField();
        fNameField.setPreferredSize(fieldSize);

        JLabel lNameLabel = new JLabel("Last Name:");
        JTextField lNameField = new JTextField();
        lNameField.setPreferredSize(fieldSize);

        JLabel usernameLabel = new JLabel("Username:");
        JTextField newUsernameField = new JTextField();
        newUsernameField.setPreferredSize(fieldSize);

        JLabel emailLabel = new JLabel("Email Address:");
        JTextField emailField = new JTextField();
        emailField.setPreferredSize(fieldSize);

 

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField newPasswordField = new JPasswordField();
        newPasswordField.setPreferredSize(fieldSize);

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        JPasswordField confirmPasswordField = new JPasswordField();
        confirmPasswordField.setPreferredSize(fieldSize);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> registerUser(newUsernameField.getText(), newPasswordField.getText()));
        JButton backButton = new JButton("Back to Login");
        backButton.addActionListener(e -> showLoginScreen());

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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        signUpPanel.add(buttonPanel, gbc);

        signUpPanel.setPreferredSize(new Dimension(409, 292));
        frame.add(signUpPanel, "SignUp");
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
}