package user;
import Client.ChatClient;
import entities.Park;
import entities.ParkManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import userCC.WorkerCC;

/**
 * @author shady + marwa
 *
 */
public class ParkMangerAvailability {
	ParkManager ParkManager = (ParkManager) ChatClient.currentUser;
	
	   @FXML
	    private Button CheckVacancyBtn;
	   @FXML
		private Button BackBtn;
	    
	    
	    @FXML
	    private Label NumberOfCurrentVisitorLabel;

	    @FXML
	    private Label NumberOfAvailablePlaces;

	    
	    /**
	     * Checks the Availability of the park and updates the UI with the current number of visitors and available places.
	     * @param event The MouseEvent that triggers the action.
	     */
	    @FXML
	    void CheckVacancy(MouseEvent event) {
	    	Park park = WorkerCC.checkParkVacancy(ParkManager.getPark().getName());
	    	NumberOfCurrentVisitorLabel.setText(String.valueOf(park.getCurrentNumOfVisitors()));
	    	NumberOfAvailablePlaces.setText(String.valueOf(park.getMaxNumOfVisitor()-park.getCurrentNumOfVisitors()));
	    }
	    /**
	     * Closes the current stage.
	     * @param actionEvent The ActionEvent that triggers the action.
	     */
	    public void BackBtn(ActionEvent actionEvent) {
	        
            Stage currentStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
	        currentStage.close();
           
       
    
	    }


}
