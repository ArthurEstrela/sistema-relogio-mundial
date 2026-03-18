import java.io.*;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @author Arthur
 * @since 18/03/2026
 * Classe responsável pelo processamento individual de cada cliente em uma Thread separada.
 */
public class Worker implements Runnable {
    // O uso de 'final' aqui resolve o problema do "effectively final" para o try-with-resources
    private final Socket s;

    public Worker(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        // Coleta informações para os logs exigidos no requisito
        String threadInfo = Thread.currentThread().getName() + " (ID: " + Thread.currentThread().getId() + ")";
        String clientInfo = s.getInetAddress() + ":" + s.getPort();
        
        System.out.println("\n[LOG] Thread " + threadInfo + " cuidando do cliente " + clientInfo);

        // O 'try (s)' garante que o socket será fechado automaticamente ao fim do bloco
        try (s) { 
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            // Lê a região enviada pelo cliente
            String regionId = in.readLine();
            
            // Opcional: Descomente para testar a concorrência no terminal
            // Thread.sleep(5000); 

            String response;
            if (regionId != null && !regionId.isEmpty()) {
                try {
                    ZonedDateTime now = ZonedDateTime.now(ZoneId.of(regionId.trim()));
                    response = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "\n";
                } catch (Exception e) {
                    response = "Erro: Região '" + regionId + "' inválida!\n";
                }
            } else {
                response = "Erro: Nenhuma região informada.\n";
            }

            // Envia a resposta final
            out.writeBytes(response);
            System.out.println("[LOG] Finalizado atendimento de " + clientInfo);

        } catch (Exception e) {
            System.err.println("Erro no processamento do cliente " + clientInfo + ": " + e.getMessage());
        }
    }
}