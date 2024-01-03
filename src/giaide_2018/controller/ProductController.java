package giaide_2018.controller;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import giaide_2018.bean.Product;
import giaide_2018.dao.ProductDAO;

public class ProductController {
	
	private PrintWriter netOut;
	private ProductDAO productDAO;
	
	public ProductController(PrintWriter netOut) {
		this.netOut = netOut;
		productDAO = new ProductDAO();
	}
	
	public boolean add(String[] params) throws SQLException {
		
		if(params.length != 4) return false;
		
		String id = params[0].trim();
		String name = params[1].trim();
		int quantity = Integer.parseInt(params[2]);
		double price = Double.parseDouble(params[3]);
		
		if(id.isEmpty() || name.isEmpty() || quantity < 0 || price < 0) return false;
		
		return productDAO.add(new Product(id, name, quantity, price));
	}
	
	public int remove(String[] params) throws SQLException {
		return productDAO.remove(params);
	}
	
	public boolean edit(String[] params) throws SQLException {
		if(params.length != 4) return false;
		
		String id = params[0].trim();
		String name = params[1].trim();
		int quantity = Integer.parseInt(params[2]);
		double price = Double.parseDouble(params[3]);
		
		return productDAO.update(new Product(id, name, quantity, price));
	}
	
	public String view(String productName) throws SQLException {
		StringBuilder str = new StringBuilder();
		
		List<Product> list = productDAO.findByName(productName);

		for(Product product : list) {
			str.append(product.toString());
			str.append("\r\n");
		}
		str.append("THE END");

		return str.toString();
	}
	
	public void run(String input) throws SQLException {
		StringTokenizer request = new StringTokenizer(input, "\t");
		String com = request.hasMoreTokens()? request.nextToken().toUpperCase() : "";
		String[] params = input.substring(com.length()).trim().split("\t");
		String respone;
		
		switch(com) {
		case "ADD":
			if(!add(params)) {
				respone = "ERROR";
			}
			else {
				respone = "OK";
			}
			break;
			
		case "REMOVE":
			respone = remove(params) + "";
			break;
			
		case "EDIT":
			if(!edit(params)) {
				respone = "CAN NOT UPDATE";
			}
			else {
				respone = "OK";
			}
			break;
			
		case "VIEW":
			if(params.length == 1) {
				respone = view(params[0]);
				break;
			}
			
			default:
				respone = "Error: invalid command";
		}
		netOut.println(respone);
	}
}