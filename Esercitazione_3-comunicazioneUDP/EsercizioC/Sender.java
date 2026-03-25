import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Sender {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1313;

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {

            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            // Invio richiesta
            String risposta = sendAndReceive(socket, serverAddr, "richiesta");
            System.out.println("Risposta: " + risposta);

            // Se serve API KEY, chiedere all'utente di inserirla
            if (risposta.contains("API KEY")) {

                System.out.print("Inserisci API KEY: ");
                String apiKey = scanner.nextLine(); // input utente

                risposta = sendAndReceive(socket, serverAddr, apiKey);
                System.out.println("Risposta con API KEY: " + risposta);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Metodo per inviare e ricevere
    private static String sendAndReceive(DatagramSocket socket, InetAddress addr, String msg) throws IOException {

        byte[] data = msg.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, addr, SERVER_PORT);
        socket.send(packet);

        byte[] buffer = new byte[1024];
        DatagramPacket response = new DatagramPacket(buffer, buffer.length);
        socket.receive(response);

        return new String(response.getData(), 0, response.getLength());
    }
}