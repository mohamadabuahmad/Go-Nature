package user;

import java.net.URL;

import java.util.ArrayList;
import java.util.ResourceBundle;

import Client.ChatClient;
import entities.DepartmentManager;
import entities.Park;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import userCC.WorkerCC;

/**
 *
 * @author Marwa
 *
 */

public class DepManagerAvailability implements Initializable {

	@FXML
	private ComboBox<String> park_comobox;

	@FXML
	private Button chack_availability_btn;

	@FXML
	private Label Num_visitor_label;

	@FXML
	private Label available_Places_label;

	@FXML
	private Label available_unplanned_Places_lbl;
	@FXML
	private Button back_btn;
	
	/**
	 * Initializes the controller with the given URL and ResourceBundle.
	 * Retrieves the DepartmentManager object from the currentWorker of the ChatClient.
	 * Populates the parkCombo combo box with park names.
	 * Disables the check vacancy button if no park is selected.
	 * @param location The URL location, which is used to resolve relative paths for the root object.
	 * @param resources The ResourceBundle, which is used to localize the root object.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    DepartmentManager dmanager = (DepartmentManager) ChatClient.currentUser;
	    ArrayList<String> parkNames = new ArrayList<>();
	    for (Park p : dmanager.getParks()) {
	        parkNames.add(p.getName());
	    }
	    ObservableList<String> arr = FXCollections.observableArrayList(parkNames);
	    park_comobox.setItems(arr);
	    park_comobox.valueProperty().addListener((obs, oldVal, newVal) -> {
	        chack_availability_btn.setDisable(newVal == null);
	    });
	    chack_availability_btn.setDisable(true);
	}

	/**
	 * Handles the action event of clicking the check button to retrieve park vacancy information.
	 * Retrieves information about park vacancy based on the selected park from the combo box.
	 * Updates labels to display the current number of visitors, available places, and available unplanned places.
	 * @param event The ActionEvent triggered by clicking the check button.
	 */
	@FXML
	void Check_Availability(ActionEvent event) {
		Park p = WorkerCC.checkParkVacancy(park_comobox.getValue());
		Num_visitor_label.setText(String.valueOf(p.getCurrentNumOfVisitors()));
		available_Places_label.setText(String.valueOf(p.getMaxNumOfVisitor() - p.getCurrentNumOfVisitors()));
		available_unplanned_Places_lbl.setText(String.valueOf((p.getMaxNumOfVisitor() - p.getMaxNumOfOrders()) - p.getCurrentNumOfUnplannedVisitors()));

	}


	@FXML
	void back(ActionEvent actionEvent) {
		Stage currentStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
	}

}
