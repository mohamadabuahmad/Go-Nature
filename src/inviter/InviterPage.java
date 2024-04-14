package inviter;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Client.ClientUI;
import entities.*;
import inviterCC.OrderCC;
import inviterCC.WaitingListCC;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import Protocol.ClientMessage;

/**
 * @author anood
 *
 */


public class InviterPage implements Initializable {

	private Order selectedOrder;
	public static String idNumber_static;
	public static String subscriptionNumber_static;
	private ArrayList<Order> arrOrders = new ArrayList<Order>();
	
	@FXML
	private ImageView ICON1;
	
	@FXML
	private Label title_homepage_label;

	@FXML
	private Button logout_btn;
	@FXML
	private Button order_btn;
	@FXML
	private AnchorPane anPane;
	@FXML
	private AnchorPane WelcomTo;
	@FXML
	private Button profile_confirm_btn;
	@FXML
	private Button profile_cancel_btn;
	
	@FXML
	private TableView<Order> visitor_table_view;
	@FXML
	private TableColumn<Order, String> parkname_tableColumn;
	@FXML
	private TableColumn<Order, Date> dateOrder_tableColumn;
	@FXML
	private TableColumn<Order, Time> time_tableColumn;
	@FXML
	private TableColumn<Order, Integer> numOfVisitors_tableColumn;
	@FXML
	private TableColumn<Order, Double> priceOrder_tableColumn;
	@FXML
	private TableColumn<Order, String> statusOrder_tableColumn;
	@FXML
	private Label label_for_tableview;



	
	
	
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/inviter/InviterPage.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Inviter");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Logs out the current inviter.
	 * This method constructs and sends a LOGOUT message to the server to indicate that the current inviter
	 * want to log out. then waits for confirmation of the LOGOUT from the SERVER. 
	 * Upon successful logout confirmation, it proceeds to load the GoNature of the application.
	 * @param event The ActionEvent triggered by clicking the logout button.
	 */
	@FXML
	void logoutBtn(ActionEvent event) {
	    try {
	        ArrayList<Object> parameters = new ArrayList<>();
	        ClientMessage msg1 = new ClientMessage("LOGOUT", parameters, 0);
	        ClientUI.chat.accept(msg1);
	        switchToHomePage(event);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	/**
	 * Switches to the GoNature of the application.
	 * This method loads the GoNature from the FXML file "GoNature.fxml" and sets it as the
	 * scene for the current stage. It then shows the stage to display the GoNature.
	 * @param event The ActionEvent that triggers the switch to the GoNature.
	 * @throws IOException If an error occurs while loading the GoNature.
	 */
	private void switchToHomePage(ActionEvent event) throws IOException {
	    Parent homePage = FXMLLoader.load(getClass().getResource("/goNatureBoundry/GoNature.fxml"));
	    Scene scene = new Scene(homePage);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(scene);
	    stage.show();
	}
	

	/**
	 * Handles the action event when the order button is clicked.
	 * This method is invoked when the order button is clicked. It loads the "OrderPage.fxml" view 
	 * and displays it in a new stage. It then closes the current stage.
	 * @param event The ActionEvent triggered by clicking the order button.
	 */
    @FXML
    void order_btn(ActionEvent event) {
        navigateToPage("OrderPage.fxml", "Order Page", event);
    }

	  /**
     * Navigates to a specified FXML page.
     * 
     * @param fxmlFileName The name of the FXML file to navigate to.
     * @param pageTitle The title for the page.
     * @param event The ActionEvent that triggers the navigation.
     */
    private void navigateToPage(String fxmlFileName, String pageTitle, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource(fxmlFileName));
            Parent nextPage = loader.load();
            Scene scene = new Scene(nextPage);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.setTitle(pageTitle);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    /**
	 * Initializes and displays the InviterPage GUI.
	 * This method is called to initialize and display the InviterPage GUI. It loads the FXML file
	 * associated with the InviterPage, sets the scene with the loaded FXML file, sets the title of
	 * the stage to "Traveler Home Page", and finally displays the stage.
	 * @param primaryStage The primary stage where the InviterPage GUI will be displayed.
	 */
    private void fetchAndDisplayOrders() {
        ArrayList<Order> ordersForTraveler = OrderCC.getInviterOrders(idNumber_static);
        ArrayList<Order> waitingListOrders = WaitingListCC.getAllOrdersInWaitingListtoInviter(idNumber_static);
        
        // Make sure the orders are correctly being marked with "waiting-list"
        waitingListOrders.forEach(order -> order.setOrderStatus("waiting-list"));
        
        // Combine the regular and waiting list orders
        arrOrders.clear();
        arrOrders.addAll(ordersForTraveler);
        arrOrders.addAll(waitingListOrders);

        // After combining, refresh the table.
        setupTableColumns();
        visitor_table_view.setItems(FXCollections.observableArrayList(arrOrders));
        visitor_table_view.refresh();
    }

    // Double-check your table column setup
    private void setupTableColumns() {
        // Ensure these property values match exactly with the fields of the Order class
        dateOrder_tableColumn.setCellValueFactory(new PropertyValueFactory<>("visitDate"));
        time_tableColumn.setCellValueFactory(new PropertyValueFactory<>("visitTime"));
        parkname_tableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPark().getName()));
        numOfVisitors_tableColumn.setCellValueFactory(new PropertyValueFactory<>("numOfVisitors"));
        priceOrder_tableColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        statusOrder_tableColumn.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
    }

	/**
	 * This method is automatically invoked by the FXMLLoader when the associated FXML file is loaded.
	 * It retrieves information about orders and waiting list status for the current inviter.
	 * It populates the UI components with the retrieved data, such as upcoming orders or orders that need confirmation.
	 * 
	 * @param arg0 The URL used to resolve relative paths for the root object, or null if the location is not known.
	 * @param arg1 The ResourceBundle that was determined by the loader, or null if the loader didn't have a ResourceBundle or if the loader didn't support ResourceBundle loading.
	 */
	
	/**
	 * Handles the action event when the confirm button is clicked.
	 * This method is invoked when the confirm button is clicked in the UI.
	 * It retrieves the selected order from the TableView and confirms it accordingly.
	 * If the selected order is in the waiting status, it confirms the order directly.
	 * If the selected order is in the waiting-list status, it updates the order status to waiting and confirms it.
	 * @param event The action event triggered by clicking the confirm button.
	 */
    @FXML
    void actionConfirmBtn(ActionEvent event) {
        Order selectedOrder = visitor_table_view.getSelectionModel().getSelectedItem();
        if (selectedOrder != null && selectedOrder.getOrderStatus().equals("waiting")) {
            LocalDate visitDate = selectedOrder.getVisitDate().toLocalDate();
            LocalTime visitTime = selectedOrder.getVisitTime().toLocalTime();
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();

            // Check if the visit time is within the next 22 to 24 hours.
            boolean isMoreThan22HoursAhead = visitTime.isAfter(currentTime.plusHours(22)) || visitDate.isAfter(currentDate);
            boolean isWithin24Hours = visitTime.isBefore(currentTime.plusHours(24)) || visitDate.equals(currentDate.plusDays(1));

            if (isMoreThan22HoursAhead && isWithin24Hours) {
                // Confirm the order
                OrderCC.confirmOrder(selectedOrder);
            } else {
                // Cancel the order
                OrderCC.cancelOrder(selectedOrder);
            }
            refreshTableView(); // Refresh the table view to show updated status
        }
    }

	private void refreshTableView() {
	    LocalDate currentDate = LocalDate.now();
	    LocalTime currentTime = LocalTime.now();

	    arrOrders.clear();
	    arrOrders.addAll(OrderCC.getInviterOrders(idNumber_static));
	    arrOrders.addAll(WaitingListCC.getAllOrdersInWaitingListtoInviter(idNumber_static));
	    visitor_table_view.setItems(FXCollections.observableArrayList(arrOrders));

	    visitor_table_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null) {
	            LocalDate visitDate = newSelection.getVisitDate().toLocalDate();
	            LocalTime visitTime = newSelection.getVisitTime().toLocalTime();
	            boolean isWithin24Hours = !visitDate.isBefore(currentDate) && 
	                                      (visitTime.minusHours(24).isBefore(currentTime) || visitTime.equals(currentTime));
	            profile_confirm_btn.setDisable(
	                !newSelection.getOrderStatus().equals("waiting") || !isWithin24Hours
	            );
	        } else {
	            profile_confirm_btn.setDisable(true);
	        }
	    });

	    visitor_table_view.refresh();
	}


	/**
	 * Handles the action event when the cancel button is clicked.
	 * This method is invoked when the cancel button is clicked in the UI.
	 * It retrieves the selected order from the TableView and cancels it accordingly.
	 * If the selected order is in the waiting status, it cancels the order directly.
	 * If the selected order is in the waiting-list status, it cancels the order from the waiting-list.
	 * @param event The action event triggered by clicking the cancel button.
	 */
	@FXML
	void actionCancelBtn(ActionEvent event) {

		if (visitor_table_view.getSelectionModel().getSelectedItem() != null) {
			selectedOrder = visitor_table_view.getSelectionModel().getSelectedItem();
			if (selectedOrder.getOrderStatus().equals("waiting")) {
				OrderCC.cancelOrder(getOrderFromarrOrders(selectedOrder));
			} else if (selectedOrder.getOrderStatus().equals("waiting-list")) {
				WaitingListCC.cancelOrderFromWaitingList(getOrderFromarrOrders(selectedOrder));
			} else
				System.out.println("Wrong Wrong");
		}
		refreshTableView();
	}

	/**
	 * Retrieves the orders that need confirmation or cancellation for the inviter.
	 * This method checks the orders for the inviter and identifies those that need confirmation or cancellation.
	 * It considers the current date and time to determine if an order is ready for confirmation or cancellation.
	 * @param ordersForinviter                   The list of orders for the inviter.
	 * @param ordersForinviterInWaitingList      The list of orders for the inviter in the waiting list.
	 * @return                                    The list of orders that need confirmation or cancellation.
	 */
	public ArrayList<Order> getTheOrdersToConfirmOrCancel(ArrayList<Order> allOrders) {
	    LocalDate currentDate = LocalDate.now();
	    LocalTime currentTime = LocalTime.now();
	    ArrayList<Order> ordersToConfirmOrCancel = new ArrayList<>();
	    for (Order order : allOrders) {
	        LocalDate orderDate = order.getVisitDate().toLocalDate();
	        LocalTime orderTime = order.getVisitTime().toLocalTime().plusHours(2); // Adjusting for comparison
	        boolean isOrderDueForConfirmation = orderDate.isEqual(currentDate) && orderTime.isAfter(currentTime)
	        && ("waiting".equals(order.getOrderStatus()) || "waiting-list".equals(order.getOrderStatus()));

	        if (isOrderDueForConfirmation) {
	            ordersToConfirmOrCancel.add(order);
	        }
	    }

	    return ordersToConfirmOrCancel;
	}


	/**
	 * Retrieves the corresponding order from the list of orders for the inviter.
	 * This method searches for the order in the list of orders associated with the inviter.
	 * It matches the order based on the orderer, visit time, visit date, and park name.
	 * @param order   The order for which to retrieve the corresponding order from the list.
	 * @return        The corresponding order from the list of orders, or null if not found.
	 */
	public Order getOrderFromarrOrders(Order order) {

		for (int i = 0; i < arrOrders.size(); i++) {
			if (arrOrders.get(i).getOrderer().equals(order.getOrderer())) {
				if (arrOrders.get(i).getVisitTime().equals(order.getVisitTime())) {
					if (arrOrders.get(i).getVisitDate().equals(order.getVisitDate())) {
						if (arrOrders.get(i).getPark().getName().equals(order.getPark().getName())) {
							return arrOrders.get(i);
						}
					}
				}
			}
		}
		return null;
	}
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	    fetchAndDisplayOrders();
	    visitor_table_view.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
	        if (newSelection != null && newSelection.getOrderStatus().equals("waiting")) {
	            LocalDate visitDate = newSelection.getVisitDate().toLocalDate();
	            LocalTime visitTime = newSelection.getVisitTime().toLocalTime();
	            LocalDate currentDate = LocalDate.now();
	            LocalTime currentTime = LocalTime.now();
	            boolean isWithin24Hours = !visitDate.isBefore(currentDate) &&
	                                      (visitTime.minusHours(24).isBefore(currentTime) || visitTime.equals(currentTime));

	            profile_confirm_btn.setVisible(isWithin24Hours);
	            profile_confirm_btn.setDisable(!isWithin24Hours);
	        } else {
	            profile_confirm_btn.setVisible(false);
	            profile_confirm_btn.setDisable(true);
	        }
	    });
	    // Make sure to initially set the confirm button to invisible and disabled.
	    profile_confirm_btn.setVisible(false);
	    profile_confirm_btn.setDisable(true);
	}

	private void combineOrders() {
	    try {
	        ArrayList<Order> ordersForTravelerInWaitingList = WaitingListCC.getAllOrdersInWaitingListtoInviter(idNumber_static);
	        ordersForTravelerInWaitingList.forEach(order -> order.setOrderStatus("waiting-list"));
	        ArrayList<Order> ordersForTraveler = OrderCC.getInviterOrders(idNumber_static);	        
	        filterAndAddRelevantOrders(ordersForTraveler);
	        filterAndAddRelevantOrders(ordersForTravelerInWaitingList);
	       arrOrders.addAll(ordersForTraveler);
	        arrOrders.addAll(ordersForTravelerInWaitingList);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	private void filterAndAddRelevantOrders(ArrayList<Order> orders) {
	    LocalDate currentDate = LocalDate.now();
	    LocalTime currentTime = LocalTime.now();

	    orders.removeIf(order -> 
	        (order.getVisitDate().toLocalDate().isBefore(currentDate)) ||
	        (order.getVisitDate().toLocalDate().isEqual(currentDate) && 
	         order.getVisitTime().toLocalTime().isBefore(currentTime))
	    );
	}

	private void updateUIBasedOnOrderStatus() {
	    ArrayList<Order> ordersToConfirmOrCancel = getTheOrdersToConfirmOrCancel(arrOrders);
	    profile_confirm_btn.setVisible(!ordersToConfirmOrCancel.isEmpty());
	    label_for_tableview.setText(!ordersToConfirmOrCancel.isEmpty() ? "Please Confirm or Cancel Your Orders!" : "Your upcoming orders:"); 
	    profile_cancel_btn.setLayoutX(!ordersToConfirmOrCancel.isEmpty() ? 510 : 410);
	    
	    setupTableColumns(); 
	    refreshTableView(ordersToConfirmOrCancel.isEmpty() ? arrOrders : ordersToConfirmOrCancel); 
	}

	private void refreshTableView(ArrayList<Order> orders) {
	    visitor_table_view.setItems(FXCollections.observableArrayList(orders)); 
	    visitor_table_view.refresh();
	}


}