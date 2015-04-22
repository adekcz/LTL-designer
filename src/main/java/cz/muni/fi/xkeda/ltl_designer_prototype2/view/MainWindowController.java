/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.SettingsConstants;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JavaFxHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JsonHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.AbstractNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
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
	private FormulaListsController formulaListsController;

	private File lastDirectory;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		reloadComponents();
		String lastDirFromSettings = Settings.get(SettingsConstants.LAST_OPEN_DIRECTORY);
		if (lastDirFromSettings != null) {
			lastDirectory = new File(lastDirFromSettings);
		}

	}

	private void reloadComponents() {
		try {
			Pane canvas = loadCanvasAndController();
			Pane formulaLists = loadListsAndController();
			formulaListsController.setCanvasController(canvasController);

			rootPane.setCenter(canvas);
			rootPane.setLeft(formulaLists);
		} catch (IOException ex) {
			Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private Pane loadListsAndController() throws IOException {
		FXMLLoader loaderLists = JavaFxHelper.getLoader(this.getClass(), "/fxml/FormulaLists.fxml");
		Pane formulaLists = (Pane) loaderLists.load();
		formulaListsController = (FormulaListsController) loaderLists.getController();
		return formulaLists;
	}

	private Pane loadCanvasAndController() throws IOException {
		FXMLLoader loaderCanvas = JavaFxHelper.getLoader(this.getClass(), "/fxml/Canvas.fxml");
		Pane canvas = (Pane) loaderCanvas.load();
		canvasController = (CanvasController) loaderCanvas.getController();
		return canvas;
	}

	@FXML
	void handleNewProjectAction(ActionEvent event) {
		reloadComponents();
	}

	@FXML
	void handleQuitAction(ActionEvent event) {
		Platform.exit();
	}

	@FXML
	void handleSettingsAction(ActionEvent event) {
		Alert settingsDialog = new Alert(Alert.AlertType.INFORMATION);
		settingsDialog.setTitle("Settings");
		settingsDialog.setContentText("Settings file can be found at: " + SettingsConstants.SETTINGS_FILE_PATH + "\n"
			+ "Edit it and restart LTL Designer\n\n\n"
			+ "(settings dialog v0.001)");
		settingsDialog.showAndWait();

	}

	@FXML
	void handleHelpAboutAction(ActionEvent event) {
		Alert aboutDialog = new Alert(Alert.AlertType.INFORMATION);
		aboutDialog.setTitle("About Dialog");
		aboutDialog.setHeaderText(null);
		aboutDialog.setContentText("© Michal Keda, FI MUNI, 2015");
		aboutDialog.initModality(Modality.WINDOW_MODAL);
		aboutDialog.initOwner(rootPane.getScene().getWindow());
		aboutDialog.setResizable(false);
		aboutDialog.show();
	}

	@FXML
	void handleOpenAction(ActionEvent event) {
		FileChooser fileChooser = createOpenDialog();
		File chosenFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
		loadJson(chosenFile);
	}

	private FileChooser createOpenDialog() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open file");
		fileChooser.setInitialDirectory(lastDirectory);
		fileChooser.setInitialFileName("");
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Project file", "*.json"),
			new FileChooser.ExtensionFilter("Project file", "*.*")
		);
		return fileChooser;
	}

	private void loadJson(File chosenFile) {
		if (chosenFile != null) {
			lastDirectory = chosenFile.getParentFile();
			try {
				Map<Integer, AbstractNode> loadJson = JsonHelper.loadJson(chosenFile);
				canvasController.addToControler(loadJson);

			} catch (FileNotFoundException ex) { //should not normally happen since filechooser does not allow to select nonexisting object
				Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
				JavaFxHelper.showErrorDialog("File not Found", ex);
			}
		}
	}

	@FXML
	void handleSaveAsAction(ActionEvent event) {
		FileChooser fileChooser = createSaveDialog();
		File chosenFile = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
		saveJson(chosenFile);

	}

	private FileChooser createSaveDialog() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save current view");
		fileChooser.setInitialFileName("formulae.json");
		fileChooser.setInitialDirectory(lastDirectory);
		fileChooser.getExtensionFilters().addAll(
			new FileChooser.ExtensionFilter("Project file", "*.json"),
			new FileChooser.ExtensionFilter("Project file", "*.*")
		);
		return fileChooser;
	}

	private void saveJson(File chosenFile) {
		if (chosenFile != null) {
			lastDirectory = chosenFile.getParentFile();
			JsonObject json = JsonHelper.elementsToJson(canvasController.getAllNodes()).build();
			JsonHelper.saveJson(json, chosenFile);
		}
	}

}
