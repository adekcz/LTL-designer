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
	public static TextNode createFormulaNode(double x, double y, CanvasController controller) {
		TextNode formulaNode = new TextNode(x, y, controller);
		formulaNode.setupGUIinteractions();
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
		ConnectingNode created = new ConnectingNode(x, y, controller, ConnectingNode.Type.GrabPoint);
		created.setupGUIinteractions();
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
		ConnectingNode formulaNode = new ConnectingNode(x, y, controller, ConnectingNode.Type.StartPoint);
		formulaNode.setupGUIinteractions();
		return formulaNode;
	}

	/**
	 * Basically startFormula nested over TextNode.Text
	 * @param x
	 * @param y
	 * @param controller
	 * @return 
	 */
	public static ConnectingNode createInnerStartFormulaNode(double x, double y, CanvasController controller) {
		ConnectingNode formulaNode = new ConnectingNode(x, y, controller, ConnectingNode.Type.StartPoint);
		formulaNode.setupGUIinteractions();
		controller.removeFromAllNodes(formulaNode);
		return formulaNode;
	}

	/**
	 * Just Line, from one node to another. 
	 * @param start
	 * @return 
	 */
	public static PolygonalChain createPolygonalChain(AbstractNode start) {
		return createPolygonalChain(start, start);
	}

	/**
	 * Just Line, from one node to another. 
	 * @param start
	 * @return 
	 */
	public static PolygonalChain createPolygonalChain(AbstractNode start, AbstractNode end) {
		PolygonalChain polygonalChain = new PolygonalChain(start, end);
		return polygonalChain;
	}

}
