import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Server {
    private File server = new File("src/Server.csv");
    //private File carRegistrationServer = new File("src/CarRegistration.csv");
    //private File jobRequestServer = new File("src/CarRegistration.csv");
    private String data;
    private ArrayList<User> users;
    private ArrayList<Client> clients;
    private ArrayList<CarOwner> carOwners;

    public Server(){
        data = "";
        users = new ArrayList<User>();
        clients = new ArrayList<Client>();
        carOwners = new ArrayList<CarOwner>();
    }

    /**
     * Validates if the user is client user
     * @param username - the username of the user
     * @return true if the user is a client user and false if not
     */
    public boolean isClient(String username){
        for(Client client: clients){
            if(client.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the the Client information of the user using the username to find it
     * @param username - the username of the user
     * @return the Client information of the user; returns null if the user not found
     */
    public Client getClient(String username) {
        for(Client client: clients){
            if(client.getUsername().equals(username)) {
                return client;
            }
        }
        return null;
    }

    /**
     * Adds a client user to the list of clients
     * @param client - the new client
     */
    public void addClient(Client client) {
        clients.add(client);
    }

    /**
     * Returns the list of clients currently in the server
     * @return the list of clients
     */
    public Client[] getClients(){
        Client[] client = new Client[clients.size()];
        for(int i = 0; i < client.length; i++){
            client[i] = clients.get(i);
        }
        return client;
    }

    /**
     * Validates if the user is a car owner user
     * @param username - the username of the user
     * @return true if the user is car owner and false otherwise
     */
    public boolean isCarOwner(String username){
        for(CarOwner carOwner : carOwners){
            if(carOwner.equals(username)){
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @param username
     * @return
     */
    public CarOwner getCarOwner(String username) {
        for(CarOwner carOwner: carOwners){
            if(carOwner.getUsername().equals(username)){
                return carOwner;
            }
        }
        return null;
    }

    /**
     * 
     * @param carOwner
     */
    public void addCarOwner(CarOwner carOwner){
        carOwners.add(carOwner);
    }

    /**
     * 
     * @param username
     * @return
     */
    public boolean isUser(String username) {
        for(User user: users) {
            if(user.getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

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
        String newData = action + "," + user + "," + date + "\n";
        data = data.concat(newData);

        try {
            FileWriter writer = new FileWriter(server);
            writer.write(data);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
