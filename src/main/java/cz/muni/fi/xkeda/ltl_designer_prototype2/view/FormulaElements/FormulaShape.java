/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasStatus;
import java.util.ArrayList;
import java.util.List;
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
public abstract class FormulaShape<E extends Shape> {

	protected static void handleClickForLineCreation(CanvasController canvasController, FormulaShape node) {
		if (canvasController.getStatus() == CanvasStatus.CONNECTING_FORMULAE) {
			if (canvasController.getConnectingLine() == null) {
				PolygonalChain line = new PolygonalChain(node);
				node.addToOutEdges(line);
				canvasController.getCanvas().getChildren().add(line.getShape());
				canvasController.setConnectingLine(line);
				canvasController.setConnectingShape(node);
			}
		}
	}

	protected static void handleClickForLineConnection(CanvasController canvasController, FormulaShape node) {
		if (canvasController.getStatus() == CanvasStatus.CONNECTING_FORMULAE) {
			if (canvasController.getConnectingLine() != null && !canvasController.getConnectingShape().equals(node)) {
				PolygonalChain inEdge = canvasController.getConnectingLine();
				node.addToInEdges(inEdge);
				inEdge.setEnd(node);
				canvasController.setConnectingLine(null);
				canvasController.setConnectingShape(null);
				canvasController.setStatus(CanvasStatus.IDLE);
			}
		}
	}

	private List<PolygonalChain> outEdges;
	private List<PolygonalChain> inEdges;
	private CanvasController controller;
	private boolean  isSelected = false;
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

	public void setIsSelected(boolean isSelected) {
		if(isSelected){
			shape.setFill(Color.YELLOW);
		} else{
			shape.setFill(Color.GREEN);
		}
		this.isSelected = isSelected;
	}
	//in more complicated formulas thiss will probably be group of different elements
	private E shape;
	protected Point2D lastPosition;

	public FormulaShape() {
		outEdges = new ArrayList<>();
		inEdges = new ArrayList<>();
	}

	 
	protected FormulaShape(E shape, CanvasController controller) {
		this();
		this.shape = shape;
		this.controller = controller;
		this.controller.add(shape);

		//TODO possible prone to be easily overwritten
		shape.setFill(Color.GREEN);
		
	}
	public void setupHandlers(){
		shape.setOnMouseClicked((MouseEvent eventMouse) -> {
			System.out.println("OnMouseClicked");
			//TODO passing "this" to method in superclass really smells.
			handleClickForLineCreation(controller, this);
			handleClickForLineConnection(controller, this);
		});
		shape.setOnMousePressed((MouseEvent mouseEvent) -> {
			if(mouseEvent.isControlDown()){
				controller.addSelected(this);
			}
			System.out.println("Potential drag Started");
			// record a delta distance for the drag and drop operation.
			double x = mouseEvent.getX();
			double y = mouseEvent.getY();
			lastPosition = new Point2D(x, y);
			shape.setCursor(Cursor.MOVE);
		});
		shape.setOnMouseReleased((MouseEvent mouseEvent) -> {
			shape.setCursor(Cursor.HAND);
		});
		shape.setOnMouseDragged((MouseEvent mouseEvent) -> {
			System.out.println("handflajds");
			
			double deltaX = mouseEvent.getX() - lastPosition.getX();
			double deltaY = mouseEvent.getY() - lastPosition.getY();
			lastPosition = new Point2D(mouseEvent.getX(), mouseEvent.getY());
			controller.moveSelectedBy(deltaX, deltaY);
		});
		shape.setOnMouseEntered((MouseEvent mouseEvent) -> {
			shape.setCursor(Cursor.HAND);
		});

	}

	public CanvasController getController() {
		return controller;
	}

	public void setController(CanvasController controller) {
		this.controller = controller;
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

		for (PolygonalChain myLine : outEdges) {
			Line line = myLine.getShape();
			line.setStartX(x);
			line.setStartY(y);
		}
	}

	protected void moveLinesBy(double deltaX, double deltaY) {
		for (PolygonalChain myLine : inEdges) {
			System.out.println("moving inEdges -- ends");
			Line line = myLine.getShape();
			//line.setLayoutX(deltaX);
			//line.setLayoutY(deltaY);
			line.setEndX(deltaX + line.getEndX());
			line.setEndY(deltaY + line.getEndY());
		}

		for (PolygonalChain myLine : outEdges) {
			System.out.println("moving outEdges -- starts");
			Line line = myLine.getShape();
			//line.setLayoutX(deltaX);
			//line.setLayoutY(deltaY);
			line.setStartX(deltaX + line.getStartX());
			line.setStartY(deltaY + line.getStartY());
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
	public abstract double getX();

	/**
	 * @return returns Y coordinate for this element. Should be somehow
	 * reasonable (e.g. center for circle)
	 */
	public abstract double getY();

	/**
	 * @param line Adds line to outgoing edges. If line is null, nothing
	 * happens;
	 */
	public void addToOutEdges(PolygonalChain line) {
		if (line != null) {
			this.outEdges.add(line);
			//line.setStart(this);
		}
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
