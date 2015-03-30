/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;

/**
 *
 * @author adekcz
 */
public class FormulaShapeFactory {

	public static FormulaNode createFormulaNode(double x, double y, CanvasController controller) {
		FormulaNode formulaNode = new FormulaNode(x, y, controller);
		formulaNode.setupHandlers();
		controller.addToAll(formulaNode);
		return formulaNode;
	}

	public static LineGrabPoint createLineGrabPoint(double x, double y, CanvasController controller) {
		LineGrabPoint created = new LineGrabPoint(x, y, controller);
		controller.addToAll(created);
		created.setupHandlers();
		created.init();
		return created;
	}


	public static PolygonalChain createPolygonalChain(FormulaShape start){
		return createPolygonalChain(start, start);
	}
	public static PolygonalChain createPolygonalChain(FormulaShape start, FormulaShape end){
		return createPolygonalChain(start, end);
	}
	public static StartFormulaNode createStartFormulaNode(double x, double y, CanvasController controller) {
		StartFormulaNode formulaNode = new StartFormulaNode(x, y, controller);
		controller.addToAll(formulaNode);
		formulaNode.setupHandlers();
		return formulaNode;
	}

}
