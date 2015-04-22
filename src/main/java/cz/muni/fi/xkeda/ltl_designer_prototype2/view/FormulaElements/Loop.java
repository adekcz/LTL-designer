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
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Optional part of textnode,
 *
 * @author adekcz
 */
public class Loop extends AbstractNode<Circle> {

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

	protected Loop(double x, double y, CanvasController canvasController) {
		super(new Circle(), canvasController);
		double radius = 30;
		double centerX = x + radius / 2;
		double centerY = y - radius / 2;
		//centerX = centerX+ getController().getCanvas().getScene().getX();
		//centerY = centerY+ getController().getCanvas().getScene().getY();
		getShape().setCenterX(centerX);
		getShape().setCenterY(centerY);
		getShape().setRadius(radius);
		getShape().setFill(null);
		getShape().setStroke(getDefaultFill());
		loopLabel = new Text(centerX, centerY, "");
		//todo create this in css file
		loopLabel.setFont(new Font(30));
	}

	private LoopType type;
	private Text loopLabel;

	@Override
	public void moveBy(double deltaX, double deltaY) {
		getShape().setCenterX(deltaX + getShape().getCenterX());
		getShape().setCenterY(deltaY + getShape().getCenterY());
		loopLabel.setX(deltaX + loopLabel.getX());
		loopLabel.setY(deltaY + loopLabel.getY());
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
		return Color.BLACK;
	}

	@Override
	public void setupGUIinteractions() {

		getController().addGraphicToCanvas(getShape());
		getController().addGraphicToCanvas(loopLabel);
		loopLabel.setOnMouseClicked((event) -> {
			if (JavaFxHelper.isDoubleClick(event)) {
				showTypePopupChooser(event.getScreenX(), event.getScreenY());
			}

		});
		loopLabel.setOnMousePressed(pressOnShape());
		loopLabel.setOnMouseReleased(releasedOnShape());
		loopLabel.setOnMouseEntered((event) -> {
			loopLabel.setCursor(Cursor.HAND);
		});
		loopLabel.setOnMouseExited((event) -> {
			loopLabel.setCursor(Cursor.DEFAULT);
		});
	}

	@Override
	public void delete() {
		super.delete();
		getController().removeFromCanvas(loopLabel);
	}

	@Override
	public void changeSelected(boolean isSelected) {
		if (isSelected) {
			Color selectedColor = Color.web(Settings.get(SettingsConstants.SELECTED_COLOR));
			getShape().setStroke(selectedColor);
			loopLabel.setStroke(selectedColor);
		} else {
			getShape().setStroke(getDefaultFill());
			loopLabel.setStroke(getDefaultFill());
		}
		setSelected(isSelected);
	}

	public LoopType getType() {
		return type;
	}

	public void changeType(LoopType type) {
		this.type = type;
		loopLabel.setText(type.toString());
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
