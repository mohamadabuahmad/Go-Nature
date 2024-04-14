package inviter;
import java.io.IOException;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.scene.control.DateCell;
import Client.ChatClient;
import entities.*;
import goNatureBoundry.popUpCC;
import inviterCC.OrderCC;
import inviterCC.PaymentController;
import inviterCC.WaitingListCC;
import inviterCC.inputValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author annod, eyas
 *
 */
public class OrderPage implements Initializable{

	
	private double price;
	public static Order newOrder=null;
	boolean check=false;
	private ArrayList<Park> arrParks ;
	@FXML
    private TextField phoneNumber_txtfield;
    @FXML
    private TextField email_txtfield;
    @FXML
    private ComboBox<Integer> numOfVisitors_combox;
    @FXML
    private DatePicker date_datePicker;
    @FXML
    private ComboBox<String> parks_combox;
    @FXML
    private Label price_label;
    @FXML
    private Label orderType_label;
    @FXML
    private Button order_btn;   
    @FXML
    private AnchorPane an;  
    @FXML
    private ComboBox<String> minutes_combox;
    @FXML
    private ComboBox<String> hours_combox;    
    @FXML
    private AnchorPane orderPage_anPane;
    @FXML
    private Button enter_WL_btn;
    @FXML
    private Button get_another_date_btn;
    @FXML
    private Label error_msg_label;
    @FXML
    private CheckBox pay_checkBox;
    @FXML
    private ImageView date_img;
	@FXML
	private Button backbtn;
    
	/**
	 * this method checks if the check box is selected,if yes all the relevant labels will be visible
	 * @param event -javaFx ActionEvent,the event that started this method 
	 */
	@FXML
	void actionComboBox(ActionEvent event) {
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
    			Parent mainPageView = FXMLLoader.load(getClass().getResource("/inviter/InviterPage.fxml"));
                Scene mainPageScene = new Scene(mainPageView);
                Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                window.setScene(mainPageScene);
                window.show();

            } catch (IOException e) { 
                e.printStackTrace();
            }
    	}
    
    /**
     * Action handler for the "Get Another Date" button.
     * This method is invoked when the "Get Another Date" button is clicked by the user. It loads the
     * AvailibilityTableGUI.fxml file and sets its contents as the children of the anchor pane (anPane_anchore).
     * @param event The action event triggered by clicking the "Get Another Date" button.
     */
    @FXML
	void get_another_date(ActionEvent event) {
     	try {
			Parent mainPageView = FXMLLoader.load(getClass().getResource("/inviter/ChoosingAnotherDate.fxml"));
            Scene mainPageScene = new Scene(mainPageView);
            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
            window.setScene(mainPageScene);
            window.show();

        } catch (IOException e) { 
            e.printStackTrace();
        }
	}
    
    /**
     * Action handler for entering the waiting list.  
     * This method is invoked when the user chooses to enter the waiting list for a new order. It attempts to add the
     * new order to the waiting list using WaitingListCC.enterWaitingList method. If successful, it opens the InviterPage
     * and displays a pop-up message indicating successful entry into the waiting list. If the waiting list is full,
     * it displays a pop-up message indicating that the waiting list is full and suggests choosing another date.
     * @param event The action event triggered by clicking the "Enter Waiting List" button.
     */
	@FXML
	void Enter_waiting_list(ActionEvent event) {
		try {
			if(WaitingListCC.enterWaitingList(newOrder)) {
			InviterPage a = new InviterPage();
			a.start(new Stage());
			((Node) event.getSource()).getScene().getWindow().hide();
			popUpCC aa = new popUpCC();
			aa.start(new Stage(), true, "Yor Are Now In the  Waiting List", "We Will Keep In Touch with you ", "Ok , Great!");
			}else {
				popUpCC aa = new popUpCC();
				aa.start(new Stage(), false, " The Waiting List Is Full!", "You Can Choose Another Date", "Ok !");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Action handler for processing an order.
	 * This method is invoked when the user clicks the "Order" button to confirm and place the order.
	 * It first checks if all necessary information for the order is provided and if a new order object has been created.
	 * Then, it checks if the order already exists for the same visitor at the same park and time.
	 * If the order does not exist, it checks the visit date and time to determine the order status.
	 * If the visit date is tomorrow or today and the visit time is in the future, the order status is set to "confirmed".
	 * If the visit date is today and the visit time is in the past, the order status is also set to "confirmed".
	 * If the visit date is today and the visit time is in the future, the order status is not set and an error message is displayed.
	 * If all conditions are met, the method attempts to add the order using OrderCC.addOrder method.
	 * If the order is added successfully, a success pop-up message is displayed, and the user is redirected to the InviterPage.
	 * If the order already exists, an error message is displayed indicating that the order already exists.
	 * If any required information is missing, an error message is displayed prompting the user to provide all necessary details.
	 * @param event The action event triggered by clicking the "Order" button.
	 */
	@FXML
	void clicked_on_order_btn(ActionEvent event) {
		if (check == true && newOrder != null) {
			ArrayList<Order> ordersForTraveler = new ArrayList<Order>();
			ordersForTraveler = OrderCC.getInviterOrders(newOrder.getOrderer());
			boolean existOrder = false;
			for (int i = 0; i < ordersForTraveler.size(); i++) {
				if (ordersForTraveler.get(i).getVisitTime().compareTo(newOrder.getVisitTime()) == 0) {
					if (ordersForTraveler.get(i).getVisitDate().compareTo(newOrder.getVisitDate()) == 0) {
						if (ordersForTraveler.get(i).getPark().getName().equals(newOrder.getPark().getName())) {
							existOrder = true;
			}	}	}	}	
			if (!existOrder) {
			    Date visitDate = newOrder.getVisitDate();
			    Time visitTime = newOrder.getVisitTime();
			    LocalDate localVisitDate = visitDate.toLocalDate(); // Correct conversion to LocalDate

			    if (ChronoUnit.DAYS.between(LocalDate.now(), localVisitDate) == 1 && visitTime.compareTo(Time.valueOf(LocalTime.now())) <= 0) {
			        newOrder.setOrderStatus("confirmed");
			    }
			    if (ChronoUnit.DAYS.between(LocalDate.now(), localVisitDate) == 0 && visitTime.compareTo(Time.valueOf(LocalTime.now())) >= 0) {
			        newOrder.setOrderStatus("confirmed");
			    }
			    if (ChronoUnit.DAYS.between(LocalDate.now(), localVisitDate) == 0 && visitTime.compareTo(Time.valueOf(LocalTime.now())) <= 0) {
			        newOrder = null;
			        error_msg_label.setText("the time is passed!");
			        error_msg_label.setVisible(true);
			        return;
			    } 
			    //error_msg_date_label.setVisible(false);
			    
				if (OrderCC.addOrder(newOrder)) {
					error_msg_label.setVisible(false);
					popUpCC aa =new popUpCC();
					aa.start(new Stage(), true, "we're excited to see you!", "Your Order has been placed successfully!", "close");
					try {
						orderPage_anPane.getChildren().clear();
						orderPage_anPane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("InviterPage.fxml")));
					} catch (IOException e) {}					
				} else {
					 try {
						error_msg_label.setVisible(false);
						orderPage_anPane.getChildren().clear();
						orderPage_anPane.getChildren().setAll((Node) FXMLLoader.load(getClass().getResource("DenyOrder.fxml")));												
					} catch (Exception e1) {
					}
				}
			} else { 
				error_msg_label.setText("the order is all ready exit");
				error_msg_label.setVisible(true);
				}
		} else {
			error_msg_label.setText("*Error : All The Fields Are Required");
			error_msg_label.setVisible(true); 	}
	}
	
	/**
	 * This method is automatically called after the FXML file has been loaded.
	 * It sets up the initial state of UI components, populating them with necessary data and setting up event listeners.
	 * 
	 * @param arg0 The URL location of the FXML file to initialize.
	 * @param arg1 The resources used to localize the FXML file's content.
	 */
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	try {					
			date_datePicker.setDayCellFactory(picker -> new DateCell() {
		        public void updateItem(LocalDate date, boolean empty) {	
		            super.updateItem(date, empty);
		            LocalDate today = LocalDate.now();
		            setDisable(empty || date.compareTo(today) < 0 );
		        }
		    });		
			
			arrParks = new ArrayList<Park>();
			arrParks = OrderCC.getAllParksforClient();
			for (Park e1 : arrParks) {
				parks_combox.getItems().addAll((String) e1.getName());
			}
		} catch (Exception e) {			
		}
		try {

				if (ChatClient.currentInviter ==null) {
					orderType_label.setText("Ordinary visitor");
					for (int i = 1; i <= 9; i++) {
						numOfVisitors_combox.getItems().addAll((Integer) i);
				}		}
				
				else
				if(ChatClient.currentInviter.isGuide() == true) {
					orderType_label.setText("Guide  visitor");			
					for (int i = 1; i <= 15; i++) {
						numOfVisitors_combox.getItems().addAll((Integer) i);
					}	
					
				}else {
					
					numOfVisitors_combox.getItems().clear();
					orderType_label.setText("Ordinary visitor");
					for (int i = 1; i <= 9; i++) {
						numOfVisitors_combox.getItems().addAll((Integer) i);
				}		}	
				
		} catch (Exception e) {			
		}
		try {
			for (Integer i = 8; i <= 18; i++) {
				hours_combox.getItems().addAll(i.toString());
			}
			for (Integer i = 0; i <= 59; i++) {
				minutes_combox.getItems().addAll(i.toString());
			}
		} catch (Exception e) {
		
	}
    }
    	
    
    /**
     * Handles the mouse event triggered when the user interacts with a component related to calculating the price.
     * Invokes the method to calculate the price based on user input.
     * @param event The MouseEvent that triggered the method call.
     */
    @FXML
    void mouseEventCalcPrice(MouseEvent event) {
    	calculateThePrice(null);
    	}
    
    
    /**
     * Calculates the price of the order based on the user input.
     * Retrieves necessary data such as park information, date, time, number of visitors, email, and telephone number.
     * Calculates the price of the order using the PaymentController.
     * Updates the price_order_label with the calculated price.
     * If the payNow_checkBox is selected, sets the order status to "waiting" and indicates payment has been made.
     * @param e The ActionEvent that triggered the method call.
     */
	@FXML
	void calculateThePrice(ActionEvent e) {
		
		try {
		try {
			Park parkOrder = null;

			for (Park e1 : arrParks) {
				if (e1.getName().equals(parks_combox.getValue())) {
					parkOrder = e1;
				}
			}
			OrderType orderType;
			if (orderType_label.getText().equals("Ordinary visitor")) {
				orderType = OrderType.PLANNEDTRAVELER;
			} else  {
				orderType = OrderType.PLANNEDGUIDE;
			} 
			@SuppressWarnings("deprecation")

			LocalDate localDate = date_datePicker.getValue();
			Date date = Date.valueOf(localDate);			
			int hours = Integer.parseInt(hours_combox.getValue());
			int minutes = Integer.parseInt(minutes_combox.getValue());
			@SuppressWarnings("deprecation")
			Time time = new Time(hours, minutes, 0);
			String id = InviterPage.idNumber_static;
			String telephoneNumber = phoneNumber_txtfield.getText();
			int numberOfVisitors = numOfVisitors_combox.getValue();
			String email = email_txtfield.getText();		
		
			if (checkInputValidity()) {
                newOrder = new Order(parkOrder, date, time, numberOfVisitors, id, null, orderType, 0.0, null, false);
                price = PaymentController.calculateOrderPrice(newOrder);
                price_label.setText(String.valueOf(price) + " nis");
                if (pay_checkBox.isSelected()) {
                    newOrder = new Order(parkOrder, date, time, numberOfVisitors, email, id, orderType, price, "waiting", true, false, telephoneNumber);
                } else {
                    newOrder = new Order(parkOrder, date, time, numberOfVisitors, email, id, orderType, price, "waiting", false, false, telephoneNumber);
                }
                check = true;
            }
																											
		} catch (Exception e2) {}
		}catch (IllegalArgumentException e4) {		
		}
	}

	
	private boolean checkInputValidity() {
	    if (numOfVisitors_combox.getValue() != null &&
	            date_datePicker.getValue() != null &&
	            parks_combox.getValue() != null &&
	            hours_combox.getValue() != null &&
	            minutes_combox.getValue() != null &&
	          	inputValidator.checkEmail(email_txtfield.getText()) &&
	           checktelephone(phoneNumber_txtfield.getText())){
	           
	        return true;
	    }
	    return false;
	}
	/**
	 * Checks if the given telephone number is valid.
	 * @param id The telephone number to be checked.
	 * @return true if the telephone number is valid, false otherwise.
	 */
	private boolean checktelephone(String id) {
		if (id.length() > 8 && id.length() < 12 ) {// checking length of id number,should be 9		
			for (int i = 0; i < id.length(); i++)
				if (!Character.isDigit(id.charAt(i)))// if one character is not a digit then id isn't valid return false
					return false;
			return true;
		}
		return false;
	}
	
	
	
}
