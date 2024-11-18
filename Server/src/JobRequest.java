// JobRequest.java


import java.util.UUID;

public class JobRequest {
    private final String jobId;           // Unique job identifier
    private String clientId;
    private String jobDescription;
    private int duration;
    private int redundancyLevel;
    private String jobDeadline;  // New field for job deadline

    // Constructor that generates a unique jobId and accepts jobDeadline
    public JobRequest(String clientId, String jobDescription, int duration, int redundancyLevel, String jobDeadline) {
        this.jobId = UUID.randomUUID().toString();  // Use UUID for unique job ID
        this.clientId = clientId;
        this.jobDescription = jobDescription;
        this.duration = duration;
        this.redundancyLevel = redundancyLevel;
        this.jobDeadline = jobDeadline;
    }
    public JobRequest(String jobId, String clientId, String jobDescription, int duration, int redundancyLevel, String jobDeadline) {
        this.jobId = jobId;  // Use existing jobId from CSV
        this.clientId = clientId;
        this.jobDescription = jobDescription;
        this.duration = duration;
        this.redundancyLevel = redundancyLevel;
        this.jobDeadline = jobDeadline;
    }

    public String getJobId() {
        return jobId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public int getDuration() {
        return duration;
    }

    public int getRedundancyLevel() {
        return redundancyLevel;
    }

    public String getJobDeadline() {
        return jobDeadline;
    }

    @Override
    public String toString() {
        return "JobRequest{" +
                "jobId='" + jobId + '\'' +
                ", clientId='" + clientId + '\'' +
                ", jobDescription='" + jobDescription + '\'' +
                ", duration=" + duration +
                ", redundancyLevel=" + redundancyLevel +
                ", jobDeadline='" + jobDeadline + '\'' +
                '}';
    }
}
