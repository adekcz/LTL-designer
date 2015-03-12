/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.view.FormulaElements;

import java.util.List;
import javafx.scene.shape.Line;

/**
 *
 * @author adekcz
 */
public class PolygonalChain extends FormulaShape<Line>{
	private FormulaShape start;
	private FormulaShape end;

	public PolygonalChain(FormulaShape start){
		this(start, start);
	}
	public PolygonalChain(FormulaShape start, FormulaShape end) {
		setShape(new Line(start.getX(), start.getY(), end.getX(),end.getY()));
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
	public double getX() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public double getY() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void moveTo(double x, double y) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
