import java.io.*;
import java.net.*;
import java.util.Scanner;

public class CalcolatriceClient {

    private static final String HOST = "localhost";
    private static final int PORTA = 8844;

    public static void main(String[] args) {
        try (Socket socket = new Socket(HOST, PORTA);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {

            // Stampa indicazioni per l'utente
            System.out.println("CALCOLATRICE REMOTA");
            System.out.println("Formato: NUMERO OPERAZIONE NUMERO");
            System.out.println("Operazioni supportate: + - * /");
            System.out.println("Scrivi 'quit' per uscire.");

            // Si occupa dell'invio e del collegamento con il server
            String input;
            while (true) {
                System.out.print("Calcolo> ");
                input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("quit")) {
                    out.println("QUIT");
                    String risposta = in.readLine();
                    System.out.println("Server: " + risposta);
                    break;
                }

                out.println(input);
                String risposta = in.readLine();
                System.out.println("Server: " + risposta);
            }

        } catch (IOException e) {
            System.err.println("Errore connessione: " + e.getMessage());
        }
    }
}

