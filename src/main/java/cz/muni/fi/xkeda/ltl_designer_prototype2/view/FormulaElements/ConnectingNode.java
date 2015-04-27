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

	/**
	 * LineGrabPoint is circular node between polygonal chain.
	 * @param x
	 * @param y
	 * @param controller
	 * @return
	 */
	public static ConnectingNode createLineGrabPoint(double x, double y, CanvasController controller) {
		ConnectingNode created = new ConnectingNode(x, y, controller);
		created.setupGUIinteractions();
		return created;
	}

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
	public final void moveBy(double deltaX, double deltaY) {
		moveLinesBy(deltaX, deltaY);
		getShape().setCenterX(deltaX + getShape().getCenterX());
		getShape().setCenterY(deltaY + getShape().getCenterY());
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

	public void setStandartPressHandler() {
		getShape().setOnMousePressed(pressOnShape());
	}

	public void setStandartReleasedHandler() {
		getShape().setOnMouseReleased(releasedOnShape());
	}
}
