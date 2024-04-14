package user;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import javafx.stage.Stage;
/**
 * @author mahamad
 *
 */
public class WorkerPage implements Initializable {


	  @FXML private Label PageTitleLabel;
	    @FXML private Label GreetingLabel;
	    @FXML private Button LogOutBtn;
	    @FXML private Pane innerPane;

	/**
	 * Initializes and displays the Worker Page upon application startup.
	 * This method loads the WorkerPage.fxml file using FXMLLoader, sets up the scene with the loaded
	 * Parent root, configures the primary stage with the scene, sets the title of the stage to the name of the park associated with the current worker, and displays the stage.
	 * @param primaryStage The primary stage of the JavaFX application.
	 */
		/**
		 * Initializes the Worker Home Page after its root element has been completely processed.
		 * This method is automatically called after the FXML file has been loaded.
		 * It sets the title of the page and greets the current worker by name.
		 * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
		 * @param resources The resources used to localize the root object, or null if the root object was not localized.
		 */
		@Override
		public void initialize(URL location, ResourceBundle resources) {
			
			PageTitleLabel.setText("Worker Home Page");
			GreetingLabel.setText("Hello," + ChatClient.currentUser.getFirstName() + "(Worker)");

		}
	public void start(Stage primaryStage) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("WorkerPage.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle(ChatClient.currentUser.getPark().getName());
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Switches to the home page of the application.
	 * This method loads the home page view from the FXML file "GoNature.fxml" and sets it as the
	 * scene for the current stage. It then shows the stage to display the home page.
	 * @param event The ActionEvent that triggers the switch to the home page.
	 * @throws IOException If an error occurs while loading the home page view.
	 */
	private void switchToHomePage(ActionEvent event) throws IOException {
	    Parent homePage = FXMLLoader.load(getClass().getResource("/goNatureBoundry/GoNature.fxml"));
	    Scene scene = new Scene(homePage);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(scene);
	    stage.show();
	}
	
    /**
     * Opens a new window displaying the specified FXML page.
     * This method loads the FXML for the given file, creates a new scene and stage for it,
     * sets the title of the new window, and finally shows the new window.
     * The current window remains open, allowing multiple windows to be open at the same time.
     *
     * @param fxmlFile The path to the FXML file to be loaded.
     * @param title The title to be set for the new window.
     * @throws IOException If there is an issue loading the FXML file.
     */
	private void navigateTo(String fxmlFile, String title) throws IOException {
	    // Load the new page
	    Parent nextPage = FXMLLoader.load(getClass().getResource(fxmlFile));
	    Scene scene = new Scene(nextPage);

	    // Create a new stage for the new page
	    Stage newStage = new Stage();
	    newStage.setScene(scene);
	    newStage.setTitle(title);

	    // Show the new stage, keeping the current stage open
	    newStage.show();
	}

	  /**
     * Handles navigation to the Unplanned Order Page when the relevant action is triggered.
     * Opens the Unplanned Order Page in a new window.
     *
     * @param event The ActionEvent that triggers the navigation.
     */
    @FXML
    void goToUnplannedOrderPage(ActionEvent event) {
        try {
            navigateTo("UnplannedVisit.fxml", "Unplanned Visit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs out the current user by sending a logout request to the server.
     * After successful logout, navigates to the GoNature home page.
     *
     * @param event The ActionEvent triggered by clicking the logout button.
     */
	@FXML
	void logOut(ActionEvent event) {
	    try {
	        ArrayList<Object> parameters = new ArrayList<>();
	        ClientMessage msg1 = new ClientMessage("LOGOUT", parameters, 0);
	        ClientUI.chat.accept(msg1);
	        switchToHomePage(event);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

    /**
     * Handles navigation to the Worker Availability Page.
     * Opens the Worker Availability Page in a new window.
     *
     * @param event The ActionEvent that triggers the navigation.
     */
    @FXML
    void onVacancyClicked(ActionEvent event) {
        try {
            navigateTo("WorkerAvailability.fxml", "Check Availability");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles navigation to the Check Exit or Enter Page.
     * Opens the Check Exit or Enter Page in a new window.
     *
     * @param event The ActionEvent that triggers the navigation.
     */
    @FXML
    void onCheckOrder(ActionEvent event) {
        try {
            navigateTo("ExitorEnter.fxml", "Check Exit or Enter");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	}