/**
 * Client UDP che legge messaggio dall'utente
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Sender {

    private static final String DEST_IP = "127.0.0.1";
    private static final int DEST_PORT = 8698;

    public static void main(String[] args) throws IOException 
    {
        //Create Datagram socket
        DatagramSocket socket = new DatagramSocket();
        
        // legge il messaggio dall'utente
        Scanner scanner = new Scanner(System.in);
        System.out.print("Inserisci il messaggio da inviare: ");
        String message = scanner.nextLine();
        scanner.close();
        
        System.out.println("Sender: starting on port " + DEST_PORT);
        InetAddress destIpAddr = InetAddress.getByName(DEST_IP);
        DatagramPacket sendPacket = new DatagramPacket(message.getBytes(), message.length(), destIpAddr, DEST_PORT);
        socket.send(sendPacket);
        byte[] buf = new byte[1024];
        DatagramPacket recv = new DatagramPacket(buf, buf.length);
        socket.receive(recv); // attende risposta dal server
        String risposta = new String(recv.getData(), 0, recv.getLength());
        System.out.println("Sender: risposta dal server = '" + risposta + "'");
        socket.close();
    }
}

