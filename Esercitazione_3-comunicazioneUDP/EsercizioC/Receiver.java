import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Receiver {

    private static final int PORT = 1313;
    private static final String FILE_NAME = "connections.txt";

    public static void main(String[] args) {
        System.out.println("Server UDP avviato sulla porta " + PORT);

        HashMap<String, Integer> counter = loadFromFile();

        try (DatagramSocket socket = new DatagramSocket(PORT)) {

            byte[] buffer = new byte[1024];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String clientIP = packet.getAddress().getHostAddress();

                int count = counter.getOrDefault(clientIP, 0) + 1;
                counter.put(clientIP, count);
                saveToFile(counter);

                String response;

                if (count <= 9) {
                    response = getDateTime();
                } 
                else if (count == 10) {
                    response = "Bonus esauriti. Dalla prossima richiesta serve una API key.";
                } 
                else {
                    response = "Servizio a pagamento. Inserire API key.";
                }

                byte[] data = response.getBytes();
                DatagramPacket reply = new DatagramPacket(
                        data, data.length, packet.getAddress(), packet.getPort());

                socket.send(reply);

                System.out.println("IP: " + clientIP + " | Connessione: " + count + " | Risposta: " + response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getDateTime() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(f);
    }

    private static HashMap<String, Integer> loadFromFile() {
        HashMap<String, Integer> map = new HashMap<>();

        File file = new File(FILE_NAME);
        if (!file.exists()) return map;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(" ");
                map.put(p[0], Integer.parseInt(p[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static void saveToFile(HashMap<String, Integer> map) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (String ip : map.keySet()) {
                pw.println(ip + " " + map.get(ip));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
