import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class RegisteredVehiclesPanel extends JPanel {
    private List<Vehicle> vehicles;
    private JPanel vehiclesList;
    private Client client;
    private VCRTSGUI parentGUI; // Reference to the parent GUI
    private static final Color BACKGROUND_COLOR = new Color(240, 250, 255);
    private static final Color BUTTON_COLOR = new Color(44, 118, 220);
    private static final Color TEXT_COLOR = new Color(29, 42, 59);
    private static final Font HEADER_FONT = new Font("Serif", Font.BOLD, 24);
    private static final Font DETAIL_FONT = new Font("Serif", Font.PLAIN, 16);

    public RegisteredVehiclesPanel(Client client, VCRTSGUI parentGUI) {
        this.client = client; // Use the passed Client instance
        this.parentGUI = parentGUI; // Reference to the parent GUI
        this.vehicles = new ArrayList<>();
        setupPanel();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        createVehiclesList();
        JScrollPane scrollPane = new JScrollPane(vehiclesList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        // Add refresh button
        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.addActionListener(e -> refreshVehicles());
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.add(refreshButton);

        add(scrollPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
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

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel vehicleLabel = new JLabel(String.format("%s %s",
                vehicle.getBrand(),
                vehicle.getModel()));
        vehicleLabel.setFont(new Font("Serif", Font.BOLD, 18));
        vehicleLabel.setForeground(TEXT_COLOR);

        JButton closeButton = new JButton("X");
        closeButton.setFont(new Font("Arial", Font.BOLD, 9));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(Color.RED);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setOpaque(true);
        closeButton.addActionListener(e -> confirmAndRemoveVehicle(vehicle));

        headerPanel.add(vehicleLabel, BorderLayout.CENTER);
        headerPanel.add(closeButton, BorderLayout.EAST);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(BACKGROUND_COLOR);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        addDetailRow(detailsPanel, "Brand: ", vehicle.getBrand());
        addDetailRow(detailsPanel, "Model: ", vehicle.getModel());
        addDetailRow(detailsPanel, "Plate Number: ", vehicle.getPlateNumber());
        addDetailRow(detailsPanel, "Serial Number: ", vehicle.getSerialNumber());
        addDetailRow(detailsPanel, "VIN: ", vehicle.getVinNum());
        addDetailRow(detailsPanel, "Residency Time: ", String.valueOf(vehicle.getResidencyTime()));

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(detailsPanel, BorderLayout.CENTER);

        return panel;
    }

    private void addDetailRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowPanel.setBackground(BACKGROUND_COLOR);

        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(DETAIL_FONT);
        labelComponent.setForeground(TEXT_COLOR);

        String displayValue = label.trim().equals("Residency Time:") ? value + " months" : value;

        JLabel valueComponent = new JLabel(displayValue);
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

    private void refreshVehicles() {
        String ownerId = parentGUI.getOwnerId();
        if (ownerId == null || ownerId.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Owner ID is not available. Please log in first.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String response = client.sendRequest("GET_CARS " + ownerId);
            System.out.println("Server Response: " + response); // Debug log

            if (response != null && !response.trim().isEmpty()) {
                vehicles = parseVehiclesFromResponse(response);
                updateVehiclesList();
            } else {
                vehicles.clear();
                updateVehiclesList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error refreshing vehicles: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void confirmAndRemoveVehicle(Vehicle vehicle) {
        vehicle.setOwnerID(parentGUI.getOwnerId());
        int result = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to remove this vehicle?\n" +
                        "Brand: " + vehicle.getBrand() + "\n" +
                        "Model: " + vehicle.getModel() + "\n" +
                        "VIN: " + vehicle.getVinNum(),
                "Confirm Vehicle Removal",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            String response = client.removeVehicle(vehicle.getOwnerID(), vehicle.getVinNum());

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

    private List<Vehicle> parseVehiclesFromResponse(String response) {
        List<Vehicle> vehicleList = new ArrayList<>();
        if (response == null || response.trim().isEmpty()) {
            return vehicleList;
        }

        String[] lines = response.split("\n");
        for (String line : lines) {
            try {
                if (line.trim().isEmpty() || line.startsWith("Vehicles for Owner")) {
                    continue;
                }

                // Parse the comma-separated values
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    // Extract Car ID from the first part
                    String carIdStr = parts[0].split(":")[1].trim();
                    int carId = Integer.parseInt(carIdStr);

                    // Extract other fields, removing the field labels
                    String model = parts[1].split(":")[1].trim();
                    String brand = parts[2].split(":")[1].trim();
                    String plateNumber = parts[3].split(":")[1].trim();
                    String serialNumber = parts[4].split(":")[1].trim();
                    String vinNumber = parts[5].split(":")[1].trim();
                    String residencyTimeStr = parts[6].split(":")[1].trim();
                    // Convert residency time string to integer
                    int residencyTime;
                    try {
                        
                        residencyTimeStr = residencyTimeStr.replaceAll("[^0-9]", "");
                        residencyTime = Integer.parseInt(residencyTimeStr);
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing residency time: " + residencyTimeStr);
                        residencyTime = 0; 
                    }

                    Vehicle vehicle = new Vehicle(
                            carId,
                            "Available",
                            parentGUI.getOwnerId(),
                            model,
                            brand,
                            plateNumber,
                            serialNumber,
                            vinNumber,
                            residencyTime);
                    vehicleList.add(vehicle);
                }
            } catch (Exception e) {
                System.err.println("Error parsing vehicle line: " + line);
                e.printStackTrace();
            }
        }
        return vehicleList;
    }

    public void addVehicle(Vehicle vehicle) {
        if (vehicle != null && !vehicles.contains(vehicle)) {
            vehicles.add(vehicle);
            updateVehiclesList();
        }
    }
}
