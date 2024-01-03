package giaide_2021.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Date;

import giaide_2021.model.Candidate;

public class RafCandidate {
	
	private RandomAccessFile raf;
	
	private int count;
	private long lastCursor;
	
	private final int LENGTH_STR = 25;
	private final int LENGTH_IMG = 100 * 1024;
	private final long LENGTH_CANDIDATE = LENGTH_STR * 4 + LENGTH_IMG + 12;
	
	public RafCandidate(File file) throws IOException {
		super();
		raf = new RandomAccessFile(file, "rw");
		
		if(raf.length() <= 0) {
			raf.writeInt(count);
			lastCursor = raf.getFilePointer();
		}
		else {
			count = raf.readInt();
			lastCursor = raf.length();
		}
	}
	
	public String fixStr(String s) {
		StringBuilder strFixed = new StringBuilder(s);
		
		for(int i = s.length(); i < LENGTH_STR; i++) {
			strFixed.append((char) 0);
		}
		return strFixed.toString();
	}
	
	public String readString() throws IOException {
		StringBuilder str = new StringBuilder();
		long cursor = raf.getFilePointer();
		
		for(int i = 0; i < LENGTH_STR; i++) {
			char c = raf.readChar();
			
			if(c == 0) {
				raf.seek(cursor + LENGTH_STR * 2);
				break;
			}
			str.append(c);
		}
		
		return str.toString();
	}
	
	public void writeImg(InputStream is, long size) throws IOException {
		byte[] data = new byte[8 * 1024];
		boolean lastData = false;
		long addSize = LENGTH_IMG - size;
		
		while(size > 0) {
			int byteRead = (int) Math.min(data.length, size);
			if(is.read(data, 0, byteRead) != -1) {
				lastData = data[data.length - 1] == 0;
				raf.write(data, 0, byteRead);
			}
			size -= byteRead;
		}
		
		while(addSize > 0) {
			int byteRead = (int) Math.min(data.length, addSize);
			byte[] byteAdd = new byte[byteRead];
			
			if(lastData) {
				Arrays.fill(byteAdd, (byte) 1);
			}
			raf.write(byteAdd);
			
			addSize -= byteRead;
		}
	}
	
	public byte[] readImg() throws IOException {
		byte[] data = new byte[100 * 1024];
		raf.read(data);
		
		for(int i = data.length - 1; i > 0; i--) {
			if(data[i] != data[i - 1]) {
				return Arrays.copyOfRange(data, 0, i);
			}
		}
		return data;
	}
	
	public boolean update(int id, String address) throws IOException {
		long cursor = getCursor(id);
		
		if(get(cursor) == null) {
			return false;
		}
		
		raf.seek(cursor + LENGTH_STR * 2 + 12);
		raf.writeChars(fixStr(address));
		
		return true;
	}
	
	public void register(Candidate candidate, File img) throws IOException {
		
		lastCursor = getCursor(candidate.getId());
		
		while(get(lastCursor) != null) {
			candidate.setId(getCount() + 1);
			lastCursor = getCursor(candidate.getId());
		}
		
		raf.seek(lastCursor);
		raf.writeInt(candidate.getId());
		raf.writeChars(fixStr(candidate.getFullName()));
		raf.writeLong(candidate.getBirthDate().getTime());
		raf.writeChars(fixStr(candidate.getAddress()));
		
		long cursor = raf.getFilePointer();
		InputStream is = new BufferedInputStream(new FileInputStream(img));
		writeImg(is, img.length());
		is.close();
		
		raf.seek(cursor);
		candidate.setImg(readImg());
		lastCursor = raf.length();
		
		count = candidate.getId();
		raf.seek(0);
		raf.writeInt(count);
	}
	
	public int getCount() {
		try {
			raf.seek(0);
			count = raf.readInt();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.count;
	}
	
	public long getCursor(int id) {
		return (long)--id * LENGTH_CANDIDATE + 4;
	}
	
	public Candidate get(long cursor) {
		int id;
		String fullName;
		Date birthDate;
		String address;
		byte[] img;
		
		try {
			raf.seek(cursor);
			id = raf.readInt();
			fullName = readString();
			birthDate = new Date(raf.readLong());
			address = readString();
			img = readImg();
		}
		catch (IOException e) {
			return null;
		}

		return new Candidate(id, fullName, birthDate, address, img);
	}
	
	public Candidate getCandidate(int id) {
		return get(getCursor(id));
	}
	
	public void close() throws IOException {
		raf.close();
	}
}
