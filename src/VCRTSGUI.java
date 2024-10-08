import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Dialog.ModalityType;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

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
    private JLabel infoBoxMessage = new JLabel("");
    private final int APP_WIDTH = 480;
    private final int APP_HEIGHT = 600;

    //Pages for the GUI
    private final String LOGIN_PAGE = "Login Page";
    private final String SIGN_UP_PAGE = "Sign Up Page";
    private final String MAIN_PAGE_NAME = "Main Page";
    private final String CREATE_JOB_REQUEST_PAGE = "Create Job Request Page";
    private final String CREATE_CAR_RENTAL_PAGE = "Car Rental Page";

    //fields to store user information
    private JLabel currentUserID = new JLabel("");

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

    private PageSwitcher pageSwitcher = new PageSwitcher();
    private UserVerifier userVerifier = new UserVerifier();
    // private JobRequestListener JobRequestListener = new JobRequestListener();
    // private CarRentalListener CarRentalListener = new CarRentalListener();

    private User currentUser;

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

        // ImageIcon logo = new ImageIcon("src/VCRTS_logo.png");
        // setIconImage(logo.getImage());

        infoBoxMessage.setHorizontalAlignment(JLabel.CENTER);

        infoBox.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        infoBox.setLayout(new BorderLayout());
        infoBox.setSize(300, 200);
        infoBox.setResizable(false);
        infoBox.setModalityType(ModalityType.APPLICATION_MODAL);
        infoBox.add(infoBoxMessage, BorderLayout.CENTER);
        
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
        createLoginScreen();
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
        
        //Verification for user information
        loginButton.addActionListener(userVerifier);
        //password.addKeyListener(userVerifier);

        //Switches to sign up page
        signUpButton.setName(SIGN_UP_PAGE);
        signUpButton.addActionListener(pageSwitcher);
        pageSwitchButtons.add(signUpButton);

        //Set color and font of login button
        loginButton.setBackground(buttonColor);
        loginButton.setForeground(textColor);
        loginButton.setFont(loginButton.getFont().deriveFont(buttonSize));

        //Set color and font register button
        signUpButton.setBackground(buttonColor);
        signUpButton.setForeground(textColor);
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
        JTextField dateOfBirth = new JTextField(20);
        // JDateChooser dateOfBirth = new JDateChooser();
        // dateOfBirth.setLocale(Locale.US);

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
        
        //Verifies user information
        signUpButton.addActionListener(userVerifier);

        //Switch to the login page
        loginButton.setName(LOGIN_PAGE);
        loginButton.addActionListener(pageSwitcher);
        pageSwitchButtons.add(loginButton);

        //Set up background & font for buttons
        signUpButton.setBackground(buttonColor);
        signUpButton.setForeground(textColor);
        signUpButton.setFont(signUpButton.getFont().deriveFont(buttonSize));

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
        dateOfBirthLabel.setFont(dateOfBirthLabel.getFont().deriveFont(textSize));
        passwordLabel.setFont(passwordLabel.getFont().deriveFont(textSize));
        passwordConfirmLabel.setFont(passwordLabel.getFont().deriveFont(textSize));

        //Add the sub-panels and buttons 
        signUpPanel.add(header);
        signUpPanel.add(fNameSubPanel);
        signUpPanel.add(lNameSubPanel);
        signUpPanel.add(usernameSubPanel);
        signUpPanel.add(emailAddressSubPanel);
        signUpPanel.add(dateOfBirthSubPanel);
        signUpPanel.add(passwordSubPanel);
        signUpPanel.add(passwordConfirmSubPanel);
        signUpPanel.add(signUpButton);
        signUpPanel.add(loginButton);

        //Add frame and screen to lists
        frame.add(signUpPanel, SIGN_UP_PAGE);
        screens.add(SIGN_UP_PAGE);
    }

    /**
     * Allows users to add job requests to use computation power
     */
    public void createJobRequestPage() {

    }
    
    /**
     * Allows users to add their cars to the system to be rented
     */
    public void createCarRentalPage() {

    }

    interface FieldClearer {
        public void clearFields();
    }

    class PageSwitcher implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String requestedPage = "";

            for(int i = 0; i < pageSwitchButtons.size(); i++){
                if(e.getSource().equals(pageSwitchButtons.get(i))) {
                    requestedPage = pageSwitchButtons.get(i).getName();
                }
            }

            for(int i = 0; i < screens.size(); i++){
                if(requestedPage.equals(screens.get(i))) {
                    ((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), screens.get(i));
                }
            }
        }
    }

    class UserVerifier extends User implements ActionListener, KeyListener, FieldClearer {
        private JTextField usernameField;
        private JPasswordField passwordField;

        public UserVerifier(){
            super();
            usernameField = new JTextField();
            passwordField = new JPasswordField();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(((JButton)e.getSource()).getText().equals("Sign Up Button")){
                if(!this.getUsername().equals("") && !this.getPassword().equals("")) {
                    currentUser = new User(this.getUsername(), this.getPassword());
                    //Add code to add to .csv
                    clearFields();
                    currentUserID.setText("\t UserID: " + currentUser.getUsername());
                    showMainPage();
                }
                else {
                    System.out.println("An error occurred, please try again");
                    infoBoxMessage.setText("An error occurred. Please try again");
                    infoBox.setVisible(true);
                }
            }
            //Login verification
            else if(((JButton)e.getSource()).getText().equals("Login")) {
                //Add code to check .csv
            }
        }

        public void showMainPage(){
            ((CardLayout)frame.getContentPane().getLayout()).show(frame.getContentPane(), MAIN_PAGE_NAME);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if(e.getSource().getClass().getSimpleName().equals("JTextField")){
                usernameField = (JTextField)e.getSource();
                this.setUsername(((JTextField)e.getSource()).getText());
            }
            if(e.getSource().getClass().getSimpleName().equals("JPasswordField")) {
                passwordField = (JPasswordField)e.getSource();
                this.setPassword(String.valueOf(((JPasswordField)e.getSource()).getPassword()));
            }
        }

        @Override
        public void clearFields() {
            usernameField.setText("");
            passwordField.setText("");

            this.setUsername("");
            this.setPassword("");
        }

        //These methods are not needed
        @Override
        public void keyTyped(KeyEvent e) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'keyTyped'");
        }

        @Override
        public void keyPressed(KeyEvent e) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'keyPressed'");
        }
    }
}
