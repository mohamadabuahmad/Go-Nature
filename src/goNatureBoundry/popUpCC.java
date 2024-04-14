package goNatureBoundry;

import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * @author group 7 
 *
 */
public class popUpCC implements Initializable {

	@FXML
	private Label colorLabel2;

	@FXML
	private Label colorLabel;

	@FXML
	private Label mainMessage;

	@FXML
	private Label secondaryMessage;

	@FXML
	private Button Button;

	@FXML
	private ImageView image;

	@FXML
	private ImageView image1;

	private static boolean success;
	private static String mainMessageStr, secondaryMessageStr, buttonTextStr;

	private double xOffset = 0;
	private double yOffset = 0;

	private static Stage primaryStage;

	/**
	 * This method initializes and starts the pop-up window with the specified parameters.
	 * It sets the main message, secondary message, success status, button text, and displays the pop-up window.
	 * @param primaryStage      the primary stage of the pop-up window
	 * @param success           the success status of the operation
	 * @param mainMessage       the main message to be displayed
	 * @param secondaryMessage  the secondary message to be displayed
	 * @param buttonText        the text to be displayed on the button
	 */
	public void start(Stage primaryStage, boolean success, String mainMessage, String secondaryMessage,
			String buttonText) {
		mainMessageStr = mainMessage;
		secondaryMessageStr = secondaryMessage;
		this.success = success;
		buttonTextStr = buttonText;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/GUI/popUp.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			scene.setFill(Color.TRANSPARENT);
			primaryStage.initStyle(StageStyle.UNDECORATED);
			primaryStage.initStyle(StageStyle.TRANSPARENT);
			this.primaryStage = primaryStage;
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method initializes the pop-up window elements when the corresponding FXML file is loaded.
	 * It sets the styles, text, and visibility of various elements based on the provided parameters.
	 * @param location  the URL location of the FXML file
	 * @param resources the ResourceBundle containing locale-specific objects
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Button.setText(buttonTextStr);
		mainMessage.setText(mainMessageStr);
		secondaryMessage.setText(secondaryMessageStr);
		image1.setVisible(!success);
		image.setVisible(success);

	}

	/**
	 * This method handles the action event when the exit button is clicked.
	 * It hides the window associated with the event source.
	 * @param event the ActionEvent triggered by clicking the exit button
	 */
	@FXML
	void exit(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
	}

	/**
	 * This method handles the mouse drag event on the anchor pane.
	 * It adjusts the position of the primary stage based on the mouse drag movement.
	 * @param event the MouseEvent triggered by dragging the anchor pane
	 */
	@FXML
	void anchorPaneDragged(MouseEvent event) {
		System.out.println(primaryStage.toString());
		primaryStage.setX(event.getScreenX() + xOffset);
		primaryStage.setY(event.getScreenY() + yOffset);
	}

	/**
	 * This method handles the mouse press event on the anchor pane.
	 * It calculates the xOffset and yOffset based on the position of the primary stage and the mouse press.
	 * @param event the MouseEvent triggered by pressing the mouse on the anchor pane
	 */
	@FXML
	void anchorPanePressed(MouseEvent event) {
		xOffset = primaryStage.getX() - event.getScreenX();
		yOffset = primaryStage.getY() - event.getScreenY();
	}
}