package print_dir;

import java.io.File;

public class Dir {
	
	public static void dirTree(String path) {
		File file = new File(path);
		
		if(!file.exists()) return;
		
		if(file.isFile()) System.out.println(printFile(file, 0));
		else if(file.isDirectory()) {
			dirTreeHelper(file, 0);
		}
	}
	
	public static void dirTreeHelper(File dir, int level) {
		System.out.println(printDir(dir, level));
		
		File[] list = dir.listFiles();
		
		for(File folder : list) {
			if(folder.isDirectory()) {
				dirTreeHelper(folder, level + 1);
			}
		}
		
		for(File file : list) {
			if(file.isFile()) {
				System.out.println(printFile(file, level + 1));
			}
		}
	}
	
	public static String printDir(File dir, int level) {
		StringBuilder indent = getIndent(level);
		indent.append(dir.getName().toUpperCase());
		return indent.toString();
	}
	
	public static String printFile(File file, int level) {
		StringBuilder indent = getIndent(level);
		indent.append(file.getName().toLowerCase());
		return indent.toString();
	}
	
	public static StringBuilder getIndent(int level) {
		StringBuilder builder = new StringBuilder();
		
		if(level == 0) {
			builder.append("+-");
		}
		else {
			builder.append("   ");
			
			for(int i = 1; i < level; i++) {
				builder.append("|  ");
			}
			builder.append("+-");
		}
		return builder;
	}
	
	public static void main(String[] args) {
		String path = "C:\\Users\\MSI GTX\\Workspace\\java\\school\\LTM\\data\\root";
		dirTree(path);
	}
}
