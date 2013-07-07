package commons.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManagerUtil {

	public static File createFile(String directory, String fileName) {
		File folder = new File(directory);
		
		if (!folder.exists()) {
			folder.mkdirs();
		}
		File file = new File(folder,fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	public static File createFile(String path) {
	    File file = new File(path);
	    File parent = file.getParentFile();
	    if (parent != null && !parent.exists()) {
	        parent.mkdirs();
	    }
	    try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
	    return file;
	}
	public static List<File> getAllSubFiles(File directory) {
		ArrayList<File> fileList = new ArrayList<File>();
		File[] files = directory.listFiles();
		for (File child : files) {
			if (child.isDirectory()) {
				System.out.println("get into folder["+child.getPath()+"]...");
				getAllSubFiles(child);
			} else {
				fileList.add(child);
			}
		}
		return fileList;
	}
	
}
