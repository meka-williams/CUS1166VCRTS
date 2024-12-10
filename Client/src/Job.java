public class Job {
    private String jobId;
    private String clientId;
    private String description;
    private int duration;
    private int redundancyLevel;
    private String deadline;
    private String status;
    private String completionTime;

    public Job(String jobId, String clientId, String description, int duration,
               int redundancyLevel, String deadline, String status, String completionTime) {
        this.jobId = jobId;
        this.clientId = clientId;
        this.description = description;
        this.duration = duration;
        this.redundancyLevel = redundancyLevel;
        this.deadline = deadline;
        this.status = status;
        this.completionTime = completionTime;
    }

    // Getters
    public String getJobId() { return jobId; }
    public String getClientId() { return clientId; }
    public String getDescription() { return description; }
    public int getDuration() { return duration; }
    public int getRedundancyLevel() { return redundancyLevel; }
    public String getDeadline() { return deadline; }
    public String getStatus() { return status; }
    public String getCompletionTime() { return completionTime; }

    // Setters
    public void setStatus(String status) { this.status = status; }
    public void setCompletionTime(String completionTime) { this.completionTime = completionTime; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return jobId.equals(job.jobId);
    }

    @Override
    public int hashCode() {
        return jobId.hashCode();
    }
}
