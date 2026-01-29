import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Sender {

    private static final String SERVER_IP = "127.0.0.1";
    private static final int SERVER_PORT = 1313;

    public static void main(String[] args) {

        try (DatagramSocket socket = new DatagramSocket()) {

            byte[] data = "richiesta".getBytes();
            InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

            DatagramPacket packet = new DatagramPacket(data, data.length, serverAddr, SERVER_PORT);
            socket.send(packet);

            byte[] buffer = new byte[1024];
            DatagramPacket response = new DatagramPacket(buffer, buffer.length);
            socket.receive(response);

            String msg = new String(response.getData(), 0, response.getLength());
            System.out.println("Risposta dal server: " + msg);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


