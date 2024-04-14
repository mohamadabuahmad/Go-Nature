package user;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import entities.ParkManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * @author shady
 *
 */
public class ParkManagerPage implements Initializable {

	ParkManager ParkManager = (ParkManager) ChatClient.currentUser;
	@FXML
	private ImageView HomepageBtn;
	

	@FXML
	private ImageView ReportButton;

	@FXML
	private ImageView ChangeParameterBtn;

	@FXML
	private ImageView SetSaleBtn;

	@FXML
	private ImageView CheckParkVacancyBtn;

	@FXML
	private Label PageTitle;

	@FXML
	private Label NameTextView;

	@FXML
	private Button LogoutBtn;

	@FXML
	private AnchorPane anPane;

	private ImageView selected;

	
	/**
	 * Sets the style of the parent pane of the selected image to indicate selection.
	 * If another image was previously selected, resets its parent pane style.
	 * @param newImage The ImageView of the newly selected image.
	 */
	public void selectedImage(ImageView newImage) {
		Pane p1;
		if (selected != null) {
			p1 = (Pane) selected.getParent();
			p1.setStyle(null);
		}
		Pane p = (Pane) newImage.getParent();

		p.setStyle("-fx-background-color: #e7d8c0");

		selected = newImage;
	}

	/**
	 * It sets up the view with data based on the current worker.
	 * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    try {
	        ParkManager = (ParkManager) ChatClient.currentUser;
	        NameTextView.setText("Hello , (" + ParkManager.getFirstName() + " " + ParkManager.getLastName() + ")");  
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	@FXML
	void OpenReportGUI(MouseEvent event) {
	    openPage("ParkMangerReports.fxml");
	}

	@FXML
	void OpenChangeParkParameters(MouseEvent event) {
	    openPage("ChangeParameters.fxml");
	}

	@FXML
	void OpenCheckParkavailability(MouseEvent event) {
	    openPage("ParkMangerAvailability.fxml");
	}

	private void openPage(String fxmlPath) {
	    try {
	        Parent page = FXMLLoader.load(getClass().getResource(fxmlPath));
	        Scene scene = new Scene(page);
	        Stage stage = new Stage();
	        String title = "";
	        switch (fxmlPath) {
	            case "ParkMangerReports.fxml":
	                title = "Report";
	                break;
	            case "ChangeParameters.fxml":
	                title = "Change Parameters";
	                break;
	            case "ParkMangerAvailability.fxml":
	                title = "Availability";
	                break;
	            // Add cases for other FXML files if needed
	        }
	        stage.setTitle(title);
	        stage.setScene(scene);
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}


	/**
	 * Starts the ParkManagerPage GUI.
	 * @param primaryStage The primary stage for this application, onto which the application scene can be set.
	 */
	public void start(Stage primaryStage) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("ParkManagerPage.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("ParkManager");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * Logs out the current user.
	 * This method constructs and sends a LOGOUT message to the server to indicate that the current user
	 * is logging out. It then waits for confirmation of the LOGOUT from the SERVER. 
	 * Upon successful logout confirmation, it proceeds to load the home page of the application.
	 * @param event The ActionEvent triggered by clicking the logout button.
	 */
	@FXML
	void Logout(MouseEvent event) {
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
	 * Switches to the GoNature. 
	 * @param event The MouseEvent that triggers the action.
	 * @throws IOException If an I/O error occurs during loading of the GoNature page .
	 */
	private void switchToHomePage(MouseEvent event) throws IOException {
	    Parent homePage = FXMLLoader.load(getClass().getResource("/goNatureBoundry/GoNature.fxml"));
	    Scene scene = new Scene(homePage);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(scene);
	    stage.show();
	}
	



}
