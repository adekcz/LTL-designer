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

	/**
	 * @param x
	 * @param y
	 * @param controller
	 * @return Returns formula node. Rectangle with text, that might contain connectors to another formulas.
	 */
	public static FormulaNode createFormulaNode(double x, double y, CanvasController controller) {
		FormulaNode formulaNode = new FormulaNode(x, y, controller);
		formulaNode.setupHandlers();
		controller.addToAll(formulaNode);
		controller.add(formulaNode.getShape());
		return formulaNode;
	}

	/**
	 * LineGrabPoint is circular node between polygonal chain.
	 * @param x
	 * @param y
	 * @param controller
	 * @return 
	 */
	public static ConnectingNode createLineGrabPoint(double x, double y, CanvasController controller) {
		ConnectingNode created = new ConnectingNode(x, y, controller);
		controller.addToAll(created);
		controller.add(created.getShape());
		created.setupHandlersForGrabPoint();
		return created;
	}

	/**
	 * Should be only one in correct formula.
	 * @param x
	 * @param y
	 * @param controller
	 * @return 
	 */
	public static ConnectingNode createStartFormulaNode(double x, double y, CanvasController controller) {
		ConnectingNode formulaNode = createInnerStartFormulaNode(x, y, controller);
		controller.addToAll(formulaNode);
		return formulaNode;
	}

	/**
	 * Basically startFormula nested over FormulaNode.Text
	 * @param x
	 * @param y
	 * @param controller
	 * @return 
	 */
	public static ConnectingNode createInnerStartFormulaNode(double x, double y, CanvasController controller) {
		ConnectingNode formulaNode = new ConnectingNode(x, y, controller);
		controller.add(formulaNode.getShape());
		formulaNode.setupHandlers();
		return formulaNode;
	}

	/**
	 * Just Line, from one node to another. 
	 * @param start
	 * @return 
	 */
	public static PolygonalChain createPolygonalChain(FormulaShape start) {
		return createPolygonalChain(start, start);
	}

	/**
	 * Just Line, from one node to another. 
	 * @param start
	 * @return 
	 */
	public static PolygonalChain createPolygonalChain(FormulaShape start, FormulaShape end) {
		PolygonalChain polygonalChain = new PolygonalChain(start, end);
		return polygonalChain;
	}

}
