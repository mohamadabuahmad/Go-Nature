package user;

import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import userCC.DepartmentMangerCC;

/**
 * controller for the CancelReportC
 * @author mohamad
 *
 */

public class CancelReportC implements Initializable{


	public static String ParkName; 
	public static Date reportDate ;

    @FXML
    private Label MonthLabel;

    @FXML
    private Label nameofpark;

    @FXML
    private Label  noResponseLabel;

    @FXML
    private Label numcancelesordersLabel;

    @FXML
    private Label allcancelandresponseorders;
    
    
    /**
     * Initializes the cancellation report GUI components with provided data before 
     * loading the GUI and it get the data from the Department Manager.
     * @param location   The location used to resolve relative paths for the root object.
     * @param resources  The resources used to localize the root object.
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		ArrayList<Integer> cancelReport = (ArrayList<Integer>) DepartmentMangerCC.cancelReport;

		int noResponseOrderNumber = cancelReport.get(0);
		int cancelOrderNumber = cancelReport.get(1);
		int totalNumber = noResponseOrderNumber + cancelOrderNumber;

		MonthLabel.setText((reportDate.getMonth() + 1) + "/" + (reportDate.getYear() + 1900));
		nameofpark.setText("GoNature - " + ParkName);
		noResponseLabel.setText(String.valueOf(noResponseOrderNumber));
		numcancelesordersLabel.setText(String.valueOf(cancelOrderNumber));
		allcancelandresponseorders.setText(String.valueOf(totalNumber ));
	}
	
	/**
	 * Initializes and displays the Cancellation Report GUI.
	 * @param primaryStage The primary stage of the JavaFX application.
	 */
	  public void start(Stage primaryStage) {
			try {	
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("CancelReport.fxml"));
				Parent root = loader.load();
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setTitle("Report");
				primaryStage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}


}
