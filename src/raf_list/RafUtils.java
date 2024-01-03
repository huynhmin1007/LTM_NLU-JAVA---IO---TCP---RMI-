package raf_list;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RafUtils {
	
	private RandomAccessFile raf;
	private int count;
	private long lastCursor;
	
	private final int STR_LENGTH = 25;
	private final int OBJ_LENGTH = 12 + STR_LENGTH * 2;
 	
	public RafUtils(String file) throws IOException {
		this.raf = new RandomAccessFile(file, "rw");
		if(raf.length() > 0) {
			count = raf.readInt();
			lastCursor = raf.length();
		}
		else {
			raf.writeInt(count);
			lastCursor = raf.getFilePointer();
		}
	}
	
	public String fixStr(String s) {
		StringBuilder builder = new StringBuilder(s);
		for(int i = s.length(); i < STR_LENGTH; i++) {
			builder.append((char)0);
		}
		return builder.toString();
	}
	
	public void add(Student student) throws IOException {
		raf.seek(0);
		raf.writeInt(++count);
		
		raf.seek(lastCursor);
		raf.writeInt(student.getId());
		raf.writeChars(fixStr(student.getName()));
		raf.writeDouble(student.getGrade());
		
		lastCursor = raf.length();
	}
	
	public String getStr(long cursor) throws IOException {
		raf.seek(cursor);
		StringBuilder str = new StringBuilder();
		
		for(int i = 0; i < STR_LENGTH; i++) {
			char c = raf.readChar();
			if(c == 0) break;
			str.append(c);
		}
		return str.toString();
	}
	
	public Student getByCursor(long cursor) throws IOException {
		raf.seek(cursor);
		
		Student student = new Student();
		student.setId(raf.readInt());
		student.setName(getStr(raf.getFilePointer()));
		raf.seek(cursor + 4 + STR_LENGTH * 2);
		student.setGrade(raf.readDouble());
		
		return student;
	}
	
	public Student get(int index) throws IOException {
		return getByCursor(index * OBJ_LENGTH + 4);
	}
	
	public void close() throws IOException {
		raf.close();
	}
	
	public static void main(String[] args) {
		String path = "C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\root\\student.txt";
		
		try {
			RafUtils raf = new RafUtils(path);
			raf.add(new Student(1, "Huỳnh Minh", 10.0));
			raf.add(new Student(2, "Lê Mai", 10.0));
			raf.add(new Student(3, "Nguyễn Huỳnh Quang", 9.5));
			
			System.out.println(raf.get(2));
			
			raf.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
