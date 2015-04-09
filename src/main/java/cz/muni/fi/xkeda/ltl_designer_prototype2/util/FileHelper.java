/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.util;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author adekcz
 */
public class FileHelper {

	/**
	 *
	 */
	public static void createPredecessorFolderStructure(File file) {
		File parent = file.getParentFile();
		if (!parent.exists()) {
			boolean parentCreated = parent.mkdirs();
			if (!parentCreated) {
				JavaFxHelper.showErrorDialog(new IOException("Folder structure for file: " + file.getAbsolutePath() +" could not be created. Please do it manually.\n"));
			}
		}

	}

}
