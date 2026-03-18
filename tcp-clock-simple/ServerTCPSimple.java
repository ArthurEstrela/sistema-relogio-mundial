import java.io.*;
import java.net.*;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author Arthur
 * @since 18/03/2026
 *        Servidor TCP Simples (Atende um por vez)
 */
public class ServerTCPSimple {
    public static void main(String[] args) {
        int porta = 9877; // Usando porta diferente do UDP

        try (ServerSocket welcomeSocket = new ServerSocket(porta)) {
            System.out.println("Servidor TCP (Single-Thread) rodando na porta " + porta);

            while (true) {
                // O servidor trava aqui até alguém conectar
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("Cliente conectado! Iniciando processamento de 7 segundos...");

                Thread.sleep(7000); // <--- ADICIONE ISSO AQUI (importa java.lang.* ou trata a exceção)

                BufferedReader inFromClient = new BufferedReader(
                        new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                // 1. Lê a região
                String regionId = inFromClient.readLine();
                System.out.println("Solicitação: " + regionId);

                // 2. Processa a hora
                String response;
                try {
                    ZonedDateTime now = ZonedDateTime.now(ZoneId.of(regionId.trim()));
                    response = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n";
                } catch (Exception e) {
                    response = "Erro: Região inválida!\n";
                }

                // 3. Responde e fecha a conexão do cliente (Requisito da Parte 2)
                outToClient.writeBytes(response);
                connectionSocket.close();
                System.out.println("Conexão encerrada com o cliente.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}