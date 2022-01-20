import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class Client {
	private static Socket socket;
	
	/*
	 * Application client
	 */
	public static void main(String[] args) throws Exception
	{

		// Adresse et port du serveur	
		BufferedReader screenReader = new BufferedReader(new InputStreamReader(System.in));
		
		String serverAddress = screenReader.readLine();
		String port = screenReader.readLine();
		
		System.out.println(serverAddress + " " + port);
		
		String test = screenReader.readLine();
		
		String test2 = "";
		
		// Creation d'une nouvelle connexion avec le serveur
		//socket= new Socket(serverAddress, port);
		
		//System.out.format("The server is running on %s :%d%n", serverAddress, port);
		
		// Creation d'un canal entrant pour recevoir les message envoyes par le serveur
		//DataInputStream in = new DataInputStream(socket.getInputStream());
		
		// Attente de la reception d'un message envoye par le serveur sur le canal
		//String helloMessageFromServer = in.readUTF();
		//System.out.println(helloMessageFromServer);
		
		//Fermeture de la connexion avec le serveur

		//socket.close();
	}
	
	
}
