package giaide_2021.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(2000);
			while(true) {
				Socket socket = serverSocket.accept();
				new ServerThread(socket).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
