import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Arthur
 * @since 18/03/2026
 * Servidor TCP que escala usando Threads.
 */
public class ServerTCPMultithread {
    public static void main(String[] args) {
        int porta = 9878;

        try (ServerSocket welcomeSocket = new ServerSocket(porta)) {
            System.out.println("Servidor Multithread ON na porta " + porta);

            while (true) {
                // O loop principal NÃO processa nada, apenas aceita
                Socket connectionSocket = welcomeSocket.accept();
                
                // Dispara uma nova Thread para cada cliente
                Thread threadProcessamento = new Thread(new Worker(connectionSocket));
                threadProcessamento.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}