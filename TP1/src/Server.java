import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.Scanner;

public class Server {
	private static ServerSocket listener;
	
	public static void main(String[] args) throws Exception
	{
		Scanner consoleReader = new Scanner(System.in);
		ChatRoom chatRoom = new ChatRoom("Main Room");
		
		String serverAddress = InputValidator.getValidAddress(consoleReader);
		int serverPort = InputValidator.getValidPort(consoleReader);
		
		listener = new ServerSocket();
		listener.setReuseAddress(true);
		InetAddress serverIp = InetAddress.getByName(serverAddress);
		
		listener.bind(new InetSocketAddress(serverIp, serverPort));
		
		System.out.format("The server is running on %s:%d%n", serverAddress, serverPort);
		
		try
		{
			while (true)
			{
				ClientHandler newClient = new ClientHandler(listener.accept(), chatRoom);
				newClient.start();
			}
		}
		finally
		{
			listener.close();
		}
	}
}


