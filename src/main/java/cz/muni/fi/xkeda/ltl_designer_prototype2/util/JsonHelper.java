/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.util;

import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.FormulaNode;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.FormulaShape;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.LineGrabPoint;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.PolygonalChain;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.StartFormulaNode;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author adekcz
 */
public class JsonHelper {
	public static void saveJson(JsonObject json, String path){
		System.out.println(json);
	}

	private static void indexElements(List<FormulaShape> nodes) {
		int index = 1;
		for (FormulaShape node : nodes) {
			node.setIndex(index);
			index++;
			if (node instanceof FormulaNode) {
				FormulaNode formulaNode = (FormulaNode) node;
				for (StartFormulaNode startNode : formulaNode.getStartPoints()) {
					startNode.setIndex(index);
					index++;
				}
			}
		}
	}

	public static JsonObject elementsToJson(List<FormulaShape> nodes) {
		resetIndexes(nodes);
		indexElements(nodes);

		JsonObjectBuilder json = Json.createObjectBuilder();
		JsonArrayBuilder formulaNodes = Json.createArrayBuilder();
		JsonArrayBuilder grabPoints = Json.createArrayBuilder();
		JsonArrayBuilder startPoints = Json.createArrayBuilder();
		JsonArrayBuilder edges = Json.createArrayBuilder();

		fillJsonArrays(nodes, formulaNodes, startPoints, edges, grabPoints);

		json.add("FormulaNodes", formulaNodes)
			.add("GrabPoints", grabPoints)
			.add("StartPoints", startPoints)
			.add("Edges", edges);
		resetIndexes(nodes);
		return json.build();
	}

	private static void fillJsonArrays(List<FormulaShape> nodes, JsonArrayBuilder formulaNodes, JsonArrayBuilder startPoints, JsonArrayBuilder edges, JsonArrayBuilder grabPoints) throws UnsupportedOperationException, IllegalStateException {
		for (int i = 0; i < nodes.size(); i++) {
			FormulaShape uncastNode = nodes.get(i);
			if (uncastNode instanceof FormulaNode) {
				FormulaNode currNode = (FormulaNode) uncastNode;
				formulaNodes.add(convertNode(currNode));
				for (StartFormulaNode startPoint : currNode.getStartPoints()) {
					convertFormulasStartpoints(startPoint, startPoints, edges);
				}
			} else if (uncastNode instanceof StartFormulaNode) {
				StartFormulaNode currStartNode = (StartFormulaNode) uncastNode;
				startPoints.add(convertStartAndGrabPoint(currStartNode));
			} else if (uncastNode instanceof LineGrabPoint) {
				StartFormulaNode currStartNode = (StartFormulaNode) uncastNode;
				grabPoints.add(convertStartAndGrabPoint(currStartNode));
			} else {
				throw new UnsupportedOperationException("Convertor for " + uncastNode.getClass() + " to JSON was not implemented");
			}

		}
	}

	private static void convertFormulasStartpoints(StartFormulaNode start, JsonArrayBuilder startPoints, JsonArrayBuilder edges) throws IllegalStateException {
		startPoints.add(convertStartAndGrabPoint(start));
		if (start.getOutEdge() != null) {
			//TODO remove eventually
			if (start.getIndex() != start.getOutEdge().getStart().getIndex()) {
				throw new IllegalStateException("Those two things should be same object => same index");
			}
			edges.add(Json.createObjectBuilder()
				.add("start", start.getIndex())
				.add("end", start.getOutEdge().getEnd().getIndex()));

		}
	}

	private static void resetIndexes(List<FormulaShape> nodes) {
		for (FormulaShape node : nodes) {
			node.setIndex(0);
		}
	}

	private static JsonObject convertNode(FormulaNode currNode) {
		JsonObjectBuilder jsonNode = Json.createObjectBuilder();
		jsonNode.add("X", currNode.getX())
			.add("Y", currNode.getY())
			.add("text", currNode.getText())
			.add("index", currNode.getIndex());
		return jsonNode.build();
	}

	//TODO merge with grabpoints
	private static JsonObject convertStartAndGrabPoint(StartFormulaNode currNode) {
		JsonObjectBuilder jsonNode = Json.createObjectBuilder();
		jsonNode.add("X", currNode.getX())
			.add("Y", currNode.getY())
			.add("index", currNode.getIndex());
		return jsonNode.build();
	}

	private static JsonObject convertLine(PolygonalChain line) {
		JsonObjectBuilder jsonNode = Json.createObjectBuilder();
		jsonNode.add("start", line.getStart().getIndex())
			.add("end", line.getEnd().getIndex());
		return jsonNode.build();

	}

}
