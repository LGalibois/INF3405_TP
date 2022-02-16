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
	private DataOutputStream out;
	private DataInputStream in;
	private ChatRoom chatRoom;
	public String username;
	
	public ClientHandler(Socket socket, ChatRoom room)
	{
		this.username = null;
		this.socket = socket;
		this.chatRoom = room;
		System.out.println("New connection with client" + this.getIp());
	}
	
	public String getIp() {
		return socket.getInetAddress().toString();
	}
	
	public String getPort() {
		return Integer.toString(socket.getPort());
	}
	
	private void onMessageReceived(String message) {
		if (username != null) {
			chatRoom.sendMessage(this, message);
		}
		else {
			try {
				String[] args = message.split(" ");
				if (args.length != 2) return;
				if (!(args[0] instanceof String) || !(args[1] instanceof String)) return;
				String username = args[0];
				String password = CredentialsManager.getInstance().getPassword(username);
				if (password == null) {
					CredentialsManager.getInstance().addCredentials(username, args[1]);
				}
				if (password != args[1]) {
					sendMessage("denied");
				}
				sendMessage("granted");
				this.username = username;
				chatRoom.join(this);
			}
			catch (Exception e) {
				System.out.println("Error treating message for " + (username != null ? username : "unregistered client"));
			}
		}
	}
	
	public void sendMessage(String message) {
		if (out != null) {
			try {
				out.writeUTF(new String(message));
			}
			catch (IOException e) {
				System.out.println("Could not send message");
			}
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
		} catch (ParseException e) {
			System.out.println("Error parsing");
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