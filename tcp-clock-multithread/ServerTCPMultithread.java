/**
 * Autor: Arthur Faria Estrela
 * Data: 18/03/2026
 * Resumo: Esta classe implementa o servidor TCP Concorrente (Multithread). O objetivo é 
 * garantir escalabilidade: ao aceitar a conexão de um cliente, o servidor delega o 
 * atendimento a uma nova Thread (usando a classe Worker) e volta imediatamente a aguardar novas conexões.
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class ServerTCPMultithread {
    public static void main(String[] args) {
        int porta = 9878;

        try (ServerSocket welcomeSocket = new ServerSocket(porta)) {
            System.out.println("Servidor Multithread ON na porta " + porta);

            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                
                Thread threadProcessamento = new Thread(new Worker(connectionSocket));
                threadProcessamento.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}