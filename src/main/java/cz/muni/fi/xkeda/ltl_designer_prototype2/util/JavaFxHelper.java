/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.util;

import com.google.common.base.Strings;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;

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
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		return loader;
	}
	
	
	public static double getWidth(Node node){
		return node.getLayoutBounds().getMaxX() - node.getLayoutBounds().getMinX();
	}
	public static int countSubstringOccurencies(String text, String pattern){
		if(Strings.isNullOrEmpty(text) || Strings.isNullOrEmpty(pattern)){
			return 0;
		}
		Pattern pat = Pattern.compile(pattern);
		Matcher mat = pat.matcher(text);
		int result = 0;
		while(mat.find()){
			result++;
		}

		return result;
	}
}
