import java.time.LocalDateTime;

public class JobRequest {
    private static int jobCounter = 0; 
    private final int jobId;          
    private final String clientId;      
    private final int duration;         
    private final LocalDateTime timestamp; 

    // Constructor to initialize a job request with client ID and duration
    public JobRequest(String clientId, int duration) {
        this.jobId = ++jobCounter;       // Increment job counter for a unique job ID
        this.clientId = clientId;
        this.duration = duration;
        this.timestamp = LocalDateTime.now(); // Capture the current time as submission time
    }

    
    public int getJobId() {
        return jobId;
    }

    
    public String getClientId() {
        return clientId;
    }

    
    public int getDuration() {
        return duration;
    }

    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    
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
