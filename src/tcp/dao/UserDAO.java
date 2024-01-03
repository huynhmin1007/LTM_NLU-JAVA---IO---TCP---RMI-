package tcp.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tcp.bean.User;

public class UserDAO extends DAO {
	
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
		
		if(res.next()) {
			int uId = res.getInt("ID");
			String uUsername = res.getString("username");
			String uPassword = res.getString("password");
			
			return new User(uId, uUsername, uPassword);
		}
		return null;
	}
}
