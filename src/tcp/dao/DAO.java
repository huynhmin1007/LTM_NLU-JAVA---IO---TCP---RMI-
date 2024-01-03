package tcp.dao;

import java.sql.Connection;

import tcp.jdbc.JDBCConnector;

public abstract class DAO<T> {
	
	protected Connection conn;

	public DAO() {
		super();
		conn = JDBCConnector.me();
	}
}
