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

    private static final int PORT = 1313; // Porta del server
    private static final String FILE_NAME = "connections.txt"; // File per salvare connessioni
    private static final String API_KEY = "12345"; // API KEY valida

    public static void main(String[] args) {
        System.out.println("Server UDP avviato sulla porta " + PORT);

        HashMap<String, Integer> counter = loadFromFile(); // Carica conteggi da file

        try (DatagramSocket socket = new DatagramSocket(PORT)) {

            byte[] buffer = new byte[1024]; // Buffer per dati in ingresso

            while (true) { // Server sempre attivo
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet); // Riceve richiesta

                String richiesta = new String(packet.getData(), 0, packet.getLength()); // Messaggio client
                String clientIP = packet.getAddress().getHostAddress(); // IP client

                int count = counter.getOrDefault(clientIP, 0) + 1; // Incrementa contatore
                counter.put(clientIP, count);
                saveToFile(counter); // Salva su file

                String risposta;

                if (count <= 9) {
                    risposta = getDateTime(); // Accesso gratuito
                } 
                else {
                    // Controllo API KEY
                    if (count == 10) {
                        risposta = "Bonus esauriti! Il servizio è ora a pagamento.";
                    } else if (richiesta.equals(API_KEY)) {
                        risposta = "Accesso consentito con API KEY: " + getDateTime();
                    } else {
                        risposta = "Inserire la API KEY per sbloccare il servizio.";
                    }
                }

                byte[] data = risposta.getBytes(); // Converte risposta in byte
                DatagramPacket reply = new DatagramPacket(
                        data, data.length, packet.getAddress(), packet.getPort());

                socket.send(reply); // Invia risposta

                // Log su console
                System.out.println("IP: " + clientIP + " | Connessione: " + count + " | Richiesta: " + richiesta + " | Risposta: " + risposta);
            }

        } catch (IOException e) {
            e.printStackTrace(); // Gestione errori
        }
    }

    // Restituisce data e ora corrente
    private static String getDateTime() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(f);
    }

    // Carica i dati dal file
    private static HashMap<String, Integer> loadFromFile() {
        HashMap<String, Integer> map = new HashMap<>();

        File file = new File(FILE_NAME);
        if (!file.exists()) return map; // Se file non esiste

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(" "); // Divide IP e conteggio
                map.put(p[0], Integer.parseInt(p[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    // Salva i dati su file
    private static void saveToFile(HashMap<String, Integer> map) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (String ip : map.keySet()) {
                pw.println(ip + " " + map.get(ip)); // Scrive IP e conteggio
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}