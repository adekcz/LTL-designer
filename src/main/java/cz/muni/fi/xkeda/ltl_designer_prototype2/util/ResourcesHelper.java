/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.util;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author adekcz
 */
public class ResourcesHelper {

	public static URL getResource(String name) {
		return ResourcesHelper.class.getResource(name);
	}

	public static String getResourceAsString(String path) {
		return getResource(path).toExternalForm();
	}

	public static InputStream getResourceAsInputStream(String path) {
		return ResourcesHelper.class.getResourceAsStream(path);
	}

	/**
	 * Creates files in default folder. Creates: Settings.properties all
	 * premanufactured formulaes
	 */
	public static void initAllFiles() {
		try {
			List<String> allPremafucturedFiles = getAllPremafucturedFiles();
			allPremafucturedFiles.remove(0);
			for (String file : allPremafucturedFiles) {
				exportResource("/" + file);

			}
		} catch (IOException ex) {
			Logger.getLogger(ResourcesHelper.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private static String getJarFolder() throws URISyntaxException {
		return new File(ResourcesHelper.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/'); //yeah, this is copy and paste from internet

	}

	/**
	 * http://stackoverflow.com/questions/10308221/how-to-copy-file-inside-jar-to-outside-the-jar
	 * Export a resource embedded into a Jar file to the local file path.
	 *
	 * @param resourceName ie.: "/SmartLibrary.dll"
	 * @return The path to the exported resource
	 * @throws Exception
	 */
	static public String exportResource(String resourceName) {
		File toBeCreated = new File(Settings.APP_HOME_FOLDER_PATH + resourceName.replace('\\', '/'));
		if (!toBeCreated.exists()) {
			FileHelper.createPredecessorFolderStructure(toBeCreated);

			writeResource(resourceName, toBeCreated);
		}
		return Settings.APP_HOME_FOLDER_PATH + resourceName;
	}

	private static void writeResource(String resourceName, File toBeCreated) {
		try (InputStream stream = ResourcesHelper.class.getResourceAsStream(resourceName); //note that each / is a directory down in the "jar tree" been the jar the root of the tree
			OutputStream resStreamOut = new FileOutputStream(toBeCreated)) {

			writeStream(stream, resStreamOut);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(ResourcesHelper.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(ResourcesHelper.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private static void writeStream(final InputStream stream, final OutputStream resStreamOut) throws IOException {
		int readBytes;
		byte[] buffer = new byte[4096];
		while ((readBytes = stream.read(buffer)) > 0) {
			resStreamOut.write(buffer, 0, readBytes);
		}
	}

	public static List<String> getAllPremafucturedFiles() throws IOException {
		List<String> allFiles = new ArrayList<>();
		CodeSource src = ResourcesHelper.class.getProtectionDomain().getCodeSource();
		if (src != null) {
			loadPathsFromJar(src, allFiles);
		} else {
			/* Fail... */
		}
		return allFiles;
	}

	private static void loadPathsFromJar(CodeSource src, List<String> allFiles) throws IOException {
		URL jar = src.getLocation();
		ZipInputStream zip = new ZipInputStream(jar.openStream());
		while (true) {
			if (checkNextZipEntry(zip, allFiles)) {
				break;
			}
		}
	}

	private static boolean checkNextZipEntry(ZipInputStream zip, List<String> allFiles) throws IOException {
		ZipEntry e = zip.getNextEntry();
		if (e == null) {
			return true;
		}
		String name = e.getName();
		if (name.startsWith(Settings.SETTINGS_RESOURCE_FOLDER_PATH) && !name.equals(Settings.SETTINGS_RESOURCE_FOLDER_PATH)) {
			allFiles.add(name);
		}
		return false;
	}
}
