package giaide_2018.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import giaide_2018.bean.User;

public class UserDAO extends DAO<User> {
	
	public boolean isValidUsername(String username) throws SQLException {
		String sql = "SELECT * FROM users WHERE username = ?";
		
		PreparedStatement stat = conn.prepareStatement(sql);
		stat.setString(1, username);
		
		return stat.executeQuery().next();
	}
	
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
