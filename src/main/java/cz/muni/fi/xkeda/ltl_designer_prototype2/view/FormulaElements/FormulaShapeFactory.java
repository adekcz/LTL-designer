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
		controller.add(formulaNode.getShape());
		return formulaNode;
	}

	public static ConnectingNode createLineGrabPoint(double x, double y, CanvasController controller) {
		ConnectingNode created = new ConnectingNode(x, y, controller);
		controller.addToAll(created);
		controller.add(created.getShape());
		created.setupHandlersForGrabPoint();
		return created;
	}

	public static ConnectingNode createStartFormulaNode(double x, double y, CanvasController controller) {
		ConnectingNode formulaNode = new ConnectingNode(x, y, controller);
		controller.addToAll(formulaNode);
		controller.add(formulaNode.getShape());
		formulaNode.setupHandlers();
		return formulaNode;
	}

	public static PolygonalChain createPolygonalChain(FormulaShape start){
		return createPolygonalChain(start, start);
	}
	public static PolygonalChain createPolygonalChain(FormulaShape start, FormulaShape end){
		PolygonalChain polygonalChain = new PolygonalChain(start, end);
		return polygonalChain;
	}

}
