/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view;

import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JavaFxHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JsonHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.menu.AboutDialogController;
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
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
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
	void handleHelpAboutAction(ActionEvent event) {
		AboutDialogController.createAboutDialog((Node) rootPane).show();
	}

	@FXML
	void handleSaveAsAction(ActionEvent event) {
			JsonObject json = JsonHelper.elementsToJson(canvasController.getAllNodes());
			JsonHelper.saveJson(json, "C:\\tmp\\neco.json");
		/*System.out.println("neco");
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save current view");
		File file = fileChooser.showSaveDialog(rootPane.getScene().getWindow());
		if (file != null) {
			System.out.println(file.getAbsoluteFile());
			JsonObject json = JsonHelper.elementsToJson(canvasController.getAllNodes());
			JsonHelper.saveJson(json, "C:\\tmp\\neco.json");
		}
			*/
	}
}
