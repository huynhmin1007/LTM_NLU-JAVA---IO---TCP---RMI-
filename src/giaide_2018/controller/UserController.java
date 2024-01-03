package giaide_2018.controller;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.StringTokenizer;

import giaide_2018.bean.User;
import giaide_2018.dao.UserDAO;


public class UserController {
	
	private UserDAO userDAO;
	private PrintWriter netOut;
	
	private String lastUsername;
	
	public UserController(PrintWriter netOut) {
		this.netOut = netOut;
		this.userDAO = new UserDAO();
	}
	
	public User login(String input) throws SQLException {
		StringTokenizer request = new StringTokenizer(input, "\t");
		String com = request.hasMoreTokens()? request.nextToken().toUpperCase() : "";
		String param = request.hasMoreTokens()? request.nextToken() : "";
		String respone;
		
		switch(com) {
		case "USER":
			if(userDAO.isValidUsername(param)) {
				respone = "OK";
				lastUsername = param;
			}
			else {
				respone = "FALSE";
			}
			break;
			
		case "PASS": 
			if(lastUsername == null) {
				respone = "FALSE";
			}
			else {
				User user = userDAO.login(lastUsername, param);
				
				if(user == null) {
					respone = "FALSE";
				}
				else {
					respone = "OK";
					netOut.println(respone);
					return user;
				}
			}
			break;
			
			default:
				respone = "Error: invalid command";
		}
		netOut.println(respone);
		return null;
	}
}
