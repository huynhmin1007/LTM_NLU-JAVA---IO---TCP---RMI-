package tcp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.StringTokenizer;

import tcp.controller.StudentController;
import tcp.controller.UserController;

public class ServerThread extends Thread {
	
	private Socket socket;
	private BufferedReader netIn;
	private PrintWriter netOut;
	
	private UserController userController;
	private StudentController studentController;
	
	public ServerThread(Socket socket) throws IOException {
		super();
		this.socket = socket;
		netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		netOut = new PrintWriter(socket.getOutputStream(), true);
		
		userController = new UserController(netOut);
		studentController = new StudentController(netOut);
		
		netOut.println("Welcome!");
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		try {
			boolean isLogin = false;
			while(true) {
				String line = netIn.readLine();
				
				if(line == null || "EXIT".equalsIgnoreCase(line)) {
					netOut.println("Goodbye...");
					break;
				}
				
				StringTokenizer request = new StringTokenizer(line);
				
				if(!isLogin) {
					if(userController.login(request)) {						
						isLogin = true;
					}
				}
				else {
					studentController.run(line);
				}
			}
			
			socket.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
