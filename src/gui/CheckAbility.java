package gui;



import Client.ChatClient;
import controllers.WorkerCC;
import entities.Park;
import entities.ParkManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class CheckAbility {
	ParkManager ParkManager = (ParkManager) ChatClient.currentWorker;
	
	   @FXML
	    private Button CheckAbilityBtn;

	    @FXML
	    private Label abilityNumOfVisitor;

	    @FXML
	    private Label currentNumOfVisitors;

	    
	
	    
	    /**
	     * this function show number of current visitors and number of availability places in the park 
	     * it Edit the labels when the user click the chickParkVacancey Button 
	     * @param event
	     */
	    @FXML
	    void Check_Ability(MouseEvent event) {
	    	Park park = WorkerCC.CheckAbility(ParkManager.getPark().getName());
	    	currentNumOfVisitors.setText(String.valueOf(park.getCurrentNumOfVisitors()));
	    	abilityNumOfVisitor.setText(String.valueOf(park.getMax_num_of_visitores()-park.getCurrentNumOfVisitors()));
	    }


}
