/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.SettingsConstants;
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

	public ConnectingNode(double x, double y) {
		this(x, y, null);
	}

	protected ConnectingNode(double x, double y, CanvasController canvasController) {
		super(new Circle(x, y, 10), canvasController);
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
	public Color getDefaultFill() {
		return Color.web(Settings.get(SettingsConstants.GRAB_POINT_COLOR));
	}

	@Override
	public void delete() {
		AbstractNode start = getInEdge().getStart();
		AbstractNode end = getOutEdge().getEnd();
		super.delete();
		if (start != null && end != null) {
			start.connectGraphicallyTo(end);
		}
	}

	@Override
	public void setupGUIinteractions() {
		super.setupGUIinteractions();
	}

}
