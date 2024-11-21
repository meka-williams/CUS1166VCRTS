// UserManager.java

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManager {
	 private static final String USER_DATA_FILE = "UserInformation.csv";
    private Map<String, String[]> users;

    public UserManager() {
        users = new HashMap<>();
        loadUsersFromCSV();
    }

    private void loadUsersFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {  // Confirm the format matches
                    System.out.printf("Loading user: Username=%s, Password=%s, AccountType=%s\n", parts[2], parts[5], parts[6]);
                    users.put(parts[2], parts); // Username at index 2, Password at index 5
                } else {
                    System.out.println("Invalid user data format: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users from CSV: " + e.getMessage());
        }
    }


    public String registerUser(String message) {
        System.out.println("Processing registration command: " + message); // Debugging output

        // Regular expression to match quoted strings or non-space sequences
        Pattern pattern = Pattern.compile("\"([^\"]*)\"|(\\S+)");
        Matcher matcher = pattern.matcher(message);

        List<String> partsList = new ArrayList<>();
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                // Quoted string without the quotes
                partsList.add(matcher.group(1));
            } else {
                // Unquoted word
                partsList.add(matcher.group(2));
            }
        }

        String[] parts = partsList.toArray(new String[0]);
        if (parts.length != 8) {
            return "Error: Invalid registration command";
        }

        String firstName = parts[1];
        String lastName = parts[2];
        String username = parts[3];
        String email = parts[4];
        String dob = parts[5];  // Already formatted as "yyyy-MM-dd"
        String password = parts[6];
        String accountType = parts[7];

        // Check if username already exists
        if (users.containsKey(username)) {
            return "Error: Username already exists";
        }

        // Store the user's data in the map
        String[] userData = {firstName, lastName, username, email, dob, password, accountType};
        users.put(username, userData);

        // Save to CSV with all fields in the correct order
        saveUserToCSV(firstName, lastName, username, email, dob, password, accountType);

        return "Registration successful";
    }

    public String loginUser(String message) {
        String[] parts = message.split(" ");
        if (parts.length != 3) {  
            return "Error: Invalid login command";
        }

        String username = parts[1];
        String password = parts[2];

        System.out.printf("Attempting login: Username=%s, Password=%s\n", username, password);

        // Hardcoded credentials for "vcc"
        if (username.equals("vcc") && password.equals("vccadmin")) {
            System.out.println("Hardcoded VCC login successful");
            return "Login successful,VCCController";
        }

        // Check against stored users map
        if (!users.containsKey(username)) {
            System.out.println("Error: Username not found in users map");
            return "Error: Username not found";
        }

        String[] userData = users.get(username);
        String storedPassword = userData[5];
        String accountType = userData[6];

        System.out.printf("Found user: StoredPassword=%s, AccountType=%s\n", storedPassword, accountType);

        if (!storedPassword.equals(password)) {
            System.out.println("Error: Incorrect password");
            return "Error: Incorrect password";
        }

        return "Login successful," + accountType;
    }



    private void saveUserToCSV(String firstName, String lastName, String username, String email, String dob, String password, String accountType) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USER_DATA_FILE, true))) {
            writer.println(String.join(",", firstName, lastName, username, email, dob, password, accountType));
        } catch (IOException e) {
            System.err.println("Error saving user to CSV: " + e.getMessage());
        }
    }
}
