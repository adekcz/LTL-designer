/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view;

import cz.muni.fi.xkeda.ltl_designer_prototype2.util.ResourcesHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.AbstractNode;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.ConnectingNode;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.FormulaShapeFactory;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.PolygonalChain;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.TextNode;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
	private List<AbstractNode> selectedNodes;
	private List<AbstractNode> allNodes;


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// allow the label to be dragged around.

		selectedNodes = new ArrayList<>();
		allNodes = new ArrayList<>();
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
		switch (status) {
			case IDLE:
				if (event.getTarget().equals(canvas)) {
					deselectAll();
				}
				break;
			case CONNECTING_FORMULAE:
				if (isClickedIntoEmptySpace(x, y)) {
					addGrabPoint(x, y);
				}
				break;
			case CREATING_NEW_ELEMENT:
				FormulaShapeFactory.createFormulaNode(x, y, this);
				setStatus(CanvasStatus.IDLE);
				break;
			case CREATING_DOT:
				FormulaShapeFactory.createStartFormulaNode(x, y, this);
				setStatus(CanvasStatus.IDLE);
				break;
			case CREATING_TEXT:
				addTextNode(x, y);
				break;
			default:
				throw new AssertionError(status.name());
		}
	}

	private void addTextNode(double x, double y) {
		TextNode formulaNode = FormulaShapeFactory.createFormulaNode(x, y, this);
		hbFormula.setVisible(true);
		txtFormulae.setStyle("-fx-background-color: LavenderBlush;");
		txtFormulae.requestFocus();
		txtFormulae.setOnKeyPressed(formulaEnterPressed(formulaNode));
	}

	private EventHandler<? super KeyEvent> formulaEnterPressed(TextNode formulaNode) {
		return (eventPressed) -> {
			if (eventPressed.getCode() == KeyCode.ENTER) {
				formulaNode.changeText(txtFormulae.getText());
				txtFormulae.setText("");
				hbFormula.setVisible(false);
				setStatus(CanvasStatus.IDLE);
			}
			
		};
	}

	private void addGrabPoint(double x, double y) {
		ConnectingNode grabPoiont = FormulaShapeFactory.createLineGrabPoint(x, y, this);
		PolygonalChain inLine = getConnectingLine();
		inLine.setEnd(grabPoiont);
		grabPoiont.addToInEdges(inLine);
		
		PolygonalChain outLine = FormulaShapeFactory.createPolygonalChain(grabPoiont);
		grabPoiont.setOutEdge(outLine);
		getCanvas().getChildren().add(outLine.getShape());
		setConnectingLine(outLine);
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

	private boolean isClickedInside(double x, double y, AbstractNode node) {
		return node.getShape().intersects(x, y, 2, 2);
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
				System.out.println("WORKING");
				rootPane.setCursor(Cursor.cursor(ResourcesHelper.getResourceAsString("/images/Cursor3.png")));
				break;
			case CREATING_TEXT:
				rootPane.setCursor(Cursor.TEXT);
				break;
			default:
				throw new AssertionError(status.name());
		}

	}

	private void resetCursor() {
		rootPane.setCursor(Cursor.DEFAULT);
	}

	public void moveSelectedBy(double deltaX, double deltaY) {
		for (AbstractNode shape : selectedNodes) {
			shape.moveBy(deltaX, deltaY);
		}
	}

	//todo write in ascii: ACCESSORS

	public void addSelected(AbstractNode shape) {
		if (!shape.isIsSelected()) {
			shape.setIsSelected(true);
			selectedNodes.add(shape);
		}
	}

	public void deselectAll() {
		for (AbstractNode selectedNode : selectedNodes) {
			selectedNode.setIsSelected(false);
		}
		selectedNodes.clear();
	}
	private boolean isClickedIntoEmptySpace(double x, double y) {
		//todo give this rething. I think that there was reason for this. Give explanation when you figure it out.
		return getConnectingShape() != null && !isClickedInside(x, y, getConnectingShape());
	}
	public void add(Shape shape) {
		canvas.getChildren().add(shape);
	}


	public CanvasStatus getStatus() {
		return status;
	}

	/**
	 * 
	 * @return Start node in current attempt to connect two nodes.
	 */
	public AbstractNode getConnectingShape() {
		if(connectingLine != null){
			return connectingLine.getStart();
		}
		return null;
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
	/**
	 * do not add same element twice
	 *
	 * @param shape
	 */
	public void addToAll(AbstractNode shape) {
		allNodes.add(shape);
	}

	public List<AbstractNode> getAllNodes() {
		return Collections.unmodifiableList(allNodes);
	}
	public void removeFromAllNodes(AbstractNode node){
		allNodes.remove(node);
	}

}
