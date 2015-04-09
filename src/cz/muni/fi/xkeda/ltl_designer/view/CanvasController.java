/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.view;

import cz.muni.fi.xkeda.ltl_designer.resources.ResourcesManager;
import cz.muni.fi.xkeda.ltl_designer.view.FormulaElements.FormulaNode;
import cz.muni.fi.xkeda.ltl_designer.view.FormulaElements.FormulaShape;
import cz.muni.fi.xkeda.ltl_designer.view.FormulaElements.LineGrabPoint;
import cz.muni.fi.xkeda.ltl_designer.view.FormulaElements.PolygonalChain;
import cz.muni.fi.xkeda.ltl_designer.view.FormulaElements.StartFormulaNode;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.CacheHint;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
	@FXML
	private TextField txtFormulae;
	@FXML
	private HBox hbFormula;

	private CanvasStatus status;

	private PolygonalChain connectingLine;
	private FormulaShape connectingShape;
	private List<FormulaShape> selectedNodes;
	private double x;
	private double y;

// records relative x and y co-ordinates.
	static class Delta {

		double x, y;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// allow the label to be dragged around.
		Circle circle = new Circle(40, 40, 40, Color.GOLD);
		Color col = new Color(0.5, 0, 0, 0.5);
		 circle.setCache(true);
        //circle.setCacheHint(CacheHint.QUALITY);
        // Do an animation
        circle.setCacheHint(CacheHint.SPEED);
		circle.setFill(col);
		add(circle);
		final Delta dragDelta = new Delta();
		circle.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				// record a delta distance for the drag and drop operation.
				dragDelta.x = circle.getLayoutX() - mouseEvent.getSceneX();
				dragDelta.y = circle.getLayoutY() - mouseEvent.getSceneY();
				circle.setCursor(Cursor.MOVE);
			}
		});
		circle.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				circle.setCursor(Cursor.HAND);
			}
		});
		circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				circle.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
				circle.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
			}
		});
		circle.setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				circle.setCursor(Cursor.HAND);
			}
		});

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
		x = event.getX();
		y = event.getY();
		switch (status) {
			case IDLE:
				break;
			case CONNECTING_FORMULAE:
				if (getConnectingShape() != null && !getConnectingShape().getShape().intersects(x, y, 2, 2)) {
					LineGrabPoint.createLineGrabPoint(x, y, this);
				}
				break;
			case CREATING_NEW_ELEMENT:
				new FormulaNode(x, y, this);
				setStatus(CanvasStatus.IDLE);
				break;
			case CREATING_DOT:
				new StartFormulaNode(x, y, this);
				setStatus(CanvasStatus.IDLE);
				break;
			case CREATING_TEXT:
				FormulaNode formulaNode = new FormulaNode(x, y, this);
				hbFormula.setVisible(true);
				txtFormulae.setStyle("-fx-background-color: LavenderBlush;");
				txtFormulae.requestFocus();
				txtFormulae.setOnKeyPressed((eventPressed) -> {
					if (eventPressed.getCode() == KeyCode.ENTER) {

						formulaNode.setText(txtFormulae.getText());
						txtFormulae.setText("");
						hbFormula.setVisible(false);
						setStatus(CanvasStatus.IDLE);
					}

				});
				break;
			default:
				throw new AssertionError(status.name());

		}
	}

	@FXML
	void handleTextAction(ActionEvent event) {
		setStatus(CanvasStatus.CREATING_TEXT);
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
			case CREATING_TEXT:
				rootPane.setCursor(Cursor.TEXT);
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

	public FormulaShape getConnectingShape() {
		return connectingShape;
	}

	public void setConnectingShape(FormulaShape connectingShape) {
		this.connectingShape = connectingShape;
	}

	public void setConnectingLine(PolygonalChain line) {
		this.connectingLine = line;
	}

	public PolygonalChain getConnectingLine() {
		return connectingLine;
	}

	public Pane getCanvas() {
		return canvas;
	}

}