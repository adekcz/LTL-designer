/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JavaFxHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.event.Event;
import javafx.event.EventHandler;
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

	private final static String START_PLACEHOLDER = Settings.get(Settings.PLACEHOLDER_FOR_FORMULA_START);

	private String textualFormula;
	private Text text;
	private final List<ConnectingNode> startPoints;
	//todo implement saving to json
	private Loop loop;

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
	}

	@Override
	public void setupHandlers() {
		super.setupHandlers(); //To change body of generated methods, choose Tools | Templates.
		EventHandler<? super MouseEvent> oldClickHandler = getShape().getOnMouseClicked();
		EventHandler<? super MouseEvent> newClickHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				System.out.println("NEW HANDLER, shiny, ");
				oldClickHandler.handle(event);
				if (getController().getStatus() == CanvasStatus.CREATING_SELF_LOOP && loop == null) {
					loop = new Loop(getShape().getX() + getShape().getWidth(), getShape().getY(), getController());
					loop.setupGUIinteractions();
					System.out.println(event.getSceneX());
					System.out.println(event.getSceneY());
					System.out.println(event.getScreenX());
					System.out.println(event.getScreenY());
					loop.showTypePopupChooser(event.getScreenX(), event.getScreenY());

				}

			}

		};
		getShape().setOnMouseClicked(newClickHandler);

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

	@Override
	public void moveBy(double deltaX, double deltaY) {
		moveLinesBy(deltaX, deltaY);
		moveShapeBy(deltaX, deltaY);
		moveTextBy(deltaX, deltaY);
		moveStartNodesBy(deltaX, deltaY);
		if (loop != null) {
			loop.moveBy(deltaX, deltaY);
		}
		//TODO refactor, list containing shapes into abstarctNode, itarate over that list 
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
			getShape().setX(deltaX + getShape().getX());
			getShape().setY(deltaY + getShape().getY());
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

	private void createGUItext() {
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
		//TODO FIXME BUG does not work, for some peculiar reason, if loaded from json, you can grab start points and move them individually,
		// even thought this code is definitely run 
		for (ConnectingNode startPoint : startPoints) {
			handOverEvents(startPoint.getShape());
		}
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
			System.out.println("Event hadoverd");
			Event.fireEvent(getShape(), event);
		});
		node.setOnMouseClicked((event) -> {
			Event.fireEvent(getShape(), event);
		});
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
		StartingNode startNode = FormulaShapeFactory.createInnerStartFormulaNode(getShape().getX() + width, getShape().getY() + 10, getController());
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
	public Color getDefaultFill() {
		return Color.web(Settings.get(Settings.FORMULA_COLOR));
	}

	@Override
	public void delete() {
		super.delete();
		for (ConnectingNode cn : startPoints) {
			cn.delete();
		}
		getController().removeFromCanvas(text);
		if (loop != null) {
			loop.delete();
		}
		disconnect();

	}

}
