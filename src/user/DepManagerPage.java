package user;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import entities.DepartmentManager;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
/**
 * controller for the ChangeParameters
 * @author Mohamad
 *
 */
public class DepManagerPage implements Initializable {
    public static String name ;
	
	@FXML
	private Label role;

	@FXML
	private Button logout_btn;
	

	@FXML
	private Label main;



	public static DepartmentManager dep;

	/**
	 * Initializes and displays the Department Manager GUI.
	 * Loads the DepartmentManagerPage.fxml using FXMLLoader, sets it as the root,
	 * and displays it in the primaryStage with the specified title.
	 * @param primaryStage The primary stage to display the Department Manager GUI.
	 */
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("DepartmentManagerPage.fxml"));
			
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Department Manager ");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles the action event of clicking the logout button.
	 * Constructs a logout message and sends it to the server.
	 * Waiting for confirmation from the server to LOGOUT.
	 * After receiving confirmation of logout from the server, proceeds to load GoNature page 
	 * @param event The ActionEvent triggered by clicking the logout button.
	 */
	@FXML
	void log_out(ActionEvent event) {
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
	 * Switches to the GoNature page.
	 * Loads the GoNature page. using FXMLLoader and sets it as the scene for the current stage.
	 * @param event The ActionEvent triggered by the action that requires switching to the GoNature page .
	 * @throws IOException If an error occurs while loading the GoNature page 
	 */
	private void switchToHomePage(ActionEvent event) throws IOException {
	    Parent homePage = FXMLLoader.load(getClass().getResource("/goNatureBoundry/GoNature.fxml"));
	    Scene scene = new Scene(homePage);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(scene);
	    stage.show();
	}




	/**
	 * Handles the event of clicking on the parameters button.
	 * Loads the parameters page  using FXMLLoader and displays it in a new stage.
	 * @param event The MouseEvent triggered by clicking on the parameters button.
	 */
	@FXML
	void handleParameters(MouseEvent event) {
	    try {
	        Parent parametersPage = FXMLLoader.load(getClass().getResource("DepartmentManagerParameters.fxml"));
	        Scene scene = new Scene(parametersPage);
	        Stage stage = new Stage();
	        stage.setTitle("Parameters Confirm"); 
	        stage.setScene(scene); 
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}




	/**
	 * Handles the event of clicking on the reports button.
	 * Loads the reports page  using FXMLLoader and displays it in a new stage.
	 * @param event The MouseEvent triggered by clicking on the reports button.
	 */
	@FXML
	void handleReports(MouseEvent event) {
	    try {
	        Parent parametersPage = FXMLLoader.load(getClass().getResource("DepManagerReports.fxml"));
	        Scene scene = new Scene(parametersPage);
	        Stage stage = new Stage();
	        stage.setTitle("Reports"); 
	        stage.setScene(scene); 
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
	/**
	 * Handles the event of clicking on the availability button.
	 * Loads the availability check page  using FXMLLoader and displays it in a new stage.
	 * @param event The MouseEvent triggered by clicking on the availability button.
	 */
	@FXML
	void handleParkAvailability(MouseEvent event) {
	    try {
	        Parent parametersPage = FXMLLoader.load(getClass().getResource("DepManagerAvailability.fxml"));
	        Scene scene = new Scene(parametersPage);
	        Stage stage = new Stage();
	        stage.setTitle("Check availability "); 
	        stage.setScene(scene); 
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	}
	

	/**
	 * Initializes the Department Manager GUI with the appropriate role and main label text.
	 * Sets the RoleLabel text to greet the current department manager with their first name and role.
	 * Sets the MainLabel text to indicate the home page.
	 * @param location The URL location, which is used to resolve relative paths for the root object.
	 * @param resources The ResourceBundle, which is used to localize the root object.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		role.setText("Hello, " + ChatClient.currentUser.getFirstName() + "(Department Manager)");
		main.setText("Home Page");

	}

}
