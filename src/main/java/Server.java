import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {
    public static final int PORT = 8080;
    public static LinkedList<ServerToClientConnection> serverConnections = new LinkedList<>();
    public static MessageStory messageStory;

    public static void main(String[] args) throws IOException {
        messageStory = new MessageStory();
        System.out.println("Server Started");
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = server.accept();
                try {
                    serverConnections.add(new ServerToClientConnection(socket));
                } catch (IOException e) {
                    socket.close();
                }
            }
        }
    }
}
