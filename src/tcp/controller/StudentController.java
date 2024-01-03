package tcp.controller;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import tcp.bean.Student;
import tcp.dao.StudentDAO;

public class StudentController {
	
	private StudentDAO studentDAO;
	private PrintWriter netOut;

	public StudentController(PrintWriter netOut) {
		super();
		this.netOut = netOut;
		studentDAO = new StudentDAO();
	}
	
	public void run(String line) throws NumberFormatException, SQLException {
		StringTokenizer request = new StringTokenizer(line);
		String com = request.hasMoreTokens()? request.nextToken().toUpperCase() : null;
		String param = line.substring(com.length()).trim();
		String respone;
		
		Student student;
		
		switch(com) {
		case "FINDBYID":
			student = studentDAO.findById(Integer.parseInt(param));
			if(student == null) {
				respone = "Student not found";
			}
			else {
				respone = student.toString();
			}
			break;
			
		case "FINDBYNAME":
			List<Student> list = studentDAO.findByName(param);
			
			if(list.size() != 0 && list != null) {
				StringBuilder str = new StringBuilder();
				for(Student st : list) {
					str.append(st.toString());
					str.append("\r\n");
				}
				respone = str.toString();
			}
			else {
				respone = "Student not found";
			}
			break;
			
			default:
				respone = "Error: invalid command";
		}
		netOut.println(respone);
	}
}
