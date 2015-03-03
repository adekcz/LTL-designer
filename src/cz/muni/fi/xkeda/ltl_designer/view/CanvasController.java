/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.view;

import cz.muni.fi.xkeda.ltl_designer.view.FormulaElements.MyCircle;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

/**
 * FXML Controller class
 *
 * @author adekcz
 */
public class CanvasController implements Initializable {

	@FXML
	private BorderPane rootPane;
	@FXML
	private Pane canvas;

	private CanvasStatus status;

//TODO remove as soon as possible
	Line line = new Line();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		status = CanvasStatus.IDLE;
		canvas.setOnMouseMoved((eventMoved) -> {
			line.setEndX(eventMoved.getX());
			line.setEndY(eventMoved.getY());

		});

	}


	@FXML
	void handleCanvasClick(MouseEvent event) {
		double x = event.getX();
		double y = event.getY();
		if (equalsToCurrentCursor(Cursor.HAND)) {
			MyCircle circle = new MyCircle(x, y, 30, this);
			canvas.getChildren().add(circle);

		}
		setStatus(CanvasStatus.IDLE);
	}
		
	@FXML
	void handleConnectAction(ActionEvent event) {
		setStatus(CanvasStatus.CONNECTING_FORMULAE);

	}
	@FXML
	void handleNextAction(ActionEvent event) {
		setStatus(CanvasStatus.CREATING_NEW_ELEMENT);

	}

	@FXML
	void handleGlobalAction(ActionEvent event) {

	}

	@FXML
	void handleFinallyAction(ActionEvent event) {

	}

	@FXML
	void handleUntilAction(ActionEvent event) {

	}

	@FXML
	void handleReleaseAction(ActionEvent event) {

	}

//       ##     ## ####  ######   ######  
//       ###   ###  ##  ##    ## ##    ## 
//       #### ####  ##  ##       ##       
//       ## ### ##  ##   ######  ##       
//       ##     ##  ##        ## ##       
//       ##     ##  ##  ##    ## ##    ## 
//       ##     ## ####  ######   ###### 
	private boolean equalsToCurrentCursor(Cursor other) {
		if (rootPane == null || other == null || rootPane.getCursor() == null) {
			return false;
		}
		return rootPane.getCursor().equals(other);
	}
	private void setStatus(CanvasStatus status){
		this.status = status;
		switch (status){
			case IDLE: {
				resetCursor();
				break;
			}
			case CONNECTING_FORMULAE:
				rootPane.setCursor(Cursor.CROSSHAIR);
				break;
			case CREATING_NEW_ELEMENT:
				rootPane.setCursor(Cursor.HAND);
				break;
			default:
				throw new AssertionError(status.name());

		}

	}

	private void resetCursor() {
		rootPane.setCursor(Cursor.DEFAULT);
	}

	public CanvasStatus getStatus() {
		return status;
	}

}
