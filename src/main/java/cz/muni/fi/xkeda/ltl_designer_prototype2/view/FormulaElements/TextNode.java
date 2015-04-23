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
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasState;
import java.util.Collections;
import java.util.List;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author adekcz
 */
public class TextNode extends AbstractNode<Rectangle> {

	private TextSubnode textSubnode;
	//todo implement saving to json
	private Loop loop;

	//possible more than one element
	private AbstractNode parent;

	public TextNode(double x, double y) {
		this(x, y, null);
	}

	protected TextNode(double x, double y, CanvasController canvasController) {
		super(new Rectangle(x, y, 100, 30), canvasController);
		textSubnode = new TextSubnode(this, canvasController);
	}

	@Override
	public void setupGUIinteractions() {
		super.setupGUIinteractions();
		textSubnode.setupGUIinteractions(getController());
		if (loop != null) {
			Loop.LoopType type = loop.getType();
			createLoop();
			loop.changeType(type);
		}
		if(getInEdge()!=null){
			getInEdge().getShape().setEndX(getRepresentativeX());
			getInEdge().getShape().setEndY(getRepresentativeY());
		}

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
				if (getController().getStatus() == CanvasState.CREATING_SELF_LOOP && loop == null) {
					createLoop();
					loop.showTypePopupChooser(event.getScreenX(), event.getScreenY());

				}

			}

		};
		getShape().setOnMouseClicked(newClickHandler);

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

	/**
	 * TODO FIX
	 * probably does not work unless underlying shape is already on canvas.
	 * @return 
	 */
	@Override
	public double getRepresentativeX() {
		return getShape().getX() + JavaFxHelper.getWidth(getShape()) / 2;
	}

	/**
	 * TODO FIX
	 * probably does not work unless underlying shape is already on canvas.
	 * @return 
	 */
	@Override
	public double getRepresentativeY() {
		return getShape().getY() + JavaFxHelper.getHeight(getShape()) / 2;
	}

	/**
	 *
	 * @return unmodified text under this formula
	 */
	public String getText() {
		return textSubnode.getTextualFormula();
	}

	@Override
	public void moveBy(double deltaX, double deltaY) {
		moveLinesBy(deltaX, deltaY);
		moveShapeBy(deltaX, deltaY);

		textSubnode.moveBy(deltaX, deltaY);
		if (loop != null) {
			loop.moveBy(deltaX, deltaY);
		}
		//TODO refactor, list containing shapes into abstarctNode, itarate over that list 
	}

	private void moveShapeBy(double deltaX, double deltaY) {
		if (getShape() != null) {
			getShape().setX(deltaX + getShape().getX());
			getShape().setY(deltaY + getShape().getY());
		}
	}

	@Override
	public Color getDefaultFill() {
		return Color.web(Settings.get(SettingsConstants.FORMULA_COLOR));
	}

	@Override
	public void delete() {
		super.delete();

		textSubnode.delete();

		if (loop != null) {
			loop.delete();
		}
		disconnect();

	}

	public Loop getLoop() {
		return loop;
	}

	public void setLoop(Loop loop) {
		this.loop = loop;
	}

	public List<ConnectingNode> getStartPoints() {
		return Collections.unmodifiableList(textSubnode.getStartPoints());
	}

	public void changeText(String text) {
		textSubnode.changeText(text);
	}

	public void setText(String string) {
		textSubnode.setText(string);
	}

	public void addStartPoint(StartingNode startPoint) {
		textSubnode.addStartPoint(startPoint);
	}

}
