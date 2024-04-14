package user;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import entities.reportHelper; // Class names should be in CamelCase.
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import userCC.ParkMangerCC;

/**
 * 
 * @author mohamad,shady
 *
 * Controls the Usage Report GUI page.
 * This class is responsible for initializing and updating the Usage Report view with data retrieved from the database.
 */
public class UsageReportC implements Initializable {

    public static String parkName; // Use camelCase for variable names.
    public static Date reportDate; // Renamed for clarity.

    @FXML
    private Label reportDateLabel;
    @FXML
    private Label parkNameLabel;
    @FXML
    private TableColumn<reportHelper, Date> dateColumn;
    @FXML
    private TableColumn<reportHelper, String> timeSpanColumn;
    @FXML
    private TableView<reportHelper> reportTable;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parkNameLabel.setText("GoNature - " + parkName);
        reportDateLabel.setText((reportDate.getMonth() + 1) + "\\" + (reportDate.getYear() + 1900));
        
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        timeSpanColumn.setCellValueFactory(new PropertyValueFactory<>("range"));
        
        reportTable.setItems(FXCollections.observableArrayList(ParkMangerCC.usageReport)); // Assuming the static field is properly updated elsewhere.
    }

    /**
     * Displays the Usage Report scene on the given stage.
     * 
     * @param primaryStage The primary stage for this application.
     */
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("UsageReport.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Usage Report");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
