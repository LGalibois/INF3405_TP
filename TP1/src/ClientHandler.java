import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import org.json.simple.parser.ParseException;

import java.net.Socket;

public class ClientHandler extends Thread 
{
	int MAX_MESSAGE_LENGTH = 200;
	
	private Socket socket;
	private List<ClientHandler> clients;
	private DataOutputStream out;
	private DataInputStream in;
	private int clientNumber;
	private String username;
	
	public ClientHandler(Socket socket, List<ClientHandler> clients, int clientNumber)
	{
		this.username = null;
		this.socket = socket;
		this.clientNumber = clientNumber;
		this.clients = clients;
		System.out.println("New connection with client#" + clientNumber + " at " + socket);
	}
	
	private void onMessageReceived(String message) throws IOException, ParseException {
		if (username != null) {
			for (ClientHandler client : clients) {
				if (client != this) {
					client.sendMessage(username + " : " + message);
				}
			}
		}
		else {
			String[] args = message.split(" ");
			if (args.length != 2) return;
			if (!(args[0] instanceof String) || !(args[1] instanceof String)) return;
			String username = args[0];
			String password = CredentialsManager.getInstance().getPassword(username);
			if (password == null) {
				CredentialsManager.getInstance().addCredentials(username, args[1]);
			}
			if (password != args[1]) return;
			this.username = username;
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
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
			out.writeUTF("Hello from server - you are client #" + clientNumber);
			
			while (!socket.isClosed()) {
				if (in.available() > 0) {
					onMessageReceived(in.readUTF());
				}
			}
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