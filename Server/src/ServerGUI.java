import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.xml.stream.FactoryConfigurationError;

import java.awt.*;
import java.util.List;

public class ServerGUI extends JFrame {
    private JTabbedPane tabbedPane;
    private DefaultTableModel vehiclesTableModel;
    private DefaultTableModel jobsTableModel;
    private JTextArea logArea;
    private JLabel statusLabel;
    private Server server;

    private Color buttonColor = new Color(44, 118, 220);
    private Color backgroundColor = new Color(240, 250, 255);
    private Color textColor = new Color(29, 42, 59);

    public ServerGUI() {
        setupMainWindow();
        server = new Server(this);
        updateServerStatus(server.isRunning());

        setTitle("VCRTS Server");
        ImageIcon logo = new ImageIcon("bin/ServerLogo.png");
        setIconImage(logo.getImage());

    }

    private void setupMainWindow() {
        setTitle("VCRTS Server Control Panel");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main Panel Layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(backgroundColor);

        // Add Status Panel
        mainPanel.add(createStatusPanel(), BorderLayout.NORTH);

        // Add Tabbed Panel for Vehicles and Jobs
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Registered Vehicles", createVehiclePanel());
        tabbedPane.addTab("Job Requests", createJobManagementPanel());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // Add Log Panel
        mainPanel.add(createLogPanel(), BorderLayout.SOUTH);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createTitledBorder("Server Status"));

        statusLabel = new JLabel("Server Status: Stopped");
        statusLabel.setFont(new Font("Serif", Font.BOLD, 20));
        statusLabel.setForeground(Color.RED);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(backgroundColor);
        JButton startButton = createStyledButton("Start Server");
        JButton stopButton = createStyledButton("Stop Server");

        startButton.addActionListener(e -> {
            if (!server.isRunning()) {
                server.start();
                updateServerStatus(true);
            }
        });

        stopButton.addActionListener(e -> {
            if (server.isRunning()) {
                server.stop();
                updateServerStatus(false);
            }
        });

        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);

        panel.add(statusLabel, BorderLayout.WEST);
        panel.add(buttonPanel, BorderLayout.EAST);

        return panel;
    }

    private JPanel createVehiclePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createTitledBorder("Registered Vehicles"));

        String[] columns = {"VIN", "Owner ID", "Status", "Model", "Brand", "Plate Number", "Serial Number", "Residency Date"};
        vehiclesTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table non-editable
            }
        };

        JTable vehiclesTable = new JTable(vehiclesTableModel);
        vehiclesTable.getTableHeader().setReorderingAllowed(false);
        vehiclesTable.setFont(new Font("Serif", Font.PLAIN, 14));
        vehiclesTable.getTableHeader().setOpaque(false);
        vehiclesTable.getTableHeader().setBackground(Color.WHITE);
        vehiclesTable.setBackground(backgroundColor);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(backgroundColor);
        JButton refreshButton = createStyledButton("Refresh");

        refreshButton.addActionListener(e -> refreshVehicleTable());

        buttonPanel.add(refreshButton);

        panel.add(new JScrollPane(vehiclesTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }


    private JPanel createJobManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createTitledBorder("Job Requests"));

        String[] columns = {"Job ID", "Client ID", "Description", "Duration", "Redundancy", "Deadline", "Timestamp"};
        jobsTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make the table non-editable
            }
        };

        JTable jobsTable = new JTable(jobsTableModel);
        jobsTable.getTableHeader().setReorderingAllowed(false);
        jobsTable.getTableHeader().setOpaque(false);
        jobsTable.getTableHeader().setBackground(Color.WHITE);
        jobsTable.setBackground(backgroundColor);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(backgroundColor);
        JButton refreshButton = createStyledButton("Refresh");
        JButton computeCompletionButton = createStyledButton("Compute Completion Time");

        refreshButton.addActionListener(e -> refreshJobRequestsTable());
        computeCompletionButton.addActionListener(e -> computeCompletionTime());

        buttonPanel.add(refreshButton);
        buttonPanel.add(computeCompletionButton);

        panel.add(new JScrollPane(jobsTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(backgroundColor);
        panel.setBorder(BorderFactory.createTitledBorder("Server Log"));

        logArea = new JTextArea(6, 50);
        logArea.setEditable(false);

        JButton clearButton = createStyledButton("Clear Log");
        clearButton.addActionListener(e -> logArea.setText(""));

        panel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        panel.add(clearButton, BorderLayout.SOUTH);

        return panel;
    }

    public void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    private void refreshVehicleTable() {
        SwingUtilities.invokeLater(() -> {
            vehiclesTableModel.setRowCount(0);
            List<CarRentals> vehicles = server.getVCController().getVehiclesReady();
            for (CarRentals vehicle : vehicles) {
                vehiclesTableModel.addRow(new Object[]{
                        vehicle.getVinNumber(), vehicle.getOwnerId(), "Ready", vehicle.getVehicleModel(),
                        vehicle.getVehicleBrand(), vehicle.getPlateNumber(), vehicle.getSerialNumber(), vehicle.getResidencyTime()
                });
            }
            log("Vehicle table refreshed.");
        });
    }

    private void refreshJobRequestsTable() {
        SwingUtilities.invokeLater(() -> {
            jobsTableModel.setRowCount(0);
            List<JobRequest> jobs = server.getVCController().getJobsQueue();
            for (JobRequest job : jobs) {
                jobsTableModel.addRow(new Object[]{
                        job.getJobId(), job.getClientId(), job.getJobDescription(), job.getDuration(),
                        job.getRedundancyLevel(), job.getJobDeadline(), job.getTimestamp()
                });
            }
            log("Job requests table refreshed.");
        });
    }

    private void markSelectedJobComplete(JTable jobsTable) {
        int selectedRow = jobsTable.getSelectedRow();
        if (selectedRow >= 0) {
            String jobId = (String) jobsTableModel.getValueAt(selectedRow, 0);
            String response = server.getVCController().markJobComplete("MARK_COMPLETE " + jobId);
            log(response);
            if (response.contains("successfully")) {
                jobsTableModel.removeRow(selectedRow);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a job to mark as complete.");
        }
    }

    public void updateServerStatus(boolean isRunning) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("Server Status: " + (isRunning ? "Running" : "Stopped"));
            statusLabel.setForeground(isRunning ? Color.GREEN : Color.RED);
        });
    }

    public boolean showConfirmDialog(String message) {
        int result = JOptionPane.showConfirmDialog(this, message,
                "Server Confirmation", JOptionPane.YES_NO_OPTION);
        log("Admin confirmation: " + (result == JOptionPane.YES_OPTION ? "Approved" : "Declined"));
        return result == JOptionPane.YES_OPTION;
    }
    private void markSelectedVehicleComplete(JTable vehiclesTable) {
        int selectedRow = vehiclesTable.getSelectedRow();
        if (selectedRow >= 0) {
            String vinNumber = (String) vehiclesTableModel.getValueAt(selectedRow, 0); // VIN
            String ownerId = (String) vehiclesTableModel.getValueAt(selectedRow, 1);  // Owner ID
            System.out.println(vinNumber);
            System.out.println(ownerId);
            // Example action: Update vehicle status in the table
            String response = server.getVCController().handleVehicleCompletion(ownerId, vinNumber);
            log(response);

            if (response.contains("successfully")) {
                vehiclesTableModel.setValueAt("Completed", selectedRow, 2); // Update Status column
            } else {
                JOptionPane.showMessageDialog(this, "Failed to mark vehicle as complete: " + response);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a vehicle to mark as complete.");
        }
        
    }

    private void computeCompletionTime() {
        JFrame frame = new JFrame();
        String jobInfo = server.getVCController().displayJobsAndCompletionTimes();
        JOptionPane.showMessageDialog(frame, jobInfo, "VCC Job Queue and Completion Times", JOptionPane.INFORMATION_MESSAGE);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        //button.setPreferredSize(new Dimension(150, 50));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(buttonColor);
        button.setFocusPainted(false);
        button.setBounds(500, 500, 100, 25);
        return button;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ServerGUI gui = new ServerGUI();
            gui.setVisible(true);
        });
    }
}
