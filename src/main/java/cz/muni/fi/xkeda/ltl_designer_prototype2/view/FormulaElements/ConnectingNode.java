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
 * class representing "dot". Start point for formulae and subformulae.
 *
 * @author adekcz
 */
public class ConnectingNode extends AbstractNode<Circle> {

	/**
	 * Construtctor that only initializes values, not behaviour of
	 * interactions with canvas
	 *
	 * @param x
	 * @param y
	 */
	public ConnectingNode(double x, double y) {
		this(x, y, null);
	}

	protected ConnectingNode(double x, double y, CanvasController canvasController) {
		super(new Circle(x, y, 10, Color.PURPLE), canvasController);
	}


	@Override
	public final void moveTo(double x, double y) {
		moveLinesTo(x, y);
		getShape().setCenterX(x);
		getShape().setCenterY(y);
	}

	@Override
	public final void moveBy(double deltaX, double detlaY) {
		moveLinesBy(deltaX, detlaY);
		getShape().setCenterX(deltaX + getShape().getCenterX());
		getShape().setCenterY(detlaY + getShape().getCenterY());
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
