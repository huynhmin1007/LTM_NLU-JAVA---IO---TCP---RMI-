package rmi.dao;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

import rmi.bean.User;

public interface IUserDAO extends Remote {

	boolean isValidUsername(String username) throws RemoteException, SQLException;
	User login(String username, String password) throws RemoteException, SQLException;
}
