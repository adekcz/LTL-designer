/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JavaFxHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JsonHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.menu.AboutDialogController;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javax.json.JsonObject;

/**
 *
 * @author adekcz
 */
public class MainWindowController implements Initializable {

	@FXML
	private BorderPane rootPane;
	@FXML
	private Label label;

	private CanvasController canvasController;

	private File lastDirectory;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			FXMLLoader loader = JavaFxHelper.getLoader(this.getClass(), "/fxml/Canvas.fxml");
			Pane pane = (Pane) loader.load();
			canvasController = (CanvasController) loader.getController();

			rootPane.setCenter(pane);
		} catch (IOException ex) {
			Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

//        ##     ## ######## ##    ## ##     ## 
//        ###   ### ##       ###   ## ##     ## 
//        #### #### ##       ####  ## ##     ## 
//        ## ### ## ######   ## ## ## ##     ## 
//        ##     ## ##       ##  #### ##     ## 
//        ##     ## ##       ##   ### ##     ## 
//        ##     ## ######## ##    ##  #######  
	@FXML
	void handleSettingsAction(ActionEvent event) {
		Alert settingsDialog = new Alert(Alert.AlertType.INFORMATION);
		settingsDialog.setTitle("Settings");
		settingsDialog.setContentText("Settings file can be found at: " + Settings.get(Settings.SETTINGS_PATH) + "\n" + 
			"Edit it and restart LTL Designer\n\n\n" +
			"(settings dialog v0.001)");
		settingsDialog.showAndWait();

	}

	@FXML
	void handleHelpAboutAction(ActionEvent event) {
		AboutDialogController.createAboutDialog((Node) rootPane).show();
	}

	@FXML
	void handleOpenAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open file");
		fileChooser.setInitialDirectory(lastDirectory);
		fileChooser.setInitialFileName("");
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Project file", "*.json"),
			new FileChooser.ExtensionFilter("Project file", "*.*")
		);
		File chosenFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
		try {
			if (chosenFile != null) {
				lastDirectory = chosenFile.getParentFile();
				JsonHelper.loadJson(chosenFile, canvasController);
			}
		} catch (FileNotFoundException ex) { //should not normally happen since filechooser does not allow to select nonexisting object
			Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
			JavaFxHelper.showErrorDialog(ex);
		}
	}

	@FXML
	void handleSaveAsAction(ActionEvent event) {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save current view");

		fileChooser.setInitialFileName("formulae.json");
		fileChooser.setInitialDirectory(lastDirectory);
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Project file", "*.json"),
			new FileChooser.ExtensionFilter("Project file", "*.*")
		);
		File chosenFile = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
		// decide how to save file based on this:
		//System.out.println(fileChooser.getSelectedExtensionFilter().getExtensions());
		if (chosenFile != null) {
			lastDirectory = chosenFile.getParentFile();
			try {
				System.out.println(chosenFile.getAbsoluteFile());
				JsonObject json = JsonHelper.elementsToJson(canvasController.getAllNodes()).build();
				JsonHelper.saveJson(json, chosenFile);
			} catch (FileNotFoundException ex) {
				Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
				JavaFxHelper.showErrorDialog(ex);

			}
		}

	}

}
