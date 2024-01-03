package net_copy;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {
	
	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(2000);
			while(true) {
				new ServerThread(serverSocket.accept()).start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
