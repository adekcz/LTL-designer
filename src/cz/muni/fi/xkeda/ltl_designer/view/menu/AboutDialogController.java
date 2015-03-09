/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.muni.fi.xkeda.ltl_designer.view.menu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author adekcz
 */
public class AboutDialogController implements Initializable {

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}	
    private Stage dialogStage;

    /**
     * Sets the stage of this dialog.
     * 
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

	/**
	 * Closes dialog
	 */
	@FXML
	public void handleCloseAction(){
		dialogStage.close();
	}


	//TODO possibly to superclass
	/**
	 * Returns <b> modal </b> Stage with about dialog. Just call stage.show()
	 * @param parent
	 * @return 
	 */
	public static Stage createAboutDialog(Node parent) {
		  Stage stage = new Stage();
    Parent root;
		try {
			root = FXMLLoader.load(AboutDialogController.class.getResource("AboutDialog.fxml"));
			stage.setScene(new Scene(root));
			stage.setTitle("About");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.initOwner(parent.getScene().getWindow());
			stage.setResizable(false);
		} catch (IOException ex) {
			Logger.getLogger(AboutDialogController.class.getName()).log(Level.SEVERE, null, ex);
		}
	return stage;

	}
	
}
