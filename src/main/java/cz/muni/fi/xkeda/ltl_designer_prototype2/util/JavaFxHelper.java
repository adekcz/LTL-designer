/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.util;

import javafx.fxml.FXMLLoader;

/**
 *
 * @author adekcz
 */
public class JavaFxHelper {
	/**
	 * @param clazz that will be used access resource
	 * @param path to *.fxml file
	 * @return 
	 */
	public static FXMLLoader getLoader(Class clazz, String path){
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(clazz.getResource(path));
		return loader;
	}
	
	
}
