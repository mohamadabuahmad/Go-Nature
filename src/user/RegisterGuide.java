package user;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import entities.Inviter;
import goNatureBoundry.popUpCC;
import inviterCC.IdentificationCC;
import inviterCC.inputValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import userCC.ServiceRepresentativeCC;

/**
 * @author marwa
 *
 */
public class RegisterGuide implements Initializable{

	
	@FXML
	private Button addGuideButton;

	@FXML
	private Button backbtn;

	@FXML
	private TextField textFieldForId;
	
    @FXML
    private Label errorLabel;
    
    @FXML
    private ImageView visitor_guide_img;
    

    /**
     * This method handles the action event when the back button is clicked.
     * It loads the serviceRepresentivePage.fxml and displays it in the stage.
     * @param event the ActionEvent triggered by clicking the back button
     */
    @FXML
    void back(ActionEvent event) {
        	try {
    			Parent mainPageView = FXMLLoader.load(getClass().getResource("serviceRepresentivePage.fxml"));
                Scene mainPageScene = new Scene(mainPageView);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(mainPageScene);
                window.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
    	}
    /**
     * This method handles the action event when the add guide button is clicked.
     * It validates the input guide ID, checks if the guide already exists, and adds the guide if it doesn't.
     * It displays appropriate pop-up messages based on the outcome.
     * @param event the ActionEvent triggered by clicking the add guide button
     */
	@FXML
	void addGuide(ActionEvent event) {
	    String guideId = textFieldForId.getText();

	    switch (IdentificationCC.checkGuide(guideId)) {
	        case 1: // Guide already exists
	            displayPopUp("oops!!", "Guide is already exists");
	            break;
	        case 5: // Guide added successfully
	            ServiceRepresentativeCC.addNewGuide(new Inviter(guideId, true));
	            displayPopUp("Great!", "Guide added successfully");
	            break;
	        default: // Error or unknown case
	            if (!inputValidator.checkId(guideId)) {
	                errorLabel.setVisible(true);
	            } else {
	                Inviter traveler = new Inviter(guideId, true);
	                ServiceRepresentativeCC.addNewGuide(traveler);
	                displayPopUp("Great!", "Guide added successfully");
	            }
	            break;
	    }
	}

	private void displayPopUp(String title, String message) {
	    popUpCC popup = new popUpCC();
	    popup.start(new Stage(), true, title, message, "OK");
	    errorLabel.setVisible(false);
	}


	/**
	 * This method initializes the controller when the corresponding FXML file is loaded.
	 * It sets the visibility of the errorLabel to false.
	 * @param location  the URL location of the FXML file
	 * @param resources the ResourceBundle containing locale-specific objects
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		errorLabel.setVisible(false);

	}

}
