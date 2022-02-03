import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.net.Socket;

public class ClientHandler extends Thread 
{
	int MAX_MESSAGE_LENGTH = 200;
	
	private Socket socket;
	private List<ClientHandler> clients;
	private DataOutputStream out;
	private DataInputStream in;
	private int clientNumber;
	
	public ClientHandler(Socket socket, List<ClientHandler> clients, int clientNumber)
	{
		this.socket = socket;
		this.clientNumber = clientNumber;
		this.clients = clients;
		System.out.println("New connection with client#" + clientNumber + " at " + socket);
	}
	
	private void onMessageReceived(String message) throws IOException {
		for (ClientHandler client : clients) {
			if (client != this) {
				client.sendMessage(message);
			}
		}
	}
	
	public void sendMessage(String message) throws IOException {
		if (out != null) {
			out.writeUTF(new String(message));
		}
	}
	
	public void run()
	{
		
		
		try
		{
<<<<<<< HEAD
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			
			out.writeUTF("Hello from server - you are client #" + clientNumber);
			
=======
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			
			while (!socket.isClosed()) {
				if (in.available() > 0) {
					onMessageReceived(in.readUTF());
				}
			}
>>>>>>> fed910ae7a7987849b8affd99818e2fe630634e0
		}
		catch (IOException e)
		{
			System.out.println("Error handling client# " + clientNumber + ": " + e);
		}
		finally
		{
			try
			{
				socket.close();
			}
			catch (IOException e)
			{
				System.out.println("Could not close a socket, what's going on?");
			}
			System.out.println("Connection with client# " + clientNumber + " closed");
		}
	}
}