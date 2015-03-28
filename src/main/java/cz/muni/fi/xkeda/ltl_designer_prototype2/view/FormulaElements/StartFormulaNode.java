/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * class representing "dot". Start point for formulae and subformulae.
 *
 * @author adekcz
 */
public class StartFormulaNode extends FormulaShape<Circle> {

	/**
	 *
	 * @param x x coordinate of center of circle
	 * @param y y coordinate of center of circle
	 * @param canvasController controller for specific stage
	 */
	public StartFormulaNode(double x, double y, CanvasController canvasController) {
		super(new Circle(x, y, 10, Color.PURPLE), canvasController);
		getShape().setOnMouseClicked((MouseEvent eventMouse) -> {
			FormulaShape.handleClickForLineCreation(canvasController, StartFormulaNode.this);
			getShape().setFill(Color.BLUE);
			if (eventMouse.getClickCount() == 2) {
				System.out.println("Double clicked");
			}
			//TODO handle if clicked not nothing (possibly in some higher layer
		});

	}


	@Override
	public final double getX() {
		return getShape().getCenterX();
	}

	@Override
	public final double getY() {
		return getShape().getCenterY();
	}

	@Override
	public final void moveTo(double x, double y) {
		moveLinesTo(x, y);
		getShape().setCenterX(x);
		getShape().setCenterY(y);
	}

	@Override
	public final void moveBy(double deltaX, double detlaY) {
		//TODO create method for moving circles
		moveLinesBy(deltaX, detlaY);
		getShape().setCenterX(deltaX + getShape().getCenterX());
		getShape().setCenterY(detlaY + getShape().getCenterY());
	}

}
