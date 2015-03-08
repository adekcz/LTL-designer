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
import javafx.scene.shape.Shape;

/**
 *
 * @author adekcz
 */
public class MyCircle extends MyShape<Circle>{
	private CanvasController canvasController;
	//possible more than one element
	private MyShape parent;
	private MyShape child;
	private MyLine connectingLine;

	public MyCircle(double x, double y, int rad, CanvasController canvasController) {
		setShape(new Circle(x, y, rad));
		this.canvasController = canvasController;
		canvasController.add(getShape());

		getShape().setFill(Color.GREEN);
		getShape().setOnMouseClicked(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent eventMouse) {
				getShape().setFill(Color.RED);
				getShape().setStroke(Color.GREEN);
				if (canvasController.getStatus() == CanvasStatus.CONNECTING_FORMULAE) {
					if (canvasController.getConnectingLine() == null) {
						MyLine line = new MyLine(MyCircle.this);
						canvasController.getCanvas().getChildren().add(line.getShape());
						canvasController.setConnectingLine(line);
						canvasController.setConnectingShape(MyCircle.this);
						connectingLine = line;
					}
				}
				if (canvasController.getStatus() == CanvasStatus.CONNECTING_FORMULAE) {
					if (canvasController.getConnectingLine() != null && !canvasController.getConnectingShape().equals(MyCircle.this)) {
						canvasController.getConnectingShape().setChild(MyCircle.this);
						connectingLine = canvasController.getConnectingLine();
						connectingLine.setEnd(MyCircle.this);
						parent = canvasController.getConnectingShape();
						canvasController.setConnectingLine(null);
						canvasController.setConnectingShape(null);
						canvasController.setStatus(CanvasStatus.IDLE);
					}
				}
			}
		});

		getShape().setOnMouseDragged((eventDragged) -> {
			if (eventDragged.isPrimaryButtonDown()) {
				double x1 = eventDragged.getX();
				double y1 = eventDragged.getY();

				getShape().setCenterX(x1);
				getShape().setCenterY(y1);
			}
			//TODO obsolete
			if (connectingLine != null) {
				double x1 = eventDragged.getX();
				double y1 = eventDragged.getY();
				if (this.equals(connectingLine.getEnd())) {
					this.connectingLine.getShape().setEndX(x1);
					this.connectingLine.getShape().setEndY(y1);
				} else{
					this.connectingLine.getShape().setStartX(x1);
					this.connectingLine.getShape().setStartY(y1);
				}

			}

		});
	}

	public MyShape getChild() {
		return child;
	}

	@Override
	public void setChild(MyShape child) {
		this.child = child;
	}

	public MyShape getMyParent() {
		return parent;
	}

	public void setMyParent(MyShape parent) {
		this.parent = parent;
	}

	public MyLine getConnectingLine() {
		return connectingLine;
	}

	public void setConnectingLine(MyLine connectingLine) {
		this.connectingLine = connectingLine;
	}


	@Override
	public double getCenterX() {
		return getShape().getCenterX();
	}

	@Override
	public double getCenterY() {
		return getShape().getCenterY();
	}

	@Override
	public void moveTo(double x, double y) {
		moveLines(x, y);
		getShape().setCenterX(x);
		getShape().setCenterY(y);
	}
}
