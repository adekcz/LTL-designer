/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.view;

import cz.muni.fi.xkeda.ltl_designer.resources.ResourcesManager;
import cz.muni.fi.xkeda.ltl_designer.view.FormulaElements.MyCircle;
import cz.muni.fi.xkeda.ltl_designer.view.FormulaElements.MyLine;
import cz.muni.fi.xkeda.ltl_designer.view.FormulaElements.MyShape;
import cz.muni.fi.xkeda.ltl_designer.view.FormulaElements.StartFormulaNode;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

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

	private MyLine connectingLine;
	private MyShape connectingShape;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		status = CanvasStatus.IDLE;
		canvas.setOnMouseMoved((eventMoved) -> {
			if (status == CanvasStatus.CONNECTING_FORMULAE && connectingLine != null) {
				//-2 because clicking on element does not propagate throught line if line is directly under cursor... OPTIONALY try to find better solution
				connectingLine.getShape().setEndX(eventMoved.getX() - 2);
				connectingLine.getShape().setEndY(eventMoved.getY() - 2);
			}
		});

	}
//             ######## ##     ## ######## ##    ## ######## 
//             ##       ##     ## ##       ###   ##    ##    
//             ##       ##     ## ##       ####  ##    ##    
//             ######   ##     ## ######   ## ## ##    ##    
//             ##        ##   ##  ##       ##  ####    ##    
//             ##         ## ##   ##       ##   ###    ##    
//             ########    ###    ######## ##    ##    ##    
//             
//             
//             ##     ##    ###    ##    ## ########  ##       ######## ########   ######  
//             ##     ##   ## ##   ###   ## ##     ## ##       ##       ##     ## ##    ## 
//             ##     ##  ##   ##  ####  ## ##     ## ##       ##       ##     ## ##       
//             ######### ##     ## ## ## ## ##     ## ##       ######   ########   ######  
//             ##     ## ######### ##  #### ##     ## ##       ##       ##   ##         ## 
//             ##     ## ##     ## ##   ### ##     ## ##       ##       ##    ##  ##    ## 
//             ##     ## ##     ## ##    ## ########  ######## ######## ##     ##  ###### 

	@FXML
	void handleCanvasClick(MouseEvent event) {
		double x = event.getX();
		double y = event.getY();
		//TODO --- completely wrong condition
		if (status == CanvasStatus.CREATING_DOT) {
			new StartFormulaNode(x, y, this);
			setStatus(CanvasStatus.IDLE);
		}
		if(status == CanvasStatus.CREATING_NEW_ELEMENT){
			new MyCircle(x, y, 20, this);
			setStatus(CanvasStatus.IDLE);
		}
	}

	@FXML
	void handleNodeAction(ActionEvent event) {
		setStatus(CanvasStatus.CREATING_NEW_ELEMENT);
	}

	@FXML
	void handleStartAction(ActionEvent event) {
		setStatus(CanvasStatus.CREATING_DOT);
	}

	@FXML
	void handleArrowAction(ActionEvent event) {
		setStatus(CanvasStatus.CONNECTING_FORMULAE);
	}

	@FXML
	void handleConnectAction(ActionEvent event) {

	}

	@FXML
	void handleNextAction(ActionEvent event) {

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

	public void setStatus(CanvasStatus status) {
		this.status = status;
		switch (status) {
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
			case CREATING_DOT:
				rootPane.setCursor(Cursor.cursor(ResourcesManager.getResourceAsString("Cursor3.png")));
				break;
			default:
				throw new AssertionError(status.name());

		}

	}

	public void add(Shape shape) {
		canvas.getChildren().add(shape);
	}

	private void resetCursor() {
		rootPane.setCursor(Cursor.DEFAULT);
	}

	public CanvasStatus getStatus() {
		return status;
	}

	public MyShape getConnectingShape() {
		return connectingShape;
	}

	public void setConnectingShape(MyShape connectingShape) {
		this.connectingShape = connectingShape;
	}

	public void setConnectingLine(MyLine line) {
		this.connectingLine = line;
	}

	public MyLine getConnectingLine() {
		return connectingLine;
	}

	public Pane getCanvas() {
		return canvas;
	}

}
