package user;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Client.ChatClient;
import entities.ParkManager;
import entities.ReportType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import userCC.ParkMangerCC;
/**
 * @author mohamad,shady
 *
 */
public class ParkMangerReports implements Initializable {
    @FXML
    private ComboBox<String> ReportTypeCombo;
    @FXML
    private DatePicker ReportMonth;
    @FXML
    private Label message;
    @FXML
    private Button CreateReportBtn;
    @FXML
    private Button BackBtn;

    private ParkManager parkManager = (ParkManager) ChatClient.currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeReportTypeCombo();
        message.setVisible(false);
    }

    private void initializeReportTypeCombo() {
        ArrayList<String> reportTypes = new ArrayList<>();
        reportTypes.add(ReportType.number_of_visitors_report.toString());
        reportTypes.add(ReportType.Usags_Report.toString());
        ReportTypeCombo.setItems(FXCollections.observableArrayList(reportTypes));
        ReportTypeCombo.getSelectionModel().selectFirst();
    }

    @FXML
    void CreateReportBtn(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        LocalDate selectedDate = ReportMonth.getValue();
        Date sqlDate = convertToSqlDate(selectedDate);

        String selectedReportType = ReportTypeCombo.getValue();
        generateReport(selectedReportType, sqlDate);
    }

    private boolean validateInput() {
        LocalDate selectedDate = ReportMonth.getValue();
        if (selectedDate == null || !isDateValidForReport(selectedDate)) {
            message.setText("All Fields Are Required and must be for a past month!");
            message.setVisible(true);
            return false;
        }
        message.setVisible(false);
        return true;
    }

    private Date convertToSqlDate(LocalDate date) {
        return new Date(date.getYear() - 1900, date.getMonthValue() - 1, date.getDayOfMonth());
    }

    private boolean isDateValidForReport(LocalDate date) {
        return date.isBefore(LocalDate.now()) &&
               (date.getYear() < LocalDate.now().getYear() ||
                date.getMonthValue() < LocalDate.now().getMonthValue());
    }

    private void generateReport(String reportType, Date date) {
        switch (reportType) {
            case "Number Of visitors Report":
            	ParkMangerCC.createTotalVisitorReport(date, parkManager.getPark().getName());
                launchTotalVisitorReport(date);
                break;
            case "Usags Report":
            	ParkMangerCC.createUsageReport(date, parkManager.getPark().getName());
                launchUsageReport(date);
                break;
            default:
                message.setText("Report type not supported.");
                message.setVisible(true);
                break;
        }
    }

    private void launchTotalVisitorReport(Date date) {
        TotalVisitorReportC controller = new TotalVisitorReportC();
        TotalVisitorReportC.parkName = parkManager.getPark().getName();
        TotalVisitorReportC.reportDate = date;
        controller.start(new Stage());
    }

    private void launchUsageReport(Date date) {
        UsageReportC controller = new UsageReportC();
        UsageReportC.parkName = parkManager.getPark().getName();
        UsageReportC.reportDate = date;
        controller.start(new Stage());
    }

    @FXML
    void handleBack(ActionEvent event) {
        closeCurrentStage(event);
    }

    private void closeCurrentStage(ActionEvent event) {
        ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();
    }

    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ParkManagerReports.fxml"));
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("Park Manager Reports");
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
