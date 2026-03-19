import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


public class ServerUDP {
    public static void main(String[] args) {
        int porta = 9876;
        try (DatagramSocket serverSocket = new DatagramSocket(porta)) {
            System.out.println("Servidor UDP rodando na porta " + porta);

            byte[] receiveData = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                String regionId = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                System.out.println("Solicitação recebida: " + regionId);

                String response;
                try {
                    ZonedDateTime now = ZonedDateTime.now(ZoneId.of(regionId));
                    response = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                } catch (Exception e) {
                    response = "Erro: Região inválida!";
                }

                byte[] sendData = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(
                    sendData, 
                    sendData.length, 
                    receivePacket.getAddress(),
                    receivePacket.getPort()
                );
                serverSocket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}