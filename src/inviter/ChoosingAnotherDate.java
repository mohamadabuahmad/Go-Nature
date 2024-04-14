package inviter;

import java.io.IOException;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entities.*;
import goNatureBoundry.popUpCC;
import inviterCC.OrderCC;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * this class works when there is no any left space in the park and the visitor wants to choose another order time
 * @author Eyas
 *
 */


public class ChoosingAnotherDate implements Initializable {

	static OrderPage orderp ;
	@FXML
	private AnchorPane ap;
	@FXML
	private TableView<Order> choosing_another_dateTime_TblV;

	@FXML
	private TableColumn<Order, Date> choosing_another_Date_col;

	@FXML
	private TableColumn<Order, Time> choosing_another_time_col;

	@FXML
	private Button choosing_another_order_Btn;
	
	@FXML
	private Button back_btn;
	

	Order or;
	
	
	/**
	 * Method that runs in the beginning!
	 * It initializes the Table View of the availability orders time and date.
	 * @param arg0 The URL location, which is used to resolve relative paths for the root object.
	 * @param arg1 The ResourceBundle, which is used to localize the root object.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {		
		ArrayList<Timestamp> availableorders = new ArrayList<Timestamp>();
		availableorders = OrderCC.fetchAvailibleOrderDates(OrderPage.newOrder.getVisitTime(), OrderPage.newOrder.getVisitDate(),OrderPage.newOrder.getNumOfVisitors(), OrderPage.newOrder.getPark().getName());
		ArrayList<Order> array2 = new ArrayList<Order>();
		for(int i =0;i<availableorders.size();i++) {
			Date d = new Date(availableorders.get(i).getYear(),availableorders.get(i).getMonth(), availableorders.get(i).getDate());
			Time t = new Time(availableorders.get(i).getHours(), availableorders.get(i).getMinutes(),0);
			array2.add(new Order(null,d , t, i, null, null, null, 0, null, false));
		}  		  		
		choosing_another_Date_col.setCellValueFactory(new PropertyValueFactory<Order, Date>("visitDate"));
		choosing_another_time_col.setCellValueFactory(new PropertyValueFactory<Order, Time>("visitTime"));
		choosing_another_dateTime_TblV.setItems(FXCollections.observableArrayList(array2));
		choosing_another_dateTime_TblV.refresh();
	}
	
	/**
	 * Handles the event of choosing a date and time from a table view,
	 * it launched when the inviter selects another order in the table view
	 * @param e The MouseEvent triggering the method call.
	 */
	@FXML
	void  handleSelectedDateTime(MouseEvent e) {		
			if(choosing_another_dateTime_TblV.getSelectionModel().getSelectedItem() != null) {
				or =choosing_another_dateTime_TblV.getSelectionModel().getSelectedItem();				
			}
	}	
	 
	/**
	 * Handles the action event of choosing another order,
	 * Method is launched when the inviter chooses another order time. 
	 * @param event The ActionEvent triggered by choosing another order.
	 */
	@FXML
	void handleChoosingAnotherOrder(ActionEvent event) {

		try {
			
    		Order newo = OrderPage.newOrder;
    	    if(choosing_another_dateTime_TblV.getSelectionModel().getSelectedItem() != null) {
    	    	newo.setVisitDate(or.getVisitDate());
    	    	newo.setVisitTime(or.getVisitTime());
    	    if (OrderCC.addOrder(newo)) {
			popUpCC aa = new popUpCC();
			aa.start(new Stage(), true, "We are Waitting For You!", "Your Order has been placed successfully!",
					"Ok , Great!");
			ap.getChildren().clear();
			ap.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("InviterPage.fxml")));
    	    }else
    	    	System.out.println("Your order doesn't placed !");
    	    }else
    	    {
    	    	System.out.println("Error! :you should Choose  time and date");    	
    	    }
		} catch (IOException e) {
		}
	}

	/**
     * Action handler for the "Back" button.
     * This method is invoked when the "Back" button is clicked by the user. It navigates back to the
     * main page (InviterPage) by loading the appropriate FXML file and setting it as the scene for the
     * current stage.
     * @param event The action event triggered by clicking the "Back" button.
     */
    @FXML
    void back(ActionEvent event) {


        	try {
    			Parent mainPageView = FXMLLoader.load(getClass().getResource("InviterPage.fxml"));
                Scene mainPageScene = new Scene(mainPageView);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(mainPageScene);
                window.show();

            } catch (IOException e) { 
                e.printStackTrace();
            }
    	}
	
}
