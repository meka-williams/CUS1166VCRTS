import java.util.ArrayList;

public class Client extends User{
    private ArrayList<JobRequest> requestedJobs;
    
    public Client(String username, String password){
        super(username, password);
        requestedJobs = new ArrayList<>();
    }

    public Client(String username, String fname, String lname, String emailAddress, String password){
        super(username, fname, lname, lname, emailAddress, password);
        requestedJobs = new ArrayList<JobRequest>();
    }

    public void submitJob(JobRequest jobRequest){
        requestedJobs.add(jobRequest);
    }

    public String getQueuedJobs() {
        String allRequestedJobs = "";
        for(int i = 0; i < requestedJobs.size(); i++){
            allRequestedJobs = allRequestedJobs.concat(String.valueOf(requestedJobs.get(i)));
        }
        return allRequestedJobs;
    }

    @Override
    public String toString() {
        return "Client ID: " + this.getUsername() + getQueuedJobs();
    }
}
