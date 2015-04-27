/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.textnode;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.SettingsConstants;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JavaFxHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasState;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.AbstractNode;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.ConnectingNode;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements.StartingNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author adekcz
 */
public class TextNode extends AbstractNode<TextField> {

	private Font textFont = Font.font("Monospaced", 20);

	//todo implement saving to json
	private Loop loop;

	private AbstractNode parent;

	private final static String START_PLACEHOLDER = Settings.get(SettingsConstants.PLACEHOLDER_FOR_FORMULA_START);

	private List<ConnectingNode> startPoints;

	/**
	 * @param x
	 * @param y
	 * @param controller
	 * @return Returns formula node. Rectangle with text, that might contain connectors to another
	 * formulas.
	 */
	public static TextNode createFormulaNode(double x, double y, CanvasController controller) {
		TextNode formulaNode = new TextNode(x, y, controller);
		formulaNode.setupGUIinteractions();
		return formulaNode;
	}

	protected TextNode(double x, double y, CanvasController canvasController) {
		super(new TextField(), canvasController);
		getShape().setLayoutX(x);
		getShape().setLayoutY(y);

		getShape().setFont(textFont);
		getShape().setPromptText("Formula goes here, (start placeholder is: " + Settings.get(SettingsConstants.PLACEHOLDER_FOR_FORMULA_START));
		getShape().setEditable(true);
		startPoints = new ArrayList<>();
	}

	public TextNode(double x, double y) {
		this(x, y, null);
	}

	//todo remove or move up
	private Integer clickCount = 0;

	@Override
	public void setupGUIinteractions() {
		super.setupGUIinteractions();
		handleEventsForStartingPoints();
		if (loop != null) {
			Loop.LoopType type = loop.getType();
			createLoop();
			loop.changeType(type);
		}
		if (getInEdge() != null) {
			getInEdge().getShape().setEndX(getRepresentativeX());
			getInEdge().getShape().setEndY(getRepresentativeY());
		}
		if (getOutEdge() != null) {
			getOutEdge().getShape().setStartX(getRepresentativeX());
			getOutEdge().getShape().setStartY(getRepresentativeY());
		}
	}

	public void handleEventsForStartingPoints() {
		//TODO FIXME BUG does not work, for some peculiar reason, if loaded from json, you can grab start points and move them individually,
		// even thought this code is definitely run 
		for (ConnectingNode startPoint : startPoints) {
			handOverDragged(startPoint.getShape());
			startPoint.setStandartPressHandler();
			startPoint.setStandartReleasedHandler();
		}
	}

	private void handOverDragged(Node node) {
		node.setOnMouseDragged((event) -> {
			Event.fireEvent(getShape(), event);
		});
	}

	@Override
	public void setupHandlers() {
		super.setupHandlers();
		handleClick();

		repairDraggingStupidHack();

		handleFocusChanged();

		handleKeyPressed();
		handleTextChanged();

	}

	private void handleTextChanged() {
		getShape().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> ob, String o, String n) {
				getShape().setPrefColumnCount(getShape().getText().length() + 2);
			}
		});
	}

	private void handleKeyPressed() {
		getShape().setOnKeyPressed((KeyEvent event) -> {
			if (event.getCode() == KeyCode.ENTER) {
				giveFocusAway();
				createStartingPoints(getShape().getText());
			}

		});
	}

	private void handleFocusChanged() {
		getShape().focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean isFocusedOld, Boolean isFocusedNew) {
				System.out.println("focusChange: " + clickCount + " newval: " + isFocusedNew);
				if (!isFocusedNew && clickCount == 2) {
					createStartingPoints(getShape().getText());
				}
			}
		});
	}

	private void repairDraggingStupidHack() {
		//this is ugly hack, dragging TextField is buggy, this, for some weird reason, works.
		Circle circle = new Circle();
		circle.setOnMousePressed(pressOnShape());
		circle.setOnMouseDragged(dragOnShape());

		getShape().setOnMousePressed((event) -> {
			System.out.println("In shape pressed");
			event.fireEvent(circle, event);
		});
		getShape().setOnMouseDragged((event) -> {
			System.out.println("In shape dragged");
			event.fireEvent(circle, event);
		});
	}

	private void handleClick() {
		EventHandler<? super MouseEvent> oldClickHandler = getShape().getOnMouseClicked();
		EventHandler<? super MouseEvent> newClickHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				System.out.println("NEW HANDLER, shiny, ");
				oldClickHandler.handle(event);
				if (getController().getStatus() == CanvasState.CREATING_SELF_LOOP && loop == null) {
					createLoop();
					loop.showTypePopupChooser(event.getScreenX(), event.getScreenY());
				}
				if (event.getClickCount() == 2) {
					System.out.println("BUM");
					clickCount = 2;
				} else {
					clickCount = 1;
					//Event.fireEvent(getParentShape(), event);
				}
			}
		};

		getShape().setOnMouseClicked(newClickHandler);
	}

	private void giveFocusAway() {
		getController().getCanvas().requestFocus(); //another hack...
	}

	/**
	 * adds text and creates starting points
	 *
	 * @param textToAdd public void changeText(String textToAdd) { getShape().setText(textToAdd);
	 * createStartingPoints(textToAdd); }
	 */
	public void createStartingPoints(String textToAdd) {
		System.out.println("texttoadd: " + textToAdd);
		getShape().setText(textToAdd.replaceAll(START_PLACEHOLDER, "   "));
		String[] textFragments = textToAdd.split(START_PLACEHOLDER);
		int count = JavaFxHelper.countSubstringOccurencies(textToAdd, START_PLACEHOLDER);

		createInnerElements(count, textFragments);
	}

	private void createInnerElements(int count, String[] textFragments) {
		String textUsedSoFar = "";
		for (int i = 0; i < count; i++) {
			textUsedSoFar += textFragments[i] + "   ";
			double width = computeTextWidth(textUsedSoFar);
			createInnerStartingPoint(width);
		}
	}

	private double computeTextWidth(String textUsedSoFar) {
		Text newText = new Text(textUsedSoFar.replaceAll(START_PLACEHOLDER, "   "));
		newText.setFont(textFont);
		System.out.println(JavaFxHelper.getWidth(newText));
		//return textUsedSoFar.length() * 11;
		return JavaFxHelper.getWidth(newText);
	}

	private void createInnerStartingPoint(double width) {
		StartingNode startNode = StartingNode.createInnerStartFormulaNode(getShape().getLayoutX() + width, getShape().getLayoutY() + 10, getController());
		startPoints.add(startNode);
		handOverDragged(startNode.getShape());
	}

	@Override
	public void moveBy(double deltaX, double deltaY) {
		moveLinesBy(deltaX, deltaY);
		moveTextBy(deltaX, deltaY);

		moveStartNodesBy(deltaX, deltaY);

		if (loop != null) {
			loop.moveBy(deltaX, deltaY);
		}
		//TODO refactor, list containing shapes into abstarctNode, itarate over that list 
	}

	private void moveTextBy(double deltaX, double deltaY) {
		if (getShape() != null) {
			getShape().setLayoutX(deltaX + getShape().getLayoutX());
			getShape().setLayoutY(deltaY + getShape().getLayoutY());
		}
	}

	private void moveStartNodesBy(double deltaX, double deltaY) {
		for (ConnectingNode startNode : startPoints) {
			startNode.moveBy(deltaX, deltaY);
		}
	}

	@Override
	public Color getDefaultFill() {
		return Color.web(Settings.get(SettingsConstants.FORMULA_COLOR));
	}

	@Override
	public void delete() {
		super.delete();

		for (ConnectingNode cn : startPoints) {
			cn.delete();
		}
		getController().removeFromCanvas(getShape());

		if (loop != null) {
			loop.delete();
		}
		disconnect();

	}

	private void createLoop() {
		loop = new Loop(getShape(), getController());
		loop.setupGUIinteractions();
	}

	public AbstractNode getMyParent() {
		return parent;
	}

	public void setMyParent(AbstractNode parent) {
		this.parent = parent;
	}

	public Loop getLoop() {
		return loop;
	}

	public void setLoop(Loop loop) {
		this.loop = loop;
	}

	public List<ConnectingNode> getStartPoints() {
		return Collections.unmodifiableList(startPoints);
	}

	public void setText(String changedText) {
		getShape().setText(changedText);
	}

	public String getText() {
		return getShape().getText();
	}

	public void addStartPoint(StartingNode node) {
		startPoints.add(node);
	}

	/**
	 * TODO FIX probably does not work unless underlying shape is already on canvas.
	 *
	 * @return
	 */
	@Override
	public double getRepresentativeX() {
		return getShape().getLayoutX() + JavaFxHelper.getWidth(getShape()) / 2;
	}

	/**
	 * TODO FIX probably does not work unless underlying shape is already on canvas.
	 *
	 * @return
	 */
	@Override
	public double getRepresentativeY() {
		return getShape().getLayoutY() + JavaFxHelper.getHeight(getShape()) / 2;
	}

}
