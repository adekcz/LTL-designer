/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.SettingsConstants;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasState;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 * class that should be used as parent for all graphical formulas subclasses. provides basic
 * operations and sets "interface" for other classes.
 *
 * @author adekcz
 * @param <E> Underlying Shape that is somehow main graphical shape in specific subclass;
 */
public abstract class AbstractNode<E extends Shape> {

	private PolygonalChain outEdge;
	private PolygonalChain inEdge;
	private CanvasController controller;
	private boolean isSelected = false;
	private int index = 0;

	//in more complicated formulas thiss will probably be group of different elements
	private E shape;
	protected Point2D lastPosition;

	protected AbstractNode(E shape, CanvasController controller) {
		this.shape = shape;
		this.controller = controller;
	}

	public void setupGUIinteractions() {
		setupHandlers();
		fillWithDefaultFill();
		getController().addToAll(this);
		getController().addGraphicToCanvas(this.getShape());
	}

	protected void setupHandlers() {
		System.out.println("default handlers");
		shape.setOnMouseClicked(clickOnShape());
		shape.setOnMousePressed(pressOnShape());
		shape.setOnMouseReleased(releasedOnShape());
		shape.setOnMouseDragged(dragOnShape());
		shape.setOnMouseEntered(setHandCursorHandler());

	}

	private EventHandler<MouseEvent> clickOnShape() {
		return (MouseEvent eventMouse) -> {
			//TODO passing "this" to method in superclass really smells.
			handleClickForLineCreation(controller, this);
			handleClickForLineConnection(controller, this);
		};
	}

	protected EventHandler<MouseEvent> pressOnShape() {
		return (MouseEvent mouseEvent) -> {
			changeSelected(true);
			if (mouseEvent.isControlDown()) {
				controller.addSelected(this);
			}
			// record a delta distance for the drag and drop operation.
			double x = mouseEvent.getX();
			double y = mouseEvent.getY();
			lastPosition = new Point2D(x, y);
			shape.setCursor(Cursor.CLOSED_HAND);
		};
	}

	protected EventHandler<MouseEvent> releasedOnShape() {
		return (MouseEvent mouseEvent) -> {
			System.out.println("RELEASED");
			shape.setCursor(Cursor.HAND);
			if (!controller.containsSelected(this)) {
				changeSelected(false);
			}
		};
	}

	private EventHandler<MouseEvent> dragOnShape() {
		return (MouseEvent mouseEvent) -> {
			double deltaX = mouseEvent.getX() - lastPosition.getX();
			double deltaY = mouseEvent.getY() - lastPosition.getY();
			lastPosition = new Point2D(mouseEvent.getX(), mouseEvent.getY());

			controller.moveSelectedBy(deltaX, deltaY, this);
			mouseEvent.consume();
		};
	}

	private EventHandler<MouseEvent> setHandCursorHandler() {
		return (MouseEvent mouseEvent) -> {
			shape.setCursor(Cursor.HAND);

		};
	}

	protected void handleClickForLineCreation(CanvasController canvasController, AbstractNode node) {
		if (canvasController.getStatus() == CanvasState.CONNECTING_FORMULAS) {
			if (canvasController.getConnectingLine() == null) {
				canvasController.createConnectingLine(node);

			}
		}
	}

	protected void handleClickForLineConnection(CanvasController canvasController, AbstractNode node) {
		if (canvasController.getStatus() == CanvasState.CONNECTING_FORMULAS) {
			if (canvasController.getConnectingLine() != null && !canvasController.getConnectingShape().equals(node)) {
				AbstractNode start = canvasController.getConnectingLine().getStart();
				start.connectGraphicallyTo(node);
				canvasController.removeCompletely(canvasController.getConnectingLine());

				canvasController.setConnectingLine(null);
				canvasController.setStatus(CanvasState.IDLE);
			}
		}
	}

	public CanvasController getController() {
		return controller;
	}

	public void setController(CanvasController controller) {
		this.controller = controller;
	}

	public E getShape() {
		return shape;
	}

	public void setShape(E shape) {
		this.shape = shape;
	}

	public abstract void moveBy(double deltaX, double deltaY);

	protected void moveLinesBy(double deltaX, double deltaY) {
		moveInEdge(deltaX, deltaY);
		moveOutEdge(deltaX, deltaY);
	}

	private void moveOutEdge(double deltaX, double deltaY) {
		if (outEdge != null) {
			Line line = outEdge.getShape();
			line.setStartX(deltaX + line.getStartX());
			line.setStartY(deltaY + line.getStartY());
		}
	}

	private void moveInEdge(double deltaX, double deltaY) {
		if (inEdge != null) {
			Line line = inEdge.getShape();
			line.setEndX(deltaX + line.getEndX());
			line.setEndY(deltaY + line.getEndY());
		}
	}

	/**
	 * @return returns X coordinate for this element. Should be somehow reasonable (e.g. center for
	 * circle)
	 */
	public abstract double getRepresentativeX();

	/**
	 * @return returns Y coordinate for this element. Should be somehow reasonable (e.g. center for
	 * circle)
	 */
	public abstract double getRepresentativeY();

	public void fillWithDefaultFill() {
		getShape().setFill(getDefaultFill());
	}

	public abstract Color getDefaultFill();

	public PolygonalChain connectGraphicallyTo(AbstractNode other) {
		PolygonalChain line = connectSymbolically(other);
		getController().addGraphicToCanvas(line.getShape());
		line.getShape().toBack();
		return line;
	}

	public PolygonalChain connectSymbolically(AbstractNode other) {
		PolygonalChain line = FormulaShapeFactory.createPolygonalChain(this, other);
		return line;
	}
	
	public void delete() {
		controller.removeCompletely(this);
		if (inEdge != null) {
			inEdge.delete();
		}
		if (outEdge != null) {
			outEdge.delete();
		}
		disconnect();
	}

	protected void disconnect() {
		outEdge = null;
		inEdge = null;
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

	public void setInEdge(PolygonalChain line) {
		this.inEdge = line;
	}

	public PolygonalChain getInEdge() {
		return inEdge;
	}


	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public boolean isSelected() {
		return isSelected;
	}

	protected void setSelected(boolean selected) {
		this.isSelected = selected;
	}

	public void changeSelected(boolean isSelected) {
		if (isSelected) {
			shape.setFill(Color.web(Settings.get(SettingsConstants.SELECTED_COLOR)));
		} else {
			fillWithDefaultFill();
		}
		setSelected(isSelected);
	}

}
