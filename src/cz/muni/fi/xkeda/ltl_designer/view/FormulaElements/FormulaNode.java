/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer.view.CanvasController;
import cz.muni.fi.xkeda.ltl_designer.view.CanvasStatus;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

/**
 *
 * @author adekcz
 */
public class FormulaNode extends FormulaShape<Rectangle> {

	private CanvasController canvasController;
	private Text text;
	//possible more than one element
	private FormulaShape parent;
	private FormulaShape child;

	public FormulaNode(double x, double y, CanvasController canvasController) {
		setShape(new Rectangle(x, y, 100, 30));
		this.canvasController = canvasController;
		canvasController.add(getShape());

		getShape().setFill(Color.GREEN);
		getShape().setOnMouseClicked((MouseEvent eventMouse) -> {
			getShape().setFill(Color.RED);
			getShape().setStroke(Color.GREEN);
			if (canvasController.getStatus() == CanvasStatus.CONNECTING_FORMULAE) {
				if (canvasController.getConnectingLine() == null) {
					MyLine line = new MyLine(FormulaNode.this);
					addToOutEdges(line);
					canvasController.getCanvas().getChildren().add(line.getShape());
					canvasController.setConnectingLine(line);
					canvasController.setConnectingShape(FormulaNode.this);
				}
			}
			if (canvasController.getStatus() == CanvasStatus.CONNECTING_FORMULAE) {
				if (canvasController.getConnectingLine() != null && !canvasController.getConnectingShape().equals(FormulaNode.this)) {
					MyLine connectingLine1 = canvasController.getConnectingLine();
					addToInEdges(connectingLine1);
					connectingLine1.setEnd(FormulaNode.this);
					canvasController.setConnectingLine(null);
					canvasController.setConnectingShape(null);
					canvasController.setStatus(CanvasStatus.IDLE);
				}
			}
		});

		getShape().setOnMouseDragged((eventDragged) -> {
			if (eventDragged.isPrimaryButtonDown()) {
				double x1 = eventDragged.getX();
				double y1 = eventDragged.getY();
				moveTo(x1, y1);
			} 


		});
	}

	public FormulaShape getChild() {
		return child;
	}


	public FormulaShape getMyParent() {
		return parent;
	}

	public void setMyParent(FormulaShape parent) {
		this.parent = parent;
	}


	@Override
	public double getCenterX() {
		return getShape().getX() + getShape().getWidth()/2;
	}

	@Override
	public double getCenterY() {
		return getShape().getY() + getShape().getHeight()/2;
	}

	@Override
	public void moveTo(double x, double y) {
		moveLines(x, y);
		getShape().setX(x); 
		getShape().setY(y);
	}
}
