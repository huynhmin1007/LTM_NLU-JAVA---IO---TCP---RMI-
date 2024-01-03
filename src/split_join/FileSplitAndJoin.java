package split_join;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileSplitAndJoin {
	//orser: 1,2,3,4,5
	static private String makeDestFileName(String source, int order) {
		String ext;
		if (order<10) ext = ".00" + order;
		else if (order<100) ext = ".0" + order;
		else ext = "." + order;
		return source + ext;
	}
	
	static private boolean transfer(InputStream is, OutputStream out, int pSize) throws IOException {
		byte[] buff = new byte[100*1024];
		int remain = pSize;
		int readByte;
		int reqByte;
		while(remain>0 ) {
			reqByte = (remain<buff.length)? remain: buff.length;
			readByte = is.read(buff,0,reqByte);
			if (readByte==-1) { // EOF
				return false;
			}
			out.write(buff,0,readByte);
			remain -= readByte;
		}
		return true;
	}

	static public void split(String path, int pSize) throws IOException {
		String dest;
		int order = 0;
		InputStream is;
		OutputStream os;
		is = new FileInputStream(path);
		boolean res = false;;
		do {
			order++;
			dest = makeDestFileName(path, order);
			os = new FileOutputStream(dest);
			res = transfer(is, os, pSize);		
			os.close();
		} while (res);
		is.close();
	}
	public static void main(String[] args) throws IOException {
		String path = "C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\root\\text.pdf";
		int pSize = 1500000;
		long bt = System.currentTimeMillis();
		split(path, pSize);
		long et = System.currentTimeMillis();
		System.out.println("Spliting in: " + (et-bt) + " ms");
	}
}

