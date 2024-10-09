import java.io.File;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Server {
    private File userServer = new File("src/UserInformation.csv");
    private File carRegistrationServer = new File("src/CarRegistration.csv");
    private File jobRequestServer = new File("src/CarRegistration.csv");

    private BufferedReader userReader = new BufferedReader(new FileReader("src/UserInformation.csv"));
    private BufferedReader carReader = new BufferedReader(new FileReader("src/CarRegistration.csv"));
    private BufferedReader jobReader = new BufferedReader(new FileReader("src/JobRequest.csv"));

    private String data;
    private ArrayList<User> users;
    private ArrayList<JobRequest> jobs;
    private ArrayList<CarRentals> cars;

    public Server() throws IOException{
        data = "";
        String line = "";
        while((line = userReader.readLine()) != null) {
            String[] tokens = line.split(",");
            users.add(new User(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]), tokens[6]);
        }

        line = "";
        while((line = carReader.readLine()) != null){
            String[] tokens = line.split(",");
            cars.add(new CarRentals(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4]), tokens[5]);
        }

        line = "";
        while((line = jobReader.readLine()) != null) {
            String[] tokens= line.split(",");
            jobs.add(new JobRequest(tokens[0], Integer.parseInt(tokens[1])), tokens[2]);
        }
    }

    // /**
    //  * Validates if the user is client user
    //  * @param username - the username of the user
    //  * @return true if the user is a client user and false if not
    //  */
    // public boolean isClient(String username){
    //     for(Client client: clients){
    //         if(client.getUsername().equals(username)) {
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    // /**
    //  * Returns the the Client information of the user using the username to find it
    //  * @param username - the username of the user
    //  * @return the Client information of the user; returns null if the user not found
    //  */
    // public Client getClient(String username) {
    //     for(Client client: clients){
    //         if(client.getUsername().equals(username)) {
    //             return client;
    //         }
    //     }
    //     return null;
    // }

    // /**
    //  * Adds a client user to the list of clients
    //  * @param client - the new client
    //  */
    // public void addClient(Client client) {
    //     clients.add(client);
    // }

    // /**
    //  * Returns the list of clients currently in the server
    //  * @return the list of clients
    //  */
    // public Client[] getClients(){
    //     Client[] client = new Client[clients.size()];
    //     for(int i = 0; i < client.length; i++){
    //         client[i] = clients.get(i);
    //     }
    //     return client;
    // }

    // /**
    //  * Validates if the user is a car owner user
    //  * @param username - the username of the user
    //  * @return true if the user is car owner and false otherwise
    //  */
    // public boolean isCarOwner(String username){
    //     for(CarOwner carOwner : carOwners){
    //         if(carOwner.equals(username)){
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    // /**
    //  * 
    //  * @param username
    //  * @return
    //  */
    // public CarOwner getCarOwner(String username) {
    //     for(CarOwner carOwner: carOwners){
    //         if(carOwner.getUsername().equals(username)){
    //             return carOwner;
    //         }
    //     }
    //     return null;
    // }

    // /**
    //  * 
    //  * @param carOwner
    //  */
    // public void addCarOwner(CarOwner carOwner){
    //     carOwners.add(carOwner);
    // }

    // /**
    //  * 
    //  * @param username
    //  * @return
    //  */
    // public boolean isUser(String username) {
    //     for(User user: users) {
    //         if(user.getUsername().equals(username)){
    //             return true;
    //         }
    //     }
    //     return false;
    // }

    /**
     * 
     * @param username
     * @return
     */
    public User getUser(String username) {
        for(User user: users) {
            if(user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public void addUser(User user){
        users.add(user);
    }

    public boolean accountFound(String username, String password) {
        for(User user: users){
            if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void updateServer(String action, User user){
        Date date = new Date();
        
        if(action.equals("Sign Up")){
            users.add(user);
            data = user.getUsername() + "," + user.getFname() + "," + user.getLname() + "," +user.getDateOfBirth() + "," + user.getEmailAddress() + "," + user.getPassword();

        }
        else if(action.equals("Car Register")) {
            //data = 
        }
        else if(action.equals("Job Request")) {

        }
        
    }
}
