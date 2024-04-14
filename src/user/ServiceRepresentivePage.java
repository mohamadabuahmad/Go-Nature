package user;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import Client.ChatClient;
import Client.ClientUI;
import Protocol.ClientMessage;
import entities.Inviter;
import entities.personAddToTable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import userCC.ServiceRepresentativeCC;

/**
 * 
 * @author marwa
 *
 */
public class ServiceRepresentivePage implements Initializable {


	
	@FXML
	private TextField textFieldForId;

	@FXML
	private Button searchForIdButton;


	@FXML
	private TableColumn<personAddToTable, String> idColumn;


	@FXML
	private TableView<personAddToTable> tableView;

	private ObservableList<personAddToTable> data = FXCollections.observableArrayList();
	@FXML
	private Button addGuideButton;

	@FXML
	private Label helloMessageLabel;

	@FXML
	private Button logOutButton;
	
	/**
	 * Opens the Add Guide GUI window.
	 * This method loads the RegisterGuide.fxml file and sets up a new Scene for the Add Guide GUI.
	 * It then retrieves the current window (Stage) and switches its Scene to the Add Guide GUI Scene,
	 * displaying the Add Guide GUI window.
	 *
	 * @param event The ActionEvent representing the action that triggered the method call.
	 */
	  private void changeScene(String fxmlFile, String title, ActionEvent event) {
	        try {
	            Parent parent = FXMLLoader.load(getClass().getResource(fxmlFile));
	            Scene scene = new Scene(parent);
	            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
	            window.setTitle(title);
	            window.setScene(scene);
	            window.show();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	    @FXML
	    void openAddGuideGui(ActionEvent event) {
	        changeScene("RegisterGuide.fxml", "Add Guide", event);
	    }

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
	 * Switches the current window's scene to the GoNature.
	 * This method loads the GoNature.fxml file, creates a new Scene for the GoNature,
	 * and sets the current window's scene to the GoNature Scene, effectively displaying the GoNature.
	 * @param event The ActionEvent representing the action that triggered the method call.
	 * @throws IOException If an input/output exception occurs while loading the FXML file.
	 */
	private void switchToHomePage(ActionEvent event) throws IOException {
	    Parent homePage = FXMLLoader.load(getClass().getResource("/goNatureBoundry/GoNature.fxml"));
	    Scene scene = new Scene(homePage);
	    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	    stage.setScene(scene);
	    stage.show();
	}


	

	/**
	 * Initializes and displays the Service Representative page upon application startup.
	 * This method loads the serviceRepresentivePage.fxml file using FXMLLoader, sets up the scene with the loaded
	 * Parent root, configures the primary stage with the scene, sets the title of the stage, and displays the stage.
	 * @param primaryStage The primary stage of the JavaFX application.
	 */
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("serviceRepresentivePage.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Service Representive");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initializes the Service Representative page after its root element has been completely processed.
	 * This method is automatically called after the FXML file has been loaded.
	 * It sets the greeting message with the name of the current worker, retrieves a list of guides,
	 * populates the TableView with guide data, and sets up sorting by guide ID.
	 * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if the root object was not localized.
	 */
	@Override
	public void initialize(java.net.URL location, ResourceBundle resources) {
		String name = ChatClient.currentUser.getFirstName();
		helloMessageLabel.setText("Hello," + name + " (Service Representive)");
		ArrayList<Inviter> guideList = ServiceRepresentativeCC.getAllGuides();
		int i = 0;
		for (i = 0; i < guideList.size(); i++) {
			Inviter guide = guideList.get(i);
			SimpleStringProperty id = new SimpleStringProperty((String) guide.getIdNumber());
			personAddToTable p2 = new personAddToTable(id);
			data.add(p2);
		}

		idColumn.setCellValueFactory(new PropertyValueFactory<personAddToTable, String>("id"));
		tableView.setItems(data);
		tableView.getSortOrder().add(idColumn);
	}
	
	
	/**
	 * Handles the search action for filtering data based on ID.
	 * This method is invoked when the user triggers the search action.
	 * It retrieves the text entered in the textFieldForId, filters the data in the TableView based on the entered ID,
	 * and updates the TableView with the filtered data.
	 * @param event The ActionEvent representing the action that triggered the method call.
	 */
		@FXML
		void searchId(ActionEvent event) {
			tableView.setItems(filterList(data, textFieldForId.getText()));
		}
		
		/**
		 * Filters a list of personAddToTable objects based on a search text.
		 * This method iterates through the provided list of personAddToTable objects and filters them based on whether
		 * the search text matches certain criteria defined by the searchFindsOrder method. It returns an ObservableList
		 * containing the filtered results.
		 * @param list       The list of personAddToTable objects to be filtered.
		 * @param searchText The text used for filtering the list.
		 * @return An ObservableList containing the filtered results.
		 */
			private ObservableList<personAddToTable> filterList(List<personAddToTable> list, String searchText) {
				List<personAddToTable> filteredList = new ArrayList<>();
				for (personAddToTable person : list) {
					if (searchFindsOrder(person, searchText))
						filteredList.add(person);
				}
				
				return FXCollections.observableList(filteredList);
			}
			
			
			/**
			 * Determines if a personAddToTable object matches the search criteria based on ID.
			 * This method checks whether the ID of the personAddToTable object starts with the provided search text,
			 * ignoring case sensitivity.
			 * @param person     The personAddToTable object to be checked.
			 * @param searchText The search text used to match against the ID.
			 * @return True if the ID of the personAddToTable object matches the search text (ignoring case), otherwise false.
			 */
			private boolean searchFindsOrder(personAddToTable person, String searchText){

				return person.getId().toLowerCase().startsWith(searchText.toLowerCase());
			}


}