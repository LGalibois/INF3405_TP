import java.util.InputMismatchException;
import java.util.Scanner;

public  class InputValidator {
	static final int NUMBER_OF_SEGMENT = 4;
	static final int MINIMUM_PORT = 5000;
	static final int MAXIMUM_PORT = 5050;
	static final int MAXIMUM_MESSAGE_SIZE = 200;
	
	/**
	 * Cette fonction valide si le message est plus petit que la constante MAXIMUM_MESSAGE_SIZE
	 * @param message : le message a validee
	 * @return retourne vrai si le message est plus petit que la constante
	 */
	public static boolean isMessageValid(String message) {	
		return message.length() <= MAXIMUM_MESSAGE_SIZE;
	}
	
	/**
	 * Le fonction valide que l'adresse ip passer est bien des chiffres entre 0 et 128
	 * @param serverAddress : l'adresse ip a validee
	 * @return retourne vrai si l'adresse est validee
	 */
	private static boolean validateServerAddress(String serverAddress)
	{
		boolean result = false;
		
		String[] serverAddressSegment = serverAddress.split("\\.");
		if(serverAddressSegment.length == NUMBER_OF_SEGMENT) {
			try 
			{	
				result = true;
				for (int i = 0; i < NUMBER_OF_SEGMENT; i++)
				{
					int segment = Integer.parseInt(serverAddressSegment[i]);
					result = result && segment >= 0 && segment <= 256;
				}
			}
			catch(NumberFormatException exception)
			{
				System.out.println("Les segment de l'adresse ip doivent etre des chiffres");
			}
			
		}
		
		return result;
	}
	
	/**
	 * La fonction retourne un port valide
	 * @param consoleReader
	 * @return
	 */
	public static String getValidAddress(Scanner consoleReader) 
	{
		String serverAddress;
		
		do 
		{
			System.out.println("Entrez l'addresse du serveur :");
			serverAddress = consoleReader.nextLine();
		}while(!validateServerAddress(serverAddress));
		
		return serverAddress;
	}
	
	public static int getValidPort(Scanner consoleReader)
	{
		boolean isValid = false;
		int serverPort = 5000;
		
		do
		{
			try
			{	
				System.out.println("Entrez le port du serveur :");
				serverPort = consoleReader.nextInt();
				
				isValid = serverPort >= MINIMUM_PORT && serverPort <= MAXIMUM_PORT;
			}
			catch(InputMismatchException error)
			{
				System.out.println("Le port doit etre un nombre");
			}
		}while(!isValid);
		
		return serverPort;
	}
	
}
