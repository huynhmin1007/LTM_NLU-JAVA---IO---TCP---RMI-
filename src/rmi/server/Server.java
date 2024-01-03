package rmi.server;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import rmi.dao.ProductDAO;
import rmi.dao.UserDAO;

public class Server {
	
	public static void main(String[] args) {
		try {
			Registry reg = LocateRegistry.createRegistry(1090);
			
			UserDAO userDAO = new UserDAO();
			ProductDAO productDAO = new ProductDAO();
			
			reg.rebind("USERDAO", userDAO);
			reg.rebind("PRODUCTDAO", productDAO);
			
			System.out.println("Server is running...");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
