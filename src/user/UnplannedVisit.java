package user;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import Client.ChatClient;
import entities.Order;
import entities.OrderType;
import entities.Park;
import goNatureBoundry.Invoice;
import goNatureBoundry.popUpCC;
import inviterCC.IdentificationCC;
import inviterCC.OrderCC;
import inviterCC.PaymentController;
import inviterCC.inputValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import userCC.WorkerCC;

/**

* 
 * @author anood
 *
 */
public class UnplannedVisit implements Initializable {

    @FXML private ComboBox<OrderType> visitTypeComboBox;
    @FXML private Button showInvoiceBtn, calculatePriceBtn, backBtn, addOrderBtn;
    @FXML private TextField numOfVisitorsTextField, IDNumberTextField;
    @FXML private Label priceLabel, idNumberLabel, errorMsgLabel;
    @FXML private ComboBox<Integer> numOfVisitorsComboBox;

    private Order currentOrder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeComboBoxes();
        clearForm();
        setupListeners();
        visitTypeChanged(null); // Set the correct range when the form is first displayed
    }


    private void initializeComboBoxes() {
        visitTypeComboBox.getItems().addAll(OrderType.UNPLANNEDGUIDE, OrderType.UNPLANNEDTRAVELER);
        visitTypeComboBox.getSelectionModel().selectFirst();
        numOfVisitorsComboBox.getSelectionModel().selectFirst();
    }

    private void clearForm() {
        priceLabel.setText("");
        IDNumberTextField.setText("");
        IDNumberTextField.setPromptText("optional");
        errorMsgLabel.setVisible(false);
        showInvoiceBtn.setVisible(false);
    }

    private void setupListeners() {
        visitTypeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> updateIdNumberLabel());
    }

    private void updateIdNumberLabel() {
        idNumberLabel.setText(visitTypeComboBox.getValue() == OrderType.UNPLANNEDGUIDE ? "Guide Id : " : "Traveler Id : ");
        
    }

    @FXML
    private void calculatePrice(ActionEvent event) {
        if (!validateInput()) return;

        OrderType orderType = visitTypeComboBox.getValue();
        int numOfVisitors = numOfVisitorsComboBox.getValue();
        String idNumber = IDNumberTextField.getText().trim();

        currentOrder = createOrder(orderType, numOfVisitors, idNumber);
        // Ensure the price is calculated and set in the createOrder method as shown above
        priceLabel.setText(String.format("%.2f ₪", currentOrder.getPrice()));
        showInvoiceBtn.setVisible(true);
    }


    private boolean validateInput() {
        // Validate Order Type
        if (visitTypeComboBox.getValue() == null) {
            displayError("Please select an order type.");
            return false;
        }
          
        // Validate Number of Visitors
        Integer numOfVisitors = numOfVisitorsComboBox.getValue();
       if (numOfVisitors == null ) {
           displayError("You must select a Number of visitors .");
           return false;
       }
        // Validate ID Number for Unplanned Guide or Traveler
        OrderType selectedOrderType = visitTypeComboBox.getValue();
        String idNumber = IDNumberTextField.getText().trim();

        if (selectedOrderType == OrderType.UNPLANNEDGUIDE || selectedOrderType == OrderType.UNPLANNEDTRAVELER) {
            if (!inputValidator.checkId(idNumber)) {
                displayError("Incorrect ID format.");
                return false;
            }
            
            if (selectedOrderType == OrderType.UNPLANNEDGUIDE && IdentificationCC.checkGuide(idNumber) != 1) {
                displayError("No such guide found.");
                return false;
            }
        }

        // If all checks pass
        errorMsgLabel.setVisible(false); // Hide error message if previously shown
        return true;
    }

    private Order createOrder(OrderType orderType, int numOfVisitors, String idNumber) {
        Park park = ChatClient.currentUser.getPark();
        Date visitDate = Date.valueOf(LocalDate.now());
        Time visitTime = Time.valueOf(LocalTime.now());
        // Assuming the constructor sets a default price or calculates it based on other parameters
        Order order = new Order(park, visitDate, visitTime, numOfVisitors, null, idNumber, 
                                orderType, 0.0, "confirmed", false);
        
        // Example call to calculate price - adjust based on your actual logic
        double price = PaymentController.calculateOrderPrice(order);
        order.setPrice(price); // Assuming there's a method in Order to set the price

        return order;
    }




    @FXML
	void addOrder(ActionEvent event) {
		calculatePrice(null);
		if (currentOrder != null) {
			Park p = WorkerCC.checkParkVacancy(ChatClient.currentUser.getPark().getName());
			int maxNumOfUnplannedVisitors = p.getMaxNumOfVisitor() - p.getMaxNumOfOrders();
			int availableUnplannedVisitors = maxNumOfUnplannedVisitors - p.getCurrentNumOfUnplannedVisitors();
			if (currentOrder.getNumOfVisitors() <= availableUnplannedVisitors) {
				OrderCC.addOrder(currentOrder);
				popUpCC success = new popUpCC();
				success.start(new Stage(), true, "Great!.", "order added succesfully!", "Great!");
			} else {
				popUpCC failure = new popUpCC();
				failure.start(new Stage(), false, "oops.", "There is no  enough space in the park", "Ok");
			}
		}

	}
    @FXML
	void open_invoice(ActionEvent event) {
		calculatePrice(null);
		Invoice bc = new Invoice();
		String bill;
		if (currentOrder == null)
			return;
		bill = PaymentController.getInvoice(currentOrder);
		if (bill != null)
			bc.start(new Stage(), Date.valueOf(LocalDate.now()).toString(), currentOrder.getOrderer(), bill);
	}

    @FXML
    private void backBtn(ActionEvent actionEvent) {
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    private void displayError(String message) {
        errorMsgLabel.setText(message);
        errorMsgLabel.setVisible(true);
        priceLabel.setText("");
    }
    @FXML
    void visitTypeChanged(ActionEvent event) {
        OrderType visitType = visitTypeComboBox.getSelectionModel().getSelectedItem();
        numOfVisitorsComboBox.getItems().clear(); // Clear the combo box before adding new items
        
        switch (visitType) {
            case UNPLANNEDGUIDE:
                idNumberLabel.setText("Guide Id : ");
                numOfVisitorsComboBox.getItems().addAll(
                    IntStream.rangeClosed(1, 15).boxed().collect(Collectors.toList()));
                numOfVisitorsComboBox.getSelectionModel().selectFirst(); // Select the first item
                break;

            case UNPLANNEDTRAVELER:
                idNumberLabel.setText("Traveler Id : ");
                numOfVisitorsComboBox.getItems().addAll(
                    IntStream.rangeClosed(1, 9).boxed().collect(Collectors.toList()));
                numOfVisitorsComboBox.getSelectionModel().selectFirst(); // Select the first item
                break;

            default:
                break;
        }
    }

    }
