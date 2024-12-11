import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SubmittedJobsPanel extends JPanel {
    private List<Job> jobs;
    private JPanel jobsList;
    private Client client;
    private VCRTSGUI parentGUI;
    private static final Color BACKGROUND_COLOR = new Color(240, 250, 255);
    private static final Color BUTTON_COLOR = new Color(44, 118, 220);
    private static final Color TEXT_COLOR = new Color(29, 42, 59);
    private static final Font HEADER_FONT = new Font("Serif", Font.BOLD, 24);
    private static final Font DETAIL_FONT = new Font("Serif", Font.PLAIN, 16);

    public SubmittedJobsPanel(Client client, VCRTSGUI parentGUI) {
        this.client = client;
        this.parentGUI = parentGUI;
        this.jobs = new ArrayList<>();
        setupPanel();
    }

    private void setupPanel() {
        setLayout(new BorderLayout());
        setBackground(BACKGROUND_COLOR);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        createJobsList();
        JScrollPane scrollPane = new JScrollPane(jobsList);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BACKGROUND_COLOR);

        // Add refresh button
        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.addActionListener(e -> refreshJobs());
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.add(refreshButton);

        add(scrollPane, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("Submitted Jobs", SwingConstants.CENTER);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        headerPanel.add(titleLabel, BorderLayout.CENTER);
        return headerPanel;
    }

    private void createJobsList() {
        jobsList = new JPanel();
        jobsList.setLayout(new BoxLayout(jobsList, BoxLayout.Y_AXIS));
        jobsList.setBackground(BACKGROUND_COLOR);
        updateJobsList();
    }

    private void updateJobsList() {
        jobsList.removeAll();

        if (jobs.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(BACKGROUND_COLOR);

            JLabel emptyLabel = new JLabel("No jobs submitted", SwingConstants.CENTER);
            emptyLabel.setFont(DETAIL_FONT);
            emptyLabel.setForeground(Color.GRAY);

            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
            jobsList.add(emptyPanel);
        } else {
            for (Job job : jobs) {
                jobsList.add(createJobPanel(job));
                jobsList.add(Box.createVerticalStrut(10));
            }
        }

        jobsList.revalidate();
        jobsList.repaint();
    }

    private JPanel createJobPanel(Job job) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                BorderFactory.createLineBorder(TEXT_COLOR, 1, true)));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setMaximumSize(new Dimension(400, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        JLabel jobLabel = new JLabel(String.format("%s",
                job.getDescription()));
        jobLabel.setFont(new Font("Serif", Font.BOLD, 18));
        jobLabel.setForeground(TEXT_COLOR);

        headerPanel.add(jobLabel, BorderLayout.CENTER);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
        detailsPanel.setBackground(BACKGROUND_COLOR);
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        addDetailRow(detailsPanel, "Status: ", job.getStatus());
        addDetailRow(detailsPanel, "Duration: ", job.getDuration() + " hours");
        //addDetailRow(detailsPanel, "Redundancy Level: ", String.valueOf(job.getRedundancyLevel()));
        //addDetailRow(detailsPanel, "Deadline: ", job.getDeadline());
        if (job.getCompletionTime() != null && !job.getCompletionTime().isEmpty()) {
            addDetailRow(detailsPanel, "Completion Time: ", job.getCompletionTime());
        }
        
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

    public void refreshJobs() {
        String clientId = parentGUI.getOwnerId();
        if (clientId == null || clientId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Client ID is not available. Please log in first.");
            return;
        }

        String response = client.requestVCCJobTimes(clientId, "JobSubmitter");
        jobs = parseJobsFromResponse(response);
        updateJobsList();
    }

    private List<Job> parseJobsFromResponse(String response) {
        List<Job> jobList = new ArrayList<>();
        if (response != null && !response.isEmpty()) {
            String[] lines = response.split("\n");
            
            for (String line : lines) {
                if (line.startsWith("Job ID:")) {
                    try {
                        // Extract job details using the format from the server response
                        String jobId = extractValue(line, "Job ID:");
                        String clientId = extractValue(line, "Client ID:");
                        String description = extractValue(line, "Description:");
                        int duration = Integer.parseInt(extractValue(line, "Duration:").replace(" hours", ""));
                        String status = "Submitted"; // Default status
                        String deadline = extractValue(line, "Deadline"); 
                        int redundancyLevel = 1; // Default redundancy level
                        String completionTime = extractValue(line, "Completion Time");

                        // Create job object and add to list
                        Job job = new Job(jobId, clientId, description, duration, 
                                        redundancyLevel, deadline, status, completionTime);
                        jobList.add(job);
                    } catch (Exception e) {
                        System.err.println("Failed to parse job line: " + line + ". Error: " + e.getMessage());
                    }
                }
            }
        }
        return jobList;
    }

    private String extractValue(String line, String key) {
        int startIndex = line.indexOf(key) + key.length();
        int endIndex = line.indexOf(",", startIndex);
        if (endIndex == -1) {
            endIndex = line.length();
        }
        return line.substring(startIndex, endIndex).trim();
    }

    public void addJob(Job job) {
        if (job != null && !jobs.contains(job)) {
            jobs.add(job);
            updateJobsList();
        }
    }
}