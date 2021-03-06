import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.net.Socket;

public class ClientHandler extends Thread 
{
	final int MAX_MESSAGE_LENGTH = 200;
	final String REGISTRATION_GRANTED_MESSAGE = "registration granted";
	final String REGISTRATION_DENIED_MESSAGE = "registration denied";
	final String CLOSING_MESSAGE = "exit";
	
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
	
	/**
	 * La fonction retourne l'ip du socket
	 * @return l'ip du socket
	 */
	public String getIp() {
		return socket.getInetAddress().toString();
	}
	
	/**
	 * La fonction retourne le port utilis? par le socket
	 * @return le port du socket
	 */
	public String getPort() {
		return Integer.toString(socket.getPort());
	}
	
	/**
	 * La fonction retourne si le client a ?t? enregistr?
	 * @return si le client est enregistr?
	 */
	public Boolean isRegistered() {
		return username != null;
	}
	
	/**
	 * La fonction qui envoie le message ? la salle de clavardage
	 * @param message: le message ? envoyer
	 * @return si le message est le message pour ferm? la connection
	 */
	private boolean onMessageReceived(String message) {
		if (message.equals(CLOSING_MESSAGE)) return true;
		if (isRegistered()) {
			chatRoom.sendMessage(this, message);
		}
		else
			Register(message);
		
		return false;
	}
	
	/**
	 * La fonction enregistre un client avec un mot de passe et un nom d'utilisateur
	 * @param message: le message qui contient le nom et le mot de passe de l'utilisateur
	 */
	private void Register(String message) {
		try {
			String[] args = message.split(" ");
			System.out.println("Registering " + args[0] + " : " + args[1]);
			String username = args[0];
			String password = CredentialsManager.getInstance().getPassword(username);
			if (password == null) {
				password = args[1];
				CredentialsManager.getInstance().addCredentials(username, password);
			}
			System.out.println("password is " + password);
			if (!password.equals(args[1])) {
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
	
	/**
	 * La fonction envoie un message au client
	 * @param message: le message ? envoyer
	 */
	public void sendMessage(String message) {
		if (out != null && message != null) {
			try {
				out.writeUTF(message);
			}
			catch (IOException e) {
				System.out.println("Could not send message");
			}
		}
	}
	
	/**
	 * La fonction d?mare le thread
	 */
	public void run()
	{	
		try
		{
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
			
			boolean isClosed = false;
			while (!isClosed) {
				if (in.available() > 0) {
					isClosed = onMessageReceived(in.readUTF());
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
				chatRoom.leave(this);
				socket.close();
			}
			catch (IOException e)
			{
				System.out.println("Could not close a socket, what's going on?");
			}
		}
	}
}