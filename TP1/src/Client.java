import java.util.InputMismatchException;
import java.util.Scanner;
import java.net.Socket;
import java.io.IOException;

public class Client {
	private static Socket socket;
	
	/*
	 * Application client
	 */
	static final int NUMBER_OF_SEGMENT = 4;
	static final int MINIMUM_PORT = 5000;
	static final int MAXIMUM_PORT = 5050;
	
	public static void main(String[] args) throws Exception
	{
		String serverAddress;
		int serverPort;
		Scanner consoleReader = new Scanner(System.in);	
		
		serverAddress = getValidAddress(consoleReader);
		serverPort = getValidPort(consoleReader);
		
		System.out.println(serverAddress + " " + serverPort);
		
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
	
	public static boolean validateServerAddress(String serverAddress)
	{
		boolean result = false;
		
		String[] serverAddressSegment = serverAddress.split("\\.");
		if(serverAddressSegment.length == NUMBER_OF_SEGMENT) {
			try 
			{	
				result = true;
				for (int i = 0; i < NUMBER_OF_SEGMENT; i++)
				{
					int segment = Integer.parseInt(serverAddressSegment[i]);
					result = result && segment >= 0 && segment <= 128;
				}
			}
			catch(NumberFormatException exception)
			{
				System.out.println("Les segment de l'adresse ip doivent etre des chiffres");
			}
			
		}
		
		return result;
	}
	
	public static String getValidAddress(Scanner consoleReader) 
	{
		String serverAddress;
		
		do 
		{
			System.out.println("Entrez l'addresse du serveur :");
			serverAddress = consoleReader.nextLine();
		}while(!validateServerAddress(serverAddress));
		
		return serverAddress;
	}
	
	public static int getValidPort(Scanner consoleReader)
	{
		boolean isValid = false;
		int serverPort = 5000;
		
		do
		{
			try
			{	
				System.out.println("Entrez le port du serveur :");
				serverPort = consoleReader.nextInt();
				
				isValid = serverPort >= MINIMUM_PORT && serverPort <= MAXIMUM_PORT;
			}
			catch(InputMismatchException error)
			{
				System.out.println("Le port doit etre un nombre");
			}
		}while(!isValid);
		
		return serverPort;
	}
}
