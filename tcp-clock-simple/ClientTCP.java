import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * @author Arthur
 * @since 18/03/2026
 */
public class ClientTCP {
    public static void main(String[] args) {
        String servidor = "localhost";
        int porta = 9877;

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Digite a região (TCP): ");
            String region = scanner.nextLine();

            // Abre o socket e conecta
            try (Socket clientSocket = new Socket(servidor, porta)) {
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                // Envia a região (precisa do \n para o readLine() do servidor funcionar)
                outToServer.writeBytes(region + "\n");

                // Recebe e exibe a resposta
                String response = inFromServer.readLine();
                System.out.println("RESPOSTA DO SERVIDOR: " + response);
            } 
        } catch (IOException e) {
            System.err.println("Erro ao conectar: " + e.getMessage());
        }
    }
}