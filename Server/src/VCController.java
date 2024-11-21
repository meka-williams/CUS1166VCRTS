
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VCController {
    private List<JobRequest> jobsQueue;
    private List<CarRentals> vehiclesReady;

    public VCController() {
        this.jobsQueue = new LinkedList<>();
        this.vehiclesReady = new ArrayList<>();
        loadJobsFromCSV();
    }
    private void loadJobsFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("JobRequests.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) { // Expecting 6 parts in each job record
                    String jobId = parts[0];
                    String clientId = parts[1];
                    String jobDescription = parts[2];
                    int duration = Integer.parseInt(parts[3]);
                    int redundancyLevel = Integer.parseInt(parts[4]);
                    String jobDeadline = parts[5];

                    JobRequest job = new JobRequest(jobId, clientId, jobDescription, duration, redundancyLevel, jobDeadline);
                    jobsQueue.add(job);
                    System.out.println("Loaded job from CSV: " + job); // Debug output
                } else {
                    System.err.println("Invalid job format in CSV: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading jobs from CSV: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing job details: " + e.getMessage());
        }
    }
    public String handleJobSubmission(String message) {
        System.out.println("Received job submission message: " + message); // Debugging
        Pattern pattern = Pattern.compile("JOB_SUBMIT (\\S+) \"([^\"]+)\" (\\d+) (\\d+) (\\S+)");
        Matcher matcher = pattern.matcher(message);

        if (!matcher.matches()) {
            return "Invalid job submission format.";
        }

        String clientId = matcher.group(1);
        String jobDescription = matcher.group(2);
        int duration = Integer.parseInt(matcher.group(3));
        int redundancyLevel = Integer.parseInt(matcher.group(4));
        String jobDeadline = matcher.group(5);

        JobRequest job = new JobRequest(clientId, jobDescription, duration, redundancyLevel, jobDeadline);
        jobsQueue.add(job);  // Add to queue with correct clientId
        logJobRequest(job);

        System.out.println("Job successfully added to queue for clientId: " + clientId); // Debugging
        System.out.println("Current jobsQueue: " + jobsQueue); // Print entire queue for verification

        return "Job submitted and logged successfully with ID: " + job.getJobId();
    }


    public String handleCarReady(String message) {
        String[] parts = message.split(" ");
        if (parts.length < 8) {
            return "Error: Invalid car readiness format.";
        }

        String ownerId = parts[1];
        String model = parts[2];
        String brand = parts[3];
        String plateNumber = parts[4];
        String serialNumber = parts[5];
        String vinNumber = parts[6];
        String residencyDate = parts[7];

        CarRentals car = new CarRentals(ownerId, model, brand, plateNumber, serialNumber, vinNumber, residencyDate);
        vehiclesReady.add(car);
        logCarReady(car);

        return "Car ready notification logged successfully.";
    }

    private void logJobRequest(JobRequest job) {
        String jobInfo = String.format("%s,%s,%s,%d,%d,%s", 
                                       job.getJobId(), 
                                       job.getClientId(), 
                                       job.getJobDescription(), 
                                       job.getDuration(), 
                                       job.getRedundancyLevel(),
                                       job.getJobDeadline());
        appendToFile("JobRequests.csv", jobInfo);
    }

    private void logCarReady(CarRentals car) {
        String carInfo = String.format("%s,%s,%s,%s,%s,%s,%s",
                                       car.getOwnerId(), car.getVehicleModel(), car.getVehicleBrand(), 
                                       car.getPlateNumber(), car.getSerialNumber(), 
                                       car.getVinNumber(), car.getResidencyTime());
        appendToFile("CarRegistration.csv", carInfo);
    }

    private void appendToFile(String fileName, String data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName, true))) {
            writer.println(data);
        } catch (IOException e) {
            System.err.println("Error saving to file: " + fileName);
        }
    }

    public void allocateJobToCars(JobRequest job) {
        int requiredCars = job.getRedundancyLevel();
        List<CarRentals> allocatedCars = new ArrayList<>();

        for (CarRentals car : vehiclesReady) {
            if (allocatedCars.size() < requiredCars) {
                car.assignJob(job);
                allocatedCars.add(car);
            }
        }

        if (allocatedCars.size() < requiredCars) {
            System.out.println("Warning: Not enough cars available for requested redundancy level.");
        } else {
            System.out.println("Job assigned to " + allocatedCars.size() + " cars.");
        }
    }

    // Restored and updated method to display jobs and completion times, with role-based access
    public String displayJobsAndCompletionTimes(String clientId, String role) {
        if (!"VCCController".equals(role)) {
            return "Access denied: Insufficient permissions.";
        }

        StringBuilder jobInfo = new StringBuilder("All Assigned Jobs and Completion Times:\n");
        int cumulativeTime = 0;

        for (JobRequest job : jobsQueue) {
            cumulativeTime += job.getDuration();
            jobInfo.append(String.format(
                "Job ID: %s, Client ID: %s, Description: %s, Duration: %d hours, Completion Time: %d hours\n",
                job.getJobId(), job.getClientId(), job.getJobDescription(), job.getDuration(), cumulativeTime
            ));
            System.out.println("Returning job info: " + jobInfo.toString());
        }

        return jobInfo.toString();
    }


    // Optional method to get all jobs if the client requests with a specific role (for further flexibility)
    public String getAllJobs() {
        StringBuilder jobInfo = new StringBuilder("All Jobs in Queue:\n");
        for (JobRequest job : jobsQueue) {
            jobInfo.append(String.format("Job ID: %s, Client ID: %s, Description: %s, Duration: %d, Redundancy Level: %d, Deadline: %s\n",
                                         job.getJobId(), job.getClientId(), job.getJobDescription(),
                                         job.getDuration(), job.getRedundancyLevel(), job.getJobDeadline()));
        }
        return jobInfo.toString();
    }
    public String markJobComplete(String message) {
        try {
            String[] parts = message.split(" ");
            if (parts.length != 2) {
                return "Error: Invalid MARK_COMPLETE command format.";
            }

            String jobId = parts[1].trim();

            // Find and remove the job from the queue
            JobRequest jobToRemove = null;
            for (JobRequest job : jobsQueue) {
                if (job.getJobId().equals(jobId)) {
                    jobToRemove = job;
                    break;
                }
            }

            if (jobToRemove == null) {
                return "Error: Job ID not found.";
            }

            jobsQueue.remove(jobToRemove); // Remove the job from the in-memory queue
            deleteJobFromCSV(jobId);       // Remove the job from the CSV
            return "Job marked as complete and removed successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing MARK_COMPLETE request.";
        }
    }

    // Helper method to remove a job from the CSV
    private boolean deleteJobFromCSV(String jobId) {
        File inputFile = new File("JobRequests.csv");
        File tempFile = new File("JobRequests_temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            boolean jobFound = false;

            // Read each line and write to the temp file, excluding the job to delete
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(jobId + ",")) {
                    jobFound = true;
                    continue; // Skip this job
                }
                writer.println(line); // Write other jobs to temp file
            }

            if (!jobFound) {
                System.err.println("Job ID not found: " + jobId);
                return false;
            }

        } catch (IOException e) {
            System.err.println("Error processing JobRequests.csv: " + e.getMessage());
            return false;
        }

        // Delete the original file
        if (!inputFile.delete()) {
            System.err.println("Failed to delete original file.");
            return false;
        }

        // Rename temp file to original file name
        if (!tempFile.renameTo(inputFile)) {
            System.err.println("Failed to rename temporary file.");
            return false;
        }

        return true;
    }


}
