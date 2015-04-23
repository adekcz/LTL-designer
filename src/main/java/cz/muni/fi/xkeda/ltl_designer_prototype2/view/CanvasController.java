/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view;

import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JsonHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.ResourcesHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.AbstractNode;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.ConnectingNode;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.FormulaShapeFactory;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.PolygonalChain;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.TextNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
	private TextField txtInputFormula;
	@FXML
	private HBox hbFormula;

	private CanvasState state;

	private PolygonalChain connectingLine;
	private List<AbstractNode> selectedNodes;
	private List<AbstractNode> allNodesNotEmbedded;

	private Point2D lastPosition;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		selectedNodes = new ArrayList<>();
		allNodesNotEmbedded = new ArrayList<>();
		state = CanvasState.IDLE;

		// EXPERMINETAL AREA
		canvas.setFocusTraversable(true);
		canvas.requestFocus();

		setupHandlers();
	}

	private void setupHandlers() {
		canvas.setOnMouseMoved(handleCanvasMouseMoved());
		canvas.setOnDragOver(handleCanvasDragOver());
		canvas.setOnDragDropped(handleCanvasDragDropped());
	}

	private EventHandler<? super MouseEvent> handleCanvasMouseMoved() {
		return (eventMoved) -> {
			if (state == CanvasState.CONNECTING_FORMULAS && connectingLine != null) {
				/* -2 because clicking on element does not propagate throught line if line is directly under cursor
				 OPTIONALY try to find better solution */
				connectingLine.getShape().setEndX(eventMoved.getX() - 2);
				connectingLine.getShape().setEndY(eventMoved.getY() - 2);
			}
		};
	}

	private EventHandler<DragEvent> handleCanvasDragOver() {
		return (DragEvent event) -> {
			if (event.getGestureSource() != canvas
				&& event.getDragboard().hasContent(ListFileCell.fileDragFormat)) {
				event.acceptTransferModes(TransferMode.ANY);
			}
			event.consume();
		};
	}

	private EventHandler<DragEvent> handleCanvasDragDropped() {
		return (DragEvent event) -> {
			double x = event.getX();
			double y = event.getY();

			renderFromJsonFile((File) event.getDragboard().getContent(ListFileCell.fileDragFormat));
			event.setDropCompleted(true);
			event.consume();
		};
	}

	private void renderFromJsonFile(File draggedFile) {
		try {
			Map<Integer, AbstractNode> loadJson = JsonHelper.loadJson(draggedFile);
			addToControler(loadJson);
		} catch (FileNotFoundException ex) {
			Logger.getLogger(CanvasController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@FXML
	void handleKeyPressed(KeyEvent event) {
		if (event.getCode() == KeyCode.DELETE) {
			removeAllSelected();
		}
	}

	@FXML
	void handleDeleteSelected(MouseEvent event) {
		removeAllSelected();
	}

	@FXML
	void handleCanvasDrag(MouseEvent mouseEvent) {
		System.out.println("canvas drag");
		if (!mouseEvent.getTarget().equals(canvas)) {
			return;
		}
		double deltaX = mouseEvent.getX() - lastPosition.getX();
		double deltaY = mouseEvent.getY() - lastPosition.getY();
		lastPosition = new Point2D(mouseEvent.getX(), mouseEvent.getY());

		moveAll(deltaX, deltaY);
		mouseEvent.consume();
	}

	@FXML
	void handleCanvasPress(MouseEvent event) {
		System.out.println("press");
		rootPane.setCursor(Cursor.CLOSED_HAND);
		lastPosition = new Point2D(event.getX(), event.getY());
	}

	@FXML
	void handleCanvasRelease(MouseEvent event) {
		System.out.println("released");
		resetCursor();
	}

	@FXML
	void handleTextAction(ActionEvent event) {
		setStatus(CanvasState.CREATING_TEXT);
	}

	@FXML
	void handleNodeAction(ActionEvent event) {
		setStatus(CanvasState.CREATING_NEW_ELEMENT);
	}

	@FXML
	void handleStartAction(ActionEvent event) {
		setStatus(CanvasState.CREATING_DOT);
	}

	@FXML
	void handleArrowAction(ActionEvent event) {
		setStatus(CanvasState.CONNECTING_FORMULAS);
	}

	@FXML
	void handleSelfLoopClicked(ActionEvent event) {
		setStatus(CanvasState.CREATING_SELF_LOOP);
	}

	@FXML
	void handleCanvasClick(MouseEvent event) {
		canvas.requestFocus();
		double x = event.getX();
		double y = event.getY();
		switch (state) {
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
				setStatus(CanvasState.IDLE);
				break;
			case CREATING_DOT:
				FormulaShapeFactory.createStartFormulaNode(x, y, this);
				setStatus(CanvasState.IDLE);
				break;
			case CREATING_TEXT:
				addTextNode(x, y);
				break;
			case DRAGGING_SAVED_FORMULA:
			case CREATING_SELF_LOOP:
				setStatus(CanvasState.IDLE);
				break;
			default:
				throw new AssertionError(state.name());
		}
	}

	private boolean isClickedIntoEmptySpace(double x, double y) {
		//todo give this rething. I think that there was reason for this. Give explanation when you figure it out.
		//log entry 2: and figure out what you meant by that comment
		return getConnectingShape() != null && !isClickedInside(x, y, getConnectingShape());
	}

	private boolean isClickedInside(double x, double y, AbstractNode node) {
		return node.getShape().intersects(x, y, 2, 2);
	}

	private void addGrabPoint(double x, double y) {
		ConnectingNode grabPoiont = FormulaShapeFactory.createLineGrabPoint(x, y, this);
		AbstractNode start = getConnectingLine().getStart();
		start.connectGraphicallyTo(grabPoiont);
		removeCompletely(getConnectingLine());

		createConnectingLine(grabPoiont);
	}

	private void addTextNode(double x, double y) {
		TextNode formulaNode = FormulaShapeFactory.createFormulaNode(x, y, this);
		hbFormula.setVisible(true);
		txtInputFormula.setStyle("-fx-background-color: LavenderBlush;");
		txtInputFormula.requestFocus();
		txtInputFormula.setOnKeyPressed(handleTxtFormulaEnterPressed(formulaNode));
	}

	private EventHandler<? super KeyEvent> handleTxtFormulaEnterPressed(TextNode formulaNode) {
		return (eventPressed) -> {
			if (eventPressed.getCode() == KeyCode.ENTER) {
				formulaNode.changeText(txtInputFormula.getText());
				txtInputFormula.setText("");
				hbFormula.setVisible(false);
				setStatus(CanvasState.IDLE);
			}

		};
	}

	public void setStatus(CanvasState status) {
		this.state = status;
		switch (status) {
			case IDLE: {
				resetCursor();
				break;
			}
			case CREATING_DOT:
				rootPane.setCursor(Cursor.cursor(ResourcesHelper.getResourceAsString("/images/Cursor3.png")));
				break;
			case CREATING_TEXT:
				rootPane.setCursor(Cursor.TEXT);
				break;
			case CREATING_NEW_ELEMENT:
			case DRAGGING_SAVED_FORMULA:
				rootPane.setCursor(Cursor.HAND);
				break;
			case CONNECTING_FORMULAS:
			case CREATING_SELF_LOOP:
				rootPane.setCursor(Cursor.CROSSHAIR);
				break;
			default:
				throw new AssertionError(status.name());
		}

	}

	private void resetCursor() {
		rootPane.setCursor(Cursor.DEFAULT);
	}

	/**
	 * Moves all nodes that were previously selected
	 *
	 * @param current node on which move action originated
	 */
	public void moveSelectedBy(double deltaX, double deltaY, AbstractNode current) {
		if (!selectedNodes.contains(current)) {
			current.moveBy(deltaX, deltaY);
		}
		moveSelectedBy(deltaX, deltaY);
	}

	private void moveSelectedBy(double deltaX, double deltaY) {
		for (AbstractNode shape : selectedNodes) {
			shape.moveBy(deltaX, deltaY);
		}
	}

	private void moveAll(double deltaX, double deltaY) {
		for (AbstractNode shape : allNodesNotEmbedded) {
			shape.moveBy(deltaX, deltaY);
		}
	}

	public boolean containsSelected(AbstractNode shape) {
		return selectedNodes.contains(shape);
	}

	public void deselectAll() {
		for (AbstractNode selectedNode : selectedNodes) {
			selectedNode.changeSelected(false);
		}
		selectedNodes.clear();
	}

	public void addSelected(AbstractNode shape) {
		selectedNodes.add(shape);
	}

	public void addGraphicToCanvas(Shape shape) {
		canvas.getChildren().add(shape);
	}

	/**
	 * do not addGraphicToCanvas same element twice //todo enforce in code
	 *
	 * @param shape
	 */
	public void addToAll(AbstractNode shape) {
		allNodesNotEmbedded.add(shape);
	}

	public List<AbstractNode> getAllNodes() {
		return Collections.unmodifiableList(allNodesNotEmbedded);
	}

	public void addToControler(Map<Integer, AbstractNode> nodes) {
		for (AbstractNode aNode : nodes.values()) {
			aNode.setController(this);
			aNode.setupGUIinteractions();
			//TODO why is not this inside setupGUIInteractions?
			if (aNode.getOutEdge() != null) {
				addGraphicToCanvas(aNode.getOutEdge().getShape());
				aNode.getOutEdge().setController(this);
				aNode.getOutEdge().getShape().toBack();
			}
		}
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

	public void removeFromCanvas(Shape shape) {
		canvas.getChildren().remove(shape);
	}

	public CanvasState getStatus() {
		return state;
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

	public void createConnectingLine(AbstractNode start) {
		PolygonalChain line = FormulaShapeFactory.createPolygonalChain(start);
		getCanvas().getChildren().add(line.getShape());
		setConnectingLine(line);
	}

}
