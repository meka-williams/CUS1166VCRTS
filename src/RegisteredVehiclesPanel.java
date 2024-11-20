import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RegisteredVehiclesPanel extends JPanel {
    private List<Vehicle> vehicles;
    private JPanel vehiclesList;

    public RegisteredVehiclesPanel() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        setPreferredSize(new Dimension(300, 600));

        // Header
        JLabel headerLabel = new JLabel("Registered Vehicles");
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerLabel.setFont(new Font(headerLabel.getFont().getName(), Font.BOLD, 14));
        add(headerLabel, BorderLayout.NORTH);

        // Scrollable vehicles list
        vehiclesList = new JPanel();
        vehiclesList.setLayout(new BoxLayout(vehiclesList, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(vehiclesList);
        add(scrollPane, BorderLayout.CENTER);

        vehicles = new ArrayList<>();
        updateVehiclesList();
    }

    private void updateVehiclesList() {
        vehiclesList.removeAll();
        
        for (Vehicle vehicle : vehicles) {
            JPanel vehiclePanel = createVehiclePanel(vehicle);
            vehiclesList.add(vehiclePanel);
            vehiclesList.add(Box.createVerticalStrut(5));
        }

        vehiclesList.revalidate();
        vehiclesList.repaint();
    }

    private JPanel createVehiclePanel(Vehicle vehicle) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5),
            BorderFactory.createLineBorder(Color.LIGHT_GRAY)
        ));

        // Vehicle details
        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.add(new JLabel("ID: " + vehicle.getVehicleID()));
        detailsPanel.add(new JLabel(vehicle.getBrand() + " " + vehicle.getModel()));
        detailsPanel.add(new JLabel("Owner ID: " + vehicle.getOwnerID()));
        detailsPanel.add(new JLabel("Status: " + vehicle.getStatus()));
        detailsPanel.add(new JLabel("Plate: " + vehicle.getPlateNumber()));
        detailsPanel.add(new JLabel("VIN: " + vehicle.getVinNum()));

        // Delete button
        JButton deleteButton = new JButton("X");
        deleteButton.addActionListener(e -> confirmDelete(vehicle));

        panel.add(detailsPanel, BorderLayout.CENTER);
        panel.add(deleteButton, BorderLayout.EAST);

        return panel;
    }

    private void confirmDelete(Vehicle vehicle) {
        int result = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to delete this vehicle?",
            "Confirm Deletion",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            vehicles.remove(vehicle);
            updateVehiclesList();
        }
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        updateVehiclesList();
    }
}