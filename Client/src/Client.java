

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    // Constructor: Establishes a connection to the server
    public Client() {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Connected to the server.");
        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
        }
    }
    public void clearBuffer() {
        try {
            while (in.ready()) {
                String lingeringOutput = in.readLine();
                System.out.println("Cleared lingering output: " + lingeringOutput);
            }
        } catch (IOException e) {
            System.err.println("Error clearing buffer: " + e.getMessage());
        }
    }


    // Sends a request to the server and returns the server's response
 // Client.java: Updated `sendRequest` method
    public String sendRequest(String request) {
    	clearBuffer();
        try {
            System.out.println("[DEBUG] Sending request: " + request);
            out.println(request); // Send the request
            out.flush();          // Ensure the request is sent immediately

            StringBuilder responseBuilder = new StringBuilder();
            String line;

            // Read the response until the server sends an empty line
            while ((line = in.readLine()) != null) {
                if (line.isEmpty()) { // End of response
                    break;
                }
                responseBuilder.append(line).append("\n");
            }

            String response = responseBuilder.toString().trim();
            System.out.println("[DEBUG] Received response: " + response);
            return response;

        } catch (IOException e) {
            System.err.println("[ERROR] Error communicating with server: " + e.getMessage());
            return "[ERROR] Could not complete request.";
        }
    }







    // Registers a user with the specified details, including account type
    public String register(String firstName, String lastName, String username, String email, String dob, String password, String accountType) {
        String formattedDob;
        try {
            // Parse and reformat the date from "MMM d, yyyy" to "yyyy-MM-dd"
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate parsedDate = LocalDate.parse(dob, inputFormatter);
            formattedDob = parsedDate.format(outputFormatter);
        } catch (DateTimeParseException e) {
            return "Error: Invalid date format. Please use 'MMM d, yyyy' (e.g., Nov 5, 2024)";
        }

        // Format the command with quoted fields to handle spaces in user inputs
        String request = String.format("REGISTER \"%s\" \"%s\" \"%s\" \"%s\" \"%s\" \"%s\" \"%s\"",
                firstName.trim(),
                lastName.trim(),
                username.trim(),
                email.trim(),
                formattedDob,
                password.trim(),
                accountType.trim());

        System.out.println("Sending registration command: " + request); // Debugging output
        return sendRequest(request);  // Send the request to the server
    }



    // Logs in the user and identifies if they are a VCCController
    public String login(String username, String password) {
        String request = String.format("LOGIN %s %s", username.trim(), password.trim());
        System.out.println("Sending login command: " + request); // Debugging output
        String response = sendRequest(request);
        System.out.println("Reponse from Login command is:"+ response);
        return response;
    }

    // Requests all jobs for a VCCController to view job information
    public String requestAllJobs() {
        System.out.println("Requesting all jobs for VCC Controller");
        String response = sendRequest("GET_ALL_JOBS"); // Send the request to the server
        System.out.println("Received response: \n" + response); // Debugging

        if (response.length() > 6) {
            return response; // Return the valid response
        }

        System.out.println("No valid response received.");
        return "No jobs available."; // Default message if no valid response
    }



    // Submits a job with the given details to the server
    public String submitJob(String clientId, String jobDescription, int duration, int redundancyLevel, String jobDeadline) {
        String formattedDeadline;
        try {
            // Parse the date assuming the input might be in "MMM d, yyyy" format
            LocalDate parsedDate = LocalDate.parse(jobDeadline, DateTimeFormatter.ofPattern("MMM d, yyyy"));
            // Format it to "yyyy-MM-dd" for consistency
            formattedDeadline = parsedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            return "Error: Invalid date format for job deadline. Please use 'MMM d, yyyy' (e.g., Nov 5, 2024)";
        }

        // Enclose jobDescription in quotes to handle spaces
        String request = String.format("JOB_SUBMIT %s \"%s\" %d %d %s", clientId, jobDescription, duration, redundancyLevel, formattedDeadline);
        return sendRequest(request);
    }



    // Sends a notification to the server indicating a car is ready
    public String notifyCarReady(String ownerId, String vehicleModel, String vehicleBrand, String plateNumber, String serialNumber, String vinNumber, String residencyTime) {
        String request = String.format("CAR_READY %s %s %s %s %s %s %s", 
            ownerId.trim(), vehicleModel.trim(), vehicleBrand.trim(), plateNumber.trim(), 
            serialNumber.trim(), vinNumber.trim(), residencyTime.trim());
        return sendRequest(request);
    }

    // Requests job completion times for a specific client
    public String requestVCCJobTimes(String clientId, String role) {
        String request = "DISPLAY_JOB_TIMES " + clientId + " " + role;
        System.out.println("Sending job times request to server: " + request);
        String response = sendRequest(request);  // Get full response from server
        System.out.println("Received job times response from server: \n" + response);  // Confirm full response received
        return response;
    }


    // Closes the client socket and associated streams
    public void close() {
        try {
            if (socket != null) socket.close();
            if (in != null) in.close();
            if (out != null) out.close();
        } catch (IOException e) {
            System.err.println("Error closing client: " + e.getMessage());
        }
    }
}