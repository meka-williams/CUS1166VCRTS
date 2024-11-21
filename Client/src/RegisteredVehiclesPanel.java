import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class RegisteredVehiclesPanel extends JPanel {
    private List<Vehicle> vehicles;
    private JPanel vehiclesList;
    private Client client;  // Add client reference
    private static final Color BACKGROUND_COLOR = new Color(240, 250, 255);  // Match VCRTSGUI background
    private static final Color BUTTON_COLOR = new Color(44, 118, 220);     // Match VCRTSGUI button color
    private static final Color TEXT_COLOR = new Color(29, 42, 59);         // Match VCRTSGUI text color
    private static final Font HEADER_FONT = new Font("Serif", Font.BOLD, 24);
    private static final Font DETAIL_FONT = new Font("Serif", Font.PLAIN, 16);

    public RegisteredVehiclesPanel() {
        this.client = new Client();  // Initialize client
        this.vehicles = new ArrayList<>();
        setupPanel();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Create scrollable vehicles list
        createVehiclesList();
        JScrollPane scrollPane = new JScrollPane(vehiclesList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);
        
        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel titleLabel = new JLabel("Registered Vehicles", SwingConstants.CENTER);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        return headerPanel;
    }

    private void createVehiclesList() {
        vehiclesList = new JPanel();
        vehiclesList.setLayout(new BoxLayout(vehiclesList, BoxLayout.Y_AXIS));
        vehiclesList.setBackground(BACKGROUND_COLOR);
        updateVehiclesList();
    }

    private void updateVehiclesList() {
        vehiclesList.removeAll();
        
        if (vehicles.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(BACKGROUND_COLOR);
            
            JLabel emptyLabel = new JLabel("No vehicles registered", SwingConstants.CENTER);
            emptyLabel.setFont(DETAIL_FONT);
            emptyLabel.setForeground(Color.GRAY);
            
            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
            vehiclesList.add(emptyPanel);
        } else {
            for (Vehicle vehicle : vehicles) {
                vehiclesList.add(createVehiclePanel(vehicle));
                vehiclesList.add(Box.createVerticalStrut(10));
            }
        }

        vehiclesList.revalidate();
        vehiclesList.repaint();
    }

    private JPanel createVehiclePanel(Vehicle vehicle) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(TEXT_COLOR, 1, true)));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setMaximumSize(new Dimension(400, 250));

        // Create header panel with close button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Vehicle title label
        JLabel vehicleLabel = new JLabel(String.format("%s %s (ID: %d)",
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getVehicleID()));
        vehicleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        vehicleLabel.setForeground(TEXT_COLOR);

        // Create close button
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 20));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.RED);
        closeButton.setPreferredSize(new Dimension(25, 25));
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setOpaque(true);
        closeButton.addActionListener(e -> confirmAndRemoveVehicle(vehicle));

        // Add components to header panel
        headerPanel.add(vehicleLabel, BorderLayout.CENTER);
        headerPanel.add(closeButton, BorderLayout.EAST);

        // Details panel
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(BACKGROUND_COLOR);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Add details
        addDetailRow(detailsPanel, "Username: ", String.valueOf(vehicle.getOwnerID()));
        addDetailRow(detailsPanel, "Status: ", vehicle.getStatus());
        addDetailRow(detailsPanel, "VIN: ", vehicle.getVinNum());
        addDetailRow(detailsPanel, "Plate: ", vehicle.getPlateNumber());
        addDetailRow(detailsPanel, "Serial: ", vehicle.getSerialNumber());
        addDetailRow(detailsPanel, "Residency Time: ", vehicle.getResidencyTime() + " days");

        // Remove button at bottom (optional, since we now have the X button)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton removeButton = createStyledButton("Remove Vehicle");
        removeButton.addActionListener(e -> confirmAndRemoveVehicle(vehicle));
        buttonPanel.add(removeButton);

        // Add all components to main panel
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(detailsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowPanel.setBackground(BACKGROUND_COLOR);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(DETAIL_FONT);
        labelComponent.setForeground(TEXT_COLOR);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(DETAIL_FONT);
        valueComponent.setForeground(TEXT_COLOR);

        rowPanel.add(labelComponent);
        rowPanel.add(valueComponent);
        panel.add(rowPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Serif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 30));
        return button;
    }

    private void confirmAndRemoveVehicle(Vehicle vehicle) {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to remove this vehicle?\n" +
            "Brand: " + vehicle.getBrand() + "\n" +
            "Model: " + vehicle.getModel() + "\n" +
            "VIN: " + vehicle.getVinNum(),
            "Confirm Vehicle Removal",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            // Convert ownerID to String since the client method expects a String
            String response = client.removeVehicle(String.valueOf(vehicle.getOwnerID()), vehicle.getVinNum());
            
            if (response != null && response.contains("successful")) {
                vehicles.remove(vehicle);
                updateVehiclesList();
                JOptionPane.showMessageDialog(this, "Vehicle removed successfully");
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Failed to remove vehicle. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicle != null && !vehicleExists(vehicle.getVinNum())) {
            vehicles.add(vehicle);
            updateVehiclesList();
        }
    }

    public boolean vehicleExists(String vinNumber) {
        if (vinNumber == null) return false;
        return vehicles.stream().anyMatch(v -> vinNumber.equals(v.getVinNum()));
    }

    public void clearVehicles() {
        vehicles.clear();
        updateVehiclesList();
    }

    public int getVehicleCount() {
        return vehicles.size();
    }
}