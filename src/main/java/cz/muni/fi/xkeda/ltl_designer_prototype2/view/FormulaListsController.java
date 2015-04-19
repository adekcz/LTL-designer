/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.FileHelper;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author adekcz
 */
public class FormulaListsController implements Initializable {

	@FXML
	private ListView<File> defaultFormulasLV;
	private CanvasController canvasController;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		defaultFormulasLV.setBackground(new Background(new BackgroundFill(Color.BEIGE, CornerRadii.EMPTY, Insets.EMPTY)));
		ObservableList<File> data = FXCollections.observableArrayList();
		System.out.println(Settings.SETTINGS_FOLDER_PATH);
		List<File> files = FileHelper.getAllFiles(new File(Settings.SETTINGS_FOLDER_PATH), "json");
		data.addAll(files);
		defaultFormulasLV.setItems(data);
		
		//= FXCollections.observableArrayList(
			//"chocolate", "salmon", "gold", "coral", "darkorchid",
			//"darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue",
			//"blueviolet", "brown");

		defaultFormulasLV.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
			@Override
			public ListCell<File> call(ListView<File> list) {
				ListFileCell cell = new ListFileCell(canvasController);
				return cell;
			}
		}
		);
		//defaultFormulasLV.getSelectionModel().g
		//defaultFormulasLV.getSelectionModel().selectedItemProperty().addListener(
			//new ChangeListener<String>() {
				//public void changed(ObservableValue<? extends String> ov,
					//String old_val, String new_val) {
				//}
			//});

		// TODO
	}

	void setCanvasController(CanvasController canvasController) {
		this.canvasController = canvasController;
	}

}
