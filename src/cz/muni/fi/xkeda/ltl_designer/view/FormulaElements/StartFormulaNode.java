/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer.view.CanvasController;
import cz.muni.fi.xkeda.ltl_designer.view.CanvasStatus;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * class representing "dot". Start point for formulae and subformulae.
 * @author adekcz
 */
public class StartFormulaNode extends  FormulaShape<Circle>{

	/**
	 * 
	 * @param x x coordinate of center of circle
	 * @param y y coordinate of center of circle
	 * @param canvasController  controller for specific stage
	 */
	public StartFormulaNode(double x, double y, CanvasController canvasController){
		super(new Circle(x, y, 10, Color.BLACK),canvasController);
			getShape().setOnMouseClicked((MouseEvent eventMouse) -> {
			FormulaShape.handleClickForLineCreation(canvasController, StartFormulaNode.this);
		//TODO handle if clicked not nothing (possibly in some higher layer
		});

		getShape().setOnMouseDragged((eventDragged) -> {
			if (eventDragged.isPrimaryButtonDown()) {
				double x1 = eventDragged.getX();
				double y1 = eventDragged.getY();
				moveTo(x1, y1);
			}

		});
	}


	public Circle getCircle() {
		return getShape();
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
		moveLines(x, y);
		getShape().setCenterX(x);
		getShape().setCenterY(y);
	}

	

}
