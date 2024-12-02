

// Server.java
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

// Removed Scanner import
// import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Server {
    private static final int PORT = 12345;
    private ServerSocket serverSocket;
    private UserManager userManager;
    private VCController vcController;
    private ServerGUI gui;
    private boolean isRunning;
    private ConnectedClients connectedClients = new ConnectedClients();

    public Server(ServerGUI gui) {
        this.gui = gui;
        this.userManager = new UserManager();
        this.vcController = new VCController();
        this.vcController.setGUI(gui);
        isRunning = false;
    }

    public void start() {
        if (isRunning) return;
        
        try {
            serverSocket = new ServerSocket(PORT);
            isRunning = true;
            gui.log("Server started on port " + PORT);
            
            new Thread(() -> {
                while (isRunning) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        new Thread(() -> handleClient(clientSocket)).start();
                    } catch (IOException e) {
                        if (isRunning) {
                            gui.log("Connection error: " + e.getMessage());
                        }
                    }
                }
            }).start();
        } catch (IOException e) {
            gui.log("Could not start server: " + e.getMessage());
        }
    }

    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                gui.log("Server stopped");
            }
        } catch (IOException e) {
            gui.log("Error stopping server: " + e.getMessage());
        }
    }
    public VCController getVCController() {
        return vcController;
    }
    public boolean isRunning() {
        return isRunning;
    }

    private void handleClient(Socket clientSocket) {
        connectedClients.add(clientSocket);
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            String clientMessage;
            while ((clientMessage = in.readLine()) != null) {
                gui.log("Received: " + clientMessage);
                String response = processClientMessage(clientMessage);
                out.println(response);
                out.println();
                out.flush();
            }
        } catch (IOException e) {
            gui.log("Error handling client: " + e.getMessage());
        } finally {
            connectedClients.remove(clientSocket);
        }
    }

    private void broadcastVehicleRemoval(String ownerId, String vinNumber) {
        String message = "VEHICLE_REMOVED " + ownerId + " " + vinNumber;
        List<Socket> clients = connectedClients.getAll();

        for (Socket clientSocket : clients) {
            try {
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                out.println(message);
                out.println(); // Empty line to mark end of message
            } catch (IOException e) {
                gui.log("Error broadcasting to client: " + e.getMessage());
            }
        }
        gui.log("Broadcasted vehicle removal: " + message);
    }


    private String processClientMessage(String clientMessage) {
        if (clientMessage.startsWith("REGISTER")) {
            return handleRegistration(clientMessage);
        } else if (clientMessage.startsWith("LOGIN")) {
            return userManager.loginUser(clientMessage);
        } else if (clientMessage.startsWith("JOB_SUBMIT")) {
            return promptAndHandleJobSubmission(clientMessage);
        } else if (clientMessage.startsWith("CAR_READY")) {
            return promptAndHandleCarReady(clientMessage);
        } else if (clientMessage.startsWith("DISPLAY_JOB_TIMES")) {
            return handleDisplayJobTimes(clientMessage);
        } else if (clientMessage.equals("GET_ALL_JOBS")) {
            return vcController.getAllJobs();
        } else if (clientMessage.startsWith("MARK_COMPLETE")) {
            return vcController.markJobComplete(clientMessage);
        } else if (clientMessage.startsWith("REMOVE_VEHICLE")) {
            return vcController.handleVehicleRemoval(clientMessage);
        }
        return "Invalid request";
    }

    private String handleRegistration(String message) {
        String[] parts = message.split(" ");
        String accountType = parts[7];
        
        if (accountType.equals("VCCController")) {
            return promptAndHandleVCCControllerRegistration(message);
        }
        return userManager.registerUser(message);
    }

    private String promptAndHandleVCCControllerRegistration(String message) {
        if (gui.showConfirmDialog("Approve VCC Controller registration request?")) {
            return userManager.registerUser(message);
        }
        return "VCC Controller registration declined.";
    }

    private String promptAndHandleJobSubmission(String message) {
        if (gui.showConfirmDialog("Accept job submission request?")) {
            return vcController.handleJobSubmission(message);
        }
        return "Job submission declined.";
    }

    private String promptAndHandleCarReady(String message) {
        if (gui.showConfirmDialog("Accept car ready notification?")) {
            return vcController.handleCarReady(message);
        }
        return "Car readiness declined.";
    }

    private String handleDisplayJobTimes(String message) {
        String[] parts = message.split(" ");
        return vcController.displayJobsAndCompletionTimes(parts[1], parts.length > 2 ? parts[2] : "");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerGUI gui = new ServerGUI();
            Server server = new Server(gui);
            gui.setServer(server);
            gui.setVisible(true);
        });
    }
}