import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class RegisteredVehiclesPanel extends JPanel {
    private List<Vehicle> vehicles;
    private JPanel vehiclesList;
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);
    private static final Color BUTTON_COLOR = new Color(139, 189, 189);
    private static final Font DETAIL_FONT = new Font("Arial", Font.PLAIN, 12);

    public RegisteredVehiclesPanel() {
        vehicles = new ArrayList<>();
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Registered Vehicles"));
        setPreferredSize(new Dimension(400, 600));
        setBackground(BACKGROUND_COLOR);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel headerLabel = new JLabel("Registered Vehicles", SwingConstants.CENTER);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerLabel.setFont(new Font(headerLabel.getFont().getName(), Font.BOLD, 16));
        headerPanel.add(headerLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);

        // Vehicles List Panel with ScrollPane
        vehiclesList = new JPanel();
        vehiclesList.setLayout(new BoxLayout(vehiclesList, BoxLayout.Y_AXIS));
        vehiclesList.setBackground(BACKGROUND_COLOR);
        vehiclesList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(vehiclesList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setBackground(BACKGROUND_COLOR);
        
        add(scrollPane, BorderLayout.CENTER);

        updateVehiclesList();
    }

    private void updateVehiclesList() {
        vehiclesList.removeAll();
        
        if (vehicles.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(BACKGROUND_COLOR);
            
            JLabel emptyLabel = new JLabel("No vehicles registered", SwingConstants.CENTER);
            emptyLabel.setFont(emptyLabel.getFont().deriveFont(Font.ITALIC));
            emptyLabel.setForeground(Color.GRAY);
            
            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
            vehiclesList.add(emptyPanel);
        } else {
            for (Vehicle vehicle : vehicles) {
                vehiclesList.add(createVehiclePanel(vehicle));
                vehiclesList.add(Box.createVerticalStrut(10)); // spacing between vehicles
            }
        }

        // Update the title with the count
        String title = String.format("Registered Vehicles (%d)", vehicles.size());
        if (getBorder() instanceof TitledBorder) {
            ((TitledBorder) getBorder()).setTitle(title);
        }

        // Refresh the UI
        vehiclesList.revalidate();
        vehiclesList.repaint();
        repaint();
    }

    private JPanel createVehiclePanel(Vehicle vehicle) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1, true)
        ));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setMaximumSize(new Dimension(360, 250));

        // Create title and X button panel
        JPanel titlePanel = new JPanel(new BorderLayout(5, 0));
        titlePanel.setBackground(BACKGROUND_COLOR);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Add ID as title on the left
        JLabel idLabel = new JLabel("ID: " + vehicle.getVehicleID());
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titlePanel.add(idLabel, BorderLayout.WEST);
        
        // Create X button
        JButton closeButton = new JButton("×");  // Using × character
        closeButton.setFont(new Font("Arial", Font.BOLD, 20));  // Bigger font
        closeButton.setForeground(Color.RED);
        closeButton.setBackground(Color.RED);
        closeButton.setBorderPainted(false);
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(25, 25));
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.addActionListener(e -> confirmDelete(vehicle));
        titlePanel.add(closeButton, BorderLayout.EAST);

        // Vehicle details panel
        JPanel detailsPanel = new JPanel(new GridLayout(6, 1, 5, 5));  // Reduced to 6 rows
        detailsPanel.setBackground(BACKGROUND_COLOR);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add details (excluding ID since it's in the title now)
        addDetailLabel(detailsPanel, "Vehicle: " + vehicle.getBrand() + " " + vehicle.getModel());
        addDetailLabel(detailsPanel, "Owner ID: " + vehicle.getOwnerID());
        addDetailLabel(detailsPanel, "Status: " + vehicle.getStatus());
        addDetailLabel(detailsPanel, "Plate: " + vehicle.getPlateNumber());
        addDetailLabel(detailsPanel, "VIN: " + vehicle.getVinNum());
        addDetailLabel(detailsPanel, "Checkpoint: " + (vehicle.getLastCheckpointID() == -1 ? "None" : vehicle.getLastCheckpointID()));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);

        JButton deleteButton = new JButton("Remove");
        deleteButton.setBackground(BUTTON_COLOR);
        deleteButton.setForeground(Color.WHITE);
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.setPreferredSize(new Dimension(80, 30));
        deleteButton.addActionListener(e -> confirmDelete(vehicle));
        buttonPanel.add(deleteButton);

        // Add all components to main panel
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(detailsPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void addDetailLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 14));  // Increased font size
        panel.add(label);
    }

    private void confirmDelete(Vehicle vehicle) {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to remove this vehicle?\nVIN: " + vehicle.getVinNum(),
            "Confirm Vehicle Removal",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            vehicles.remove(vehicle);
            updateVehiclesList();
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

    public int getVehicleCount() {
        return vehicles.size();
    }
}