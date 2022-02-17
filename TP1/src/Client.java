import java.util.InputMismatchException;
import java.util.Scanner;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime; 
import java.net.InetAddress;

public class Client {
	private static Socket socket;
	private static DataOutputStream out;
	private static DataInputStream in; 

	
	public static void main(String[] args) throws Exception
	{
		
		Scanner consoleReader = new Scanner(System.in);	
		
		String serverAddress = InputValidator.getValidAddress(consoleReader);
		int serverPort = InputValidator.getValidPort(consoleReader);
		
		System.out.println("Entrez votre username: ");
		String username =  consoleReader.nextLine();
		
		System.out.println("Entrez votre mot de passe :");
		String password = consoleReader.nextLine();
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		InetAddress ip = InetAddress.getLocalHost();
		
		socket = new Socket(serverAddress, serverPort);
		
	
		// Creation d'un canal entrant pour recevoir les message envoyes par le serveur
		out = new DataOutputStream(socket.getOutputStream());
		
	
		out.writeUTF(username + " " + password);
	
		//Fermeture de la connexion avec le serveur

		//socket.close();
		String message;
		do {
		message = sendMessage();
		}
		while (message !=  "exit");
		
	}
	public static String sendMessage()  {
		
		Scanner consoleReader = new Scanner(System.in);	
		
		String message;
		do {
		System.out.println("Entrez votre message");
		message = consoleReader.nextLine();
		}
		while(!InputValidator.isMessageValid(message)); 
		
		try {
			out.writeUTF(message);
		}
		
				catch (Exception e) {
					
				}
				
		
		return message;
		
	}
}
