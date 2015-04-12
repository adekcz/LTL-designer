/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
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
 *
 * @author adekcz
 */
public class TextNode extends AbstractNode<Rectangle> {

	private final static String START_PLACEHOLDER = "XXX";

	private String textualFormula;
	private Text text;
	private final List<ConnectingNode> startPoints;

	public List<ConnectingNode> getStartPoints() {
		return Collections.unmodifiableList(startPoints);
	}

	//possible more than one element
	private AbstractNode parent;

	public TextNode(double x, double y) {
		this(x, y, null);
	}

	protected TextNode(double x, double y, CanvasController canvasController) {
		super(new Rectangle(x, y, 100, 30), canvasController);
		startPoints = new ArrayList<>();
		textualFormula = "";

		//todo this also into super class as static or somehow better method
		//getShape().setOnMouseDragged((eventDragged) -> {
		//if (eventDragged.isPrimaryButtonDown()) {
		//double x1 = eventDragged.getRepresentativeX();
		//double y1 = eventDragged.getRepresentativeY();
		//moveTo(x1, y1);
		//}
//
		//});
	}

	public AbstractNode getMyParent() {
		return parent;
	}

	public void setMyParent(AbstractNode parent) {
		this.parent = parent;
	}

	@Override
	public double getRepresentativeX() {
		return getShape().getX();
	}

	@Override
	public double getRepresentativeY() {
		return getShape().getY();
	}

	/**
	 *
	 * @return unmodified text under this formula
	 */
	public String getText() {
		return textualFormula;
	}

	//TODO Deltas will be better, after all, (it will get rid of magic constant;
	@Override
	public final void moveTo(double x, double y) {
		moveLinesTo(x, y);
		if (getShape() != null) {
			getShape().setX(x);
			getShape().setY(y);
		}
		if (text != null) {
			text.setX(x);
			text.setY(y + 20);
		}

	}

	@Override
	public void moveBy(double deltaX, double deltaY) {
		moveLinesBy(deltaX, deltaY);
		moveShapeBy(deltaX, deltaY);
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

	private void moveShapeBy(double deltaX, double deltaY) {
		if (getShape() != null) {
			getShape().setX(getShape().getX() + deltaX);
			getShape().setY(getShape().getY() + deltaY);
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

	private void createGUItext(){
		text = createNewText(textualFormula);
		getShape().setWidth(JavaFxHelper.getWidth(text));
		getController().add(text); // order of adding to canvas matter, text should be added before starting points (mouseeevent handlers rely on this..)

	}

	public void setText(String text) {
		this.textualFormula = text;
	}

	@Override
	public void setupGUIinteractions() {
		super.setupGUIinteractions();
		createGUItext();
	}

	private Text createNewText(String textToAdd) {
		Text newText = new Text(getShape().getX(), getShape().getY() + 20, textToAdd.replaceAll(START_PLACEHOLDER, "   "));
		newText.setFont(new Font(20));
		newText.setTextAlignment(TextAlignment.JUSTIFY);
		handOverEvents(newText);
		return newText;
	}

	public void addStartPoint(ConnectingNode node) {
		startPoints.add(node);
	}

	private void handOverEvents(Node node) {
		node.setOnMousePressed((MouseEvent event) -> {
			Event.fireEvent(getShape(), event);
		});
		if (node instanceof Text) {
			node.setOnMouseClicked((event) -> {
				Event.fireEvent(getShape(), event);
			});
		}
		node.setOnMouseDragged((event) -> {
			Event.fireEvent(getShape(), event);
		});
		node.setOnMouseReleased((event) -> {
			Event.fireEvent(getShape(), event);
		});
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
		ConnectingNode startNode = FormulaShapeFactory.createInnerStartFormulaNode(getShape().getX() + width, getShape().getY() + 10, getController());
		startNode.getShape().setFill(Color.PINK);
		startPoints.add(startNode);
		handOverEvents(startNode.getShape());
	}

	private double createInnerText(String textUsedSoFar) {
		Text textToComputeWidth = createNewText(textUsedSoFar);
		textToComputeWidth.setFill(Color.SEASHELL);
		double width = JavaFxHelper.getWidth(textToComputeWidth);
		return width;
	}

	@Override
	public void setDefaultFill() {
		getShape().setFill(Color.web(Settings.get(Settings.FORMULA_COLOR)));
	}

}
