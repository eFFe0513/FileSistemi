import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class CalcolatriceServer {

    private static final int PORTA = 8844;

    public static void main(String[] args) {
        int contatoreOperazioni = 0;

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Server avviato sulla porta " + PORTA);

            boolean serverAttivo = true;
            while (serverAttivo) {
                try (Socket clientSocket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    String richiesta;
                    while ((richiesta = in.readLine()) != null) {
                        richiesta = richiesta.trim();
                        if (richiesta.equalsIgnoreCase("QUIT")) {
                            out.println("CHIUSURA Operazioni eseguite " + contatoreOperazioni + ". Arrivederci!");
                            serverAttivo = false;
                            break;
                        }

                        String[] parti = richiesta.split(" ");
                        if (parti.length != 3) {
                            out.println("ERRORE Formato non valido. Usa NUMERO OPERAZIONE NUMERO");
                            continue;
                        }

                        double num1, num2;
                        try {
                            num1 = Double.parseDouble(parti[0]);
                            num2 = Double.parseDouble(parti[2]);
                        } catch (NumberFormatException e) {
                            out.println("ERRORE Formato non valido. Usa NUMERO OPERAZIONE NUMERO");
                            continue;
                        }

                        String operazione = parti[1];
                        double risultato;

                        try {
                            switch (operazione) {
                                case "+":
                                    risultato = num1 + num2;
                                    break;
                                case "-":
                                    risultato = num1 - num2;
                                    break;
                                case "*":
                                    risultato = num1 * num2;
                                    break;
                                case "/":
                                    if (num2 == 0) {
                                        out.println("ERRORE Divisione per zero non consentita");
                                        continue;
                                    }
                                    risultato = num1 / num2;
                                    break;
                                default:
                                    out.println("ERRORE Operazione non supportata");
                                    continue;
                            }
                        } catch (Exception ex) {
                            out.println("ERRORE " + ex.getMessage());
                            continue;
                        }

                        contatoreOperazioni++;
                        out.println("RISULTATO " + risultato);
                    }

                } catch (IOException e) {
                    System.out.println("Errore comunicazione client: " + e.getMessage());
                }
            }
            System.out.println("Server terminato.");

        } catch (IOException e) {
            System.err.println("Errore avvio server: " + e.getMessage());
        }
    }
}
