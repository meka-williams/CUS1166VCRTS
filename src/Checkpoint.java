public class Checkpoint {
    private int checkpointID;
    private int jobID;
    private int vehicleID;
    private Object image;
    private float percentLeft;


    public Checkpoint(int checkpointID, int jobID, int vehicleID){
        this.checkpointID = checkpointID;
        this.jobID = jobID;
        this.vehicleID = vehicleID;
        this.percentLeft = 100.0f;

    }

    /**
     * This creates an image object that represents the current state of the Job
     * @returns Object representing the checkpoint image.
     */
    public Object createImage(){
        return new Object();
    }

    public void copyToNewVehicle(Vehicle vehicle){
        vehicle.setLastCheckpointID(this.checkpointID);
    }
    
    public void restartJob(){
        this.percentLeft = 100.0f;
    }
    

    public int getCheckpointID(){
        return checkpointID;
    }

    public void setCheckpointID(int checkpointID){
        this.checkpointID = checkpointID;
    }

    public int getJobID(){
        return jobID;
    }

    public void setJobID(int jobID){
        this.jobID = jobID;
    }

    public int getVehicleID(){
        return vehicleID;
    }

    public void setVehicleID(int vechileID){
        this.vehicleID = vechileID;
    }

    public float getPercentLeft(){
        return percentLeft;
    }

    public void setPercentLeft(float percentLeft){
        this.percentLeft = percentLeft;
    }

    public Object getImage(){
        return image;
    }

    public void setImage(Object image){
        this.image = image;
    }
}
