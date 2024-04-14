package user;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import userCC.LoginCC;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import Protocol.ServerMessage;
import entities.*;
import goNatureBoundry.GoNature;
import inviterCC.*;

/**
 * 
 * @author celine
 *
 */

public class Login {

	@FXML
	private AnchorPane ap;

	@FXML
	private Label error_label;

	@FXML
	private TextField user_name_txtfield;

	@FXML
	private PasswordField password_txtfield;

	@FXML
	private ImageView back_btn;

	@FXML
	private Button sign_in_btn; 
	
	/**
	 * Starts the Login GUI.
	 * This method initializes and displays the Login GUI when called. It loads the FXML file for the Login GUI,
	 * sets the scene with the root node, sets the stage title to "Sign In", and shows the stage.
	 * @param primaryStage The primary stage for the Login GUI.
	 */
	public void start(Stage primaryStage) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Login");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles the Enter key press event.
	 * This method is invoked when the Enter key is pressed while focused on a specific control.
	 * It triggers the action of signing in when the Enter key is pressed.
	 * @param event The KeyEvent representing the key press event.
	 */
	@FXML
	void EnterPressed(KeyEvent event) {
		if (event.getCode().equals(KeyCode.ENTER))
			handleSignIn(new ActionEvent());
	}
	/**
	 * Handles the mouse click event on the back button.
	 * This method is invoked when the back button is clicked. 
	 * It closes the current window and opens the GoNature page.
	 * @param event The MouseEvent representing the mouse click event.
	 */
	@FXML
	void back(MouseEvent event) {
		Stage stage = (Stage) ap.getScene().getWindow();
		GoNature homePage = new GoNature();
		homePage.start(stage);

	}

	

	/**
	 * Action handler for the Sign In button.
	 * This method is invoked when the Sign In button is clicked. It retrieves the username and password
	 * entered by the user, validates them against the system's records, and redirects the user to the appropriate
	 * page(according to type of worker). If the login attempt is successful, the user is redirected to their respective
	 * home page. If the user is already logged in, an appropriate message is displayed.
	 * @param event The action event triggered by clicking the Sign In button.
	 */
	@FXML
	void handleSignIn(ActionEvent event) {
	    String userName = user_name_txtfield.getText();
	    String password = password_txtfield.getText();
	    ArrayList<Object> params = new ArrayList<>();
	    ArrayList<Object> idNumber = new ArrayList<>();
	    User worker = LoginCC.checkUser(userName, password);
	    ChatClient.currentUser = worker;
	    if (worker != null) {
	        String userType = getUserType(worker);
	        
	        try {
	            params.add(new Connection(InetAddress.getLocalHost().getHostAddress(),
	                    InetAddress.getLocalHost().getHostName(), userType, worker.getWorkerId()));
	        } catch (UnknownHostException e) {
	            e.printStackTrace();
	        }
	        
	        idNumber.add(worker.getWorkerId());
	        ClientMessage msg1 = new ClientMessage("checkAndUpdateOrInsertUser", idNumber, idNumber.size());
	        ClientUI.chat.accept(msg1);
	        System.out.println("ClientMessage to server: " + msg1);
	        ServerMessage sm1 = ChatClient.messageRecievedFromServerEvents.get(msg1.getMethodName());
	        System.out.println("Server response to server: " + sm1);
	        
	        ClientMessage msg = new ClientMessage("", params, params.size());
	        ClientUI.chat.accept(msg);
	        ServerMessage sm = ChatClient.messageRecievedFromServerEvents.get(msg.getMethodName());
	        
	        if ((boolean) sm1.getData()) {
	            openUserPage(userType);
	            ap.getScene().getWindow().hide();
	        } else {
	            error_label.setText("You are already logged in!");
	            error_label.setVisible(true);
	        }
	    } else {
	        System.out.println("This Account is not exist!");
	        error_label.setText("Login details are incorrect");
	        error_label.setVisible(true);
	    }
	}

	
	/**
	 * this method received User and return String the type of user
	 * @param user
	 * @return String type of user
	 */
	private String getUserType(User user) {
	    if (user instanceof ServiceRepesentative) {
	        return "Service Representative";
	    } else if (user instanceof ParkManager) {
	        return "Park Manager";
	    } else if (user instanceof DepartmentManager) {
	        return "Department Manager";
	    } else if (user instanceof User) {
	        return "Worker";
	    } else {
	        return "Unknown";
	    }
	}
	/**
	 * this method recievsd the type of user and open the page of this typeuser
	 * @param userType
	 */

	private void openUserPage(String userType) {
	    switch (userType) {
	        case "Service Representative":
	            ServiceRepresentivePage serviceRepresentivePage = new ServiceRepresentivePage();
	            serviceRepresentivePage.start(new Stage());
	            break;
	        case "Park Manager":
	            ParkManagerPage parkManagerPage = new ParkManagerPage();
	            parkManagerPage.start(new Stage());
	            break;
	        case "Department Manager":
	            DepManagerPage depManagerPage = new DepManagerPage();
	            depManagerPage.start(new Stage());
	            break;
	        case "Worker":
	            WorkerPage workerHomePage = new WorkerPage();
	            workerHomePage.start(new Stage());
	            break;
	        default:
	            System.out.println("Unknown user type!");
	            break;
	    }
	}
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
