import java.time.LocalDateTime;

public class JobRequest {
    private static int jobCounter = 0; // Unique ID counter for job requests
    private final int jobId;           // Unique ID for each job request
    private final String clientId;      // ID of the client submitting the job
    private final int duration;         // Duration of the job in hours
    private final LocalDateTime timestamp; // Timestamp for when the job was submitted

    // Constructor to initialize a job request with client ID and duration
    public JobRequest(String clientId, int duration) {
        this.jobId = ++jobCounter;       // Increment job counter for a unique job ID
        this.clientId = clientId;
        this.duration = duration;
        this.timestamp = LocalDateTime.now(); // Capture the current time as submission time
    }

    // Getter for job ID
    public int getJobId() {
        return jobId;
    }

    // Getter for client ID
    public String getClientId() {
        return clientId;
    }

    // Getter for job duration
    public int getDuration() {
        return duration;
    }

    // Getter for submission timestamp
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    // String representation of the job request for easy logging or debugging
    @Override
    public String toString() {
        return "JobRequest{" +
                "jobId=" + jobId +
                ", clientId='" + clientId + '\'' +
                ", duration=" + duration +
                ", timestamp=" + timestamp +
                '}';
    }
}
