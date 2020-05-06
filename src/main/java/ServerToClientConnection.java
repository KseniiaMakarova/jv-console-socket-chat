import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerToClientConnection extends Thread {
    private final Socket socket;
    private final BufferedReader socketReader;
    private final BufferedWriter socketWriter;

    public ServerToClientConnection(Socket socket) throws IOException {
        this.socket = socket;
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Server.messageStory.printStory(socketWriter);
        start();
    }

    @Override
    public void run() {
        String message;
        try {
            message = socketReader.readLine();
            socketWriter.write(message + "\n");
            socketWriter.flush();
            while (true) {
                message = socketReader.readLine();
                if (message == null || message.equals("stop")) {
                    this.downService();
                    break;
                }
                System.out.println("Echoing: " + message);
                Server.messageStory.addToStory(message);
                for (ServerToClientConnection connection : Server.serverConnections) {
                    connection.send(message);
                }
            }
        } catch (IOException e) {
            this.downService();
        }
    }

    private void send(String message) {
        try {
            socketWriter.write(message + "\n");
            socketWriter.flush();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private void downService() {
        if (!socket.isClosed()) {
            try {
                socket.close();
                socketReader.close();
                socketWriter.close();
            } catch (IOException e) {
                e.getMessage();
            }
            for (ServerToClientConnection connection : Server.serverConnections) {
                if (connection.equals(this)) {
                    connection.interrupt();
                }
                Server.serverConnections.remove(this);
            }
        }
    }
}
