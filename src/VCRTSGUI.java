import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Dialog.ModalityType;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Project:     Vehicular Cloud Real Time System
 * Class:       VCRTSGUI.java
 * Authors:     Shameka Williams, Farheen Mahmud, Jery Vizhnay, Bryan Benjamin, Hasan Mousa
 * Date:            
 * 
 */
public class VCRTSGUI {
    //Set up necessary components for GUI
    private JFrame frame = new JFrame();
    private JDialog infoBox = new JDialog();
    private JLabel infoBoxheader = new JLabel("");
    private final int APP_WIDTH = 480;
    private final int APP_HEIGHT = 600;

    //Pages for the GUI
    private final String LOGIN_PAGE = "Login Page";
    private final String SIGN_UP_PAGE = "Sign Up Page";
    private final String CREATE_JOB_REQUEST_PAGE = "Create Job Request Page";
    private final String CREATE_CAR_RENTAL_PAGE = "Car Rental Page";

    //To store the names of the screens of GUI
    private ArrayList<String> screens = new ArrayList<String>();
    
    //To store the buttons that switches screens
    private ArrayList<JButton> pageSwitchButtons = new ArrayList<JButton>();

    //Color Fields
    private Color backgroundColor;
    private Color buttonColor;
    private Color buttonHoverColor;
    private Color textColor;

    //Size fields
    private float buttonSize = 15;
    private float textSize = 19;

    /**
     * 
     */
    public VCRTSGUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new CardLayout());
        frame.setTitle("Vehicular Cloud Real Time System");
        frame.setSize(APP_WIDTH, APP_HEIGHT);
        frame.setResizable(false);
        frame.setLocation(250, 100);
        frame.getContentPane().setBackground(Color.WHITE);

        infoBoxheader.setHorizontalAlignment(JLabel.CENTER);

        infoBox.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        infoBox.setLayout(new BorderLayout());
        infoBox.setSize(300, 200);
        infoBox.setResizable(false);
        infoBox.setModalityType(ModalityType.APPLICATION_MODAL);
        infoBox.add(infoBoxheader, BorderLayout.CENTER);
        
        //Setting colors for attributes (background, button, & text)
        backgroundColor = new Color(192, 222, 229);
        buttonColor = new Color(97, 164, 173);
        buttonHoverColor = new Color(0, 59, 70);
        textColor = new Color(0, 0, 0);

        startApp();
        frame.setVisible(true);
        infoBox.setLocationRelativeTo(frame);
    }

    public static void main(String[] args) {
        new VCRTSGUI();
    }

    /**\
     * Creates the various screens of the GUI that the user can navigate to
     */
    public void startApp() {
        //createLoginScreen();
        createSignUpScreen();
    }

    /**
     * Creates the login screen for the GUI
     * Allows users to log into the system with an existing account
     */
    public void createLoginScreen() {
        //Create panel for login screen
        JPanel loginPanel = new JPanel();
        JLabel header = new JLabel("Login");

        //Create panel & label for username field
        JPanel usernameSubPanel = new JPanel();
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField username = new JTextField(20);
        
        //Create panel & label for password field
        JPanel passwordSubPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Password: ");
        JPasswordField password = new JPasswordField(20);
        
        //Create button for login confirmation
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(buttonColor);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);
        
        //Create button for registration
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(buttonColor);
        signUpButton.setBorderPainted(false);
        signUpButton.setOpaque(true);

        //Set up layout for username sub-panel
        usernameSubPanel.setLayout(new BorderLayout(5,0));
        usernameSubPanel.setBackground(backgroundColor);
        usernameSubPanel.add(usernameLabel, BorderLayout.WEST);
        usernameSubPanel.add(username, BorderLayout.EAST);

        //Set up layout for password sub-panel
        passwordSubPanel.setLayout(new BorderLayout(5, 0));
        passwordSubPanel.setBackground(backgroundColor);
        passwordSubPanel.add(passwordLabel, BorderLayout.WEST);
        passwordSubPanel.add(password, BorderLayout.EAST);
        
        //Set color and font of login button
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(textColor);
        loginButton.setFont(loginButton.getFont().deriveFont(buttonSize));

        //Set color and font register button
        signUpButton.setName(LOGIN_PAGE);
        signUpButton.setBackground(buttonColor);
        signUpButton.setForeground(textColor);
        pageSwitchButtons.add(signUpButton);
        signUpButton.setFont(signUpButton.getFont().deriveFont(buttonSize));

        //Sets the font size for the labels
        header.setFont(header.getFont().deriveFont(textSize));
        usernameLabel.setFont(usernameLabel.getFont().deriveFont(textSize));
        passwordLabel.setFont(passwordLabel.getFont().deriveFont(textSize));

        //Sets the layout and background of the panel
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 50));
        loginPanel.setBackground(backgroundColor);

        //Adds the the components of the panel
        loginPanel.add(header);
        loginPanel.add(usernameSubPanel);
        loginPanel.add(passwordSubPanel);
        loginPanel.add(loginButton);
        loginPanel.add(signUpButton);
        frame.add(loginPanel, LOGIN_PAGE);
        screens.add(LOGIN_PAGE);
    }

    /**
     * Creates a sign up screen for the GUI
     * Allows users to create a new account by providing identifiable information
     */
    public void createSignUpScreen(){
        //Create panel for sign up screen
        JPanel signUpPanel = new JPanel();
        JLabel header = new JLabel("Sign Up");
        
        //Create panel & label for first name
        JPanel fNameSubPanel = new JPanel();
        JLabel fNameLabel = new JLabel("First Name: ");
        JTextField fName = new JTextField(20);

        //Create panel & label for last name
        JPanel lNameSubPanel = new JPanel();
        JLabel lNameLabel = new JLabel("Last Name: ");
        JTextField lName = new JTextField(20);

        //Create panel & label for username
        JPanel usernameSubPanel = new JPanel();
        JLabel usernameLabel = new JLabel("Username: ");
        JTextField username = new JTextField(20);

        //Create panel & label for email address
        JPanel emailAddressSubPanel = new JPanel();
        JLabel emailAddressLabel = new JLabel("Email Address: ");
        JTextField emailAddress = new JTextField(20);

        //Create panel & label for date of birth
        JPanel dateOfBirthSubPanel = new JPanel();
        JLabel dateOfBirthLabel = new JLabel("Date of Birth: ");
        JDateChooser dateOfBirth = new JDateChooser();
        dateOfBirth.setLocale(Locale.US);

        //Create panel & label for password
        JPanel passwordSubPanel = new JPanel();
        JLabel passwordLabel = new JLabel("Password: ");
        JPasswordField password = new JPasswordField(20);

        //Create panel & label for password confirmation
        JPanel passwordConfirmSubPanel = new JPanel();
        JLabel passwordConfirmLabel = new JLabel("Confirm Password: ");
        JPasswordField passwordConfirm = new JPasswordField(20);

        //Create button to sign up
        JButton signUpButton = new JButton("Sign Up");
        signUpButton.setBackground(buttonColor);
        signUpButton.setBorderPainted(false);
        signUpButton.setOpaque(true);

        //Create button to return to the login screen
        JButton loginButton = new JButton("Login");
        loginButton.setBackground(buttonColor);
        loginButton.setBorderPainted(false);
        loginButton.setOpaque(true);

        //Set layout and background of sub-panels
        fNameSubPanel.setLayout(new BorderLayout(5, 0));
        fNameSubPanel.setBackground(backgroundColor);
        fNameSubPanel.add(fNameLabel, BorderLayout.WEST);
        fNameSubPanel.add(fName, BorderLayout.EAST);

        lNameSubPanel.setLayout(new BorderLayout(5, 0));
        lNameSubPanel.setBackground(backgroundColor);
        lNameSubPanel.add(lNameLabel, BorderLayout.WEST);
        lNameSubPanel.add(lName, BorderLayout.EAST);

        usernameSubPanel.setLayout(new BorderLayout(5,0));
        usernameSubPanel.setBackground(backgroundColor);
        usernameSubPanel.add(usernameLabel, BorderLayout.WEST);
        usernameSubPanel.add(username, BorderLayout.EAST);

        emailAddressSubPanel.setLayout(new BorderLayout(5, 0));
        emailAddressSubPanel.setBackground(backgroundColor);
        emailAddressSubPanel.add(emailAddressLabel, BorderLayout.WEST);
        emailAddressSubPanel.add(emailAddress, BorderLayout.EAST);

        dateOfBirthSubPanel.setLayout(new BorderLayout(5, 0));
        dateOfBirthSubPanel.setBackground(backgroundColor);
        dateOfBirthSubPanel.add(dateOfBirthLabel, BorderLayout.WEST);
        dateOfBirthSubPanel.add(dateOfBirth, BorderLayout.EAST);

        passwordSubPanel.setLayout(new BorderLayout(5, 0));
        passwordSubPanel.setBackground(backgroundColor);
        passwordSubPanel.add(passwordLabel, BorderLayout.WEST);
        passwordSubPanel.add(password, BorderLayout.EAST);

        passwordConfirmSubPanel.setLayout(new BorderLayout(5, 0));
        passwordConfirmSubPanel.setBackground(backgroundColor);
        passwordConfirmSubPanel.add(passwordConfirmLabel, BorderLayout.WEST);
        passwordConfirmSubPanel.add(passwordConfirm, BorderLayout.EAST);

        //Set up background & font for buttons
        signUpButton.setBackground(buttonColor);
        signUpButton.setForeground(textColor);
        signUpButton.setFont(signUpButton.getFont().deriveFont(buttonSize));

        loginButton.setName(LOGIN_PAGE);
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(textColor);
        loginButton.setFont(loginButton.getFont().deriveFont(buttonSize));

        signUpPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 30));
        signUpPanel.setBackground(backgroundColor);

        //Set font size for components 
        header.setFont(header.getFont().deriveFont(textSize));
        fNameLabel.setFont(fNameLabel.getFont().deriveFont(textSize));
        lNameLabel.setFont(lNameLabel.getFont().deriveFont(textSize));
        usernameLabel.setFont(usernameLabel.getFont().deriveFont(textSize));
        emailAddressLabel.setFont(emailAddressLabel.getFont().deriveFont(textSize));
        passwordLabel.setFont(passwordLabel.getFont().deriveFont(textSize));
        passwordConfirmLabel.setFont(passwordLabel.getFont().deriveFont(textSize));

        //Add the sub-panels and buttons 
        signUpPanel.add(header);
        signUpPanel.add(fNameSubPanel);
        signUpPanel.add(lNameSubPanel);
        signUpPanel.add(usernameSubPanel);
        signUpPanel.add(emailAddressSubPanel);
        signUpPanel.add(passwordSubPanel);
        signUpPanel.add(passwordConfirmSubPanel);
        signUpPanel.add(signUpButton);
        signUpPanel.add(loginButton);

        //Add frame and screen to lists
        frame.add(signUpPanel, SIGN_UP_PAGE);
        screens.add(SIGN_UP_PAGE);
    }
}
