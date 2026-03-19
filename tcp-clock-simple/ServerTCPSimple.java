/**
 * Autor: Arthur Faria Estrela
 * Data: 18/03/2026
 * Resumo: Esta classe implementa um servidor TCP Single-Thread. Ela atende às requisições 
 * de forma sequencial, onde aceita a conexão de um cliente, processa a região recebida 
 * para retornar a hora exata, devolve a resposta e encerra o socket antes de atender o próximo.
 */
import java.io.*;
import java.net.*;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class ServerTCPSimple {
    public static void main(String[] args) {
        int porta = 9877; 

        try (ServerSocket welcomeSocket = new ServerSocket(porta)) {
            System.out.println("Servidor TCP (Single-Thread) rodando na porta " + porta);

            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("Cliente conectado: " + connectionSocket.getInetAddress());

                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                String regionId = inFromClient.readLine();
                System.out.println("Solicitação: " + regionId);

                String response;
                try {
                    ZonedDateTime now = ZonedDateTime.now(ZoneId.of(regionId.trim()));
                    response = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n";
                } catch (Exception e) {
                    response = "Erro: Região inválida!\n";
                }

                outToClient.writeBytes(response);
                connectionSocket.close(); 
                System.out.println("Conexão encerrada com o cliente.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}