package rmi.controller;

import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.StringTokenizer;

import rmi.bean.User;
import rmi.dao.IUserDAO;

public class UserController {
	
	private IUserDAO userDAO;
	private PrintWriter netOut;
	
	private String lastUsername;
	
	public UserController(PrintWriter netOut, IUserDAO userDAO) {
		this.netOut = netOut;
		this.userDAO = userDAO;
	}
	
	public User login(String input) throws SQLException, RemoteException {
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
