import java.io.*;
import java.net.*;
import java.util.Scanner;


public class ClientTCP {
    public static void main(String[] args) {
        String servidor = "localhost";
        int porta = 9877;

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Digite a região (TCP): ");
            String region = scanner.nextLine();

            try (Socket clientSocket = new Socket(servidor, porta)) {
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                outToServer.writeBytes(region + "\n");

                String response = inFromServer.readLine();
                System.out.println("RESPOSTA DO SERVIDOR: " + response);
            } 
        } catch (IOException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
    }
}