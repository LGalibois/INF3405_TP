import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Date;
import java.util.Iterator;
import java.text.SimpleDateFormat;
import java.io.File;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;

public class ChatRoom {
	final int MAX_MESSAGE_LENGTH = 200;
	final int CHAT_HISTORY_CAPACITY = 15;
	final String LOGS_FILE_NAME_FORMAT = "%s-chat-logs";
	final String MESSAGE_FORMAT = "[%s - %s:%s - %s]: %s";
	final String DATE_FORMAT = "dd/MM/yyyy@HH:mm:ss";
	
	private List<ClientHandler> connectedClients;
	private LinkedList<String> messageHistory;
	private SimpleDateFormat dateFormatter;
	public String name;
	
	
	public ChatRoom(String roomName) {
		this.messageHistory = new LinkedList<String>();
		this.connectedClients = new ArrayList<ClientHandler>();
		this.dateFormatter = new SimpleDateFormat(DATE_FORMAT);
		this.name = roomName;
		this.initiateMessageHistory();
	}
	
	/**
	 * La fonction ajoute un thread clientHandler dans la salle de clavardage
	 * @param client: le client à ajouter à la salle de clavardage
	 */
	public void join(ClientHandler client) {
		connectedClients.add(client);
		client.sendMessage(String.format("[Server]: Welcome to chat room %s!", name));
		
		Iterator<String> message = messageHistory.descendingIterator();
		while(message.hasNext()) {
			client.sendMessage(message.next());
		}

	}
	
	/**
	 * La fonction ouvre ou crée le fichier pour sauvegarder les messages
	 */
	private void initiateMessageHistory() {
		try {
			File file = new File(String.format(LOGS_FILE_NAME_FORMAT, name));
			file.createNewFile();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while (reader.ready())
				updateMessageHistory(reader.readLine());
			reader.close();
		}
		catch (IOException e) {
			System.out.println("Could not read chat logs for room " + name);
		}
	}
	
	/**
	 * La fonction ajoute un message dans le messageHistory
	 * @param message: le message à ajouter
	 */
	private void updateMessageHistory(String message) {
		messageHistory.addFirst(message);
		if (messageHistory.size() > CHAT_HISTORY_CAPACITY)
			messageHistory.removeLast();
	}
	
	/**
	 * La fonction ajoute un message dans le fichier log
	 * @param message: le message à ajouter
	 */
	private void updateMessageHistoryLog(String message) {	
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(String.format(LOGS_FILE_NAME_FORMAT, name), true));
			writer.println(message);
			writer.close();
		}
		catch (IOException e) {
			System.out.println("Could not write message in logs file");
		}
	}
	
	/**
	 * La fonction ajoute le nom, l'ip, le port et la date au message
	 * @param senderClient: le client qui a envoyer le message
	 * @param message: le message envoyé
	 * @return le message formatté
	 */
	private String formatMessage(ClientHandler senderClient, String message) {
		return String.format(
				MESSAGE_FORMAT,
				senderClient.username,
				senderClient.getIp(),
				senderClient.getPort(),
				dateFormatter.format(new Date()),
				message);
	}
	
	/**
	 * La fonction enlève un client de la salle de clavardage
	 * @param client: le client à retirer
	 */
	public void leave(ClientHandler client) {
		connectedClients.remove(client);
	}
	
	/**
	 * La fonction envoie un message à tous les clients dans la salle de clavardage
	 * @param senderClient: le client qui a envoyer le message
	 * @param message: le message à envoyer
	 */
	public void sendMessage(ClientHandler senderClient, String message) {
		if (message == null) return;
		if (message.length() > MAX_MESSAGE_LENGTH) return;
		if (!connectedClients.contains(senderClient)) return;
		
		message = formatMessage(senderClient, message);
		System.out.println(message);
		
		for (ClientHandler client : connectedClients) {
			if (client != senderClient) {
				client.sendMessage(message);
			}
		}
		updateMessageHistory(message);
		updateMessageHistoryLog(message);
	}
}
