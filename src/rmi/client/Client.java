package rmi.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.SQLException;

import rmi.bean.User;
import rmi.controller.ProductController;
import rmi.controller.UserController;
import rmi.dao.IProductDAO;
import rmi.dao.IUserDAO;

public class Client {
	
	public static final String END_MARKER = "OVER";

	private BufferedReader netIn;
	private PrintWriter netOut;
	
	private User user;
	
	private UserController userController;
	private ProductController productController;
	
	private Registry reg;
	
	public Client() throws IOException, NotBoundException {
		netIn = new BufferedReader(new InputStreamReader(System.in));
		netOut = new PrintWriter(System.out, true);
		
		reg = LocateRegistry.getRegistry("127.0.0.1", 1090);
		
		userController = new UserController(netOut, (IUserDAO) reg.lookup("USERDAO"));
		productController = new ProductController(netOut, (IProductDAO) reg.lookup("PRODUCTDAO"));
	}
	
	public void run() throws NotBoundException {
		
		String welcome = "WELCOME TO MANAGE PRODUCT SYSTEM";
		netOut.println(welcome);
		
		boolean isLogin = false;
		
		try {
			while(true) {
				String input = netIn.readLine();

				if(input == null) {
					break;
				}
				else if("EXIT".equalsIgnoreCase(input)) {
					userController = new UserController(netOut, (IUserDAO) reg.lookup("USERDAO"));
					productController = new ProductController(netOut, (IProductDAO) reg.lookup("PRODUCTDAO"));
					user = null;
					isLogin = false;
					netOut.println(END_MARKER);
					continue;
				}
				else if("QUIT".equalsIgnoreCase(input)) {
					netIn.close();
					netOut.close();
					break;
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
			}
			netIn.close();
			netOut.close();
		}
		catch(IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Client client;
		try {
			client = new Client();
			client.run();
		} catch (IOException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}