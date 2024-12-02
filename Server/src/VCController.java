
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VCController {
    private List<JobRequest> jobsQueue;
    private List<CarRentals> vehiclesReady;
    private ServerGUI gui;

    public VCController() {
        jobsQueue = new LinkedList<>();
        vehiclesReady = new ArrayList<>();
        loadJobsFromCSV();
    }

    public void setGUI(ServerGUI gui) {
        this.gui = gui;
    }

    private void loadJobsFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader("JobRequests.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) {
                    JobRequest job = new JobRequest(parts[0], parts[1], parts[2],
                            Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), parts[5]);
                    jobsQueue.add(job);
                    if (gui != null) {
                        gui.log("Loaded job: " + job.getJobId());
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            if (gui != null) {
                gui.log("Error loading jobs: " + e.getMessage());
            }
        }
    }

    public String handleJobSubmission(String message) {
        try {
            Pattern pattern = Pattern.compile("JOB_SUBMIT (\\S+) \"([^\"]+)\" (\\d+) (\\d+) (\\S+)");
            Matcher matcher = pattern.matcher(message);

            if (!matcher.matches()) {
                return "Invalid job submission format.";
            }

            JobRequest job = new JobRequest(
                    matcher.group(1),
                    matcher.group(2),
                    Integer.parseInt(matcher.group(3)),
                    Integer.parseInt(matcher.group(4)),
                    matcher.group(5));

            jobsQueue.add(job);
            logJobRequest(job);

            if (gui != null) {
                gui.log("New job submitted: " + job.getJobId());
            }

            return "Job submitted successfully with ID: " + job.getJobId();
        } catch (Exception e) {
            String error = "Error submitting job: " + e.getMessage();
            if (gui != null) {
                gui.log(error);
            }
            return error;
        }
    }

    public String handleCarReady(String message) {
        try {
            String[] parts = message.split(" ");
            if (parts.length < 8) {
                return "Invalid car readiness format.";
            }

            CarRentals car = new CarRentals(parts[1], parts[2], parts[3],
                    parts[4], parts[5], parts[6], parts[7]);
            vehiclesReady.add(car);
            logCarReady(car);

            Vehicle guiVehicle = new Vehicle(
                    generateVehicleId(),
                    "Available",
                    car.getOwnerId(),
                    car.getVehicleModel(),
                    car.getVehicleBrand(),
                    car.getPlateNumber(),
                    car.getSerialNumber(),
                    car.getVinNumber(),
                    0);

            if (gui != null) {
                gui.addVehicle(guiVehicle);
                gui.log("New vehicle registered: " + car.getVinNumber());
            }

            return "Car registered successfully.";
        } catch (Exception e) {
            String error = "Error registering car: " + e.getMessage();
            if (gui != null) {
                gui.log(error);
            }
            return error;
        }
    }

    private void logJobRequest(JobRequest job) {
        String jobInfo = String.format("%s,%s,%s,%d,%d,%s",
                job.getJobId(), job.getClientId(), job.getJobDescription(),
                job.getDuration(), job.getRedundancyLevel(), job.getJobDeadline());
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
            if (gui != null) {
                gui.log("Error saving to file " + fileName + ": " + e.getMessage());
            }
        }
    }

public String handleVehicleRemoval(String message) {
    try {
        String[] parts = message.split(" ");
        if (parts.length != 3) return "Invalid command format";

        String ownerId = parts[1];
        String vinNumber = parts[2];

        CarRentals carToRemove = vehiclesReady.stream()
            .filter(car -> car.getVinNumber().equals(vinNumber))
            .findFirst()
            .orElse(null);

        if (carToRemove != null) {
            vehiclesReady.remove(carToRemove);
            removeCarFromCSV(vinNumber);
            // Notify clients
            notifyVehicleRemoval(ownerId, vinNumber);
            if (gui != null) {
                gui.log("Vehicle removed: " + vinNumber);
            }
            return "Vehicle removed successfully";
        }
        return "Vehicle not found";
    } catch (Exception e) {
        return "Error removing vehicle: " + e.getMessage();
    }
}

private void notifyVehicleRemoval(String ownerId, String vinNumber) {
    String message = "VEHICLE_REMOVED " + ownerId + " " + vinNumber;
    // Send message to client through socket connection
    // Implementation depends on your client-server communication setup
}

    private int generateVehicleId() {
        return (int) (Math.random() * 10000);
    }

    public void allocateJobToCars(JobRequest job) {
        int requiredCars = job.getRedundancyLevel();
        List<CarRentals> allocatedCars = new ArrayList<>();

        for (CarRentals car : vehiclesReady) {
            if (allocatedCars.size() < requiredCars) {
                car.assignJob(job);
                allocatedCars.add(car);
                if (gui != null) {
                    gui.updateVehicleStatus(Integer.parseInt(car.getOwnerId()), "Assigned");
                    gui.log("Assigned job " + job.getJobId() + " to car " + car.getVinNumber());
                }
            }
        }

        if (gui != null) {
            if (allocatedCars.size() < requiredCars) {
                gui.log("Warning: Only allocated " + allocatedCars.size() + "/" + requiredCars
                        + " requested cars for job " + job.getJobId());
            } else {
                gui.log("Successfully allocated " + requiredCars + " cars to job " + job.getJobId());
            }
        }

    }

    // Restored and updated method to display jobs and completion times, with
    // role-based access
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
                    job.getJobId(), job.getClientId(), job.getJobDescription(), job.getDuration(), cumulativeTime));
            System.out.println("Returning job info: " + jobInfo.toString());
        }

        return jobInfo.toString();
    }

    // Optional method to get all jobs if the client requests with a specific role
    // (for further flexibility)
    public String getAllJobs() {
        StringBuilder jobInfo = new StringBuilder("All Jobs in Queue:\n");
        for (JobRequest job : jobsQueue) {
            jobInfo.append(String.format(
                    "Job ID: %s, Client ID: %s, Description: %s, Duration: %d, Redundancy Level: %d, Deadline: %s\n",
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
            deleteJobFromCSV(jobId); // Remove the job from the CSV
            return "Job marked as complete and removed successfully.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing MARK_COMPLETE request.";
        }
    }

    private boolean deleteJobFromCSV(String jobId) {
        File inputFile = new File("JobRequests.csv");
        File tempFile = new File("JobRequests_temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            boolean jobFound = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith(jobId + ",")) {
                    jobFound = true;
                    continue;
                }
                writer.println(line);
            }

            if (!jobFound) {
                if (gui != null)
                    gui.log("Job ID not found: " + jobId);
                return false;
            }

        } catch (IOException e) {
            if (gui != null)
                gui.log("Error processing JobRequests.csv: " + e.getMessage());
            return false;
        }

        if (!inputFile.delete()) {
            if (gui != null)
                gui.log("Failed to delete original file.");
            return false;
        }

        if (!tempFile.renameTo(inputFile)) {
            if (gui != null)
                gui.log("Failed to rename temporary file.");
            return false;
        }

        return true;
    }

    private void removeCarFromCSV(String vinNumber) {
        File inputFile = new File("CarRegistration.csv");
        File tempFile = new File("CarRegistration_temp.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                PrintWriter writer = new PrintWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6 && !parts[5].equals(vinNumber)) {
                    writer.println(line);
                }
            }

            if (!inputFile.delete()) {
                if (gui != null)
                    gui.log("Could not delete original file");
                return;
            }

            if (!tempFile.renameTo(inputFile)) {
                if (gui != null)
                    gui.log("Could not rename temp file");
            }

        } catch (IOException e) {
            if (gui != null)
                gui.log("Error processing CarRegistration.csv: " + e.getMessage());
        }
    }
}
