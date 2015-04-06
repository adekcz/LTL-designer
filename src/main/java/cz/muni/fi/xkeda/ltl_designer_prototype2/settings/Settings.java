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
	public static final String SETTINGS_PATH = System.getProperty("user.dir") + File.separator + "settings.properties";

	private static final Properties currentSettings = new Properties();

	private static final String SETTINGS_PROPERTIES_PATH = "/various/defaultProperties.properties";

	/**
	 * Creates file with defaults if it does not exists and reads it. Reads
	 * existing file otherwise.
	 */
	public static void initSettings() {
		File settingsFile = new File(SETTINGS_PATH);
		Properties loaded = null;
		if (settingsFile.exists()) {
			try {
				 loaded = loadSettings(new FileInputStream(settingsFile));
			} catch (FileNotFoundException ex) {
				Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			loaded = loadDefaultSettings();
			saveSettings(loaded);
		}
		currentSettings.putAll(loaded);

	}

	private static Properties loadDefaultSettings() {
		InputStream input = ResourcesHelper.getResourceAsInputStream(SETTINGS_PROPERTIES_PATH);
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
			System.exit(-1);
		}
		return prop;
	}

	private static void replaceSettings(Properties other) {
		currentSettings.clear();
		currentSettings.putAll(other);
	}

	private static void saveSettings(Properties prop) {
		try {
			OutputStream output = null;
			output = new FileOutputStream(SETTINGS_PATH);
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
