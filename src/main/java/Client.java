import java.io.IOException;

public class Client {
    public static final String LOCALHOST = "localhost";
    public static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        new ClientToServerConnection(LOCALHOST, PORT).start();
    }
}
