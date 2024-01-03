package tcp.controller;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.StringTokenizer;

import tcp.bean.User;
import tcp.dao.UserDAO;

public class UserController {
	
	private PrintWriter netOut;
	
	private UserDAO userDAO;
	
	private String lastUsername = null;
	
	public UserController(PrintWriter netOut) {
		this.netOut = netOut;
		userDAO = new UserDAO();
	}
	
	public boolean login(StringTokenizer request) throws SQLException {
		String com = request.hasMoreTokens()? request.nextToken().toUpperCase() : null;
		String param = request.hasMoreTokens()? request.nextToken() : null;
		String respone;
		
		if(request.hasMoreTokens()) {
			netOut.println("Error: Invalid command");
			return false;
		}
		
		switch(com) {
		case "USER":
			if(userDAO.isValidUsername(param)) {
				respone = "OK!";
				lastUsername = param;
			}
			else {
				respone = "Error: invalid username";
			}
			break;
		
		case "PASS":
			if(lastUsername == null) {
				respone = "Error: username first";
			}
			else {
				User user = userDAO.login(lastUsername, param);
				if(user == null) {
					respone = "Error: invalid password";
				}
				else {
					respone = "Login successful!";
					netOut.println(respone);
					return true;
				}
			}
			break;
			
			default:
				respone = "Error: invalid command";
				break;
		}
		netOut.println(respone);
		return false;
	}
}
