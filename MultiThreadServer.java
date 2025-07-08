import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class MultiThreadServer {

    private static final int PORT = 8010;

    // Tumhare 12 core CPU ke liye tuned
    private static final int CORE_POOL_SIZE = 8000;
    private static final int MAX_POOL_SIZE = 16000;
    private static final int QUEUE_CAPACITY = 2000000; // 2 million backlog
  // Large queue
    private static final long KEEP_ALIVE_TIME = 300;   // 5 min
    // Seconds

    public static void main(String[] args) throws IOException {
        ExecutorService threadPool = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy() // Agar queue full ho toh caller thread handle kare
        );

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("🚀 Server started on port " + PORT);
            System.out.println("💡 Thread Pool: Core=" + CORE_POOL_SIZE + ", Max=" + MAX_POOL_SIZE + ", Queue=" + QUEUE_CAPACITY);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("✅ New client connected: " + clientSocket.getInetAddress());
                    threadPool.execute(new ClientHandler(clientSocket));
                } catch (RejectedExecutionException e) {
                    System.err.println("⚠️ Too many requests! Client rejected.");
                }
            }
        } finally {
            threadPool.shutdown();
        }
    }

    static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                String firstLine = in.readLine();

                if (firstLine == null) {
                    System.err.println("❌ Empty request received.");
                    return;
                }

                System.out.println("📩 First Line: " + firstLine);

                // Check if it's an HTTP request (wrk sends HTTP GET)
                if (firstLine.startsWith("GET") || firstLine.startsWith("POST")) {
                    handleHttpRequest(in, out);
                } else {
                    handleCustomClient(firstLine, in, out);
                }

            } catch (IOException | InterruptedException e) {
                System.err.println("❌ Client error: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("🔒 Client disconnected.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void handleHttpRequest(BufferedReader in, PrintWriter out) throws IOException {
            // Consume remaining HTTP headers
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                System.out.println("🌐 HTTP Header: " + line);
            }

            String responseBody = "Hello from Java Server!";
            String httpResponse =
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/plain\r\n" +
                            "Content-Length: " + responseBody.length() + "\r\n" +
                            "Connection: close\r\n" +
                            "\r\n" +
                            responseBody;

            out.print(httpResponse);
            out.flush();
            System.out.println("✅ Sent HTTP response");
        }

        private void handleCustomClient(String firstLine, BufferedReader in, PrintWriter out) throws IOException, InterruptedException {
            out.println("🖐 Welcome to High Performance Server!");
            System.out.println("📩 Received (Custom Client): " + firstLine);

            if ("exit".equalsIgnoreCase(firstLine)) {
                out.println("👋 Goodbye!");
                return;
            }

            // Simulate processing delay (can be removed)
            Thread.sleep(50);

            out.println("✅ Echo: " + firstLine);

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("📩 Received (Custom Client): " + inputLine);

                if ("exit".equalsIgnoreCase(inputLine)) {
                    out.println("👋 Goodbye!");
                    break;
                }

                 // Simulated delay
                out.println("✅ Echo: " + inputLine);
            }
        }
    }
}
