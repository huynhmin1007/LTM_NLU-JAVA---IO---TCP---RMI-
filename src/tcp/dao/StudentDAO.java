package tcp.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import tcp.bean.Student;

public class StudentDAO extends DAO {
	
	private Student get(ResultSet res) throws SQLException {
		int sId = res.getInt("ID");
		String name = res.getString("name");
		double grade = res.getDouble("grade");
		
		return new Student(sId, name, grade);
	}
	
	public Student findById(int id) throws SQLException {
		String sql = "SELECT * FROM students WHERE id = ?";
		
		PreparedStatement stat = conn.prepareStatement(sql);
		stat.setInt(1, id);
		
		ResultSet res = stat.executeQuery();
		
		if(res.next()) {
			return get(res);
		}
		return null;
	}
	
	public List<Student> findByName(String name) throws SQLException {
		String sql = "SELECT * FROM students WHERE name like ?";
		
		PreparedStatement stat = conn.prepareStatement(sql);
		stat.setString(1, "%" + name);
		
		ResultSet res = stat.executeQuery();
		List<Student> list = new ArrayList<Student>();
		
		while(res.next()) {
			list.add(get(res));
		}
		return list;
	}
}
