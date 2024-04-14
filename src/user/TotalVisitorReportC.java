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
import userCC.ParkMangerCC;

/**
 * Controller class for displaying the total visitor report.
 */
public class TotalVisitorReportC implements Initializable {

    public static String parkName;
    public static Date reportDate;

    @FXML
    private Label monthLabel;
    @FXML
    private Label plannedVisitorLabel;
    @FXML
    private Label plannedGuideLabel;
    @FXML
    private Label unplannedTravelerLabel;
    @FXML
    private Label unplannedGuideLabel;
    @FXML
    private Label totalVisitorLabel;

    /**
     * Initializes the report page with total visitor data.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<String> reportData = ParkMangerCC.totalVisitorReport;
        monthLabel.setText(formatDate(reportDate));
        plannedVisitorLabel.setText(reportData.get(0));
        plannedGuideLabel.setText(reportData.get(2));
        unplannedTravelerLabel.setText(reportData.get(3));
        unplannedGuideLabel.setText(reportData.get(5));
        totalVisitorLabel.setText(calculateTotalVisitors(reportData).toString());
    }

    /**
     * Formats the report date.
     */
    private String formatDate(Date date) {
        return (date.getMonth() + 1) + "/" + (date.getYear() + 1900);
    }

    /**
     * Calculates the total number of visitors.
     */
    private Integer calculateTotalVisitors(ArrayList<String> reportData) {
        return reportData.subList(0, 4).stream()
                         .mapToInt(Integer::parseInt)
                         .sum();
    }

    /**
     * Starts the Total Visitors Report page.
     */
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("TotalVisitorsReport.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Visitor Report");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
