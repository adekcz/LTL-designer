/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer.view.CanvasController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author adekcz
 */
public class LineGrabPoint extends FormulaShape<Circle> {

	public LineGrabPoint(double x, double y, CanvasController controller) {
		super(new Circle(x, y, 10, Color.BLACK), controller);
		PolygonalChain connectingLine1 = controller.getConnectingLine();
		connectingLine1.setEnd(this);
		addToInEdges(connectingLine1);

		PolygonalChain polygonalChain = new PolygonalChain(this);
		controller.getCanvas().getChildren().add(polygonalChain.getShape());
		controller.setConnectingLine(polygonalChain);
		controller.setConnectingShape(this);

			getShape().setOnMouseDragged((eventDragged) -> {
			if (eventDragged.isPrimaryButtonDown()) {
				double x1 = eventDragged.getX();
				double y1 = eventDragged.getY();
				moveTo(x1, y1);
			}

		});
	}

	@Override
	public void moveTo(double x, double y) {
		//TODO create method for moving circles
		moveLines(x, y);
		getShape().setCenterX(x);
		getShape().setCenterY(y);
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
