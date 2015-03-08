/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer.view.CanvasController;
import cz.muni.fi.xkeda.ltl_designer.view.CanvasStatus;
import java.util.ArrayList;
import java.util.List;
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
public class StartFormulaNode extends  MyShape<Circle>{

	public StartFormulaNode(double x, double y, CanvasController canvasController){
		super(new Circle(x, y, 10, Color.BLACK),canvasController);
		canvasController.add(getShape());
			getShape().setOnMouseClicked(new EventHandler<MouseEvent>() {

			public void handle(MouseEvent eventMouse) {
				getShape().setFill(Color.RED);
				getShape().setStroke(Color.GREEN);
				if (canvasController.getStatus() == CanvasStatus.CONNECTING_FORMULAE) {
					if (canvasController.getConnectingLine() == null) {
						MyLine line = new MyLine(StartFormulaNode.this);
						addToOutEdges(line);
						canvasController.getCanvas().getChildren().add(line.getShape());
						canvasController.setConnectingLine(line);
						canvasController.setConnectingShape(StartFormulaNode.this);
					}
				}
				if (canvasController.getStatus() == CanvasStatus.CONNECTING_FORMULAE) {
					if (canvasController.getConnectingLine() != null && !canvasController.getConnectingShape().equals(StartFormulaNode.this)) {
						canvasController.getConnectingShape().setChild(StartFormulaNode.this);
						MyLine connectingLine = canvasController.getConnectingLine();
						addToInEdges(connectingLine);
						connectingLine.setEnd(StartFormulaNode.this);
						canvasController.setConnectingLine(null);
						canvasController.setConnectingShape(null);
						canvasController.setStatus(CanvasStatus.IDLE);
					}
				}
				//TODO handle if clicked not nothing (possibly in some higher layer
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

	public Circle getCircle() {
		return getShape();
	}


	public final double getCenterX() {
		return getShape().getCenterX();
	}
	public final double getCenterY() {
		return getShape().getCenterY();
	}


	@Override
	public void setChild(MyShape aThis) {
		System.out.println("should set child");
	}

	@Override
	public void moveTo(double x, double y) {
		moveLines(x, y);
		getShape().setCenterX(x);
		getShape().setCenterY(y);
	}

}
