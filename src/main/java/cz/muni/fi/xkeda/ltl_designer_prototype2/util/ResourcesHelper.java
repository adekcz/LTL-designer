/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.util;

import java.io.InputStream;
import java.net.URL;

/**
 *
 * @author adekcz
 */
public class ResourcesHelper {
	public static URL getResource(String name){
		return ResourcesHelper.class.getResource(name);
	}

	public static String getResourceAsString(String path) {
		return getResource(path).toExternalForm();
	}
	public static InputStream getResourceAsInputStream(String path){
		return ResourcesHelper.class.getResourceAsStream(path);
	}
	
}
