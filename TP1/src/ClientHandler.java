import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;

public class ClientHandler extends Thread 
{
	final int MAX_MESSAGE_LENGTH = 200;
	final String REGISTRATION_GRANTED_MESSAGE = "registration granted";
	final String REGISTRATION_DENIED_MESSAGE = "registration denied";
	
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
	
	public Boolean isRegistered() {
		return username != null;
	}
	
	private void onMessageReceived(String message) {
		if (isRegistered())
			chatRoom.sendMessage(this, message);
		else
			Register(message);
	}
	
	private void Register(String message) {
		try {
			String[] args = message.split(" ");
			System.out.println("Registering " + args[0] + " : " + args[1]);
			String username = args[0];
			String password = CredentialsManager.getInstance().getPassword(username);
			System.out.println("password is: " + password);
			if (password == null) {
				CredentialsManager.getInstance().addCredentials(username, args[1]);
			}
			if (password != args[1]) {
				sendMessage(REGISTRATION_DENIED_MESSAGE);
				return;
			}
			sendMessage(REGISTRATION_GRANTED_MESSAGE);
			this.username = username;
			chatRoom.join(this);
		}
		catch (Exception e) {
			System.out.println("Error while trying to register client: " + e.getMessage());
		}
	}
	
	public void sendMessage(String message) {
		if (out != null) {
			try {
				out.writeUTF(message);
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
			
			while (!socket.isClosed()) {
				if (in.available() > 0) {
					onMessageReceived(in.readUTF());
				}
			}
		}
		catch (Exception e)
		{
			System.out.println("Error with ClientHandler");
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
		}
	}
}