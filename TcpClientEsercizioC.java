package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class TcpClient {
	public static void main(String[] args) throws Exception {
		
		String severAddress="127.0.0.1";  // localhost
		int severPort=2230; // Selezione porta 2230
		String clientMsg = "";
		String serverMsg = "";
		
		try {
			// Creazione della connessione tra server e socket
			System.out.print("Client: Connessione al server=" + severAddress + ":" + severPort + " ... ");
			Socket socket = new Socket(severAddress, severPort); 
			System.out.println("Connected");

			// Crea input e output streams
			BufferedReader inUserStream = new BufferedReader(new InputStreamReader(System.in));
			// Input stream per i dati provenienti dal socket 
			DataInputStream inSocketStream = new DataInputStream(socket.getInputStream());
			// Output stream 
			DataOutputStream outSocketStream = new DataOutputStream(socket.getOutputStream());
			
			while (!clientMsg.equals("quit")) {
				// Chiedi all'utente il messaggio da inserire
				System.out.print("Client: inserisci il messaggio da inviare> ");
				clientMsg = inUserStream.readLine();

				// Manda il testo inserito al server
				System.out.println("Client: invio il messaggio: " + clientMsg);
				outSocketStream.writeUTF(clientMsg);
				outSocketStream.flush();

				// Leggi dati dal socket input stream
				serverMsg = inSocketStream.readUTF();
				System.out.println("Client: ricevuto il messaggio: " + serverMsg);
				
				// Controlla se il server ha terminato la connessione
				if (serverMsg.equals("CONDIZIONE_RAGGIUNTA")) {
					System.out.println("Client: Condizione raggiunta - consonanti = metà vocali");
					break;
				}
			}

			// Chiusura risorse
			outSocketStream.close();
			inSocketStream.close();
			inUserStream.close();
			socket.close();		
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
