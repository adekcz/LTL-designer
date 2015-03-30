/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.util;

import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.ConnectingNode;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.FormulaNode;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.FormulaShape;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.PolygonalChain;
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
	private static final String KEY_CONNECTING_POINTS = "ConnectingPoints";
	private static final String KEY_FORMULA_NODES = "FormulaNodes";

	private static final String KEY_INDEX = "index";
	private static final String KEY_TEXT = "text";
	private static final String KEY_Y = "Y";
	private static final String KEY_X = "X";

	public static final String testJson = "{\"FormulaNodes\":[{\"X\":300.1240234375,\"Y\":95.0,\"text\":\"Prvni XXX druhy XXX\",\"index\":1},{\"X\":156.6064453125,\"Y\":264.0,\"text\":\"Connect\",\"index\":6},{\"X\":353.384765625,\"Y\":294.0,\"text\":\"(asfd)\",\"index\":7},{\"X\":445.6025390625,\"Y\":-10.0,\"text\":\"poslendi\",\"index\":9}],\"ConnectingPoints\":[{\"X\":293.810546875,\"Y\":90.0,\"index\":4},{\"X\":372.248046875,\"Y\":90.0,\"index\":5},{\"X\":94.0,\"Y\":90.0,\"index\":8},{\"X\":502.0,\"Y\":321.0,\"index\":10},{\"X\":494.0,\"Y\":228.0,\"index\":11},{\"X\":431.0,\"Y\":225.0,\"index\":12},{\"X\":469.0,\"Y\":185.0,\"index\":13},{\"X\":549.0,\"Y\":235.0,\"index\":14},{\"X\":547.0,\"Y\":319.0,\"index\":15},{\"X\":569.0,\"Y\":163.0,\"index\":16},{\"X\":517.0,\"Y\":87.0,\"index\":17},{\"X\":582.0,\"Y\":-6.0,\"index\":18}],\"Edges\":[{\"start\":4,\"end\":6},{\"start\":5,\"end\":7},{\"start\":4,\"end\":6},{\"start\":5,\"end\":7},{\"start\":8,\"end\":1},{\"start\":10,\"end\":11},{\"start\":11,\"end\":12},{\"start\":12,\"end\":13},{\"start\":13,\"end\":14},{\"start\":14,\"end\":15},{\"start\":15,\"end\":16},{\"start\":16,\"end\":17},{\"start\":17,\"end\":18},{\"start\":18,\"end\":9}]}\n"
		+ "";

	public static void saveJson(JsonObject json, String path) {
		System.out.println(json);
	}

	public static void loadJson(String json, CanvasController controller) {
		JsonReader jsonReader = Json.createReader(new StringReader(testJson));
		JsonObject object = jsonReader.readObject();
		jsonReader.close();

		JsonArray formulas = object.getJsonArray(KEY_FORMULA_NODES);
		JsonArray startPoints = object.getJsonArray(KEY_CONNECTING_POINTS);
		JsonArray edges = object.getJsonArray(KEY_EDGES);

		for (JsonValue jsonvalue : startPoints) {
			JsonObject startPoint = (JsonObject) jsonvalue;
			JsonValue x = startPoint.get(KEY_X);
			JsonValue y = startPoint.get(KEY_Y);
			JsonValue index = startPoint.get(KEY_INDEX);

		}
		for (JsonValue jsonvalue : formulas) {
			JsonObject formula = (JsonObject) jsonvalue;

		}
		for (JsonValue jsonvalue : edges) {
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

	public static JsonObjectBuilder elementsToJson(List<FormulaShape> nodes) {
		resetIndexes(nodes);
		indexElements(nodes);

		JsonObjectBuilder json = Json.createObjectBuilder();
		JsonArrayBuilder formulaNodes = Json.createArrayBuilder();
		JsonArrayBuilder connectingPoints = Json.createArrayBuilder();
		JsonArrayBuilder edges = Json.createArrayBuilder();

		fillJsonArrays(nodes, formulaNodes, connectingPoints, edges);

		json.add(KEY_FORMULA_NODES, formulaNodes)
			.add(KEY_CONNECTING_POINTS, connectingPoints)
			.add(KEY_EDGES, edges);
		resetIndexes(nodes);
		return json;
	}

	private static void fillJsonArrays(List<FormulaShape> nodes, JsonArrayBuilder formulaNodes, JsonArrayBuilder connectinPoints, JsonArrayBuilder edges) throws UnsupportedOperationException, IllegalStateException {
		for (int i = 0; i < nodes.size(); i++) {
			FormulaShape uncastNode = nodes.get(i);
			if (uncastNode instanceof FormulaNode) {
				FormulaNode currNode = (FormulaNode) uncastNode;
				JsonObjectBuilder nodeBuilder = convertNode(currNode);
				JsonArrayBuilder innerStartPointsArray = Json.createArrayBuilder();
				for (ConnectingNode connectinPoint : currNode.getStartPoints()) {
					JsonObjectBuilder jsonPoint = convertFormulaShape(connectinPoint);
					innerStartPointsArray.add(jsonPoint);
					convertOutEdges(connectinPoint.getOutEdge(), edges);
				}
				nodeBuilder.add(KEY_EDGES, innerStartPointsArray);
				formulaNodes.add(nodeBuilder);
				
			} else if (uncastNode instanceof ConnectingNode) {
				ConnectingNode currConnectingNode = (ConnectingNode) uncastNode;
				connectinPoints.add(convertFormulaShape(currConnectingNode));
				convertOutEdges(currConnectingNode.getOutEdge(), edges);
		//	} else if (uncastNode instanceof LineGrabPoint) {
				//		LineGrabPoint currStartNode = (LineGrabPoint) uncastNode;
				//		grabPoints.add(convertFormulaShape(currStartNode));
				//		convertOutEdges(currStartNode.getOutEdge(), edges);
			} else {
				throw new UnsupportedOperationException("Convertor for " + uncastNode.getClass() + " to JSON was not implemented");
			}

		}
	}

	//TODO refractor so thtat it returns jsonobject
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

	private static JsonObjectBuilder convertNode(FormulaNode currNode) {
		JsonObjectBuilder jsonNode = Json.createObjectBuilder();
		jsonNode.add(KEY_X, currNode.getX())
			.add(KEY_Y, currNode.getY())
			.add(KEY_TEXT, currNode.getText())
			.add(KEY_INDEX, currNode.getIndex());
		return jsonNode;
	}

	//TODO merge with grabpoints
	private static JsonObjectBuilder convertFormulaShape(FormulaShape currNode) {
		JsonObjectBuilder jsonNode = Json.createObjectBuilder();
		jsonNode.add(KEY_X, currNode.getX())
			.add(KEY_Y, currNode.getY())
			.add("index", currNode.getIndex());
		return jsonNode;
	}

	private static JsonObjectBuilder convertLine(PolygonalChain line) {
		JsonObjectBuilder jsonNode = Json.createObjectBuilder();
		jsonNode.add("start", line.getStart().getIndex())
			.add("end", line.getEnd().getIndex());
		return jsonNode;

	}

}
