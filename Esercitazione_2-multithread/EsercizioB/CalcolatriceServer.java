import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CalcolatriceServer {

    public static void main(String[] args) {
        int port = 8844;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server avviato sulla porta " + port);

            while (true) {
                // Accetta un client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuova connessione accettata");

                // Avvia un thread che gestisce quel client
                new Thread(new ClientHandler(clientSocket)).start();
            }

        } catch (IOException e) {
            System.out.println("Errore nel server: " + e.getMessage());
        }
    }

        // Runnable per gestire il singolo client
        private static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ) {
                String richiesta;
                // Leggiamo più richieste finché il client non chiude la connessione
                while ((richiesta = in.readLine()) != null) {
                    richiesta = richiesta.trim();
                    if (richiesta.isEmpty()) {
                        continue;
                    }

                    System.out.println("Richiesta ricevuta: " + richiesta);

                    // Se il client manda "quit" terminiamo la connessione
                    if (richiesta.equalsIgnoreCase("quit")) {
                        out.println("Arrivederci");
                        break;
                    }

                    String risultato = calcola(richiesta);
                    out.println(risultato);
                    // rimani in ascolto per una eventuale prossima richiesta sullo stesso socket
                }

            } catch (IOException e) {
                System.out.println("Errore connessione client: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }

        // Calcolatrice identica alla precedente
        private String calcola(String input) {
            try {
                String[] parts = input.split("\\s+");
                double a = Double.parseDouble(parts[0]);
                String op = parts[1];
                double b = Double.parseDouble(parts[2]);

                switch (op) {
                    case "+": return "" + (a + b);
                    case "-": return "" + (a - b);
                    case "*": return "" + (a * b);
                    case "/": return (b != 0) ? "" + (a / b) : "Errore: divisione per zero";
                    default: return "Operatore non valido";
                }

            } catch (Exception e) {
                return "Formato non valido (usa: numero operatore numero)";
            }
        }
    }
}

