/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.util;

import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.FormulaNode;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.FormulaShape;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.LineGrabPoint;
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

	public static JsonObject elementsToJson(List<FormulaShape> nodes) {
		JsonObjectBuilder json = Json.createObjectBuilder();
		JsonArrayBuilder formulaNodes = Json.createArrayBuilder();
		JsonArrayBuilder grabPoints = Json.createArrayBuilder();
		for (FormulaShape node : nodes) {
			node.setIndex(0);
		}
		for (int i = 0; i < nodes.size(); i++) {
			if (nodes.get(i) instanceof FormulaNode) {
				FormulaNode currNode = (FormulaNode) nodes.get(i);

				formulaNodes.add(convertNode(currNode, i));

			}

		}
		for (FormulaShape node : nodes) {
			node.setIndex(0);
		}
		return json.build();
	}

    private static JsonObject convertNode(FormulaNode currNode, int i) {
        JsonObjectBuilder jsonNode = Json.createObjectBuilder();
        jsonNode.add("X", currNode.getX())
                .add("Y", currNode.getY())
                .add("text", currNode.getText())
                .add("index", i);
        return jsonNode.build();
    }

      private static JsonObject convertStart(StartFormulaNode currNode, int i) {
        JsonObjectBuilder jsonNode = Json.createObjectBuilder();
        jsonNode.add("X", currNode.getX())
                .add("Y", currNode.getY())
                .add("index", i);
        return jsonNode.build();
    }
        private static JsonObject convertGrabPoint(LineGrabPoint currNode, int i) {
        JsonObjectBuilder jsonNode = Json.createObjectBuilder();
        jsonNode.add("X", currNode.getX())
                .add("Y", currNode.getY())
                .add("index", i);
        return jsonNode.build();
    }
}
