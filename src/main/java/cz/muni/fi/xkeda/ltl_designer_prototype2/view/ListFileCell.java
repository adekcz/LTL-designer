/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view;

import java.io.File;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

/**
 *
 * @author adekcz
 */
public class ListFileCell extends javafx.scene.control.ListCell<File> {

	public static final DataFormat fileDragFormat = new DataFormat("file");
	private File file;

	ListFileCell(CanvasController canvasController) {
		setOnDragDetected((event) -> {
			if (!isEmpty()) {
				/* drag was detected, start a drag-and-drop gesture*/
				/* allow any transfer mode */
				Dragboard db = ListFileCell.this.startDragAndDrop(TransferMode.ANY);

				/* Put a string on a dragboard */
				ClipboardContent content = new ClipboardContent();
				content.put(fileDragFormat, file);
				canvasController.setStatus(CanvasState.DRAGGING_SAVED_FORMULA);
				db.setContent(content);

				event.consume();

			}
		});
	}

	@Override
	public void updateItem(File item, boolean empty) {
		super.updateItem(item, empty);
		file = item;
		if (item != null) {
			setText(item.getName());
		}
		if (empty) {
			setText("empty line");
		}
	}
}
