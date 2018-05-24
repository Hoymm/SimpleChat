package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Clients {
    private final static Logger logger = Logger.getAnonymousLogger();
    private final List<Client> clients;

    Clients() {
        clients = new ArrayList<>();
    }

    public void add(Client accept) {
        clients.add(accept);
    }

    public int howManyClients(){
        return clients.size();
    }

    void sendMessageToAll(String message) {
        for (Client client : clients){
            client.sendMessage(message);
        }
    }

    void sendMessageToAllDespite(String message, Client clientsNotToMessage) {
        System.out.println("NOT TO SEND" + clientsNotToMessage.clientID);
        for (Client client : clients){
            if (!client.equals(clientsNotToMessage)) {
                System.out.println("SEND" + client.clientID);
                client.sendMessage(message);
            }
        }
    }

    void runAddingService(ServerSocket serverSocket) {
        new Thread(() -> {
            while(true) {
                try {
                    Socket socket = serverSocket.accept();
                    Client client = new Client(socket, this::sendMessageToAllDespite);
                    clients.add(client);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
