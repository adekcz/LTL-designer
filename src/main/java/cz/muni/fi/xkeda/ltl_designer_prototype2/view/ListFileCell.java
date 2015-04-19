/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view;

import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 *
 * @author adekcz
 */
public class ListFileCell extends javafx.scene.control.ListCell<File> implements ChangeListener<File> {

	public static final DataFormat fileDragFormat = new DataFormat("file");
	private File file;


	ListFileCell(CanvasController canvasController) {
		setOnDragDetected((event) -> {
			/* drag was detected, start a drag-and-drop gesture*/
			/* allow any transfer mode */
			Dragboard db = ListFileCell.this.startDragAndDrop(TransferMode.ANY);

			/* Put a string on a dragboard */
			ClipboardContent content = new ClipboardContent();
			content.put(fileDragFormat, file);
			content.putString(ListFileCell.this.getText());
			canvasController.setStatus(CanvasStatus.DRAGGING_SAVED_FORMULA);
			db.setContent(content);

			event.consume();

		});
	}


	@Override
	public void updateItem(File item, boolean empty) {
		super.updateItem(item, empty);
		file = item;
		if(item != null){
			setText(item.getName());
		} 
		if(empty){
			setText("empty line");
		}
	}
//Basic mind making I'm adding up to the cell so that

	@Override
	public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
		System.out.println("Changed");

	}
}
