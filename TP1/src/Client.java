import java.util.InputMismatchException;
import java.util.Scanner;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime; 
import java.net.InetAddress;

public class Client {
	private final static String REGISTRATION_GRANTED_MESSAGE = "registration granted";
	
	private static Socket socket;
	private static DataOutputStream out;
	private static DataInputStream in;
	private static Scanner consoleReader;
	
	public static void main(String[] args) throws Exception
	{
		consoleReader = new Scanner(System.in);	
		
		String serverAddress = InputValidator.getValidAddress(consoleReader);
		int serverPort = InputValidator.getValidPort(consoleReader);
		
		socket = new Socket(serverAddress, serverPort);
		
	
		// Creation d'un canal entrant pour recevoir les message envoyes par le serveur
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(socket.getInputStream());
		
		connect();
		MessageReceiver messageReceiver = new MessageReceiver(socket);
		messageReceiver.start();

		String message;
		do {
			message = sendMessage();
		} while (message !=  "exit");
		
		messageReceiver.stop();
		socket.close();
	}

	public static void connect()
	{
		String response = "";
		
		do 
		{
			String username = InputValidator.getValidUsername(consoleReader);
			String password = InputValidator.getValidPassword(consoleReader);
			
			try {
				out.writeUTF(String.format("%s %s", username, password));
				response = in.readUTF();
			}
			catch(Exception exception)
			{
				System.out.println(exception.getMessage());
			}	
		}while(response != REGISTRATION_GRANTED_MESSAGE);
	}
	
	public static String sendMessage()  {
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
