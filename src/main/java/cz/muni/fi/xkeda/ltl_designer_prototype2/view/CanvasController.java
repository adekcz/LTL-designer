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
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

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
	private TextField txtInputFormula;
	@FXML
	private HBox hbFormula;

	private CanvasStatus status;

	private PolygonalChain connectingLine;
	private List<AbstractNode> selectedNodes;
	private List<AbstractNode> allNodesNotEmbedded;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// allow the label to be dragged around.

		selectedNodes = new ArrayList<>();
		allNodesNotEmbedded = new ArrayList<>();
		status = CanvasStatus.IDLE;
		canvas.setOnMouseMoved((eventMoved) -> {
			if (status == CanvasStatus.CONNECTING_FORMULAS && connectingLine != null) {
				//-2 because clicking on element does not propagate throught line if line is directly under cursor... OPTIONALY try to find better solution
				connectingLine.getShape().setEndX(eventMoved.getX() - 2);
				connectingLine.getShape().setEndY(eventMoved.getY() - 2);
			}
		});

		// EXPERMINETAL AREA
		canvas.requestFocus();
		canvas.setFocusTraversable(true);
		//you can delete everything up to end of method
		canvas.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
		canvas.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
			System.out.println("filter: " + event.getCode());
		});
		canvas.setOnKeyPressed((event) -> {
			System.out.println("Pressed: " + event.getCode());
		});

		canvas.setScaleX(0.9);
		canvas.setScaleY(0.9);

		canvas.setOnDragOver(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				System.out.println("Handling dragOver");
				/* data is dragged over the target */
				/* accept it only if it is not dragged from the same node 
				 * and if it has a string data */
				if (event.getGestureSource() != canvas
					&& event.getDragboard().hasString()) {
					/* allow for both copying and moving, whatever user chooses */
					event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
				}
				//double x = event.getX();
				//double y = event.getY();
//
				//Rectangle rectangle = new Rectangle(x, y, 10, 20);
				//rectangle.setFill(Color.web(event.getDragboard().getString()));
				//add(rectangle);

				event.consume();
			}
		});
		canvas.setOnDragDropped(new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				System.out.println("Handling DROPPED");
				/* data is dragged over the target */
				/* accept it only if it is not dragged from the same node 
				 * and if it has a string data */
				double x = event.getX();
				double y = event.getY();

				BigInteger p = (BigInteger) event.getDragboard().getContent(Zxxxxxxxxx.dataformat);
				Text droppedText = new Text(x, y, event.getDragboard().getString());
				add(droppedText);
				event.setDropCompleted(true);
				event.consume();
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
	void handleKeyTyped(KeyEvent event) {
		System.out.println("event keyTyped" + event.getCode());
		if (event.getCode() == KeyCode.DELETE) {
			removeAllSelected();
		}
	}

	@FXML
	void handleDeleteSelected(MouseEvent event) {
		removeAllSelected();
	}

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
			case CONNECTING_FORMULAS:
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
		txtInputFormula.setStyle("-fx-background-color: LavenderBlush;");
		txtInputFormula.requestFocus();
		txtInputFormula.setOnKeyPressed(formulaEnterPressed(formulaNode));
	}

	private EventHandler<? super KeyEvent> formulaEnterPressed(TextNode formulaNode) {
		return (eventPressed) -> {
			if (eventPressed.getCode() == KeyCode.ENTER) {
				formulaNode.changeText(txtInputFormula.getText());
				txtInputFormula.setText("");
				hbFormula.setVisible(false);
				setStatus(CanvasStatus.IDLE);
			}

		};
	}

	private void addGrabPoint(double x, double y) {
		ConnectingNode grabPoiont = FormulaShapeFactory.createLineGrabPoint(x, y, this);
		AbstractNode start = getConnectingLine().getStart();
		start.connectTo(grabPoiont);
		removeCompletely(getConnectingLine());

		addConnectingLine(grabPoiont);
	}
//		ConnectingNode grabPoiont = FormulaShapeFactory.createLineGrabPoint(x, y, this);
//		PolygonalChain inLine = getConnectingLine();
//		inLine.setEnd(grabPoiont);
//		grabPoiont.addToInEdges(inLine);
//
//		PolygonalChain outLine = FormulaShapeFactory.createPolygonalChain(grabPoiont);
//		grabPoiont.setOutEdge(outLine);
//		getCanvas().getChildren().add(outLine.getShape());
//		setConnectingLine(outLine);

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
		setStatus(CanvasStatus.CONNECTING_FORMULAS);
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
			case CONNECTING_FORMULAS:
				rootPane.setCursor(Cursor.CROSSHAIR);
				break;
			case CREATING_NEW_ELEMENT:
				rootPane.setCursor(Cursor.HAND);
				break;
			case CREATING_DOT:
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

	public void moveSelectedBy(double deltaX, double deltaY, AbstractNode current) {
		if (!selectedNodes.contains(current)) {
			current.moveBy(deltaX, deltaY);
		}
		moveSelectedBy(deltaX, deltaY);
	}

   //###     ######   ######  ########  ######   ######   #######  ########   ######  
	//## ##   ##    ## ##    ## ##       ##    ## ##    ## ##     ## ##     ## ##    ## 
	//##   ##  ##       ##       ##       ##       ##       ##     ## ##     ## ##       
//##     ## ##       ##       ######    ######   ######  ##     ## ########   ######  
//######### ##       ##       ##             ##       ## ##     ## ##   ##         ## 
//##     ## ##    ## ##    ## ##       ##    ## ##    ## ##     ## ##    ##  ##    ## 
//##     ##  ######   ######  ########  ######   ######   #######  ##     ##  ######  
	public void addSelected(AbstractNode shape) {
		selectedNodes.add(shape);
	}

	public boolean containsSelected(AbstractNode shape) {
		return selectedNodes.contains(shape);
	}

	public void deselectAll() {
		for (AbstractNode selectedNode : selectedNodes) {
			selectedNode.setSelected(false);
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
		if (connectingLine != null) {
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
		allNodesNotEmbedded.add(shape);
	}

	public List<AbstractNode> getAllNodes() {
		return Collections.unmodifiableList(allNodesNotEmbedded);
	}

	public void removeFromAllNodes(AbstractNode node) {
		allNodesNotEmbedded.remove(node);
	}

	/**
	 * from canvas and from internal lists
	 *
	 * @param node
	 */
	public void removeCompletely(AbstractNode node) {
		if (node != null) {
			canvas.getChildren().remove(node.getShape());
			allNodesNotEmbedded.remove(node);
			selectedNodes.remove(node);
		}
	}

	private void removeAllSelected() {
		while (!selectedNodes.isEmpty()) {
			selectedNodes.get(0).delete();
		}
	}

	public void removeFromCanvas(Text text) {
		canvas.getChildren().remove(text);
	}

	public void addConnectingLine(AbstractNode start) {
		PolygonalChain line = FormulaShapeFactory.createPolygonalChain(start);
		getCanvas().getChildren().add(line.getShape());
		setConnectingLine(line);
	}
}
