package giaide_2018.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

import giaide_2018.bean.User;
import giaide_2018.controller.ProductController;
import giaide_2018.controller.UserController;

public class ServerThread extends Thread {
	
	public static final String END_MARKER = "OVER";
	
	private Socket socket;
	private BufferedReader netIn;
	private PrintWriter netOut;
	
	private User user;
	
	private UserController userController;
	private ProductController productController;
	
	public ServerThread(Socket socket) throws IOException {
		this.socket = socket;
		netIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		netOut = new PrintWriter(socket.getOutputStream(), true);
		
		userController = new UserController(netOut);
		productController = new ProductController(netOut);
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		String welcome = "WELCOME TO MANAGE PRODUCT SYSTEM";
		netOut.println(welcome);
		netOut.println(END_MARKER);
		
		boolean isLogin = false;
		
		try {
			while(true) {
				String input = netIn.readLine();

				if(input == null) {
					break;
				}
				else if("EXIT".equalsIgnoreCase(input)) {
					userController = new UserController(netOut);
					productController = new ProductController(netOut);
					user = null;
					isLogin = false;
					netOut.println(END_MARKER);
					continue;
				}
				
				if(!isLogin) {
					user = userController.login(input);
					if(user != null) {
						isLogin = true;
					}
				}
				else {
					productController.run(input);
				}
				netOut.println(END_MARKER);
			}
			netIn.close();
			netOut.close();
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
