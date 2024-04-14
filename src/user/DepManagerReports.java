package user;


import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Client.ChatClient;
import entities.DepartmentManager;
import entities.Park;
import entities.ReportType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import userCC.DepartmentMangerCC;
/**
 * controller for the ChangeParameters
 * @author Mohamad
 *
 */
public class DepManagerReports implements Initializable {
	DepartmentManager Dep = (DepartmentManager) ChatClient.currentUser;
	
	@FXML
	private Button reportbtn;

	@FXML
	private ComboBox<String> type_combox;

	@FXML
	private DatePicker mounthreport_datepicker;

	@FXML
	private ComboBox<String> selectpark_combox;

	@FXML
	private Label error_msglabel;
	
	@FXML
	private Button back_btn;
	
	String Name_of_park;
	
	
	/**
	 * Initializes the Department Manager page with relevant data.
	 * This method initializes the Department Manager page by populating the park selection combo box and report type combo box
	 * with data obtained from the current worker's associated parks and available report types.
	 * It retrieves the Department Manager object from the current worker and fetches the list of parks associated with the manager.
	 * The park names are then added to the park selection combo box.
	 * Additionally, it populates the report type combo box with predefined report types, such as "Visits Report" and "Cancelled Order Report",
	 * and selects the first report type by default.
	 * 
	 * @param arg0 The URL to initialize the resource.
	 * @param arg1 The ResourceBundle containing locale-specific objects.
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		DepartmentManager depManager = (DepartmentManager) ChatClient.currentUser;
		ArrayList<String> names_of_parks = new ArrayList<String>();
		for (Park p : depManager.getParks())
			names_of_parks.add(p.getName());
		selectpark_combox.setItems(FXCollections.observableArrayList(names_of_parks));
		selectpark_combox.getSelectionModel().select(0);

		ArrayList<String> reports_names = new ArrayList<String>();

		reports_names.add(ReportType.Visits_Report.toString());
		reports_names.add(ReportType.cancelled_order_report.toString());
		


		type_combox.setItems(FXCollections.observableArrayList(reports_names));
		type_combox.getSelectionModel().select(0);} 
	
	
	
	
	
	/**
	 * Handles the action event of clicking the create report button.
	 * This method retrieves the selected date, report type, and park name from the corresponding UI components.
	 * It performs validation checks to ensure that all required fields are filled and that the selected date is in the past.
	 * If validation passes, it proceeds to create the selected type of report (either a Cancelled Order Report or a Visits Report)
	 * using the DepartmentManagerCC class methods. Then, it displays the created report in a new stage using the appropriate
	 * report controller classes (CancelReportC or VisitReportC).
	 * @param event The ActionEvent triggered by clicking the create report button.
	 */

	@FXML
	void handle_create_report_btn(ActionEvent event) {
	    LocalDate selectedDate = mounthreport_datepicker.getValue();

	    
	    if (!isValidDate(selectedDate)) {
	        return;
	    }

	    // בדיקת בחירת חודש
	    if (selectedDate == null) {
	        error_msglabel.setText("You should choose a month");
	        error_msglabel.setVisible(true);
	        return;
	    }

	 
	    String type_of_report = type_combox.getValue();
	    Name_of_park = selectpark_combox.getValue();
	    Date selected_date = new Date(selectedDate.getYear() - 1900,
	            selectedDate.getMonthValue() - 1, selectedDate.getDayOfMonth());
	    error_msglabel.setVisible(false);

	    // יצירת הדוח בהתאם לסוג הדוח הנבחר
	    switch (type_of_report) {
	        case "Visits Report":
	            createVisitsReport(selected_date, Name_of_park);
	            break;
	        case "Cancelled Order Report":
	            createCancelReport(selected_date, Name_of_park);
	            break;
	        default:
	            // במידה ולא נבחר סוג דוח תציג הודעת שגיאה
	            error_msglabel.setText("Invalid report type!");
	            error_msglabel.setVisible(true);
	            break;
	    }
	}

	
	
	// בדיקת תקינות תאריך
	private boolean isValidDate(LocalDate selectedDate) {
	    // Get the first day of the current month
	    LocalDate firstDayOfCurrentMonth = LocalDate.now().withDayOfMonth(1);
	    
	    if (selectedDate == null || !selectedDate.isBefore(firstDayOfCurrentMonth)) {
	        error_msglabel.setText("You can only create reports for past months.");
	        error_msglabel.setVisible(true);
	        return false;
	    }
	    return true;
	}
	//create visit report
	private void createVisitsReport(Date selected_date, String parkName) {
	    DepartmentMangerCC.createVisitReport(selected_date, parkName);
	    VisitReportC visit_report = new VisitReportC();
	    visit_report.ParkName = parkName;
	    visit_report.reportDate = selected_date;
	    visit_report.start(new Stage());
	}
	//create cancel report
	private void createCancelReport(Date selected_date, String parkName) {
	    DepartmentMangerCC.createCancelReport(selected_date, parkName);
	    CancelReportC cancel_report = new CancelReportC();
	    cancel_report.ParkName = parkName; 
	    cancel_report.reportDate = selected_date;
	    cancel_report.start(new Stage());
	}
	
	@FXML
	void back(ActionEvent actionEvent) {
		Stage currentStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        currentStage.close();
	}



}
