package rmi.dao;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import rmi.jdbc.JDBCConnector;

public abstract class DAO<T> extends UnicastRemoteObject {
	
	protected Connection conn;
	
	public DAO() throws RemoteException {
		this.conn = JDBCConnector.getConnect();
	}
	
	public abstract T get(ResultSet res) throws SQLException;
}
