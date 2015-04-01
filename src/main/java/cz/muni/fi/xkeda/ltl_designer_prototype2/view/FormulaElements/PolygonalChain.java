/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import javafx.scene.shape.Line;

/**
 *
 * @author adekcz
 */
public class PolygonalChain extends AbstractNode<Line>{
	private AbstractNode start;
	private AbstractNode end;

	protected PolygonalChain(AbstractNode start){
		this(start, start);
	}
	protected PolygonalChain(AbstractNode start, AbstractNode end) {
		setShape(new Line(start.getX(), start.getY(), end.getX(),end.getY()));
		this.start = start;
		this.end = end;
	}

	public AbstractNode getStart() {
		return start;
	}

	public void setStart(AbstractNode start) {
		this.start = start;
	}

	public AbstractNode getEnd() {
		return end;
	}

	public void setEnd(AbstractNode end) {
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
		moveLinesTo(x, y);
	}
	@Override
	public final void moveBy(double deltaX, double detlaY) {
		//TODO create method for moving circles
		moveLinesBy(deltaX, detlaY);
	}

}
