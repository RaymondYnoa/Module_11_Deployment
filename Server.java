/* *
 * CEN 3024C
 * @version Module 11 Deployment Assignment
 * @author Raymond Ynoa
 * 
 * Write a client/server application with two parts, a server and a client.
 * Have the client send the server a request to compute whether a number that
 * the user provided is prime. The server responds with yes or no, then the
 * client displays the answer.
 * */

// Imported Java packages.
import java.io.*;
import java.net.*;

// Server side.
public class Server {
    public static void main(String[] args) {
        int port = 8080; // Use available port
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Listening on port " + port);
            
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket.getInetAddress().getHostAddress());
                
                // Create a new thread to handle each client request.
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler extends Thread {
    private final Socket clientSocket;
    
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }
    
    @Override
    public void run() {
        try (DataInputStream input = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream())) {
            
            // Read the number from the client.
            int number = input.readInt();
            boolean isPrime = isPrime(number);
            
            // Send the result back to the client.
            output.writeBoolean(isPrime);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Helper function to check if a number is prime.
    private boolean isPrime(int number) {
        if (number <= 1)
            return false; // Is not prime.
        
        for (int i = 2; i * i <= number; i++) {
            if (number % i == 0)
                return false; // Is not prime.
        }
        return true; // Is prime.
    }
}