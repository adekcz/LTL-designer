/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view;

import java.io.File;
import java.math.BigInteger;
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
public class Zxxxxxxxxx extends javafx.scene.control.ListCell<File> implements ChangeListener<File> {

	public static final DataFormat dataformat = new DataFormat("bigInt");

	public Zxxxxxxxxx() {
		setOnDragDetected((event) -> {
			/* drag was detected, start a drag-and-drop gesture*/
			/* allow any transfer mode */
			Dragboard db = Zxxxxxxxxx.this.startDragAndDrop(TransferMode.ANY);

			/* Put a string on a dragboard */
			ClipboardContent content = new ClipboardContent();
			BigInteger p = new BigInteger("12343");
			content.put(dataformat, p);
			content.putString(Zxxxxxxxxx.this.getText());
			db.setContent(content);

			event.consume();

		});
	}

	@Override
	public void updateItem(File item, boolean empty) {
		super.updateItem(item, empty);
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
