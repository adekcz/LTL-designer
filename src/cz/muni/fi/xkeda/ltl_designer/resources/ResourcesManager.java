/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.resources;

import java.net.URL;

/**
 *
 * @author adekcz
 */
public class ResourcesManager {
	public static URL getResource(String name){
		return ResourcesManager.class.getResource(name);
	}

	public static String getResourceAsString(String name) {
		return getResource(name).toExternalForm();
	}
	
}
