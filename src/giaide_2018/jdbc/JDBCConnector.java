package giaide_2018.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnector {
	
	private static final String DRIVER = "net.ucanaccess.jdbc.UcanaccessDriver";
	private static final String DB = "jdbc:ucanaccess://C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\ProductManagement.accdb";
	private static Connection conn;
	
	public static Connection getConnect() {
		if(conn == null) {
			try {
				connect();
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	public static void connect() throws ClassNotFoundException, SQLException {
		Class.forName(DRIVER);
		conn = DriverManager.getConnection(DB);
	}
}
