package print_dir;

import java.io.File;
import java.util.ArrayList;

public class DirStat {
	
	public static void dirTree(String path) {
		File file = new File(path);
		
		if(!file.exists()) return;
		
		if(file.isFile()) System.out.println(printFile(file, 0));
		else if(file.isDirectory()) {
			dirTreeHelper(file, 0);
		}
	}
	
//	public static long dirTreeHelper(File dir, int level, StringBuilder str) {
//		long size = dir.length();
//        File[] list = dir.listFiles();
//        
//        if (list != null) {
//        	for(int i = list.length - 1; i >= 0; i--) {
//    			File file = list[i];
//    			if(file.isFile()) {
//    				size += file.length();
//    				str.insert(0, printFile(file, level + 1) + "\n");
//    			}
//    		}
//    		
//    		for(int i = list.length - 1; i >= 0; i--) {
//    			File folder = list[i];
//    			if(folder.isDirectory()) {
//    				size += dirTreeHelper(folder, level + 1, str);
//    			}
//    		}
//        }
//        
//        str.insert(0, printDir(dir, level) + size + "\n");
//        
//        return size;
//    }
	
	public static void dirTreeHelper(File dir, int level) {
		System.out.println(printDir(dir, level) + getFolderSize(dir));
		
		File[] list = dir.listFiles();
		if(list != null) {
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
    }
	
	public static long getFolderSize(File dir) {
		long size = dir.length();
		
		File[] list = dir.listFiles();
		if(list != null) {
			for(File file : list) {
				if(file.isDirectory()) {
					size += getFolderSize(file);
				}
				else if(file.isFile()) {
					size += file.length();
				}
			}
		}
		return size;
	}
	
	public static String printDir(File dir, int level) {
		StringBuilder indent = getIndent(level);
		indent.append(dir.getName().toUpperCase());
		indent.append(": ");
		return indent.toString();
	}
	
	public static String printFile(File file, int level) {
		StringBuilder indent = getIndent(level);
		indent.append(file.getName().toLowerCase());
		indent.append(": " + file.length());
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