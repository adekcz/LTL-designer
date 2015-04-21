/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer_prototype2.view.FormulaElements;

import cz.muni.fi.xkeda.ltl_designer_prototype2.settings.Settings;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JavaFxHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.util.JsonHelper;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.CanvasController;
import cz.muni.fi.xkeda.ltl_designer_prototype2.view.ListFileCell;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.scene.input.DragEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 *
 * @author adekcz
 */
public class StartingNode extends ConnectingNode {
	private boolean isInner = false;

	public boolean isInner() {
		return isInner;
	}

	public void setInner(boolean isInner) {
		this.isInner = isInner;
	}

	public StartingNode(double x, double y, CanvasController canvasController) {
		super(x, y, canvasController);
	}

	public StartingNode(double x, double y) {
		this(x, y, null);
	}

	@Override
	public void fillWithDefaultFill() {
		getShape().setFill(Color.web(Settings.get(Settings.START_POINT_COLOR)));
	}

	@Override
	public void delete() {
		getController().removeCompletely(this);
		if (getInEdge() != null) {
			getInEdge().delete();
		}
		if (getOutEdge() != null) {
			getOutEdge().delete();
		}
		disconnect();
		//TODO respectively, delete this and make deleting in connecting node correct
	}

	@Override
	public void setupGUIinteractions() {
		super.setupGUIinteractions(); //To change body of generated methods, choose Tools | Templates.
		if(isInner()){
			getController().removeFromAllNodes(this);
		}
	}

	@Override
	public void setupHandlers() {
		super.setupHandlers(); //To change body of generated methods, choose Tools | Templates.
		getShape().setOnDragDropped(handleDropppingSavedFormula());
	}

	protected EventHandler<DragEvent> handleDropppingSavedFormula() {
		return new EventHandler<DragEvent>() {
			public void handle(DragEvent event) {
				System.out.println("Handling DROPPED CON NODE");
				/* data is dragged over the target */
				/* accept it only if it is not dragged from the same node 
				 * and if it has a string data */
				double x = event.getX();
				double y = event.getY();

				File draggedFile = (File) event.getDragboard().getContent(ListFileCell.fileDragFormat);
				Text droppedText = new Text(x, y, event.getDragboard().getString());
				getController().add(droppedText);
				try {
					Map<Integer, AbstractNode> loadJson = JsonHelper.loadJson(draggedFile);
					StartingNode oldStart = getStartNode(loadJson);
					AbstractNode end  = oldStart.getOutEdge().getEnd();
					getController().addToControler(loadJson);
					oldStart.delete();
					StartingNode.this.connectGraphicallyTo(end);
				} catch (FileNotFoundException ex) {
					Logger.getLogger(CanvasController.class.getName()).log(Level.SEVERE, null, ex);
				}
				event.setDropCompleted(true);
				event.consume();
			}

			private StartingNode getStartNode(Map<Integer, AbstractNode> loadJson) {
				StartingNode start = null;
				for (AbstractNode node : loadJson.values()) {
					if (node instanceof StartingNode) {
						StartingNode currNode = (StartingNode) node;
						if (!currNode.isInner && start != null) {

							IllegalArgumentException ex = new IllegalArgumentException("Formula you selected has more then one Start point");
							JavaFxHelper.showErrorDialog("Multiple start points", ex);
							throw ex;
						}
						if(!currNode.isInner){
							start = (StartingNode) node;
						}
					}
				}
				if (start == null) {
					IllegalArgumentException ex = new IllegalArgumentException("Formula you selected has no Start point");
					JavaFxHelper.showErrorDialog("No start Points", ex);
					throw ex;
				}
				return start;
			}
		};

	}

}
