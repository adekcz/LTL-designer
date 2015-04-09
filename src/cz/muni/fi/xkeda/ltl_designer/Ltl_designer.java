/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer;

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
        this.primaryStage = stage;
        this.primaryStage.setTitle("LTL Designer");

	    FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Ltl_designer.class.getResource("view/MainWindow.fxml"));
		rootLayout = (BorderPane) loader.load();
		
		Scene scene = new Scene(rootLayout);
		
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
}