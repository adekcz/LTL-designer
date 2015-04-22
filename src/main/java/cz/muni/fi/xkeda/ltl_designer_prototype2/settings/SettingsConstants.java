/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.settings;

import java.io.File;

/**
 *
 * @author adekcz
 */
public class SettingsConstants {
	public static final String LAST_OPEN_DIRECTORY = "LAST_OPEN_DIRECTORY";

	public static final String GRAB_POINT_COLOR = "GRAB_POINT_COLOR";
	public static final String PLACEHOLDER_FOR_FORMULA_START = "PLACEHOLDER_FOR_FORMULA_START";
	public static final String START_POINT_COLOR = "START_POINT_COLOR";
	public static final String FORMULA_COLOR = "FORMULA_COLOR";
	public static final String SELECTED_COLOR = "SELECTED_COLOR";

	public static final String APP_HOME_FOLDER_PATH = System.getProperty("user.dir");
	public static final String SETTINGS_RESOURCE_FOLDER_PATH = "saved";
	public static final String SETTINGS_FOLDER_PATH = APP_HOME_FOLDER_PATH + File.separator + "saved";
	public static final String SETTINGS_FILE_PATH = APP_HOME_FOLDER_PATH + File.separator + SETTINGS_RESOURCE_FOLDER_PATH + File.separator + "defaultProperties.properties";
	public static final String SETTINGS_RESOURCE_FILE_PATH = SETTINGS_RESOURCE_FOLDER_PATH + "/defaultProperties.properties";
}
