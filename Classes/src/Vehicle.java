
public class Vehicle {

    private int vehicleID;
    private String status;
    private int ownerID;
    private String model;
    private String brand;
    private String plateNumber;
    private String serialNumber;
    private String vinNum;
    private int residencyTime;
    private int lastCheckpointID; // Field to store the last checkpoint ID

    // Constructor
    public Vehicle(int vehicleID, String status, int ownerID, String model, String brand, 
                   String plateNumber, String serialNumber, String vinNum, int residencyTime) {
        this.vehicleID = vehicleID;
        this.status = status;
        this.ownerID = ownerID;
        this.model = model;
        this.brand = brand;
        this.plateNumber = plateNumber;
        this.serialNumber = serialNumber;
        this.vinNum = vinNum;
        this.residencyTime = residencyTime;
        this.lastCheckpointID = -1; // Initialize with -1 to indicate no checkpoint
    }

    // Method to set the last checkpoint ID for this vehicle
    public void setLastCheckpointID(int checkpointID) {
        this.lastCheckpointID = checkpointID;
    }

    // Method to retrieve the last checkpoint ID
    public int getLastCheckpointID() {
        return lastCheckpointID;
    }

    // Other getters and setters for the fields
    public int getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(int vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getVinNum() {
        return vinNum;
    }

    public void setVinNum(String vinNum) {
        this.vinNum = vinNum;
    }

    public int getResidencyTime() {
        return residencyTime;
    }

    public void setResidencyTime(int residencyTime) {
        this.residencyTime = residencyTime;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "vehicleID=" + vehicleID +
                ", status='" + status + '\'' +
                ", ownerID=" + ownerID +
                ", model='" + model + '\'' +
                ", brand='" + brand + '\'' +
                ", plateNumber='" + plateNumber + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", vinNum='" + vinNum + '\'' +
                ", residencyTime=" + residencyTime +
                ", lastCheckpointID=" + lastCheckpointID +
                '}';
    }
}
