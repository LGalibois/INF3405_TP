import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;

public class ChatRoom {
	final int CHAT_HISTORY_CAPACITY = 15;
	final String LOGS_FILE_NAME_FORMAT = "%s-chat-logs";
	final String MESSAGE_FORMAT = "[%s - %s:%s - %s]: %s";
	final String DATE_FORMAT = "dd/MM/yyyy@HH:mm:ss";
	
	private List<ClientHandler> connectedClients;
	private String[] messageHistory;
	private SimpleDateFormat dateFormatter;
	public String name;
	
	
	public ChatRoom(String roomName) {
		this.messageHistory = new String[CHAT_HISTORY_CAPACITY];
		this.connectedClients = new ArrayList<ClientHandler>();
		this.dateFormatter = new SimpleDateFormat(DATE_FORMAT);
		this.name = roomName;
		this.initiateMessageHistory();
	}
	
	public void join(ClientHandler client) {
		connectedClients.add(client);
		for (String message: messageHistory)
			client.sendMessage(message);
	}
	
	private void initiateMessageHistory() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(String.format(LOGS_FILE_NAME_FORMAT, name)));
			while (reader.ready())
				updateMessageHistory(reader.readLine());
			reader.close();
		}
		catch (IOException e) {
			System.out.println("Could not read chat logs for room " + name);
		}
	}
	
	private void updateMessageHistory(String message) {
		int i = 0;
		for (; i < CHAT_HISTORY_CAPACITY - 1 && messageHistory[i] != null; i++)
			messageHistory[i] = messageHistory[i + 1];
		messageHistory[i] = message;
		
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(String.format(LOGS_FILE_NAME_FORMAT, name)));
			writer.append(message);
			writer.close();
		}
		catch (IOException e) {
			System.out.println("Could not write message in logs file");
		}
	}
	
	private String formatMessage(ClientHandler senderClient, String message) {
		return String.format(
				MESSAGE_FORMAT,
				senderClient.username,
				senderClient.getIp(),
				senderClient.getPort(),
				dateFormatter.format(new Date()),
				message);
	}
	
	public void sendMessage(ClientHandler senderClient, String message) {
		if (!connectedClients.contains(senderClient)) return;
		message = formatMessage(senderClient, message);
		for (ClientHandler client : connectedClients) {
			if (client != senderClient) {
				client.sendMessage(message);
			}
		}
		updateMessageHistory(message);
	}
}
