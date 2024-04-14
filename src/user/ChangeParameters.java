package user;

import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Client.ChatClient;
import entities.ParameterChangeRequest;
import entities.ParameterType;
import entities.Park;
import entities.ParkManager;
import goNatureBoundry.popUpCC;
import inviterCC.inputValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import userCC.ParkMangerCC;
/**
 * controller for the ChangeParameters
 * @author Shady
 *
 */

public class ChangeParameters implements Initializable{
	ParkManager park_Manager = (ParkManager) ChatClient.currentUser;
    @FXML
    private Button Change_stay_time_btn;

    @FXML
    private Button Change_max_num_visitor_btn;

    @FXML
    private Button Change_num_unplanned_visitorsBtn;

    @FXML
    private TextField StayTime_OldValue; 

    @FXML
    private TextField MaxNumVisitors_OldValue;

    @FXML
    private TextField MaxNumUnplanned_OldValue;

    @FXML
    private TextField StayTime_NewValue;

    @FXML
    private TextField MaxNumOfVisitor_NewValue;

    @FXML
    private TextField MaxNumUnplanned_NewValue;
    
    @FXML
    private Label msg;
    @FXML
	private Button BackBtn;
    
    
    /**
     * Handles the request of changing the maximum number of unplanned visitors, 
     * and insert it to the DataBase.
     * @param event The MouseEvent triggering the method call.
     */
    @FXML
    void Change_Max_Num_OfUnplanned_Visitors(MouseEvent event) {

    	long millis=System.currentTimeMillis();
    	ArrayList<Object> parameter  = ParkMangerCC.getParameter(park_Manager.getPark().getName());    
    	if(!inputValidator.checkInputIfNumber(MaxNumUnplanned_NewValue.getText()) || (Long.valueOf(MaxNumUnplanned_NewValue.getText()) > Long.valueOf(MaxNumVisitors_OldValue.getText()))) {
    		msg.setVisible(true);
    		MaxNumUnplanned_NewValue.setFocusTraversable(true);
    		msg.setText("Please Enter a Correct number"); 
    		
    	}else {
    		msg.setVisible(false);
         	ParameterChangeRequest req = new ParameterChangeRequest(ParameterType.MAXNUMBEROFORDER,String.valueOf((Integer)parameter.get(1)-Integer.valueOf(MaxNumUnplanned_NewValue.getText()))
    			, new Park(park_Manager.getPark().getName(), null, null, 0, 0)
    			, null, null);
         	//Insert the request to the dataBase.
    	    ParkMangerCC.setParameter(req, new java.sql.Date(millis));
    	
    	    popUpCC success=new popUpCC();
		    success.start(new Stage(),true, "Great!", "Your Request has been sent", "ok");
    	}
    }

    
    
    /**
     * Handles the request of changing the  max number of visitors ,
     * and insert it to the DataBase.
     * @param event The MouseEvent triggering the method call.
     */
    @FXML
    void Change_Number_Of_Visitort_Parameter(MouseEvent event) {
    	long millis=System.currentTimeMillis();
    	
    	if(!inputValidator.checkInputIfNumber(MaxNumOfVisitor_NewValue.getText()) ) {
    		msg.setVisible(true);
    		msg.setText("Please Enter a Correct number!");
    		
    	}else {
    		msg.setVisible(false);
    	ParameterChangeRequest req = new ParameterChangeRequest(ParameterType.MAXNUMBEROFVISITORS, MaxNumOfVisitor_NewValue.getText()
    			, new Park(park_Manager.getPark().getName(), null, null, 0, 0)
    			, null, null);
    	//Insert the request to the dataBase.
    	ParkMangerCC.setParameter(req, new java.sql.Date(millis));
    	
    	popUpCC success=new popUpCC();
		success.start(new Stage(),true, "Great!", "Your Request has been sent", "ok!");
    	}
    }
    
    
    /**
     * Handles the request of changing the stay time parameter,and insert it to the DataBase.
     * @param event The MouseEvent triggering the method call.
     */
    @FXML
    void Change_Stay_Time_Parameter(MouseEvent event) {
    	long millis=System.currentTimeMillis();
    	if(StayTime_NewValue.getText().equals("") || !inputValidator.checkTime(StayTime_NewValue.getText())) {
    		msg.setText("Please Enter a Correct Time");
    		msg.setVisible(true);
    	} else {
    		
    		msg.setVisible(false);
    	ParameterChangeRequest req = new ParameterChangeRequest(ParameterType.STAYTIME, StayTime_NewValue.getText()
    			, new Park(park_Manager.getPark().getName(), null, null, 0, 0)
    			, null, null);
    	ParkMangerCC.setParameter(req, new java.sql.Date(millis));

    	popUpCC success=new popUpCC();
		success.start(new Stage(),true, "Great!", "Your Request has been sent", "ok!");
    	}
    	
    }
    
    
	 public void BackBtn(ActionEvent actionEvent){
	        
	            Stage currentStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
		        currentStage.close();     
	       
	    }
    
    
	 /**
	  * Initializes the Park Manager GUI components by setting the labels for old parameter values.
	  * @param location   The URL location, which is used to resolve relative paths for the root object.
	  * @param resources  The ResourceBundle, which is used to localize the root object.
	  */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ArrayList<Object> par  = ParkMangerCC.getParameter(park_Manager.getPark().getName());
		StayTime_OldValue.setText(((Time)par.get(0)).toString());
		MaxNumVisitors_OldValue.setText(String.valueOf((Integer)par.get(1)));
		MaxNumUnplanned_OldValue.setText(String.valueOf((Integer)par.get(1)-(Integer)par.get(2)));	
	}
}
