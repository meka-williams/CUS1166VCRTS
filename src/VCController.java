import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class VCController {
    private int controllerID;                  // Identifier for the VCController
    private List<JobRequest> jobsQueue;        // Queue of jobs waiting to be processed
    private List<CarRentals> vehiclesReady;    // List of cars ready to process jobs
    private int redundancyLevel;               // Number of cars each job should be assigned to

    
    public VCController(int controllerID, int redundancyLevel) {
        this.controllerID = controllerID;
        this.redundancyLevel = redundancyLevel;
        this.jobsQueue = new LinkedList<>();
        this.vehiclesReady = new ArrayList<>();
    }

    
    public void submitJob(JobRequest job) {
        jobsQueue.add(job);          // Add job to queue
        logJobRequest(job);          // Log job details to VC job file
        assignJobToVehicles(job);    // Assign job to available vehicles with redundancy
    }

    // Logs job details to a dedicated file for VC records
    public void saveOwnerCarToFile(String ownerId, String vehicleBrand, String vehicleModel, String plateNumber, String serialNumber, String vinNumber, int residencyTime) {
        String fileName = "vcrts_data_owner.csv"; // Updated file name
        String header = "Owner ID,Vehicle Info,Residency Time";
        String data = String.format("%s,%s,%d", ownerId, vehicleBrand, vehicleModel, plateNumber, serialNumber, vinNumber, residencyTime);

        File file = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (!file.exists() || file.length() == 0) {
                writer.write(header);
                writer.newLine();
            }
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving owner car to file.");
        }
    
    }

    private void logJobRequest(JobRequest job) {
        String fileName = "VC_jobs.csv";
        String header = "Job ID,Client ID,Job Duration,Timestamp";
        String data = String.format("%d,%s,%d,%s", job.getJobId(), job.getClientId(), job.getDuration(), job.getTimestamp());

        File file = new File(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            if (!file.exists() || file.length() == 0) {
                writer.write(header);
                writer.newLine();
            }
            writer.write(data);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving job to VC job file.");
        }
    }

    
    private void assignJobToVehicles(JobRequest job) {
        int assignedCount = 0;
        for (CarRentals vehicle : vehiclesReady) {
            if (assignedCount < redundancyLevel) {
                vehicle.assignJob(job); // Assuming CarRentals has a method to assign a job
                assignedCount++;
            } else {
                break;
            }
        }
        if (assignedCount < redundancyLevel) {
            System.out.println("Warning: Not enough vehicles to meet redundancy level for job ID " + job.getJobId());
        }
    }

    // Calculates cumulative completion times for each job in the queue
    public String calculateCompletionTimes() {
        StringBuilder completionTimes = new StringBuilder("Job Completion Times (VCC):\n");
        int cumulativeTime = 0;

        for (JobRequest job : jobsQueue) {
            cumulativeTime += job.getDuration();
            completionTimes.append(String.format("Job ID: %d - Completion Time: %d hours\n", job.getJobId(), cumulativeTime));
        }
        
        return completionTimes.toString();
    }
    public String displayJobsAndCompletionTimes() {
        StringBuilder jobInfo = new StringBuilder("Assigned Jobs and Completion Times:\n");
        int cumulativeTime = 0;

        for (JobRequest job : jobsQueue) {
            cumulativeTime += job.getDuration();
            jobInfo.append(String.format("Job ID: %d, Client ID: %s, Duration: %d hours, Completion Time: %d hours\n",
                            job.getJobId(), job.getClientId(), job.getDuration(), cumulativeTime));
        }

        return jobInfo.length() > 0 ? jobInfo.toString() : "No jobs currently assigned.";
    }


    
    public void addVehicle(CarRentals vehicle) {
        vehiclesReady.add(vehicle);
    }
}
