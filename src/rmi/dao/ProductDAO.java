package rmi.dao;

import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import rmi.bean.Product;

public class ProductDAO extends DAO<Product> implements IProductDAO {
	
	public ProductDAO() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public Product get(ResultSet res) throws SQLException {
		String id = res.getString(Column.IDSP.name());
		String name = res.getString(Column.TEN_SAN_PHAM.name());
		int quantity = res.getInt(Column.SO_LUONG.name());
		double price = res.getDouble(Column.GIA_BAN.name());
		
		return new Product(id, name, quantity, price);
	}
	
	public Product get(String id) throws SQLException {
		String sql = String.format("SELECT * FROM products WHERE %s = ?", Column.IDSP);
		
		PreparedStatement stat = conn.prepareStatement(sql);
		stat.setString(1, id);
		
		ResultSet res = stat.executeQuery();
		
		return res.next()? get(res) : null;
	}
	
	@Override
	public boolean add(Product product) throws SQLException {
		if(get(product.getId()) != null) return false;
		
		String sql = String.format("INSERT INTO products (%s, %s, %s, %s) VALUES (?, ?, ?, ?)",
				Column.IDSP,
				Column.TEN_SAN_PHAM,
				Column.SO_LUONG,
				Column.GIA_BAN);
		
		PreparedStatement stat = conn.prepareStatement(sql);
		stat.setString(1, product.getId());
		stat.setString(2, product.getName());
		stat.setInt(3, product.getQuantity());
		stat.setDouble(4, product.getPrice());
		
		return stat.executeUpdate() == 1;
	}
	
	@Override
	public int remove(String[] params) throws SQLException {
		StringBuilder sql = new StringBuilder(String.format("DELETE FROM products WHERE %s IN (?", Column.IDSP));
		
		for(int i = 1; i < params.length; i++) {
			sql.append(", ?");
		}
		sql.append(")");
		
		PreparedStatement stat = conn.prepareStatement(sql.toString());
		
		for(int i = 0; i < params.length; i++) {
			stat.setString(i + 1, params[i]);
		}
		
		return stat.executeUpdate();
	}
	
	@Override
	public boolean update(Product product) throws SQLException {
		String sql = String.format("UPDATE products SET %s = ?, %s = ?, %s = ? WHERE %s = ?", 
				Column.TEN_SAN_PHAM,
				Column.SO_LUONG,
				Column.GIA_BAN,
				Column.IDSP);
		
		PreparedStatement stat = conn.prepareStatement(sql);
		stat.setString(1, product.getName());
		stat.setInt(2, product.getQuantity());
		stat.setDouble(3, product.getPrice());
		stat.setString(4, product.getId());
		
		return stat.executeUpdate() == 1;
	}
	
	@Override
	public List<Product> findByName(String name) throws SQLException {
		String sql = String.format("SELECT * FROM products WHERE %s like ?", Column.TEN_SAN_PHAM);
		
		PreparedStatement stat = conn.prepareStatement(sql);
		stat.setString(1, "%" + name + "%");
		
		ResultSet res = stat.executeQuery();
		List<Product> list = new ArrayList<Product>();
		
		while(res.next()) {
			list.add(get(res));
		}
		
		return list;
	}
	
	public enum Column {
		IDSP, TEN_SAN_PHAM, SO_LUONG, GIA_BAN
	}
}