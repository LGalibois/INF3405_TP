import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class MessageReceiver extends Thread
{
	private Socket socket;
	
	public MessageReceiver(Socket socket)
	{
		this.socket = socket;
	}
	
	public void run()
	{
		try
		{
			DataInputStream input = new DataInputStream(socket.getInputStream());
			
			while(true)
			{
				String message = input.readUTF();
				System.out.println(message);
			}
		}
		catch (IOException e)
		{
			
		}
	}
}
