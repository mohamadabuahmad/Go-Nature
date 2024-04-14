package goNatureBoundry;

import inviter.Identification;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import user.Login;

/**
 * controller class for the GoNature
 * 
 * @author Celine
 *
 */
public class GoNature {

	@FXML
	private Button workHerebtn;

	@FXML
	private Button startOrderbtn;

	
	/**
	 * Starts the application by loading the GoNature.
	 * This method loads the FXML file "GoNature.fxml" using FXMLLoader to create the root
	 * node of the scene graph. It then sets the created scene onto the primary stage
	 * provided as a parameter. Finally, it sets the title of the primary stage to "Home Page"
	 * and displays it to the user.
	 * @param primaryStage The primary stage of the JavaFX application where the home page will be displayed.
	 */
	public void start(Stage primaryStage) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("GoNature.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Home Page");
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Opens the Identification GUI.
	 * This method instantiates an instance of the Identification class, which likely represents
	 * GUI component for user identification. It then starts the GUI by calling its start method 
	 * with a new Stage. Additionally, it hides the current window
	 * associated with the event.
	 * @param event The ActionEvent triggering the method call, typically originating from
	 * a user interaction with a GUI component such as a button.
	 */
	@FXML
	void openIdentificationGUI(ActionEvent event) {

		Identification idController = new Identification();
		idController.start(new Stage());

		((Node) event.getSource()).getScene().getWindow().hide();

	}

	/**
	 * Opens the Login GUI.
	 * This method instantiates an instance of the Login class, which likely represents
	 * GUI component for WORKER sign-in. It then starts the GUI by calling its start method 
	 * with a new Stage. Additionally, it hides the current window
	 * associated with the event.
	 * @param event The ActionEvent triggering the method call, typically originating from
	 * a user interaction with a GUI component such as a button.
	 */
	@FXML
	void openSignInGUI(ActionEvent event) {

		Login cc = new Login();
		cc.start(new Stage());

		((Node) event.getSource()).getScene().getWindow().hide();
	}


}
