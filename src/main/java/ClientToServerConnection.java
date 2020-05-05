import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClientToServerConnection {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private final Socket socket;
    private final BufferedReader socketReader;
    private final BufferedWriter socketWriter;
    private final BufferedReader consoleReader;
    private String username;

    public ClientToServerConnection(String host, int port) throws IOException {
        socket = new Socket(host, port);
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
        socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void start() throws IOException {
        askToEnterUsername();
        new ReadMessage().start();
        new WriteMessage().start();
    }

    private void askToEnterUsername() throws IOException {
        System.out.print("Enter your name: ");
        username = consoleReader.readLine();
        socketWriter.write("Hello " + username + "\n");
        socketWriter.flush();
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                socketReader.close();
                socketWriter.close();
            }
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private class ReadMessage extends Thread {
        @Override
        public void run() {
            String message;
            try {
                while (true) {
                    message = socketReader.readLine();
                    if (message.equals("stop")) {
                        downService();
                        break;
                    }
                    System.out.println(message);
                }
            } catch (IOException e) {
                downService();
            }
        }
    }

    private class WriteMessage extends Thread {
        @Override
        public void run() {
            while (true) {
                String message;
                try {
                    String formattedTime = LocalTime.now().format(FORMATTER);
                    message = consoleReader.readLine();
                    if (message.equals("stop")) {
                        socketWriter.write("stop" + "\n");
                        downService();
                        break;
                    } else {
                        socketWriter.write("(" + formattedTime + ") "
                                + username + ": " + message + "\n");
                    }
                    socketWriter.flush();
                } catch (IOException e) {
                    downService();
                }
            }
        }
    }
}
