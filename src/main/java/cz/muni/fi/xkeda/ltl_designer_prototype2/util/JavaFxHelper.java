/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.util;

import com.google.common.base.Strings;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

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
	public static FXMLLoader getLoader(Class clazz, String path) {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(clazz.getResource(path));
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		return loader;
	}

	public static double getWidth(Node node) {
		return node.getLayoutBounds().getMaxX() - node.getLayoutBounds().getMinX();
	}

	public static int countSubstringOccurencies(String text, String pattern) {
		if (Strings.isNullOrEmpty(text) || Strings.isNullOrEmpty(pattern)) {
			return 0;
		}
		Pattern pat = Pattern.compile(pattern);
		Matcher mat = pat.matcher(text);
		int result = 0;
		while (mat.find()) {
			result++;
		}

		return result;
	}

	public static boolean isDoubleClick(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY)) {
			if (event.getClickCount() == 2) {
				return true;
			}
		}
		return false;
	}

	public static void showErrorDialog(String title, Exception ex) {
		Alert errorDialog = new Alert(Alert.AlertType.ERROR);
		errorDialog.setTitle(title);
		errorDialog.setContentText(ex.getMessage());
		GridPane expContent = createExpandableExceptionRegion(ex);

		errorDialog.getDialogPane().setExpandableContent(expContent);
		errorDialog.showAndWait();
	}

	private static GridPane createExpandableExceptionRegion(Exception ex) {
		String exceptionText = getExceptionText(ex);
		Label label = new Label("The exception stacktrace was:");
		TextArea textArea = getTextAreaForExceptionRegion(exceptionText);

		GridPane expContent = createExceptionContentPane(label, textArea);
		return expContent;
	}

	private static GridPane createExceptionContentPane(Label label, TextArea textArea) {
		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);
		return expContent;
	}

	private static TextArea getTextAreaForExceptionRegion(String exceptionText) {
		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);
		return textArea;
	}

	private static String getExceptionText(Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();
		return exceptionText;
	}

}
