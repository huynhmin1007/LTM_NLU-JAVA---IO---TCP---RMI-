package giaide_2018.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import giaide_2018.jdbc.JDBCConnector;

public abstract class DAO<T> {
	
	protected Connection conn;
	
	public DAO() {
		this.conn = JDBCConnector.getConnect();
	}
	
	public abstract T get(ResultSet res) throws SQLException;
}
