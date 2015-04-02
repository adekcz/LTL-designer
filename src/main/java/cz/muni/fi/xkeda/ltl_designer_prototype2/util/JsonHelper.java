/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.util;

import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.AbstractNode;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.ConnectingNode;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.PolygonalChain;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.TextNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;

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

	private static final String KEY_EDGE_END = "end";
	private static final String KEY_EDGE_START = "start";

	public static final String testJson = "{\"FormulaNodes\":[{\"X\":402.5556640625, \"Y\":116.0, \"text\":\"asfdho XXX sdf XXX soifj XXX\", \"index\":1, \"Edges\":[{\"X\":384.650390625, \"Y\":111.0, \"index\":2}, {\"X\":438.56640625, \"Y\":111.0, \"index\":3}, {\"X\":502.111328125, \"Y\":111.0, \"index\":4}]}, {\"X\":167.26171875, \"Y\":290.0, \"text\":\"fsd\", \"index\":5, \"Edges\":[]}, {\"X\":303.26171875, \"Y\":269.0, \"text\":\"dsf\", \"index\":6, \"Edges\":[]}, {\"X\":349.26171875, \"Y\":272.0, \"text\":\"sdf\", \"index\":7, \"Edges\":[]}], \"ConnectingPoints\":[{\"X\":40.0, \"Y\":76.0, \"index\":8}], \"Edges\":[{\"start\":2, \"end\":5}, {\"start\":3, \"end\":6}, {\"start\":4, \"end\":7}, {\"start\":8, \"end\":1}]}\n"
		+ "";

	public static void saveJson(JsonObject json, File file) throws FileNotFoundException {
		Map<String, Object> properties = new HashMap<>(1);
		properties.put(JsonGenerator.PRETTY_PRINTING, true);

		JsonWriterFactory createWriterFactory = Json.createWriterFactory(properties);
		JsonWriter writer = createWriterFactory.createWriter(new FileOutputStream(file));

		writer.writeObject(json);
		writer.close();

	}

	public static void loadJson(File file, CanvasController controller) throws FileNotFoundException {
		Map<Integer, AbstractNode> nodes = new HashMap<>();
		JsonReader jsonReader = Json.createReader(new FileReader(file));
		JsonObject object = jsonReader.readObject();
		jsonReader.close();

		JsonArray formulas = object.getJsonArray(KEY_FORMULA_NODES);
		JsonArray startPoints = object.getJsonArray(KEY_CONNECTING_POINTS);
		JsonArray edges = object.getJsonArray(KEY_EDGES);

		for (JsonValue jsonvalue : startPoints) {
			JsonObject startPointJson = (JsonObject) jsonvalue;
			JsonNumber x = (JsonNumber) startPointJson.get(KEY_X);
			JsonNumber y = (JsonNumber) startPointJson.get(KEY_Y);
			JsonNumber index = (JsonNumber) startPointJson.get(KEY_INDEX);
			ConnectingNode startPoint = new ConnectingNode(x.doubleValue(), y.doubleValue());
			nodes.put(index.intValue(), startPoint);
		}
		for (JsonValue jsonvalue : formulas) {
			JsonObject formulaJson = (JsonObject) jsonvalue;
			JsonNumber x = (JsonNumber) formulaJson.get(KEY_X);
			JsonNumber y = (JsonNumber) formulaJson.get(KEY_Y);
			JsonNumber index = (JsonNumber) formulaJson.get(KEY_INDEX);
			JsonString text = (JsonString) formulaJson.getJsonString(KEY_TEXT);

			TextNode formulaNode = new TextNode(x.intValue(), y.intValue());
			formulaNode.setText(text.getString());
			nodes.put(index.intValue(), formulaNode);
			for (JsonValue jsonValueConnectingPoint : formulaJson.getJsonArray(KEY_EDGES)) {
				JsonObject connectingPoint = (JsonObject) jsonValueConnectingPoint;
				JsonNumber x2 = (JsonNumber) connectingPoint.get(KEY_X);
				JsonNumber y2 = (JsonNumber) connectingPoint.get(KEY_Y);
				JsonNumber index2 = (JsonNumber) connectingPoint.get(KEY_INDEX);
				ConnectingNode startPoint = new ConnectingNode(x2.doubleValue(), y2.doubleValue());
				formulaNode.addStartPoint(startPoint);
				nodes.put(index2.intValue(), startPoint);
			}

		}
		for (JsonValue jsonvalue : edges) {
			JsonObject edge = (JsonObject) jsonvalue;
			int startIndex = ((JsonNumber) edge.get(KEY_EDGE_START)).intValue();
			int endIndex = ((JsonNumber) edge.get(KEY_EDGE_END)).intValue();
			nodes.get(startIndex).connectTo(nodes.get(endIndex));
		}

		for (AbstractNode aNode : nodes.values()) {
			aNode.setController(controller);
			aNode.setupGUIinteractions();
			if (aNode.getOutEdge() != null) {
				controller.add(aNode.getOutEdge().getShape());
			}
		}
		System.out.println("");
	}

	private static void indexElements(List<AbstractNode> nodes) {
		int index = 1;
		for (AbstractNode node : nodes) {
			node.setIndex(index);
			index++;
			if (node instanceof TextNode) {
				TextNode formulaNode = (TextNode) node;
				for (ConnectingNode startNode : formulaNode.getStartPoints()) {
					startNode.setIndex(index);
					index++;
				}
			}
		}
	}

	public static JsonObjectBuilder elementsToJson(List<AbstractNode> nodes) {
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

	private static void fillJsonArrays(List<AbstractNode> nodes, JsonArrayBuilder formulaNodes, JsonArrayBuilder connectinPoints, JsonArrayBuilder edges) throws UnsupportedOperationException, IllegalStateException {
		for (int i = 0; i < nodes.size(); i++) {
			AbstractNode uncastNode = nodes.get(i);
			if (uncastNode instanceof TextNode) {
				TextNode currNode = (TextNode) uncastNode;
				JsonObjectBuilder nodeBuilder = convertNode(currNode);
				JsonArrayBuilder innerStartPointsArray = Json.createArrayBuilder();
				for (ConnectingNode connectinPoint : currNode.getStartPoints()) {
					JsonObjectBuilder jsonPoint = convertConnectingNode(connectinPoint);
					innerStartPointsArray.add(jsonPoint);
					convertOutEdges(connectinPoint.getOutEdge(), edges);
				}
				nodeBuilder.add(KEY_EDGES, innerStartPointsArray);
				formulaNodes.add(nodeBuilder);

			} else if (uncastNode instanceof ConnectingNode) {
				ConnectingNode currConnectingNode = (ConnectingNode) uncastNode;
				connectinPoints.add(convertConnectingNode(currConnectingNode));
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
				.add(KEY_EDGE_START, outEdge.getStart().getIndex())
				.add(KEY_EDGE_END, outEdge.getEnd().getIndex()));

		}
	}

	private static void resetIndexes(List<AbstractNode> nodes) {
		for (AbstractNode node : nodes) {
			node.setIndex(0);
		}
	}

	private static JsonObjectBuilder convertNode(TextNode currNode) {
		JsonObjectBuilder jsonNode = Json.createObjectBuilder();
		jsonNode.add(KEY_X, currNode.getShape().getX())
			.add(KEY_Y, currNode.getShape().getY())
			.add(KEY_TEXT, currNode.getText())
			.add(KEY_INDEX, currNode.getIndex());
		return jsonNode;
	}

	//TODO merge with grabpoints
	private static JsonObjectBuilder convertConnectingNode(ConnectingNode currNode) {
		JsonObjectBuilder jsonNode = Json.createObjectBuilder();
		jsonNode.add(KEY_X, currNode.getRepresentativeX())
			.add(KEY_Y, currNode.getRepresentativeY())
			.add("index", currNode.getIndex());
		return jsonNode;
	}

	private static JsonObjectBuilder convertLine(PolygonalChain line) {
		JsonObjectBuilder jsonNode = Json.createObjectBuilder();
		jsonNode.add(KEY_EDGE_START, line.getStart().getIndex())
			.add(KEY_EDGE_END, line.getEnd().getIndex());
		return jsonNode;

	}

}
