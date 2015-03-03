/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer.view.CanvasController;
import cz.muni.fi.xkeda.ltl_designer.view.CanvasStatus;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 * @author adekcz
 */
public class MyCircle extends Circle {
	private CanvasController canvasController;

	public MyCircle(double x, double y, int rad, CanvasController canvasController) {
		super(x, y, rad);
		this.canvasController = canvasController;
		
			this.setFill(Color.GREEN);
			this.setOnMouseClicked((eventMouse) -> {
				this.setFill(Color.RED);
				this.setStroke(Color.GREEN);

				if(canvasController.getStatus() == CanvasStatus.CONNECTING_FORMULAE){

				}
			});



			this.setOnMouseDragged((eventDragged) -> {
				System.out.println("insederNeco");
				if(eventDragged.isPrimaryButtonDown()){
					System.out.println("InsideDragged");
					double x1 = eventDragged.getX();
					double y1 = eventDragged.getY();

					this.setCenterX(x1);
					this.setCenterY(y1);
				}
			
			});
	}

	


	
	
	
	
}
