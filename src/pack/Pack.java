package pack;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;

public class Pack {
	
	public static void copy(InputStream is, RandomAccessFile os, long size) throws IOException {
		byte[] data = new byte[8 * 1024];
		while(size > 0) {
			int byteRead = (int) Math.min(data.length, size);
			if(is.read(data, 0, byteRead) != -1) {
				os.write(data, 0, byteRead);
			}
			size -= byteRead;
		}
	}
	
	public static void copy(RandomAccessFile is, OutputStream os, long size) throws IOException {
		byte[] data = new byte[8 * 1024];
		while(size > 0) {
			int byteRead = (int) Math.min(data.length, size);
			if(is.read(data, 0, byteRead) != -1) {
				os.write(data, 0, byteRead);
			}
			size -= byteRead;
		}
	}
	
	public static boolean pack(String sFile, String dPath) throws IOException {
		File dir = new File(sFile);
		
		if(!dir.exists()) return false;
		
		File[] list = dir.listFiles();
		
		if(list != null) {
			File dest = new File(dPath);
			RandomAccessFile raf = new RandomAccessFile(dest, "rw");
			raf.writeInt(list.length);
			
			long[] pos = new long[list.length];
			
			for(int i = 0; i < list.length; i++) {
				File file = list[i];
				
				pos[i] = raf.getFilePointer();
				
				raf.writeLong(pos[i]);
				raf.writeUTF(file.getName());
				raf.writeLong(file.length());
			}
			
			for(int i = 0; i < list.length; i++) {
				File file = list[i];
				
				long cursor = raf.getFilePointer();
				
				raf.seek(pos[i]);
				raf.writeLong(cursor);
				raf.seek(cursor);
				
				InputStream is = new BufferedInputStream(new FileInputStream(file));
				copy(is, raf, file.length());
				is.close();
			}
			raf.close();
		}
		return true;
	}
	
	public static boolean unpack(String sFile, String fileName, String dPath) throws IOException {
		File pack = new File(sFile);
		
		if(!pack.exists()) return false;
		
		RandomAccessFile raf = new RandomAccessFile(pack, "r");
		int count = raf.readInt();
		
		for(int i = 0; i < count; i++) {
			long pos = raf.readLong();
			String name = raf.readUTF();
			long fileSize = raf.readLong();
			
			if(fileName.equalsIgnoreCase(name)) {
				raf.seek(pos);
				OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(dPath)));
				copy(raf, os, fileSize);
				os.close();
				break;
			}
		}
		return true;
	}
	
	public static void main(String[] args) {
		String path = "C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\root";
		String destPack = "C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\root.rar";
		String fileExtract = "text.pdf";
		String destUnPack = "C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\" + fileExtract;
		
		try {
			pack(path, destPack);
			unpack(destPack, fileExtract, destUnPack);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
