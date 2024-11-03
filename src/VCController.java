
import java.util.ArrayList;
import java.util.List;

public class VCController {
    private int controllerID;
    private List<JobRequest> jobsQueue;
    private List<CarRentals> vehiclesReady;
    private int redundancyLevel;
    private static VCController instance;

    public VCController(int controllerID) {
        this.controllerID = controllerID;
        this.jobsQueue = new ArrayList<>();
        this.vehiclesReady = new ArrayList<>();
        this.redundancyLevel = 1;
    }

    public static VCController getInstance() {
        if (instance == null) {
            instance = new VCController(1);
        }
        return instance;
    }

    /**
     * This assigns a job to available vehicles
     * @param job The job to be assigned
     */
    public void assignJobToVehicles(JobRequest job) {
        if (vehiclesReady.isEmpty()) {
            // If No vehicles available, add to queue
            jobsQueue.add(job);
            return;
        }

        // This finds available vehicles and assign job
        int assignedCount = 0;
        for (CarRentals vehicle : vehiclesReady) {
            if (assignedCount < redundancyLevel) {
                assignJob(vehicle, job);
                assignedCount++;
            } else {
                break;
            }
        }
    }

    private void assignJob(CarRentals vehicle, JobRequest job) {
        try {
            // Validation
            if (vehicle == null || job == null) {
                System.out.println("Error: Vehicle or job is null");
                return;
            }
    
            // This checks if vehicle has required information
            if (vehicle.getVehicleModel().isEmpty() || vehicle.getPlateNumber().isEmpty()) {
                System.out.println("Error: Vehicle information is incomplete");
                return;
            }
    
            // This set's job as in progress
            job.setInProgress(true);
            
            // This Log's the assignment
            System.out.println("Job assigned successfully:");
            System.out.println("Vehicle Model: " + vehicle.getVehicleModel());
            System.out.println("Plate Number: " + vehicle.getPlateNumber());
            System.out.println("Job Title: " + job.getTitle());
            System.out.println("Duration: " + job.getDurationTime() + " hours");
            
        } catch (Exception e) {
            System.out.println("Error assigning job: " + e.getMessage());
            job.setInProgress(false);
        }
    }

    /**
     * Creates a checkpoint for a vehicle's current job state
     * @param vehicle The vehicle to create checkpoint for
     * @return Checkpoint object containing the job state
     */
    public Checkpoint triggerCheckpoint(CarRentals vehicle) {
        if (vehicle == null) {
            return null;
        }

        Checkpoint checkpoint = new Checkpoint(
            generateCheckpointID(),
            0, // You'll need to implement job ID tracking
            Integer.parseInt(vehicle.getSerialNumber()) // Using serial number as vehicle ID
        );
        
        return checkpoint;
    }

    /**
     * Recruits a new vehicle into the system
     * @return The newly recruited vehicle
     */
    public CarRentals recruitNewVehicle() {
        CarRentals newVehicle = new CarRentals(); // Using default constructor
        vehiclesReady.add(newVehicle);
        return newVehicle;
    }

    /**
     * Marks a job as complete and updates system state
     * @param jobID The ID of the completed job
     */
    public void submitJobComplete(int jobID) {
        // Remove job from queue if present
        jobsQueue.removeIf(job -> job.getJobId() == jobID);
    }

    /**
     * Removes a vehicle from the ready queue
     * @param vehicle The vehicle to remove
     * @return true if vehicle was removed, false otherwise
     */
    public boolean removeVehicleFromQueue(CarRentals vehicle) {
        return vehiclesReady.remove(vehicle);
    }

    public CarRentals getVehicleById(int vehicleId) {
        for (CarRentals vehicle : vehiclesReady) {
            if (vehicle.getSerialNumber().equals(String.valueOf(vehicleId))) {
                return vehicle;
            }
        }
        return null;
    }

    // Helper methods
    private int generateCheckpointID() {
        return (int) (System.currentTimeMillis() % Integer.MAX_VALUE);
    }

    // Getters and Setters
    public int getControllerID() {
        return controllerID;
    }

    public void setControllerID(int controllerID) {
        this.controllerID = controllerID;
    }

    public List<JobRequest> getJobsQueue() {
        return jobsQueue;
    }

    public List<CarRentals> getVehiclesReady() {
        return vehiclesReady;
    }

    public int getRedundancyLevel() {
        return redundancyLevel;
    }

    public void setRedundancyLevel(int redundancyLevel) {
        this.redundancyLevel = redundancyLevel;
    }
}
