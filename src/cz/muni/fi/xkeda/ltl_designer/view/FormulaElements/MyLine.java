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
public class MyLine extends FormulaShape<Line>{
	private FormulaShape start;
	private FormulaShape end;

	public MyLine(FormulaShape start){
		this(start, start);
	}

	public MyLine(FormulaShape start, FormulaShape end) {
		setShape(new Line(start.getCenterX(), start.getCenterY(), end.getCenterX(),end.getCenterY()));
		this.start = start;
		this.end = end;
	}

	public FormulaShape getStart() {
		return start;
	}

	public void setStart(FormulaShape start) {
		this.start = start;
	}

	public FormulaShape getEnd() {
		return end;
	}

	public void setEnd(FormulaShape end) {
		this.end = end;
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
