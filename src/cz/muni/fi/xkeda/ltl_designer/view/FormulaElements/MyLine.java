/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.view.FormulaElements;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

/**
 *
 * @author adekcz
 */
public class MyLine extends MyShape<Line>{
	private MyShape start;
	private MyShape end;

	public MyLine(MyShape start){
		this(start, start);
	}

	public MyLine(MyShape start, MyShape end) {
		setShape(new Line(start.getCenterX(), start.getCenterY(), end.getCenterX(),end.getCenterY()));
		this.start = start;
		this.end = end;
	}

	public MyShape getStart() {
		return start;
	}

	public void setStart(MyShape start) {
		this.start = start;
	}

	public MyShape getEnd() {
		return end;
	}

	public void setEnd(MyShape end) {
		this.end = end;
	}

	


	@Override
	public void setChild(MyShape aThis) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}


	@Override
	public double getCenterX() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public double getCenterY() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void moveTo(double x, double y) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
