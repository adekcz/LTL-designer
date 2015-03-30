/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.util;

import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.FormulaNode;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.FormulaShape;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.PolygonalChain;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.ConnectingNode;
import java.io.StringReader;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonValue;

/**
 *
 * @author adekcz
 */
public class JsonHelper {

	private static final String KEY_EDGES = "Edges";
	private static final String KEY_START_POINTS = "StartPoints";
	private static final String KEY_GRAB_POINTS = "GrabPoints";
	private static final String KEY_FORMULA_NODES = "FormulaNodes";

	private static final String KEY_INDEX = "index";
	private static final String KEY_TEXT = "text";
	private static final String KEY_Y = "Y";
	private static final String KEY_X = "X";

	public static final String testJson = "{\"FormulaNodes\":[{\"X\":113.73046875,\"Y\":156.0,\"text\":\"Prvni Uzel\",\"index\":1}],\"GrabPoints\":[{\"X\":426.0,\"Y\":243.0,\"index\":3},{\"X\":458.0,\"Y\":23.0,\"index\":4},{\"X\":354.0,\"Y\":252.0,\"index\":5},{\"X\":400.0,\"Y\":25.0,\"index\":6},{\"X\":431.0,\"Y\":-15.0,\"index\":7},{\"X\":484.0,\"Y\":64.0,\"index\":8},{\"X\":510.0,\"Y\":11.0,\"index\":9},{\"X\":345.0,\"Y\":71.0,\"index\":10},{\"X\":288.0,\"Y\":259.0,\"index\":11},{\"X\":496.0,\"Y\":238.0,\"index\":12},{\"X\":524.0,\"Y\":151.0,\"index\":13}],\"StartPoints\":[{\"X\":390.0,\"Y\":58.0,\"index\":2}],\"Edges\":[{\"start\":2,\"end\":3},{\"start\":3,\"end\":4},{\"start\":4,\"end\":5},{\"start\":5,\"end\":6},{\"start\":6,\"end\":7},{\"start\":7,\"end\":8},{\"start\":8,\"end\":9},{\"start\":9,\"end\":10},{\"start\":10,\"end\":11},{\"start\":11,\"end\":12},{\"start\":12,\"end\":13},{\"start\":13,\"end\":1}]}\n" +
"";

	public static void saveJson(JsonObject json, String path) {
		System.out.println(json);
	}

	public static void loadJson(String json, CanvasController controller) {
		JsonReader jsonReader = Json.createReader(new StringReader(testJson));
		JsonObject object = jsonReader.readObject();
		jsonReader.close();

		JsonArray formulas = object.getJsonArray(KEY_FORMULA_NODES);
		JsonArray grabPoints = object.getJsonArray(KEY_GRAB_POINTS);
		JsonArray startPoints = object.getJsonArray(KEY_START_POINTS);
		JsonArray edges = object.getJsonArray(KEY_EDGES);

		for(JsonValue jsonvalue: startPoints){
			JsonObject startPoint = (JsonObject) jsonvalue;
			JsonValue x = startPoint.get(KEY_X);
			JsonValue y = startPoint.get(KEY_Y);
			JsonValue index = startPoint.get(KEY_INDEX);


		}
		for(JsonValue jsonvalue: grabPoints){
			JsonObject grabPoint = (JsonObject) jsonvalue;


		}
		for(JsonValue jsonvalue: formulas){
			JsonObject formula = (JsonObject) jsonvalue;

		}
		for(JsonValue jsonvalue: edges){
			JsonObject edge = (JsonObject) jsonvalue;

		}
	}

	private static void indexElements(List<FormulaShape> nodes) {
		int index = 1;
		for (FormulaShape node : nodes) {
			node.setIndex(index);
			index++;
			if (node instanceof FormulaNode) {
				FormulaNode formulaNode = (FormulaNode) node;
				for (ConnectingNode startNode : formulaNode.getStartPoints()) {
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

		json.add(KEY_FORMULA_NODES, formulaNodes)
			.add(KEY_GRAB_POINTS, grabPoints)
			.add(KEY_START_POINTS, startPoints)
			.add(KEY_EDGES, edges);
		resetIndexes(nodes);
		return json.build();
	}

	private static void fillJsonArrays(List<FormulaShape> nodes, JsonArrayBuilder formulaNodes, JsonArrayBuilder startPoints, JsonArrayBuilder edges, JsonArrayBuilder grabPoints) throws UnsupportedOperationException, IllegalStateException {
		for (int i = 0; i < nodes.size(); i++) {
			FormulaShape uncastNode = nodes.get(i);
			if (uncastNode instanceof FormulaNode) {
				FormulaNode currNode = (FormulaNode) uncastNode;
				formulaNodes.add(convertNode(currNode));
				for (ConnectingNode startPoint : currNode.getStartPoints()) {
					convertOutEdges(startPoint.getOutEdge(), edges);
				}
			} else if (uncastNode instanceof ConnectingNode) {
				ConnectingNode currStartNode = (ConnectingNode) uncastNode;
				startPoints.add(convertFormulaShape(currStartNode));
				convertOutEdges(currStartNode.getOutEdge(), edges);
		//	} else if (uncastNode instanceof LineGrabPoint) {
		//		LineGrabPoint currStartNode = (LineGrabPoint) uncastNode;
		//		grabPoints.add(convertFormulaShape(currStartNode));
		//		convertOutEdges(currStartNode.getOutEdge(), edges);
			} else {
				throw new UnsupportedOperationException("Convertor for " + uncastNode.getClass() + " to JSON was not implemented");
			}

		}
	}

	private static void convertOutEdges(PolygonalChain outEdge, JsonArrayBuilder edges) throws IllegalStateException {
		if (outEdge != null) {
			edges.add(Json.createObjectBuilder()
				.add("start", outEdge.getStart().getIndex())
				.add("end", outEdge.getEnd().getIndex()));

		}
	}

	private static void resetIndexes(List<FormulaShape> nodes) {
		for (FormulaShape node : nodes) {
			node.setIndex(0);
		}
	}

	private static JsonObject convertNode(FormulaNode currNode) {
		JsonObjectBuilder jsonNode = Json.createObjectBuilder();
		jsonNode.add(KEY_X, currNode.getX())
			.add(KEY_Y, currNode.getY())
			.add(KEY_TEXT, currNode.getText())
			.add(KEY_INDEX, currNode.getIndex());
		return jsonNode.build();
	}

	//TODO merge with grabpoints
	private static JsonObject convertFormulaShape(FormulaShape currNode) {
		JsonObjectBuilder jsonNode = Json.createObjectBuilder();
		jsonNode.add(KEY_X, currNode.getX())
			.add(KEY_Y, currNode.getY())
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
