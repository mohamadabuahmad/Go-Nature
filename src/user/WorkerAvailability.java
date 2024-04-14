package user;

import Client.ChatClient;
import entities.Park;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import userCC.WorkerCC;

/**
 * @author marwa + mohamad
 * This class handles interactions in the worker availability UI, allowing workers to check
 * the current park status, including the number of visitors and available slots.
 */
public class WorkerAvailability {

    @FXML
    private ComboBox<String> parkComboBox; // Renamed for clarity

    @FXML
    private Button checkVacancyButton; // Renamed for clarity

    @FXML
    private Label numberOfVisitorsLabel; // Renamed for clarity

    @FXML
    private Label availablePlacesLabel; // Renamed for clarity

    @FXML
    private Label availableUnplannedPlacesLabel; // Renamed for clarity

    @FXML
    private Button backButton; // Renamed for clarity

    /**
     * Checks the current park vacancy status and updates the UI.
     * This method retrieves and displays the current number of visitors, available places,
     * and available unplanned places for the park associated with the current worker.
     * @param event The event triggered by clicking the "Check" button.
     */
    @FXML
    void onCheckVacancyClicked(ActionEvent event) {
        Park currentPark = WorkerCC.checkParkVacancy(ChatClient.currentUser.getPark().getName());
        numberOfVisitorsLabel.setText(String.valueOf(currentPark.getCurrentNumOfVisitors()));
        availablePlacesLabel.setText(String.valueOf(currentPark.getMaxNumOfVisitor() - currentPark.getCurrentNumOfVisitors()));
        availableUnplannedPlacesLabel.setText(String.valueOf((currentPark.getMaxNumOfVisitor() - currentPark.getMaxNumOfOrders()) - currentPark.getCurrentNumOfUnplannedVisitors()));
    }

    /**
     * Closes the current window and returns to the previous page.
     * This method is called when the "Back" button is clicked, closing the current stage.
     * @param event The event triggered by clicking the "Back" button.
     */
    @FXML
    void onBackButtonClicked(ActionEvent event) {
        closeCurrentStage(event);
    }

    /**
     * Closes the current stage based on the event source.
     * A helper method used for closing the current window.
     * @param event The ActionEvent associated with the UI interaction.
     */
    private void closeCurrentStage(ActionEvent event) {
        Node sourceNode = (Node) event.getSource();
        Stage currentStage = (Stage) sourceNode.getScene().getWindow();
        currentStage.close();
    }
}
