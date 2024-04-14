package user;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import userCC.ParkEntranceAndExitCC;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import Client.ChatClient;
import entities.Order;
import goNatureBoundry.Invoice;
import inviterCC.PaymentController;
import inviterCC.inputValidator;
import javafx.event.ActionEvent;
/**
 * controller for the ChangeParameters
 * @author Mohamad
 *
 */

public class ExitorEnter {

    @FXML
    private Label title;
    @FXML
    private TextField id_txtfield;
    @FXML
    private Button check_id_btn;
    @FXML
    private Label id_label; 
    @FXML
    private Label date_label;
    @FXML
    private Label time_order_label; 
    @FXML
    private Label price_label; 
    @FXML
    private Label num_visitors_label; 
    @FXML
    private Label type; 
    @FXML
    private Label status_pay_label; 
    @FXML
    private Label phone_label;
    @FXML
    private Button enter_btn;
    @FXML
    private Label id_format;
    @FXML
    private Button back_btn;
    @FXML
    
    private Button exit_btn;
    private Order currOrder=null; 
    
    
    
    /**
	 * Initializes the UI components for the order confirmation.
	 * This method sets the visibility of orderIdStatusLabel, ExitButton, and EnterButton
	 * to false during initialization, ensuring they are not initially displayed on the UI.
	 * These components are typically made visible as needed during user interactions.
	 * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if the root object was not localized.
	 */
	public void initialize(URL location, ResourceBundle resources) {
		id_format.setVisible(false);
		exit_btn.setVisible(false);
		enter_btn.setVisible(false);
		
	}
   
    /**
     * Handles the action event of checking an order ID.
     * This method retrieves the order ID entered by the user from the text field.
     * It then invokes the appropriate method to identify the order associated with the provided ID within the current park.
     * If the order is found, it updates the UI with the order details and sets up the entrance or exit confirmation accordingly.
     * If the order is not found, it displays an error message indicating that the order ID doesn't exist.
     * @param event The ActionEvent triggered by clicking the check ID button.
     */
    @FXML
    void handle_check_id(ActionEvent event) {
    	String id=id_txtfield.getText();
     	if(inputValidator.checkId(id))
    	{
    	Order order=ParkEntranceAndExitCC.identify(id, ChatClient.currentUser.getPark().getName());
    	if(order!=null) {
    		currOrder=order;
    		display_order_details(order);
    		id_format.setVisible(false); 
    		enter_btn.setVisible(true);
    		if(order.isArrived())
    		{
    			if(order.getExit()!=null)
    			{
    				id_format.setText("order id doesn't exist");
            		id_format.setVisible(true);
            		currOrder=null;
            		enter_btn.setVisible(false);
            		reset_order_details();
            		return;
    			}
    			enter_btn.setText("Confirm Exit");
    		}
    		else {
    			
    			enter_btn.setText("Confirm Entrance");
			}
    		
		}
    		else {
    			id_format.setText("order id doesn't exist");
        		id_format.setVisible(true);
        		currOrder=null;
        		enter_btn.setVisible(false);
        		reset_order_details();
    		}
    	
    }}
    
  
    
    /**
     * Fills the UI with details of the provided order.
     * This method updates the UI components with details retrieved from the provided order object.
     * It sets the ID number label, date label, time label, price label, number of visitors label, order type label, 
     * payment status label, and phone number label with corresponding information from the order.
     * @param order The Order object containing details to be displayed on the UI.
     */
    public void display_order_details(Order order)
    {
    	
    	id_label.setText(String.valueOf(order.getOrderer())); 
        date_label.setText(order.getVisitDate().toString());
        time_order_label.setText(order.getVisitTime().toString());
        price_label.setText(String.format("%.2f", order.getPrice()));
        num_visitors_label.setText(String.valueOf(order.getNumOfVisitors()));
        type.setText(order.getOrderType().toString());
        
        String paymentStatus = order.getPayStatus() ? "Paid" : "Not Paid";
        status_pay_label.setText(paymentStatus);
        
        phone_label.setText(order.getPhoneNumber());
    }


    
    /**
     * Clears the UI components displaying order details.
     * This method resets the text of UI components used to display order details,
     * effectively clearing them from the UI.
     */
    public void reset_order_details()
    {
    	id_label.setText("");
    	date_label.setText("");
    	time_order_label.setText("");
    	price_label.setText("");
    	num_visitors_label.setText("");
    	type.setText("");
    	status_pay_label.setText("");
    	phone_label.setText("");
    }
  
    
    
    
    
    /**
     * Handles the action event of confirming entrance or exit of an order.
     * This method retrieves the text displayed on the EnterButton to determine whether
     * the action is confirming entrance or exit. If confirming entrance, it checks
     * if the order's payment status is unpaid, and if so, generates a bill and displays it.
     * Then, it identifies the order at the entrance. If confirming exit, it identifies
     * the order at the exit and clears the order details from the UI.
     * @param event The ActionEvent triggered by clicking the confirm entrance or exit button.
     */ 
    @FXML
    void handle_confirm_enter(ActionEvent event) {
    	 String action = enter_btn.getText();
    	    if (action.equals("Confirm Entrance") && currOrder != null) {
    	        if (!currOrder.getPayStatus()) {
    	            Invoice invoice = new Invoice();
    	            String invo1 = PaymentController.getInvoice(currOrder);
    	            if (invo1 != null)
    	                invoice.start(new Stage(), Date.valueOf(LocalDate.now()).toString(), currOrder.getOrderer(), invo1);
    	        }
    	        ParkEntranceAndExitCC.identifyAtEntrance(currOrder.getOrderer(), currOrder.getPark().getName());
    	        enter_btn.setText("Confirm Exit");
    	    } else if (action.equals("Confirm Exit") && currOrder != null) {
    	        ParkEntranceAndExitCC.identifyAtExit(currOrder.getOrderer(), currOrder.getPark().getName());
    	        reset_order_details();
    	        currOrder = null;
    	        enter_btn.setVisible(false);
    	        id_txtfield.setText("");
    	    }
    }

	@FXML
	void back(ActionEvent actionEvent) {
		Stage currentStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
	}

	
	
	
	
}
