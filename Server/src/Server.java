// Server.java
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server {
    private static final int PORT = 12345;  // Port number for the server to listen on
    private ServerSocket serverSocket;      // Server socket for accepting client connections
    private UserManager userManager;        // Manages user registration and login
    private VCController vcController;      // Manages job and car readiness data

    // Constructor - Initializes the server, user manager, and VCC controller
    public Server() {
        try {
            serverSocket = new ServerSocket(PORT);
            userManager = new UserManager();
            vcController = new VCController();
            System.out.println("Server is running on port " + PORT);
        } catch (IOException e) {
            System.err.println("Could not start server: " + e.getMessage());
        }
    }

    // Start the server - Continuously accepts new client connections and handles each client in a new thread
    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();  // Accept client connection
                new Thread(() -> handleClient(clientSocket)).start();  // Handle each client in a new thread
            } catch (IOException e) {
                System.err.println("Connection error: " + e.getMessage());
            }
        }
    }

    // Handles communication with a connected client
    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                System.out.println("Received: " + clientMessage);
                String response;

                if (clientMessage.startsWith("REGISTER")) {
                    response = handleRegistration(clientMessage);
                } else if (clientMessage.startsWith("LOGIN")) {
                    response = userManager.loginUser(clientMessage);
                } else if (clientMessage.startsWith("JOB_SUBMIT")) {
                    response = promptAndHandleJobSubmission(clientMessage);
                } else if (clientMessage.startsWith("CAR_READY")) {
                    response = promptAndHandleCarReady(clientMessage);
                } else if (clientMessage.startsWith("DISPLAY_JOB_TIMES")) {
                    response = handleDisplayJobTimes(clientMessage);
                } else if (clientMessage.equals("GET_ALL_JOBS")) {
                    response = vcController.getAllJobs();
                } else if (clientMessage.startsWith("MARK_COMPLETE")) {
                    response = vcController.markJobComplete(clientMessage);
                } else {
                    response = "Invalid request";
                }

                // Send the response back to the client
                out.println(response);
                out.println(); // Indicate the end of the response
                out.flush();

                // Log any remaining data in the buffer
                logRemainingBuffer(in);
            }
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        }
    }

    // Helper method to log the remaining buffer contents
    private void logRemainingBuffer(BufferedReader in) {
        try {
            if (in.ready()) { // Check if there is more data in the buffer
                System.out.println("Remaining buffer contents:");
                while (in.ready()) { // While more data is available in the buffer
                    System.out.println(in.readLine());
                }
            } else {
                System.out.println("Buffer post response is empty.");
            }
        } catch (IOException e) {
            System.err.println("Error reading buffer: " + e.getMessage());
        }
    }

    // Handle registration requests, with special handling for VCCController registration
    private String handleRegistration(String message) {
        String[] parts = message.split(" ");
        String accountType = parts[7];  // Account type is expected to be the last part of the message

        // Prompt for confirmation if the account type is "VCCController"
        if (accountType.equals("VCCController")) {
            return promptAndHandleVCCControllerRegistration(message);
        } else {
            // Regular registration process for non-VCCController accounts
            return userManager.registerUser(message);
        }
    }

    // Prompt to approve or deny a VCCController registration request
    private String promptAndHandleVCCControllerRegistration(String message) {
        System.out.print("Approve VCC Controller registration request? (Y/N): ");
        if (promptForConfirmation()) {
            return userManager.registerUser(message);  // Register the user if approved
        } else {
            return "VCC Controller registration declined.";  // Notify the client of declined registration
        }
    }

    // Handle job submission requests with server-side approval prompt
    private String promptAndHandleJobSubmission(String message) {
        System.out.print("Accept job submission request? (Y/N): ");
        if (promptForConfirmation()) {
            String response = vcController.handleJobSubmission(message);
            System.out.println("Job submission response: " + response);  // Debugging output for job submission
            return response;
        } else {
            return "Job submission declined.";
        }
    }

    // Handle car readiness notifications with server-side approval prompt
    private String promptAndHandleCarReady(String message) {
        System.out.print("Accept car ready notification? (Y/N): ");
        if (promptForConfirmation()) {
            return vcController.handleCarReady(message);
        } else {
            return "Car readiness declined.";
        }
    }

    // Handles the request to display jobs and their completion times for a specific client
    private String handleDisplayJobTimes(String message) {
        String[] parts = message.split(" ");
        String clientId = parts[1];
        String role = parts.length > 2 ? parts[2] : ""; // Get role if provided

        return vcController.displayJobsAndCompletionTimes(clientId, role);
    }

    // Prompt for server administrator confirmation (Y/N) - returns true if 'Y' is entered
    private boolean promptForConfirmation() {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine().trim().toUpperCase();
        return input.equals("Y");
    }

    // Main method - Starts the server
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
}
