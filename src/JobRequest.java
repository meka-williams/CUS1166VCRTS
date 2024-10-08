public class JobRequest {
    private boolean inProgress;
    private String title;
    private int durationTime;
    private Client jobOwner;

    public JobRequest(){
        this.title = "";
        this.durationTime = -1;
    }

    public JobRequest(String title, int durationTime){
        this.title = title;
        this.durationTime = durationTime;
    }


    /**
     * @return boolean return the inProgress
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * @param inProgress the inProgress to set
     */
    public void setInProgress(boolean inProgress) {
        this.inProgress = inProgress;
    }

    /**
     * @return String return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return int return the durationTime
     */
    public int getDurationTime() {
        return durationTime;
    }

    /**
     * @param durationTime the durationTime to set
     */
    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }

    /**
     * @return Client return the jobOwner
     */
    public Client getJobOwner() {
        return jobOwner;
    }

    /**
     * @param jobOwner the jobOwner to set
     */
    public void setJobOwner(Client jobOwner) {
        this.jobOwner = jobOwner;
    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return super.toString();
    }

}
