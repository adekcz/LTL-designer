/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer.view.CanvasController;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 *
 * @author adekcz
 */
public abstract class MyShape<E extends Shape> {
	private List<MyLine> outEdges;
	private List<MyLine> inEdges;
	private CanvasController controller;
	//in more complicated formulas thiss will probably be group of different elements
	private E shape;

	public MyShape(){
		outEdges = new ArrayList<>();
		inEdges = new ArrayList<>();
	}
	public MyShape(E shape, CanvasController controller){
		this();
		this.shape = shape;
		this.controller = controller;
	}

	public CanvasController getController() {
		return controller;
	}

	public void setController(CanvasController controller) {
		this.controller = controller;
	}

	public E getShape(){
		return shape;
	}

	public void setShape(E shape){
		this.shape = shape;
	}
	
	//TODO pointless?
	public abstract void setChild(MyShape aThis);
	/**
	 * Moves by deltaX and deltaY units. Also moves with appropriate end of all lines that are connected to this
	 * @param x where to move
	 * @param y where to move
	 */
	protected void moveLines(double x, double y){
		for(MyLine myLine:inEdges){
			Line line = myLine.getShape();
			line.setEndX(x);
			line.setEndY(y);
		}
		for(MyLine myLine:outEdges){
			Line line = myLine.getShape();
			line.setStartX(x);
			line.setStartY(y);
		}
	}
	public abstract void moveTo(double x, double y);

	public abstract double getCenterX();
	public abstract double getCenterY();

	public void addToOutEdges(MyLine line){
		this.outEdges.add(line);
	}
	public void addToInEdges(MyLine line){
		this.inEdges.add(line);
	}

	
}
