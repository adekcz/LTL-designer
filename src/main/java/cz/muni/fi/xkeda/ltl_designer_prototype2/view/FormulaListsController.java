/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	private ListView<String> defaultFormulasLV;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		defaultFormulasLV.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
		ObservableList<String> data = FXCollections.observableArrayList(
			"chocolate", "salmon", "gold", "coral", "darkorchid",
			"darkgoldenrod", "lightsalmon", "black", "rosybrown", "blue",
			"blueviolet", "brown");

		defaultFormulasLV.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
			@Override
			public ListCell<String> call(ListView<String> list) {
				Zxxxxxxxxx zxxxxxxxxx = new Zxxxxxxxxx();

				return zxxxxxxxxx;
			}
		}
		);
		//defaultFormulasLV.getSelectionModel().g
		defaultFormulasLV.getSelectionModel().selectedItemProperty().addListener(
			new ChangeListener<String>() {
				public void changed(ObservableValue<? extends String> ov,
					String old_val, String new_val) {
				}
			});
		defaultFormulasLV.setItems(data);

		// TODO
	}

}
