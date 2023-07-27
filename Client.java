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
import javax.swing.*;

// Client side.
public class Client extends JFrame {
    private final JTextField inputField;
    private final JButton checkButton;
    private final JLabel resultLabel;
    
    // Client user interactive UI.
    public Client() {
        setTitle("Prime Number Checker");
        setSize(300, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        inputField = new JTextField(10);
        checkButton = new JButton("Check Prime");
        checkButton.addActionListener(e -> checkPrime());
        resultLabel = new JLabel("Enter a number and click 'Check Prime'.");
        
        panel.add(inputField);
        panel.add(checkButton);
        panel.add(resultLabel);
        add(panel);
    }
    
    private void checkPrime() {
        try (Socket socket = new Socket("localhost", 8080)) {
            int number = Integer.parseInt(inputField.getText());
            
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());
            
            // Send the number to the server
            output.writeInt(number);
            
            // Receive the result from the server
            boolean isPrime = input.readBoolean();
            
            // Display the result to the user
            resultLabel.setText(number + " is " + (isPrime ? "prime" + "." : "not prime" + "."));
            
        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid input. Please enter a valid number.");
        } catch (IOException ex) {
            resultLabel.setText("Error connecting to the server.");
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Client client = new Client();
            client.setVisible(true);
        });
    }
}