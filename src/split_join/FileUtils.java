package split_join;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils {
	
	public String getIdentify(String source, int order) {
		return source + String.format(".%03d", order);
	}
	
	public void copy(InputStream is, OutputStream os, long size) throws IOException {
		byte[] data = new byte[8 * 1024];
		while(size > 0) {
			int byteRead = (int) Math.min(data.length, size);
			if(is.read(data, 0, byteRead) != -1) {
				os.write(data, 0, byteRead);
			}
			size -= byteRead;
		}
	}
	
	public void split(String path, long pSize) throws IOException {
		File file = new File(path);
		
		if(!file.exists()) return;
		
		if(file.isFile()) {
			File newFolder = new File(file.getParent() + "\\split");
			newFolder.mkdir();
			
			final String fileChild = newFolder.getAbsolutePath() + "\\" + file.getName();
			long totalSize = file.length();
			
			InputStream is = new BufferedInputStream(new FileInputStream(file));
			
			for(int i = 1; totalSize > 0; i++) {
				OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(getIdentify(fileChild, i))));
				
				long sizeFileChild = Math.min(totalSize, pSize);
				copy(is, os, sizeFileChild);
				totalSize -= sizeFileChild;
				
				os.close();
			}
			is.close();
		}
	}
	
	public void join(String path, String dest) throws IOException {
		File dir = new File(path);
		
		if(!dir.exists()) return;
		
		File[] list = dir.listFiles();
		if(list != null) {
			File fileDest = new File(dest);
			
			OutputStream os = new BufferedOutputStream(new FileOutputStream(fileDest));
			for(File file : list) {
				InputStream is = new BufferedInputStream(new FileInputStream(file));
				copy(is, os, file.length());
				
				is.close();
			}
			os.close();
		}
	}
	
	public static void main(String[] args) {
		String split = "C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\root\\text.pdf";
		String srcJoin = "C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\root\\split";
		String destJoin = "C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\root\\join.pdf";
		
		FileUtils fileUtils = new FileUtils();
		try {
			fileUtils.split(split, 1500000);
			fileUtils.join(srcJoin, destJoin);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
