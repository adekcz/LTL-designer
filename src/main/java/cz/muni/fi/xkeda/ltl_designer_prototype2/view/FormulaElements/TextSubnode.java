/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.SettingsConstants;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JavaFxHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * Contains text and starting points, not intended for use outside of TextNode.java
 *
 * @author adekcz
 */
public class TextSubnode {

	private final static String START_PLACEHOLDER = Settings.get(SettingsConstants.PLACEHOLDER_FOR_FORMULA_START);

	private String textualFormula;
	private Text text;
	private List<ConnectingNode> startPoints;
	private CanvasController controller;
	private final TextNode parent;


	public TextSubnode(TextNode parent, CanvasController controller) {
		this.parent = parent;
		this.controller = controller;
		startPoints = new ArrayList<>();
		textualFormula = "";
	}

	public String getTextualFormula() {
		return textualFormula;
	}

	public void setTextualFormula(String textualFormula) {
		this.textualFormula = textualFormula;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public List<ConnectingNode> getStartPoints() {
		return Collections.unmodifiableList(startPoints);
	}

	public void setStartPoints(List<ConnectingNode> startPoints) {
		this.startPoints = startPoints;
	}

	public void moveBy(double deltaX, double deltaY) {
		moveTextBy(deltaX, deltaY);
		moveStartNodesBy(deltaX, deltaY);
	}

	private void moveStartNodesBy(double deltaX, double deltaY) {
		for (ConnectingNode startNode : startPoints) {
			startNode.moveBy(deltaX, deltaY);
		}
	}

	private void moveTextBy(double deltaX, double deltaY) {
		if (text != null) {
			text.setX(deltaX + text.getX());
			text.setY(deltaY + text.getY());
		}
	}

	/**
	 * adds text and creates starting points
	 *
	 * @param textToAdd
	 */
	public void changeText(String textToAdd) {
		textualFormula = textToAdd;
		createGUItext();
		createStartingPoints(textToAdd);
	}

	public void setupGUIinteractions(CanvasController controller){
		this.controller = controller;
		createGUItext();
	}
	public void createGUItext() {
		text = createNewText(textualFormula);
		getParentShape().setWidth(JavaFxHelper.getWidth(text));
		getController().addGraphicToCanvas(text); // order of adding to canvas matter, text should be added before starting points (mouseeevent handlers rely on this..)

			//TODO FIXME BUG does not work, for some peculiar reason, if loaded from json, you can grab start points and move them individually,
		// even thought this code is definitely run 
		for (ConnectingNode startPoint : startPoints) {
			handOverEvents(startPoint.getShape());
		}

	}

	private Text createNewText(String textToAdd) {
		Text newText = new Text(getParentShape().getX(), getParentShape().getY() + 20, textToAdd.replaceAll(START_PLACEHOLDER, "   "));
		newText.setFont(new Font(20));
		newText.setTextAlignment(TextAlignment.JUSTIFY);
		handOverEvents(newText);
		return newText;
	}

	public void setText(String text) {
		this.textualFormula = text;
	}

	public void addStartPoint(ConnectingNode node) {
		startPoints.add(node);
	}

	private void createStartingPoints(String textToAdd) {
		String[] textFragments = textToAdd.split(START_PLACEHOLDER);
		int count = JavaFxHelper.countSubstringOccurencies(textToAdd, START_PLACEHOLDER);

		createInnerElements(count, textFragments);
	}

	private void createInnerElements(int count, String[] textFragments) {
		String textUsedSoFar = "";
		for (int i = 0; i < count; i++) {
			textUsedSoFar += textFragments[i] + "   ";
			double width = createInnerText(textUsedSoFar);
			createInnerFormula(width);
		}
	}

	private void createInnerFormula(double width) {
		StartingNode startNode = FormulaShapeFactory.createInnerStartFormulaNode(getParentShape().getX() + width, getParentShape().getY() + 10, getController());
		startPoints.add(startNode);
		handOverEvents(startNode.getShape());
	}

	private double createInnerText(String textUsedSoFar) {
		Text textToComputeWidth = createNewText(textUsedSoFar);
		textToComputeWidth.setFill(Color.SEASHELL);
		double width = JavaFxHelper.getWidth(textToComputeWidth);
		return width;
	}

	void delete() {
		for (ConnectingNode cn : startPoints) {
			cn.delete();
		}
		getController().removeFromCanvas(text);
	}

	public CanvasController getController() {
		return controller;
	}

	public void setController(CanvasController controller) {
		this.controller = controller;
	}


	public Rectangle getParentShape(){
		return parent.getShape();
	}


	private void handOverEvents(Node node) {
		node.setOnMousePressed((MouseEvent event) -> {
			System.out.println("Event hadoverd");
			Event.fireEvent(getParentShape(), event);
		});
		//don't hand over event for circles from Inner Formulas (mainly handling creating connection lines)
		if (node instanceof Text) {
			node.setOnMouseClicked((event) -> {
				Event.fireEvent(getParentShape(), event);
			});
		}
		node.setOnMouseDragged((event) -> {
			Event.fireEvent(getParentShape(), event);
		});
		node.setOnMouseReleased((event) -> {
			Event.fireEvent(getParentShape(), event);
		});
	}
}
