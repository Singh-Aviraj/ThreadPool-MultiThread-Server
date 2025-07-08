import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Server {

    public void serve() throws IOException {
        System.out.println("Server is running...");
        int port = 8010; // Default port
        ServerSocket socket = new ServerSocket(port);
        socket.setSoTimeout(10000);
        while (true) {
            try{
                System.out.println("Waiting for a connection...");
                Socket client = socket.accept();
                System.out.println("Client connected: " + client.getInetAddress().getHostAddress());

                PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                out.println("HTTP/1.1 200 OK");
                out.println("Content-Type: text/plain");
                out.close();
                in.close();
                client.close();

            }catch(IOException ex){
                System.out.println("Socket timed out or error occurred: " + ex.getMessage());
                continue; // Continue to wait for the next connection
            }
        }
    }



    public static void main(String[] args) {
        Server server = new Server();
        try {
            server.serve();
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
        }

    }
}
