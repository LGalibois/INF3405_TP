import java.io.DataInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime; 
import java.net.InetAddress;

public class Client {
	private static Socket socket;
	
	/*
	 * Application client
	 */
	
	
	//public static String getValidMessage(BufferedReader Message) {
		
	//	if (Message.readLine().length() > 200 ) {
		
	//		System.out.println("Le message ne peux pas dépacer 200 caractères");
		
	//	}
	//	else
	//		System.out.println("Le message validé");
			
	//		return Message.readLine();
	//}
	
	public static void main(String[] args) throws Exception
	{

		// Adresse et port du serveur	
		BufferedReader screenReader = new BufferedReader(new InputStreamReader(System.in));
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		InetAddress ip = InetAddress.getLocalHost();
		
		String serverAddress = screenReader.readLine();
		String port = screenReader.readLine();
		
		
			
		
		System.out.println(serverAddress + " " + port + " " +  dtf.format(now) + " " + ip.getHostAddress());
		

		
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
