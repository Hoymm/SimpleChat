package server;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Logger;

public class ChatServer {
    private static final Logger logger = Logger.getAnonymousLogger();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7777);
        Clients clients = new Clients();
        clients.runAddingService(serverSocket);

    }
}
