package inviter;

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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * controller class for the  identification GUI
 * 
 * @author celine
 *
 */

public class Identification  {


	@FXML
	private AnchorPane AnPane;

	@FXML
	private ImageView backImage;
	@FXML
	private Label error_label;
	@FXML
	private Button identify_btn;
	@FXML
	private TextField id_text;
	@FXML
	private Label star_1_label;


	/**
	 * Starts the Identification GUI.
	 * This method loads the FXML file "Identification.fxml" using FXMLLoader to create the root
	 * node of the scene graph. It then sets the created scene onto the primary stage provided
	 * as a parameter. Finally, it sets the title of the primary stage to "identification"
	 * and displays it to the user.
	 * @param primaryStage The primary stage of the JavaFX application where the Identification GUI will be displayed.
	 */
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/inviter/Identification.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Identification");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	



	/**
	 * This method is launched when the user clicks on back button, the method
	 * closes the inviter identification window and opens GoNature window directly!
	 * @param event The MouseEvent representing the "Back" button click event.
	 */
	@FXML
	void backClicked(MouseEvent event) {
		Stage stage = (Stage) AnPane.getScene().getWindow();
		GoNature homePage = new GoNature();
		homePage.start(stage);

	}

	/**
	 * Handles the action when the identification button is clicked.
	 * This method is called when the inviter clicks on the identification button. It retrieves the
	 * identification number entered by the inviter from the id_text field. If the entered ID number
	 * passes validation checks, it proceeds to check the ID number against the database using
	 * IdentificationCC.checkId method. If the ID number is valid and not already logged in, it
	 * sends a message to the server to check and update or insert the user. It then opens the
	 * InviterPage GUI if the user is successfully identified, or displays an error message if
	 * the ID number is invalid or the user is already logged in.
	 * @param event The ActionEvent representing the click event of the identification button.
	 */
	@FXML
	void identificationBtnAction(ActionEvent event) {

		
		String txtNumber = id_text.getText();
		ArrayList<Object> params = new ArrayList<Object>();
		ArrayList<Object> IDNumber = new ArrayList<Object>();
			if (inputValidator.checkId(txtNumber)) {
				ChatClient.currentInviter = IdentificationCC.checkId(txtNumber);
				InviterPage.idNumber_static = txtNumber;
				try {
					params.add(new Connection(InetAddress.getLocalHost().getHostAddress(),
							InetAddress.getLocalHost().getHostName(), ChatClient.currentInviter == null ? "Traveler"
									: ChatClient.currentInviter.isGuide() ? "Guide" : "Traveler",
							txtNumber));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				System.out.println(txtNumber);
				IDNumber.add(txtNumber);
				ClientMessage msg1 = new ClientMessage("checkAndUpdateOrInsertUser", IDNumber, IDNumber.size());
				ClientUI.chat.accept(msg1);
				ServerMessage sm1 = ChatClient.messageRecievedFromServerEvents.get(msg1.getMethodName());
				System.out.println(sm1);
				ClientMessage msg = new ClientMessage("", params, params.size());
				ClientUI.chat.accept(msg);
				ServerMessage sm = ChatClient.messageRecievedFromServerEvents.get(msg.getMethodName());
				if ((boolean) sm1.getData()) {
					InviterPage vSHPCC = new InviterPage();
					vSHPCC.start(new Stage());
					((Node) event.getSource()).getScene().getWindow().hide();
					
				} else {
					error_label.setText(" Sorry!,You are already logged in!");
				}
			} else {
				error_label.setText("Error! Id number should be 9 digits");
			}
	 }
}