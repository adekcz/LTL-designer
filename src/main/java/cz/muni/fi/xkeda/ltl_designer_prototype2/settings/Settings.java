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


	public static final Properties currentSettings = new Properties();

	public static String get(String key) {
		return currentSettings.getProperty(key);
	}

	/**
	 * Reads settings from default location. If it does not exists, tries to create it.
	 */
	public static void initSettings() {
		File settingsFile = new File(SettingsConstants.SETTINGS_FILE_PATH);
		Properties loaded;
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
		InputStream input = ResourcesHelper.getResourceAsInputStream("/" + SettingsConstants.SETTINGS_RESOURCE_FILE_PATH);
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

	private static void saveSettings(Properties prop) {
		try (OutputStream output = new FileOutputStream(SettingsConstants.SETTINGS_FILE_PATH)) {
			// save properties to project root folder
			prop.store(output, "Contains all customizable settings in LTL Designer. Edit _Sensibly_.");
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
		}

	}


}
