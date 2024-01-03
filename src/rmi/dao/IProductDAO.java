package rmi.dao;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

import rmi.bean.Product;

public interface IProductDAO extends Remote {
	
	boolean add(Product product) throws RemoteException, SQLException;
	int remove(String[] params) throws RemoteException, SQLException;
	boolean update(Product product) throws RemoteException, SQLException;
	List<Product> findByName(String name) throws RemoteException, SQLException;
}
