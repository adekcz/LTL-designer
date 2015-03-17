/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author adekcz
 */
public class LineGrabPoint extends FormulaShape<Circle> {

	private LineGrabPoint(double x, double y, CanvasController controller) {
		super(new Circle(x, y, 10, Color.BLACK), controller);

			getShape().setOnMouseDragged((eventDragged) -> {
			if (eventDragged.isPrimaryButtonDown()) {
				double x1 = eventDragged.getX();
				double y1 = eventDragged.getY();
				moveTo(x1, y1);
			}

		});
	}
	public static LineGrabPoint createLineGrabPoint(double x, double y, CanvasController controller){
		LineGrabPoint created = new LineGrabPoint(x, y, controller);
		created.init();
		return created;
	}

	public void init(){
		PolygonalChain inLine = getController().getConnectingLine();
		inLine.setEnd(this);
		addToInEdges(inLine);

		PolygonalChain outLine = new PolygonalChain(this);
		addToOutEdges(outLine);
		getController().getCanvas().getChildren().add(outLine.getShape());
		getController().setConnectingLine(outLine);
		getController().setConnectingShape(this);
	}

	//TODO solve overridable method call in constructor. make static methods? make init methdos that requrire user invokation?
	@Override
	public final void moveTo(double x, double y) {
		//TODO create method for moving circles
		moveLinesTo(x, y);
		getShape().setCenterX(x);
		getShape().setCenterY(y);
	}
	@Override
	public final void moveBy(double deltaX, double detlaY) {
		//TODO create method for moving circles
		moveLinesBy(deltaX, detlaY);
		getShape().setCenterX(deltaX+getShape().getCenterX());
		getShape().setCenterY(detlaY+getShape().getCenterY());
	}

	@Override
	public final double getX() {
		return getShape().getCenterX();
	}
	@Override
	public final double getY() {
		return getShape().getCenterY();
	}
}
