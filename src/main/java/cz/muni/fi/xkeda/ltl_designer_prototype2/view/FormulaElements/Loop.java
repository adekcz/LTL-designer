/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.SettingsConstants;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JavaFxHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import javafx.event.ActionEvent;
import javafx.scene.Cursor;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Optional part of textnode,
 *
 * @author adekcz
 */
public class Loop extends AbstractNode<CubicCurve> {

	//TODO this is empirical !!!! 
	private static final double controlPointsOffset = 50;
	private static final double labelOffset = 20;

	private LoopType type;
	private Text label;



	public enum LoopType {

		FINITE, INFINITE;

		@Override
		public String toString() {
			switch (this) {
				case FINITE:
					return "*";
				case INFINITE:
					return "\u221E";
				default:
					throw new IllegalArgumentException();
			}
		}

	}

	public Loop(LoopType type) {
		super(new CubicCurve(), null);
		this.type = type;
	}
	/**
	 * @param parent coordinates will be computed from rectangle
	 */
	protected Loop(Rectangle parent, CanvasController canvasController) {
		super(new CubicCurve(), canvasController);

		initCurve(parent);
		initText(parent);
	}

	private void initText(Rectangle parent) {
		double x = parent.getX() + parent.getWidth() / 2;
		double y = parent.getY() - labelOffset;
		label = new Text(x, y, "");
		//todo create this in css file
		label.setFont(new Font(30));
	}

	private void initCurve(Rectangle parent) {
		double x1 = parent.getX();
		double x2 = parent.getX() + parent.getWidth();
		double y = parent.getY() + parent.getHeight() / 2;

		CubicCurve curve = getShape();

		curve.setStartX(x1);
		curve.setStartY(y);
		curve.setEndX(x2);
		curve.setEndY(y);

		curve.setControlX1(x1 - controlPointsOffset);
		curve.setControlY1(y - controlPointsOffset);
		curve.setControlX2(x2 + controlPointsOffset);
		curve.setControlY2(y - controlPointsOffset);

		curve.setFill(null);
		curve.setStroke(getDefaultFill());
	}


	@Override
	public void moveBy(double deltaX, double deltaY) {
		moveCurveBy(deltaX, deltaY);
		label.setX(deltaX + label.getX());
		label.setY(deltaY + label.getY());
	}

	private void moveCurveBy(double deltaX, double deltaY) {
		CubicCurve curve = getShape();
		curve.setStartX(curve.getStartX() + deltaX);
		curve.setEndX(curve.getEndX() + deltaX);
		curve.setStartY(curve.getStartY() + deltaY);
		curve.setEndY(curve.getEndY() + deltaY);

		curve.setControlX1(curve.getControlX1() + deltaX);
		curve.setControlX2(curve.getControlX2() + deltaX);
		curve.setControlY1(curve.getControlY1() + deltaY);
		curve.setControlY2(curve.getControlY2() + deltaY);
	}

	@Override
	public final double getRepresentativeX() {
		return label.getX();
	}

	@Override
	public final double getRepresentativeY() {
		return label.getY();
	}

	@Override
	public Color getDefaultFill() {
		return Color.BLACK;
	}

	@Override
	public void setupGUIinteractions() {

		getController().addGraphicToCanvas(getShape());
		getController().addGraphicToCanvas(label);
		label.setOnMouseClicked((event) -> {
			if (JavaFxHelper.isDoubleClick(event) || event.isSecondaryButtonDown()) {
				showTypePopupChooser(event.getScreenX(), event.getScreenY());
			}

		});
		label.setOnMousePressed(pressOnShape());
		label.setOnMouseReleased(releasedOnShape());
		label.setOnMouseEntered((event) -> {
			label.setCursor(Cursor.HAND);
		});
		label.setOnMouseExited((event) -> {
			label.setCursor(Cursor.DEFAULT);
		});
	}

	@Override
	public void delete() {
		super.delete();
		getController().removeFromCanvas(label);
	}

	@Override
	public void changeSelected(boolean isSelected) {
		if (isSelected) {
			Color selectedColor = Color.web(Settings.get(SettingsConstants.SELECTED_COLOR));
			getShape().setStroke(selectedColor);
			label.setStroke(selectedColor);
		} else {
			getShape().setStroke(getDefaultFill());
			label.setStroke(getDefaultFill());
		}
		setSelected(isSelected);
	}

	public LoopType getType() {
		return type;
	}

	public void changeType(LoopType type) {
		this.type = type;
		label.setText(type.toString());
	}

	public void showTypePopupChooser(double offsetX, double offsetY) {
		ContextMenu typeChooser = new ContextMenu();
		MenuItem infiniteItem = new MenuItem(LoopType.INFINITE.toString());
		infiniteItem.setOnAction((ActionEvent e) -> {
			changeType(LoopType.INFINITE);
		});
		MenuItem finiteItem = new MenuItem(LoopType.FINITE.toString());
		finiteItem.setOnAction((ActionEvent e) -> {
			changeType(LoopType.FINITE);
		});
		typeChooser.getItems().addAll(infiniteItem, finiteItem);
		System.out.println("Ass window" + getShape().getParent());
		//typeChooser.show(getController().getCanvas(), getRepresentativeX()+offsetX, getRepresentativeY()+offsetY);
		typeChooser.show(getController().getCanvas(), offsetX, offsetY);

	}
}
