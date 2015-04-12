/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.settings;

import cz.muni.fi.xkeda.ltl_designer_prototype2.util.ResourcesHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adekcz
 */
//todo move this to custom editable file
public class Settings {


	//keys to map
	public static final String FORMULA_COLOR = "FORMULA_COLOR";
	public static final String START_POINT_COLOR = "START_POINT_COLOR";
	public static final String GRAB_POINT_COLOR = "GRAB_POINT_COLOR";
	public static final String SELECTED_COLOR = "SELECTED_COLOR";

	private static final Properties currentSettings = new Properties();


	public static final String SETTINGS_RESOURCE_FOLDER_PATH = "saved";
	public static final String SETTINGS_RESOURCE_FILE_PATH = SETTINGS_RESOURCE_FOLDER_PATH + "/defaultProperties.properties";

	public static final String SETTINGS_FOLDER_PATH = System.getProperty("user.dir");
	public static final String SETTINGS_FILE_PATH = SETTINGS_FOLDER_PATH + File.separator + SETTINGS_RESOURCE_FOLDER_PATH + File.separator + "defaultProperties.properties";

	/**
	 * Reads settings from default location. If it does not exists, tries to
	 * create it.
	 */
	public static void initSettings() {
		File settingsFile = new File(SETTINGS_FILE_PATH);
		Properties loaded = null;
		if (settingsFile.exists()) {
			loaded = loadFromExistingFile(settingsFile);
		} else {
			loaded = loadDefaultSettings();
			saveSettings(loaded);
		}
		currentSettings.putAll(loaded);

	}

	private static Properties loadFromExistingFile(File settingsFile) {
		Properties loaded = null;
		try {
			loaded = loadSettings(new FileInputStream(settingsFile));
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
		}
		return loaded;
	}

	private static Properties loadDefaultSettings() {
		InputStream input = ResourcesHelper.getResourceAsInputStream("/" + SETTINGS_RESOURCE_FILE_PATH);
		Properties prop = loadSettings(input);
		return prop;
	}

	private static Properties loadSettings(InputStream input) {
		Properties prop = new Properties();
		try {
			//load a properties file from class path, inside static method
			prop.load(input);
		} catch (IOException ex) {
			Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, "Could not load default settings", ex);
		}
		return prop;
	}

	private static void replaceSettings(Properties other) {
		currentSettings.clear();
		currentSettings.putAll(other);
	}

	private static void saveSettings(Properties prop) {
		try (OutputStream output = new FileOutputStream(SETTINGS_FILE_PATH)) {
			// save properties to project root folder
			prop.store(output, "Contains all customizable settings in LTL Designer. Edit _Sensibly_.");
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	public static String get(String key) {
		return currentSettings.getProperty(key);
	}

}
