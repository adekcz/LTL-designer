/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import javafx.event.Event;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author adekcz
 */
public class FormulaNode extends FormulaShape<Rectangle> {

	private Text text;
	//possible more than one element
	private FormulaShape parent;
	private FormulaShape child;

// records relative x and y co-ordinates.
	public FormulaNode(double x, double y, CanvasController canvasController) {
		super(new Rectangle(x, y, 100, 30), canvasController);

		

	
		//todo this also into super class as static or somehow better method
		//getShape().setOnMouseDragged((eventDragged) -> {
			//if (eventDragged.isPrimaryButtonDown()) {
				//double x1 = eventDragged.getX();
				//double y1 = eventDragged.getY();
				//moveTo(x1, y1);
			//}
//
		//});
	}


	public FormulaShape getChild() {
		return child;
	}

	public FormulaShape getMyParent() {
		return parent;
	}

	public void setMyParent(FormulaShape parent) {
		this.parent = parent;
	}

	@Override
	public double getX() {
		return getShape().getX() + getShape().getWidth() / 2;
	}

	@Override
	public double getY() {
		return getShape().getY() + getShape().getHeight() / 2;
	}

	//TODO Deltas will be better, after all, (it will get rid of magic constant;
	@Override
	public final void moveTo(double x, double y) {
		moveLinesTo(x, y);
		if (getShape() != null) {
			getShape().setX(x);
			getShape().setY(y);
		}
		if (text != null) {
			text.setX(x);
			text.setY(y + 20);
		}

	}	

	@Override
	public void moveBy(double deltaX, double deltaY) {
		System.out.println("DeltaX: " + deltaX + " " + "DeltaY: " + deltaY);
		moveLinesBy(deltaX, deltaY);
		if (getShape() != null) {
			System.out.println("before Change layoutX:" + getShape().getLayoutX() + " getX " + getShape().getX());
			getShape().setX(getShape().getX() + deltaX);
			System.out.println("after Change layoutX:" + getShape().getLayoutX() + " getX " + getShape().getX());
			getShape().setY(getShape().getY() + deltaY);
		}
		if (text != null) {
			text.setX(deltaX + text.getX());
			text.setY(deltaY + text.getY() + 20);
		}
	}
	

	public void setText(String textToAdd) {
		text = new Text(getShape().getX(), getShape().getY() + 20, textToAdd);
		text.setFont(new Font(20));
		text.setWrappingWidth(200);
		text.setTextAlignment(TextAlignment.JUSTIFY);

		text.setOnMouseClicked((event) -> {
			Event.fireEvent(getShape(), event);
		});
		text.setOnMouseDragged((event) -> {
			Event.fireEvent(getShape(), event);
		});
		getController().add(text);
	}

}
