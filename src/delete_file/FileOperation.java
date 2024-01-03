package delete_file;

import java.io.File;

public class FileOperation {
	
	public static boolean remove(String path) {
		File file = new File(path);
		
		if(!file.exists()) return true;
		
		File[] list = file.listFiles();
		
		if(list != null) {
			for(File f : list) {
				remove(f.getAbsolutePath());
			}
		}
		return file.delete();
	}
	
	public static void main(String[] args) {
		String path = "C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\test";
		System.out.println(remove(path));;
	}
}
