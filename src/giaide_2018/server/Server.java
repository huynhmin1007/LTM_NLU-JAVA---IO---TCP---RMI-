package giaide_2018.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(6969);
			while(true) {
				Socket socket = serverSocket.accept();
				ServerThread thread = new ServerThread(socket);
				thread.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
