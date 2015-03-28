/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JavaFxHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import java.util.ArrayList;
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
public class FormulaNode extends FormulaShape<Rectangle> {

	private final static String START_PLACEHOLDER = "XXX";

	private String textualFormula;
	private Text text;
	private List<StartFormulaNode> startPoints;

	//possible more than one element
	private FormulaShape parent;
	private FormulaShape child;

// records relative x and y co-ordinates.
	public FormulaNode(double x, double y, CanvasController canvasController) {
		super(new Rectangle(x, y, 100, 30), canvasController);
		startPoints = new ArrayList<>();
		textualFormula = "";

		//todo this also into super class as static or somehow better method
		//getShape().setOnMouseDragged((eventDragged) -> {
		//if (eventDragged.isPrimaryButtonDown()) {
		//double x1 = eventDragged.getX();
		//double y1 = eventDragged.getY();
		//moveTo(x1, y1);
		//}
//
		//});
	}

	public FormulaShape getChild() {
		return child;
	}

	public FormulaShape getMyParent() {
		return parent;
	}

	public void setMyParent(FormulaShape parent) {
		this.parent = parent;
	}

	@Override
	public double getX() {
		return getShape().getX() + getShape().getWidth() / 2;
	}

	@Override
	public double getY() {
		return getShape().getY() + getShape().getHeight() / 2;
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
		System.out.println("DeltaX: " + deltaX + " " + "DeltaY: " + deltaY);
		moveLinesBy(deltaX, deltaY);
		if (getShape() != null) {
			System.out.println("Shape X: " + getShape().getX() + " Y: " + getShape().getY());
			getShape().setX(getShape().getX() + deltaX);
			getShape().setY(getShape().getY() + deltaY);
		}
		if (text != null) {
			System.out.println("Text X: " + getShape().getX() + " Y: " + getShape().getY());
			text.setX(deltaX + text.getX());
			text.setY(deltaY + text.getY());
		}

		for(StartFormulaNode startNode : startPoints){
			startNode.moveBy(deltaX, deltaY);
		}
	}

	public void setText(String textToAdd) {
		textualFormula = textToAdd;
		text = createNewText(textToAdd);
		getShape().setWidth(JavaFxHelper.getWidth(text));
		getController().add(text); // order of adding to canvas matter, text should be added before starting points (mouseeevent handlers rely on this..)
		createStartingPoints(textToAdd);
	}

	private Text createNewText(String textToAdd) {
		Text newText = new Text(getShape().getX(), getShape().getY() + 20, textToAdd.replaceAll(START_PLACEHOLDER, "   "));
		newText.setFont(new Font(20));
		newText.setTextAlignment(TextAlignment.JUSTIFY);
		handOverEvents(newText);
		return newText;
	}

	private void handOverEvents(Node node) {
		node.setOnMousePressed((MouseEvent event) -> {
			Event.fireEvent(getShape(), event);
		});
		if(node instanceof Text){
			node.setOnMouseClicked((event) -> {
				Event.fireEvent(getShape(), event);
			});
		}
		node.setOnMouseDragged((event) -> {
			Event.fireEvent(getShape(), event);
		});
	}
// prvni XXX druhy XXX treti XXX ocasek

	private void createStartingPoints(String textToAdd) {
		String[] textFragments = textToAdd.split(START_PLACEHOLDER);
		int count = JavaFxHelper.countSubstringOccurencies(textToAdd, START_PLACEHOLDER);

		String textUsedSoFar = "";
		for (int i = 0; i < count; i++) {
			textUsedSoFar += textFragments[i] + "   ";
			Text textToComputeWidth = createNewText(textUsedSoFar);
			textToComputeWidth.setFill(Color.SEASHELL);
			double width = JavaFxHelper.getWidth(textToComputeWidth);
			StartFormulaNode startNode = new StartFormulaNode(getShape().getX() + width, getShape().getY() + 10, getController());
			startNode.getShape().setFill(Color.PINK);
			startPoints.add(startNode);
			handOverEvents(startNode.getShape());
		}
	}

}
