package giaide_2018.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import giaide_2018.server.ServerThread;

public class Client {
	
	private Socket socket;
	private BufferedReader netIn;
	private PrintWriter netOut;
	private BufferedReader userIn;
	
	public Client(String ip, int port) throws UnknownHostException, IOException {
		this.socket = new Socket(ip, port);
		netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		netOut = new PrintWriter(socket.getOutputStream(), true);
		userIn = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void run() {
		try {
			while(true) {
				String respone;
				while((respone = netIn.readLine()) != null) {
					if(respone.equalsIgnoreCase(ServerThread.END_MARKER)) break;
					System.out.println(respone);
				}
				
				String line = userIn.readLine();
				
				if("QUIT".equalsIgnoreCase(line)) {
					closeConnection();
					break;
				}
				netOut.println(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeConnection() throws IOException {
		netIn.close();
		netOut.close();
		userIn.close();
		socket.close();
	}
	
	public static void main(String[] args) {
		try {
			Client client = new Client("127.0.0.1", 6969);
			client.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
