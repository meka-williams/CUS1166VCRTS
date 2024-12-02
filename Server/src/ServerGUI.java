import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ServerGUI extends JFrame {
    private JTable vehicleTable;
    private DefaultTableModel tableModel;
    private JTextArea logArea;
    private JLabel statusLabel;
    private List<Vehicle> vehicles;
    private Server server;
 
    public ServerGUI() {
        setupMainWindow();
        vehicles = new ArrayList<>();
        server = new Server(this);
    }
 
    private void setupMainWindow() {
        setTitle("VCRTS Server Control Panel");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        mainPanel.add(createStatusPanel(), BorderLayout.NORTH);
        mainPanel.add(createVehiclePanel(), BorderLayout.CENTER);
        mainPanel.add(createLogPanel(), BorderLayout.SOUTH);
        
        add(mainPanel);
        setLocationRelativeTo(null);
    }
 
    private JPanel createStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Server Status"));
        
        statusLabel = new JLabel("Server Status: Stopped");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(Color.RED);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton startButton = new JButton("Start Server");
        JButton stopButton = new JButton("Stop Server");
        
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
        panel.setBorder(BorderFactory.createTitledBorder("Registered Vehicles"));
        
        String[] columns = {
            "Vehicle ID", "Status", "Owner ID", "Model", "Brand", 
            "Plate Number", "Serial Number", "VIN", "Residency Time", "Last Checkpoint"
        };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        vehicleTable = new JTable(tableModel);
        vehicleTable.getTableHeader().setReorderingAllowed(false);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton refreshButton = new JButton("Refresh");
        JButton removeButton = new JButton("Remove Selected");
        
        refreshButton.addActionListener(e -> refreshVehicleTable());
        removeButton.addActionListener(e -> removeSelectedVehicle());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(removeButton);
        
        panel.add(new JScrollPane(vehicleTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
 
    private JPanel createLogPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Server Log"));
        
        logArea = new JTextArea(6, 50);
        logArea.setEditable(false);
        
        JButton clearButton = new JButton("Clear Log");
        clearButton.addActionListener(e -> logArea.setText(""));
        
        panel.add(new JScrollPane(logArea), BorderLayout.CENTER);
        panel.add(clearButton, BorderLayout.SOUTH);
        
        return panel;
    }
 
    public void setServer(Server server) {
        this.server = server;
    }
 
    public boolean showConfirmDialog(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, 
            "Server Confirmation", JOptionPane.YES_NO_OPTION);
        log("Confirmation dialog: " + message + " - Result: " + 
            (result == JOptionPane.YES_OPTION ? "Approved" : "Declined"));
        return result == JOptionPane.YES_OPTION;
    }
 
    public void addVehicle(Vehicle vehicle) {
        SwingUtilities.invokeLater(() -> {
            vehicles.add(vehicle);
            Object[] rowData = {
                vehicle.getVehicleID(),
                vehicle.getStatus(),
                vehicle.getOwnerID(),
                vehicle.getModel(),
                vehicle.getBrand(),
                vehicle.getPlateNumber(),
                vehicle.getSerialNumber(),
                vehicle.getVinNum(),
                vehicle.getResidencyTime(),
                vehicle.getLastCheckpointID()
            };
            tableModel.addRow(rowData);
        });
    }
 
    public void updateVehicleStatus(int vehicleId, String newStatus) {
        SwingUtilities.invokeLater(() -> {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                if ((int)tableModel.getValueAt(i, 0) == vehicleId) {
                    tableModel.setValueAt(newStatus, i, 1);
                    break;
                }
            }
        });
    }
 
    public void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }
 
    private void refreshVehicleTable() {
        tableModel.setRowCount(0);
        vehicles.clear();
        // Server will need to implement a method to get all vehicles
        log("Vehicle table refreshed");
    }
 
    private void removeSelectedVehicle() {
        int selectedRow = vehicleTable.getSelectedRow();
    if (selectedRow >= 0) {
        String ownerId = (String)tableModel.getValueAt(selectedRow, 2);
        String vinNumber = (String)tableModel.getValueAt(selectedRow, 7);
        
        String response = server.getVCController().handleVehicleRemoval(
            "REMOVE_VEHICLE " + ownerId + " " + vinNumber);
        log(response);
        
        if (response.contains("successfully")) {
            tableModel.removeRow(selectedRow);
            vehicles.removeIf(v -> v.getVinNum().equals(vinNumber));
        }
    } else {
        JOptionPane.showMessageDialog(this, "Please select a vehicle to remove");
    }
}
 
    public void updateServerStatus(boolean isRunning) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("Server Status: " + (isRunning ? "Running" : "Stopped"));
            statusLabel.setForeground(isRunning ? Color.GREEN : Color.RED);
        });
    }
}