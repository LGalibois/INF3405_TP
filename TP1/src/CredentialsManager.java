import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileReader;

public class CredentialsManager {
	static private CredentialsManager instance = null;
	static private String JSON_PATH = "data.json";
	
	private JSONArray credentialsArray;
	
	
	private CredentialsManager() throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		File file = new File(JSON_PATH);
		if (!file.exists()) {
			file.createNewFile();
			this.credentialsArray = new JSONArray();
			this.saveCredentials();
		}
		else {
			Object obj = parser.parse(new FileReader(file));
			this.credentialsArray = (JSONArray) obj;
		}
		
	}
	
	/**
	 * La fonction retourne le singleton pour le CredentialsManager
	 * @return le CredentialsManager
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	static public CredentialsManager getInstance() throws FileNotFoundException, IOException, ParseException {
		if (instance == null) {
			instance = new CredentialsManager();
		}
		return instance;
	}
	
	/**
	 * La fonction sauvegarde les nom et les mots de passe des utilisateurs
	 * @throws IOException
	 */
	private void saveCredentials() throws IOException {
		FileWriter writer = new FileWriter(JSON_PATH);
		writer.write(credentialsArray.toJSONString());
		writer.close();
	}
	
	/**
	 * La fonction ajoute un mot de passe et un nom d'utilisateur
	 * @param username: le nom d'utilisateur
	 * @param password: le mot de passe d'utilisateur
	 */
	public void addCredentials(String username, String password) {
		JSONObject credentials = new JSONObject();
		credentials.put("username", username);
		credentials.put("password", password);
		this.credentialsArray.add(credentials);
		try {
			saveCredentials();
		}
		catch(IOException e) {
			System.out.println("Error writing credentials for " + username);
		}
		
	}
	
	/**
	 * La fonction retourne un mot de passe pour un nom d'utilisateur
	 * @param username: le nom de l'utilisateur
	 * @return le mot de passe de l'utilisateur
	 */
	public String getPassword(String username) {
		for(Object obj: this.credentialsArray) {
			JSONObject credentials = (JSONObject) obj;
			if (credentials.get("username").equals(username)) {
				return credentials.get("password").toString();
			}
		}
		return null;
	}
}
