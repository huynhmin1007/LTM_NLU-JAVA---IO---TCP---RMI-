package rmi.dao;

import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import rmi.bean.User;


public class UserDAO extends DAO<User> implements IUserDAO {
	
	public UserDAO() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean isValidUsername(String username) throws SQLException {
		String sql = "SELECT * FROM users WHERE username = ?";
		
		PreparedStatement stat = conn.prepareStatement(sql);
		stat.setString(1, username);
		
		return stat.executeQuery().next();
	}
	
	@Override
	public User login(String username, String password) throws SQLException {
		String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
		
		PreparedStatement stat = conn.prepareStatement(sql);
		stat.setString(1, username);
		stat.setString(2, password);
		
		ResultSet res = stat.executeQuery();
		
		return res.next()? get(res) : null;
	}

	@Override
	public User get(ResultSet res) throws SQLException {
		String username = res.getString("username");
		String password = res.getString("password");
		
		return new User(username, password);
	}
}
