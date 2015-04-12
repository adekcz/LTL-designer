/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasStatus;
import java.util.ArrayList;
import java.util.List;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 * class that should be used as parent for all graphical formulae subclasses.
 * provides basic operations and sets "interface" for other classes.
 *
 * @author adekcz
 * @param <E> Underlying Shape that is somehow main graphical shape in specific
 * subclass;
 */
public abstract class AbstractNode<E extends Shape> {

	protected static void handleClickForLineCreation(CanvasController canvasController, AbstractNode node) {
		if (canvasController.getStatus() == CanvasStatus.CONNECTING_FORMULAE) {
			if (canvasController.getConnectingLine() == null) {
				PolygonalChain line = FormulaShapeFactory.createPolygonalChain(node);
				node.setOutEdge(line);
				canvasController.getCanvas().getChildren().add(line.getShape());
				canvasController.setConnectingLine(line);
			}
		}
	}

	protected static void handleClickForLineConnection(CanvasController canvasController, AbstractNode node) {
		if (canvasController.getStatus() == CanvasStatus.CONNECTING_FORMULAE) {
			if (canvasController.getConnectingLine() != null && !canvasController.getConnectingShape().equals(node)) {
				PolygonalChain inEdge = canvasController.getConnectingLine();
				inEdge.getStart().connectTo(node);
				canvasController.getCanvas().getChildren().remove(inEdge.getShape());
				canvasController.add(inEdge.getStart().getOutEdge().getShape());
				canvasController.setConnectingLine(null);
				canvasController.setStatus(CanvasStatus.IDLE);
			}
		}
	}

	private PolygonalChain outEdge;
	private List<PolygonalChain> inEdges;
	private CanvasController controller;
	private boolean isSelected = false;
	private int index = 0;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isIsSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		if (isSelected) {
			shape.setFill(Color.web(Settings.get(Settings.SELECTED_COLOR)));
		} else {
			setDefaultFill();
		}
		this.isSelected = isSelected;
	}
	//in more complicated formulas thiss will probably be group of different elements
	private E shape;
	protected Point2D lastPosition;

	public AbstractNode() {
		inEdges = new ArrayList<>();
	}

	protected AbstractNode(E shape, CanvasController controller) {
		inEdges = new ArrayList<>();
		this.shape = shape;
		this.controller = controller;
	}

	public void setupHandlers() {
		System.out.println("default handlers");
		setDefaultFill();
		shape.setOnMouseClicked(clickOnShape());
		shape.setOnMousePressed(pressOnShape());
		shape.setOnMouseReleased(releasedOnShape());
		shape.setOnMouseDragged(dragOnShape());
		shape.setOnMouseEntered(setHandCursorHandler());

	}

	private EventHandler<MouseEvent> setHandCursorHandler() {
		return (MouseEvent mouseEvent) -> {
			shape.setCursor(Cursor.HAND);

		};
	}

	private EventHandler<MouseEvent> releasedOnShape() {
		return (MouseEvent mouseEvent) -> {
			System.out.println("RELEASED");
			shape.setCursor(Cursor.HAND);
			if (!controller.containsSelected(this)) {
				setSelected(false);
			}
		};
	}

	private EventHandler<MouseEvent> dragOnShape() {
		return (MouseEvent mouseEvent) -> {
			double deltaX = mouseEvent.getX() - lastPosition.getX();
			double deltaY = mouseEvent.getY() - lastPosition.getY();
			lastPosition = new Point2D(mouseEvent.getX(), mouseEvent.getY());

			controller.moveSelectedBy(deltaX, deltaY, this);
		};
	}

	private EventHandler<MouseEvent> pressOnShape() {
		return (MouseEvent mouseEvent) -> {
			System.out.println("PRESSED");
			setSelected(true);
			if (mouseEvent.isControlDown()) {
				controller.addSelected(this);
			}
			// record a delta distance for the drag and drop operation.
			double x = mouseEvent.getX();
			double y = mouseEvent.getY();
			lastPosition = new Point2D(x, y);
			shape.setCursor(Cursor.MOVE);
		};
	}

	private EventHandler<MouseEvent> clickOnShape() {
		return (MouseEvent eventMouse) -> {
			System.out.println("OnMouseClicked");
			//TODO passing "this" to method in superclass really smells.
			handleClickForLineCreation(controller, this);
			handleClickForLineConnection(controller, this);
		};
	}

	public CanvasController getController() {
		return controller;
	}

	public void setController(CanvasController controller) {
		this.controller = controller;
	}

	public void setupGUIinteractions() {
		setupHandlers();
		getController().addToAll(this);
		getController().add(this.getShape());
	}

	/**
	 *
	 * @return
	 */
	public E getShape() {
		return shape;
	}

	/**
	 * @param shape Sets main underlying Shape
	 */
	public void setShape(E shape) {
		this.shape = shape;
	}

	/**
	 * Moves to x and y coordinate. Also moves with all lines that are
	 * connected to this (with line-end that is connected to this).
	 *
	 * @param x where to move
	 * @param y where to move
	 */
	protected void moveLinesTo(double x, double y) {
		for (PolygonalChain myLine : inEdges) {
			Line line = myLine.getShape();
			line.setEndX(x);
			line.setEndY(y);
		}

		if (outEdge != null) {
			Line line = outEdge.getShape();
			line.setStartX(x);
			line.setStartY(y);
		}
	}

	protected void moveLinesBy(double deltaX, double deltaY) {
		moveInEdges(deltaX, deltaY);
		moveOutEdge(deltaX, deltaY);
	}

	private void moveOutEdge(double deltaX, double deltaY) {
		if (outEdge != null) {
			Line line = outEdge.getShape();
			//line.setLayoutX(deltaX);
			//line.setLayoutY(deltaY);
			line.setStartX(deltaX + line.getStartX());
			line.setStartY(deltaY + line.getStartY());
		}
	}

	private void moveInEdges(double deltaX, double deltaY) {
		for (PolygonalChain myLine : inEdges) {
			Line line = myLine.getShape();
			//line.setLayoutX(deltaX);
			//line.setLayoutY(deltaY);
			line.setEndX(deltaX + line.getEndX());
			line.setEndY(deltaY + line.getEndY());
		}
	}

	/**
	 * Where to move.
	 *
	 * @param x coordinate x
	 * @param y coordinate y
	 */
	public abstract void moveTo(double x, double y);

	public abstract void moveBy(double deltaX, double deltaY);

	/**
	 * @return returns X coordinate for this element. Should be somehow
	 * reasonable (e.g. center for circle)
	 */
	public abstract double getRepresentativeX();

	/**
	 * @return returns Y coordinate for this element. Should be somehow
	 * reasonable (e.g. center for circle)
	 */
	public abstract double getRepresentativeY();

	public abstract void setDefaultFill();

	public void connectTo(AbstractNode other) {
		PolygonalChain line = FormulaShapeFactory.createPolygonalChain(this, other);
		setOutEdge(line);
		other.addToInEdges(line);
	}

	/**
	 * @param line Sets line as only outgoing edge from this node.
	 */
	public void setOutEdge(PolygonalChain line) {
		this.outEdge = line;
	}

	public PolygonalChain getOutEdge() {
		return outEdge;
	}

	/**
	 * @param line Adds line to outgoing edges. If line is null, nothing
	 * happens;
	 *
	 */
	public void addToInEdges(PolygonalChain line) {
		if (line != null) {
			this.inEdges.add(line);
			//line.setEnd(this);
		}
	}

}
