import java.io.*;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Worker implements Runnable {
    private final Socket s;

    public Worker(Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        String threadInfo = Thread.currentThread().getName() + " (ID: " + Thread.currentThread().getId() + ")";
        String clientInfo = s.getInetAddress() + ":" + s.getPort();
        
        System.out.println("\n[LOG] Thread " + threadInfo + " cuidando do cliente " + clientInfo);

        try (s) { 
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            DataOutputStream out = new DataOutputStream(s.getOutputStream());

            String regionId = in.readLine();


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

            out.writeBytes(response);
            System.out.println("[LOG] Finalizado atendimento de " + clientInfo);

        } catch (Exception e) {
            System.err.println("Erro no processamento do cliente " + clientInfo + ": " + e.getMessage());
        }
    }
}