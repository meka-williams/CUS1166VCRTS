import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private static final int PORT = 12345;       // Port for the server
    private ServerSocket serverSocket;          // Server socket
    private boolean running = false;            // Server state
    private ExecutorService threadPool;         // Thread pool for client handling
    private ServerGUI serverGUI;                // Reference to the GUI

    private UserManager userManager;            // Handles user registration and login
    private VCController vcController;          // Handles job and vehicle management

    // Constructor to initialize server components with GUI reference
    public Server(ServerGUI serverGUI) {
        this.serverGUI = serverGUI;
        this.userManager = new UserManager();
        this.vcController = new VCController();
        serverGUI.log("Server instance created. Ready to start.");
    }

  
   
    @Override
    public void run() {
        serverGUI.updateServerStatus(true);
        serverGUI.log("Server is running on port " + PORT + "...");

        try {
            while (running) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.execute(() -> handleClient(clientSocket)); // Handle each client in a thread
                } catch (IOException e) {
                    if (running) {
                        serverGUI.log("Error accepting client connection: " + e.getMessage());
                    }
                }
            }
        } finally {
            shutdownServer(); // Ensure resources are cleaned up
        }
    }



    // Start the server
    public synchronized void start() {
        if (!running) {
            try {
                serverSocket = new ServerSocket(PORT); // Initialize server socket
                threadPool = Executors.newFixedThreadPool(10); // Initialize thread pool
                running = true;
                new Thread(this).start(); // Start the server in a new thread
                serverGUI.log("Server started on port " + PORT);
            } catch (IOException e) {
                serverGUI.log("Failed to start server: " + e.getMessage());
            }
        } else {
            serverGUI.log("Server is already running.");
        }
    }


    public synchronized void stop() {
        if (running) {
            running = false;
            try {
                if (serverSocket != null && !serverSocket.isClosed()) {
                    serverSocket.close();
                    serverSocket = null; // Reset the server socket
                }
                if (threadPool != null && !threadPool.isShutdown()) {
                    threadPool.shutdownNow();
                    threadPool = null; // Reset the thread pool
                }
                serverGUI.log("Server stopped.");
                serverGUI.updateServerStatus(false);
            } catch (IOException e) {
                serverGUI.log("Error stopping server: " + e.getMessage());
            }
        } else {
            serverGUI.log("Server is not running.");
        }
    }


    // Returns the current running state of the server
    public synchronized boolean isRunning() {
        return running;
    }

    // Shuts down server resources
    private void shutdownServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            threadPool.shutdown();
        } catch (IOException e) {
            serverGUI.log("Error during server shutdown: " + e.getMessage());
        }
    }

    // Handles client communication
    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                serverGUI.log("Received: " + clientMessage);
                String response = processClientMessage(clientMessage);

                out.println(response);
                out.println(); // Send an empty line to indicate end of response
                out.flush();
            }
        } catch (IOException e) {
            serverGUI.log("Error handling client connection: " + e.getMessage());
        }
    }

    // Processes client messages and routes to the appropriate handler
    private String processClientMessage(String clientMessage) {
        try {
            if (clientMessage.startsWith("REGISTER")) {
                return userManager.registerUser(clientMessage);
            } else if (clientMessage.startsWith("LOGIN")) {
                return userManager.loginUser(clientMessage);
            } else if (clientMessage.startsWith("JOB_SUBMIT")) {
                return promptAndHandleJobSubmission(clientMessage);
            } else if (clientMessage.startsWith("CAR_READY")) {
                return promptAndHandleCarReady(clientMessage);
            } else if (clientMessage.startsWith("DISPLAY_JOB_TIMES")) {
                return vcController.displayJobsAndCompletionTimes();
            } else if (clientMessage.equals("GET_ALL_JOBS")) {
                return vcController.displayJobsAndCompletionTimes();
            } else if (clientMessage.startsWith("MARK_COMPLETE")) {
                return vcController.markJobComplete(clientMessage);
            } else if (clientMessage.startsWith("REMOVE_VEHICLE")) {
                return vcController.handleVehicleRemoval(clientMessage);
            } else if (clientMessage.startsWith("GET_CARS")) {
                return handleGetCars(clientMessage);
            } else {
                return "Invalid request";
            }
        } catch (Exception e) {
            serverGUI.log("Error processing client message: " + e.getMessage());
            return "Error: Unable to process request";
        }
    }
    private String handleGetCars(String clientMessage) {
        try {
            String[] parts = clientMessage.split(" ");
            if (parts.length != 2) {
                return "Invalid GET_CARS request format. Usage: GET_CARS <ownerId>";
            }
    
            String ownerId = parts[1].trim();
            // Get the formatted string response directly from VCController
            String response = vcController.getVehiclesByOwnerId(ownerId);
            
            // Check if response is empty or indicates no vehicles
            if (response == null || response.trim().isEmpty() || 
                response.equals("Vehicles for Owner ID: " + ownerId + ":\n")) {
                return "No vehicles found for owner ID: " + ownerId;
            }
    
            return response;
    
        } catch (Exception e) {
            e.printStackTrace();
            return "Error fetching vehicles: " + e.getMessage();
        }
    }
    

    private String handleLogin(String message) {
        try {
            System.out.println("\nDebug: Server handling login request: " + message);
            
            String[] parts = message.split(" ");
            if (parts.length != 3) {
                System.out.println("Debug: Invalid login format. Expected 3 parts, got " + parts.length);
                return "Error: Invalid login format";
            }
            
            String username = parts[1].trim();
            String password = parts[2].trim();
            
            System.out.println("Debug: Attempting login for user: " + username);
            
            // Call UserManager's login method
            String loginResult = userManager.loginUser(message);
            System.out.println("Debug: UserManager returned: " + loginResult);
            
            return loginResult;
        } catch (Exception e) {
            System.out.println("Debug: Server exception during login: " + e.getMessage());
            e.printStackTrace();
            return "Error: Server error - " + e.getMessage();
        }
    }
    
    private String promptAndHandleJobSubmission(String message) {
        boolean approved = serverGUI.showConfirmDialog("Approve job submission request?\nDetails: " + message);
        if (approved) {
            String response = vcController.handleJobSubmission(message);
            serverGUI.log("Job submission approved: " + response);
            return response;
        } else {
            serverGUI.log("Job submission declined by admin.");
            return "Job submission declined.";
        }
    }

    
    private String promptAndHandleCarReady(String message) {
        boolean approved = serverGUI.showConfirmDialog("Approve vehicle readiness notification?\nDetails: " + message);
        if (approved) {
            String response = vcController.handleCarReady(message);
            serverGUI.log("Car readiness approved: " + response);
            return response;
        } else {
            serverGUI.log("Car readiness declined by admin.");
            return "Car readiness declined.";
        }
    }


    // Accessors for VCController
    public VCController getVCController() {
        return vcController;
    }
}
