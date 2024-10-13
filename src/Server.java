import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class Server {
    private File server = new File("src/Server.csv");
    private ArrayList<User> users;

    public Server() {
        users = new ArrayList<>();
        loadUsersFromCSV();  // Load users from CSV on startup
    }

    /**
     * Loads users from the Server.csv file and populates the users list.
     */
    public void loadUsersFromCSV() {
        try {
            Scanner scanner = new Scanner(server);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] details = line.split(",");  // Assuming CSV format: action,username,password,date
                if (details.length >= 3 && details[0].trim().equals("New Sign Up")) {
                    String username = details[1].trim();
                    String password = details[2].trim();
                    System.out.println("Loading user from CSV: " + username + " with password: " + password);
                    User user = new User(username, password);
                    if (!isUser(username)) {
                        users.add(user);
                    }
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Saves user registration and login actions to the Server.csv file.
     * @param action The action to log (e.g., "New Sign Up", "New Login").
     * @param user The user involved in the action.
     */
    public void updateServer(String action, User user) {
        Date date = new Date();
        String newData = action + "," + user.getUsername() + "," + user.getPassword() + "," + date + "\n";

        try {
            FileWriter writer = new FileWriter(server, true);  // Append mode
            writer.write(newData);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if a user exists with the provided username and password.
     * @param username The username to check.
     * @param password The password to check.
     * @return True if the account is found, otherwise false.
     */
    public boolean accountFound(String username, String password) {
        for (User user : users) {
            System.out.println("Checking user: " + user.getUsername() + " with password: " + user.getPassword());
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                System.out.println("Account found for username: " + username);
                return true;
            }
        }
        System.out.println("Account not found for username: " + username);
        return false;
    }

    /**
     * Adds a new user to the server's list of users.
     * @param user The user to add.
     */
    public void addUser(User user) {
        users.add(user);
        updateServer("New Sign Up", user);
    }

    /**
     * Checks if a user exists with the provided username.
     * @param username The username to check.
     * @return True if the user exists, otherwise false.
     */
    public boolean isUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves a user by their username.
     * @param username The username to search for.
     * @return The User object if found, otherwise null.
     */
    public User getUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
