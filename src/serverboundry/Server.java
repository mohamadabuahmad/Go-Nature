package serverboundry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import entities.Connection;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import server.EchoServer;
import server.ServerUI;
import server.mysqlConnection;

/**
 * controller for the server GUI
 * 
 * @author  celine 
 *
 */
public class Server implements Initializable {

	@FXML
	private Button connectBtn;

	@FXML
	private Button disconnectBtn;

	@FXML
	private TextField portTextField;

	@FXML
	private TableView<Connection> connectedClientsTable;

	@FXML
	private TableColumn<Connection, String> ipAddressColumn;

	@FXML
	private TableColumn<Connection, String> nameColumn;

	@FXML
	private TableColumn<Connection, String> roleColumn;

	@FXML
	private Text numOfConnections;
	@FXML
	private TextArea Console;
	private StringBuilder ConsoleTexter;
	@FXML
	private Circle connectionCircle;

	@FXML
	private TextField mysqlPassword;

	public static String pass_Mysql;

	private ArrayList<Connection> connectionList;

	//// threads doing background tasks////

	private Thread connectionUpdater;

	private Thread waitingListDeleter;

	private Thread unconfirmedOrderDeleter;

	private Thread oneDayBeforeNotifier;

	/**
	 * This method initializes and starts the server window.
	 * It loads the Server.fxml file, sets up the scene and title, and displays the primary stage.
	 * Additionally, it sets the server controller instance.
	 * @param primaryStage the primary stage of the server window
	 * @throws Exception if an error occurs while loading the FXML file or initializing the server
	 */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("Server.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.show();
		EchoServer.serverController = loader.getController();
	}

	
	/**
	 * This method handles the action event when the connect button is clicked.
	 * It retrieves the port number from the portTextField, initializes the MySQL password,
	 * and starts the server with the provided port number.
	 * It also starts several threads for database operations and updates the UI accordingly.
	 * @param event the ActionEvent triggered by clicking the connect button
	 */
	@FXML
	void connect(ActionEvent event) {
	    String port = portTextField.getText();
	    pass_Mysql = mysqlPassword.getText();

	    if (port.trim().isEmpty()) {
	        writeToConsole("You must enter a port number");
	        return;
	    }

	    if (ServerUI.runServer(port)) {
	        initializeThreads(port);
	    } else {
	        connectionCircle.setFill(javafx.scene.paint.Color.RED);
	    }
	}

	private void initializeThreads(String port) {
	    connectionCircle.setFill(javafx.scene.paint.Color.GREEN);
	    
	    if (mysqlConnection.getConn() != null) {
	        startThreads();
	        writeToConsole("Driver definition succeeded\n" + "SQL connection succeed\n"
	                + "Server listening for connections on port " + port);
	    } else {
	        writeToConsole("Server listening for connections on port " + port);
	        writeToConsole("connection to mysql failed!");
	        try {
				disconnect(null);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}

	private void startThreads() {
	    Thread connectionUpdater = new Thread(() -> {
	        while (true) {
	            removeConnection();
	            try {
	                Thread.sleep(10);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    });

	    Thread waitingListDeleter = new Thread(() -> {
	        while (true) {
	            mysqlConnection.check_statusOrderInWaitingList(null);
	            try {
	                Thread.sleep(2000); // sleep for 2 seconds
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    });

	    Thread unconfirmedOrderDeleter = new Thread(() -> {
	        while (true) {
	            mysqlConnection.updatestatusTo_Unconfirmed(null);
	            try {
	                Thread.sleep(5000); // sleep for 5 seconds
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    });

	    Thread oneDayBeforeNotifier = new Thread(() -> {
	        while (true) {
	            mysqlConnection.oneDayBefore_notify_inviter(null);
	            try {
	                Thread.sleep(1000); // sleep for 1 second
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    });

	    unconfirmedOrderDeleter.start();
	    waitingListDeleter.start();
	    connectionUpdater.start();
	    oneDayBeforeNotifier.start();
	}

	/**
	 * This method handles the action event when the disconnect button is clicked.
	 * It stops the server, updates the UI accordingly, and stops the threads associated with server operations.
	 * @param event the ActionEvent triggered by clicking the disconnect button
	 * @throws InterruptedException if an interruption occurs while stopping the threads
	 */



	@FXML
	void disconnect(ActionEvent event) throws InterruptedException {
	    // Clear the users table
	    mysqlConnection.deleteAllUsers();
	    
	    // Properly close the server
	    ServerUI.closeServer();
	    
	    // Update UI to reflect disconnected state
	    connectionCircle.setFill(javafx.scene.paint.Color.RED);
	    portTextField.setText("");
	    stopThreads(); // Ensure this method properly interrupts and joins any background threads
	    writeToConsole("Server has stopped listening for connections.");
	    numOfConnections.setText("0");
	}

	private void stopThreads() {
	    if (connectionUpdater != null && connectionUpdater.isAlive()) {
	        connectionUpdater.interrupt();
	    }
	    if (waitingListDeleter != null && waitingListDeleter.isAlive()) {
	        waitingListDeleter.interrupt();
	    }
	    if (unconfirmedOrderDeleter != null && unconfirmedOrderDeleter.isAlive()) {
	        unconfirmedOrderDeleter.interrupt();
	    }
	    if (oneDayBeforeNotifier != null && oneDayBeforeNotifier.isAlive()) {
	        oneDayBeforeNotifier.interrupt();
	    }
	}



	/**
	 * This method adds a new connection to the server.
	 * It increments the number of connections and updates the UI accordingly.
	 */
	public synchronized void addConnection() {
		numOfConnections.setText(String.valueOf(Integer.parseInt(numOfConnections.getText()) + 1));
		connectedClientsTable.setItems(FXCollections.observableArrayList(EchoServer.clientTypes));
		connectedClientsTable.refresh();
	}

	/**
	 * Initializes the controller after its root element has been completely processed.
	 * This method is automatically called after the FXML file has been loaded.
	 * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
	 * @param resources The resources used to localize the root object, or null if the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		connectionList = EchoServer.clientTypes;
		ipAddressColumn.setCellValueFactory(new PropertyValueFactory<Connection, String>("ipAddress"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<Connection, String>("hostName"));
		roleColumn.setCellValueFactory(new PropertyValueFactory<Connection, String>("role"));
		connectedClientsTable.setItems(FXCollections.observableArrayList(connectionList));
		ConsoleTexter = new StringBuilder();
	}

	/**
	 * Removes disconnected clients from the server's UI and updates the displayed number of connections.
	 * This method is synchronized to ensure thread safety when accessing shared resources.
	 * It retrieves the number of disconnected clients from the server and updates the count displayed on the UI.
	 */
	public synchronized void removeConnection() {
	    int numDisconnected = ServerUI.sv.checkDisconnectedClients();
	    updateConnectionCount(-numDisconnected);
	}

	public void refresh() {
	    connectedClientsTable.setItems(FXCollections.observableArrayList(EchoServer.clientTypes));
	    connectedClientsTable.refresh();
	}

	public synchronized void writeToConsole(String message) {
	    appendToConsole(message + "\n");
	    limitConsoleTextSize();
	}

	private void updateConnectionCount(int delta) {
	    int currentCount = Integer.parseInt(numOfConnections.getText());
	    numOfConnections.setText(String.valueOf(currentCount + delta));
	}

	private void appendToConsole(String message) {
	    ConsoleTexter.append(message);
	    Platform.runLater(() -> Console.setText(ConsoleTexter.toString()));
	}

	private void limitConsoleTextSize() {
	    final int MAX_TEXT_SIZE = 10000;
	    if (ConsoleTexter.length() > MAX_TEXT_SIZE) {
	        ConsoleTexter.delete(0, ConsoleTexter.length() - MAX_TEXT_SIZE);
	    }
	}

	@FXML
	void handleImportDataBtnAction(ActionEvent event) {
	    FileChooser fileChooser = new FileChooser();
	    // Set extension filter, if you want to filter for specific file types
	    FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (.csv)", ".csv");
	    fileChooser.getExtensionFilters().add(extFilter);

	    // Set the title for the FileChooser dialog
	    fileChooser.setTitle("Open CSV File");

	    // Show open file dialog to choose the file
	    File file = fileChooser.showOpenDialog(null);

	    // Proceed only if a file was selected
	    if (file != null) {
	        String filePath = file.getAbsolutePath();
	        String tableName = "users";

	        // Call the method to insert data from the specified file path
	        // This assumes mysqlConnection is a class with a static method insertDataFromFilePath
	        mysqlConnection.insertDataFromFilePath(tableName, filePath);
	        writeToConsole("Data import initiated for the file: " + filePath);
	    } else {
	        writeToConsole("File selection cancelled.");
	    }
	}




}