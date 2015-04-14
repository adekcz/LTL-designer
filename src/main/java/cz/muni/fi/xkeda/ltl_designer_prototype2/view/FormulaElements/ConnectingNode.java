/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * class representing "dot". Start point for formulas and subformulas.
 *
 * @author adekcz
 */
public class ConnectingNode extends AbstractNode<Circle> {

	public enum Type {

		StartPoint, GrabPoint
	}

	private Type type;

	/**
	 * Construtctor that only initializes values, not behaviour of
	 * interactions with canvas
	 */
	public ConnectingNode(double x, double y, Type type) {
		this(x, y, null, type);
	}

	protected ConnectingNode(double x, double y, CanvasController canvasController, Type type) {
		super(new Circle(x, y, 10), canvasController);
		this.type = type;
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
	public final double getRepresentativeX() {
		return getShape().getCenterX();
	}

	@Override
	public final double getRepresentativeY() {
		return getShape().getCenterY();
	}

	@Override
	public void setDefaultFill() {
		switch (type) {
			case GrabPoint:
				getShape().setFill(Color.web(Settings.get(Settings.GRAB_POINT_COLOR)));
				break;
			case StartPoint:
				getShape().setFill(Color.web(Settings.get(Settings.START_POINT_COLOR)));
				break;
		}
	}

	@Override
	public void delete() {
		if (type == Type.GrabPoint) {
			AbstractNode start = getInEdge().getStart();
			AbstractNode end = getOutEdge().getEnd();
			super.delete();
			if (start != null && end != null) {
				start.connectTo(end);
			}
		}
		if (type == Type.StartPoint) {
			super.delete();
		}
	}

	public Type getType() {
		return type;
	}
}
