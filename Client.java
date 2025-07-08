import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Client {

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8010;
    private static final int TOTAL_CLIENTS = 1000;
    private static final int REQUESTS_PER_CLIENT = 1000;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService clientPool = Executors.newFixedThreadPool(200); // 200 parallel clients

        for (int i = 0; i < TOTAL_CLIENTS; i++) {
            clientPool.submit(() -> {
                try (
                        Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                        BufferedReader in = new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(
                                socket.getOutputStream(), true)
                ) {
                    System.out.println("Connected: " + in.readLine());

                    for (int j = 0; j < REQUESTS_PER_CLIENT; j++) {
                        out.println("Hello from client " + Thread.currentThread().getId() + " req#" + j);
                        String response = in.readLine();
                        if (response != null)
                            System.out.println("📨 Server: " + response);
                    }
                    out.println("exit");

                } catch (IOException e) {
                    System.err.println("Client error: " + e.getMessage());
                }
            });
        }

        clientPool.shutdown();
        clientPool.awaitTermination(10, TimeUnit.MINUTES);
        System.out.println("🎉 Load test complete.");
    }
}
