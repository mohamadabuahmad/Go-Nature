package user;

import java.net.URL;

import java.util.ArrayList;
import java.util.ResourceBundle;
import Client.ChatClient;
import entities.DepartmentManager;
import entities.ParameterChangeRequest;
import entities.ParameterType;
import entities.Park;
import entities.TableViewHelper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import userCC.DepartmentMangerCC;
import userCC.ReportsController;
import userCC.WorkerCC;

/**
 * controller for the ChangeParameters
 * @author Mohamad
 *
 */


public class DepartmentManagerParameters implements Initializable {

	@FXML
	private Button Req_confirm_btn;

	@FXML
	private TableColumn<TableViewHelper, String> Park_col;

	@FXML
	private TableColumn<TableViewHelper, String> type_col;

	@FXML
	private TableColumn<TableViewHelper, String> Send_date_col;

	@FXML
	private TableColumn<TableViewHelper, String> OldValue_col;

	@FXML
	private TableColumn<TableViewHelper, String> new_value_col;
	@FXML
	private Button back_btn;
	@FXML
	private Button rej_req_btn;

	@FXML
	private TableView<TableViewHelper> Para_req_tble;

	private TableViewHelper selected = null;

	private DepartmentManager d;
	
	
	
	
	/**
	 * Initializes the controller with the given URL and ResourceBundle.
	 * Retrieves the DepartmentManager object from the currentWorker of the ChatClient.
	 * Overall, this method initializes the TableView with appropriate cell value factories and populates it with parameter 
	 * change requests retrieved from the system.
	 * @param location The URL location, which is used to resolve relative paths for the root object.
	 * @param resources The ResourceBundle, which is used to localize the root object.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		d = (DepartmentManager) ChatClient.currentUser;
		Park_col.setCellValueFactory(new PropertyValueFactory<TableViewHelper, String>("parkName"));
		type_col.setCellValueFactory(new PropertyValueFactory<TableViewHelper, String>("name"));
		Send_date_col.setCellValueFactory(new PropertyValueFactory<TableViewHelper, String>("date"));
		OldValue_col.setCellValueFactory(new PropertyValueFactory<TableViewHelper, String>("oldValue"));
		new_value_col.setCellValueFactory(new PropertyValueFactory<TableViewHelper, String>("newValue"));
		Para_req_tble.setItems(get_request());
	}

	
	/**
	 * Handles the action event of clicking the confirm request button.
	 * If a parameter change request is selected, confirms the request by updating the 
	 * corresponding park parameter and removes the request from the Table View.
	 * @param event The ActionEvent triggered by clicking the confirm request button.
	 */
	@FXML
	void Confirm_Request(ActionEvent event) {
		if (selected != null) {
			Park park = null;
			for (Park p : d.getParks())
				if (selected.getParkName().equals(p.getName())) {
					park = p;
					break;
				}
			ParameterChangeRequest p = new ParameterChangeRequest(ParameterType.valueOf(selected.getName()),
			selected.getNewValue(), park, selected.getDate(), selected.getStatus());
			DepartmentMangerCC.confirmParameter(p, selected.getDate(), selected.getNewValue());
			Para_req_tble.getItems().removeAll(Para_req_tble.getSelectionModel().getSelectedItems());

		}

	}

	/**
	 * Handles the action event of clicking the reject button for a parameter change request.
	 * If a parameter change request is selected, it retrieves the associated park,
	 * creates a ParameterChangeRequest object, and unconfirms the parameter change request.
	 * @param event The ActionEvent triggered by clicking the reject button.
	 */
	@FXML
	void reject_request(ActionEvent event) {
		if (selected != null) {
			Park park = null;
			for (Park p1 : d.getParks())
				if (selected.getParkName().equals(p1.getName())) {
					park = p1;
					break;
				}
			ParameterChangeRequest p = new ParameterChangeRequest(ParameterType.valueOf(selected.getName()),
			selected.getNewValue(), park, selected.getDate(), selected.getStatus());
			DepartmentMangerCC.unconfirmParameter(p, selected.getDate(), selected.getNewValue());
			Para_req_tble.getItems().removeAll(Para_req_tble.getSelectionModel().getSelectedItems());

		}
	}

	/**
	 * Handles the event of selecting a parameter change request from the table view.
	 * If a parameter change request is selected, it updates the selectedRed variable with the selected item
	 * and converts the name of the request to uppercase with spaces removed.
	 * @param event The MouseEvent triggered by selecting a request from the table view.
	 */
	@FXML
	void request_that_selcted(MouseEvent event) {
		if (Para_req_tble.getSelectionModel().getSelectedItem() != null) {
			selected = Para_req_tble.getSelectionModel().getSelectedItem();
			String str = selected.getName();
			selected.setName(str.replaceAll("\\s+", "").toUpperCase());

		}
	}

	
	
	
	/**
	 * Retrieves from the DB a list of parameter change requests for all parks managed by the current department manager.
	 * @return An ObservableList of TableViewHelper objects representing parameter change requests.
	 */
	private ObservableList<TableViewHelper> get_request() {
		int i = 1;
		ObservableList<TableViewHelper> requests = FXCollections.observableArrayList();
		DepartmentManager dp = (DepartmentManager) ChatClient.currentUser;
		ArrayList<ParameterChangeRequest> result = null;
		for (Park p : dp.getParks()) {
			result = ReportsController.GetAllRequests(p.getName());
			if (result != null) {
				for (ParameterChangeRequest temp : result)
					if (temp instanceof ParameterChangeRequest)
						if (((ParameterChangeRequest) temp).getStatus().equals("waiting")) {
							TableViewHelper t = new TableViewHelper(i,
									ParameterType.getType(((ParameterChangeRequest) temp).getType()),
									((ParameterChangeRequest) temp).getStatus(),
									((ParameterChangeRequest) temp).getSendDate(),
									((ParameterChangeRequest) temp).getPark().getName());
							t.setClassType("Parameter");
							t.setOldValue(getOldParameter(t.getParkName(),
									((ParameterChangeRequest) temp).getType().toString()));
							t.setNewValue(((ParameterChangeRequest) temp).getNewValue());
							requests.add(t);
							i++;
						}
			}
		}
		return requests;
	}

	@FXML
	void back_btn(ActionEvent actionEvent) {
		Stage currentStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
	}
	
	
	
	/**
	 * Retrieves the old parameter value based on the park name and parameter type.
	 * @param name The name of the park.
	 * @param type The type of parameter for which the old value is being retrieved.
	 * @return The old parameter value corresponding to the given park name and type.
	 */
	private Object getOldParameter(String name, String type) {
		Park p = WorkerCC.checkParkVacancy(name);
		switch (type) {
		case "MAXNUMBEROFORDER":
			return p.getMaxNumOfOrders();
		case "MAXNUMBEROFVISITORS":
			return p.getMaxNumOfVisitor();
		case "STAYTIME":
			return p.getStayTime();
		default:
			break;
		}
		return null;
	}
}
