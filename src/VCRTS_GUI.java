//package vcrts_gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VCRTS_GUI extends JFrame {

	private JRadioButton clientButton;
	private JRadioButton ownerButton;
	private JPanel clientPanel;
	private JPanel ownerPanel;
	private JPanel mainPanel; // Panel to hold both client and owner panels
	private CardLayout cardLayout; // Layout manager to switch between panels
	private JTextField clientIdField;
	private JTextField jobDurationField;
	private JTextField jobDeadlineField;
	private JTextField ownerIdField;
	//private JTextField vehicleInfoField;
	private JTextField vehicleModelField;
	private JTextField vehicleBrandField;
	private JTextField plateNumberField;
	private JTextField serialNumberField;
	private JTextField vinNumberField;
	private JTextField residencyTimeField;
	private JButton submitButton;
	private ButtonGroup roleGroup;

	public VCRTS_GUI() {
		setTitle("Vehicular Cloud Real Time System (VCRTS)");
		setSize(400, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());
		ImageIcon logo = new ImageIcon("src/VCRTS_logo.png");
		setIconImage(logo.getImage());
		// Panel for choosing role (Client or Owner)
		JPanel rolePanel = new JPanel();
		rolePanel.setLayout(new FlowLayout());
		clientButton = new JRadioButton("Client");
		ownerButton = new JRadioButton("Owner");
		roleGroup = new ButtonGroup();
		roleGroup.add(clientButton);
		roleGroup.add(ownerButton);
		rolePanel.add(clientButton);
		rolePanel.add(ownerButton);
		add(rolePanel, BorderLayout.NORTH);

		// CardLayout for switching between Client and Owner panels
		cardLayout = new CardLayout();
		mainPanel = new JPanel(cardLayout);

		// Client Input Panel
		clientPanel = new JPanel();
		clientPanel.setLayout(new GridLayout(4, 2));
		clientPanel.add(new JLabel("Client ID:"));
		clientIdField = new JTextField();
		clientPanel.add(clientIdField);
		clientPanel.add(new JLabel("Job Duration (hrs):"));
		jobDurationField = new JTextField();
		clientPanel.add(jobDurationField);
		clientPanel.add(new JLabel("Job Deadline:"));
		jobDeadlineField = new JTextField();
		clientPanel.add(jobDeadlineField);

		// Owner Input Panel
		ownerPanel = new JPanel();
		ownerPanel.setLayout(new GridLayout(14, 2));

		ownerPanel.add(new JLabel("Owner ID:"));
		ownerIdField = new JTextField();
		ownerPanel.add(ownerIdField);

		ownerPanel.add(new JLabel("Vehicle Model:"));
		vehicleModelField = new JTextField();
		ownerPanel.add(vehicleModelField);

		ownerPanel.add(new JLabel("Vehicle Brand:"));
		vehicleBrandField = new JTextField();
		ownerPanel.add(vehicleBrandField);

		ownerPanel.add(new JLabel("Plate Number:"));
		plateNumberField = new JTextField();
		ownerPanel.add(plateNumberField);

		ownerPanel.add(new JLabel("Serial Number:"));
		serialNumberField = new JTextField();
		ownerPanel.add(serialNumberField);

		ownerPanel.add(new JLabel("VIN Number:"));
		vinNumberField = new JTextField();
		ownerPanel.add(vinNumberField);

		ownerPanel.add(new JLabel("Residency Time (hrs):"));
		residencyTimeField = new JTextField();
		ownerPanel.add(residencyTimeField);

		// Add both panels to the CardLayout mainPanel
		mainPanel.add(clientPanel, "Client");
		mainPanel.add(ownerPanel, "Owner");
		add(mainPanel, BorderLayout.CENTER);

		
		submitButton = new JButton("Submit");
		add(submitButton, BorderLayout.SOUTH);

		// Action listeners for role selection
		clientButton.addActionListener(e -> cardLayout.show(mainPanel, "Client"));
		ownerButton.addActionListener(e -> cardLayout.show(mainPanel, "Owner"));

		// Action listener for submit button
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (clientButton.isSelected()) {
					handleClientSubmission();
				} else if (ownerButton.isSelected()) {
					handleOwnerSubmission();
				} else {
					JOptionPane.showMessageDialog(null, "Please select a role: Client or Owner.");
				}
			}
		});

		// Set default selection to Owner so it's visible initially or can be set to blank later
		ownerButton.setSelected(true);
		cardLayout.show(mainPanel, "Owner");
	}

	private void handleClientSubmission() {
	    String clientId = clientIdField.getText();
	    String jobDuration = jobDurationField.getText();
	    String jobDeadline = jobDeadlineField.getText();
	    LocalDateTime timestamp = LocalDateTime.now();
	    String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	    
	    if (!clientId.isEmpty() && !jobDuration.isEmpty() && !jobDeadline.isEmpty()) {
	        String data = clientId + "," + jobDuration + "," + jobDeadline + "," + formattedTimestamp;
	        saveToFile(data);  
	        JOptionPane.showMessageDialog(null, "Client data submitted!");
	        clearFields();
	    } else {
	        JOptionPane.showMessageDialog(null, "Please fill in all the fields.");
	    }
	}

	private void handleOwnerSubmission() {
	    String ownerId = ownerIdField.getText();
	    //String vehicleInfo = vehicleInfoField.getText();
		String vehicleModel = vehicleModelField.getText();
		String vehicleBrand = vehicleBrandField.getText();
		String plateNumber = plateNumberField.getText();
		String serialNumber = serialNumberField.getText();
		String vinNumber = vinNumberField.getText();
	    String residencyTime = residencyTimeField.getText();
	    LocalDateTime timestamp = LocalDateTime.now();
	    String formattedTimestamp = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

	    if (!ownerId.isEmpty() && !vehicleModel.isEmpty() && !vehicleBrand.isEmpty() && !plateNumber.isEmpty() && !serialNumber.isEmpty() && !vinNumber.isEmpty() && !residencyTime.isEmpty()) {
	        String data = ownerId + "," + vehicleModel + "," + vehicleBrand + "," + plateNumber + "," + serialNumber + "," + vinNumber + "," + residencyTime + "," + formattedTimestamp;
	        saveToFile(data); 
	        JOptionPane.showMessageDialog(null, "Owner data submitted!");
	        clearFields();
	    } else {
	        JOptionPane.showMessageDialog(null, "Please fill in all the fields.");
	    }
	}

	private void saveToFile(String data) {
	    String fileName;
	    String header;

	    // Determine which file to write based on the selected panel
	    if (clientButton.isSelected()) {
	        fileName = "vcrts_data_client.csv";
	        header = "Client ID,Job Duration,Job Deadline,Timestamp";
	    } else if (ownerButton.isSelected()) {
	        fileName = "vcrts_data_owner.csv";
	        header = "Owner ID,Vehicle Info,Residency Time,Timestamp";
	    } else {
	        JOptionPane.showMessageDialog(null, "Please select a role: Client or Owner.");
	        return;
	    }

	    // Check if the file already exists
	    boolean fileExists = new java.io.File(fileName).exists();

	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
	        // If the file does not exist, write the headers
	        if (!fileExists) {
	            writer.write(header);
	            writer.newLine();
	        }
	        
	        // Write the data
	        writer.write(data);
	        writer.newLine();
	        
	    } catch (IOException e) {
	        JOptionPane.showMessageDialog(null, "Error saving data to CSV file.");
	    }
	}

	private void clearFields() {
		clientIdField.setText("");
		jobDurationField.setText("");
		jobDeadlineField.setText("");
		ownerIdField.setText("");
		//vehicleInfoField.setText("");
		vehicleModelField.setText("");
		vehicleBrandField.setText("");
		plateNumberField.setText("");
		serialNumberField.setText("");
		vinNumberField.setText("");
		residencyTimeField.setText("");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			VCRTS_GUI gui = new VCRTS_GUI();
			gui.setVisible(true);
		});
	}
}
