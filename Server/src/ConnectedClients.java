import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectedClients {
 private List<Socket> clients = new ArrayList<>();
    
    public synchronized void add(Socket client) {
        clients.add(client);
    }
    
    public synchronized void remove(Socket client) {
        clients.remove(client);
    }
    
    public synchronized List<Socket> getAll() {
        return new ArrayList<>(clients);
    }
} 