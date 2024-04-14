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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import userCC.DepartmentMangerCC;

/**
 * @author mohamad,shady
 *
 */
public class VisitReportC implements Initializable{
	public static String ParkName;
	public static Date reportDate;
    @FXML
    private Label RDate;

    @FXML
    private Label parkN;

    @FXML
    private Label PlannedTraveler;
    @FXML
    private Label PlannedGuide;
    @FXML
    private Label UnplannedTraveler;
    @FXML
    private Label UnplannedGuide;
    @FXML
    private BarChart<String, Integer> IntranceTimeChart;
    @FXML
    private BarChart<String, Integer> StayTimeChart;

    @FXML
    private PieChart stayTimeChart,stayTimeChart1;

    /**
     * Initializes the visit report page.
     * Sets up the page labels with visit report data and configures pie charts for displaying
     * stay time and visitor category distributions based on the report. The first pie chart shows 
     * the distribution of stay times in three different slots, while the second pie chart 
     * categorizes visitors into Planned Travelers, Planned Guides, Unplanned Travelers, 
     * and Unplanned Guides, showing the total counts for each.
     *
     * @param location  The URL location used to resolve relative paths for the root object, or null if unknown.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    // Display park name and report date
	    parkN.setText("GoNature - " + ParkName);
	    RDate.setText((reportDate.getMonth()+1)+"/"+(reportDate.getYear()+1900));

	    // Assuming DepartmentMangerCC.visitReport is populated with March data
	    ArrayList<Integer> report = (ArrayList<Integer>) userCC.DepartmentMangerCC.visitReport;

	    // Update labels with the visitor counts from the report
	    // Note: Adjust these indices based on how your report data is structured
	    PlannedTraveler.setText(String.format("\t%d\t\t\t%d\t\t\t%d", report.get(0), report.get(1), report.get(2)));
	    PlannedGuide.setText(String.format("\t%d\t\t\t%d\t\t\t%d", report.get(3), report.get(4), report.get(5)));
	    UnplannedTraveler.setText(String.format("\t%d\t\t\t%d\t\t\t%d", report.get(6), report.get(7), report.get(8)));
	    UnplannedGuide.setText(String.format("\t%d\t\t\t%d\t\t\t%d", report.get(9), report.get(10), report.get(11)));

	    // Setup PieChart for Stay Time Distribution
	    stayTimeChart.getData().clear(); // Clear previous data
	    stayTimeChart.getData().addAll(
	        new PieChart.Data("8:00-12:00", report.subList(0, 3).stream().mapToInt(Integer::intValue).sum()),
	        new PieChart.Data("12:00-16:00", report.subList(3, 6).stream().mapToInt(Integer::intValue).sum()),
	        new PieChart.Data("16:00-19:00", report.subList(6, 9).stream().mapToInt(Integer::intValue).sum())
	    );
	    stayTimeChart.setTitle("Stay Time Distribution");

	    // Setup PieChart for Visitor Distribution
	    stayTimeChart1.getData().clear(); // Clear previous data
	    stayTimeChart1.getData().addAll(
	        new PieChart.Data("Planned Traveler", report.subList(0, 3).stream().mapToInt(Integer::intValue).sum()),
	        new PieChart.Data("Planned Guide", report.subList(3, 6).stream().mapToInt(Integer::intValue).sum()),
	        new PieChart.Data("Unplanned Traveler", report.subList(6, 9).stream().mapToInt(Integer::intValue).sum()),
	        new PieChart.Data("Unplanned Guide", report.subList(9, 12).stream().mapToInt(Integer::intValue).sum())
	    );
	    stayTimeChart1.setTitle("Visitor Distribution");
	}
	/**
	 * Initializes and displays the Visit Report page upon application startup.
	 * This method loads the visitReport.fxml file using FXMLLoader, sets up the scene with the loaded
	 * Parent root, configures the primary stage with the scene, sets the title of the stage, and displays the stage.
	 * @param primaryStage The primary stage of the JavaFX application.
	 */
	public void start(Stage primaryStage) {
			try {	
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("visitReport.fxml"));
				Parent root = loader.load();
				Scene scene = new Scene(root);
				primaryStage.setScene(scene);
				primaryStage.setTitle("visitReport");
				primaryStage.show();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
}
