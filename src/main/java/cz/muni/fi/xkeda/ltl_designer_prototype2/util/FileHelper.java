/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adekcz
 */
public class FileHelper {

	public static void createPredecessorFolderStructure(File file) {
		File parent = file.getParentFile();
		if (!parent.exists()) {
			boolean parentCreated = parent.mkdirs();
			if (!parentCreated) {
				IOException ioException = new IOException("Folder structure for file: " + file.getAbsolutePath() + " could not be created. Please do it manually.\n");
				JavaFxHelper.showErrorDialog("Folder structure could not be crated.", ioException);
			}
		}
	}

	/**
	 * @param dir where to look
	 * @param suffix NOT regex, plain old string; Empty string matches all files
	 * @return list of files (not directories) one level below dir
	 */
	public static List<File> getAllFiles(File dir, String suffix) {
		List<File> filesInDir = new ArrayList<>();
		for (File current : dir.listFiles()) {
			if (current.isFile() && current.getAbsolutePath().endsWith(suffix)) {
				filesInDir.add(current);
			}
		}
		return filesInDir;
	}

}
