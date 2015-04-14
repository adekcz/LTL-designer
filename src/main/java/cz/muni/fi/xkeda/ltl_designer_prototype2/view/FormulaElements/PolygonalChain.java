/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author adekcz
 */
public class PolygonalChain extends AbstractNode<Line>{
	private AbstractNode start;
	private AbstractNode end;

	protected PolygonalChain(AbstractNode start, CanvasController controller){
		this(start, start, controller);
	}
	protected PolygonalChain(AbstractNode start, AbstractNode end, CanvasController controller) {
		super(new Line(start.getRepresentativeX(), start.getRepresentativeY(), end.getRepresentativeX(),end.getRepresentativeY()), controller);
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
	public void moveTo(double x, double y) {
		moveLinesTo(x, y);
	}
	@Override
	public final void moveBy(double deltaX, double detlaY) {
		//TODO create method for moving circles
		moveLinesBy(deltaX, detlaY);
	}

	@Override
	public void setDefaultFill() {
		getShape().setFill(Color.BLACK);
	}

	@Override
	public double getRepresentativeX() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public double getRepresentativeY() {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void delete() {
		super.delete();
	}

}
