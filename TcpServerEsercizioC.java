/**
 * from network/..
 * javac network/TcpServer.java; java network.TcpServer 
 */
package network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
	public static void main(String[] args) throws Exception {
		
		int severPort=2230;
		String clientMsg = "";
		
		try {			 
			// Creazione del socket sul server e ascolto sulla porta
			ServerSocket serverSocket = new ServerSocket(severPort);
			System.out.println("Server: in ascolto sulla porta " + severPort);

			// Attesa della connessione con il client
			Socket clientSocket = serverSocket.accept();
			
			// Create input and output streams to read/write data
			DataInputStream inStream = new DataInputStream(clientSocket.getInputStream());
			DataOutputStream outStream = new DataOutputStream(clientSocket.getOutputStream());	

			// Scambio di dati tra client e server
			while(!clientMsg.equals("quit")) {
				//Lettura dato da stream di rete
				clientMsg = inStream.readUTF();
				System.out.println("Server: ricevuto messaggio " + clientMsg );
				
				// Conta vocali e consonanti
				int vocali = 0;
				int consonanti = 0;
				String text = clientMsg.toLowerCase();
				
				for (int i = 0; i < text.length(); i++) {
					char c = text.charAt(i);
					if (Character.isLetter(c)) {
						if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u') {
							vocali++;
						} else {
							consonanti++;
						}
					}
				}
				
				// Verifica condizione di terminazione
				if (consonanti * 2 == vocali && consonanti > 0) {
					outStream.writeUTF("CONDIZIONE_RAGGIUNTA");
					outStream.flush();
					System.out.println("Server: condizione raggiunta - consonanti=" + consonanti + ", vocali=" + vocali);
					break;
				}
				
				//Invio dati su stream di rete
				String response = "Vocali: " + vocali + ", Consonanti: " + consonanti;
				outStream.writeUTF(response);
				outStream.flush();
				System.out.println("Server: invio messaggio " + response);
			}

			// Close resources
			serverSocket.close();
			clientSocket.close();
			inStream.close();
			outStream.close();

		} catch (Exception e) {
			System.out.println(e);
		}
	}
}