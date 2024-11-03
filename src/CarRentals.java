public class CarRentals {
    private String vehicleModel;
    private String brand;
    private String plateNumber;
    private String serialNumber;
    private String vinNumber;
    private int vehicleID;

    public CarRentals(){
        vehicleModel = "";
        plateNumber = "";
        vehicleID = 0;
    }

    public CarRentals(String vehicleModel, String brand, String plateNumber, String serialNumber, String vinNumber){
        this.vehicleModel = vehicleModel;
        this.brand = brand;
        this.plateNumber = plateNumber;
        this.serialNumber = serialNumber;
        this.vinNumber = vinNumber;
        this.vehicleID = vehicleID;
    }
    

    /**
     * @return String return the vehicleModel
     */
    public String getVehicleModel() {
        return vehicleModel;
    }

    /**
     * @param vehicleModel the vehicleModel to set
     */
    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    /**
     * @return String return the brand
     */
    public String getBrand() {
        return brand;
    }

    /**
     * @param brand the brand to set
     */
    public void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @return String return the plateNumber
     */
    public String getPlateNumber() {
        return plateNumber;
    }

    /**
     * @param plateNumber the plateNumber to set
     */
    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    /**
     * @return String return the serialNumber
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber the serialNumber to set
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @return String return the vinNumber
     */
    public String getVinNumber() {
        return vinNumber;
    }

    /**
     * @param vinNumber the vinNumber to set
     */
    public void setVinNumber(String vinNumber) {
        this.vinNumber = vinNumber;
    }

    @Override
    public String toString() {
        return "Vehicle Model" + vehicleModel + "," + 
        "Brand: " + brand + "," + 
        "Plate Number: " + plateNumber + "," +
        "Serial Number: " + serialNumber + "," +
        "VIN Number: " + vinNumber +
        "Vehicle ID: " + vehicleID;
    }

    public int getVehicleID(){
        return vehicleID;
    }

    public void setVehicleID() {
        this.vehicleID = vehicleID;
    }



}
