import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Scanner;


public class ClientUDP {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite a região (ex: America/Sao_Paulo): ");
        String region = scanner.nextLine();

        try (DatagramSocket clientSocket = new DatagramSocket()) {
            clientSocket.setSoTimeout(5000);

            InetAddress IPAddress = InetAddress.getByName("localhost");
            byte[] sendData = region.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
            clientSocket.send(sendPacket);

            byte[] receiveData = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            
            try {
                clientSocket.receive(receivePacket);
                String modifiedSentence = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.println("HORA NO SERVIDOR: " + modifiedSentence);
            } catch (SocketTimeoutException e) {
                System.err.println("Servidor ocupado ou offline (Timeout de 5s atingido)");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        scanner.close();
    }
}