package pack;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class FileUtils {
	
	public void copy(InputStream is, RandomAccessFile os, long size) throws IOException {
		byte[] data = new byte[8 * 1024];
		while(size > 0) {
			int byteRead = (int) Math.min(data.length, size);
			if(is.read(data, 0, byteRead) != -1) {
				os.write(data, 0, byteRead);
			}
			size -= byteRead;
		}
	}
	
	public void copy(RandomAccessFile is, OutputStream os, long size) throws IOException {
		byte[] data = new byte[8 * 1024];
		while(size > 0) {
			int byteRead = (int) Math.min(data.length, size);
			if(is.read(data, 0, byteRead) != -1) {
				os.write(data, 0, byteRead);
			}
			size -= byteRead;
		}
	}
	
	public void pack(String folder, String dest) throws IOException {
		File dir = new File(folder);
		
		if(!dir.exists()) return;
		
		File[] list = dir.listFiles();
		
		if(list != null) {
			RandomAccessFile raf = new RandomAccessFile(new File(dest), "rw");
			for(File file : list) {
				InputStream is = new BufferedInputStream(new FileInputStream(file));
				
				raf.writeUTF(file.getName());
				raf.writeLong(file.length());
				
				copy(is, raf, file.length());
				
				is.close();
			}
			raf.close();
		}
	}
	
	public void unpack(String source, String extractFile, String dest) throws IOException {
		File pack = new File(source);
		
		if(!pack.exists()) return;
		
		RandomAccessFile raf = new RandomAccessFile(pack, "r");
		File unpack = new File(dest);
		
		while(true) {
			String fileName = raf.readUTF();
			long fileSize = raf.readLong();
			
			if(extractFile.equalsIgnoreCase(fileName)) {
				OutputStream os = new BufferedOutputStream(new FileOutputStream(unpack));
				copy(raf, os, fileSize);
				os.close();
				break;
			}
			raf.seek(raf.getFilePointer() + fileSize);
		}
		raf.close();
	}
	
	public static void main(String[] args) {
		String path = "C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\root";
		String destPack = "C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\root.rar";
		String fileExtract = "text.pdf";
		String destUnPack = "C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\" + fileExtract;
		
		FileUtils fileUtils = new FileUtils();
		
		try {
			fileUtils.pack(path, destPack);
			fileUtils.unpack(destPack, fileExtract, destUnPack);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
