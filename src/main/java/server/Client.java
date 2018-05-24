package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.logging.Logger;

public class Client {
    private static int ID_COUNTER = 0;

    private final static Logger logger = Logger.getAnonymousLogger();
    private Socket socket;
    private BiConsumer<String, Client> messageToOtherUsers;
    private PrintWriter printWriter;

    // TODO make private
    int clientID;

    public Client(Socket socket, BiConsumer<String, Client> messageToOtherUsers) {
        this.socket = socket;
        this.messageToOtherUsers = messageToOtherUsers;
        listenToClientMesseges();
        clientID = ID_COUNTER++;
    }

    public void sendMessage(String message) {
        try {
            printWriter = new PrintWriter(socket.getOutputStream());
            printWriter.println(message);
            printWriter.flush();
        } catch (IOException e) {
            logger.info("I was unable to send message to " + socket);
            e.printStackTrace();
        }
    }

    public void listenToClientMesseges() {
        new Thread(() -> {
            try {
                Scanner scanner = new Scanner(socket.getInputStream());
                String userInput;
                while (!(userInput = scanner.nextLine()).equalsIgnoreCase("BYE")) {
                    logger.info(socket.getInetAddress() + " send message: " + userInput);
                    messageToOtherUsers.accept(userInput, this);
                }
            } catch (IOException e) {
                logger.info("Unable to get info from user " + socket);
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return this.clientID == client.clientID;
    }

    @Override
    public int hashCode() {

        return Objects.hash(socket, messageToOtherUsers, printWriter);
    }
}
