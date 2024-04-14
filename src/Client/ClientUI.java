// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package Client;
import goNatureBoundry.GoNature;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/**
 * It provides functionality to start the client application, load the connection
 * setup form, and manage the primary stage.
 *
 * This class extends javafx.application.Application and implements the start() method to
 * initialize the client's GUI.
 *
 * It also contains the main method to launch the JavaFX application and the stop method to
 * handle application shutdown.
 * @author Group7
 */
public class ClientUI extends Application {
    public static ClientController chat;
    public static GoNature clientCC;

    /**
     * The start method initializes the client first GUI
     * by loading the connect GUI.
     *
     * @param primaryStage The primary stage for the JavaFX application.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/goNatureBoundry/Connect.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Connect to Server");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    /**
     * The stop method is invoked when the JavaFX application is stopped.
     * It terminates the application by calling System.exit(0).
     */
    @Override
    public void stop() {
        System.exit(0);
    }
}