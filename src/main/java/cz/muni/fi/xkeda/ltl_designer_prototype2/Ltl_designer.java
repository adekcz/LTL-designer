/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JavaFxHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.ResourcesHelper;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author adekcz
 */
public class Ltl_designer extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	@Override
	public void start(Stage stage) throws Exception {
		ResourcesHelper.initAllFiles();
		Settings.initSettings();
		
		loadLayoutFromFxml();
		setupStage(stage);
	}

	private void loadLayoutFromFxml() throws IOException {
		FXMLLoader loader = JavaFxHelper.getLoader(this.getClass(), "/fxml/MainWindow.fxml");
		rootLayout = (BorderPane) loader.load();
	}

	private void setupStage(Stage stage) {

		this.primaryStage = stage;
		this.primaryStage.setTitle("LTL Designer");
		this.primaryStage.setMinHeight(300);
		this.primaryStage.setMinWidth(450);

		displayStage();
	}

	private void displayStage() {
		Scene scene = new Scene(rootLayout);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
